package org.feup.aiad.group08.agents;

import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class WarehouseAgent extends DFUserAgent {

    private static final long serialVersionUID = -908203983166089013L;

    public WarehouseAgent() {
        addSystemRole(SystemRole.WAREHOUSE);
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new ReceiveStockPurchaseRequestBehaviour(this,
                MessageTemplate.MatchConversationId(MessageType.PURCHASE_STOCK.toString())));
    }

    private class ReceiveStockPurchaseRequestBehaviour extends AchieveREResponder {

        private static final long serialVersionUID = 7445345420227866962L;

        public ReceiveStockPurchaseRequestBehaviour(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
                throws FailureException {
            ACLMessage res = MessageFactory.purchaseStockReply(request);

            System.out.println("Warehouse Received stock purchase request from " + request.getSender().getName());

            return res;
        }

    }
}
