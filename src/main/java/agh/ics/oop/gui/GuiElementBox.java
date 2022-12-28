package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    public Image image;
    public ImageView imageView;
    public GuiElementBox(IMapElement element) throws FileNotFoundException {
        image = new Image(new FileInputStream(element.getPath()));
        imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
    }
    public ImageView getImageView(){
        return imageView;
    }
}
