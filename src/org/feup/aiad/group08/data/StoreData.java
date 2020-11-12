package org.feup.aiad.group08.data;

public class StoreData implements StatusConvertible {

    public int unitsSold;
    public float profit;

    public StoreData(int unitsSold, float profit) {
        this.unitsSold = unitsSold;
        this.profit = profit;
    }

    @Override
    public AgentStatus toAgentStatus() {
        AgentStatus status = new AgentStatus();

        status.addParam("UnitsSold", unitsSold);
        status.addParam("Profit", profit);

        return status;
    }
    
}
