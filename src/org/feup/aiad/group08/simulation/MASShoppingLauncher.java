package org.feup.aiad.group08.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.awt.Color;

import org.feup.aiad.group08.agents.AdvertiserAgent;
import org.feup.aiad.group08.agents.CustomerAgent;
import org.feup.aiad.group08.agents.ManagerAgent;
import org.feup.aiad.group08.agents.StoreAgent;
import org.feup.aiad.group08.agents.WarehouseAgent;
import org.feup.aiad.group08.csvManager.CSVReader;
import org.feup.aiad.group08.csvManager.parsers.CustomerParser;
import org.feup.aiad.group08.csvManager.parsers.StoreParser;
import org.feup.aiad.group08.definitions.StockPurchaseConditions;
import org.feup.aiad.group08.utils.Utils;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.space.Object2DGrid;


public class MASShoppingLauncher extends Repast3Launcher {

    private Runtime rt;
    private Profile p;
    private ContainerController container;
    private int numCustomers;
    private int numStores;

    private List<DefaultDrawableNode> nodes = new ArrayList<>();

    @Override
    public String[] getInitParam() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "MAS Shopping";
    }

    @Override
    protected void launchJADE() {
        rt = Runtime.instance();
        p = new ProfileImpl();
        container = rt.createMainContainer(p);

        try {
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
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private void createWarehouse() throws StaleProxyException {
        SortedMap<Integer, Float> quantityDiscountModel = new TreeMap<>();
        quantityDiscountModel.put(10, 0.05f);
        quantityDiscountModel.put(25, 0.08f);
        quantityDiscountModel.put(50, 0.1f);
        quantityDiscountModel.put(100, 0.15f);
        StockPurchaseConditions spc = new StockPurchaseConditions(20, quantityDiscountModel);
        container.acceptNewAgent("warehouse", new WarehouseAgent(spc)).start();
    }

    private void createStores() throws StaleProxyException {

        String fileToReadName = "./data/input/stores_sumItems6.csv";
        String delimiter = ";";
        StoreParser parser = new StoreParser();
        CSVReader csvreader = null;
        try {
            csvreader = new CSVReader<>(fileToReadName, delimiter, parser);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<List<String>> storesData = csvreader.getData();
        for (List<String> line : storesData) {
            StoreAgent store = parser.parseLine(line);
            
            DefaultDrawableNode node = 
						generateNode(store.getName(), Color.GREEN,
								new Random().nextInt(100), new Random().nextInt(100));
            
            nodes.add(node);

            container.acceptNewAgent(store.getStoreName(), store).start();
        }

        numStores = storesData.size();
    }

    private void createCustomers() throws StaleProxyException {  
        
        String fileToReadName = "./data/input/customers_high_balance.csv";
        String delimiter = ";";
        CustomerParser parser = new CustomerParser();
        CSVReader csvreader = null;
        try {
            csvreader = new CSVReader<>(fileToReadName, delimiter, parser);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<List<String>> customersData = csvreader.getData();
        for (List<String> line : customersData) {
            CustomerAgent customer = parser.parseLine(line);
            
            DefaultDrawableNode node = 
						generateNode(customer.getName(), Color.WHITE,
								new Random().nextInt(100), new Random().nextInt(100));
            
            nodes.add(node);

            container.acceptNewAgent(customer.getCustomerName(), customer).start();

        }

        numCustomers = customersData.size();
    }
    
    private void createManager() throws StaleProxyException {
        container.acceptNewAgent("manager", new ManagerAgent(5, 3, numStores, numCustomers)).start();
    }

    private void createAdvertiser() throws StaleProxyException {
        container.acceptNewAgent("advertiser", new AdvertiserAgent()).start();
    }

    @Override
    public void begin() {
        super.begin();

        DisplaySurface dsurf = new DisplaySurface(this, "MAS Shopping");
        registerDisplaySurface("MAS Shopping", dsurf);

        Network2DDisplay display = new Network2DDisplay(nodes, 100, 100);

        dsurf.addDisplayableProbeable(display, "Agents");
        dsurf.addZoomable(display);
        addSimEventListener(dsurf);

        dsurf.display();

        getSchedule().scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
    }

    private DefaultDrawableNode generateNode(String label, Color color, int x, int y) {
        OvalNetworkItem oval = new OvalNetworkItem(x,y);
        oval.allowResizing(false);
        oval.setHeight(5);
        oval.setWidth(5);
        
		DefaultDrawableNode node = new DefaultDrawableNode(label, oval);
		node.setColor(color);
        
		return node;
	}
    
}
