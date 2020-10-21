package org.feup.aiad.group08.agents;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFRegisterAgent extends Agent {

    private static final long serialVersionUID = -3233593926341881240L;

    private List<String> serviceTypes = new ArrayList<>();

    protected void addServiceType(String serviceType) {
        serviceTypes.add(serviceType);
    }
    
    @Override
    protected void setup() {
        DFAgentDescription ad = new DFAgentDescription();
        ad.setName(getAID());

        for (String st : serviceTypes) {
            ServiceDescription sd = new ServiceDescription();
            sd.setType(st);
            sd.setName(getLocalName());
            ad.addServices(sd);
        }

        try {
            DFService.register(this, ad);
            System.out.println(getName() + " registered in the DF");
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
