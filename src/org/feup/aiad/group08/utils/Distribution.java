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

        // This iteration normalizes the probabilities to be between 0 and 1.
        for (T eventKey : probsMap.keySet()) {
            probsMap.put(eventKey, probsMap.get(eventKey)/sumProb);
        }
    }

    public T getRandomEvent() {
        
        float randomNumber = rand.nextFloat();

        System.out.println("\nRandom Number:" + randomNumber);
        
        //float lowerBound = 0;
        float absDiff = 1;
        float absDiffMin = 1;
        T randomEvent = null;

        for (T eventKey : probsMap.keySet()) {
            absDiff = Math.abs((randomNumber - probsMap.get(eventKey)));
            if(absDiff <= absDiffMin){
                absDiffMin = absDiff;
                randomEvent = eventKey;
            }            
        }
        return randomEvent;     
    }
}
