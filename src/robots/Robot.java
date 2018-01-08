package robots;

public class Robot {
    private double signalA;
    private double signalB;
    private double signalC;

    int type; // 0 basic 1 'satelite' 2 'our' robot

    Robot(double distanceA, double distanceB, double distanceC) {
        this.signalA = 1 / distanceA;
        this.signalB = 1 / distanceB;
        this.signalC = 1 / distanceC;
    }

    boolean isSafe() { // checking if this robot is within triangle
        return true;
    }
}
