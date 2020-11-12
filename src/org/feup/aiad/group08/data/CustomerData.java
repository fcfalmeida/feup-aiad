package org.feup.aiad.group08.data;

public class CustomerData implements StatusConvertible {
    
    public float happiness;

    public CustomerData(float happiness) {
        this.happiness = happiness;
    }

    @Override
    public AgentStatus toAgentStatus() {
        AgentStatus as = new AgentStatus();

        as.addParam("Happiness", happiness);

        return as;
    }

}
