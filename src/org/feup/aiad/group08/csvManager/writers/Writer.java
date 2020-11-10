package org.feup.aiad.group08.csvManager.writers;

import java.util.List;

public interface Writer<T> {
    
    List<String> writeLine(T obj);
}
