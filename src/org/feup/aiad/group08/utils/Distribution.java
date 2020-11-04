package org.feup.aiad.group08.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

// TODO: Ver melhor isto
public class Distribution<T> {

    List<Float> probs = new ArrayList<>();
    List<T> events = new ArrayList<>();
    float sumProb;
    Random rand = new Random();

    public Distribution(Map<T, Float> probs) {

        for (T event : probs.keySet()) {
            sumProb += probs.get(event);
            events.add(event);
            this.probs.add(probs.get(event));
        }
    }

    public T sample() {

        float prob = rand.nextFloat() * sumProb;
        int i;
        for (i = 0; prob > 0; i++) {
            prob -= probs.get(i);
        }
        return events.get(i - 1);
    }
}
