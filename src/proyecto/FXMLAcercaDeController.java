/*
finished commenting.
 */
package proyecto;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Clase controladora del archivo FXMLAcercaDe.fxml
 *
 * @author german
 */
public class FXMLAcercaDeController implements Initializable {

    /**
     * AnchorPane que actua como base de la pantalla que se muestra.
     */
    @FXML
    private AnchorPane apAcerca;

    /**
     * Método que carga al iniciar el controlador de la clase.
     *
     * @param url default
     * @param rb default
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cerrar();
    }

    /**
     * Cierra la ventana actual al pulsar la tecla ESC o al hacer click sobre
     * ella.
     */
    private void cerrar() {
        apAcerca.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                Stage stage2 = (Stage) apAcerca.getScene().getWindow();
                stage2.close();
            }
        });
        apAcerca.setOnMouseClicked(e -> {
            Stage stage2 = (Stage) apAcerca.getScene().getWindow();
            stage2.close();

        });
    }
}
