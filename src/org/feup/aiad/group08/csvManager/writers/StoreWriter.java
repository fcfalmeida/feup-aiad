package org.feup.aiad.group08.csvManager.writers;

import java.util.ArrayList;
import java.util.List;

import org.feup.aiad.group08.agents.StoreAgent;

public class StoreWriter implements Writer<StoreAgent> {

    @Override
    public List<String> writeLine(StoreAgent store) {
        List<String> line = new ArrayList<>();

        line.add(store.getType().toString());
        // ...
        
        return line;
    }
    
}
