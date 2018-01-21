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

    }
    
    @FXML
    void handleCancelAction(ActionEvent event) {
        System.exit(0);
    }

}
