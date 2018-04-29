/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author DAW
 */
public class FXMLLoginController implements Initializable {

    private ResultSet rs;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    @FXML
    private PasswordField tfPass;
    @FXML
    private TextField tfUser;
    @FXML
    private Button login;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void usuarioPass() {
        PreparedStatement stmt;
        tfUser.setPromptText("Usuario");
        tfPass.setPromptText("Contraseña");
        try {

            stmt = con.prepareStatement("SELECT * from empleados where usuario=? and contrasena=?");
            stmt.setString(1, tfUser.getText());
            stmt.setString(2, tfPass.getText());
            rs = stmt.executeQuery();

            if (rs.next()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLFacturacion.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));

                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();

                    stage.setTitle("..::FACTURACION::..");

                    //cierra la ventana abierta anteriormente
                    Stage stage2 = (Stage) login.getScene().getWindow();
                    stage2.close();

                } catch (Exception ex) {
                    ex.getMessage();
                }
                System.out.println("login ok");
            } else {
                System.out.println("login failed");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("USUARIO: " + ex.getMessage());
        }
    }
}
