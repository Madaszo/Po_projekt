package agh.ics.oop.gui;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
public class GuiElementBox {
    public static Map<String,Image> resources(String path) throws FileNotFoundException {
        Map<String,Image> w = new HashMap<String,Image>();
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null){
            for (File child : directoryListing){
                String getPath = child.getPath();
                System.out.println(getPath);
                Image image = new Image(new FileInputStream(getPath));
                w.put(getPath,image);
            }
        }
        return w;
    }
}
