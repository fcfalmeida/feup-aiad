package org.feup.aiad.group08.definitions;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

import org.feup.aiad.group08.utils.Utils;

import jade.core.AID;

public class SalesInfo implements Serializable {

    private static final long serialVersionUID = -6234236657351857452L;
    private static final float[] possibleDiscounts = new float[] { .05f, .1f, .15f, .2f, .25f, .3f, .35f, .4f, .45f,
            .5f, .55f, .6f };

    // Item price already includes discount
    private float itemPrice;
    private float discountPercentage;
    private StoreType storeType;
    private AID storeName;

    public SalesInfo(float itemPrice, float discountPercentage, StoreType storeType, AID storeName) {
        this.itemPrice = Utils.roundTo2Decimals(itemPrice);
        this.discountPercentage = discountPercentage;
        this.storeType = storeType;
        this.storeName = storeName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public float discountPercentage() {
        return discountPercentage;
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

    public static float randomDiscount() {
        int randomIndex = new Random().nextInt(possibleDiscounts.length);

        return possibleDiscounts[randomIndex];
    }

    /**
     * Given a purchase and a selling price, returns the highest possible discount
     * that still respects the minimum profit margin
     * 
     * @param purchasePrice   purchase price
     * @param sellingPrice    selling price
     * @param minProfitMargin minimum profit margin
     * @return the best possible discount
     */
    public static float bestDiscount(float purchasePrice, float sellingPrice, float minProfitMargin) {
        float bestDiscount = 0;

        for (float discount : possibleDiscounts) {
            float sellingPriceWithDiscount = Utils.applyDiscount(sellingPrice, discount);
            float profitMargin = Utils.calculateProfitMargin(purchasePrice, sellingPriceWithDiscount);

            if (profitMargin >= minProfitMargin)
                bestDiscount = discount;
        }

        return bestDiscount;
    }
}
