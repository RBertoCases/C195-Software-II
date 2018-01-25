package rcases.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorMessage;
    
    @FXML
    void handleSignInAction(ActionEvent event) {
    //template code to be altered to JavaFX
        if(usernameField.getText().length()==0)  // Checking for empty field
            errorMessage.setText("Empty fields detected ! Please fill up all fields");
        else if(passwordField.getText().length()==0)  // Checking for empty field
            errorMessage.setText("Empty fields detected ! Please fill up all fields");
        else{
            String user = usernameField.getText();   // Collecting the input
            String pass = passwordField.getText(); // Collecting the input
            
            if(validate_login(user,pass))
                errorMessage.setText("Correct Login Credentials");        
            else
                errorMessage.setText("Incorrect Login Credentials");
            }
        
    }
    
    private boolean validate_login(String username,String password) {
        try{           
            Class.forName("com.mysql.jdbc.Driver");  // MySQL database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109:3306/U04Esb", "U04Esb", "53688216525");     
            PreparedStatement pst = conn.prepareStatement("Select * from user where userName=? and password=?");
            pst.setString(1, username); 
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();                        
            if(rs.next())            
                return true;    
            else
                return false;            
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }       
}
    
    @FXML
    void handleCancelAction(ActionEvent event) {
        System.exit(0);
    }

}
