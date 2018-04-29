/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author DAW
 */
public class FXMLMainMenuController implements Initializable {

    @FXML
    private Pane pane1, pane2, pane3, pane4;

//    @FXML
//    private void loadFxml(ActionEvent event) throws IOException {
//        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLEmpleados.fxml"));
//        pane1.getChildren().add(newLoadedPane);
//        pane1.toFront();
//    }

    @FXML
    private Button a, b, bt_personal, bt_clientes;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == a) {
            pane1.toFront();
        } else if (event.getSource() == b) {
            pane2.toFront();
        } else if (event.getSource() == bt_personal) {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLEmpleados.fxml"));
            pane3.getChildren().add(newLoadedPane);
            pane3.toFront();
        } else if (event.getSource() == bt_clientes) {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLClientes.fxml"));
            pane4.getChildren().add(newLoadedPane);
            pane4.toFront();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
