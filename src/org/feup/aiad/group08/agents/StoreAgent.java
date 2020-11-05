package org.feup.aiad.group08.agents;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

import org.feup.aiad.group08.behaviours.InformBehaviour;
import org.feup.aiad.group08.behaviours.ReceiveInformBehaviour;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SalesInfo;
import org.feup.aiad.group08.definitions.StoreType;
import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;

public class StoreAgent extends DFUserAgent {

    private static final long serialVersionUID = -3205276776739404040L;
    private StoreType type;
    private float balanceAvailable;
    private int stock;

    public StoreAgent(StoreType type) {
        this.type = type;
        addSystemRole(SystemRole.STORE);
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new ReceiveStockPurchaseAuthorizationBehaviour(this));
        addBehaviour(new SendSaleInfo(this));
    }

    public float getBalance() {
        return balanceAvailable;
    }

    //
    public int getStock() {
        Random rand = new Random();
        int numberRequestStock = rand.nextInt(51);
        return numberRequestStock;
    }
    //

    public float checkStock() {
        // if (stock == 0) {
        // }
        return stock;
    }

    private class ReceiveStockPurchaseAuthorizationBehaviour extends ReceiveInformBehaviour {

        private static final long serialVersionUID = -3093017051433226823L;

        public ReceiveStockPurchaseAuthorizationBehaviour(Agent agent) {
            super(agent, MessageType.AUTHORIZE_STOCK_PURCHASE);
        }

        @Override
        public void processMessage(ACLMessage msg) {
            System.out.println("Store " + getAID().getLocalName() + " received stock purchase authorization from Manager");

            AID warehouse = searchOne(SystemRole.WAREHOUSE);
            ACLMessage stockPurchaseMsg = MessageFactory.purchaseStock(warehouse);

            addBehaviour(new PurchaseStockBehaviour(getAgent(), stockPurchaseMsg));
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
            addBehaviour(new SendStockPurchaseConfirmationBehaviour(getAgent()));
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
