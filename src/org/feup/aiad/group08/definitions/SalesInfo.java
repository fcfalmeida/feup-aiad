package org.feup.aiad.group08.definitions;

import java.io.Serializable;
import java.util.Objects;

import jade.core.AID;

public class SalesInfo implements Serializable {

    private static final long serialVersionUID = -6234236657351857452L;
    private float itemPrice;
    private float discountPercentage;
    private StoreType storeType;
    private AID storeName;

    public SalesInfo(float itemPrice, float discountPercentage, StoreType storeType, AID storeName) {
        this.itemPrice = itemPrice;
        this.discountPercentage = discountPercentage;
        this.storeType = storeType;
        this.storeName = storeName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public float discountPercentage() {
        return itemPrice;
    }

    public StoreType storeType() {
        return storeType;
    }

    public AID getStoreName() {
        return storeName;
    }

    public float calculateFinalPrice() {
        return (itemPrice - (itemPrice * discountPercentage));
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemPrice, discountPercentage, storeType, storeName);
    }

    @Override
    public String toString() {
        return storeName.getLocalName() + "(" + storeType + ") - " + "Item Price: " + itemPrice + " | Discount: "
                + discountPercentage;
    }
}
