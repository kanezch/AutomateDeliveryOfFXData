package com.kane.FXDataReceiver;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by
 *
 * @author kane
 * @date 1/12/19
 */
public class CsvUtilTest {

    @Test
    public void When_givenCurrentTime_Expect_CSVFilePath() {
        String expectedFilePath = "/tmp/obsval_20191202_1000.csv";

        String currentTime = "20191202_1000";
        String path = CsvUtil.assembleCSVFilePath(currentTime);

        Assert.assertEquals(expectedFilePath, path);
    }

    @Test
    public void When_createCSVSuccessful_Expect_True() throws Exception{
        String path = "src/test/obsval_20191202_1000.csv";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("AUDUSD", 0.65);
        boolean result = CsvUtil.createCSV(jsonObject, path);
        Assert.assertTrue(result);

        Reader reader = Files.newBufferedReader(Paths.get(path));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        JSONObject readJsonObject = new JSONObject();
        for (CSVRecord csvRecord:csvParser){
            readJsonObject.put(csvRecord.get(0), csvRecord.get(1));
        }
        System.out.println(readJsonObject.toString());
        Assert.assertTrue(readJsonObject.toString().contains("\"AUDUSD\":\"0.65\""));
    }
}