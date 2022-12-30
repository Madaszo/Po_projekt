package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.rules.DeterministicGenomeExecutioner;
import agh.ics.oop.rules.FullRandomMutationer;
import agh.ics.oop.rules.GlobeConstraint;
import agh.ics.oop.rules.GreenEquator;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Simulator implements EngineObserver, Runnable {
    JSONObject config;
    Stage stage;
    Scene scene;
    WorldMap map;
    SimulationEngine SE;
    MapStats mapStats;
    Thread simEngineThread;
    GridPane gridPane;
    Map<String, Image> gub = new HashMap<String,Image>();
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    Vector2d[] greenerGrass;

    // statistics fields
    final Label simulationTime = new Label("");
    final Label numOfAnimals = new Label("");
    final Label numOfPlants = new Label("");
    final Label numOfFreeTiles = new Label("");
    final Label mostPopularGenome = new Label("");
    final Label mostPopularGenomeNum = new Label("");
    final Label averageEnergyLevel = new Label("");
    final Label averageLifespan = new Label("");
    XYChart.Series<Number, Number> animalSeries = new XYChart.Series<>();
    XYChart.Series<Number, Number> grassSeries = new XYChart.Series<>();


    static int tileWH = 15;
    Simulator(JSONObject conf, Stage stage){
        this.stage = stage;
        this.config = conf;
    }

    public void updateScene(SimulationEngine SE) {
        Platform.runLater(()->{
            updateGrid();
            updateStatistics();
        });
    }

    private void updateGrid() {
        gridPane.getChildren().removeAll(imageViews);
        imageViews.clear();
        Label label = new Label("Y\\X");
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.add(label,0,0);

            Vector2d ur = new Vector2d(map.getWidth(),map.getHeight());
            for(int i = 0; i < map.getWidth();i++){
                for(int j = 0; j < map.getHeight(); j++){
                    Vector2d v2 = new Vector2d(i,j);


                if(map.animalsAt(v2) != null && map.animalsAt(v2).size()>1){
                    ImageView im = new ImageView(gub.get("src\\main\\resources\\images\\love.png"));
                    im.setFitHeight(tileWH);
                    im.setFitWidth(tileWH);
                    imageViews.add(im);
                    gridPane.add(im,i+1,ur.y-j);
                }else if(map.animalsAt(v2) != null && map.animalsAt(v2).size()==1){
                    ImageView im = new ImageView(gub.get(map.animalsAt(v2).get(0).getPath()));
                    im.setFitHeight(tileWH);
                    im.setFitWidth(tileWH);
                    imageViews.add(im);
                    gridPane.add(im,i+1,ur.y-j);
                }else if (map.getGrass(v2)!=null) {
                    ImageView im = new ImageView(gub.get(map.getGrass(v2).getPath()));
                    im.setFitHeight(tileWH);
                    im.setFitWidth(tileWH);
                    imageViews.add(im);
                    gridPane.add(im,i+1,ur.y-j);
                }
            }
        }
    }

    private void updateStatistics() {
        simulationTime.setText(String.valueOf(mapStats.getSimulationTime()));
        numOfAnimals.setText(String.valueOf(mapStats.getNumOfAnimals()));
        numOfPlants.setText(String.valueOf(mapStats.getNumOfGrasses()));
        numOfFreeTiles.setText(String.valueOf(mapStats.getFreeTilesNum()));
        mostPopularGenome.setText(Arrays.toString(mapStats.getMostPopularGenome()));
        mostPopularGenomeNum.setText(String.valueOf(mapStats.getMostPopularGenomeNum()));
        averageEnergyLevel.setText(String.format("%.2f", mapStats.getAvgEnergy()));
        averageLifespan.setText(String.format("%.2f", mapStats.getAvgLifespan()));
        animalSeries.getData().add(new XYChart.Data<>(mapStats.getSimulationTime(), mapStats.getNumOfAnimals()));
        grassSeries.getData().add(new XYChart.Data<>(mapStats.getSimulationTime(), mapStats.getNumOfGrasses()));
    }


    @Override
    public void run() {
        stage.setTitle("Simulator");
        try {
            gub = GuiElementBox.resources("src/main/resources/images");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            Long animals = (Long) config.get("startingAnimals");
            map = new WorldMap(w.intValue(),h.intValue(), s.intValue(), g.intValue(),e.intValue(),l.intValue(),
                    st.intValue(),f.intValue(),
                    n.intValue(), min.intValue(),
                    max.intValue(),new GreenEquator(), new FullRandomMutationer(),
                    new DeterministicGenomeExecutioner(),
                    new GlobeConstraint(w.intValue(),h.intValue()));
            map.randomAnimals(animals.intValue());
            greenerGrass = map.grassSpawner.greenerGrass(map);

            // GRID INITIALIZATION
            gridPane = new GridPane();
            gridPane.getColumnConstraints().add(new ColumnConstraints(tileWH*2));
            gridPane.getRowConstraints().add(new RowConstraints(tileWH*2));
            for(int i = 0; i < map.getWidth();i++){
                gridPane.getColumnConstraints().add(new ColumnConstraints(tileWH));
                Label label1 = new Label(Integer.toString(i));
                GridPane.setHalignment(label1, HPos.CENTER);
                gridPane.add(label1,i+1,0);
            }
            for(int i = 0; i < map.getHeight();i++){
                gridPane.getRowConstraints().add(new RowConstraints(tileWH));
                Label label1 = new Label(Integer.toString(i));
                GridPane.setHalignment(label1, HPos.CENTER);
                gridPane.add(label1,0,map.getHeight()-i);
            }
            for(int i = 0; i < map.getWidth();i++) {
                for (int j = 0; j < map.getHeight(); j++) {
                    Vector2d v2 = new Vector2d(i,j);
                    if(v2.between(greenerGrass)) {
                        Pane pane = new Pane();
                        pane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 128, 0),
                                new CornerRadii(0), new Insets(0))));
                        gridPane.add(pane, i + 1, map.getHeight() - j);
                    }else {
                        Pane pane = new Pane();
                        pane.setBackground(new Background(new BackgroundFill(Color.rgb(152, 251, 152),
                                new CornerRadii(0), new Insets(0))));
                        gridPane.add(pane, i + 1, map.getHeight() - j);
                    }
                }
            }
            ScrollPane scrollPane = new ScrollPane(gridPane);
            double scrollPaneEdge = Math.min(Screen.getPrimary().getBounds().getHeight()*0.8,
                    Screen.getPrimary().getBounds().getWidth()*0.8);
            scrollPane.setPrefSize(scrollPaneEdge ,scrollPaneEdge);

            // CHART INITIALIZATION
            final NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Day of simulation");
            final NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Population");
            final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Grass and animal population over time");
            lineChart.setCreateSymbols(false);
            animalSeries.setName("Animals population");
            lineChart.getData().add(animalSeries);
            grassSeries.setName("Number of grass patches");
            lineChart.getData().add(grassSeries);

            // STATISTICS INITIALIZATION
            VBox statsBox = new VBox(new Label("Simulation statistics:"),
                    new HBox(new Label("Days of simulation: "), simulationTime),
                    new HBox(new Label("Num. of animals: "), numOfAnimals),
                    new HBox(new Label("Num. of plants: "), numOfPlants),
                    new HBox(new Label("Num. of free tiles: "), numOfFreeTiles),
                    new HBox(new Label("The most popular genome "), mostPopularGenome, new Label(" has "), mostPopularGenomeNum, new Label(" representatives")),
                    new HBox(new Label("Average energy level: "), averageEnergyLevel),
                    new HBox(new Label("Average lifespan: "), averageLifespan),
                    lineChart);

            HBox hBox = new HBox(statsBox, scrollPane);
            Group root = new Group(hBox);
            scene = new Scene(root);

            SE = new SimulationEngine(map,this);
            mapStats = SE.mapStats;
            stage.setScene(scene);
            stage.show();
            System.out.println(map);
            updateScene(SE);
            simEngineThread = new Thread(SE);
            simEngineThread.start();

    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
