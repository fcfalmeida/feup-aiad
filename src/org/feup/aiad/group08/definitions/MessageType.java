package org.feup.aiad.group08.definitions;

public enum MessageType {

    // Sent by ManagerAgent to allow StoreAgent to purchase stock
    AUTHORIZE_STOCK_PURCHASE("authorize-stock-purchase"),
    // Sent by StoreAgent to WarehouseAgent to request stock purchase
    PURCHASE_STOCK("purchase-stock"),
    // Sent by StoreAgent to ManagerAgent when it finishes purchasing stock
    CONFIRM_STOCK_PURCHASE("confirm-stock-purchase"),
    // Sent by StoreAgent to WarehouseAgent to request stock purchase conditions
    REQUEST_STOCK_PURCHASE_CONDITIONS("request-stock-purchase-conditions"),

    // Sent by ManagerAgent to allow CustomerAgent to purchase item
    AUTHORIZE_ITEM_PURCHASE("authorize-item-purchase"),
    // Sent by CustomerAgent to StoreAgent to request item purchase
    PURCHASE_ITEM("purchase-item"),

    // Sent by StoreAgent to AdvertiserAgent to let them know what sale the store will be doing
    STORE_SALES_INFO("store-sales-info"),
    // Sent by AdvertiserAgent to all CustomerAgent to inform them of current sales
    ADVERTISER_SALES_INFO("advertiser-sales-info"),

    // Sent by ManagerAgent to all Agents
    AGENT_STATUS("agent-status");

    private final String type;

    MessageType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static MessageType fromString(String text) {
        for (MessageType mt : MessageType.values()) {
            if (mt.type.equalsIgnoreCase(text))
                return mt;
        }
        return null;
    }
}
