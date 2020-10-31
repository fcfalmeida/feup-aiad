package org.feup.aiad.group08.csvManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class CSVReader {

    private String fileToReadName;
    private  String delimiter;
    private List<List<String>> data;

    /**
     * This constructor reads every line of the csv and stores the values of the
     * line in a list. Each line represented by a list is added to the data list
     * The class was mode for reading customer and store information from a csv file
     * @param fileToReadName - Name of the csv file that is going to be read, e.g.
     *                       "customers.csv"
     * @param delimiter      - Character or string that delimits each value in the
     *                       lines of the csv
     * @throws IOException
     */
    public CSVReader(String fileToReadName, String delimiter) throws IOException {

        this.fileToReadName = fileToReadName;
        this.delimiter = delimiter;

        try ( BufferedReader buffReader = new BufferedReader( new FileReader(fileToReadName))) {
            String line;
            while (( line = buffReader.readLine()) != null) {
                String[] lineValues = line.split(delimiter);
                data.add(Arrays.asList(lineValues));
            }
        }
    }

    public String getFileToReadName(){
        return fileToReadName;
    }
    
    public String getDelimiter(){
        return delimiter;
    }    
}