package org.feup.aiad.group08.agents;

public enum ServiceType {
    STORE("store"),
    CUSTOMER("customer");
    
    private final String type;

    ServiceType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
