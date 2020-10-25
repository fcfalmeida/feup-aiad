package org.feup.aiad.group08.definitions;

public enum MessageType {

    AUTHORIZE_STOCK_PURCHASE("authorize-stock-purchase");
    
    private final String type;

    MessageType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
