package agh.ics.oop.gui;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class App extends Application {
    Scene scene;
    Stage stage;
    Thread thread;
    public void init(){

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Initializer");
        Label label = new Label("Number of simulations: ");
        GridPane.setHalignment(label, HPos.CENTER);
        Button button = new Button("Start");
        TextField textField = new TextField("1");
        HBox noS = new HBox(label,textField);
        Label label1 = new Label("Configuration number: ");
        TextField textField1 = new TextField("1");
        HBox conf = new HBox(label1,textField1);
        VBox vBox = new VBox(noS, conf,button);
        button.setOnAction((value)->{
            String noS1 = textField.getText();
            String conf1 = textField1.getText();
            int n = Integer.valueOf(noS1);
            Thread[] threads = new Thread[n];
            JSONObject configuration;
            try {
                configuration = Simulator.conf(conf1);
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
        });
        scene = new Scene(vBox, 800, 600);
        stage = primaryStage;
        stage.setScene(scene);
        stage.show();
    }

}
