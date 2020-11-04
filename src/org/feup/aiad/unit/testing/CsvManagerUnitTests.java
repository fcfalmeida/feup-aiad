package org.feup.aiad.unit.testing;

import java.io.File;
import java.io.IOException;

import org.feup.aiad.group08.csvManager.CSVReader;
import org.feup.aiad.group08.csvManager.CSVWriter;

import java.util.List;

public class CsvManagerUnitTests {

    public static void main(String[] args) throws IOException {

        System.out.println(new File("."));
        String readFileName = "./data/input/customersTest.csv";
        String writeFileName = "./data/output/customerBuyResults.csv";

        String delimiter = ";";

        // This tests the CSVReader Class
        CSVReader csvReader = new CSVReader(readFileName, delimiter);

        List<List<String>> data = csvReader.getData();

        for (List<String> customer : data) {
            for (String customerAttribute : customer) {
                System.out.println(customerAttribute);
            }
            System.out.println();
        }

        // This tests the CSVWriter Class 
        CSVWriter csvWriter = new CSVWriter(writeFileName, delimiter, data);
    }
}
