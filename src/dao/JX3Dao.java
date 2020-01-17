package dao;

import domain.JX3;
import sqlsession.SqlSessionFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class JX3Dao {

    private static SqlSessionFactory sqlSessionFactory = new SqlSessionFactory();
    private static volatile JX3Dao jx3Dao;
    public static JX3Dao getInstance(){
        if(jx3Dao == null){
            synchronized(JX3Dao.class){
                if(jx3Dao == null) jx3Dao = new JX3Dao();
            }
        }
        return jx3Dao;
    }

    public long insert(Object[] args) throws SQLException {
        return sqlSessionFactory.insert(JX3.class, args);
    }

    public int delete() throws SQLException {
        return sqlSessionFactory.update(JX3.class);
    }
    public int delete(String prerequisite) throws SQLException {
        return sqlSessionFactory.update(JX3.class, prerequisite);
    }

    public int update(Object...args) throws SQLException {
        return sqlSessionFactory.update(JX3.class,args);
    }
    public int update(String prerequisite, Object...args) throws SQLException {
        return sqlSessionFactory.update(JX3.class, prerequisite, args);
    }

    public ArrayList select() throws SQLException {
        try {
            return sqlSessionFactory.select(JX3.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList select(String prerequisite) throws SQLException {
        try {
            return sqlSessionFactory.select(JX3.class, prerequisite);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
