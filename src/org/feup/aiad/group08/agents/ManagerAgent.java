package org.feup.aiad.group08.agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class ManagerAgent extends Agent {

    private static final long serialVersionUID = 1L;
    
    private SystemPhase currentPhase;
    private int salesPhaseElapsedTime;
    
    public int salesPhaseDuration;

    public ManagerAgent(int salesPhaseDuration) {
        this.salesPhaseDuration = salesPhaseDuration;
    }

    @Override
    protected void setup() {
        super.setup();

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

        // TODO: notify all StoreAgent that they can begin purchasing stock
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
}
