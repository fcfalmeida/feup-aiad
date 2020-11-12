package org.feup.aiad.group08.agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;
import org.feup.aiad.group08.behaviours.InformBehaviour;
import org.feup.aiad.group08.behaviours.ReceiveInformBehaviour;
import org.feup.aiad.group08.data.AgentStatus;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SystemPhase;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

public class ManagerAgent extends DFUserAgent {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_MAX_ITERATIONS = 5;

    private SystemPhase currentPhase = SystemPhase.SALES;
    private int salesPhaseElapsedTime;
    private int maxIterations;
    private int currentIteration = 0;
    // Total stores that exist during the current iteration
    private List<AID> stores = new ArrayList<>();
    // Stores that have finished purchasing for the current iteration
    private Vector<AID> stockPurchaseConfirmations = new Vector<>();
    // Total customers that exist during the current iteration
    private List<AID> customers = new ArrayList<>();
    // Agent statuses
    private Vector<AgentStatus> statuses = new Vector<>();

    public int salesPhaseDuration;

    public ManagerAgent(int salesPhaseDuration, int maxIterations) {
        this.salesPhaseDuration = salesPhaseDuration;
        this.maxIterations = maxIterations;

        addSystemRole(SystemRole.MANAGER);
    }

    public ManagerAgent(int salesPhaseDuration) {
        this(salesPhaseDuration, DEFAULT_MAX_ITERATIONS);
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new PhaseControlBehaviour(this, 1000));
        addBehaviour(new ReceiveStockPurchaseConfirmationBehaviour(this));

        nextPhase();
    }

    private void nextPhase() {
        switch (currentPhase) {
            case RESTOCK:
                startSalesPhase();
                break;
            case SALES:
                if (currentIteration < maxIterations) {
                    System.out.println("\nIteration " + (currentIteration + 1) + "/" + maxIterations);
                    startRestockPhase();
                    currentIteration++;
                } else
                    System.out.println("Finished " + maxIterations + " iterations");
                break;
            default:
                break;
        }
    }

    private void startRestockPhase() {
        currentPhase = SystemPhase.RESTOCK;
        System.out.println("\nRESTOCK PHASE");

        stockPurchaseConfirmations.clear();
        stores = search(SystemRole.STORE);

        addBehaviour(new AuthorizeStockPurchaseBehaviour(this));
    }

    private void startSalesPhase() {
        currentPhase = SystemPhase.SALES;
        salesPhaseElapsedTime = 0;
        System.out.println("\nSALES PHASE");

        customers = search(SystemRole.CUSTOMER);

        addBehaviour(new SendSalesPhaseNotification(this));
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
                    // Clear status messages received on the previous iteration
                    statuses.clear();
                    List<AID> receivers = new ArrayList<>(stores);
                    receivers.addAll(customers);

                    ACLMessage statusMsg = MessageFactory.agentStatus(receivers.toArray(new AID[0]));
                    addBehaviour(new RequestAgentStatusBehaviour(getAgent(), statusMsg));
                }
            }
        }
    }

    private class RequestAgentStatusBehaviour extends AchieveREInitiator {

        private static final long serialVersionUID = -7991959884729782092L;

        public RequestAgentStatusBehaviour(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            try {
                AgentStatus status = (AgentStatus) inform.getContentObject();
                statuses.add(status);

                int expectedStatuses = stores.size() + customers.size();

                if (statuses.size() == expectedStatuses) {
                    System.out.println(Arrays.toString(statuses.toArray()));
                    // TODO: write to file
                    nextPhase();
                }
                
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }

    }

    private class AuthorizeStockPurchaseBehaviour extends InformBehaviour {

        private static final long serialVersionUID = -5814692859721095838L;

        public AuthorizeStockPurchaseBehaviour(Agent agent) {
            super(agent, MessageType.AUTHORIZE_STOCK_PURCHASE);
            receivers = stores;
        }
    }

    private class ReceiveStockPurchaseConfirmationBehaviour extends ReceiveInformBehaviour {

        private static final long serialVersionUID = 1079771040428551493L;

        public ReceiveStockPurchaseConfirmationBehaviour(Agent agent) {
            super(agent, MessageType.CONFIRM_STOCK_PURCHASE);
        }

        @Override
        public void processMessage(ACLMessage msg) {
            System.out.println(
                    "Manager received stock purchase confirmation from store " + msg.getSender().getLocalName());

            stockPurchaseConfirmations.add(msg.getSender());

            if (stockPurchaseConfirmations.size() == stores.size())
                nextPhase(); // start sales phase when all stores have finished purchasing stock
        }

    }

    private class SendSalesPhaseNotification extends InformBehaviour {

        private static final long serialVersionUID = 7031131499464261774L;

        public SendSalesPhaseNotification(Agent agent) {
            super(agent, MessageType.AUTHORIZE_ITEM_PURCHASE);

            AID advertiser = searchOne(SystemRole.ADVERTISER);
            receivers.add(advertiser);
        }

    }
}
