/*
finished commenting
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
 * Clase controladora del archivo FXMLClientes.fxml
 *
 * @author German Navarro
 */
public class FXMLClientesController implements Initializable {

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
     * para el cliente.
     */
    private String genIdCliente = "";
    private ResultSet rs;
    private ResultSet rs2;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private FilteredList<Cliente> listaClientesFiltrada = new FilteredList(listaClientes, obj -> true);
    int posicionCliente = 0;

    @FXML
    private TableView<Cliente> tablaBusquedaCliente;
    @FXML
    private TableColumn<Cliente, String> tCnombreCliente, tCcontacto, tCcargoContacto, tCdireccion, tCciudad, tCpais, tCtelefono;

    @FXML
    private TextField tfIdCliente, tfNombreCliente, tfContactoCliente, tfCargoContactoCliente,
            tfDireccionCliente, tfCiudadCliente, tfPaisCliente, tfTelefono, tfBusquedaClientes;

    //IDs botones de edición
    @FXML
    private Button bt_editarTexto, bt_cancelarEditar, bt_guardarEditar;

    //IDs botones nuevo registro
    @FXML
    private Button bt_nuevoCliente, bt_cancelarNuevoCliente, bt_borrarRegistro, bt_guardarNuevoRegistro, bt_menuPrincipal;

    private final ListChangeListener<Cliente> selectorTablaClientes = new ListChangeListener<Cliente>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Cliente> c) {
            ponerClienteSeleccionado();
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
            //rellenar lista de clientes en listado
            Cliente.fillClientesList(listaClientes);

            //lista de clientes para filtrar
            tablaBusquedaCliente.setItems(listaClientesFiltrada);

            //busqueda en tiempo real por nombre, contacto, cargo contacto, ciudad. Tiene en cuenta las tildes 
            tfBusquedaClientes.setOnKeyReleased(keyEvent -> {
                listaClientesFiltrada.setPredicate(obj -> obj.getDatosBusqueda().toLowerCase().contains(tfBusquedaClientes.getText().toLowerCase().trim()));
            });
            //Valores para rellenar la vista de la tabla
            tCnombreCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
            tCcontacto.setCellValueFactory(new PropertyValueFactory<Cliente, String>("contacto"));
            tCcargoContacto.setCellValueFactory(new PropertyValueFactory<Cliente, String>("cargoContacto"));
            tCdireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
            tCciudad.setCellValueFactory(new PropertyValueFactory<Cliente, String>("ciudad"));
            tCpais.setCellValueFactory(new PropertyValueFactory<Cliente, String>("pais"));
            tCtelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));

            //En el initialize añadimos el «Listener» al TableView
            final ObservableList<Cliente> tablaClienteSel
                    = tablaBusquedaCliente.getSelectionModel().getSelectedItems();
            tablaClienteSel.addListener(selectorTablaClientes);

        } catch (Exception ex) {
            System.out.println("Relacionado con Clientes en el initialize: " + ex.getMessage());
        }

        disableTextFieldEditable();
        estadoInicialBotonesVisibles();
    }

    /**
     * Método, que se cuando realiza una búsqueda, captura el objeto
     * seleccionado
     *
     * @return Un objeto del tipo Cliente que ha sido seleccionado.
     */
    public Cliente getTablaClientesSeleccionado() { //de aqui va a los textfields

        Cliente clienteSeleccionado = null;
        if (tablaBusquedaCliente != null) {
            List<Cliente> tabla = tablaBusquedaCliente.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                clienteSeleccionado = tabla.get(0);
                return clienteSeleccionado;
            }
        }
        return clienteSeleccionado;
    }

    /**
     * Método, que cuando se realiza una búsqueda, muestra en el formulario el
     * objeto seleccionado.
     */
    public void ponerClienteSeleccionado() {
        final Cliente cliente = getTablaClientesSeleccionado();
        posicionCliente = listaClientes.indexOf(cliente);
        if (cliente != null) {
            tfIdCliente.setText(cliente.getId());
            tfNombreCliente.setText(cliente.getNombre());
            tfContactoCliente.setText(cliente.getContacto());
            tfCargoContactoCliente.setText(cliente.getCargoContacto());
            tfDireccionCliente.setText(cliente.getDireccion());
            tfCiudadCliente.setText(cliente.getCiudad());
            tfPaisCliente.setText(cliente.getPais());
            tfTelefono.setText(cliente.getTelefono());
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
            Stage stage2 = (Stage) bt_menuPrincipal.getScene().getWindow();
            stage2.close();

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    /**
     * Activa los campos para editar el cliente seleccionado.
     */
    @FXML
    private void editarTextoFX() {
        if (validateEmptyField("Debe seleccionar primero un cliente", tfIdCliente.getText().isEmpty())) {
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
        listaClientes.clear();
        Cliente.fillClientesList(listaClientes);
    }

    /**
     * Activa y limplia los campos para crear un nuevo cliente.
     */
    @FXML
    private void nuevoClienteFX() {
        clearForm();
        genIdCliente = generateString();
        tfIdCliente.setText(genIdCliente);
        enableTextFieldEditable();
        nuevoClientePressed();
        actualizaTablaBusqueda();
        //el usuario rellena los datos en este punto
    }

    /**
     * LLama al método para guardar los datos nuevos en la base de datos.
     */
    @FXML
    private void guardarNuevoRegistroFX() {
        if (validateEmptyField("No hay datos para guardar", tfIdCliente.getText().isEmpty())) {
            if (validateEmptyField("Ingrese el nombre", tfNombreCliente.getText().isEmpty())) {
                estadoInicialBotonesVisibles();
                guardarNuevoRegistro();
                actualizaTablaBusqueda();
            }
        }
    }

    /**
     * Al ejecutarse este método los botones vuelven al estado inicial y los
     * campos para escribir texto vuelven a estar deshabilitados.
     */
    @FXML
    private void cancelarNuevoClienteFX() {
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
        if (validateEmptyField("Debe seleccionar primero un cliente", tfIdCliente.getText().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrado de datos");
            alert.setHeaderText("");
            alert.setContentText("¿Se va a eliminar el registro actual. Seguro que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                borrarRegistro();
                tablaBusquedaCliente.refresh();//Actualizar la tabla que muestra los registros
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
        PreparedStatement stmt2;

        try {
            stmt2 = con.prepareStatement("DELETE FROM clientes where idcliente=?");
            stmt2.setString(1, tfIdCliente.getText());
            stmt2.executeUpdate();
            stmt2.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Generador de codigos aleatorios usados para identificar al cliente.
     *
     * @return Código que identifica al cliente.
     */
    public String generateString() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        uuid = uuid.substring(0, Math.min(uuid.length(), 5));
        return uuid.toUpperCase();
    }

    /**
     * Limpia el formulario.
     */
    private void clearForm() {
        tfIdCliente.clear();
        tfNombreCliente.clear();
        tfContactoCliente.clear();
        tfCargoContactoCliente.clear();
        tfDireccionCliente.clear();
        tfCiudadCliente.clear();
        tfPaisCliente.clear();
        tfTelefono.clear();
    }

    /**
     * Guarda un registro nuevo en la base de datos.
     */
    private void guardarNuevoRegistro() {
        PreparedStatement stmt;
        try {
            String id = genIdCliente;
            String nombre = tfNombreCliente.getText();
            String contacto = tfContactoCliente.getText();
            String cargo = tfCargoContactoCliente.getText();
            String direccion = tfDireccionCliente.getText();
            String ciudad = tfCiudadCliente.getText();
            String pais = tfPaisCliente.getText();
            String telefono = tfTelefono.getText();

            stmt = con.prepareStatement("INSERT INTO clientes ( idcliente, nombre, contacto,CargoContacto, direccion, ciudad, pais, telefono) "
                    + " VALUES (\"" + id + "\",\"" + nombre + "\",\"" + contacto + "\",\"" + cargo + "\",\"" + direccion + "\",\"" + ciudad + "\",\"" + pais + "\",\"" + telefono + "\" )");
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registros");
            alert.setHeaderText(null);
            alert.setContentText("Nuevo Cliente guardado correctamente");
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
        if (validateEmptyField("No hay datos para guardar", tfIdCliente.getText().isEmpty())) {
            PreparedStatement stmt;
            try {
                String id = tfIdCliente.getText();
                String nombre = tfNombreCliente.getText();
                String contacto = tfContactoCliente.getText();
                String cargo = tfCargoContactoCliente.getText();
                String direccion = tfDireccionCliente.getText();
                String ciudad = tfCiudadCliente.getText();
                String pais = tfPaisCliente.getText();
                String telefono = tfTelefono.getText();
                stmt = con.prepareStatement("UPDATE clientes SET nombre=\"" + nombre + "\", Contacto=\"" + contacto + "\",cargoContacto=\"" + cargo + "\" ,direccion=\"" + direccion + "\", ciudad=\"" + ciudad + "\",pais=\"" + pais + "\",telefono=\"" + telefono + "\" where idcliente=? ");
                stmt.setString(1, tfIdCliente.getText());
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
        tfIdCliente.setEditable(false);// siempre deshabilitado
        tfNombreCliente.setEditable(true);
        tfContactoCliente.setEditable(true);
        tfCargoContactoCliente.setEditable(true);
        tfDireccionCliente.setEditable(true);
        tfCiudadCliente.setEditable(true);
        tfPaisCliente.setEditable(true);
        tfTelefono.setEditable(true);
    }

     /**
     * Deshabilita los campos para que no sean editables.
     */
    private void disableTextFieldEditable() {
        tfIdCliente.setMouseTransparent(true);// siempre deshabilitado
        tfIdCliente.setEditable(false);// siempre deshabilitado
        tfNombreCliente.setEditable(false);
        tfContactoCliente.setEditable(false);
        tfCargoContactoCliente.setEditable(false);
        tfDireccionCliente.setEditable(false);
        tfCiudadCliente.setEditable(false);
        tfPaisCliente.setEditable(false);
        tfTelefono.setEditable(false);
    }

    /**
     * Acciones que se realizan cuando se presiona el botón 'Editar'.
     */
    private void editarTextoPressed() {
        tablaBusquedaCliente.setDisable(true);
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(true);
        bt_guardarEditar.setVisible(true);
        bt_nuevoCliente.setVisible(false);
        bt_cancelarNuevoCliente.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(false);
    }

    /**
     * Acciones que se realizan cuando se presiona el botón 'Nuevo Cliente'.
     */
    private void nuevoClientePressed() {
        tablaBusquedaCliente.setDisable(true);
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoCliente.setVisible(false);
        bt_cancelarNuevoCliente.setVisible(true);
        bt_guardarNuevoRegistro.setVisible(true);
        bt_borrarRegistro.setVisible(false);
    }

    /**
     * Definición del estado de visibilidad o habilitabilidad en el que deben
     * estar los botones.
     */
    private void estadoInicialBotonesVisibles() {
        tablaBusquedaCliente.setDisable(false);
        bt_editarTexto.setVisible(true);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoCliente.setVisible(true);
        bt_cancelarNuevoCliente.setVisible(false);
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
