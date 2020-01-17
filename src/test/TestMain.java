package test;

import sqlsession.SQLTable;
import sqlsession.SQLColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class TestMain {

    public static void main(String[] args) throws NoSuchFieldException {
        TestA testA = new TestA();
        Field field = testA.getClass().getDeclaredField("userName");
        show(testA.getClass(), field);
    }
    public static void show(Class clazz, Field theField){
        System.out.println(clazz);
        System.out.println(theField.getName());
        SQLTable annotation =(SQLTable) clazz.getAnnotation(SQLTable.class);
        System.out.println(annotation.tableName());
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            System.out.println(field.getName());
        }
        for(Field field : fields){
            SQLColumn sqlColumnAnnotation = field.getAnnotation(SQLColumn.class);
            System.out.println(sqlColumnAnnotation.columnName() + ", " + sqlColumnAnnotation.columnType());
        }
    }
}

@SQLTable(tableName = "A")
class TestA{

    @SQLColumn(columnName = "user_name", columnType = String.class)
    private String userName;

    @SQLColumn(columnName = "password", columnType = String.class)
    private String password;

    @SQLColumn(columnName = "phone", columnType = String.class)
    private String phone;
}
