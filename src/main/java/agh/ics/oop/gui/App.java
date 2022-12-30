package agh.ics.oop.gui;

import agh.ics.oop.ConfigurationReader;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

import java.io.FileNotFoundException;

public class App extends Application {
    Scene scene;
    Stage stage;
    Thread thread;

    TextField numOfSimsField;
    File confFile = null;
    File csvDirectory;
    final FileChooser configChooser = new FileChooser();
    final DirectoryChooser csvChooser = new DirectoryChooser();
    public void init(){
        // CONFIG CHOOSER SETTINGS
        configChooser.setTitle("Choose a configuration");
        configChooser.setInitialDirectory(new File("src/main/resources/"));
        configChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );
        // CSV CHOOSER SETTINGS
        csvChooser.setTitle("Choose a configuration");
        csvChooser.setInitialDirectory(new File("src/main/resources/csv_files"));
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Initializer");

        // number of simulations HBox
        Label label = new Label("Number of simulations: ");
        numOfSimsField = new TextField("1");
        numOfSimsField.setPrefWidth(30);
        HBox noSBox = new HBox(label,numOfSimsField);
        noSBox.alignmentProperty().setValue(Pos.CENTER_LEFT);

        // choose configuration HBox
        Label label1 = new Label("Configuration: ");
        TextField confTextField = new TextField("no configuration chosen");
        confTextField.setPrefWidth(400);
        confTextField.setEditable(false);
        confTextField.setFocusTraversable(false);
        final Button confChooseButton = new Button("set configuration");
        confChooseButton.setOnAction((value) -> {
            confFile = configChooser.showOpenDialog(stage);
            if (confFile != null) {
                confTextField.setText(confFile.getPath());
            } else {
                confTextField.setText("no configuration chosen");
            }
        });
        HBox confBox = new HBox(label1,confTextField, confChooseButton);
        confBox.alignmentProperty().setValue(Pos.CENTER_LEFT);

        // csv file HBox
        Label csvLabel = new Label("Export statistics into csv file ");
        TextField csvTextField = new TextField("no directory chosen");
        csvTextField.setPrefWidth(400);
        csvTextField.setEditable(false);
        csvTextField.setFocusTraversable(false);
        csvTextField.setDisable(true);
        final Button csvChooseButton = new Button("set csv file directory");
        csvChooseButton.setDisable(true);
        csvChooseButton.setOnAction((value) -> {
            csvDirectory = csvChooser.showDialog(stage);
            if (csvDirectory != null) {
                csvTextField.setText(csvDirectory.getPath());
            } else {
                csvTextField.setText("no directory chosen");
            }
        });
        CheckBox csvCheckBox = new CheckBox();
        csvCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                csvDirectory = null;
                csvTextField.setText("no directory chosen");
                csvTextField.setDisable(false);
                csvChooseButton.setDisable(false);
            } else {
                csvTextField.setDisable(true);
                csvChooseButton.setDisable(true);
            }
        });
        HBox csvBox = new HBox(csvLabel, csvCheckBox, csvTextField, csvChooseButton);
        csvBox.alignmentProperty().set(Pos.CENTER_LEFT);

        // start button
        Button startButton = new Button("Start simulations");
        startButton.setOnAction((value)-> {
            try {
                startSimulations();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // show scene and stage
        VBox vBox = new VBox(noSBox, confBox, csvBox, startButton);
        scene = new Scene(vBox, 800, 600);
        stage = primaryStage;
        stage.setScene(scene);
        stage.show();
    }

    private void startSimulations() throws IOException {
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

                if (csvDirectory == null) {
                    System.out.println("csvDirectory is null");
                    simulators[i] = new Simulator(configuration,stages[i]);
                } else {
                    String fileName;
                    for (int j = 1; true; j++) {
                        if (!new File(csvDirectory.getPath() + File.separator + j + ".csv").exists()) {
                            fileName = j + ".csv";
                            break;
                        }
                    }
                    File csvFile = new File(csvDirectory.getPath() + File.separator + fileName);
                    csvFile.createNewFile();
                    System.out.print("csvDirectory isn't null ");
                    System.out.println(csvFile.getPath());
                    simulators[i] = new Simulator(configuration,stages[i], csvFile);
                }


                threads[i] = new Thread(simulators[i]);
            }
            for(Thread thread1: threads){
                thread1.run();
            }
        }
    }
}
