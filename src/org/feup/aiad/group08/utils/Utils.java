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
        return Utils.roundTo2Decimals(profitMargin);
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
     * Rounds a value to 2 decimal places
     * @param value value
     * @return value rounded to 2 decimal places
     */
    public static float roundTo2Decimals(float value) {
        return (float) Math.round(value * 100f) / 100f;
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
