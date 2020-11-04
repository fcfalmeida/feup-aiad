package org.feup.aiad.unit.testing;

import org.junit.jupiter.api.Test;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.feup.aiad.group08.agents.CustomerAgent;
import org.feup.aiad.group08.agents.ManagerAgent;
import org.feup.aiad.group08.agents.StoreAgent;
import org.feup.aiad.group08.agents.WarehouseAgent;
import org.feup.aiad.group08.definitions.StoreType;

public class CustomerUnitTests {

    private CustomerAgent customerAgent;
    private StoreAgent storeAgent;
    private WarehouseAgent warehouseAgent;
    private ManagerAgent managerAgent;

    static Runtime rt;
    static Profile p;
    static ContainerController container;

    public static void main(String[] args) throws StaleProxyException {

        rt = Runtime.instance();
        p = new ProfileImpl();
        container = rt.createAgentContainer(p);

        container.acceptNewAgent("WarehouseTest", new WarehouseAgent()).start();

        container.acceptNewAgent("StoreTest", new StoreAgent(StoreType.BOOKS)).start();

        container.acceptNewAgent("ManagerTest", new ManagerAgent(5)).start();
    }       
    
    @Test
    public void testGenerateInfluenceability(){
        //CustomerAgent customer = new CustomerAgent();
        //double influenceability = customer.getInfluenceability();         
        //assertTrue(influenceability >= 1 && influenceability <= 10, "Customer generateInfluenceability is not in the expected range.");
    }

    @Test
    public void testPurchaseItemBehaviour(){
        //CustomerAgent customer = new CustomerAgent();
    }
}
