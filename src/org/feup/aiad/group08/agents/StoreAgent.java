package org.feup.aiad.group08.agents;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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
            // TODO: addBehaviour(new PurchaseStockBehaviour())

            ACLMessage res = request.createReply();
            res.setPerformative(ACLMessage.INFORM);

            return res;
        }

    }
}
