
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {
  public static ArrayList<Vector3> points;
  public static ObservableList<Body> planets = FXCollections.observableArrayList();
  
  private Stage primaryStage;
  private Pane canvasPane = new Pane();
  private Canvas canvas = new Canvas(1000, 700);
  private GraphicsContext gCon = canvas.getGraphicsContext2D();
  
  static TabPane Menu;
  static AnchorPane Main;
  
  public static Body parent;
  public static int parentIndex;
  public static int currentParent;
  public static double timeScale = 1000;
  public static long t = 0;
  
  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Solar System Simulator");

    showUI();
    
    new AnimationTimer() {
      @Override
      public void handle(long now) {
        Graph.drawPlanets(gCon, planets);
        t += 1 * timeScale;
      }
    }.start();
  }

  public static void main(String[] args) {
    launch(args);
  }

  public static void addPlanet(Body planet) {
    planets.add(planet);
  }
  
  public ObservableList<Body> getPlanets() {
    return planets;
  }

  /**
   * Shows the person overview inside the root layout.
   */
  public void showUI() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("Main.fxml"));
      Main = (AnchorPane) loader.load();
      
      loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("ControlsTab.fxml"));
      Menu = (TabPane) ((AnchorPane) loader.load()).getChildren().get(0);
      
      SplitPane _main = ((SplitPane) Main.getChildren().get(0));
      
      _main.getItems().add(0, canvasPane);
      _main.getItems().add(1, Menu);
      _main.getItems().remove(2);
      _main.getItems().remove(2);
      _main.setDividerPosition(0, .79);
      
      canvasPane.getChildren().add(canvas);
      
      loadPlanets();
      
      Graph.drawPlanets(gCon, planets);
      // Show the scene containing the root layout.
      Scene scene = new Scene(Main);
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static ObservableList<Body> loadSystem(String fileName) {
    planets.clear();
    try { planets = FileManager.readObs(fileName, planets); }
    catch(Exception e) { e.printStackTrace(); }
    
    return planets;
  }
  
  public static Body getPlanetFromString(String name) {
    for (Body b : planets)
      if (b.name.equals(name))
        return b;
    return null;
  }
  
  public static void loadPlanets() {
    planets.clear();
    planets.add(new Body("Sun", 300000));
    for (Planet p : Planet.values())
      planets.add(new Orbit(p, Graph.getParent(p.parent, planets)));

    try { FileManager.save("SolarSystem.txt", planets); }
    catch(Exception e) { e.printStackTrace(); }
    
    planets = loadSystem("SolarSystem.txt");
    parent = planets.get(0);
  }
  
  public static void newSystem() {
    planets.clear();
    planets.add(new Body("Sun", 300000));
  }
}
