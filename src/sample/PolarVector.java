package sample;

public class PolarVector {
    private final double r;
    private final double theta;

    public PolarVector(double r, double theta) {
        this.r = r;
        this.theta = theta;
    }

    /**
     * Getter for the radius value.
     * @return the radius
     */
    public double getR() { return r; }

    /**
     * Getter for the angle theta.
     * @return the angle theta
     */
    public double getTheta() { return theta; }

    /**
     * Gets this vector in cartesian form.
     * @return Cartesian vector with the same corrdinates as this vector
     */
    public CartVector toCart() {
        double x = Math.cos(theta) * r;
        double y = Math.sin(theta) * r;
        return new CartVector(x, y);
    }

    /**
     * Converts this vector to string for debugging purposes
     */
    public String toString() {
        return "( " + r + ", " + theta + " )";
    }
}
