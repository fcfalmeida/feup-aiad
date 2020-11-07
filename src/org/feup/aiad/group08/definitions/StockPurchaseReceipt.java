package org.feup.aiad.group08.definitions;

import java.io.Serializable;

public class StockPurchaseReceipt implements Serializable {

    private static final long serialVersionUID = 4017259109385436836L;
    private float unitPrice;
    private int quantity;
    // This value is merely informative. The unit price already includes the discount
    private float appliedDiscount;

    public StockPurchaseReceipt(float unitPrice, int quantity, float appliedDiscount) {
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.appliedDiscount = appliedDiscount;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getAppliedDiscount() {
        return appliedDiscount;
    }

    @Override
    public String toString() {
        return "Unit Price: " + unitPrice + " | Quantity: " + quantity + " | Applied Discount: " + appliedDiscount;
    }
}
