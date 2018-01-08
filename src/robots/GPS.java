package robots;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GPS extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* config */
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("GPS");
        primaryStage.setScene(new Scene(root, 1000, 600));
        /* eof config */

        //generateWorld();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
