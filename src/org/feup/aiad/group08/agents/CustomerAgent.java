package org.feup.aiad.group08.agents;

import java.util.Random;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.util.Vector;

public class CustomerAgent extends Agent {

    private static final long serialVersionUID = -8345978142167560058L;
    private static final int INFLUENCE_UPPER_LIMIT = 10;
    private static final int INFLUENCE_LOWER_LIMIT = 1;

    private float balance; // The total funds availaible for the customer to purchase.
    private double influenceability; // Each agent can be more easily or not influenced by promotions
    private Vector<String> storePreferences;

    public CustomerAgent(float initBalance, Vector<String> storePreferences) {
        balance = initBalance;
        this.storePreferences = storePreferences;
        influenceability = generateInfluenceability();
        
    }

    public CustomerAgent(){
        balance = 100;
        storePreferences = new Vector<String>(2);
        storePreferences.add("Tech");
        storePreferences.add("Food");
        influenceability = generateInfluenceability();
    }

    public float getBalance() {
        return balance;
    }

    public Vector<String> getStorePreferences() {
        return storePreferences;
    }

    public double getInfluenceability(){
        return influenceability;
    }
        
    private static double generateInfluenceability(){
        return new Random().nextInt(INFLUENCE_UPPER_LIMIT - INFLUENCE_LOWER_LIMIT + 1) + INFLUENCE_LOWER_LIMIT;
    }
}
