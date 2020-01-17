package util;

import myexception.FreeConnectionException;
import readproperties.ReadConfig;

import java.sql.Connection;

public class ConnectionPool {

    private static final byte BUSY_VALUE = 1;
    private static final byte FREE_VALUE = 0;
    private static final byte NULL_VALUE = -1;
    private static Connection[] connectionsList = new MyConnection[ReadConfig.getInstance().getConnectionCount("minConnectionSize","3")];
    private static byte[] connectionBitMap = new byte[ReadConfig.getInstance().getConnectionCount("minConnectionSize","3")];
    private static int total = 0;


    static{
        for(int i = 0; i < connectionBitMap.length; i ++){
            connectionBitMap[i] = -1;
        }
    }

    // 找寻连接的下标
    private static int getIndex(byte bit){
        for(int i = 0; i < connectionBitMap.length; i ++){
            if(connectionBitMap[i] == bit){
                return i;  // 找到了，返回下标
            }
        }
        return Integer.MAX_VALUE;  // 没找到，返回Integer最大值
    }

    // 找寻空闲连接的下标
    private static int getFreeIndex(){
        return getIndex(FREE_VALUE);
    }
    // 找寻空置连接的下标
    private static int getNullIndex(){
        return getIndex(NULL_VALUE);
    }

    // 分配连接
    private static synchronized Connection getDistribute(int index){
        if(connectionBitMap[index] == BUSY_VALUE) return null;
        if(connectionBitMap[index] == NULL_VALUE){ // 如果它是空置的，那创建一个连接
            connectionsList[index] = new MyConnection(index);
            total ++;
        }
        // 走到这里，无论如何，肯定是能返回一个连接的
        // 因为如果你是空闲的，那本来就有连接
        // 如果你是空置的，原本没有连接，走到这里的时候，也已经创建了一个连接了
        connectionBitMap[index] = BUSY_VALUE;
        return connectionsList[index];
    }

    // 扩容方法
    private static int grow(){
        Connection[] newConnectionsList = new MyConnection[connectionsList.length * 2];
        byte[] newConnectionBitMap = new byte[connectionBitMap.length * 2];
        System.arraycopy(connectionsList, 0, newConnectionsList, 0, connectionsList.length);
        System.arraycopy(connectionBitMap, 0, newConnectionBitMap, 0, connectionBitMap.length);
        // 新扩容的部分，连接全部置为空置
        for(int i = connectionBitMap.length; i < newConnectionBitMap.length; i ++){
            newConnectionBitMap[i] = -1;
        }
        connectionsList = newConnectionsList;
        connectionBitMap = newConnectionBitMap;
        return getNullIndex();
    }

    // 该方法用于获取一个连接
    public static Connection getConnection(){
        int freeIndex = getFreeIndex(); // 寻找空闲连接
        // 找到空闲的连接，直接通过分配连接方法，返回一个连接，至于返回的是不是null，不由我们处理
        if(freeIndex != Integer.MAX_VALUE) return getDistribute(freeIndex);

        // 能走到这里，肯定是没有空闲的连接
        int nullIndex = getNullIndex(); // 找寻空置的连接
        // 找到空置的连接，直接通过分配连接方法，返回一个连接，至于返回的是不是null，不由我们处理
        if(nullIndex != Integer.MAX_VALUE) return getDistribute(nullIndex);

        // 能走到这里，那肯定是没有空闲的连接，也没有空置的连接，那就看能不能扩容
        if(total < ReadConfig.getInstance().getConnectionCount("maxConnectionSize")) return getDistribute(grow()); // 能扩容，那就扩容并且利用空置连接
        return null; // 能走到这里，那就是没有空闲连接，没有空置连接，也不能扩容
    }

    // 该方法用于释放连接
    public static void freeConnection(Connection connection) throws FreeConnectionException {
        for(int index = 0; index < connectionsList.length; index ++){ // 找出要释放的连接的下标
            // 之所以要 connectionBitMap[index] == BUSY_VALUE
            // 是因为防止无故释放/重复释放，即如果不这么写，那空闲的也可以释放，出现二次释放的情况，虽然不会出错，但是不好
            if(connectionsList[index] == connection && connectionBitMap[index] == BUSY_VALUE){
                connectionBitMap[index] = FREE_VALUE;
                System.out.println("操作完成，释放连接成功");
                return;
            }
        }
        // 没找到，释放异常
        throw new FreeConnectionException("释放连接异常");
    }
}
