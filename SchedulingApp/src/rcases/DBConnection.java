/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author rcases
 */
public class DBConnection {
    /* If I made this static, then I would not need to create an object in the main(..) */
    private static Connection connDB;
    
    public DBConnection(){}
    /* If the Connection variable turns static, so will this method */
    public static void init(){
        System.out.println("Connecting to the database");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            /* Replace the X's with the information for your database instance */
            connDB = DriverManager.getConnection("jdbc:mysql://52.206.157.109:3306/U04Esb", "U04Esb", "53688216525");;
        }catch (ClassNotFoundException ce){
            System.out.println("Cannot find the right class.  Did you remember to add the mysql library to your Run Configuration?");
            ce.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
}
    }
    
    public static Connection getConn(){
    
        return connDB;
    }
    
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
