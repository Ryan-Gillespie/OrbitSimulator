public class Vector3 {
  public static final Vector3 ORIGIN = new Vector3(0, 0, 0);
  
  public double x;
  public double y;
  public double z;
  
  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Vector3 getRelative(Vector3 p) {
    return new Vector3(this.x-p.x, this.y-p.y, this.z-p.z);
  }
  
  public double getDistance(Vector3 other) {
    return Math.sqrt(Math.pow(x-other.x, 2) + Math.pow(y-other.y, 2) + Math.pow(z-other.z, 2));
  }
  
  public Vector3 addTo(Vector3 other) {
    return new Vector3(x+other.x, y+other.y, z+other.z);
  }
  
  public Vector3 scalar(double scalar) {
    return new Vector3(x*scalar, y*scalar, z*scalar);
  }
  
  public Vector3 toCartesian() {
    double x1 = (x * Math.sin(y) * Math.cos(z));
    double y1 = (x * Math.cos(y));
    double z1 = (x * Math.sin(y) * Math.sin(z));
    
    this.x = x1;
    this.y = y1;
    this.z = z1;
    
    return this;
  }
  
  public Vector3 toPolar() {
    double r = Math.sqrt(x*x + y*y + z*z);
    double theta = Math.acos(z/r);
    double phi = Math.atan(y/x);
    
    this.x = r;
    this.y = theta;
    this.z = phi;
    
    return this;
  }
  
  public String toString() {
    return "( " + x + ", " + y + ", " + z + " )";
  }
}
