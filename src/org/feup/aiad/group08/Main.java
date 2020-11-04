package org.feup.aiad.group08;

import org.feup.aiad.group08.agents.AdvertiserAgent;
import org.feup.aiad.group08.agents.ManagerAgent;
import org.feup.aiad.group08.agents.StoreAgent;
import org.feup.aiad.group08.agents.WarehouseAgent;
import org.feup.aiad.group08.definitions.StoreType;
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

        createWarehouse();
        // Wait for warehouse to register in the DF
        Utils.sleep(500);

        createStores();
        // Wait for stores to register in the DF
        Utils.sleep(500);

        // Wait for advertiser to register in the DF
        createAdvertiser();
        Utils.sleep(500);

        createManager();
    }

    private static void createWarehouse() throws StaleProxyException {
        container.acceptNewAgent("warehouse", new WarehouseAgent()).start();
    }

    private static void createStores() throws StaleProxyException {
        container.acceptNewAgent("store1", new StoreAgent(StoreType.BOOKS)).start();
        container.acceptNewAgent("store2", new StoreAgent(StoreType.TECH)).start();
        container.acceptNewAgent("store3", new StoreAgent(StoreType.CLOTHES)).start();
        container.acceptNewAgent("store4", new StoreAgent(StoreType.FOOD)).start();
        container.acceptNewAgent("store5", new StoreAgent(StoreType.FURNITURE)).start();
        container.acceptNewAgent("store6", new StoreAgent(StoreType.GAMES)).start();
        container.acceptNewAgent("store7", new StoreAgent(StoreType.BOOKS)).start();
        container.acceptNewAgent("store8", new StoreAgent(StoreType.TECH)).start();
        container.acceptNewAgent("store9", new StoreAgent(StoreType.CLOTHES)).start();
    }
    
    private static void createManager() throws StaleProxyException {
        container.acceptNewAgent("manager", new ManagerAgent(5, 3)).start();
    }

    private static void createAdvertiser() throws StaleProxyException {
        container.acceptNewAgent("advertiser", new AdvertiserAgent()).start();
    }
}
