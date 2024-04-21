package org.example.aplicatie.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class UtilsDB {
    private Properties dbProps;

    private Connection instance=null;

    public UtilsDB(Properties props){
        dbProps=props;
    }

    private Connection getNewConnection(){

        String url=dbProps.getProperty("jdbc.url");
        String user=dbProps.getProperty("jdbc.user");
        String pass=dbProps.getProperty("jdbc.pass");
        Connection con=null;
        try {
            if (user!=null && pass!=null)
                con= java.sql.DriverManager.getConnection(url,user,pass);
            else
                con= java.sql.DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return instance;
    }

}
