package org.feup.aiad.group08.definitions;

public enum MessageType {

    // Sent by ManagerAgent to allow StoreAgent to purchase stock
    AUTHORIZE_STOCK_PURCHASE("authorize-stock-purchase"),
    // Sent by StoreAgent to WarehouseAgent to request stock purchase
    PURCHASE_STOCK("purchase-stock"),
    // Sent by StoreAgent to ManagerAgent when it finishes purchasing stock
    CONFIRM_STOCK_PURCHASE("confirm-stock-purchase"),

    // Sent by ManagerAgent to allow CustomerAgent to purchase item
    AUTHORIZE_ITEM_PURCHASE("authorize-item-purchase"),
    // Sent by CustomerAgent to StoreAgent to request item purchase
    PURCHASE_ITEM("purchase-item");
    
    private final String type;

    MessageType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
