package org.feup.aiad.group08.csvManager;

import java.util.Collection;
import java.util.List;

import org.feup.aiad.group08.csvManager.writers.Writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter<T> {

    private String fileToWriteName;
    private String delimiter;
    private Collection<T> data;
    private Writer<T> writer;
    
    /**
     * This class writes to a file data from a List<List<String>>.
     * @param fileToWriteName - The name of the file to write to
     * @param delimiter - The delimiter that separates the values in the file, normally in .csv it can be ";" or ","
     * @param data - The list of objects of type T to write
     * @param writer - The writer object responsible for generating a line for the object of type T
     * @throws IOException
     */
    public CSVWriter(String fileToWriteName, String delimiter, Collection<T> data, Writer<T> writer) {

        this.fileToWriteName = fileToWriteName;
        this.delimiter = delimiter;
        this.data = data;
        this.writer = writer;
    }

    public String getFileToWriteName(){
        return fileToWriteName;
    }

    public String getDelimiter(){
        return delimiter;
    }

    public Collection<T> getData(){
        return data;
    }

    public void writeData() throws IOException {
        try( BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fileToWriteName, true))){
            for(T obj : data ) {
                List<String> line = writer.writeLine(obj);
                for (int i = 0; i < line.size(); i++) {
                    String lineValue = line.get(i);
                    String delimiterToWrite = i < line.size() - 1 ? delimiter : "";
                    buffWriter.write( lineValue + delimiterToWrite); 
                }
                buffWriter.newLine();
            }
        }
    }
}
