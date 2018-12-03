/*
finished commenting.
 */
package proyecto;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Clase controladora del archivo FXMLIVA.fxml
 *
 * @author German Navarro
 */
public class FXMLIVAController implements Initializable {

    /**
     * Variable de clase privada: IVA.
     */
    private double iva;
    /**
     * Variable de clase privada: número que identifica al tipo de usuario
     * conectado.
     */
    private int tipoUsuario;

    @FXML
    private Label lbIvaDBase;
    @FXML
    private TextField tfIVA;
    @FXML
    private Button btGuardar;

    /**
     * Método que existe por defecto, NO USADO.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    /**
     * Método que carga al iniciar el controlador de la clase. Consulta el IVA en
     * la base de datos.
     *
     * @param tipoUsuario Identifica el tipo de usuario.
     */
    public void initVariable(int tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        seguridadSegunUsuario();

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM iva");
            rs = stmt.executeQuery();
            rs.first();
            iva = rs.getDouble("valor");
            lbIvaDBase.setText(Double.toString(iva));
        } catch (SQLException ex) {
            System.out.println("initVariable: " + ex.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, initVariable: " + ex.getMessage());
            }
        }
    }

    /**
     * Actualiza el valor del IVA en la base de datos.
     */
    @FXML
    private void guardarFX() {

        String valor = tfIVA.getText();

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE iva set valor=" + valor + "");
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registros");
            alert.setHeaderText(null);
            alert.setContentText("IVA actualizado correctamente");
            alert.showAndWait();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, guardarFactura: " + ex.getMessage());
            }
        }
        //cierra la ventana abierta anteriormente
        Stage stage2 = (Stage) btGuardar.getScene().getWindow();
        stage2.close();

    }

    /**
     * Define la seguridad según el tipo de usuario.
     */
    public void seguridadSegunUsuario() {
        switch (tipoUsuario) {
            case 1:
                btGuardar.setDisable(false);
                break;
            case 2:
                btGuardar.setDisable(true);
                break;
            case 3:
                btGuardar.setDisable(true);
                break;
            default:
                break;
        }
    }
}
