package org.feup.aiad.group08.agents;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFSearchAgent extends Agent {
    
    private static final long serialVersionUID = -5785851533295258475L;

    /**
     * Searches the DF for agents that provide all of the specified services
     * @param servicesTypes list of service types
     * @return list of agent IDs
     */
    protected List<AID> search(String... servicesTypes) {
        List<AID> results = new ArrayList<>();

        DFAgentDescription template = new DFAgentDescription();
        
        for (String st : servicesTypes) {
            ServiceDescription sd = new ServiceDescription();
            sd.setType(st);
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
}
