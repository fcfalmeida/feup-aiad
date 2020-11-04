package org.feup.aiad.group08.agents;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import org.feup.aiad.group08.definitions.StoreType;
import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.utils.Distribution;
import org.feup.aiad.group08.definitions.SalesInfo;

public class CustomerAgent extends DFUserAgent {

    private static final long serialVersionUID = -8345978142167560058L;

    private static final int INFLUENCE_UPPER_LIMIT = 10;
    private static final int INFLUENCE_LOWER_LIMIT = 1;

    private float balance; // The total funds availaible for the customer to purchase.
    private float influenceability; // Each agent can be more easily or not influenced by promotions
    private List<StoreType> storePreferences;

    private Vector<SalesInfo> salesInfo = new Vector<>();

    public CustomerAgent(float initBalance, List<StoreType> storePreferences) {
        addSystemRole(SystemRole.CUSTOMER);

        balance = initBalance;
        this.storePreferences = storePreferences;
        influenceability = generateInfluenceability();
        
    }

    public float getBalance() {
        return balance;
    }

    public List<StoreType> getStorePreferences() {
        return storePreferences;
    }

    public float getInfluenceability() {
        return influenceability;
    }

    // This method generates a random integer between an interval that will define how influentable the costumer is to sales.
    private static float generateInfluenceability() {
        return new Random().nextInt(INFLUENCE_UPPER_LIMIT - INFLUENCE_LOWER_LIMIT + 1) + INFLUENCE_LOWER_LIMIT;
    }

    /**
     * This class is used for the CustomerAgent to communicate that it wants to buy an item from a StoreAgent.
     */
    private class PurchaseItemBehaviour extends AchieveREInitiator{
        
        private static final long serialVersionUID = 1L;

        // Wait for sales from AdvertiserAgent

        // Choose best item from the list of items

        public PurchaseItemBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		
        @Override
        protected void handleInform(ACLMessage inform) {
            System.out.println("Store " + getAID().getName() + " received item purchase confirmation from Store");
            // The item is successfully purchased from the store.
        }
    }

    private SalesInfo decideBestPurchase(){
        Map<SalesInfo, Float> preferenceProbs = new HashMap<>();

        for (SalesInfo sInfo : salesInfo) {
            float preferenceProb = calculatePreferenceProb(sInfo.storeType());
            preferenceProbs.put(sInfo, preferenceProb * sInfo.discountPercentage() * influenceability);
        }
        
        Distribution<SalesInfo> salesDist = new Distribution<>(preferenceProbs);

        return salesDist.sample();        
    }

    private float calculatePreferenceProb(StoreType preference){
        int index = storePreferences.indexOf(preference);

        // The last element of storePreferences list has the highest percentage
        return (float)(index + 1)/storePreferences.size();
    }
}
