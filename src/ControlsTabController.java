import java.io.FileNotFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class ControlsTabController {
  static String selected;
  private boolean alreadyInit = false;
  
  @FXML protected Slider panSlider;
  @FXML protected Slider tiltSlider;
  @FXML protected Slider zoomSlider;
  @FXML protected Slider timeSlider;
  
  @FXML public static Label vText;
  @FXML public static Label rText;
  @FXML public static Label mText;
  
  @FXML public ComboBox<String> StatPlanetList;
  @FXML protected TextField NameBox;
  @FXML protected TextField MBox;
  @FXML protected TextField aBox;
  @FXML protected TextField eBox;
  @FXML protected TextField iBox;
  @FXML protected TextField thetaBox;
  @FXML protected TextField fileBox; 
  
  @FXML public ComboBox<String> planetList;
  
  @FXML protected void newSystemHandle(ActionEvent event) {
    MainApp.newSystem();
  }
  
  @FXML protected void saveAsHandle(ActionEvent event) {
    try {
      FileManager.save(fileBox.getText(), MainApp.planets);
    } catch(FileNotFoundException e) { }
  }
  
  @FXML protected void loadHandle(ActionEvent event) {
    MainApp.planets = FileManager.readObs(fileBox.getText(), MainApp.planets);
  }
  
  @FXML protected void setNameHandle(ActionEvent event) {
    MainApp.getPlanetFromString(selected).setName(NameBox.getText());
    initialize();
    selected = NameBox.getText();
    StatPlanetList.setValue(NameBox.getText());
  }
  
  @FXML protected void setMassHandle(ActionEvent event) {
    MainApp.getPlanetFromString(selected).setMass(Double.parseDouble(MBox.getText()) * Orbit.E2kg);
  }
  
  @FXML protected void setAHandle(ActionEvent event) {
    MainApp.getPlanetFromString(selected).setA(Double.parseDouble(aBox.getText()) * Orbit.Au2m);
  }
  
  @FXML protected void setEHandle(ActionEvent event) {
    MainApp.getPlanetFromString(selected).setE(Double.parseDouble(eBox.getText()));
  }
  
  @FXML protected void setIHandle(ActionEvent event) {
    MainApp.getPlanetFromString(selected).setI(Math.toRadians(Double.parseDouble(iBox.getText())));
  }
  
  @FXML protected void setPhiHandle(ActionEvent event) {
    MainApp.getPlanetFromString(selected).setPhi(Math.toRadians(Double.parseDouble(thetaBox.getText())));
  }
  
  @FXML protected void newPlanetHandle(ActionEvent event) {
    String name = NameBox.getText();
    double mass = Double.parseDouble(MBox.getText());
    double a = Double.parseDouble(aBox.getText());
    double e = Double.parseDouble(eBox.getText());
    double i = Math.toRadians(Double.parseDouble(iBox.getText()));
    double phi = Math.toRadians(Double.parseDouble(thetaBox.getText()));
    MainApp.planets.add(new Orbit(name, a, e, mass, i, phi, "Sun", MainApp.planets));
    initialize();
    selected = name;
    StatPlanetList.setValue(name);
  }
  
  @FXML protected void removePlanetHandle(ActionEvent event) {
    MainApp.getPlanetFromString(selected).remove();
    initialize();
  }
  
  @FXML protected void toggleLinesHandle(ActionEvent event) {
    Graph._drawLines = !Graph._drawLines;
  }
  
  @FXML protected void toggleLabelsHandle(ActionEvent event) {
    Graph._drawLabels = !Graph._drawLabels;
  }
  
  @FXML protected void toggleTrailsHandle(ActionEvent event) {
    Graph.points.clear();
    Graph._drawDots = !Graph._drawDots;
  }
  
  @FXML protected void setNewParentHandle(ActionEvent event) {
    
  }
  
  public void initialize() {
    if (!alreadyInit)
      MainApp.loadPlanets();
    
    planetList.getItems().clear();
    for (Body b : MainApp.planets) 
      planetList.getItems().add(b.name);
    planetList.setValue("Sun");
    planetList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      public void changed(ObservableValue<? extends String> selected, String old, String _new) {
        MainApp.parent = MainApp.getPlanetFromString(_new);
      }
    });
    
    StatPlanetList.getItems().clear();
    for (Body b : MainApp.planets) 
      StatPlanetList.getItems().add(b.name);
    StatPlanetList.setValue("Earth");
    selected = "Earth";
    StatPlanetList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      public void changed(ObservableValue<? extends String> _selected, String _old, String _new) {
        selected = _new;
      }
    });
    
    panSlider.valueProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
        Graph.theta = Math.toRadians((double) new_val);
      }
    });
    
    tiltSlider.valueProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
        Graph.phi = Math.toRadians((double) new_val);
      }
    });
    
    zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
        Orbit.scalar = (double) new_val;
      }
    });
    
    timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
        MainApp.timeScale = (double) new_val;
      }
    });
    
    alreadyInit = true;
  }
}
