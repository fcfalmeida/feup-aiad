package org.feup.aiad.group08.agents;

import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.StockPurchaseConditions;
import org.feup.aiad.group08.definitions.StockPurchaseReceipt;
import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

public class WarehouseAgent extends DFUserAgent {

    private static final long serialVersionUID = -908203983166089013L;

    private StockPurchaseConditions spc;

    public WarehouseAgent(StockPurchaseConditions spc) {
        this.spc = spc;
        addSystemRole(SystemRole.WAREHOUSE);
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new ReceiveStockPurchaseRequestBehaviour(this));
        addBehaviour(new SendStockPurchaseConditions(this));
    }

    private class SendStockPurchaseConditions extends AchieveREResponder {

        private static final long serialVersionUID = -8419347787177137973L;

        public SendStockPurchaseConditions(Agent a) {
            super(a, MessageTemplate.MatchConversationId(MessageType.REQUEST_STOCK_PURCHASE_CONDITIONS.toString()));
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
                throws FailureException {

            ACLMessage reply = MessageFactory.requestStockPurchaseConditionsReply(request, spc);

            return reply;
        }
    }

    private class ReceiveStockPurchaseRequestBehaviour extends AchieveREResponder {

        private static final long serialVersionUID = 7445345420227866962L;

        public ReceiveStockPurchaseRequestBehaviour(Agent a) {
            super(a, MessageTemplate.MatchConversationId(MessageType.PURCHASE_STOCK.toString()));
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
                throws FailureException {

            int requestedQuantity = 0;

            try {
                requestedQuantity = (Integer) request.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            System.out.println("Warehouse Received stock purchase request from " + request.getSender().getLocalName());
            
            float finalUnitPrice = spc.finalUnitPrice(requestedQuantity);
            float appliedDiscount = spc.appliedDiscount(requestedQuantity);
            float totalPrice = (finalUnitPrice * requestedQuantity) - (finalUnitPrice * requestedQuantity * appliedDiscount);
            totalPrice = Math.round(totalPrice * 100) / 100;

            StockPurchaseReceipt receipt = new StockPurchaseReceipt(finalUnitPrice,
                    requestedQuantity, appliedDiscount, totalPrice);
            
            ACLMessage res = MessageFactory.purchaseStockReply(request, receipt);

            return res;
        }

    }
}
