/*
finished commenting.
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
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Clase controladora del archivo FXMLMainMenu.fxml
 *
 * @author German Navarro
 */
public class FXMLMainMenuController implements Initializable {

    /**
     * Variable de clase privada: número que identifica al tipo de usuario
     * conectado.
     */
    private int tipoUsuario;

    /**
     * Variable de clase privada: número que identifica al empleado.
     */
    private int empleado;

    @FXML
    private Button btEditarFactura, btProveedores, btEmpleados, bt_clientes, bt_facturacion,
            bt_productos, bt_categorias, btSalir;
    @FXML
    private AnchorPane apMainAnchorPane;
    @FXML
    private MenuItem menuItemSalir, menuItemIVA, menuItemAcercaDe;

    /**
     * Método que existe por defecto. NO USADO.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Método que carga al iniciar el controlador de la clase.
     *
     * @param tipoUsuario Identifica el tipo de usuario.
     * @param empleado Identifica al empleado.
     */
    public void initVariable(int tipoUsuario, int empleado) {
        this.tipoUsuario = tipoUsuario;
        this.empleado = empleado;
        seguridadSegunUsuario();

    }

    /**
     * Comprueba el tipo de usuario para aplicar la seguridad.
     */
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
                bt_clientes.setDisable(false);
                bt_facturacion.setDisable(false);
                bt_productos.setDisable(true);
                bt_categorias.setDisable(true);
                btProveedores.setDisable(true);
                break;
            default:
                break;
        }
    }

    /**
     * Cierra la aplicación.
     */
    @FXML
    private void salirFX() {
        Stage stage = (Stage) apMainAnchorPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Abre la ventana para mostrar información de acerca de la aplicación.
     */
    @FXML
    private void acercaDeFX() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLAcercaDe.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root1, Color.rgb(0, 0, 0, 0.1)));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..:: Acerca De ::..");
            stage.show();

        } catch (IOException ex) {
            System.out.println("productosFX: " + ex.getMessage());
        }

    }

    /**
     * Abre la ventana para agregar, modificar, o borrar Productos.
     */
    @FXML
    private void productosFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLProductos.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLProductosController controller = fxmlLoader.<FXMLProductosController>getController();
            controller.initVariable(tipoUsuario, empleado);
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

    /**
     * Abre la ventana para modificar el IVA.
     */
    @FXML
    private void ivaFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLIVA.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLIVAController controller = fxmlLoader.<FXMLIVAController>getController();
            controller.initVariable(tipoUsuario);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..:: IVA ::..");
            stage.show();

        } catch (IOException ex) {
            System.out.println("ivaFX: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana para agregar, modificar, o borrar Categorías.
     */
    @FXML
    private void categoriasFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLCategorias.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLCategoriasController controller = fxmlLoader.<FXMLCategoriasController>getController();
            controller.initVariable(tipoUsuario, empleado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..:: Categorias ::..");
            stage.show();

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_categorias.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("categoriasFX: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana para agregar, modificar, o borrar Empleados.
     */
    @FXML
    private void empleadosFX(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLEmpleados.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLEmpleadosController controller = fxmlLoader.<FXMLEmpleadosController>getController();
            controller.initVariable(tipoUsuario, empleado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..::Empleados::..");
            stage.show();

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) btEmpleados.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("personalFX: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana para agregar, modificar, o borrar Proveedores.
     */
    @FXML
    private void proveedoresFX(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLProveedores.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLProveedoresController controller = fxmlLoader.<FXMLProveedoresController>getController();
            controller.initVariable(tipoUsuario, empleado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..::Proveedores::..");
            stage.show();

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) btProveedores.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("proveedoresFX: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana para crear facturas.
     */
    @FXML
    private void facturacionFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLFacturacion.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLFacturacionController controller = fxmlLoader.<FXMLFacturacionController>getController();
            controller.initVariable(tipoUsuario, empleado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..::Facturacion::..");
            stage.show();

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_facturacion.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("facturacionFX: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana para buscar o editar facturas.
     */
    @FXML
    private void busquedaFacturaFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLBusquedaFactura.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLBusquedaFacturaController controller = fxmlLoader.<FXMLBusquedaFacturaController>getController();
            controller.initVariable(tipoUsuario, empleado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..:: Buscar Factura ::..");
            stage.show();

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) btEditarFactura.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("busquedaFacturaFX: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana para agregar, modificar, o borrar Clientes.
     */
    @FXML
    private void clientexFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLClientes.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLClientesController controller = fxmlLoader.<FXMLClientesController>getController();
            controller.initVariable(tipoUsuario, empleado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..::Clientes::..");
            stage.show();

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_clientes.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            System.out.println("clientexFX: " + ex.getMessage());
        }
    }
}
