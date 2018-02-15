/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import rcases.model.Customer;

/**
 *
 * @author rober
 */
public class JDBCCustomerDAO implements CustomerDAO {

    Connection connection = null;
 
    public Connection getConnection() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if(connection == null)
                connection = DriverManager.getConnection("\"jdbc:mysql://52.206.157.109:3306/U04Esb\", \"U04Esb\", \"53688216525\"");
 
        } catch (ClassNotFoundException | SQLException e) {
 
            e.printStackTrace();
             
        }
        return connection;
    }

    @Override
    public void insert(Customer Customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Customer> select() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void closeConnection(){
        try {
              if (connection != null) {
                  connection.close();
              }
            } catch (SQLException e) { 
                //do nothing
            }
    }
    
}
