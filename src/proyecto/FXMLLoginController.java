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
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DAW
 */
public class FXMLLoginController implements Initializable {

    private String usuario;
    private String contrasena;
    private ResultSet rs;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    @FXML
    private PasswordField tfPass;
    @FXML
    private TextField tfUser;
    @FXML
    private Button bt_login;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfUser.setText("andrew"); //borrar
        tfPass.setText("123"); //borrar

        enterWithEnter();

    }

    private void enterWithEnter() {
        tfPass.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println(e.getCode());
                usuarioPass();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                System.out.println(e.getCode());
                Stage stage2 = (Stage) bt_login.getScene().getWindow();
                stage2.close();
            }
        });
        tfUser.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println(e.getCode());
                usuarioPass();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                System.out.println(e.getCode());
                Stage stage2 = (Stage) bt_login.getScene().getWindow();
                stage2.close();
            }
        });
        bt_login.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println(e.getCode());
                usuarioPass();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                System.out.println(e.getCode());
                Stage stage2 = (Stage) bt_login.getScene().getWindow();
                stage2.close();
            }
        });
    }

    @FXML
    private void usuarioPass() {
        PreparedStatement stmt;
        usuario = tfUser.getText();
        contrasena = tfPass.getText();
        tfUser.setPromptText("Usuario");
        tfPass.setPromptText("Contraseña");
        try {

            stmt = con.prepareStatement("SELECT * from usuarios where usuario=? and contrasena=?");
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            rs = stmt.executeQuery();

            if (rs.next()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMainMenu.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    FXMLMainMenuController controller = fxmlLoader.<FXMLMainMenuController>getController();
                    controller.setUser(usuario);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));

                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();

                    stage.setTitle("..::Menú Principal::..");

                    //cierra la ventana abierta anteriormente
                    Stage stage2 = (Stage) bt_login.getScene().getWindow();
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
//    
//    public void setUser(String usuario){
//         this.usuario = usuario;
//    }
}
