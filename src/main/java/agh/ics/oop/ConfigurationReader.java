package agh.ics.oop;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationReader {
    public  JSONObject readFromFile(File confFile) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader fileReader = new FileReader(confFile);
        return (JSONObject) parser.parse(fileReader);
    }
}
