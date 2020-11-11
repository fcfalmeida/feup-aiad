package org.feup.aiad.group08.csvManager.parsers;
import java.util.ArrayList;
import java.util.List;
import org.feup.aiad.group08.agents.CustomerAgent;
import org.feup.aiad.group08.definitions.StoreType;

public class CustomerParser implements Parser<CustomerAgent> {
    @Override
    public CustomerAgent parseLine(List<String> line) {

        String customerName = line.get(0);
        float initBalance = Float.valueOf(line.get(1));
        float influenceability = Float.valueOf(line.get(2));
        List<StoreType> storePreferences = new ArrayList<StoreType>();        
        storePreferences.add(StoreType.valueOf(line.get(3)));
        storePreferences.add(StoreType.valueOf(line.get(4)));
        storePreferences.add(StoreType.valueOf(line.get(5)));
        

        return new CustomerAgent(customerName, initBalance, storePreferences, influenceability);
    
    }
}