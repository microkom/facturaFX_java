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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Clase Main
 * 
 * @author German
 */
public class Main extends Application {

    /**
     * Clase Main. Llama a la ventana de Login.
     * 
     * @param stage 
     */
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));

            Scene scene = new Scene(root, Color.rgb(0, 0, 0, 0.5));//login

            stage.initStyle(StageStyle.TRANSPARENT); //hace que se vea sin botones arriba - login
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.getIcons().add(new Image("s_supermarket.png"));

            stage.show();
        } catch (IOException ex) {
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
