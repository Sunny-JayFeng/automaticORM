package readproperties;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class ReadConfig {

    private static volatile ReadConfig readConfig;
    private Properties properties;
    private String driver;
    private String url;
    private String user;
    private String password;

    public static ReadConfig getInstance(){
        if(readConfig == null){
            synchronized(ReadConfig.class){
                if(readConfig == null) readConfig = new ReadConfig();
            }
        }
        return readConfig;
    }
    {
        InputStream inputStream = null;
        try {
            properties = new Properties();
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("properties\\jdbcconfig.properties");
            properties.load(inputStream);
            loadMessage(); // 执行方法，加载配置文件中的信息
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    // 该方法用于加载配置文件中的信息
    private void loadMessage(){
        this.driver = properties.getProperty("driver");
        this.url = properties.getProperty("url");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
    }

    public String getDriver(){
        return this.properties.getProperty("driver");
    }

    public Connection getURLConnection() throws SQLException {
        this.url = properties.getProperty("url");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }

    public int getConnectionCount(String size){
        return Integer.parseInt(properties.getProperty(size));
    }

    public int getConnectionCount(String size, String defaultSize){
        return Integer.parseInt(properties.getProperty(size, defaultSize));
    }
}
