package rcases.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import rcases.SchedulingApp;

public class CustomerScreenController {

    @FXML
    private TableView<?> CustomerTable;

    @FXML
    private TableColumn<?, ?> firstNameColumn;

    @FXML
    private TableColumn<?, ?> lastNameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private ChoiceBox cityChoiceBox;

    @FXML
    private TextField address2Field;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField countryField;
    
    private SchedulingApp mainApp;
    
    public CustomerScreenController() {
    }

    @FXML
    void handleDeleteCustomer(ActionEvent event) {

    }

    @FXML
    void handleEditCustomer(ActionEvent event) {

    }

    @FXML
    void handleNewCustomer(ActionEvent event) {

    }
    
    public void setCustomerScreen(SchedulingApp mainApp) {
	this.mainApp = mainApp;
        cityChoiceBox.getItems().addAll("Phoenix","New York","London");
        cityChoiceBox.setValue("-Please Select-");
    }

}
