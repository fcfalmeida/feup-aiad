package org.feup.aiad.group08.data;

public class StoreData implements StatusConvertible {

    public String storeName;
    public int stockCapacity;
    public int unitsSold;

    public StoreData(String storeName, int stockCapacity, int unitsSold) {
        this.storeName = storeName;
        this.stockCapacity = stockCapacity;
        this.unitsSold = unitsSold;
    }

    @Override
    public AgentStatus toAgentStatus() {
        AgentStatus status = new AgentStatus();

        status.addParam("Store Name", storeName);
        status.addParam("Store Capacity", stockCapacity);
        status.addParam("UnitsSold", unitsSold);

        return status;
    }
    
}
