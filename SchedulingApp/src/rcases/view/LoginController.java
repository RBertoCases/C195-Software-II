package rcases.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorMessage;
    
    @FXML
    void handleSignInAction(ActionEvent event) {
    /**template code to be altered to JavaFX
        if(jTextField1.getText().length()==0)  // Checking for empty field
            JOptionPane.showMessageDialog(null, "Empty fields detected ! Please fill up all fields");
        else if(jPasswordField1.getPassword().length==0)  // Checking for empty field
            JOptionPane.showMessageDialog(null, "Empty fields detected ! Please fill up all fields");
        else{
            String user = jTextField1.getText();   // Collecting the input
            char[] pass = jPasswordField1.getPassword(); // Collecting the input
            String pwd = String.copyValueOf(pass);  // converting from array to string
            if(validate_login(user,pwd))
                JOptionPane.showMessageDialog(null, "Correct Login Credentials");        
            else
                JOptionPane.showMessageDialog(null, "Incorrect Login Credentials");
            }
        */
    }
    
    @FXML
    void handleCancelAction(ActionEvent event) {
        System.exit(0);
    }

}
