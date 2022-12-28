package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.rules.DeterministicGenomeExecutioner;
import agh.ics.oop.rules.FullRandomMutationer;
import agh.ics.oop.rules.GlobeConstraint;
import agh.ics.oop.rules.GreenEquator;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;

public class Simulator implements EngineObserver, Runnable {
    JSONObject config;
    Stage stage;
    Scene scene;
    WorldMap map;
    GridPane gridPane;
    Simulator(JSONObject conf, Stage stage){
        this.stage = stage;
        this.config = conf;
    }

    public void updateScene(SimulationEngine SE) {
        Platform.runLater(()->{
            gridPane.getChildren().clear();
            Label label = new Label("Y\\X");
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.add(label,0,0,1,1);
            Vector2d ur = new Vector2d(map.getWidth(),map.getHeight());
            for(int i = 0; i <= ur.x;i++){
                Label label1 =new Label(Integer.toString(i));
                GridPane.setHalignment(label1, HPos.CENTER);
                gridPane.add(label1,i+1,0,1,1);
            }
            for(int i = 0; i <= ur.y;i++){
                Label label1 = new Label(Integer.toString(i));
                GridPane.setHalignment(label1, HPos.CENTER);
                gridPane.add(label1,0,ur.y-i+1,1,1);
            }
            for(Animal animal: SE.animals){
                GuiElementBox gub;
                Vector2d position = animal.getPosition();
                try {
                    gub = new GuiElementBox(animal);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                gridPane.add(gub.getImageView(),position.x,ur.y-position.y+1,1,1);
                gridPane.getColumnConstraints().add(new ColumnConstraints(50));
                gridPane.getRowConstraints().add(new RowConstraints(50));
            }
            gridPane.getColumnConstraints().add(new ColumnConstraints(50));
            gridPane.getRowConstraints().add(new RowConstraints(50));
            gridPane.setGridLinesVisible(true);
        });
    }


    @Override
    public void run() {
        stage.setTitle("Simulator");
        Scene scene;
        try {
            Long w = (Long) config.get("width");
            Long h = (Long) config.get("height");
            Long s = (Long) config.get("startingGrass");
            Long g = (Long) config.get("grassGain");
            Long e = (Long) config.get("energyGain");
            Long l = (Long) config.get("genomeLength");
            Long st = (Long) config.get("startingEnergy");
            Long f = (Long) config.get("fedAnimal");
            Long n = (Long) config.get("neededEnergy");
            Long min = (Long) config.get("minimalMutation");
            Long max = (Long) config.get("maximumMutation");
            map = new WorldMap(w.intValue(),h.intValue(), s.intValue(), g.intValue(),e.intValue(),l.intValue(),
                    st.intValue(),f.intValue(),
                    n.intValue(), min.intValue(),
                    max.intValue(),new GreenEquator(), new FullRandomMutationer(),
                    new DeterministicGenomeExecutioner(),
                    new GlobeConstraint(w.intValue(),h.intValue()));
            gridPane = new GridPane();
            scene = new Scene(gridPane,800,800);
            SimulationEngine SE = new SimulationEngine(map,this);
            stage.setScene(scene);
            stage.show();
            updateScene(SE);

    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
