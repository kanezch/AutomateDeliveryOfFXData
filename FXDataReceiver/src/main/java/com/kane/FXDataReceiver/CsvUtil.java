package com.kane.FXDataReceiver;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;

import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvUtil {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

    public static String assembleCSVFilePath(){

        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String currentTime = DateFormatter.format(LocalDateTime.now());
        StringBuilder csvFilePathBuilder = new StringBuilder();
        csvFilePathBuilder.append("/tmp/");
        csvFilePathBuilder.append("obsval_");
        csvFilePathBuilder.append(currentTime);
        csvFilePathBuilder.append(".csv");

        logger.info("Assemble CSV file path:{}", csvFilePathBuilder.toString());
        return csvFilePathBuilder.toString();
    }

    public static void createCSV(JSONObject message, String csvFilePath) throws Exception{

        logger.info("Creat csv: message={}, path={}", message.toString(), csvFilePath);

        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("FOREX", "VALUE"));
        ) {
            Iterator<String> keys = message.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                csvPrinter.printRecord(key, message.get(key));
            }

            csvPrinter.flush();
        }

        logger.info("Creat csv done.");
    }
}
