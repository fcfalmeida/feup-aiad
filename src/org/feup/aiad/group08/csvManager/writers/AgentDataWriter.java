package org.feup.aiad.group08.csvManager.writers;

import java.util.Arrays;
import java.util.List;

import org.feup.aiad.group08.data.AgentStatus;

public class AgentDataWriter implements Writer<AgentStatus> {

    @Override
    public List<String> writeLine(AgentStatus obj) {
        String[] params = obj.toString().split(",");

        return Arrays.asList(params);
    }
    
}
