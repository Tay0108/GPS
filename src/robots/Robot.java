package robots;

public class Robot {
    private double signalA;
    private double signalB;
    private double signalC;
    private int distanceA;
    private int distanceB;
    private int distanceC;
    private int x;
    private int y;

    private int type; // 0 basic 1 'satelite' 2 'our' robot

    Robot(int x, int y, int distanceA, int distanceB, int distanceC) {
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDistanceA() {
        return distanceA;
    }

    public int getDistanceB() {
        return distanceB;
    }

    public int getDistanceC() {
        return distanceC;
    }
}
