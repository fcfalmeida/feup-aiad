package org.feup.aiad.group08.csvManager;

import java.util.List;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.IOException;

public class CSVWriter {

    private String fileToWriteName;
    private List<List<String>> data;
    
    public CSVWriter (String fileToWriteName, List<List<String>> data) throws IOException{

        this.fileToWriteName = fileToWriteName;
        this.data = data;

        
    }
}
