/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author German
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLFacturacion.fxml"));

           // Scene scene = new Scene(root, Color.rgb(0, 0, 0, 0.5));//login
            Scene scene = new Scene(root, Color.rgb(0, 0, 0, 0.5));
            
           // stage.initStyle(StageStyle.TRANSPARENT); //hace que se vea sin botones arriba - login
            stage.setTitle("Tienda");
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            System.out.println("Start page: " + ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
