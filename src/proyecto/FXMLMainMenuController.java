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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DAW
 */
public class FXMLMainMenuController implements Initializable {

//    @FXML
//    private void loadFxml(ActionEvent event) throws IOException {
//        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLEmpleados.fxml"));
//        pane1.getChildren().add(newLoadedPane);
//        pane1.toFront();
//    }
    @FXML
    private Button bt_controlDeGastos, bt_personal, bt_clientes, bt_presupuesto, bt_facturacion, bt_menuPrincipal, bt_productos, bt_categorias;

//    @FXML
//    private void handleButtonAction(ActionEvent event) throws IOException {
//        if (event.getSource() == a) {
//            pane1.toFront();
//        } else if (event.getSource() == b) {
//            pane2.toFront();
//        } else if (event.getSource() == bt_personal) {
//            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLEmpleados.fxml"));
//            pane3.getChildren().add(newLoadedPane);
//            pane3.toFront();
//        } else if (event.getSource() == bt_clientes) {
//            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLClientes.fxml"));
//            pane4.getChildren().add(newLoadedPane);
//            pane4.toFront();
//        }
//    }
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void productosFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLProductos.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Productos::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_productos.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("productosFX: " + ex.getMessage());
        }
    }

    @FXML
    private void categoriasFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLCategorias.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..:: Categorias ::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_categorias.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("categoriasFX: " + ex.getMessage());
        }
    }

    @FXML
    private void personalFX(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLEmpleados.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Empleados::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_personal.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("personalFX: " + ex.getMessage());
        }
    }

    @FXML
    private void proveedoresFX(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLProveedores.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Proveedores::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_controlDeGastos.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("proveedoresFX: " + ex.getMessage());
        }
    }

    @FXML
    private void facturacionFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLFacturacion.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Facturacion::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_facturacion.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("facturacionFX: " + ex.getMessage());
        }
    }

//    @FXML
//    private void menuPrincipalFX() {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMainMenu.fxml"));
//            Parent root1 = (Parent) fxmlLoader.load();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root1));
//
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();
//
//            stage.setTitle("..::Menú Principal::..");
//
//            //cierra la ventana abierta anteriormente
//            Stage stage2 = (Stage) bt_menuPrincipal.getScene().getWindow();
//            stage2.close();
//
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//    }
    @FXML
    private void presupuestoFX(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(""));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Menú Principal::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_menuPrincipal.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("presupuestoFX: " + ex.getMessage());
        }
    }

    @FXML
    private void clientexFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLCustomers.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Clientes::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_clientes.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("clientexFX: " + ex.getMessage());
        }
    }

}
