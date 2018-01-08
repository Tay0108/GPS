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
    private int robotsCount = 30; // default, for tests
    private Robot closestRobotA; // robot closest to transmitter A
    private Robot closestRobotB; // B
    private Robot closestRobotC; // C
    private Robot ourRobot;

    @FXML
    private Canvas coordinateSystem;

    private GraphicsContext gc;

    /*
    Łączysz P z wierzchołkami A, B i C. Liczysz pole trójkąta ABC i pola małych trójkątów ABP, BCP i ACP.
    Jeśli suma pól małych trójkątów jest równa polu trójkąta ABC to P jest wewnątrz niego. Uwaga - pamietaj o przyblizeniu, trojkaty pewnie nie beda dokladnie identyczne

     */

    private double triangleArea(double edge1, double edge2, double edge3) {
        double p = (edge1 + edge2 + edge3) / 2; // polowa obwodu
        return Math.sqrt(p * (p - edge1) * (p - edge2) * (p - edge3));
    }

    private boolean isRobotSafe(Robot robot) {
        return true;
    }

    @FXML
    private void generateWorld() {

        // umiesc anteny w trzech losowych miejscach plaszczyzny:
        int minX = 0;
        int minY = 0;
        int maxX = 660;
        int maxY = 480;
        // clear canvas:
        gc.clearRect(minX, minY, maxX, maxY);
        robots.clear();
        //gc.fillRect(minX, minY, maxX, maxY);

        double x = ThreadLocalRandom.current().nextDouble(minX, maxX + 1);
        double y = ThreadLocalRandom.current().nextDouble(minY, maxY + 1);

        transmitterA = new Transmitter(x, y);

        x = ThreadLocalRandom.current().nextDouble(minX, maxX + 1);
        y = ThreadLocalRandom.current().nextDouble(minY, maxY + 1);

        transmitterB = new Transmitter(x, y);

        x = ThreadLocalRandom.current().nextDouble(minX, maxX + 1);
        y = ThreadLocalRandom.current().nextDouble(minY, maxY + 1);

        transmitterC = new Transmitter(x, y);

        // dodaj roboty w losowych miejscach plaszczyzny:
        for (int i = 0; i < robotsCount; i++) {
            x = ThreadLocalRandom.current().nextDouble(minX, maxX + 1);
            y = ThreadLocalRandom.current().nextDouble(minY, maxY + 1);

            double distanceA = Math.sqrt(Math.pow(transmitterA.getX() - x, 2) + Math.pow(transmitterA.getY() - y, 2));
            double distanceB = Math.sqrt(Math.pow(transmitterB.getX() - x, 2) + Math.pow(transmitterB.getY() - y, 2));
            double distanceC = Math.sqrt(Math.pow(transmitterC.getX() - x, 2) + Math.pow(transmitterC.getY() - y, 2));

            robots.add(new Robot(x, y, distanceA, distanceB, distanceC));
        }
        // znajdz roboty najblizej anten i zmien ich typ na roboty antenowe:
        closestRobotA = robots.get(0);
        closestRobotB = robots.get(0);
        closestRobotC = robots.get(0);

        for (Robot current : robots) {
            if (current.getSignalA() > closestRobotA.getSignalA()) {
                closestRobotA = current;
            }
            if (current.getSignalB() > closestRobotB.getSignalB()) {
                closestRobotB = current;
            }
            if (current.getSignalC() > closestRobotC.getSignalC()) {
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
        for (Robot current : robots) {

            // losuje dopoki nie wylosuje jakiegos nietransmitera
            ourRobot = robots.get(ThreadLocalRandom.current().nextInt(0, robots.size()));
            if (ourRobot.getType() == 0) {
                ourRobot.setType(2);
                break;
            }
        }
        // wyswietl stosowna informacje:
        if (isRobotSafe(ourRobot)) {

        }
        drawWorld();
    }

    private void drawWorld() {

        gc.setFill(Color.BLUEVIOLET);
        gc.fillRect(transmitterA.getX(),transmitterA.getY(), 10, 10);
        gc.fillRect(transmitterB.getX(),transmitterB.getY(), 10, 10);
        gc.fillRect(transmitterC.getX(),transmitterC.getY(), 10, 10);
        for (Robot current : robots) {
            if (current.getType() == 0) {
                gc.setFill(Color.GREEN);
            } else if (current.getType() == 1) {
                gc.setFill(Color.RED);
            } else if (current.getType() == 2) {
                gc.setFill(Color.YELLOW);
            }
            gc.fillOval(current.getX(), current.getY(), 8, 8);
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        gc = coordinateSystem.getGraphicsContext2D();
    }

}
