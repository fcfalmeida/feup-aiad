package org.feup.aiad.group08.data;

public class CustomerData implements StatusConvertible {
    
    public String name;
    public float balance;
    public float influenceability;
    public float happiness;

    public CustomerData(String name, float balance, float influenceability, float happiness) {
        this.name = name;
        this.balance = balance;
        this.influenceability = influenceability;
        this.happiness = happiness;
    }

    @Override
    public AgentStatus toAgentStatus() {
        AgentStatus as = new AgentStatus();

        as.addParam("Name", name);
        as.addParam("Balance", balance);
        as.addParam("Unfluenceability", influenceability);
        as.addParam("Happiness", happiness);

        return as;
    }

}
