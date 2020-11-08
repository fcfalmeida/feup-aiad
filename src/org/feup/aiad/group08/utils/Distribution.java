package org.feup.aiad.group08.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is used when we have map of events and their respective probability and we want
 * to "roll the dice" on those events.
 */
public class Distribution<T> {

    float sumProb;
    Random rand = new Random();

    // (Key set) List of the events that each correspond to one probability
    // (Value Set) List of the probability values
    Map<T, Float> probsMap = new HashMap<>();

    public Distribution(Map<T, Float> probsMap) {

        this.probsMap = probsMap;

        for (T eventKey : probsMap.keySet()) {
            sumProb += probsMap.get(eventKey);            
        }
    }

    public T getRandomEvent() {
        
        float randomNumber = rand.nextFloat() * sumProb;
        
        float lowerBound = 0;

        for (T eventKey : probsMap.keySet()) {
            if (lowerBound <= randomNumber && randomNumber <= (lowerBound + probsMap.get(eventKey))){
                return eventKey;
            } else {
            lowerBound += probsMap.get(eventKey);
            }
        }

        return null;        
    }
}
