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
    
    /* -------------------------------------------------- 
    GeoSPARQL Name Spaces
    ogc: http://www.opengis.net/
    geo: http://www.opengis.net/ont/geosparql#
    geof: http://www.opengis.net/def/function/geosparql/
    geor: http://www.opengis.net/def/rule/geosparql/
    sf: http://www.opengis.net/ont/sf#
    gml: http://www.opengis.net/ont/gml#
    my: http://example.org/ApplicationSchema#
    xsd: http://www.w3.org/2001/XMLSchema#
    rdf: http://www.w3.org/1999/02/22-rdf-syntax-ns#
    rdfs: http://www.w3.org/2000/01/rdf-schema#
    owl: http://www.w3.org/2002/07/owl#
    -------------------------------------------------- */ 
    
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args){
        System.out.println("Success");
        
        String testVariable = "Parsing";
        String testLocation = "Phase Three";
        
       LOGGER.info("Error: {} Location: {}", testVariable, testLocation);
       
       //Here some new stuff.
      
    }
    
    public String getFullName(String firstName, String secondName){
        
        return firstName + " " + secondName;
    }
    
    public String getUserName(String firstName, String secondName){
        
        return firstName + secondName;
    }
}
