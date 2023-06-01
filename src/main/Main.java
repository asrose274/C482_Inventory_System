package main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Aaron Rose
 * WGU C482
 *
 * javadoc located at C482/src
 *
 * FUTURE ENHANCEMENT
 * Update the inventory level of a part to reflect how many are left after associating it with a Product
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
        stage.setTitle("Inventory System");
        stage.setScene(new Scene(root));
        stage.show();
    }


    public static void main(String [] args){

        launch(args);
    }
}
