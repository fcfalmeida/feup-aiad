package org.feup.aiad.group08.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

import org.feup.aiad.group08.behaviours.InformBehaviour;
import org.feup.aiad.group08.behaviours.ReceiveInformBehaviour;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SalesInfo;
import org.feup.aiad.group08.definitions.StockPurchaseConditions;
import org.feup.aiad.group08.definitions.StockPurchaseReceipt;
import org.feup.aiad.group08.definitions.StoreType;
import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;

public class StoreAgent extends DFUserAgent {

    private static final long serialVersionUID = -3205276776739404040L;
    private final StoreType type;
    private float balanceAvailable;
    private final int stockCapacity;
    private int currentStock;
    // stock sold in each iteration
    private List<Integer> salesHistory = new ArrayList<>();

    public StoreAgent(StoreType type, int stockCapacity, int initBalance) {
        this.type = type;
        this.stockCapacity = stockCapacity;
        balanceAvailable = initBalance;
        addSystemRole(SystemRole.STORE);
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new ReceiveStockPurchaseAuthorizationBehaviour(this));
        addBehaviour(new SendSaleInfo(this));
    }

    private class ReceiveStockPurchaseAuthorizationBehaviour extends ReceiveInformBehaviour {

        private static final long serialVersionUID = -3093017051433226823L;

        public ReceiveStockPurchaseAuthorizationBehaviour(Agent agent) {
            super(agent, MessageType.AUTHORIZE_STOCK_PURCHASE);
        }

        @Override
        public void processMessage(ACLMessage msg) {
            System.out.println(
                    "Store " + getAID().getLocalName() + " received stock purchase authorization from Manager");

            AID warehouse = searchOne(SystemRole.WAREHOUSE);
            ACLMessage requestSpcMessage = MessageFactory.requestStockPurchaseConditions(warehouse);
            addBehaviour(new RequestStockPurchaseConditions(getAgent(), requestSpcMessage));
        }
    }

    private class RequestStockPurchaseConditions extends AchieveREInitiator {

        private static final long serialVersionUID = -6579450317631086255L;

        public RequestStockPurchaseConditions(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            StockPurchaseConditions spc = null;
            try {
                spc = (StockPurchaseConditions) inform.getContentObject();
            } catch (UnreadableException e1) {
                e1.printStackTrace();
            }

            int purchaseQuantity = decidePurchaseQuantity(spc);

            System.out.println("Store " + getLocalName() + " has decided to purchase " + purchaseQuantity
                    + " units from the Warehouse");

            AID warehouse = inform.getSender();
            ACLMessage stockPurchaseMsg = MessageFactory.purchaseStock(warehouse, purchaseQuantity);

            addBehaviour(new PurchaseStockBehaviour(getAgent(), stockPurchaseMsg));
        }
    }

    /**
     * Decides how much stock to purchase based on current stock capcity, available balance
     * warehouse stock purchase conditions and sales history
     * @param spc warehouse's stock purchase conditions
     * @return the amount of stock to purchase
     */
    private int decidePurchaseQuantity(StockPurchaseConditions spc) {
        Map<Integer, Float> quantityDiscountModel = spc.getQuantityDiscountModel();

        // Initialize max quantity with current stock capacity
        int maxQuantity = stockCapacity - currentStock;

        // Average sales in the past
        int salesAverage = calculateSalesAverage();

        // Determine the max purchasable quantity according to the model
        float prevDiscount = 0;
        for (Map.Entry<Integer, Float> quantDiscount : quantityDiscountModel.entrySet()) {
            // Minimum quantity to get discount
            int minQuantity = quantDiscount.getKey();

            // Calculate purchasable quantity based on previous discount
            float unitPriceWithPrevDiscount = spc.getBaseUnitPrice() - (spc.getBaseUnitPrice() * prevDiscount);
            int purchasableQuantity = (int) (balanceAvailable / unitPriceWithPrevDiscount);

            // If balance isn't enough, the new max equals the purchasable quantity
            if (purchasableQuantity < maxQuantity)
                maxQuantity = purchasableQuantity;

            // If max quantity is lower than min quantity, that means we can't get the
            // current discount so we break the loop
            if (maxQuantity < minQuantity)
                break;

            float unitPriceWithDiscount = spc.getBaseUnitPrice() - (spc.getBaseUnitPrice() * quantDiscount.getValue());
            // Update purchasable quantity with discount
            purchasableQuantity = (int) (balanceAvailable / unitPriceWithDiscount);

            // Update max quantity
            maxQuantity = purchasableQuantity;

            prevDiscount = quantDiscount.getValue();
        }

        // Calculate the average between the sales average and the max quantity
        // calculated previously. Then take away the current stock to determine
        // the quantity that should be purchased to achieve target quantity
        // Math.min is used here to prevent the store from purchasing more stock than it can store
        int currentStockCapacity = stockCapacity - currentStock;
        if (salesAverage > 0)
            return Math.min((salesAverage + maxQuantity) / 2 - currentStock, currentStockCapacity);
        else
            return Math.min(maxQuantity, currentStockCapacity);
    }

    /**
     * Calculates the average stock sold in the past
     * 
     * @return average stock sold in past iterations, zero if no previous sales
     *         record
     */
    private int calculateSalesAverage() {
        if (salesHistory.isEmpty())
            return 0;

        float sum = 0;
        for (Integer sold : salesHistory)
            sum += sold;

        return (int) Math.floor(sum / salesHistory.size());
    }

    private class PurchaseStockBehaviour extends AchieveREInitiator {

        private static final long serialVersionUID = -7327353955455573704L;

        public PurchaseStockBehaviour(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            StockPurchaseReceipt receipt;
            try {
                receipt = (StockPurchaseReceipt) inform.getContentObject();

                currentStock += receipt.getQuantity();
                balanceAvailable -= receipt.getTotalPrice();

                System.out.println("Store " + getAID().getName()
                        + " received stock purchase confirmation from Warehouse\nReceipt: " + receipt
                        + "\nCurrent stock: " + currentStock + "/" + stockCapacity + "\nAvailable Balance: "
                        + balanceAvailable);

                // Stock purchase done
                // Tell the manager that this store is done purchasing stock
                addBehaviour(new SendStockPurchaseConfirmationBehaviour(getAgent()));
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendStockPurchaseConfirmationBehaviour extends InformBehaviour {

        private static final long serialVersionUID = -1778420810173137218L;

        public SendStockPurchaseConfirmationBehaviour(Agent agent) {
            super(agent, MessageType.CONFIRM_STOCK_PURCHASE);
            AID manager = searchOne(SystemRole.MANAGER);
            receivers.add(manager);
        }
    }

    private class SendSaleInfo extends AchieveREResponder {

        private static final long serialVersionUID = -6144402641497240759L;

        public SendSaleInfo(Agent a) {
            super(a, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.MatchConversationId(MessageType.STORE_SALES_INFO.toString())));
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
                throws FailureException {

            // TODO: decide sale
            SalesInfo si = new SalesInfo(0, 0, type, getAID());

            ACLMessage reply = MessageFactory.storeSalesInfoReply(request, si);

            return reply;
        }
    }

    private class ReceiveItemPurchaseRequestBehaviour extends AchieveREResponder {

        private static final long serialVersionUID = 1L;

        public ReceiveItemPurchaseRequestBehaviour(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
                throws FailureException {
            ACLMessage res = request.createReply();
            res.setPerformative(ACLMessage.INFORM);

            System.out.println("Store received item purchase request from " + request.getSender().getName());

            // Waits for another item purchase from other agents
            addBehaviour(new ReceiveItemPurchaseRequestBehaviour(getAgent(),
                    MessageTemplate.MatchContent(MessageType.PURCHASE_ITEM.toString())));

            return res;
        }
    }
}
