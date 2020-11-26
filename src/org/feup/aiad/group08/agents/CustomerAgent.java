package org.feup.aiad.group08.agents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import sajas.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import sajas.proto.AchieveREInitiator;
import sajas.proto.AchieveREResponder;
import uchicago.src.sim.network.DefaultDrawableNode;

import org.feup.aiad.group08.definitions.StoreType;
import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;
import org.feup.aiad.group08.simulation.Edge;
import org.feup.aiad.group08.simulation.MASShoppingLauncher;
import org.feup.aiad.group08.utils.Utils;
import org.feup.aiad.group08.behaviours.ReceiveInformBehaviour;
import org.feup.aiad.group08.data.AgentStatus;
import org.feup.aiad.group08.data.CustomerData;
import org.feup.aiad.group08.definitions.ItemPurchaseReceipt;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SalesInfo;

public class CustomerAgent extends DFUserAgent implements StatusReporter {

    private String customerName;
    private float balance; // The total funds availaible for the customer to purchase.
    private float influenceability; // Each agent can be more easily or not influenced by promotions
    private List<StoreType> storePreferences;
    private float happiness = 1;

    private List<SalesInfo> salesInfo = new Vector<>();

    private DefaultDrawableNode node;

    public CustomerAgent(String customerName, float initBalance, List<StoreType> storePreferences,
            float influenceability) {
        addSystemRole(SystemRole.CUSTOMER);

        this.customerName = customerName;
        balance = initBalance;
        this.storePreferences = storePreferences;
        this.influenceability = influenceability;
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new ReceiveAdvertisementBehaviour(this));
        addBehaviour(new SendStatusReportBehaviour(this));
    }

    public String getCustomerName() {
        return customerName;
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

    public void setNode(DefaultDrawableNode node) {
        this.node = node;
    }

    private class SendStatusReportBehaviour extends AchieveREResponder {

        private static final long serialVersionUID = 6646755738292798507L;

        public SendStatusReportBehaviour(Agent a) {
            super(a, MessageTemplate.MatchConversationId(MessageType.AGENT_STATUS.toString()));
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
                throws FailureException {
            
            AgentStatus status = createStatusReport();
            ACLMessage statusMessage = MessageFactory.agentStatusReply(request, status);

            return statusMessage;
        }
    }

    private class ReceiveAdvertisementBehaviour extends ReceiveInformBehaviour {

        private static final long serialVersionUID = 1L;

        public ReceiveAdvertisementBehaviour(Agent agent) {
            super(agent, MessageType.ADVERTISER_SALES_INFO);
        }

        @Override
        public void processMessage(ACLMessage msg) {
            // Clear all outgoing edges at the start of each iteration
            node.clearOutEdges();

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
     * This class is used for the CustomerAgent to communicate that it wants to buy
     * an item from a StoreAgent.
     */
    private class PurchaseItemBehaviour extends AchieveREInitiator {

        private static final long serialVersionUID = 1L;

        public PurchaseItemBehaviour(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleRefuse(ACLMessage refuse) {
            System.out.println("Customer " + customerName + " couldn't purchase the item because it is out of stock");
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            System.out.println("Customer " + customerName + " received item purchase confirmation from "
                    + inform.getSender().getLocalName());
            // The item is successfully purchased from the store.
            ItemPurchaseReceipt receipt;
            try {
                receipt = (ItemPurchaseReceipt) inform.getContentObject();
                happiness = calculateHappiness(receipt.getStoreType(), receipt.getItemPrice(),
                        receipt.getItemDiscount());
                System.out.println("Customer " + customerName + " happiness: " + happiness);

                DefaultDrawableNode storeNode = MASShoppingLauncher.getNode(inform.getSender().getLocalName());

                Edge edge = new Edge(node, storeNode);
                node.addOutEdge(edge);
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
    }

    private SalesInfo decideBestPurchase() {
        // Map that stores SalesInfo and their respective probabilities
        Map<SalesInfo, Float> preferenceProbs = new HashMap<>();

        // For each SalesInfo calculate its preference probability base on the ranking
        // of the customer
        for (SalesInfo sInfo : salesInfo) {
            float preferenceProb = calculatePreferenceProb(sInfo.storeType());
            // Here the preference probability is multiplied by the discount and
            // influenceavbility to get the probability of the items getting bought
            // These aren't actually probabilities and should be called weights because the
            // values can be bigger than 1.
            if (preferenceProb > 0) {
                preferenceProbs.put(sInfo, preferenceProb * sInfo.discountPercentage() * influenceability);
            }
        }

        // Distribution<SalesInfo> salesDist = new Distribution<>(preferenceProbs);
        SalesInfo bestItem = Utils.highestEntry(preferenceProbs);

        // If the customer has no money they can't buy the item
        if (bestItem.getItemPrice() > balance) {
            return null;
        }

        System.out.println(bestItem);
        return bestItem;
    }

    // This method finds the StoreType preference rank of the customer preferences
    // and calculates its probability
    private float calculatePreferenceProb(StoreType preference) {
        int index = storePreferences.indexOf(preference);

        if (index == -1) {
            return 0f;
        }

        // The last element of storePreferences list has the highest percentage
        return (float) (index + 1) / storePreferences.size();
    }

    private float calculateHappiness(StoreType storeType, float itemPrice, float itemDiscount) {
        float prefHappiness = calculatePreferenceProb(storeType);
        float happy = (prefHappiness * itemDiscount + happiness) / 2;
        return Utils.roundTo2Decimals(happy);
    }

    @Override
    public AgentStatus createStatusReport() {
        CustomerData data = new CustomerData(customerName, balance, influenceability, happiness);

        return data.toAgentStatus();
    }
}
