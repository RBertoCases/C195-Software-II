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
public class Customer {
    
    private String customerId;
    private String customerName;
    private int active;
    private String address;
    private String zip;
    
    public Customer(){
        
    }

    public Customer(String customerId, String customerName, String address, String zip) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.zip = zip;
       
    }
    
    public Customer(String customerId, String customerName, int active) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.active = active;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
    
    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }
    
}
