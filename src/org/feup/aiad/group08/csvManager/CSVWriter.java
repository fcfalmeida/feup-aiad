package org.feup.aiad.group08.csvManager;

import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

    private String fileToWriteName;
    private String delimiter;
    private List<List<String>> data;
    
    public CSVWriter (String fileToWriteName, String delimiter, List<List<String>> data) throws IOException{

        this.fileToWriteName = fileToWriteName;
        this.delimiter = delimiter;
        this.data = data;
        
        try( BufferedWriter buffWriter = new BufferedWriter( new FileWriter(fileToWriteName))){
            for( List<String> line : data ){
                for ( String lineValue : line){
                    buffWriter.write( lineValue + delimiter);                    
                }
                buffWriter.newLine();
            }
        }
    }

    public String getFileToWriteName(){
        return fileToWriteName;
    }

    public String getDelimiter(){
        return delimiter;
    }

    public List<List<String>> getData(){
        return data;
    }
}
