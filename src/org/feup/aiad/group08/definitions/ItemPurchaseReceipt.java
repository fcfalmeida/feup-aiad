package org.feup.aiad.group08.definitions;

import java.io.Serializable;

public class ItemPurchaseReceipt implements Serializable {
    
    private static final long serialVersionUID = -4738360278441055112L;
    private float itemPrice;

    public ItemPurchaseReceipt(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public float getItemPrice() {
        return itemPrice;
    }
}
