package org.feup.aiad.group08.csvManager.parsers;

import java.util.List;

import org.feup.aiad.group08.agents.StoreAgent;
import org.feup.aiad.group08.definitions.StoreType;

public class StoreParser implements Parser<StoreAgent> {

    @Override
    public StoreAgent parseLine(List<String> line) {

        String storeName = line.get(0);
        StoreType storeType = StoreType.valueOf(line.get(1)); 
        int stockCapacity = Integer.parseInt(line.get(2));
        float initBalance = Float.valueOf(line.get(3));
        float minProfitMargin = Float.valueOf(line.get(4));
        float desiredProfitMargin = Float.valueOf(line.get(5));

        

        return new StoreAgent(storeName, storeType, stockCapacity, initBalance, minProfitMargin, desiredProfitMargin);

        return null;
    }
    
}
