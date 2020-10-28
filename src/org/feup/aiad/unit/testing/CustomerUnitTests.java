package org.feup.aiad.unit.testing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.feup.aiad.group08.agents.CustomerAgent;

public class CustomerUnitTests {

    public static void main(String[] args) {        
        
        CustomerAgent customer = new CustomerAgent();
        double influenceability = customer.getInfluenceability(); 
        System.out.println(influenceability);    
    }       
        
    
    @Test
    public void testGenerateInfluenceability(){
        CustomerAgent customer = new CustomerAgent();
        double influenceability = customer.getInfluenceability();         
        assertTrue(influenceability >= 1 && influenceability <= 10, "Customer generateInfluenceability is not in the expected range.");
    }
}
