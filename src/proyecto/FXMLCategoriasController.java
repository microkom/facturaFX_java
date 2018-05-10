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
public class FXMLCategoriasController implements Initializable {

    private int tipoUsuario;
    private int empleado;
    private String genIdCategoria = "";
    private ResultSet rs;
    private ResultSet rs2;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    private ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();
    private FilteredList<Categoria> listaCategoriasFiltrada = new FilteredList(listaCategorias, obj -> true);
    int posicionCategoria = 0;

    @FXML
    private TableView<Categoria> tablaBusquedaCategoria;

    @FXML
    private TableColumn<Categoria, String> tcIdCategoria, tcNombre, tcDescripcion;

    @FXML
    private TextField tfIdCategoria, tfNombre, tfDescripcion, tfBusquedaCategorias;

    //IDs botones de edición
    @FXML
    private Button bt_editarTexto, bt_cancelarEditar, bt_guardarEditar;

    //IDs botones nuevo registro
    @FXML
    private Button bt_nuevaCategoria, bt_cancelarNuevaCategoria, bt_borrarRegistro, bt_guardarNuevoRegistro, bt_menuPrincipal;

    private final ListChangeListener<Categoria> selectorTablaCategorias = new ListChangeListener<Categoria>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Categoria> c) {
            ponerCategoriaSeleccionada();
        }
    };

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initVariable(int tipoUsuario, int empleado) {
        this.tipoUsuario = tipoUsuario;
        this.empleado = empleado;
        try {
            //CLIENTES
            //rellenar lista de categorias en listado
            Categoria.fillCategoriaList(listaCategorias);

            //lista de categorias para filtrar
            tablaBusquedaCategoria.setItems(listaCategoriasFiltrada);

            //busqueda en tiempo real por nombre, contacto, cargo contacto, ciudad. Tiene en cuenta las tildes 
            tfBusquedaCategorias.setOnKeyReleased(keyEvent -> {
                listaCategoriasFiltrada.setPredicate(obj -> obj.getDatosBusqueda().toLowerCase().contains(tfBusquedaCategorias.getText().toLowerCase().trim()));
            });

            //Valores para rellenar la vista de la tabla
            tcIdCategoria.setCellValueFactory(new PropertyValueFactory<Categoria, String>("id"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<Categoria, String>("nombre"));
            tcDescripcion.setCellValueFactory(new PropertyValueFactory<Categoria, String>("descripcion"));

            //En el initialize añadimos el «Listener» al TableView
            final ObservableList<Categoria> tablaCategoriaSel
                    = tablaBusquedaCategoria.getSelectionModel().getSelectedItems();
            tablaCategoriaSel.addListener(selectorTablaCategorias);

        } catch (Exception ex) {
            System.out.println("Relacionado con Categorias en el initialize: " + ex.getMessage());
        }

        disableTextFieldEditable();
        estadoInicialBotonesVisibles();

    }

    //Método que devuelve el objeto de la fila seleccionada
    public Categoria getTablaCategoriasSeleccionado() { //de aqui va a los textfields

        Categoria categoriaSeleccionada = null;
        if (tablaBusquedaCategoria != null) {
            List<Categoria> tabla = tablaBusquedaCategoria.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                categoriaSeleccionada = tabla.get(0);
                return categoriaSeleccionada;
            }
        }
        return categoriaSeleccionada;
    }

    //Método que a partir del objeto seleccionado lo muestra en el formulario
    //También puede habilitar/deshabilitar botones en el formualrio
    public void ponerCategoriaSeleccionada() {
        final Categoria categoria = getTablaCategoriasSeleccionado();
        posicionCategoria = listaCategorias.indexOf(categoria);
        if (categoria != null) {

            tfIdCategoria.setText(categoria.getId());
            tfNombre.setText(categoria.getNombre());
            tfDescripcion.setText(categoria.getDescripcion());
        }
    }

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
            Stage stage2 = (Stage) bt_menuPrincipal.getScene().getWindow();
            stage2.close();

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @FXML
    private void editarTextoFX() {
        if (validateEmptyField("Debe seleccionar primero una categoria", tfIdCategoria.getText().isEmpty())) {
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
        listaCategorias.clear();
        Categoria.fillCategoriaList(listaCategorias);
    }

    @FXML
    private void nuevaCategoriaFX() {
        clearForm();
        genIdCategoria = Integer.toString(nuevoNumeroId());
        tfIdCategoria.setText(genIdCategoria);
        enableTextFieldEditable();
        nuevaCategoriaPressed();
        actualizaTablaBusqueda();
        //el usuario rellena los datos en este punto
    }

    @FXML
    private void guardarNuevoRegistroFX() {
        if (validateEmptyField("No hay datos para guardar", tfIdCategoria.getText().isEmpty())) {
            if (validateEmptyField("Ingrese el nombre", tfNombre.getText().isEmpty())) {
                estadoInicialBotonesVisibles();
                guardarNuevoRegistro();
                actualizaTablaBusqueda();
            }
        }
    }

    @FXML
    private void cancelarNuevaCategoriaFX() {
        estadoInicialBotonesVisibles();
        clearForm();
        disableTextFieldEditable();
    }

    @FXML
    private void borrarRegistroFX() {
        if (validateEmptyField("Debe seleccionar primero una categoria", tfIdCategoria.getText().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrado de datos");
            alert.setHeaderText("");
            alert.setContentText("¿Se va a eliminar el registro actual. Seguro que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                borrarRegistro();
                tablaBusquedaCategoria.refresh();//Actualizar la tabla que muestra los registros
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
            stmt2 = con.prepareStatement("DELETE FROM categorias where idcategoria=?");
            stmt2.setString(1, tfIdCategoria.getText());
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
            stmt = con.prepareStatement("SELECT max(IdCategoria) FROM categorias");
            stmt.executeQuery();
            rs = stmt.executeQuery();
            rs.first();
            numFactura = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("nuevoNumeroiId: " + ex.getMessage());
        } finally {
            try {
                stmt.close();
                rs.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, nuevoNumeroiId: " + ex.getMessage());
            }
        }
        return numFactura + 1;
    }

    //mostrar formulario en blanco
    private void clearForm() {
        tfIdCategoria.clear();
        tfNombre.clear();
        tfDescripcion.clear();

    }

    //Guarda un registro nuevo
    private void guardarNuevoRegistro() {
        PreparedStatement stmt;
        try {
            String id = genIdCategoria;
            String nombre = tfNombre.getText();
            String descripcion = tfDescripcion.getText();

            stmt = con.prepareStatement("INSERT INTO categorias ( idcategoria, nombre, descripcion ) "
                    + " VALUES (\"" + id + "\",\"" + nombre + "\",\"" + descripcion + "\" )");
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registros");
            alert.setHeaderText(null);
            alert.setContentText("Nueva Categoria guardada correctamente");
            alert.showAndWait();

            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Guarda lo editado
    private void guardarEditado() {
        if (validateEmptyField("No hay datos para guardar", tfIdCategoria.getText().isEmpty())) {
            PreparedStatement stmt;
            try {
                String id = genIdCategoria;
                String nombre = tfNombre.getText();
                String descripcion = tfDescripcion.getText();

                stmt = con.prepareStatement("UPDATE categorias SET nombre=\"" + nombre
                        + "\", Descripcion=\"" + descripcion
                        + "\" where idcategoria=? ");

                stmt.setString(1, tfIdCategoria.getText());
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
        tfIdCategoria.setEditable(false);// siempre deshabilitado
        tfNombre.setEditable(true);
        tfDescripcion.setEditable(true);
    }

    private void disableTextFieldEditable() {
        tfIdCategoria.setEditable(false);// siempre deshabilitado
        tfNombre.setEditable(false);
        tfDescripcion.setEditable(false);

    }

    private void editarTextoPressed() {
        tablaBusquedaCategoria.setDisable(true);
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(true);
        bt_guardarEditar.setVisible(true);
        bt_nuevaCategoria.setVisible(false);
        bt_cancelarNuevaCategoria.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(false);
    }

    private void nuevaCategoriaPressed() {
        tablaBusquedaCategoria.setDisable(true);
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevaCategoria.setVisible(false);
        bt_cancelarNuevaCategoria.setVisible(true);
        bt_guardarNuevoRegistro.setVisible(true);
        bt_borrarRegistro.setVisible(false);
    }

    private void estadoInicialBotonesVisibles() {
        tablaBusquedaCategoria.setDisable(false);
        bt_editarTexto.setVisible(true);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevaCategoria.setVisible(true);
        bt_cancelarNuevaCategoria.setVisible(false);
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
}
