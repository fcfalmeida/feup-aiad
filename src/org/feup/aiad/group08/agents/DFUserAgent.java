package org.feup.aiad.group08.agents;

import java.util.ArrayList;
import java.util.List;

import org.feup.aiad.group08.definitions.SystemRole;

import jade.core.AID;
import sajas.core.Agent;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFUserAgent extends Agent {

    private List<SystemRole> systemRoles = new ArrayList<>();

    protected void addSystemRole(SystemRole systemRole) {
        systemRoles.add(systemRole);
    }
    
    @Override
    protected void setup() {
        DFAgentDescription ad = new DFAgentDescription();
        ad.setName(getAID());

        for (SystemRole st : systemRoles) {
            ServiceDescription sd = new ServiceDescription();
            sd.setType(st.toString());
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

    /**
     * Searches the DF for agents that provide all of the specified services
     * @param systemRoles list of system roles (service types) to search
     * @return list of AIDs
     */
    protected List<AID> search(SystemRole... systemRoles) {
        List<AID> results = new ArrayList<>();

        DFAgentDescription template = new DFAgentDescription();
        
        for (SystemRole sr : systemRoles) {
            ServiceDescription sd = new ServiceDescription();
            sd.setType(sr.toString());
            template.addServices(sd);

            try {
                DFAgentDescription[] result = DFService.search(this, template);

                for (DFAgentDescription ad : result)
                    results.add(ad.getName());
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
        
        return results;
    }
    
    /**
     * Similar to search but returns only the first agent found
     * @param systemRoles list of system roles (service types) to search
     * @return the agent's AID or null if it couldn't be found
     */
    protected AID searchOne(SystemRole... systemRoles) {
        List<AID> result = search(systemRoles);

        return result.get(0);
    }
}
