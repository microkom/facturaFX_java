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
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DAW
 */
public class FXMLMainMenuController implements Initializable {

    private int tipoUsuario;
    private int empleado;

//    @FXML
//    private void loadFxml(ActionEvent event) throws IOException {
//        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLEmpleados.fxml"));
//        pane1.getChildren().add(newLoadedPane);
//        pane1.toFront();
//    }
    @FXML
    private Button btEditarFactura, btProveedores, btEmpleados, bt_clientes, bt_facturacion, 
            bt_productos, bt_categorias;
    @FXML
    private AnchorPane pnLeftPane, pnRightPane;
    
    

//    @FXML
//    private void handleButtonAction(ActionEvent event) throws IOException {
//        if (event.getSource() == btProveedores) {
//            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLProveedores.fxml"));
//            pnProveedores.getChildren().add(newLoadedPane);
//            pnProveedores.toFront();
//        } else if (event.getSource() == btEmpleados) {
//            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLClientes.fxml"));
//            pnClientes.getChildren().add(newLoadedPane);
//            pnClientes.toFront();
//        }else if (event.getSource() == bt_clientes) {
//            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("FXMLBusquedaFactura.fxml"));
//            pnBusquedaFactura.getChildren().add(newLoadedPane);
//            pnBusquedaFactura.toFront();
//        }else if (event.getSource() == bt_facturacion) {
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
        seguridadSegunUsuario();
    }

    public void initVariable(int tipoUsuario, int empleado) {
        this.tipoUsuario = tipoUsuario;
        this.empleado = empleado;
        seguridadSegunUsuario();

    }

    public void seguridadSegunUsuario() {
        switch (tipoUsuario) {
            case 1:
                btEditarFactura.setDisable(false);
                btEmpleados.setDisable(false);
                bt_clientes.setDisable(false);
                bt_facturacion.setDisable(false);
                bt_productos.setDisable(false);
                bt_categorias.setDisable(false);
                btProveedores.setDisable(false);
                break;
            case 2:
                btEditarFactura.setDisable(true);
                btEmpleados.setDisable(true);
                bt_clientes.setDisable(true);
                bt_facturacion.setDisable(true);
                bt_productos.setDisable(true);
                bt_categorias.setDisable(true);
                btProveedores.setDisable(true);
                break;
            case 3:
                btEditarFactura.setDisable(false);
                btEmpleados.setDisable(true);
                bt_clientes.setDisable(true);
                bt_facturacion.setDisable(false);
                bt_productos.setDisable(true);
                bt_categorias.setDisable(true);
                btProveedores.setDisable(true);
                break;
            default:
                break;
        }
    }

    @FXML
    private void productosFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLProductos.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLProductosController controller = fxmlLoader.<FXMLProductosController>getController();
            controller.initVariable(tipoUsuario , empleado);
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
            FXMLCategoriasController controller = fxmlLoader.<FXMLCategoriasController>getController();
            controller.initVariable(tipoUsuario,empleado);
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
    private void empleadosFX(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLEmpleados.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLEmpleadosController controller = fxmlLoader.<FXMLEmpleadosController>getController();
            controller.initVariable(tipoUsuario,empleado);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Empleados::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) btEmpleados.getScene().getWindow();
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
            FXMLProveedoresController controller = fxmlLoader.<FXMLProveedoresController>getController();
            controller.initVariable(tipoUsuario,empleado);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Proveedores::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) btProveedores.getScene().getWindow();
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
            FXMLFacturacionController controller = fxmlLoader.<FXMLFacturacionController>getController();
            controller.initVariable(tipoUsuario,empleado);
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

    @FXML
    private void busquedaFacturaFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLBusquedaFactura.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLBusquedaFacturaController controller = fxmlLoader.<FXMLBusquedaFacturaController>getController();
            controller.initVariable(tipoUsuario,empleado);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..:: Buscar Factura ::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) btEditarFactura.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("busquedaFacturaFX: " + ex.getMessage());
        }
    }

    @FXML
    private void clientexFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLClientes.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLClientesController controller = fxmlLoader.<FXMLClientesController>getController();
            controller.initVariable(tipoUsuario,empleado);
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
