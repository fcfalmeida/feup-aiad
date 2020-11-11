package org.feup.aiad.group08;

import java.io.IOException;
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
import org.feup.aiad.group08.csvManager.CSVReader;
import org.feup.aiad.group08.csvManager.parsers.CustomerParser;
import org.feup.aiad.group08.csvManager.parsers.StoreParser;
import org.feup.aiad.group08.definitions.StockPurchaseConditions;
import org.feup.aiad.group08.definitions.StoreType;
import org.feup.aiad.group08.utils.Utils;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

//import jdk.internal.org.jline.reader.Parser;

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

        String fileToReadName = "./data/input/stores.csv";
        String delimiter = ";";
        StoreParser parser = new StoreParser();
        CSVReader csvreader = null;
        try {
            csvreader = new CSVReader<>(fileToReadName, delimiter, parser);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<List<String>> storesData = csvreader.getData();
        for (List<String> line : storesData) {
            StoreAgent store = parser.parseLine(line);
            container.acceptNewAgent(store.getStoreName(), store).start();
        }
    }

    private static void createCustomers() throws StaleProxyException {  
        
        String fileToReadName = "./data/input/customers.csv";
        String delimiter = ";";
        CustomerParser parser = new CustomerParser();
        CSVReader csvreader = null;
        try {
            csvreader = new CSVReader<>(fileToReadName, delimiter, parser);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<List<String>> customersData = csvreader.getData();
        for (List<String> line : customersData) {
            CustomerAgent customer = parser.parseLine(line);
            container.acceptNewAgent(customer.getCustomerName(), customer).start();
        }
    }
    
    private static void createManager() throws StaleProxyException {
        container.acceptNewAgent("manager", new ManagerAgent(5, 3)).start();
    }

    private static void createAdvertiser() throws StaleProxyException {
        container.acceptNewAgent("advertiser", new AdvertiserAgent()).start();
    }
}
