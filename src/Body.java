import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Body {
  protected String name;
  protected double mass;
  public Body parent;
  public Color color;
  public Vector3 position;
  protected ArrayList<Body> children;
  
  public double a;      // semi-major axis
  public double e;      // eccentricity
  public double i;      // inclination
  public double phi;    // longitude of periapse
  
  public Body(String name, double mass) {
    this.name = name;
    this.mass = mass;
    this.parent = null;
    this.color = Graph.randColor();
    this.position = Vector3.ORIGIN;
    this.children = new ArrayList<Body>();
    this.a = 0;
    this.e = 0;
    this.i = 0;
    this.phi = 0;
  }
  
  public Vector3 getPosition(double t) { return Vector3.ORIGIN; }
  
  public double getPeriod() { return 0; }
  
  public void addChild(Body child) { this.children.add(child); }
  
  public void remove() {
    for (Body child : this.children)
      child.parent = this.parent;
    
    MainApp.planets.remove(this);
  }
  
  public ArrayList<Body> getChildren() { return this.children; }
  
  public boolean isMoon() { return getDepth() > 1; }
  
  public int getDepth() {
    int i = 0;
    Body tmpParent = this.parent;
    while (tmpParent != null) {
      tmpParent = tmpParent.parent;
      i++;
    }
    return i;
  }
  
  public void setName(String name) { this.name = name; }
  
  public void setMass(double mass) { this.mass = mass; }
  
  public void setA(double a) { this.a = a; }
  
  public void setE(double e) { this.e = e; }
  
  public void setI(double i) { this.i = i; }
  
  public void setPhi(double phi) { this.phi = phi; }
  
  public String toString() { return this.name; }
}