package org.feup.aiad.group08.csvManager.writers;

import java.util.ArrayList;
import java.util.List;

public class IterationWriter implements Writer<Integer> {

    @Override
    public List<String> writeLine(Integer obj) {
        List<String> data = new ArrayList<>();
        data.add("Iteration " + obj.toString());

        return data;
    }
    
}
