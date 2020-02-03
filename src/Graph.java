import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Graph {
  static ArrayList<Vector3> points = new ArrayList<Vector3>();

  public static boolean _drawLines = true;
  public static boolean _drawDots = false;
  public static boolean _drawLabels = true;
  
  static double theta;
  static double phi;
  
  private static Random randGen = new Random();

  public static void drawPlanets(GraphicsContext graphicsContext, ObservableList<Body> planets) {
    double size;
    
    graphicsContext.setFill(Color.rgb(255, 255, 255));
    graphicsContext.fillRect(0, 0, 1000, 700);

    graphicsContext.setFill(Color.rgb(0, 0, 0));
    
    
    for (Body b : planets) 
      b.position = b.getPosition(MainApp.t).getRelative(MainApp.parent.getPosition(MainApp.t));
    
    if (_drawLines) {
      for (Body b : planets) {
        if (b.parent != null) {
          Vector3 pos = b.position;
          Vector3 Pos = b.parent.position;
          
          graphicsContext.strokeLine(pos.x + 500, pos.y + 350, Pos.x + 500, Pos.y + 350);
        }
      }
    }

    if (_drawDots)
      for (Vector3 p : points)
        graphicsContext.fillOval(p.x + 500, p.y + 350, 1, 1);
    
    for (Body b : planets) {
      Vector3 pos = b.position;
      int d = b.getDepth();
      
      size = .05 * Orbit.scalar - (3 * d) - b.position.z/50;
      
      if (_drawLabels) {
        Font f = new Font(12 - (2 * d));
        graphicsContext.setFont(f);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText(b.name, pos.x + 505 - size / 2, pos.y + 340 - size / 2);
      }

      graphicsContext.setFill(Color.BLACK);
      graphicsContext.fillOval(pos.x + 500 - size / 2, pos.y + 350 - size / 2, size+1, size+1);
      graphicsContext.setFill(b.color);
      graphicsContext.fillOval(pos.x + 500 - size / 2, pos.y + 350 - size / 2, size, size);
      
      if (_drawDots)
        points.add(pos);
    }
  }

  public static Color randColor() {
    return Color.rgb(randGen.nextInt(150), randGen.nextInt(150), randGen.nextInt(150));
  }
  
  public static double round(double val, int places){
    if(places < 0) 
      throw new IllegalArgumentException();
    
    BigDecimal bigDecimal = new BigDecimal(val);
    bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }
  
  static Body getParent(String parent, ArrayList<Body> _planets) {
    for (Body b : _planets)
      if (b.name.equals(parent))
        return b;
      
    return null;
  }

  public static Body getParent(String parent2, ObservableList<Body> planets2) {
    for (Body b : planets2)
      if (b.name.equals(parent2))
        return b;
    
    return null;
  }
}
