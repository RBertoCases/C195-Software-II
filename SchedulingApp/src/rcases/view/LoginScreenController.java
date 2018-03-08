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
import rcases.DBConnection;
import rcases.SchedulingApp;
import rcases.model.User;

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
        String user = usernameField.getText();   // Collecting the input
        String pass = passwordField.getText(); // Collecting the input
        
        if(user.length()==0 || pass.length()==0)  // Checking for empty field
            errorMessage.setText("Username or Password is empty. Please fill up both fields");
        else{
            
            User validateLogin = validateLogin(user,pass); //validateLogin(user,pass)
            if (validateLogin == null) {
                errorMessage.setText("Incorrect Login Credentials");
                return;
            }
            mainApp.showMenu(validateLogin);
            mainApp.showAppointmentScreen();
            //else
            //errorMessage.setText("Incorrect Login Credentials");
        }
    }
        
    User validateLogin(String username,String password) {
        User user = new User();
        
        try{           
            PreparedStatement pst = DBConnection.getConn().prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            pst.setString(1, username); 
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();                        
            if(rs.next()){
                user.setUsername(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setUserID(rs.getInt("userId"));
            } else {
                return null;    
            }            
                
        } catch(SQLException e){
            e.printStackTrace();
        }       
        return user;
}
    
    @FXML
    void handleCancelAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    public void setLogin(SchedulingApp mainApp) {
	this.mainApp = mainApp;
        ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());
        titleText.setText(rb.getString("title"));
        usernameText.setText(rb.getString("username"));
        passwordText.setText(rb.getString("password"));
        signinText.setText(rb.getString("signin"));
        cancelText.setText(rb.getString("cancel"));
}

}
