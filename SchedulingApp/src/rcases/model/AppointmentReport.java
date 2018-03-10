/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.model;

/**
 *
 * @author rcases
 */
public class AppointmentReport {
    
    private String month;
    private String amount;
    private String type;

    

    /**
     *
     * @param month
     * @param type
     * @param amount
     */
    public AppointmentReport(String month, String type, String amount) {
        this.month = month;
        this.type = type;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
    return "Month: " + this.month +
            " Type: " + this.type +
            " Amt: " + this.amount + ".\n" ;
    }
    
    
}
