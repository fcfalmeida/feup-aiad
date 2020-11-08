package org.feup.aiad.group08.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.feup.aiad.group08.behaviours.InformBehaviour;
import org.feup.aiad.group08.behaviours.ReceiveInformBehaviour;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SalesInfo;
import org.feup.aiad.group08.definitions.SystemRole;
import org.feup.aiad.group08.messages.MessageFactory;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

public class AdvertiserAgent extends DFUserAgent {

    private static final long serialVersionUID = 8000251209494037807L;

    private Vector<SalesInfo> salesInfo = new Vector<>();
    private List<AID> customers = new ArrayList<>();
    private List<AID> stores = new ArrayList<>();

    public AdvertiserAgent() {
        addSystemRole(SystemRole.ADVERTISER);
    }

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new ReceiveSalesPhaseStartNotification(this));
    }

    class ReceiveSalesPhaseStartNotification extends ReceiveInformBehaviour {

        private static final long serialVersionUID = 8016710322546421892L;

        public ReceiveSalesPhaseStartNotification(Agent agent) {
            super(agent, MessageType.AUTHORIZE_ITEM_PURCHASE);
        }

        @Override
        public void processMessage(ACLMessage msg) {
            salesInfo.clear();

            customers = search(SystemRole.CUSTOMER);
            stores = search(SystemRole.STORE);

            ACLMessage requestSalesInfoMsg = MessageFactory.storeSalesInfo(stores.toArray(new AID[0]));
            addBehaviour(new RequestSalesInformation(getAgent(), requestSalesInfoMsg));
        }
    }

    class RequestSalesInformation extends AchieveREInitiator {

        private static final long serialVersionUID = -4623089918513280335L;

        public RequestSalesInformation(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            try {
                SalesInfo si = (SalesInfo) inform.getContentObject();
                salesInfo.add(si);
                System.out.println(si);
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            if (salesInfo.size() == stores.size()) {
                System.out.println("Advertiser received sales info from all stores, advertising sales to customers...");
                addBehaviour(new AdvertiseBehaviour(getAgent()));
            }
        }

    }

    class AdvertiseBehaviour extends InformBehaviour {

        private static final long serialVersionUID = 16509165843322125L;

        public AdvertiseBehaviour(Agent agent) {
            super(agent, MessageType.ADVERTISER_SALES_INFO, salesInfo, customers.toArray(new AID[0]));
        }

    }
}
