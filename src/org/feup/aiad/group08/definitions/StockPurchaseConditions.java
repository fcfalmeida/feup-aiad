package org.feup.aiad.group08.definitions;

import java.io.Serializable;
import java.util.SortedMap;

public class StockPurchaseConditions implements Serializable {
    
    private static final long serialVersionUID = 7754417352745743431L;
    private float baseUnitPrice;
    private SortedMap<Integer, Float> quantityDiscountModel;

    public StockPurchaseConditions(float baseUnitPrice, SortedMap<Integer, Float> quantityDiscountModel) {
        this.baseUnitPrice = baseUnitPrice;
        this.quantityDiscountModel = quantityDiscountModel;
    }

    public float getBaseUnitPrice() {
        return baseUnitPrice;
    }

    public SortedMap<Integer, Float> getQuantityDiscountModel() {
        return quantityDiscountModel;
    }

    /**
     * Returns the discount applicable when purchasing a given quantity
     * according to the warehouse's quantity discount model
     * @param quantity the quantity
     * @return the applicable discount
     */
    public float appliedDiscount(int quantity) {
        float discount = 0;

        for (SortedMap.Entry<Integer, Float> quantDiscount : quantityDiscountModel.entrySet()) {
            if (quantity < quantDiscount.getKey())
                return discount;
            else   
                discount = quantDiscount.getValue();
        }

        return discount;
    }

    /**
     * Calculates the final unit price when purchasing a given quantity 
     * according to the warehouse's quantity discount model
     * @param quantity desired quantity
     * @return the final unit price
     */
    public float finalUnitPrice(int quantity) {
        float discount = appliedDiscount(quantity);

        return baseUnitPrice - (baseUnitPrice * discount);
    }
}
