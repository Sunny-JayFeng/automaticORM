package util;

import sqlsession.SQLTable;
import sqlsession.SQLColumn;

import java.lang.reflect.Field;
import domain.JX3;

@SuppressWarnings("all")
public class SetSQLSentence {


    public static void main(String[] args){
//        String insert = setInsertSQLSentence(JX3.class,"插入数据的值1", "插入数据的值2", "插入数据的值3");
//        String delete = setDeleteSQLSentence(JX3.class, "WHERE带条件删除");
//        String update = setUpdateSQLSentence(JX3.class, "WHERE 带条件", "修改的值D", "修改的值E", "修改的值F");
//        String select = setSelectSQLSentence(JX3.class, "WHERE带条件查询");
//
//        System.out.println(insert);
//        System.out.println();
//
//        System.out.println(delete);
//        System.out.println();
//
//        System.out.println(update);
//        System.out.println();
//
//        System.out.println(select);
//        System.out.println();
    }

    // 得到表名
    private static String getTableName(Class tableClass){
        return ((SQLTable)tableClass.getAnnotation(SQLTable.class)).tableName();
    }

    public static String setInsertSQLSentence(Class tableClass, Object[] args){
        StringBuilder insertSQLSentence = new StringBuilder("INSERT INTO ");
        String tableName = getTableName(tableClass); // 获取表名
        insertSQLSentence.append(tableName);
        insertSQLSentence.append("(");
        Field[] fields = tableClass.getDeclaredFields(); // 获取所有的字段
        for(Field field : fields){ // 增强for循环，得到所有的列名
            SQLColumn sqlColumnAnnotation = field.getAnnotation(SQLColumn.class);
            insertSQLSentence.append(sqlColumnAnnotation.columnName());
            insertSQLSentence.append(",");
        }
        insertSQLSentence.replace(insertSQLSentence.length() - 1,insertSQLSentence.length(),")");
        insertSQLSentence.append(" VALUES(");
        for(int i = 0; i < fields.length; i ++){
            insertSQLSentence.append(args[i]);
            insertSQLSentence.append(",");
        }
        insertSQLSentence.replace(insertSQLSentence.length() - 1,insertSQLSentence.length(),");");
        return insertSQLSentence.toString();
    }

    public static String setDeleteSQLSentence(Class tableClass){
        StringBuilder deleteSQLSentence = new StringBuilder("DELETE FROM ");
        String tableName = getTableName(tableClass); // 获取表名
        deleteSQLSentence.append(tableName);
//        deleteSQLSentence.append(" WHERE ");
//        Field[] fields = tableClass.getDeclaredFields();
//        for(Field field : fields){ // 找寻每个字段的keyType注解值
//            SQLColumn columnAnnotation = field.getAnnotation(SQLColumn.class);
//            if("PRIMARY KEY".equals(columnAnnotation.keyType())){ // 是主键
//                deleteSQLSentence.append(columnAnnotation.columnName());
//                deleteSQLSentence.append(" = ");
//                deleteSQLSentence.append(primaryKey);
//            }
//        }
        return deleteSQLSentence.append(";").toString();
    }

    public static String setUpdateSQLSentence(Class tableClass, Object[] args){
        StringBuilder updateSQLSentence = new StringBuilder("UPDATE ");
        String tableName = getTableName(tableClass); // 获取表名
        updateSQLSentence.append(tableName);
        updateSQLSentence.append(" SET ");
        Field[] fields = tableClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i ++){
            if ("PRIMARY KEY".equals(fields[i].getAnnotation(SQLColumn.class).keyType()) || "UNIQUE KEY".equals(fields[i].getAnnotation(SQLColumn.class).keyType())) continue;
            SQLColumn columnAnnotation = fields[i].getAnnotation(SQLColumn.class);
            String columnName = columnAnnotation.columnName();
            updateSQLSentence.append(columnName);
            updateSQLSentence.append(" = ");
            updateSQLSentence.append(args[i]);
            updateSQLSentence.append(", ");
        }
        updateSQLSentence.replace(updateSQLSentence.length() - 2,updateSQLSentence.length(), ";");
        return updateSQLSentence.toString();
    }

    public static String setSelectSQLSentence(Class tableClass){
        StringBuilder selectSQLSentence = new StringBuilder("SELECT ");
        String tableName = getTableName(tableClass);
        Field[] fields = tableClass.getDeclaredFields();
        for(Field field : fields){
            SQLColumn columnAnnotation = field.getAnnotation(SQLColumn.class);
            selectSQLSentence.append(" ");
            selectSQLSentence.append(columnAnnotation.columnName());
            selectSQLSentence.append(",");
        }
        selectSQLSentence.replace(selectSQLSentence.length() - 1, selectSQLSentence.length(), "");
        selectSQLSentence.append(" FROM ");
        selectSQLSentence.append(tableName);
        return selectSQLSentence.append(";").toString();
    }

    // 带条件的查询
    public static String setSelectSQLSentence(Class tableClass, String prerequisite){
        StringBuilder sqlSentence = new StringBuilder(setSelectSQLSentence(tableClass));
        int length = sqlSentence.length();
        sqlSentence.replace(length - 1, length, " ");
        sqlSentence.append(prerequisite);
        return sqlSentence.toString();
    }

    // 带条件的修改和删除
    // delete And update SQL
    public static String setDeleteAndUpdateSQLSentence(Class tableClass, String prerequisite, Object[]...args) {
        StringBuilder sqlSentence = null;
        if (args.length == 0) sqlSentence = new StringBuilder(setDeleteSQLSentence(tableClass));
        else sqlSentence = new StringBuilder(setUpdateSQLSentence(tableClass, args[0]));
        int length = sqlSentence.length();
        sqlSentence.replace(length - 1, length, " ");
        sqlSentence.append(prerequisite);
        return sqlSentence.toString();
    }
}
