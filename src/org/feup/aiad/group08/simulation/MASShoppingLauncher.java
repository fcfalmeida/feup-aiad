package org.feup.aiad.group08.simulation;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.analysis.BinDataSource;
import uchicago.src.sim.analysis.OpenHistogram;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.gui.RectNetworkItem;
import uchicago.src.sim.network.DefaultDrawableNode;


public class MASShoppingLauncher extends Repast3Launcher {

    private static final int WIDTH = 1100;
    private static final int HEIGHT = 400;

    private static final int CUSTOMER_NODE_VERTICAL_MARGIN = 50;
    private static final int STORE_NODE_VERTICAL_MARGIN = 120;
    private static final int STORE_NODE_HORIZONTAL_MARGIN = 20;
    private static final int CUSTOMER_NODE_HORIZONTAL_MARGIN = 60;

    private static final Color CUSTOMER_NODE_COLOR = Color.WHITE;
    private static final Color CUSTOMER_LABEL_COLOR = Color.BLACK;
    private static final Color STORE_NODE_COLOR = Color.MAGENTA;
    private static final Color STORE_LABEL_COLOR = Color.BLACK;
    
    private Runtime rt;
    private Profile p;
    private ContainerController container;
    private int numCustomers;
    private int numStores;

    private List<CustomerAgent> customers = new ArrayList<>();

    private static List<DefaultDrawableNode> nodes = new ArrayList<>();

    @Override
    public String[] getInitParam() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "MAS Shopping";
    }

    public static DefaultDrawableNode getNode(String label) {
		for(DefaultDrawableNode node : nodes) {
			if(node.getNodeLabel().equals(label)) {
				return node;
			}
		}
		return null;
	}

    @Override
    protected void launchJADE() {
        rt = Runtime.instance();
        p = new ProfileImpl();
        container = rt.createMainContainer(p);

        try {
            createWarehouse();
            createStores();
            createCustomers();
            createAdvertiser();
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
        for (int i = 0; i < storesData.size(); i++) {
            List<String> line = storesData.get(i);
            StoreAgent store = parser.parseLine(line);
            
            DefaultDrawableNode node = 
						generateStoreNode(store.getStoreName(), STORE_NODE_COLOR,
								(i + 1) * STORE_NODE_VERTICAL_MARGIN, STORE_NODE_HORIZONTAL_MARGIN);
            
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
        for (int i = 0; i < customersData.size(); i++){
            List<String> line = customersData.get(i);
            CustomerAgent customer = parser.parseLine(line);

            customers.add(customer);
            
            DefaultDrawableNode node = 
						generateCustomerNode(customer.getCustomerName(), CUSTOMER_NODE_COLOR,
                        (i + 1) * CUSTOMER_NODE_VERTICAL_MARGIN, HEIGHT - CUSTOMER_NODE_HORIZONTAL_MARGIN);
            
            nodes.add(node);
            customer.setNode(node);

            container.acceptNewAgent(customer.getCustomerName(), customer).start();

        }

        numCustomers = customersData.size();
    }
    
    private void createManager() throws StaleProxyException {
        container.acceptNewAgent("manager", new ManagerAgent(1, 3, numStores, numCustomers)).start();
    }

    private void createAdvertiser() throws StaleProxyException {
        container.acceptNewAgent("advertiser", new AdvertiserAgent()).start();
    }

    @Override
    public void begin() {
        super.begin();

        DisplaySurface surface = new DisplaySurface(this, "MAS Shopping");
        registerDisplaySurface("MAS Shopping", surface);

        Network2DDisplay display = createGraph(surface);

        surface.display();

        OpenHistogram plot = createHappinessHistogram();

        plot.display();

        getSchedule().scheduleActionAtInterval(1, surface, "updateDisplay", Schedule.LAST);
        getSchedule().scheduleActionAtInterval(1, plot, "step", Schedule.LAST);
    }

    private Network2DDisplay createGraph(DisplaySurface surface) {
        Network2DDisplay display = new Network2DDisplay(nodes, WIDTH, HEIGHT);

        surface.addDisplayableProbeable(display, "Agents");
        surface.addZoomable(display);
        addSimEventListener(surface);

        return display;
    }

    private OpenHistogram createHappinessHistogram() {
        OpenHistogram plot = new OpenHistogram("Customer Happiness", numCustomers, 0);

        BinDataSource source = new BinDataSource() {
            public double getBinValue(Object o) {
                CustomerAgent customer = (CustomerAgent) o;
                return customer.getHappiness();
            }
        };

        plot.createHistogramItem("Customer Happiness", customers, source);

        return plot;
    }

    private DefaultDrawableNode generateCustomerNode(String label, Color color, int x, int y) {
        OvalNetworkItem oval = new OvalNetworkItem(x,y);
        oval.allowResizing(false);
        oval.setHeight(40);
        oval.setWidth(40);

        oval.setLabelColor(CUSTOMER_LABEL_COLOR);

        DefaultDrawableNode node = new DefaultDrawableNode(label, oval);
        node.setColor(color);
        
		return node;
    }
    
    private DefaultDrawableNode generateStoreNode(String label, Color color, int x, int y) {
        RectNetworkItem rect = new RectNetworkItem(x,y);
        rect.allowResizing(false);
        rect.setHeight(40);
        rect.setWidth(80);

        rect.setLabelColor(STORE_LABEL_COLOR);

        DefaultDrawableNode node = new DefaultDrawableNode(label, rect);
        node.setColor(color);
        
		return node;
	}
    
}
