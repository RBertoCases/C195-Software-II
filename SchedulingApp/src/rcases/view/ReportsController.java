/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import rcases.SchedulingApp;

/**
 * FXML Controller class
 *
 * @author rcases
 */
public class ReportsController {
    
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab schedTab;

    @FXML
    private TableView<?> schedTableView;

    @FXML
    private TableColumn<?, ?> consultantColumn;

    @FXML
    private Tab apptTab;

    @FXML
    private TreeTableColumn<?, ?> monthColumn;

    @FXML
    private TreeTableColumn<?, ?> typeColumn;

    @FXML
    private TreeTableColumn<?, ?> typeAmount;

    @FXML
    private Tab custTab;

    @FXML
    private TreeTableView<?> custTreeView;

    @FXML
    private TreeTableColumn<?, ?> locationColumn;

    @FXML
    private TreeTableColumn<?, ?> custColumn;

    @FXML
    private TreeTableColumn<?, ?> custAmountColumn;
    
    private SchedulingApp mainApp;
    
    public ReportsController() {
        
    }
    
    public void setReports(SchedulingApp mainApp) {
        this.mainApp = mainApp;
        tabPane.getSelectionModel().select(apptTab);
        monthColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("Month"));
        typeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("Type"));
        typeAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("Amount"));
    }

    
}
