package org.feup.aiad.group08.utils;

import java.util.Map;

public class Utils {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates a profit margin
     * @param buyingPrice buying price
     * @param sellingPrice selling price
     * @return profit margin
     */
    public static float calculateProfitMargin(float buyingPrice, float sellingPrice) {
        float profitMargin = (sellingPrice - buyingPrice) / sellingPrice;
        return Utils.formatpercentage(profitMargin);
    }

    /**
     * Returns the given price with the given discount applied to it
     * @param initialPrice price
     * @param discount discount percentage
     * @return
     */
    public static float applyDiscount(float initialPrice, float discount) {
        return initialPrice - initialPrice * discount;
    }

    /**
     * Rounds a price to 2 decimal places
     * @param price price
     * @return price rounded to 2 decimal places
     */
    public static float formatPrice(float price) {
        return Math.round(price * 100) / 100;
    }

    /**
     * Rounds a percentage to 2 decimal places
     * @param percentage percentage
     * @return percentage rounded to 2 decimal places
     */
    public static float formatpercentage(float percentage) {
        return Math.round(percentage * 100) / 100;
    }

    public static <K,V extends Comparable<V>> K highestEntry(Map<K,V> map){

        K highestKey = null;
        V highestValue = null;

        for( Map.Entry<K, V> entry : map.entrySet()){
            if(entry.getValue().compareTo(highestValue) > 0){
                highestValue = entry.getValue();
                highestKey = entry.getKey();
            }
        }
        return highestKey;
    }
}
