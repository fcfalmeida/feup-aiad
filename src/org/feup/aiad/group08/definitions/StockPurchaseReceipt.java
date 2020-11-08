package org.feup.aiad.group08.definitions;

import java.io.Serializable;

public class StockPurchaseReceipt implements Serializable {

    private static final long serialVersionUID = 4017259109385436836L;
    private float unitPrice;
    private int quantity;
    // This value is merely informative. The unit price already includes the discount
    private float appliedDiscount;
    private float totalPrice;

    public StockPurchaseReceipt(float unitPrice, int quantity, float appliedDiscount, float totalPrice) {
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.appliedDiscount = appliedDiscount;
        this.totalPrice = totalPrice;
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

    public float getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "Unit Price: " + unitPrice + " | Quantity: " + quantity + " | Applied Discount: " + appliedDiscount
                + " | Total: " + totalPrice;
    }
}
