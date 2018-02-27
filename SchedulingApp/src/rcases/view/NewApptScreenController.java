/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rcases.DBConnection;
import rcases.model.Appointment;

/**
 * FXML Controller class
 *
 * @author rcases
 */
public class NewApptScreenController implements Initializable {

    @FXML
    private Label editApptLabel;

    @FXML
    private Label companyMachineLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField customerField;

    @FXML
    private TextField consultantField;

    @FXML
    private TextField startField;

    @FXML
    private TextField endField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button apptSaveButton;

    @FXML
    private Button apptCancelButton;


    private Stage dialogStage;
    private boolean okClicked = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleNewSave(ActionEvent event) {
        saveAppt();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dialogStage.close();
        } else {
            alert.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppointment(Appointment appointment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void saveAppt() {
        try {

                PreparedStatement pst = DBConnection.getConn().prepareStatement("INSERT INTO appointment "
                + "(customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
                pst.setString(1, "14");
                pst.setString(2, "test title");
                pst.setString(3, "test desc");
                pst.setString(4, "");
                pst.setString(5, "");
                pst.setString(6, "");
                pst.setString(7, LocalDateTime.now().toString());
                pst.setString(8, LocalDateTime.now().toString());
                pst.setString(9, LocalDateTime.now().toString());
                pst.setString(10, "test");
                pst.setString(11, LocalDateTime.now().toString());
                pst.setString(12, "test");
                int result = pst.executeUpdate();
                if (result == 1) {//one row was affected; namely the one that was inserted!
                    System.out.println("YAY! Customer");
                } else {
                    System.out.println("BOO! Customer");
                }
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
        
        //INSERT INTO U04Esb.appointment (customerId, title, description, location, contact, url, `start`, `end`, createDate, createdBy, lastUpdate, lastUpdateBy) 
	//VALUES (14, 'test14', 'test', ' ', '', ' ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', DEFAULT, 'test')
    }
    
}
