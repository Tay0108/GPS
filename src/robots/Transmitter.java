package robots;

public class Transmitter {
    private double x;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    private double y;

    Transmitter(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
