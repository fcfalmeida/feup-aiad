package org.feup.aiad.group08.definitions;


public class SalesInfo {
    
    private float itemPrice;
    private float discountPercentage;
    private StoreType storeType;

    public SalesInfo(float itemPrice, float discountPercentage, StoreType storeType) {
        this.itemPrice = itemPrice;
        this.discountPercentage = discountPercentage;
        this.storeType = storeType;
    }
    
    public float getItemPrice(){
        return itemPrice;
    }

    public float discountPercentage(){
        return itemPrice;
    }
    
    public StoreType storeType(){
        return storeType;
    }
    
    public float getFinalPrice() {
        return (itemPrice - (itemPrice * discountPercentage));
    }
}
