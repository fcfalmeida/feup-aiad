package org.feup.aiad.group08.agents;

import java.util.List;

import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SystemPhase;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

public class ManagerAgent extends DFUserAgent {

    private static final long serialVersionUID = 1L;

    private SystemPhase currentPhase;
    private int salesPhaseElapsedTime;

    public int salesPhaseDuration;

    public ManagerAgent(int salesPhaseDuration) {
        this.salesPhaseDuration = salesPhaseDuration;

        addSystemRole(SystemRole.MANAGER);
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new PhaseControlBehaviour(this, 1000));
        
        startRestockPhase();
    }

    private void nextPhase() {
        switch (currentPhase) {
            case RESTOCK:
                startSalesPhase();
                break;
            case SALES:
                startRestockPhase();
                break;
            default:
                break;
        }
    }

    private void startRestockPhase() {
        currentPhase = SystemPhase.RESTOCK;
        System.out.println("RESTOCK PHASE");

        List<AID> stores = search(SystemRole.STORE);
        ACLMessage msg = MessageFactory.authorizeStockPurchase(stores.toArray(new AID[0]));

        System.out.println("Stores found: " + stores.size());

        addBehaviour(new AuthorizeStockPurchaseBehaviour(this, msg));
    }

    private void startSalesPhase() {
        currentPhase = SystemPhase.SALES;
        salesPhaseElapsedTime = 0;
        System.out.println("SALES PHASE");

        // TODO: notify all CustomerAgent that they can begin shopping
    }

    private class PhaseControlBehaviour extends TickerBehaviour {

        private static final long serialVersionUID = 1L;

        public PhaseControlBehaviour(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            if (currentPhase.equals(SystemPhase.SALES)) {
                salesPhaseElapsedTime++;

                if (salesPhaseElapsedTime == salesPhaseDuration) {
                    nextPhase();
                }
            }
        }
    }

    private class AuthorizeStockPurchaseBehaviour extends AchieveREInitiator {

        private static final long serialVersionUID = -5814692859721095838L;

        public AuthorizeStockPurchaseBehaviour(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            System.out.println("Store " + inform.getSender().getName() + " will purchase stock");

            addBehaviour(new ReceiveStockPurchaseConfirmationBehaviour(getAgent(),
                    MessageTemplate.MatchConversationId(MessageType.CONFIRM_STOCK_PURCHASE.toString())));
        }
    }

    private class ReceiveStockPurchaseConfirmationBehaviour extends AchieveREResponder {

        private static final long serialVersionUID = 7438211244918027385L;

        public ReceiveStockPurchaseConfirmationBehaviour(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
                throws FailureException {
            System.out.println("Manager received stock purchase confirmation from store " + request.getSender().getName());

            return MessageFactory.confirmStockPurchaseReply(request);
        }
    }
}
