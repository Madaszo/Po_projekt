package agh.ics.oop.gui;

import agh.ics.oop.ConfigurationReader;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class App extends Application {
    Scene scene;
    Stage stage;
    Thread thread;

    TextField numOfSimsField;
    File confFile = null;
    final FileChooser fileChooser = new FileChooser();
    public void init(){
        // FILE CHOOSER SETTINGS
        fileChooser.setTitle("Choose a configuration");
        fileChooser.setInitialDirectory(new File("src/main/resources/"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Initializer");

        // first HBox
        Label label = new Label("Number of simulations: ");
        numOfSimsField = new TextField("1");
        numOfSimsField.setPrefWidth(30);
        HBox noSBox = new HBox(label,numOfSimsField);
        noSBox.alignmentProperty().setValue(Pos.CENTER_LEFT);

        // second HBox
        Label label1 = new Label("Configuration: ");
        TextField confTextField = new TextField("no configuration chosen");
        confTextField.setPrefWidth(400);
        confTextField.setEditable(false);
        confTextField.setFocusTraversable(false);
        final Button chooseConfButton = new Button("set path");
        chooseConfButton.setOnAction((value) -> {
            confFile = fileChooser.showOpenDialog(stage);
            confTextField.setText(confFile.getPath());
            // code below is for debugging purposes only
            if (confFile != null) {
                confTextField.setText(confFile.getPath());
                System.out.println(confFile.getPath());
            } else {
                confTextField.setText("no configuration chosen");
                System.out.println("null");
            }
        });
        HBox confBox = new HBox(label1,confTextField, chooseConfButton);
        confBox.alignmentProperty().setValue(Pos.CENTER_LEFT);

        // start button
        Button startButton = new Button("Start simulations");
        startButton.setOnAction((value)->startSimulations());

        // show scene and stage
        VBox vBox = new VBox(noSBox, confBox, startButton);
        scene = new Scene(vBox, 800, 600);
        stage = primaryStage;
        stage.setScene(scene);
        stage.show();
    }

    private void startSimulations() {
        {
            int n;
            try {
                n = Integer.parseInt(numOfSimsField.getText());
            } catch (Exception e) {
                throw new RuntimeException("Incorrect value for 'Number of simulations' field");
            }
            Thread[] threads = new Thread[n];
            JSONObject configuration;
            try {
                configuration = new ConfigurationReader().readFromFile(confFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Simulator[] simulators = new Simulator[n];
            Stage[] stages = new Stage[n];
            for(int i = 0; i < n; i++){
                stages[i] = new Stage();
                simulators[i] = new Simulator(configuration,stages[i]);
                threads[i] = new Thread(simulators[i]);
            }
            for(Thread thread1: threads){
                thread1.run();
            }
        }
    }
}
