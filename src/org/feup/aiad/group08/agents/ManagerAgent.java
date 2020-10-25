package org.feup.aiad.group08.agents;

import java.util.List;

import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SystemPhase;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class ManagerAgent extends DFUserAgent {

    private static final long serialVersionUID = 1L;

    private SystemPhase currentPhase;
    private int salesPhaseElapsedTime;

    public int salesPhaseDuration;

    public ManagerAgent(int salesPhaseDuration) {
        this.salesPhaseDuration = salesPhaseDuration;
    }

    @Override
    protected void setup() {
        addBehaviour(new PhaseControlBehaviour(this, 1000));

        System.out.println("ManagerAgent Started");

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
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(MessageType.AUTHORIZE_STOCK_PURCHASE.toString());
        
        for (AID store : stores)
            msg.addReceiver(store);

        addBehaviour(new AuthorizeStockPurchaseBehaviour(this, msg));
    }

    private void startSalesPhase() {
        currentPhase = SystemPhase.SALES;
        salesPhaseElapsedTime = 0;
        System.out.println("SALES PHASE");

        // TODO: notify all CustomerAgent that they can begin shopping
    }

    class PhaseControlBehaviour extends TickerBehaviour {

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

    class AuthorizeStockPurchaseBehaviour extends AchieveREInitiator {

        private static final long serialVersionUID = -5814692859721095838L;

        public AuthorizeStockPurchaseBehaviour(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            System.out.println("Store " + inform.getSender() + " will purchase stock");
        }
    }
}
