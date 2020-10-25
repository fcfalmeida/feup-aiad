package org.feup.aiad.group08.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SystemRole;

public class StoreAgent extends DFUserAgent {

    private static final long serialVersionUID = -3205276776739404040L;
    private float balanceAvailable;
    private int stock;
    private int sale;
    private float price;

    public StoreAgent() {
        addSystemRole(SystemRole.STORE);
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new ReceiveStockPurchaseAuthorizationBehaviour(this, 
            MessageTemplate.MatchContent(MessageType.AUTHORIZE_STOCK_PURCHASE.toString())));
    }

    public float getBalance() {
        return balanceAvailable;
    }

    public float checkStock() {
        // if (stock == 0) {
        // }
        return stock;
    }

    public float getFinalPrice() {
        return (price - (price * sale));
    }

    private class ReceiveStockPurchaseAuthorizationBehaviour extends AchieveREResponder {

        private static final long serialVersionUID = 1L;

        public ReceiveStockPurchaseAuthorizationBehaviour(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
            System.out.println("Store " + getAID().getName() + " received stock purchase authorization from Manager");
            AID warehouse = searchOne(SystemRole.WAREHOUSE);

            ACLMessage stockPurchaseMsg = new ACLMessage(ACLMessage.REQUEST);
            stockPurchaseMsg.setContent(MessageType.PURCHASE_STOCK.toString());
            stockPurchaseMsg.addReceiver(warehouse);

            addBehaviour(new PurchaseStockBehaviour(getAgent(), stockPurchaseMsg));

            ACLMessage res = request.createReply();
            res.setPerformative(ACLMessage.INFORM);
            
            return res;
        }

    }

    private class PurchaseStockBehaviour extends AchieveREInitiator {

        private static final long serialVersionUID = -7327353955455573704L;

        public PurchaseStockBehaviour(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            System.out.println("Store " + getAID().getName() + " received stock purchase confirmation from Warehouse");
            // Stock purchase done
            // Tell the manager that this store is done purchasing stock
            AID manager = searchOne(SystemRole.MANAGER);
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(MessageType.CONFIRM_STOCK_PURCHASE.toString());
            msg.addReceiver(manager);

            addBehaviour(new SendStockPurchaseConfirmationBehaviour(getAgent(), msg));
        }
    }

    private class SendStockPurchaseConfirmationBehaviour extends AchieveREInitiator {

        private static final long serialVersionUID = 8122070889759051930L;

        public SendStockPurchaseConfirmationBehaviour(Agent a, ACLMessage msg) {
            super(a, msg);
        }
    }
}
