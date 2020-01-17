package service;

import dao.JX3Dao;
import java.sql.SQLException;
import java.util.ArrayList;

public class JX3Service {

    private JX3Dao jx3Dao = JX3Dao.getInstance();
    public long insert(Object[] args) {
        try {
            return jx3Dao.insert(args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int delete() {
        try {
            return jx3Dao.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int delete(String prerequisite) {
        try {
            return jx3Dao.delete(prerequisite);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int update(Object[] args) {
        try {
            return jx3Dao.update(args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int update(String prerequisite, Object[] args) {
        try {
            return jx3Dao.update(prerequisite, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList select() {
        try {
            return jx3Dao.select();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList select(String prerequisite) {
        try {
            return jx3Dao.select(prerequisite);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
