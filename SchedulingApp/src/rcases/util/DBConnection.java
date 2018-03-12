/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author rcases
 */
public class DBConnection {
    
    private static Connection connDB;
    
    public DBConnection(){}
    
    /**
     * Opens connection to database based on following info:
     * Server name: 52.206.157.109
     * Database name: U04Esb
     * Username: U04Esb
     * Password: 53688216525
     */
    public static void init(){
        System.out.println("Connecting to the database");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            connDB = DriverManager.getConnection("jdbc:mysql://52.206.157.109:3306/U04Esb", "U04Esb", "53688216525");;
        }catch (ClassNotFoundException ce){
            System.out.println("Cannot find the right class.  Did you remember to add the mysql library to your Run Configuration?");
            ce.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
}
    }
    
    //Returns Connection
    public static Connection getConn(){
    
        return connDB;
    }
    
    //Closes connections
    public static void closeConn(){
        try{
            connDB.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Connection closed.");
        }
    }
    
}
