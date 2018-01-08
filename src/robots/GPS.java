package robots;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GPS extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* config */
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        stage.setResizable(false);
        stage.setTitle("GPS");
        stage.setScene(new Scene(root, 1000, 600));
        /* eof config */
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
