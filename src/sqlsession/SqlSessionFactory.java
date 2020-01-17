package sqlsession;

import util.ConnectionPool;
import util.SetSQLSentence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlSessionFactory {


    public long insert(Class tableClass, Object[] args) throws SQLException {
        String sqlSentence = SetSQLSentence.setInsertSQLSentence(tableClass, args);
        Connection conn = ConnectionPool.getConnection();
        PreparedStatement pstat = conn.prepareStatement(sqlSentence, Statement.RETURN_GENERATED_KEYS);
        pstat.executeUpdate();
        ResultSet rs = pstat.getGeneratedKeys();
        try{
            if(rs.next()) return rs.getLong(1);
        } finally {
            if(rs != null) rs.close();
            if(pstat != null) pstat.close();
            if(conn != null) conn.close();
        }
        return -1;
    }

    public int update(Class tableClass, Object...args) throws SQLException {
        String sqlSentence = "";
        if (args.length == 0) sqlSentence = SetSQLSentence.setDeleteSQLSentence(tableClass);
        else sqlSentence = SetSQLSentence.setUpdateSQLSentence(tableClass, args);
        return updateMethod(sqlSentence);
    }

    public int update(Class tableClass, String prerequisite, Object...args) throws SQLException {
        String sqlSentence = "";
        if (args.length == 0) sqlSentence = SetSQLSentence.setDeleteAndUpdateSQLSentence(tableClass, prerequisite);
        else sqlSentence = SetSQLSentence.setDeleteAndUpdateSQLSentence(tableClass, prerequisite, args);
        return updateMethod(sqlSentence);
    }

    private int updateMethod(String sqlSentence) throws SQLException {
        Connection conn = ConnectionPool.getConnection();
        PreparedStatement pstat = conn.prepareStatement(sqlSentence);
        pstat.executeUpdate();
        try{
            return pstat.executeUpdate();
        }finally {
            if(pstat != null) pstat.close();
            if(conn != null) conn.close();
        }
    }

    public ArrayList select(Class tableClass) throws SQLException, IllegalAccessException, InstantiationException {
        String sqlSentence = SetSQLSentence.setSelectSQLSentence(tableClass);
        return selectMethod(tableClass, sqlSentence);
    }

    public ArrayList select(Class tableClass, String prerequisite) throws SQLException, IllegalAccessException, InstantiationException {
        String sqlSentence = SetSQLSentence.setSelectSQLSentence(tableClass, prerequisite);
        return selectMethod(tableClass, sqlSentence);
    }

    private static ArrayList selectMethod(Class tableClass, String sqlSentence) throws SQLException, InstantiationException, IllegalAccessException {
        ArrayList list = new ArrayList();
        System.out.println(sqlSentence);
        Connection conn = ConnectionPool.getConnection();
        PreparedStatement pstat = conn.prepareStatement(sqlSentence);
        ResultSet rs = pstat.executeQuery();
        while(rs.next()) {
            list.add(createAObject(tableClass, rs));
        }
        try{
        }finally {
            if(pstat != null) pstat.close();
            if(conn != null) conn.close();
        }
        return list;
    }

    // 创建一个对象
    private static Object createAObject(Class tableClass, ResultSet rs) throws IllegalAccessException, InstantiationException, SQLException {
        Object theObject = tableClass.newInstance();
        setAttribute(theObject, rs);
        return theObject;
    }
    // 设置对象的属性值
    private static void setAttribute(Object theObject, ResultSet rs) throws SQLException, IllegalAccessException {
        Class clazz = theObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String columnName = field.getAnnotation(SQLColumn.class).columnName();
            field.set(theObject, rs.getObject(columnName));
        }
    }
}
