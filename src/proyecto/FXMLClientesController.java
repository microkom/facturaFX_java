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
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author DAW
 */
public class FXMLClientesController implements Initializable {

    private String genIdCliente = "";
    private ResultSet rs;
    private ResultSet rs2;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();
    private ArrayList<Cliente> clientList = new ArrayList<Cliente>();

    @FXML
    private TextField tf_IdCliente, tf_nombre, tf_contacto, tf_cargo, tf_direccion, tf_ciudad, tf_pais, tf_telefono, tf_observaciones;

    //IDs botones de edición
    @FXML
    private Button bt_editText, bt_cancelEdit, bt_saveEdit;

    //IDs botones nuevo registro
    @FXML
    private Button bt_newEntry, bt_cancelNewEntry, bt_deleteEntry, bt_saveNewEntry;

    @FXML
    private Button bt_last, bt_next, bt_previous, bt_first;

    @FXML
    private Hyperlink id_salir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initialDataFetch();
            fillOutForm();
            //comprobacion de botones
            buttons();

            //deshabilitar textFields al cargar el programa
            disableTextFieldEditable();
            initialStateButtons();
        } catch (Exception ex) {
            System.out.println("Initialize: " + ex.getMessage());
        }
    }

    private void initialDataFetch() {
        Cliente customer;
        try {
            PreparedStatement stmt;
            stmt = con.prepareStatement("SELECT * from clientes");
            rs = stmt.executeQuery();

            
            while (rs.next()) {
                String id = rs.getString("IdCliente");
                String nombre = rs.getString("nombre");
                String contacto = rs.getString("Contacto");
                String cargo = rs.getString("cargoContacto");
                String direccion = rs.getString("Direccion");
                String ciudad = rs.getString("ciudad");
                String pais = rs.getString("pais");
                String telefono = rs.getString("telefono");
                String observaciones = rs.getString("observaciones");     
                
                customer = new Cliente(id,nombre,contacto,cargo,direccion,ciudad,pais,telefono,observaciones);
                
                clientList.add(customer);
            }
            rs.first(); //colocado temporalmente para que al finalizar el while
            //se quede en el primer registro. hecho así por si llegase a modificar 
            //todo el código pensando en usar arraylist
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void fillOutForm() {
        try {
            tf_IdCliente.setText(rs.getString("IdCliente"));
            tf_nombre.setText(rs.getString("nombre"));
            tf_contacto.setText(rs.getString("Contacto"));
            tf_cargo.setText(rs.getString("cargoContacto"));
            tf_direccion.setText(rs.getString("Direccion"));
            tf_ciudad.setText(rs.getString("ciudad"));
            tf_pais.setText(rs.getString("pais"));
            tf_telefono.setText(rs.getString("telefono"));
            tf_observaciones.setText(rs.getString("observaciones"));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void disableTextFieldEditable() {

        tf_IdCliente.setDisable(true);
        tf_nombre.setDisable(true);
        tf_contacto.setDisable(true);
        tf_cargo.setDisable(true);
        tf_direccion.setDisable(true);
        tf_ciudad.setDisable(true);
        tf_pais.setDisable(true);
        tf_telefono.setDisable(true);
    }

    private void enableTextFieldEditable() {
        tf_IdCliente.setDisable(true);//siempre deshabilitado
        tf_nombre.setDisable(false);
        tf_contacto.setDisable(false);
        tf_cargo.setDisable(false);
        tf_direccion.setDisable(false);
        tf_ciudad.setDisable(false);
        tf_pais.setDisable(false);
        tf_telefono.setDisable(false);
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
            stmt2 = con.prepareStatement("SELECT count(idCliente) FROM clientes");
            rs2 = stmt2.executeQuery();
            rs2.first();
            contador = rs2.getInt(1) + added;

        } catch (SQLException ex) {
            System.out.println("registryCounter: " + ex.getMessage());
        }
        return contador;
    }

    //Generador de codigos aleatorios usados como ID del cliente
    public String generateString() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        uuid = uuid.substring(0, Math.min(uuid.length(), 5));
        return uuid.toUpperCase();
    }

    //mostrar formulario en blanco
    private void clearForm() {
        genIdCliente = generateString();
        tf_IdCliente.setText(genIdCliente);
        tf_nombre.setText("");
        tf_contacto.setText("");
        tf_cargo.setText("");
        tf_direccion.setText("");
        tf_ciudad.setText("");
        tf_pais.setText("");
        tf_telefono.setText("");
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

    //Guarda un registro nuevo
    private void saveNewEntry() {
        PreparedStatement stmt;
        try {
            String id = genIdCliente;
            String nombre = tf_nombre.getText();
            String contacto = tf_contacto.getText();
            String cargo = tf_cargo.getText();
            String direccion = tf_direccion.getText();
            String ciudad = tf_ciudad.getText();
            String pais = tf_pais.getText();
            String telefono = tf_telefono.getText();
            String observaciones = tf_observaciones.getText();

            stmt = con.prepareStatement("INSERT INTO clientes ( idcliente, nombre, contacto,CargoContacto, direccion, ciudad, pais, telefono) "
                    + " VALUES (\"" + id + "\",\"" + nombre + "\",\"" + contacto + "\",\"" + cargo + "\",\"" + direccion + "\",\"" + ciudad + "\",\"" + pais + "\",\"" + telefono + "\",\"" + observaciones + "\")");
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Guarda lo editado
    private void saveEntry() {
        PreparedStatement stmt;
        try {
            String id = tf_IdCliente.getText();
            String nombre = tf_nombre.getText();
            String contacto = tf_contacto.getText();
            String cargo = tf_cargo.getText();
            String direccion = tf_direccion.getText();
            String ciudad = tf_ciudad.getText();
            String pais = tf_pais.getText();
            String telefono = tf_telefono.getText();
            String observaciones = tf_observaciones.getText();
            stmt = con.prepareStatement("UPDATE clientes SET nombre=\"" + nombre + "\", Contacto=\"" + contacto + "\",cargoContacto=\"" + cargo + "\" ,direccion=\"" + direccion + "\", ciudad=\"" + ciudad + "\",pais=\"" + pais + "\",telefono=\"" + telefono + "\",observaciones=\"" + observaciones + "\"    where idcliente=? ");
            stmt.setString(1, rs.getString("idcliente"));
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteEntry() {
        PreparedStatement stmt2;

        try {
            stmt2 = con.prepareStatement("DELETE FROM clientes where idcliente=?");
            stmt2.setString(1, rs.getString("IdCliente"));
            stmt2.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
//crear un menu que contenga las diferentes aplicaciones
