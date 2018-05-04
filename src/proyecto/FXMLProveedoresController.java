/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author German
 */
public class FXMLProveedoresController implements Initializable {

    private String genIdProveedor = "";

    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    private ObservableList<Proveedor> listaProveedor = FXCollections.observableArrayList();
    private FilteredList<Proveedor> listaProveedorFiltrada = new FilteredList(listaProveedor, obj -> true);
    int posicionProveedor = 0;

    @FXML
    private TableView<Proveedor> tablaBusquedaProveedor;
    @FXML
    private TableColumn<Proveedor, String> tCnombreProveedor, tCcontacto, tCcargoContacto, tCdireccion, tCciudad, tCpais, tCtelefono;

    @FXML
    private TextField tfIdProveedor, tfNombreProveedor, tfContactoProveedor, tfCargoContactoProveedor,
            tfDireccionProveedor, tfCiudadProveedor, tfPaisProveedor, tfTelefono, tfBusquedaProveedor;

    //IDs botones de edición
    @FXML
    private Button bt_editarTexto, bt_cancelarEditar, bt_guardarEditar, bt_menuPrincipal;

    //IDs botones nuevo registro
    @FXML
    private Button bt_nuevoProveedor, bt_cancelarNuevoProveedor, bt_borrarRegistro, bt_guardarNuevoRegistro;

    private final ListChangeListener<Proveedor> selectorTablaProveedor = new ListChangeListener<Proveedor>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Proveedor> c) {
            ponerProveedorSeleccionado();
        }
    };

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //PROVEEDORES
            //rellenar lista de proveedores en listado
            Proveedor.fillProveedorList(listaProveedor);

            //lista de proveedores para filtrar
            tablaBusquedaProveedor.setItems(listaProveedorFiltrada);

            //busqueda en tiempo real por nombre, contacto, cargo contacto, ciudad. Tiene en cuenta las tildes 
            tfBusquedaProveedor.setOnKeyReleased(keyEvent -> {
                listaProveedorFiltrada.setPredicate(obj -> obj.getDatosBusqueda().toLowerCase().contains(tfBusquedaProveedor.getText().toLowerCase().trim()));
            });
            //Valores para rellenar la vista de la tabla
            tCnombreProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombre"));
            tCcontacto.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("contacto"));
            tCcargoContacto.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("cargoContacto"));
            tCdireccion.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("direccion"));
            tCciudad.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("ciudad"));
            tCpais.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("pais"));
            tCtelefono.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("telefono"));

            //En el initialize añadimos el «Listener» al TableView
            final ObservableList<Proveedor> tablaProveedorSel
                    = tablaBusquedaProveedor.getSelectionModel().getSelectedItems();
            tablaProveedorSel.addListener(selectorTablaProveedor);

        } catch (Exception ex) {
            System.out.println("Relacionado con Proveedor en el initialize: " + ex.getMessage());
        }

        disableTextFieldEditable();
        estadoInicialBotonesVisibles();

    }

    //Método que devuelve el objeto de la fila seleccionada
    public Proveedor getTablaProveedorSeleccionado() { //de aqui va a los textfields

        Proveedor proveedorSeleccionado = null;
        if (tablaBusquedaProveedor != null) {
            List<Proveedor> tabla = tablaBusquedaProveedor.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                proveedorSeleccionado = tabla.get(0);
                return proveedorSeleccionado;
            }
        }
        return proveedorSeleccionado;
    }

    //Método que a partir del objeto seleccionado lo muestra en el formulario
    //También puede habilitar/deshabilitar botones en el formualrio
    public void ponerProveedorSeleccionado() {
        final Proveedor proveedor = getTablaProveedorSeleccionado();
        posicionProveedor = listaProveedor.indexOf(proveedor);
        if (proveedor != null) {
            tfIdProveedor.setText(proveedor.getId());
            tfNombreProveedor.setText(proveedor.getNombre());
            tfContactoProveedor.setText(proveedor.getContacto());
            tfCargoContactoProveedor.setText(proveedor.getCargoContacto());
            tfDireccionProveedor.setText(proveedor.getDireccion());
            tfCiudadProveedor.setText(proveedor.getCiudad());
            tfPaisProveedor.setText(proveedor.getPais());
            tfTelefono.setText(proveedor.getTelefono());
        }
    }

    @FXML
    private void editarTextoFX() {
        if (validateEmptyField("Debe seleccionar primero un proveedor", tfIdProveedor.getText().isEmpty())) {
            enableTextFieldEditable();
            editarTextoPressed();
        }
    }

    @FXML
    private void cancelarEditarTextoFX() {
        estadoInicialBotonesVisibles();
        disableTextFieldEditable();
        clearForm();
    }

    @FXML
    private void guardarEditarFX() {
        estadoInicialBotonesVisibles();
        guardarEditado();
        actualizaTablaBusqueda();
        disableTextFieldEditable();
    }

    @FXML
    private void actualizaTablaBusqueda() {
        listaProveedor.clear();
        Proveedor.fillProveedorList(listaProveedor);
    }

    @FXML
    private void nuevoProveedorFX() {
        clearForm();
        genIdProveedor = Integer.toString(nuevoNumeroId());
        tfIdProveedor.setText(genIdProveedor);
        enableTextFieldEditable();
        nuevoProveedorPressed();
        actualizaTablaBusqueda();
        //el usuario rellena los datos en este punto
    }

    @FXML
    private void guardarNuevoRegistroFX() {
        if (validateEmptyField("No hay datos para guardar", tfIdProveedor.getText().isEmpty())) {
            if (validateEmptyField("Ingrese el nombre", tfNombreProveedor.getText().isEmpty())) {
                estadoInicialBotonesVisibles();
                guardarNuevoRegistro();
                actualizaTablaBusqueda();
            }
        }
    }

    @FXML
    private void cancelarNuevoProveedorFX() {
        estadoInicialBotonesVisibles();
        clearForm();
        disableTextFieldEditable();
    }

    @FXML
    private void borrarRegistroFX() {
        if (validateEmptyField("Debe seleccionar primero un proveedor", tfIdProveedor.getText().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrado de datos");
            alert.setHeaderText("");
            alert.setContentText("¿Se va a eliminar el registro actual. Seguro que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                borrarRegistro();
                tablaBusquedaProveedor.refresh();//Actualizar la tabla que muestra los registros
                estadoInicialBotonesVisibles();
                System.out.println("OK");
            } else {
                System.out.println("CANCEL");
            }
            estadoInicialBotonesVisibles();
            actualizaTablaBusqueda();
        }
    }

    private void borrarRegistro() {
        PreparedStatement stmt2;

        try {
            stmt2 = con.prepareStatement("DELETE FROM proveedores where idproveedor=?");
            stmt2.setString(1, tfIdProveedor.getText());
            stmt2.executeUpdate();
            stmt2.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private int nuevoNumeroId() {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int numFactura = 0;
        try {
            stmt = con.prepareStatement("SELECT max(IdProveedor) FROM proveedores");
            stmt.executeQuery();
            rs = stmt.executeQuery();
            rs.first();
            numFactura = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("nuevoNumeroId: " + ex.getMessage());
        } finally {
            try {
                stmt.close();
                rs.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, nuevoNumeroId: " + ex.getMessage());
            }
        }
        return numFactura + 1;
    }

    //mostrar formulario en blanco
    private void clearForm() {
        tfIdProveedor.clear();
        tfNombreProveedor.clear();
        tfContactoProveedor.clear();
        tfCargoContactoProveedor.clear();
        tfDireccionProveedor.clear();
        tfCiudadProveedor.clear();
        tfPaisProveedor.clear();
        tfTelefono.clear();
    }

    //Guarda un registro nuevo
    private void guardarNuevoRegistro() {
        PreparedStatement stmt;
        try {
            String id = genIdProveedor;
            String nombre = tfNombreProveedor.getText();
            String contacto = tfContactoProveedor.getText();
            String cargo = tfCargoContactoProveedor.getText();
            String direccion = tfDireccionProveedor.getText();
            String ciudad = tfCiudadProveedor.getText();
            String pais = tfPaisProveedor.getText();
            String telefono = tfTelefono.getText();

            stmt = con.prepareStatement("INSERT INTO proveedores ( idproveedor, nombre, contacto,CargoContacto, direccion, ciudad, pais, telefono) "
                    + " VALUES (\"" + id + "\",\"" + nombre + "\",\"" + contacto + "\",\"" + cargo + "\",\"" + direccion + "\",\"" + ciudad + "\",\"" + pais + "\",\"" + telefono + "\" )");
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registros");
            alert.setHeaderText(null);
            alert.setContentText("Nuevo Proveedor guardado correctamente");
            alert.showAndWait();

            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Guarda lo editado
    private void guardarEditado() {
        if (validateEmptyField("No hay datos para guardar", tfIdProveedor.getText().isEmpty())) {
            PreparedStatement stmt;
            try {
                String id = tfIdProveedor.getText();
                String nombre = tfNombreProveedor.getText();
                String contacto = tfContactoProveedor.getText();
                String cargo = tfCargoContactoProveedor.getText();
                String direccion = tfDireccionProveedor.getText();
                String ciudad = tfCiudadProveedor.getText();
                String pais = tfPaisProveedor.getText();
                String telefono = tfTelefono.getText();
                stmt = con.prepareStatement("UPDATE proveedores SET nombre=\"" + nombre + "\", Contacto=\"" + contacto + "\",cargoContacto=\"" + cargo + "\" ,direccion=\"" + direccion + "\", ciudad=\"" + ciudad + "\",pais=\"" + pais + "\",telefono=\"" + telefono + "\" where idproveedor=? ");
                stmt.setString(1, tfIdProveedor.getText());
                stmt.executeUpdate();
                stmt.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registros");
                alert.setHeaderText(null);
                alert.setContentText("Datos actualizados correctamente");
                alert.showAndWait();

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void enableTextFieldEditable() {
        tfIdProveedor.setEditable(false);// siempre deshabilitado
        tfNombreProveedor.setEditable(true);
        tfContactoProveedor.setEditable(true);
        tfCargoContactoProveedor.setEditable(true);
        tfDireccionProveedor.setEditable(true);
        tfCiudadProveedor.setEditable(true);
        tfPaisProveedor.setEditable(true);
        tfTelefono.setEditable(true);
    }

    private void disableTextFieldEditable() {
        tfIdProveedor.setMouseTransparent(true);// siempre deshabilitado
        tfIdProveedor.setEditable(false);// siempre deshabilitado
        tfNombreProveedor.setEditable(false);
        tfContactoProveedor.setEditable(false);
        tfCargoContactoProveedor.setEditable(false);
        tfDireccionProveedor.setEditable(false);
        tfCiudadProveedor.setEditable(false);
        tfPaisProveedor.setEditable(false);
        tfTelefono.setEditable(false);
    }

    private void editarTextoPressed() {
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(true);
        bt_guardarEditar.setVisible(true);
        bt_nuevoProveedor.setVisible(false);
        bt_cancelarNuevoProveedor.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(false);
    }

    private void nuevoProveedorPressed() {
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoProveedor.setVisible(false);
        bt_cancelarNuevoProveedor.setVisible(true);
        bt_guardarNuevoRegistro.setVisible(true);
        bt_borrarRegistro.setVisible(false);
    }

    private void estadoInicialBotonesVisibles() {
        bt_editarTexto.setVisible(true);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoProveedor.setVisible(true);
        bt_cancelarNuevoProveedor.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(true);
    }

    private boolean validateEmptyField(String text, boolean field) {
        if (field) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Validación de campos");
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    private void menuPrincipalFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMainMenu.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Menú Principal::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_menuPrincipal.getScene().getWindow();
            stage2.close();

        } catch (Exception ex) {
            ex.getMessage();
        }
    }
}
