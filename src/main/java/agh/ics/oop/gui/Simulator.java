package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.rules.DeterministicGenomeExecutioner;
import agh.ics.oop.rules.FullRandomMutationer;
import agh.ics.oop.rules.GlobeConstraint;
import agh.ics.oop.rules.GreenEquator;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;

public class Simulator implements EngineObserver, Runnable {
    JSONObject config;
    Stage stage;
    Scene scene;
    WorldMap map;
    GridPane gridPane;
    static int tileWH = 15;
    Simulator(JSONObject conf, Stage stage){
        this.stage = stage;
        this.config = conf;
    }

    public void updateScene(SimulationEngine SE) {
        Platform.runLater(()->{
            gridPane.getChildren().clear();
            Label label = new Label("Y\\X");
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.add(label,0,0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(tileWH*2));
            gridPane.getRowConstraints().add(new RowConstraints(tileWH*2));

            Vector2d ur = new Vector2d(map.getWidth(),map.getHeight());
            for(int i = 0; i <= ur.x;i++){
                Label label1 = new Label(Integer.toString(i));
                GridPane.setHalignment(label1, HPos.CENTER);
                gridPane.add(label1,i+1,0);
                gridPane.getColumnConstraints().add(new ColumnConstraints(tileWH));
            }
            for(int i = 0; i <= ur.y;i++){
                Label label1 = new Label(Integer.toString(i));
                GridPane.setHalignment(label1, HPos.CENTER);
                gridPane.add(label1,0,ur.y-i+1);
                gridPane.getRowConstraints().add(new RowConstraints(tileWH));
            }
            for(Animal animal: SE.animals){
                GuiElementBox gub;
                Vector2d position = animal.getPosition();
                try {
                    gub = new GuiElementBox(animal);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                gridPane.add(gub.getImageView(),position.x,ur.y-position.y+1);
            }
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

            // gridPane and scrollPane
            gridPane = new GridPane();
            ScrollPane scrollPane = new ScrollPane(gridPane);
            double scrollPaneEdge = Math.min(Screen.getPrimary().getBounds().getHeight()*0.8,
                    Screen.getPrimary().getBounds().getWidth()*0.8);
            scrollPane.setPrefSize(scrollPaneEdge ,scrollPaneEdge);

            // todo statistics
            // statistics
            TextField textField = new TextField("Field for test, maybe statistics should be there?");
            Group statistics = new Group(textField);
            HBox hBox = new HBox(statistics, scrollPane);
            Group root = new Group(hBox);
            scene = new Scene(root);

            SimulationEngine SE = new SimulationEngine(map,this);
            stage.setScene(scene);
            stage.show();
            updateScene(SE);

    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
