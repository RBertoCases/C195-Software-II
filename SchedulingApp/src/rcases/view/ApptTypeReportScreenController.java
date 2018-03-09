/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
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
    private List<String> types;
    private SchedulingApp mainApp;

    public void setApptTypeReportScreen(SchedulingApp mainApp) {
        this.mainApp = mainApp;
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        // Convert it to a list and add it to our ObservableList of months.
        monthNames.addAll(Arrays.asList(months));

        // Assign the month names as categories for the horizontal axis.
        xAxis.setCategories(monthNames);
        
        // Count the number of people having their birthday in a specific month.
        /* int[] monthCounter = new int[12];
        for (String t : types) {
        int month = t.;
        monthCounter[month]++;
        }*/
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        // Create a XYChart.Data object for each month. Add it to the series.
        /*for (int i = 0; i < monthCounter.length; i++) {
        series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }*/

        barChart.getData().add(series);
    }
    
}
