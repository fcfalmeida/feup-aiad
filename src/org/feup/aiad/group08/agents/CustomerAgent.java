package org.feup.aiad.group08.agents;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

import org.feup.aiad.group08.definitions.StoreType;
import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;
import org.feup.aiad.group08.utils.Distribution;
import org.feup.aiad.group08.utils.Utils;
import org.feup.aiad.group08.behaviours.ReceiveInformBehaviour;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SalesInfo;

public class CustomerAgent extends DFUserAgent {

    private static final long serialVersionUID = -8345978142167560058L;

    private static final float INFLUENCE_UPPER_LIMIT = 1f;
    private static final float INFLUENCE_LOWER_LIMIT = 0.2f;

    private float balance; // The total funds availaible for the customer to purchase.
    private float influenceability; // Each agent can be more easily or not influenced by promotions
    private List<StoreType> storePreferences;

    private List<SalesInfo> salesInfo = new Vector<>();

    public CustomerAgent(float initBalance, List<StoreType> storePreferences, float influenceability) {
        addSystemRole(SystemRole.CUSTOMER);

        balance = initBalance;
        this.storePreferences = storePreferences;
        this.influenceability = influenceability;
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new ReceiveAdvertisementBehaviour(this));
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

    // This method generates a random integer between an interval that will define
    // how influentable the costumer is to sales.
    private static float generateInfluenceability() {
        return INFLUENCE_LOWER_LIMIT + new Random().nextFloat() * (INFLUENCE_UPPER_LIMIT - INFLUENCE_LOWER_LIMIT);
    }

    private class ReceiveAdvertisementBehaviour extends ReceiveInformBehaviour {

        private static final long serialVersionUID = 1L;

        public ReceiveAdvertisementBehaviour(Agent agent) {
            super(agent, MessageType.ADVERTISER_SALES_INFO);
        }

        @Override
        public void processMessage(ACLMessage msg) {
            System.out.println("Customer " + getAgent().getLocalName() + " received sales info from Advertiser");
            try {
                salesInfo = (Vector<SalesInfo>) msg.getContentObject();
                SalesInfo bestItem = decideBestPurchase();

                if (bestItem == null) {
                    System.out.println("Customer " + getAgent().getLocalName() + " will not purchase anything.");
                    return;
                }

                ACLMessage purchaseItemMsg = MessageFactory.purchaseItem(bestItem);
                
                addBehaviour(new PurchaseItemBehaviour(getAgent(), purchaseItemMsg));
                
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
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
        // We need to add the SalesInfoVector from the Advertiser Agent
        
        // Map that stores SalesInfo and their respective probabilities
        Map<SalesInfo, Float> preferenceProbs = new HashMap<>();

        // For each SalesInfo calculate its preference probability base on the ranking of the customer
        for (SalesInfo sInfo : salesInfo) {
            float preferenceProb = calculatePreferenceProb(sInfo.storeType());
            // Here the preference probability is multiplied by the discount and influenceavbility to get the probability of the items getting bought
            // These aren't actually probabilities and should be called weights because the values can be bigger than 1.
            if(preferenceProb > 0) {
                preferenceProbs.put(sInfo, preferenceProb * sInfo.discountPercentage() * influenceability);
            }
        }
        
        //Distribution<SalesInfo> salesDist = new Distribution<>(preferenceProbs);
        SalesInfo bestItem = Utils.highestEntry(preferenceProbs);

        // If the customer has no money they can't buy the item
        if (bestItem.getItemPrice() > balance) {
            return null;
        }

        System.out.println(bestItem);
        return bestItem;      
    }

    // This method finds the StoreType preference rank of the customer preferences and calculates its probability
    private float calculatePreferenceProb(StoreType preference){
        int index = storePreferences.indexOf(preference);

        if(index == -1){
            return 0f;
        }
        
        // The last element of storePreferences list has the highest percentage
        return (float)(index + 1)/storePreferences.size();
    }
}
