/*
finished commenting.
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
 * Clase controladora del archivo FXMLCategorias.fxml
 *
 * @author German Navarro.
 */
public class FXMLCategoriasController implements Initializable {

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
     * Variable de clase privada: almacena el número de identificación generado
     * para la categoría.
     */
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
     * Método que existe por defecto, NO USADO.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

    /**
     * Método, que cuando se realiza una búsqueda, captura el objeto
     * seleccionado.
     *
     * @return Un objeto del tipo Categoría que ha sido seleccionado.
     */
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

    /**
     * Método, que cuando se realiza una búsqueda, muestra en el formulario el
     * objeto seleccionado.
     */
    public void ponerCategoriaSeleccionada() {
        final Categoria categoria = getTablaCategoriasSeleccionado();
        posicionCategoria = listaCategorias.indexOf(categoria);
        if (categoria != null) {

            tfIdCategoria.setText(categoria.getId());
            tfNombre.setText(categoria.getNombre());
            tfDescripcion.setText(categoria.getDescripcion());
        }
    }

    /**
     * Abre el menú principal
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
            Stage stage2 = (Stage) bt_menuPrincipal.getScene().getWindow();
            stage2.close();

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    /**
     * Activa los campos para editar la Categoria seleccionada.
     */
    @FXML
    private void editarTextoFX() {
        if (validateEmptyField("Debe seleccionar primero una categoria", tfIdCategoria.getText().isEmpty())) {
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
        disableTextFieldEditable();
        clearForm();
    }

    /**
     * Guarda los campos editados en la base de datos.
     */
    @FXML
    private void guardarEditarFX() {
        estadoInicialBotonesVisibles();
        guardarEditado();
        actualizaTablaBusqueda();
        disableTextFieldEditable();
    }

    /**
     * Método que actualiza la tabla que muestra la búsqueda realizada.
     */
    @FXML
    private void actualizaTablaBusqueda() {
        listaCategorias.clear();
        Categoria.fillCategoriaList(listaCategorias);
    }

    /**
     * Activa y limplia los campos para crear una nueva categoría.
     */
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

    /**
     * LLama al método para guardar los datos nuevos en la base de datos.
     */
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

    /**
     * Al ejecutarse este método los botones vuelven al estado inicial y los campos para
     * escribir texto vuelven a estar deshabilitados.
     */
    @FXML
    private void cancelarNuevaCategoriaFX() {
        estadoInicialBotonesVisibles();
        clearForm();
        disableTextFieldEditable();
    }

    /**
     * Llama al método que ejecuta el borrado del registro actual.
     *
     * @see borrarRegistro().
     */
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

     /**
     * Borra de la base de datos el registro de la categoría actualmente
     * seleccionada.
     */
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

    /**
     * Genera un número de identificación nuevo para el registro que se va a
     * crear.
     *
     * @return Número de identificación.
     */
    private int nuevoNumeroId() {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int numCategoria = 0;
        try {
            stmt = con.prepareStatement("SELECT max(IdCategoria) FROM categorias");
            stmt.executeQuery();
            rs = stmt.executeQuery();
            rs.first();
            numCategoria = rs.getInt(1);
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
        return numCategoria + 1;
    }

    /**
     * Limpia el formulario.
     */
    private void clearForm() {
        tfIdCategoria.clear();
        tfNombre.clear();
        tfDescripcion.clear();

    }

    /**
     * Guarda un registro nuevo en la base de datos.
     *
     */
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

    /**
     * Guarda los campos editados en la base de datos.
     */
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

    /**
     * Habilita los campos para ser editables.
     */
    private void enableTextFieldEditable() {
        tfIdCategoria.setEditable(false);// siempre deshabilitado
        tfNombre.setEditable(true);
        tfDescripcion.setEditable(true);
    }

    /**
     * Deshabilita los campos para que no sean editables.
     */
    private void disableTextFieldEditable() {
        tfIdCategoria.setEditable(false);// siempre deshabilitado
        tfNombre.setEditable(false);
        tfDescripcion.setEditable(false);

    }

    /**
     * Acciones que se realizan cuando se presiona el botón 'Editar'.
     */
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

    /**
     * Acciones que se realizan cuando el botón 'Nueva categoría' es presionado.
     */
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

    /**
     * Definición del estado de visibilidad o habilitabilidad en el que deben
     * estar los botones.
     */
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
}
