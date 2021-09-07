package sample;

public class CartVector {
    private final double x;
    private final double y;

    public CartVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }

    public double getY() { return y; }

    public CartVector addTo(CartVector other) {
        return new CartVector(x + other.getX(), y + other.getY());
    }

    public CartVector subtract(CartVector other) {
        return new CartVector(x - other.getX(), y - other.getY());
    }

    public CartVector scale(double scalar) {
        return new CartVector(x * scalar, y * scalar);
    }

    public String toString() {
        return "( " + x + ", " + y + " )";
    }
}
