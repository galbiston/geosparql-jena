/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class Main {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args){
        System.out.println("Success");
        
        String testVariable = "Parsing";
        String testLocation = "Phase Three";
        
       LOGGER.info("Error: {} Location: {}", testVariable, testLocation);
       
       //Here some new stuff.
      
    }
    
}
