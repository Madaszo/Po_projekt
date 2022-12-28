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
        System.out.println(confFile.getPath());
        FileReader fileReader = new FileReader(confFile);
        JSONObject o = (JSONObject) parser.parse(fileReader);
        System.out.println(o);
        return o;
    }
}
