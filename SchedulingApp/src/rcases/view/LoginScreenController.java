package rcases.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import rcases.SchedulingApp;

public class LoginScreenController {
    
            
    @FXML
    private Label errorMessage;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text usernameText;

    @FXML
    private Text passwordText;

    @FXML
    private Text titleText;

    @FXML
    private Button signinText;

    @FXML
    private Button cancelText;
    
    // Reference to the main application.
    private SchedulingApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public LoginScreenController() {
    }
    
    @FXML
    void handleSignInAction(ActionEvent event) {
        if(usernameField.getText().length()==0)  // Checking for empty field
            errorMessage.setText("Username or Password is empty. Please fill up both fields");
        else if(passwordField.getText().length()==0)  // Checking for empty field
            errorMessage.setText("Username or Password is empty. Please fill up both fields");
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
        Platform.exit();
    }
    
    public void setLogin() {
        ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());
        titleText.setText(rb.getString("title"));
        usernameText.setText(rb.getString("username"));
        passwordText.setText(rb.getString("password"));
        signinText.setText(rb.getString("signin"));
        cancelText.setText(rb.getString("cancel"));
}

}
