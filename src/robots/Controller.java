package robots;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class Controller implements Initializable {

    private Stage stage;
    private ArrayList<Robot> robots = new ArrayList<>();
    private Transmitter transmitterA;
    private Transmitter transmitterB;
    private Transmitter transmitterC;
    private Robot closestRobotA; // robot closest to transmitter A
    private Robot closestRobotB; // B
    private Robot closestRobotC; // C
    private Robot ourRobot;

    private int robotsCount = 500; // default, for tests
    private int robotRadius = 8;
    private int transmitterRadius = 10;

    @FXML
    private Canvas coordinateSystem;

    private GraphicsContext gc;

    /*
    Łączysz P z wierzchołkami A, B i C. Liczysz pole trójkąta ABC i pola małych trójkątów ABP, BCP i ACP.
    Jeśli suma pól małych trójkątów jest równa polu trójkąta ABC to P jest wewnątrz niego. Uwaga - pamietaj o przyblizeniu,
    trojkaty pewnie nie beda dokladnie identyczne

     */

    private int triangleArea(int edge1, int edge2, int edge3) {
        double p = (edge1 + edge2 + edge3) / 2; // polowa obwodu
        return (int) Math.sqrt(p * (p - edge1) * (p - edge2) * (p - edge3));
    }

    private boolean isRobotSafe(Robot robot) {
        int AB = (int) Math.sqrt(Math.pow(transmitterA.getX() - transmitterB.getX(), 2) + Math.pow(transmitterA.getY() - transmitterB.getY(), 2));
        int AC = (int) Math.sqrt(Math.pow(transmitterA.getX() - transmitterC.getX(), 2) + Math.pow(transmitterA.getY() - transmitterC.getY(), 2));
        int BC = (int) Math.sqrt(Math.pow(transmitterB.getX() - transmitterC.getX(), 2) + Math.pow(transmitterB.getY() - transmitterC.getY(), 2));
        int PA = (int) Math.sqrt(Math.pow(robot.getX() - transmitterA.getX(), 2) + Math.pow(robot.getY() - transmitterA.getY(), 2));
        int PB = (int) Math.sqrt(Math.pow(robot.getX() - transmitterB.getX(), 2) + Math.pow(robot.getY() - transmitterB.getY(), 2));
        int PC = (int) Math.sqrt(Math.pow(robot.getX() - transmitterC.getX(), 2) + Math.pow(robot.getY() - transmitterC.getY(), 2));
        int realTriangleArea = triangleArea(AB, AC, BC);

        int smallTriangle1 = triangleArea(AB, PA, PB);
        int smallTriangle2 = triangleArea(AC, PA, PC);
        int smallTriangle3 = triangleArea(BC, PB, PC);

        return Math.abs(realTriangleArea - (smallTriangle1 + smallTriangle2 + smallTriangle3)) < 100;
    }

    private boolean isRobotRelativelySafe(Robot ourRobot) {
        // usrednianie bokow
        int AB = (closestRobotA.getDistanceB() + closestRobotB.getDistanceA()) / 2;
        int AC = (closestRobotA.getDistanceC() + closestRobotC.getDistanceA()) / 2;
        int BC = (closestRobotB.getDistanceC() + closestRobotC.getDistanceB()) / 2;

        int bigTriangle = triangleArea(AB, AC, BC); // may be problematic
        int smallTriangle1 = triangleArea(ourRobot.getDistanceA(), ourRobot.getDistanceB(), AB);
        int smallTriangle2 = triangleArea(ourRobot.getDistanceA(), ourRobot.getDistanceC(), AC);
        int smallTriangle3 = triangleArea(ourRobot.getDistanceB(), ourRobot.getDistanceC(), BC);
        System.out.println(bigTriangle);
        System.out.println(smallTriangle1);
        System.out.println(smallTriangle2);
        System.out.println(smallTriangle3);
        return Math.abs(bigTriangle - (smallTriangle1 + smallTriangle2 + smallTriangle3)) < 0.1 * bigTriangle; // tolerancja
    }

    @FXML
    private void generateWorld() {
        // umiesc anteny w trzech losowych miejscach plaszczyzny:

        int minX = 0;
        int minY = 0;
        int maxX = 660;
        int maxY = 480;

        // clear canvas and list:
        gc.clearRect(minX, minY, maxX, maxY);
        robots.clear();

        int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        int y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);

        transmitterA = new Transmitter(x, y);

        x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);

        transmitterB = new Transmitter(x, y);

        x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);

        transmitterC = new Transmitter(x, y);

        // dodaj roboty w losowych miejscach plaszczyzny:
        for (int i = 0; i < robotsCount; i++) {
            x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
            y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);

            int distanceA = (int) Math.sqrt(Math.pow(transmitterA.getX() - x, 2) + Math.pow(transmitterA.getY() - y, 2));
            int distanceB = (int) Math.sqrt(Math.pow(transmitterB.getX() - x, 2) + Math.pow(transmitterB.getY() - y, 2));
            int distanceC = (int) Math.sqrt(Math.pow(transmitterC.getX() - x, 2) + Math.pow(transmitterC.getY() - y, 2));

            robots.add(new Robot(x, y, distanceA, distanceB, distanceC));
        }
        // znajdz roboty najblizej anten i zmien ich typ na roboty antenowe:
        closestRobotA = robots.get(0);
        closestRobotB = robots.get(0);
        closestRobotC = robots.get(0);

        for (Robot current : robots) {
            if (current.getDistanceA() < closestRobotA.getDistanceA()) {
                closestRobotA = current;
            }
            if (current.getDistanceB() < closestRobotB.getDistanceB()) {
                closestRobotB = current;
            }
            if (current.getDistanceC() < closestRobotC.getDistanceC()) {
                closestRobotC = current;
            }
        }

        closestRobotA.setType(1); // changing to 'satelite'
        closestRobotB.setType(1);
        closestRobotC.setType(1);

        // jesli nie ma trzech robotow, tylko ktorys jest najblizej dwoch badz trzech wierzcholkow:
        if (closestRobotA == closestRobotB && closestRobotB == closestRobotC) { // kolko jak jest jeden punkt

        }

        // z pozostalych robotow (WHERE type = 0) wybierz losowo jednego i sprawdz czy jest bezpieczny:
        // losuje dopoki nie wylosuje jakiegos nietransmitera

        while (true) {
            ourRobot = robots.get(ThreadLocalRandom.current().nextInt(0, robots.size()));
            if (ourRobot.getType() == 0) {
                ourRobot.setType(2);
                break;
            }
        }
        // wyswietl stosowna informacje:
        //if (isRobotSafe(ourRobot)) {
        //    System.out.println("Robot jest bezpieczny.");
        //}
        if (isRobotRelativelySafe(ourRobot)) {
            if (isRobotSafe(ourRobot)) {
                System.out.println("Robot jest bezpieczny.");
            } else {
                System.out.println("Robot jest względnie bezpieczny, ale tak naprawdę to nie.");
            }
        } else {
            System.out.println("Robot jest w niebezpieczeństwie.");
        }
        drawWorld();
    }


    private void drawWorld() {

        gc.setFill(Color.BLUEVIOLET);
        gc.fillOval(transmitterA.getX() - transmitterRadius / 2, transmitterA.getY() - transmitterRadius / 2, transmitterRadius, transmitterRadius);
        gc.fillOval(transmitterB.getX() - transmitterRadius / 2, transmitterB.getY() - transmitterRadius / 2, transmitterRadius, transmitterRadius);
        gc.fillOval(transmitterC.getX() - transmitterRadius / 2, transmitterC.getY() - transmitterRadius / 2, transmitterRadius, transmitterRadius);
        gc.setStroke(Color.BLUEVIOLET);
        gc.strokeLine(transmitterA.getX(), transmitterA.getY(), transmitterB.getX(), transmitterB.getY());
        gc.strokeLine(transmitterA.getX(), transmitterA.getY(), transmitterC.getX(), transmitterC.getY());
        gc.strokeLine(transmitterB.getX(), transmitterB.getY(), transmitterC.getX(), transmitterC.getY());

        for (Robot current : robots) {
            if (current.getType() == 0) {
                gc.setFill(Color.GREEN);
            } else if (current.getType() == 1) {
                gc.setFill(Color.RED);
            } else if (current.getType() == 2) {
                gc.setFill(Color.YELLOW);
            }

            gc.fillOval(current.getX() - robotRadius / 2, current.getY() - robotRadius / 2, robotRadius, robotRadius);
        }

        gc.setStroke(Color.RED);
        gc.strokeLine(closestRobotA.getX(), closestRobotA.getY(), closestRobotB.getX(), closestRobotB.getY());
        gc.strokeLine(closestRobotA.getX(), closestRobotA.getY(), closestRobotC.getX(), closestRobotC.getY());
        gc.strokeLine(closestRobotB.getX(), closestRobotB.getY(), closestRobotC.getX(), closestRobotC.getY());
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        gc = coordinateSystem.getGraphicsContext2D();
    }

}
