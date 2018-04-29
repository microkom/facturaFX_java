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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author DAW
 */
public class FXMLEmpleadosController implements Initializable {

    private ArrayList<Empleado> bossList = new ArrayList<Empleado>();
    //Usado para rellenar el contenido del checkBox cb_estadoCivil
    private ObservableList<String> estadoList = FXCollections.observableArrayList("Soltero", "Casado", "Divorciado", "Viudo");
    private ObservableList<Empleado> bossListado = FXCollections.observableArrayList(bossList);

    private ResultSet rs;
    private ResultSet rs2;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();
    private ArrayList<Empleado> employeeList = new ArrayList<Empleado>();

    @FXML
    private TextField tf_IdEmpleado, tf_nombre, tf_apellidos, tf_cargo, tf_telefono, tf_jefe;

    //IDs botones de edición
    @FXML
    private Button bt_editText, bt_cancelEdit, bt_saveEdit;

    //IDs botones nuevo registro
    @FXML
    private Button bt_newEntry, bt_cancelNewEntry, bt_deleteEntry, bt_saveNewEntry;

    @FXML
    private Button bt_last, bt_next, bt_previous, bt_first, bt_quotation;

    @FXML
    private DatePicker dp_fechaNacimiento, dp_fechaContrato;
    @FXML
    private ComboBox cb_estadoCivil, cb_jefe;
    @FXML
    private Hyperlink id_salir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //usado para rellenar el checkBox cb_estadoCivil
        cb_estadoCivil.setItems(estadoList);
        //valor inicial del checkBox cb_estadoCivil
        cb_estadoCivil.setValue("Casado");

        try {
            //fillBossList();
            initialDataFetch();
            fillOutForm();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        //comprobacion de botones
        buttons();

        //deshabilitar textFields al cargar el programa
        disableTextFieldEditable();
        initialStateButtons();
    }

    private Fecha convertStringToFecha(String text) {
        int counter = 0;
        int num1 = 0, num2 = 0, num3 = 0;
        for (String s : text.split("-")) {
            switch (counter) {
                case 0:
                    num3 = (Integer.parseInt(s));
                    break;
                case 1:
                    num2 = (Integer.parseInt(s));
                    break;
                case 2:
                    num1 = (Integer.parseInt(s));
                    break;
            }
            counter++;
        }
        Fecha date = new Fecha(num1, num2, num3);
        return date;
    }

    private void initialDataFetch() {

        try {
            PreparedStatement stmt;
            stmt = con.prepareStatement("SELECT e.IdEmpleado,e.Apellidos,e.Nombre,e.Cargo,e.FNacimiento,e.FContrato,e.Telefono,e.jefe, ea.Nombre as nombreJefe,ea.Apellidos as apellidoJefe from Empleados e left JOIN empleados ea on ea.IdEmpleado=e.Jefe");
            rs = stmt.executeQuery();
            rs.first();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

//    private void fillBossList() throws SQLException {
//        Empleado employeeTMP;
//        ResultSet rs3;
//        PreparedStatement stmt3;
//        stmt3 = con.prepareStatement("SELECT nombre,jefe FROM empleados");
//        rs3 = stmt3.executeQuery();
//
//        while (rs3.next()) {
//            String bossId = rs3.getString("Jefe");
//            String nombre = rs3.getString("nombre");
//            employeeTMP = new Empleado(nombre);
//
//            bossList.add(employeeTMP);
//        }
//    }

//    private String findOutBoss() throws SQLException {
//        Empleado employeeTMP;
//        ResultSet rs3;
//        PreparedStatement stmt3;
//        stmt3 = con.prepareStatement("SELECT nombre,jefe FROM empleados");
//        rs3 = stmt3.executeQuery();
//        String boss = "",nombre = "",sentName = "";
//        while (rs3.next()) {
//            String bossId = rs3.getString("Jefe");
//            nombre = rs3.getString("nombre");
//            employeeTMP = new Empleado(nombre);
//
//            if (rs.getString("Jefe").equals(bossId)) {
//                sentName = nombre;
//            }
//            bossList.add(employeeTMP);
//        }
//        return sentName;
//    }

    private void fillOutForm() {
        try {
            tf_IdEmpleado.setText(Integer.toString(rs.getInt("IdEmpleado")));
            tf_apellidos.setText(rs.getString("apellidos"));
            tf_nombre.setText(rs.getString("nombre"));
            tf_cargo.setText(rs.getString("cargo"));
            //para mostrar en el datePicker
            dp_fechaNacimiento.setValue(rs.getDate("fNacimiento").toLocalDate());
            dp_fechaContrato.setValue(rs.getDate("fContrato").toLocalDate());
            tf_telefono.setText(rs.getString("telefono"));

            String nombreJefe = rs.getString("nombreJefe");
            String apellidoJefe = rs.getString("apellidoJefe");

            if (nombreJefe == null) {
                nombreJefe = "";
            }
            if (apellidoJefe == null) {
                apellidoJefe = "";
            }

            tf_jefe.setText(nombreJefe + " " + apellidoJefe);

//            cb_jefe.setItems(bossListado);
//            cb_jefe.setValue(findOutBoss());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void disableTextFieldEditable() {

        tf_IdEmpleado.setDisable(true);
        tf_nombre.setDisable(true);
        tf_apellidos.setDisable(true);
        tf_cargo.setDisable(true);
        tf_telefono.setDisable(true);
        dp_fechaNacimiento.setDisable(true);
        dp_fechaContrato.setDisable(true);
        cb_estadoCivil.setDisable(true);
        tf_jefe.setDisable(true);

    }

    private void enableTextFieldEditable() {
        tf_IdEmpleado.setDisable(true);//siempre deshabilitado
        tf_nombre.setDisable(false);
        tf_apellidos.setDisable(false);
        tf_cargo.setDisable(false);
        tf_telefono.setDisable(false);
        dp_fechaNacimiento.setDisable(false);
        dp_fechaContrato.setDisable(false);
        cb_estadoCivil.setDisable(false);
        tf_jefe.setDisable(false);
    }

    //boton de edición
    @FXML
    private void editText() {
        enableTextFieldEditable();
        editTextPressed();
    }

    //boton cancelar, dentro de edición
    @FXML
    private void cancelEditText() {
        disableTextFieldEditable();
        cancelEditPressed();
    }

    @FXML
    private void newEntry() {
        clearForm();
        enableTextFieldEditable();
        newEntryPressed();
        navigationButtonsDisabled();

        //el usuario rellena los datos en este punto
    }

    @FXML
    private void cancelNewEntry() {
        fillOutForm();
        disableTextFieldEditable();
        initialStateButtons();
        buttons();
    }

    //Acciones para el botón guardar nuevo registro
    @FXML
    private void saveForm() {
        saveEntry();
        initialDataFetch();
        try {
            rs.first();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        fillOutForm();
        initialStateButtons();
        buttons();
    }

    @FXML
    private void saveNewUser() {
        saveNewEntry();
        initialDataFetch();
        try {
            rs.first();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        fillOutForm();
        initialStateButtons();
        buttons();
    }

    //Acciones para el botón borrar
    @FXML
    private void delete() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Borrado de datos");
        alert.setHeaderText("");
        alert.setContentText("¿Se va a eliminar el registro actual. Seguro que quieres continuar?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteEntry();
            initialDataFetch();
            try {
                rs.first();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            fillOutForm();
            initialStateButtons();
            buttons();
            System.out.println("OK");
        } else {
            System.out.println("CANCEL");
        }
    }

    @FXML
    private void refresh() {
        initialDataFetch();
        fillOutForm();
        buttons();
    }

    //Contador de registros
    private int registryCounter(int added) {

        PreparedStatement stmt2;

        int contador = 0;
        try {
            stmt2 = con.prepareStatement("SELECT count(idempleado) FROM empleados");
            rs2 = stmt2.executeQuery();
            rs2.first();
            contador = rs2.getInt(1) + added;

        } catch (SQLException ex) {
            System.out.println("registryCounter: " + ex.getMessage());
        }
        return contador;
    }

    //mostrar formulario en blanco
    private void clearForm() {

        tf_IdEmpleado.setText(Integer.toString(registryCounter(1)));
        tf_apellidos.clear();
        tf_nombre.clear();
        tf_cargo.clear();
        //para mostrar en el datePicker
        //LocalDates date = new setLocalDate(2001,01,01);
        //dp_fechaNacimiento.setValue.;  //pendiente solucionar
        // dp_fechaContrato.setText("");
        tf_telefono.clear();
        tf_jefe.clear();
    }

//estado de los botones para navegar entre los registros
    private void buttons() {
        try {
            if (rs.isLast()) {
                bt_last.setDisable(true);
                bt_next.setDisable(true);
                bt_first.setDisable(false);
                bt_previous.setDisable(false);
            }
            if (!rs.isLast() && !rs.isFirst()) {
                bt_last.setDisable(false);
                bt_next.setDisable(false);
                bt_first.setDisable(false);
                bt_previous.setDisable(false);
            }
            if (rs.isFirst()) {
                bt_last.setDisable(false);
                bt_next.setDisable(false);
                bt_first.setDisable(true);
                bt_previous.setDisable(true);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void navigationButtonsDisabled() {
        bt_last.setDisable(true);
        bt_next.setDisable(true);
        bt_first.setDisable(true);
        bt_previous.setDisable(true);
    }

    @FXML
    private void firstRegistry() {

        try {
            this.rs.first();
            fillOutForm();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //comprobacion de botones
        buttons();
    }

    @FXML
    private void previousRegistry() {

        try {
            this.rs.previous();
            fillOutForm();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //comprobacion de botones
        buttons();
    }

    @FXML
    private void nextRegistry() {
        try {
            this.rs.next();
            fillOutForm();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //comprobacion de botones
        buttons();
    }

    @FXML
    private void lastRegistry() {
        try {
            this.rs.last();
            fillOutForm();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //comprobacion de botones
        buttons();
    }

    @FXML
    private void salir() {
        Stage stage = (Stage) id_salir.getScene().getWindow();
        stage.close();
    }

    private void editTextInvisible() {
        bt_editText.setVisible(false);
    }

    private void editTextVisible() {
        bt_editText.setVisible(true);
    }

    private void cancelEditInvisible() {
        bt_cancelEdit.setVisible(false);
    }

    private void cancelEditVisible() {
        bt_cancelEdit.setVisible(true);
    }

    private void saveEditInvisible() {
        bt_saveEdit.setVisible(false);
    }

    private void saveEditVisible() {
        bt_saveEdit.setVisible(true);
    }

    private void newEntryInvisible() {
        bt_newEntry.setVisible(false);
    }

    private void newEntryVisible() {
        bt_newEntry.setVisible(true);
    }

    private void cancelNewEntryInvisible() {
        bt_cancelNewEntry.setVisible(false);
    }

    private void cancelNewEntryVisible() {
        bt_cancelNewEntry.setVisible(true);
    }

    private void saveNewEntryInvisible() {
        bt_saveNewEntry.setVisible(false);
    }

    private void saveNewEntryVisible() {
        bt_saveNewEntry.setVisible(true);
    }

    private void deleteEntryInvisible() {
        bt_deleteEntry.setVisible(false);
    }

    private void deleteEntryVisible() {
        bt_deleteEntry.setVisible(true);
    }

    private void editTextPressed() {
        editTextInvisible();
        cancelEditVisible();
        saveEditVisible();
        newEntryInvisible();
        saveNewEntryInvisible();
        deleteEntryInvisible();
        deleteEntryInvisible();
    }

    private void cancelEditPressed() {
        editTextVisible();
        cancelEditInvisible();
        saveEditInvisible();
        newEntryVisible();
        cancelNewEntryInvisible();
        saveNewEntryInvisible();
        deleteEntryVisible();
    }

    private void initialStateButtons() {
        editTextVisible();
        cancelEditInvisible();
        saveEditInvisible();
        newEntryVisible();
        cancelNewEntryInvisible();
        saveNewEntryInvisible();
        deleteEntryVisible();
        disableTextFieldEditable();
        buttons();
    }

    private void newEntryPressed() {
        editTextInvisible();
        cancelEditInvisible();
        saveEditInvisible();
        newEntryInvisible();
        cancelNewEntryVisible();
        saveNewEntryVisible();
        deleteEntryInvisible();
    }

    private void cancelNewEntryPressed() {
        editTextVisible();
        cancelEditInvisible();
        saveEditInvisible();
        newEntryVisible();
        cancelNewEntryInvisible();
        saveNewEntryInvisible();
        deleteEntryVisible();
        buttons();
    }

    private void saveNewEntry() {
        PreparedStatement stmt;

        try {

            String id = Integer.toString(registryCounter(1));
            String apellidos = tf_apellidos.getText();
            String nombre = tf_nombre.getText();
            String cargo = tf_cargo.getText();
            //para mostrar en el datePicker
            String fNac = dp_fechaNacimiento.getValue().toString();
            fNac = fNac.replace("-", "");
            String fCon = dp_fechaContrato.getValue().toString();
            fCon = fCon.replace("-", "");
            String telefono = tf_telefono.getText();
            String jefe = tf_jefe.getText();

            stmt = con.prepareStatement("INSERT INTO empleados ( idempleado, Apellidos, Nombre,Cargo, FNacimiento, FContrato,Telefono ,Jefe) "
                    + " VALUES (\"" + id + "\",\"" + apellidos + "\",\"" + nombre + "\",\"" + cargo + "\",\"" + fNac + "\",\"" + fCon + "\",\"" + telefono + "\",2)");
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void saveEntry() {
        PreparedStatement stmt;

        try {

            String apellidos = tf_apellidos.getText();
            String nombre = tf_nombre.getText();
            String cargo = tf_cargo.getText();
            //para mostrar en el datePicker

            String fNac = dp_fechaNacimiento.getValue().toString();
            fNac = fNac.replace("-", "");
            String fCon = dp_fechaContrato.getValue().toString();
            fCon = fCon.replace("-", "");
            String telefono = tf_telefono.getText();
            String jefe = tf_jefe.getText();

            stmt = con.prepareStatement("UPDATE empleados SET Apellidos=\"" + apellidos + "\", Nombre=\"" + nombre + "\",Cargo=\"" + cargo + "\" ,FNacimiento=" + fNac + ", FContrato=" + fCon + ",Telefono=\"" + telefono + "\"   where idempleado=? ");
            stmt.setInt(1, rs.getInt("idempleado"));
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteEntry() {
        PreparedStatement stmt2;

        try {
            stmt2 = con.prepareStatement("DELETE FROM empleados where idempleado=?");
            stmt2.setInt(1, rs.getInt("IdEmpleado"));
            stmt2.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
//crear un menu que contenga las diferentes aplicaciones
