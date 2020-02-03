import javafx.collections.ObservableList;

/**
 * Keeps track of the data associated with an orbit.
 * 
 * @author Ryan
 */
public class Orbit extends Body{
  public static final double y2s = 3.154e+7;    // years to seconds conversion
  public static final double Au2m = 1.496e+11;  // Astronomical units to meters conversion
  public static final double E2kg = 5.97e+24;   // Earth masses to kg conversion
  private final static double G = 6.67408E-11;   // gravitational constant
  
  public static double scalar = 150;
  
  public static Vector3 position;

  private double k;      // G*M
  public double period; // orbital period
  
  public Orbit(Planet planet, Body parent) {
    super(planet.name(), planet.mass);
    this.a = planet.a * Au2m;
    this.e = planet.e;
    this.i = planet.i;
    this.phi = planet.phi;
    this.parent = parent;
    this.k = G * this.parent.mass;
    this.period = planet.period;
    this.parent.addChild(this);
  }
  
  public Orbit(String name, double a, double e, double mass, double i, double phi, String parent, ObservableList<Body> planets) {
    super(name, mass);
    this.a = a * Au2m;
    this.e = e;
    this.i = i;
    this.phi = phi;
    this.parent = Graph.getParent(parent, planets);
    this.k = G * this.parent.mass;
    this.period = Math.pow(a, 1.5) * Math.pow(this.getDepth(), 9.5);
    this.parent.addChild(this);
  }
  
  /**
   * Returns the current velocity of the object in this orbit at the given radius r.
   * 
   * @param t is the current time to get the velocity in.
   * @return the current velocity at the radius r.
   */
  public double getVelocity(double t) {
    double r = getRadius(t);
    return Math.sqrt(k*((2/r) - (1/this.a)));
  }

  /**
   * Gets the current radius of the orbit given the true anomaly.
   * 
   * @param t is the time at which to measure the current radius
   * @return the current radius of the orbit.
   */
  public double getRadius(double t) {
    double theta = getTrueAnomaly(t);
    return (this.a * (1 - this.e * Math.cos(theta))) / (1 + (this.e * Math.cos(theta)));
  }
  
  /**
   * Get's the period of the current orbit, returns 0 if the orbit is parabolic or hyperbolic
   * (neither of those orbits technically have a period).
   * 
   * @return the period of the current orbit
   */
  public double getPeriod() {
    return this.period * y2s;
  }
  
  /**
   * Get's the time elapsed since periapse from the given radius r.
   * 
   * @param r is the current radius.
   * @return the time elapsed since periapse.
   */
  public double getTimeFromPeriapse(double r) {
    return (getMeanAnomoly(r) * getPeriod()) / (2 * Math.PI);
  }
  
  /**
   * Get's the eccentric anomaly at the given time t.
   * 
   * @param t is the given time in seconds.
   * @return the eccentric anomaly.
   */
  public double getEccentricAnomoly(double t) {
    double tempResult = getMeanAnomoly(t);
    double numerator;

    for (int i = 0; i < 10; i++) {
        numerator = getMeanAnomoly(t) + this.e * Math.sin(tempResult)
                - this.e * tempResult * Math.cos(tempResult);
        tempResult = numerator / (1 - this.e * Math.cos(tempResult));
    }
    return tempResult;
  }
  
  /**
   * Get's the mean anomaly at the given time t.
   * 
   * @param t is the given time in seconds.
   * @return the mean anomaly at time t.
   */
  public double getMeanAnomoly(double t) {
    return (2 * Math.PI * t) / getPeriod();
  }
  
  /**
   * Get's the current true anomaly given the time of measurement. (the current angle)
   * 
   * @param t is the given time in seconds
   * @return the true anomaly of the orbit.
   */
  public double getTrueAnomaly(double t) {
    double E = getEccentricAnomoly(t);
    return 2 * Math.atan(Math.sqrt((1 + this.e) / (1 - this.e)) * Math.tan(E / 2));
  }
   
  
  /**
   * Gets the cartesian coordinates of the current orbital position with the given time t
   * 
   * @param t is the time at which to get the position
   * @return a Vector3 of the position
   */
  @Override
  public Vector3 getPosition(double t) {
    double theta = getTrueAnomaly(t);
    double r = (getRadius(theta) / Au2m) * scalar;
    
    if (this.isMoon())
      r *= 1 + getDepth() + scalar * .25;
    
    return new Vector3(r, theta + this.phi + Graph.theta, this.i + Graph.phi).toCartesian().addTo(this.parent.getPosition(t));
  }
}
