/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.DAO;

import java.util.List;
import rcases.model.Customer;

/**
 *
 * @author rcases
 */
public interface CustomerDAO {
    
    public void insert(Customer Customer);
    public List<Customer> select();
    
}
