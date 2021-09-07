package sample;

public class OrbitingBody {
    private final static double G = 6.67408E-11;   // gravitational constant

    private final String name;
    private final double mass;
    private final double a;
    private final double e;
    private final OrbitingBody parent;

    private final double period;
    private CartVector curPos;

    /**
     * Constructor for OrbitingBody
     * @param name is the name of the orbiting body (i.e. "Earth")
     * @param mass is the mass in Earth Mass units
     * @param a is the semi-major axis in Astronomical Units
     * @param e is the Eccentricity of the orbit
     * @param parent is the body this body is orbiting around
     */
    public OrbitingBody(String name, double a, double e, double mass, OrbitingBody parent) {
        this.name = name;
        this.a = a;
        this.e = e;
        this.mass = mass;
        this.parent = parent;

        curPos = new CartVector(0, 0);
        this.period = Math.pow(a, 1.5);
    }

    /**
     * Constructor for the Center of the solar system (usually a star)
     * @param name Name of the star
     * @param mass Mass of the star in Earth Mass units
     */
    public OrbitingBody(String name, double mass) {
        this.name = name;
        this.mass = mass;
        this.a = 0;
        this.e = 0;
        this.parent = null;

        curPos = new CartVector(0, 0);
        this.period = 0;
    }

    // Getter and Setter for the name
    public String getName() { return name; }

    // Getter and Setter for the mass
    public double getMass() { return mass; }

    // Getter and Setter for a
    public double getA() { return a; }

    // Getter and Setter for e
    public double getE() { return e; }

    // Getter and Setter for Parent
    public OrbitingBody getParent() { return parent; }

    // Getter for period
    public double getPeriod() { return period; }

    /**
     * Calculates the mean anomoly for the current time t
     * @param t is the current time
     * @return the mean anomoly used to calculate the eccentric anomoly
     */
    private double getMeanAnomoly(double t) {
        return (2 * Math.PI * t) / getPeriod();
    }

    /**
     * Approximation used to calculate the angle of the planet.
     * @param t is the current time 
     * @return the eccentric anomoly used to calculate the true anomoly
     */
    private double getEccentricAnomoly(double t) {
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
     * Gets the angle at which to draw the planet
     * @param t is the current time
     * @return the angle corresponding to the current time
     */
    private double getTrueAnomoly(double t) {
        double E = getEccentricAnomoly(t);
        return 2 * Math.atan(Math.sqrt((1 + this.e) / (1 - this.e)) * Math.tan(E / 2));
    }

    /**
     * Updates the position of the planet for easy access multiple times later.
     * Call this once then use the getPosition() method to access the positions later.
     * @param t is the current time.
     */
    public void updatePosition(double t) {
        if (parent == null) {
            curPos = new CartVector(0, 0);
            return;
        }

        double theta = getTrueAnomoly(t);
        double r =  a * (1 - e * Math.cos(theta)) / (1 + (e * Math.cos(theta)));
        curPos = new PolarVector(r, theta).toCart();
    }

    /**
     * Gets the current position in constant time
     * @return A Cartesian Vector containing the current position
     */
    public CartVector getPosition() {
        return curPos;
    }

    /**
     * Gets the depth for name label display purposes
     * @return the number of parents this can reach except for the star
     */
    public int getDepth() {
        if (parent == null)
            return 0;
        return 1 + parent.getDepth();
    }
}
