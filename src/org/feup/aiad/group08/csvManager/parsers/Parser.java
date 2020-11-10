package org.feup.aiad.group08.csvManager.parsers;

import java.util.List;

public interface Parser<T> {
    
    T parseLine(List<String> line);
}
