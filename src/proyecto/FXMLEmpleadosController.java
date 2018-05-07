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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
public class FXMLEmpleadosController implements Initializable {

    private String genIdEmpleado = "";
    private ResultSet rs;
    private ResultSet rs2;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();
    private FilteredList<Empleado> listaEmpleadosFiltrada = new FilteredList(listaEmpleados, obj -> true);
    int posicionEmpleado = 0;

    @FXML
    private TableView<Empleado> tablaBusquedaEmpleado;

    @FXML
    private TableColumn<Empleado, String> tcIdEmpleado, tcNombre, tcApellidos, tcCargo, tcJefe, tcTelefono, tcFNacimiento, tcFContrato;

    @FXML
    private TextField tfIdEmpleado, tfNombre, tfApellidos, tfCargo, tfJefe, tfTelefono, tfFNacimiento, tfFContrato, tfBusquedaEmpleados;

    //IDs botones de edición
    @FXML
    private Button bt_editarTexto, bt_cancelarEditar, bt_guardarEditar;

    //IDs botones nuevo registro
    @FXML
    private Button bt_nuevoEmpleado, bt_cancelarNuevoEmpleado, bt_borrarRegistro, bt_guardarNuevoRegistro, bt_menuPrincipal;

    @FXML
    private ComboBox cb_empleados;
    private final ListChangeListener<Empleado> selectorTablaEmpleados = new ListChangeListener<Empleado>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Empleado> c) {
            ponerEmpleadoSeleccionado();
        }
    };
    @FXML
    private Button bt_refresh;

    @FXML
    private DatePicker dpFcontrato, dpFnacimiento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //CLIENTES
            //rellenar lista de empleados en listado
            Empleado.fillEmpleadoList(listaEmpleados);

            //lista de empleados para filtrar
            tablaBusquedaEmpleado.setItems(listaEmpleadosFiltrada);

            //busqueda en tiempo real por nombre, contacto, cargo contacto, ciudad. Tiene en cuenta las tildes 
            tfBusquedaEmpleados.setOnKeyReleased(keyEvent -> {
                listaEmpleadosFiltrada.setPredicate(obj -> obj.getDatosBusqueda().toLowerCase().contains(tfBusquedaEmpleados.getText().toLowerCase().trim()));
            });

            //Valores para rellenar la vista de la tabla
            tcIdEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("id"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
            tcApellidos.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellido"));
            tcCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("cargo"));
            tcJefe.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nomJefe"));
            tcTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefono"));
            tcFNacimiento.setCellValueFactory(new PropertyValueFactory<Empleado, String>("fNacimiento"));
            tcFContrato.setCellValueFactory(new PropertyValueFactory<Empleado, String>("fContrato"));

            //En el initialize añadimos el «Listener» al TableView
            final ObservableList<Empleado> tablaEmpleadoSel
                    = tablaBusquedaEmpleado.getSelectionModel().getSelectedItems();
            tablaEmpleadoSel.addListener(selectorTablaEmpleados);

        } catch (Exception ex) {
            System.out.println("Relacionado con Empleados en el initialize: " + ex.getMessage());
        }

        disableTextFieldEditable();
        estadoInicialBotonesVisibles();

    }

    //Método que devuelve el objeto de la fila seleccionada
    public Empleado getTablaEmpleadosSeleccionado() { //de aqui va a los textfields

        Empleado empleadoSeleccionado = null;
        if (tablaBusquedaEmpleado != null) {
            List<Empleado> tabla = tablaBusquedaEmpleado.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                empleadoSeleccionado = tabla.get(0);
                return empleadoSeleccionado;
            }
        }
        return empleadoSeleccionado;
    }

    //Método que a partir del objeto seleccionado lo muestra en el formulario
    //También puede habilitar/deshabilitar botones en el formualrio
    public void ponerEmpleadoSeleccionado() {
        final Empleado empleado = getTablaEmpleadosSeleccionado();
        posicionEmpleado = listaEmpleados.indexOf(empleado);
        if (empleado != null) {

            //convert String to LocalDate
            LocalDate fContratoLocalDate = LocalDate.parse(empleado.getfContrato());
            LocalDate fNacimientoLocalDate = LocalDate.parse(empleado.getfNacimiento());

            tfIdEmpleado.setText(empleado.getId());
            tfNombre.setText(empleado.getNombre());
            tfApellidos.setText(empleado.getApellido());
            tfCargo.setText(empleado.getCargo());
            //tfJefe.setText(empleado.getNomJefe());
            cb_empleados.setItems(listaEmpleados);
            cb_empleados.setValue(empleado.getNomJefe()); //listaEmpleados.get(posicionEmpleado).getNomJefe()
            tfTelefono.setText(empleado.getTelefono());

            dpFcontrato.setValue(fContratoLocalDate);
            dpFnacimiento.setValue(fNacimientoLocalDate);

            //tfFNacimiento.setText(empleado.getfNacimiento());
            //tfFContrato.setText(empleado.getfContrato());
        }
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

    @FXML
    private void editarTextoFX() {
        if (validateEmptyField("Debe seleccionar primero un empleado", tfIdEmpleado.getText().isEmpty())) {
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
        listaEmpleados.clear();
        Empleado.fillEmpleadoList(listaEmpleados);
    }

    @FXML
    private void nuevoEmpleadoFX() {
        clearForm();
        genIdEmpleado = Integer.toString(nuevoNumeroId());
        tfIdEmpleado.setText(genIdEmpleado);
        tfJefe.setVisible(false);
        cb_empleados.setItems(listaEmpleados);
        cb_empleados.setValue(listaEmpleados.get(posicionEmpleado).getNomJefe());
        enableTextFieldEditable();
        nuevoEmpleadoPressed();
        actualizaTablaBusqueda();
        //el usuario rellena los datos en este punto
    }

    @FXML
    private void guardarNuevoRegistroFX() {
        if (validateEmptyField("No hay datos para guardar", tfIdEmpleado.getText().isEmpty())) {
            if (validateEmptyField("Ingrese el nombre", tfNombre.getText().isEmpty())) {
                estadoInicialBotonesVisibles();
                guardarNuevoRegistro();
                actualizaTablaBusqueda();
            }
        }
    }

    @FXML
    private void cancelarNuevoEmpleadoFX() {
        estadoInicialBotonesVisibles();
        clearForm();
        disableTextFieldEditable();
    }

    @FXML
    private void borrarRegistroFX() {
        if (validateEmptyField("Debe seleccionar primero un empleado", tfIdEmpleado.getText().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrado de datos");
            alert.setHeaderText("");
            alert.setContentText("¿Se va a eliminar el registro actual. Seguro que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                borrarRegistro();
                tablaBusquedaEmpleado.refresh();//Actualizar la tabla que muestra los registros
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
            stmt2 = con.prepareStatement("DELETE FROM empleados where idempleado=?");
            stmt2.setString(1, tfIdEmpleado.getText());
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
            stmt = con.prepareStatement("SELECT max(idempleado) FROM empleados");
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
        tfIdEmpleado.clear();
        tfNombre.clear();
        tfApellidos.clear();
        tfCargo.clear();
        //tfJefe.clear();
        tfFNacimiento.clear();
        tfFContrato.clear();
        tfTelefono.clear();
        tfJefe.setVisible(false);
    }

    //Guarda un registro nuevo
    private void guardarNuevoRegistro() {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt;
        try {
            String id = genIdEmpleado;
            String nombre = tfNombre.getText();
            String apellidos = tfApellidos.getText();
            String cargo = tfCargo.getText();
            int jefe = cb_empleados.getSelectionModel().getSelectedIndex() + 1;
            String telefono = tfTelefono.getText();
            String fNac = dpFnacimiento.getValue().format(DateTimeFormatter.ISO_DATE);
            String fCont = dpFcontrato.getValue().format(DateTimeFormatter.ISO_DATE);

            stmt = con.prepareStatement("INSERT INTO empleados ( idempleado, nombre, apellidos,Cargo, jefe, telefono, fnacimiento, fcontrato) "
                    + " VALUES (\"" + id + "\",\"" + nombre + "\",\"" + apellidos + "\",\"" + cargo + "\",\"" + jefe + "\",\"" + telefono + "\",\"" + fNac + "\",\"" + fCont + "\" )");
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registros");
            alert.setHeaderText(null);
            alert.setContentText("Nuevo Empleado guardado correctamente");
            alert.showAndWait();

            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Guarda lo editado
    private void guardarEditado() {

        if (validateEmptyField("No hay datos para guardar", tfIdEmpleado.getText().isEmpty())) {
            Conexion conexion = new Conexion();
            Connection con = conexion.conectar();
            PreparedStatement stmt;
            try {
                String nombre = tfNombre.getText();
                String apellidos = tfApellidos.getText();
                String cargo = tfCargo.getText();
                int jefe = cb_empleados.getSelectionModel().getSelectedIndex() + 1;
                String telefono = tfTelefono.getText();
                String fNac = dpFnacimiento.getValue().format(DateTimeFormatter.ISO_DATE);
                String fCont = dpFcontrato.getValue().format(DateTimeFormatter.ISO_DATE);
                System.out.println(fNac);
                System.out.println(fCont);
                stmt = con.prepareStatement("UPDATE empleados SET Nombre=\""+nombre+"\", Apellidos=\""+apellidos+"\", Cargo=\""+cargo+"\", FNacimiento=\""+fNac+"\", FContrato=\""+fCont+"\", Telefono=\""+telefono+"\", Jefe="+jefe+" where idempleado=?");

                stmt.setString(1, tfIdEmpleado.getText());
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
        tfIdEmpleado.setEditable(false);// siempre deshabilitado
        tfNombre.setEditable(true);
        tfApellidos.setEditable(true);
        tfCargo.setEditable(true);
        tfJefe.setEditable(true);
        tfFNacimiento.setEditable(true);
        tfFContrato.setEditable(true);
        tfTelefono.setEditable(true);
    }

    private void disableTextFieldEditable() {
        tfIdEmpleado.setEditable(false);// siempre deshabilitado
        tfNombre.setEditable(false);
        tfApellidos.setEditable(false);
        tfCargo.setEditable(false);
        tfJefe.setEditable(false);
        tfFNacimiento.setEditable(false);
        tfFContrato.setEditable(false);
        tfTelefono.setEditable(false);
    }

    private void editarTextoPressed() {
        tablaBusquedaEmpleado.setDisable(true);
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(true);
        bt_guardarEditar.setVisible(true);
        bt_nuevoEmpleado.setVisible(false);
        bt_cancelarNuevoEmpleado.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(false);
    }

    private void nuevoEmpleadoPressed() {
        tablaBusquedaEmpleado.setDisable(true);
        bt_editarTexto.setVisible(false);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoEmpleado.setVisible(false);
        bt_cancelarNuevoEmpleado.setVisible(true);
        bt_guardarNuevoRegistro.setVisible(true);
        bt_borrarRegistro.setVisible(false);
        tfJefe.setVisible(false);
    }

    private void estadoInicialBotonesVisibles() {
        tablaBusquedaEmpleado.setDisable(false);
        bt_editarTexto.setVisible(true);
        bt_cancelarEditar.setVisible(false);
        bt_guardarEditar.setVisible(false);
        bt_nuevoEmpleado.setVisible(true);
        bt_cancelarNuevoEmpleado.setVisible(false);
        bt_guardarNuevoRegistro.setVisible(false);
        bt_borrarRegistro.setVisible(true);
        tfJefe.setVisible(false);
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
