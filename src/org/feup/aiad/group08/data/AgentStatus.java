package org.feup.aiad.group08.data;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class AgentStatus implements Serializable {
    
    private static final long serialVersionUID = 4053751384296890347L;

    private Map<String, Serializable> params;

    public AgentStatus() {
        params = new TreeMap<>();
    }

    public void addParam(String name, Serializable value) {
        params.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (Map.Entry<String, Serializable> param : params.entrySet()) {
            count++;
            sb.append(param.getKey())
                .append(": ")
                .append(param.getValue())
                .append(count < params.size() ? ", " : "");
        }

        return sb.toString();
    }
}
