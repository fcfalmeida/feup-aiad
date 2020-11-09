package org.feup.aiad.group08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.feup.aiad.group08.agents.AdvertiserAgent;
import org.feup.aiad.group08.agents.CustomerAgent;
import org.feup.aiad.group08.agents.ManagerAgent;
import org.feup.aiad.group08.agents.StoreAgent;
import org.feup.aiad.group08.agents.WarehouseAgent;
import org.feup.aiad.group08.definitions.StockPurchaseConditions;
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

        createCustomers();
        // Wait for customers to register in the DF
        Utils.sleep(500);

        // Wait for advertiser to register in the DF
        createAdvertiser();
        Utils.sleep(500);

        createManager();
    }

    private static void createWarehouse() throws StaleProxyException {
        SortedMap<Integer, Float> quantityDiscountModel = new TreeMap<>();
        quantityDiscountModel.put(10, 0.05f);
        quantityDiscountModel.put(25, 0.08f);
        quantityDiscountModel.put(50, 0.1f);
        quantityDiscountModel.put(100, 0.15f);
        StockPurchaseConditions spc = new StockPurchaseConditions(20, quantityDiscountModel);
        container.acceptNewAgent("warehouse", new WarehouseAgent(spc)).start();
    }

    private static void createStores() throws StaleProxyException {
        container.acceptNewAgent("store1", new StoreAgent(StoreType.BOOKS, 100, 500, .15f, .4f)).start();
        container.acceptNewAgent("store2", new StoreAgent(StoreType.TECH, 100, 782, .2f, .4f)).start();
        container.acceptNewAgent("store3", new StoreAgent(StoreType.CLOTHES, 60, 333, .2f, .4f)).start();
        container.acceptNewAgent("store4", new StoreAgent(StoreType.FOOD, 40, 1000, .18f, .4f)).start();
        container.acceptNewAgent("store5", new StoreAgent(StoreType.FURNITURE, 110, 653, .22f, .45f)).start();
        container.acceptNewAgent("store6", new StoreAgent(StoreType.GAMES, 70, 1234, .25f, .45f)).start();
        container.acceptNewAgent("store7", new StoreAgent(StoreType.BOOKS, 89, 555, .2f, .45f)).start();
        container.acceptNewAgent("store8", new StoreAgent(StoreType.TECH, 121, 99, .17f, .45f)).start();
        container.acceptNewAgent("store9", new StoreAgent(StoreType.CLOTHES, 38, 234, .18f, .45f)).start();
    }

    private static void createCustomers() throws StaleProxyException {     
        List<StoreType> storePreferences1 = new ArrayList<>();
        storePreferences1.add(StoreType.BOOKS);
        storePreferences1.add(StoreType.GAMES);
        storePreferences1.add(StoreType.FOOD);

        List<StoreType> storePreferences2 = new ArrayList<>();
        storePreferences2.add(StoreType.FURNITURE);
        storePreferences2.add(StoreType.CLOTHES);
        storePreferences2.add(StoreType.TECH);

        container.acceptNewAgent("customer1", new CustomerAgent(100, storePreferences1)).start();
        container.acceptNewAgent("customer2", new CustomerAgent(200, storePreferences1)).start();
        container.acceptNewAgent("customer3", new CustomerAgent(10, storePreferences2)).start();
        container.acceptNewAgent("customer4", new CustomerAgent(50, storePreferences2)).start();
        container.acceptNewAgent("customer5", new CustomerAgent(20, storePreferences1)).start();;
    }
    
    private static void createManager() throws StaleProxyException {
        container.acceptNewAgent("manager", new ManagerAgent(5, 3)).start();
    }

    private static void createAdvertiser() throws StaleProxyException {
        container.acceptNewAgent("advertiser", new AdvertiserAgent()).start();
    }
}
