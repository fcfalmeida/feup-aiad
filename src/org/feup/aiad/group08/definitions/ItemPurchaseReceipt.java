package org.feup.aiad.group08.definitions;

import java.io.Serializable;

public class ItemPurchaseReceipt implements Serializable {
    
    private static final long serialVersionUID = -4738360278441055112L;
    
    private StoreType storeType;
    private float itemPrice;
    private float itemDiscount;

    public ItemPurchaseReceipt(StoreType storeType, float itemPrice, float itemDiscount) {
        this.storeType = storeType;
        this.itemPrice = itemPrice;
        this.itemDiscount = itemDiscount;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public StoreType getStoreType() {
        return storeType;
    }
    
    public float getItemDiscount() {
        return itemDiscount;
    }
}
