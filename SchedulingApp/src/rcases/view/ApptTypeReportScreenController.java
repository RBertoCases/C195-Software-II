/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import rcases.DBConnection;
import rcases.SchedulingApp;

/**
 * FXML Controller class
 *
 * @author rober
 */
public class ApptTypeReportScreenController {
    
    @FXML
    private StackedBarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    private final ObservableList<String> monthNames = FXCollections.observableArrayList();
    private final ObservableList<String> typeCount = FXCollections.observableArrayList();
    private List<String> types;
    private SchedulingApp mainApp;

    public void setApptTypeReportScreen(SchedulingApp mainApp) {
        this.mainApp = mainApp;
        
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        // Convert it to a list and add it to our ObservableList of months.
        monthNames.addAll(Arrays.asList(months));

        // Assign the month names as categories for the horizontal axis.
        xAxis.setCategories(monthNames);
        
        Map<String, Double> locationMap = new HashMap<>();

            try { PreparedStatement pst = DBConnection.getConn().prepareStatement(
                "SELECT month(`start`), description " +
                "FROM appointment " +
                "order by month(`start`)"); 
                ResultSet rs = pst.executeQuery();


                while (rs.next()) {
                    String typeName = rs.getString("description");
                    Double monthType = rs.getDouble("month(`start`)");
                    
                    locationMap.put(typeName, monthType);
                }

            } catch (SQLException sqe) {
                System.out.println("Check your SQL");
                sqe.printStackTrace();
            } catch (Exception e) {
                System.out.println("Something besides the SQL went wrong.");
                e.printStackTrace();
            }     
        
       // Count the number of people having their birthday in a specific month.
        int[] monthCounter = new int[12];
        //for ( Map.Entry<Integer, String> e : locationMap.iterator().entrySet() ) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        locationMap.forEach((key, value) -> series.getData().add(new XYChart.Data(key, value)));   
        System.out.println(series);
        
        //XYChart.Series<String, Integer> series = new XYChart.Series<>();

        // Create a XYChart.Data object for each month. Add it to the series.
        for (int i = 0; i < monthCounter.length; i++) {
        series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }

        barChart.getData().add(series);
    }
    
}
