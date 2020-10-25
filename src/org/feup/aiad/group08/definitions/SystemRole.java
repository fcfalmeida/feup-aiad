package org.feup.aiad.group08.definitions;

public enum SystemRole {
    STORE("store"),
    CUSTOMER("customer"),
    MANAGER("manager");
    
    private final String type;

    SystemRole(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}