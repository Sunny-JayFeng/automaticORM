package domain;

import sqlsession.SQLTable;
import sqlsession.SQLColumn;

@SQLTable(tableName = "jx3")
public class JX3 {

    @SQLColumn(columnName = "user_name", columnType = String.class, keyType = "PRIMARY KEY")
    private String userName;

    @SQLColumn(columnName = "password", columnType = String.class)
    private String password;

    @SQLColumn(columnName = "phone", columnType = String.class, keyType = "UNIQUE KEY")
    private String phone;

    public JX3(){}
    public JX3(String userName, String password, String phone){
        this.userName = userName;
        this.password = password;
        this.phone = phone;
    }

    public String getUserName(){
        return this.userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String toString(){
        StringBuilder result = new StringBuilder("[");
        result.append(this.userName);
        result.append(",");
        result.append(this.password);
        result.append(",");
        result.append(this.phone);
        result.append("]");
        return result.toString();
    }
}
