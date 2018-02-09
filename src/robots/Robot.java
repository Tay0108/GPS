package robots;

public class Robot {
    private double signalA;
    private double signalB;
    private double signalC;
    private double distanceA;
    private double distanceB;
    private double distanceC;
    private double x;
    private double y;

    private int type; // 0 basic 1 'satelite' 2 'our' robot

    Robot(double x, double y, double distanceA, double distanceB, double distanceC) {
        this.distanceA = distanceA;
        this.distanceB = distanceB;
        this.distanceC = distanceC;
        this.signalA = 1 / distanceA;
        this.signalB = 1 / distanceB;
        this.signalC = 1 / distanceC;
        this.x = x;
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getSignalA() {
        return signalA;
    }

    public double getSignalB() {
        return signalB;
    }

    public double getSignalC() {
        return signalC;
    }

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

    public double getDistanceA() {
        return distanceA;
    }

    public double getDistanceB() {
        return distanceB;
    }

    public double getDistanceC() {
        return distanceC;
    }
}
