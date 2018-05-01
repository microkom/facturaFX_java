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
public class FXMLProductosController implements Initializable {

    private String genIdProducto = "";
    private ResultSet rs;
    private ResultSet rs2;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private FilteredList<Producto> listaProductosFiltrada = new FilteredList(listaProductos, obj -> true);
    int posicionProducto = 0;

    @FXML
    private TableView<Producto> tablaBusquedaProducto;
    @FXML
    private TableColumn<Producto, String> tCidProducto, tCnombreProducto, tCproveedor, tCcategoria, tCPrecio, tCexistencias;

    @FXML
    private TextField tfIdProducto, tfNombreProducto, tfProveedor, tfCategoria, tfPrecio, tfExistencias, tfBusquedaProductos;

    //IDs botones de edición
    @FXML
    private Button bt_editarTexto, bt_cancelarEditar, bt_guardarEditar;

    //IDs botones nuevo registro
    @FXML
    private Button bt_nuevoProducto, bt_cancelarNuevoProducto, bt_borrarRegistro, bt_guardarNuevoRegistro, bt_menuPrincipal;

    private final ListChangeListener<Producto> selectorTablaProductos = new ListChangeListener<Producto>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Producto> c) {
            ponerProductoSeleccionado();
        }
    };

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //PRODUCTOS
            //rellenar lista de productos en listado
            Producto.fillProductosList(listaProductos);

            //lista de productos para filtrar
            tablaBusquedaProducto.setItems(listaProductosFiltrada);

            //busqueda en tiempo real por nombre, contacto, cargo contacto, ciudad. Tiene en cuenta las tildes 
            tfBusquedaProductos.setOnKeyReleased(keyEvent -> {
                listaProductosFiltrada.setPredicate(obj -> 
                        obj.getDatosBusqueda().toLowerCase().contains(
                                tfBusquedaProductos.getText().toLowerCase().trim()));
            });
            //Valores para rellenar la vista de la tabla
            tCidProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("id"));
            tCnombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
            tCproveedor.setCellValueFactory(new PropertyValueFactory<Producto, String>("proveedor"));
            tCcategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("categoria"));
            tCPrecio.setCellValueFactory(new PropertyValueFactory<Producto, String>("precio"));
            tCexistencias.setCellValueFactory(new PropertyValueFactory<Producto, String>("existencias"));

            //En el initialize añadimos el «Listener» al TableView
            final ObservableList<Producto> tablaProductoSel
                    = tablaBusquedaProducto.getSelectionModel().getSelectedItems();
            tablaProductoSel.addListener(selectorTablaProductos);

        } catch (Exception ex) {
            System.out.println("Relacionado con Productos en el initialize: " + ex.getMessage());
        }

        disableTextFieldEditable();
        estadoInicialBotonesVisibles();

    }

    //Método que devuelve el objeto de la fila seleccionada
    public Producto getTablaProductosSeleccionado() { //de aqui va a los textfields

        Producto productoSeleccionado = null;
        if (tablaBusquedaProducto != null) {
            List<Producto> tabla = tablaBusquedaProducto.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                productoSeleccionado = tabla.get(0);
                return productoSeleccionado;
            }
        }
        return productoSeleccionado;
    }

    //Método que a partir del objeto seleccionado lo muestra en el formulario
    //También puede habilitar/deshabilitar botones en el formualrio
    public void ponerProductoSeleccionado() {
        final Producto producto = getTablaProductosSeleccionado();
        posicionProducto = listaProductos.indexOf(producto);
        if (producto != null) {
            tfIdProducto.setText(Integer.toString(producto.getId()));
            tfNombreProducto.setText(producto.getNombreProducto());
            tfProveedor.setText("***productos***");
            tfCategoria.setText("***productos**");
            tfPrecio.setText(Double.toString(producto.getPrecio()));
            tfExistencias.setText(Integer.toString(producto.getExistencias()));
        }
    }

    @FXML
    private void editarTextoFX() {
        if (validateEmptyField("Debe seleccionar primero un producto", tfIdProducto.getText().isEmpty())) {
            enableTextFieldEditable();
            editarTextoPressed();
        }
    }

    @FXML
    private void cancelarEditarTextoFX() {
        estadoInicialBotonesVisibles();
    }

    @FXML
    private void guardarEditarFX() {
        estadoInicialBotonesVisibles();
        guardarEditado();
        actualizaTablaBusqueda();
    }

    @FXML
    private void actualizaTablaBusqueda() {
        listaProductos.clear();
        Producto.fillProductosList(listaProductos);
    }

    @FXML
    private void nuevoProductoFX() {
        clearForm();
        genIdProducto = Integer.toString(nuevoNumeroIdProudcto());
        tfIdProducto.setText(genIdProducto);
        enableTextFieldEditable();
        nuevoProductoPressed();
        actualizaTablaBusqueda();
        //el usuario rellena los datos en este punto
    }

    @FXML
    private void guardarNuevoRegistroFX() {
        if (validateEmptyField("No hay datos para guardar", tfIdProducto.getText().isEmpty())) {
            if (validateEmptyField("Ingrese el nombre", tfNombreProducto.getText().isEmpty())) {
                estadoInicialBotonesVisibles();
                guardarNuevoRegistro();
                actualizaTablaBusqueda();
            }
        }
    }

    @FXML
    private void cancelarNuevoProductoFX() {
        estadoInicialBotonesVisibles();
        clearForm();
    }

    @FXML
    private void borrarRegistroFX() {
        if (validateEmptyField("Debe seleccionar primero un producto", tfIdProducto.getText().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrado de datos");
            alert.setHeaderText("");
            alert.setContentText("¿Se va a eliminar el registro actual. Seguro que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                borrarRegistro();
                tablaBusquedaProducto.refresh();//Actualizar la tabla que muestra los registros
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
            stmt2 = con.prepareStatement("DELETE FROM productos where idproducto=?");
            stmt2.setString(1, tfIdProducto.getText());
            stmt2.executeUpdate();
            stmt2.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //mostrar formulario en blanco
    private void clearForm() {
        tfIdProducto.clear();
        tfNombreProducto.clear();
        tfProveedor.clear();
        tfCategoria.clear();
        tfPrecio.clear();
        tfExistencias.clear();
    }

    //Guarda un registro nuevo
    private void guardarNuevoRegistro() {
        PreparedStatement stmt;
        try {
            String id = Integer.toString(nuevoNumeroIdProudcto());
            String nombre = tfNombreProducto.getText();
            String proveedor = tfProveedor.getText();
            String categoria = tfCategoria.getText();
            String precio = tfPrecio.getText();
            String existencias = tfExistencias.getText();

            stmt = con.prepareStatement("INSERT INTO productos ( idproducto, "
                    + "nomProducto, proveedor, categoria, precio, existencias) "
                    + " VALUES (\"" + id + "\",\"" + nombre + "\",\"" + proveedor 
                    + "\",\"" + categoria + "\",\"" + precio + "\",\"" + existencias + "\" )");
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registros");
            alert.setHeaderText(null);
            alert.setContentText("Nuevo Producto guardado correctamente");
            alert.showAndWait();

            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
//Sacar el último número de la factura de la base de datos

    private int nuevoNumeroIdProudcto() {

        ResultSet rs = null;
        PreparedStatement stmt = null;
        int idProducto = 0;
        try {
            stmt = con.prepareStatement("SELECT max(idproducto) FROM productos");
            stmt.executeQuery();
            rs = stmt.executeQuery();
            rs.first();
            idProducto = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("fillProductosList: " + ex.getMessage());
        } finally {
            try {
                stmt.close();
                rs.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, nuevoNumeroIdProudcto: " + ex.getMessage());
            }
        }
        genIdProducto = Integer.toString(idProducto + 1);

        return idProducto + 1;
    }

    //Guarda lo editado
    private void guardarEditado() {
        if (validateEmptyField("No hay datos para guardar", tfIdProducto.getText().isEmpty())) {
            PreparedStatement stmt;
            try {
                String id = tfIdProducto.getText();
                String nombre = tfNombreProducto.getText();
                String proveedor = tfProveedor.getText();
                String categoria = tfCategoria.getText();
                String precio = tfPrecio.getText();
                String existencias = tfExistencias.getText();

                stmt = con.prepareStatement("UPDATE productos SET nomProducto=\"" + nombre
                        + "\", Proveedor=\"" + proveedor + "\",categoria=\"" + categoria
                        + "\" ,precio=\"" + precio + "\", existencias=\"" + existencias
                        + "\" where idproducto=? ");
                stmt.setString(1, tfIdProducto.getText());
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
        tfIdProducto.setEditable(false);// siempre deshabilitado
        tfNombreProducto.setEditable(true);
        tfProveedor.setEditable(true);
        tfCategoria.setEditable(true);
        tfPrecio.setEditable(true);
        tfExistencias.setEditable(true);
    }

    private void disableTextFieldEditable() {
        tfIdProducto.setEditable(false);// siempre deshabilitado
        tfNombreProducto.setEditable(false);
        tfProveedor.setEditable(false);
        tfCategoria.setEditable(false);
        tfPrecio.setEditable(false);
        tfExistencias.setEditable(false);
    }

    private void editarTextoPressed() {
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(true);
        bt_guardarEditar.setVisible(true);
        bt_nuevoProducto.setVisible(false);
        bt_cancelarNuevoProducto.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(false);
    }

    private void nuevoProductoPressed() {
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoProducto.setVisible(false);
        bt_cancelarNuevoProducto.setVisible(true);
        bt_guardarNuevoRegistro.setVisible(true);
        bt_borrarRegistro.setVisible(false);
    }

    private void estadoInicialBotonesVisibles() {
        bt_editarTexto.setVisible(true);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoProducto.setVisible(true);
        bt_cancelarNuevoProducto.setVisible(false);
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
    private void menuPrincipalFX(){
        try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMainMenu.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));

                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();

                    stage.setTitle("..::Menú Principal::..");

                    //cierra la ventana abierta anteriormente
                    Stage stage3 = (Stage) bt_menuPrincipal.getScene().getWindow();
                    stage3.close();

                } catch (Exception ex) {
                    ex.getMessage();
                }
    }
}
