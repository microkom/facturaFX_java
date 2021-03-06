/*
finished commenting.
 */
package proyecto;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase controladora del archivo FXMLProductos.fxml
 *
 * @author German Navarro
 */
public class FXMLProductosController implements Initializable {

    /**
     * Variable de clase privada: número que identifica al tipo de usuario
     * conectado.
     */
    private int tipoUsuario;

    /**
     * Variable de clase privada: número que identifica al empleado.
     */
    private int empleado;

    /**
     * Variable de clase privada: número que identifica al producto.
     */
    private String genIdProducto = "";

    private ResultSet rs;
    private ResultSet rs2;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private ObservableList<Categoria> listaCategoria = FXCollections.observableArrayList();
    private ObservableList<Proveedor> listaProveedor = FXCollections.observableArrayList();
    private FilteredList<Producto> listaProductosFiltrada = new FilteredList(listaProductos, obj -> true);
    int posicionProducto = 0;

    @FXML
    private TableView<Producto> tablaBusquedaProducto;
    @FXML
    private TableColumn<Producto, String> tCidProducto, tCnombreProducto, tCproveedor, tCcategoria,
            tCPrecio, tCexistencias;

    @FXML
    private TextField tfIdProducto, tfNombreProducto, tfPrecio, tfExistencias, tfBusquedaProductos;

    @FXML
    private Button bt_editarTexto, bt_cancelarEditar, bt_guardarEditar, bt_nuevoProducto,
            bt_cancelarNuevoProducto, bt_borrarRegistro, bt_guardarNuevoRegistro, bt_menuPrincipal;

    @FXML
    private ComboBox cb_categorias, cb_proveedores;

    private final ListChangeListener<Producto> selectorTablaProductos = new ListChangeListener<Producto>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Producto> c) {
            ponerProductoSeleccionado();
        }
    };

    /**
     * Método que existe por defecto en la clase. NO USADO.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Método que se ejecuta al iniciar el controlador de la clase.
     *
     * @param tipoUsuario número que identifica al tipo de usuario.
     * @param empleado número que identifica al empleado.
     */
    public void initVariable(int tipoUsuario, int empleado) {
        this.tipoUsuario = tipoUsuario;
        this.empleado = empleado;
        try {
            //PRODUCTOS
            //rellenar lista de productos en listado
            Proveedor.fillProveedorList(listaProveedor);
            Categoria.fillCategoriaList(listaCategoria);
            Producto.fillProductosList(listaProductos);

            //lista de productos para filtrar
            tablaBusquedaProducto.setItems(listaProductosFiltrada);

            //busqueda en tiempo real por nombre, contacto, cargo contacto, ciudad. Tiene en cuenta las tildes 
            tfBusquedaProductos.setOnKeyReleased(keyEvent -> {
                listaProductosFiltrada.setPredicate(obj
                        -> obj.getDatosBusqueda().toLowerCase().contains(tfBusquedaProductos.getText().toLowerCase().trim()));
            });
            //Valores para rellenar la vista de la tabla
            tCidProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("id"));
            tCnombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
            tCproveedor.setCellValueFactory(new PropertyValueFactory<Producto, String>("nomProveedor"));
            tCcategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("nomCategoria"));
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

    /**
     * Método, que se cuando realiza una búsqueda, captura el objeto
     * seleccionado
     *
     * @return Un objeto del tipo Producto que ha sido seleccionado.
     */
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

    /**
     * Método, que cuando se realiza una búsqueda, muestra en el formulario el
     * objeto seleccionado.
     */
    public void ponerProductoSeleccionado() {
        final Producto producto = getTablaProductosSeleccionado();
        posicionProducto = listaProductos.indexOf(producto);
        if (producto != null) {

            tfIdProducto.setText(Integer.toString(producto.getId()));
            tfNombreProducto.setText(producto.getNombreProducto());
            cb_proveedores.setItems(listaProveedor);
            cb_proveedores.setValue(producto.getNomProveedor());
            cb_proveedores.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> System.out.println(newValue));

            cb_categorias.setItems(listaCategoria);
            cb_categorias.setValue(producto.getNomCategoria());
            tfPrecio.setText(Double.toString(producto.getPrecio()));
            tfExistencias.setText(Integer.toString(producto.getExistencias()));
        }
    }

    /**
     * Activa los campos para editar el producto seleccionado.
     */
    @FXML
    private void editarTextoFX() {
        if (validateEmptyField("Debe seleccionar primero un producto", tfIdProducto.getText().isEmpty())) {
            enableTextFieldEditable();
            editarTextoPressed();
        }
    }

    /**
     * Cancela la edición deshabilitando los campos editables.
     */
    @FXML
    private void cancelarEditarTextoFX() {
        estadoInicialBotonesVisibles();
    }

    /**
     * Guarda los campos editados en la base de datos.
     */
    @FXML
    private void guardarEditarFX() {
        estadoInicialBotonesVisibles();
        guardarEditado();
        actualizaTablaBusqueda();
    }

    /**
     * Método que actualiza la tabla que muestra la búsqueda realizada.
     */
    @FXML
    private void actualizaTablaBusqueda() {
        listaProductos.clear();
        Producto.fillProductosList(listaProductos);
    }

    /**
     * Activa y limplia los campos para crear un nuevo producto.
     */
    @FXML
    private void nuevoProductoFX() {
        clearForm();
        nuevoNumeroIdProudcto();
        tfIdProducto.setText(Integer.toString(nuevoNumeroIdProudcto()));
        enableTextFieldEditable();
        nuevoProductoPressed();
        actualizaTablaBusqueda();
        cb_proveedores.setItems(listaProveedor);
        cb_proveedores.setValue(listaProveedor.toString());
        cb_categorias.setItems(listaCategoria);
        cb_categorias.setValue(listaCategoria.toString());
        //el usuario rellena los datos en este punto
    }

    /**
     * LLama al método para guardar los datos nuevos en la base de datos.
     */
    @FXML
    private void guardarNuevoRegistroFX() {

        if (validateEmptyField("No hay datos para guardar", tfIdProducto.getText().isEmpty())) {
            if (validateEmptyField("Ingrese el nombre", tfNombreProducto.getText().isEmpty())) {
                if (validateEmptyField("Indique Existencias. Si no hay escriba 0", tfExistencias.getText().isEmpty())) {
                    if (validateEmptyField("precio", tfPrecio.getText().isEmpty())) {
                        if (validateFormatNumber("Revise el precio", tfPrecio.getText())) {
                            if (validateFormatNumber("Revise las existencias", tfExistencias.getText())) {
                                guardarNuevoRegistro();
                                actualizaTablaBusqueda();
                                estadoInicialBotonesVisibles();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Al ejecutarse este método los botones vuelven al estado inicial y los
     * campos para escribir texto vuelven a estar deshabilitados.
     */
    @FXML
    private void cancelarNuevoProductoFX() {
        estadoInicialBotonesVisibles();
        clearForm();
    }

    /**
     * Llama al método que ejecuta el borrado del registro actual.
     *
     * @see borrarRegistro().
     */
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

    /**
     * Borra de la base de datos el registro del cliente actualmente
     * seleccionado.
     */
    private void borrarRegistro() {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt2 = null;

        try {
            stmt2 = con.prepareStatement("DELETE FROM productos where idproducto=?");
            stmt2.setString(1, tfIdProducto.getText());
            stmt2.executeUpdate();
            stmt2.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                stmt2.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Limpia el formulario.
     */
    private void clearForm() {
        tfIdProducto.clear();
        tfNombreProducto.clear();
        tfPrecio.clear();
        tfExistencias.clear();
    }

    /**
     * Guarda un registro nuevo en la base de datos.
     */
    private void guardarNuevoRegistro() {

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt = null;
        try {
            String id = Integer.toString(nuevoNumeroIdProudcto());
            String nombre = tfNombreProducto.getText();
            int proveedor = cb_proveedores.getSelectionModel().getSelectedIndex() + 1;
            int categoria = cb_categorias.getSelectionModel().getSelectedIndex() + 1;
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

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    /**
     * Genera un número de identificación nuevo para el registro que se va a
     * crear consultando antes el último número de la base de datos.
     *
     * @return Número de identificación.
     */
    private int nuevoNumeroIdProudcto() {

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
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
        return idProducto + 1;
    }

    /**
     * Guarda los campos editados en la base de datos.
     */
    private void guardarEditado() {

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt = null;
        try {

            String nombre = tfNombreProducto.getText();
            int proveedor = cb_proveedores.getSelectionModel().getSelectedIndex() + 1;
            int categoria = cb_categorias.getSelectionModel().getSelectedIndex() + 1;
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
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Habilita los campos para ser editables.
     */
    private void enableTextFieldEditable() {
        tfIdProducto.setEditable(false);// siempre deshabilitado
        tfNombreProducto.setEditable(true);
        tfPrecio.setEditable(true);
        tfExistencias.setEditable(true);
    }

    /**
     * Deshabilita los campos para que no sean editables.
     */
    private void disableTextFieldEditable() {
        tfIdProducto.setEditable(false);// siempre deshabilitado
        tfNombreProducto.setEditable(false);
        tfPrecio.setEditable(false);
        tfExistencias.setEditable(false);
    }

    /**
     * Acciones que se realizan cuando se presiona el botón 'Editar'.
     */
    private void editarTextoPressed() {
        tablaBusquedaProducto.setDisable(true);
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(true);
        bt_guardarEditar.setVisible(true);
        bt_nuevoProducto.setVisible(false);
        bt_cancelarNuevoProducto.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(false);
    }

    /**
     * Acciones que se realizan cuando se presiona el botón 'Nuevo producto'.
     */
    private void nuevoProductoPressed() {
        tablaBusquedaProducto.setDisable(true);
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoProducto.setVisible(false);
        bt_cancelarNuevoProducto.setVisible(true);
        bt_guardarNuevoRegistro.setVisible(true);
        bt_borrarRegistro.setVisible(false);
    }

    /**
     * Definición del estado de visibilidad o habilitabilidad en el que deben
     * estar los botones.
     */
    private void estadoInicialBotonesVisibles() {
        tablaBusquedaProducto.setDisable(false);
        bt_editarTexto.setVisible(true);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoProducto.setVisible(true);
        bt_cancelarNuevoProducto.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(true);
    }

    /**
     * Comprueba que el campo evaluado está vacío
     *
     * @param text Texto que se muestra en la ventana de advertencia
     * @param field Campo que se comprueba
     * @return {@code false} si está vacío, {@code true} si contiene información
     */
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

    /**
     * Valida si los caracteres recibidos son un número o no.
     *
     * @param texto Texto para mostrar en la ventana de advertencia.
     * @param numero Número que se comprueba
     * @return {@code false} si es un número, {@code true} si no lo es.
     */
    private boolean validateFormatNumber(String campo, String numero) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(numero);
        if (m.find() && m.group().equals(numero)) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Validación de números");
            alert.setHeaderText(null);
            alert.setContentText(campo);
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Abre el menú principal.
     */
    @FXML
    private void menuPrincipalFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMainMenu.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLMainMenuController controller = fxmlLoader.<FXMLMainMenuController>getController();
            controller.initVariable(tipoUsuario, empleado);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..::Menú Principal::..");

            //cierra la ventana abierta anteriormente
            Stage stage3 = (Stage) bt_menuPrincipal.getScene().getWindow();
            stage3.close();

        } catch (IOException ex) {
            System.out.println("menuPrincipalFX: " + ex.getMessage());
        }
    }
}
