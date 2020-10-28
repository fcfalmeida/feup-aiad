package org.feup.aiad.group08.agents;

import java.util.Random;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.StoreType;
import org.feup.aiad.group08.definitions.SystemRole;

public class CustomerAgent extends DFUserAgent {

    private static final long serialVersionUID = -8345978142167560058L;

    private static final int INFLUENCE_UPPER_LIMIT = 10;
    private static final int INFLUENCE_LOWER_LIMIT = 1;

    private float balance; // The total funds availaible for the customer to purchase.
    private double influenceability; // Each agent can be more easily or not influenced by promotions
    private Vector<String> storePreferences;

    public CustomerAgent(float initBalance, Vector<String> storePreferences) {
        addSystemRole(SystemRole.CUSTOMER);

        balance = initBalance;
        this.storePreferences = storePreferences;
        influenceability = generateInfluenceability();

    }

    public CustomerAgent() {
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

    public double getInfluenceability() {
        return influenceability;
    }

    // This method generates a random integer between an interval that will define how influentable the costumer is to sales.
    private static double generateInfluenceability() {
        return new Random().nextInt(INFLUENCE_UPPER_LIMIT - INFLUENCE_LOWER_LIMIT + 1) + INFLUENCE_LOWER_LIMIT;
    }

    /**
     * This class is used for the CustomerAgent to communicate that it wants to buy an item from a StoreAgent.
     */
    private class PurchaseItemBehaviour extends AchieveREInitiator{
        
        private static final long serialVersionUID = 1L;

        public PurchaseItemBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		
        @Override
        protected void handleInform(ACLMessage inform) {
            System.out.println("Store " + getAID().getName() + " received item purchase confirmation from Store");
            
            /**
             * Item purchase is done
             */  
        }    
    }
}

