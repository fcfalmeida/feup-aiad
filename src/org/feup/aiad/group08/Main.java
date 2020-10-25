package org.feup.aiad.group08;

import org.feup.aiad.group08.agents.ManagerAgent;
import org.feup.aiad.group08.agents.StoreAgent;
import org.feup.aiad.group08.utils.Utils;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {

    static Runtime rt;
    static Profile p;
    static ContainerController container;

    public static void main(String[] args) throws StaleProxyException {
        
        rt = Runtime.instance();
        p = new ProfileImpl();
        container = rt.createAgentContainer(p);

        createStores();

        // Wait for stores to register in the DF
        Utils.sleep(500);

        createManager();
    }

    private static void createStores() throws StaleProxyException {
        container.acceptNewAgent("store1", new StoreAgent()).start();
        container.acceptNewAgent("store2", new StoreAgent()).start();
        container.acceptNewAgent("store3", new StoreAgent()).start();
        container.acceptNewAgent("store4", new StoreAgent()).start();
        container.acceptNewAgent("store5", new StoreAgent()).start();
        container.acceptNewAgent("store6", new StoreAgent()).start();
        container.acceptNewAgent("store7", new StoreAgent()).start();
        container.acceptNewAgent("store8", new StoreAgent()).start();
        container.acceptNewAgent("store9", new StoreAgent()).start();
    }
    
    private static void createManager() throws StaleProxyException {
        container.acceptNewAgent("manager", new ManagerAgent(5)).start();
    }
}
