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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase controladora del archivo FXMLLogin.fxml
 *
 * @author German Navarro
 */
public class FXMLLoginController implements Initializable {

    /**
     * Variable de clase privada: usuario.
     */
    private String usuario;

    /**
     * Variable de clase privada: contraseña.
     */
    private String contrasena;

    /**
     * Variable de clase privada: número que identifica al tipo de usuario
     * conectado.
     */
    private int tipoUsuario;

    /**
     * Variable de clase privada: número que identifica al empleado.
     */
    private int empleado;

    private ResultSet rs;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    @FXML
    private PasswordField tfPass;
    @FXML
    private TextField tfUser;
    @FXML
    private Button bt_login;
    @FXML
    private AnchorPane apLogin;

    /**
     * Método que carga al iniciar el controlador de la clase.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfUser.setText("andrew");   //puesto temporalmente para acceder a la app más rapido
        tfPass.setText("123");      //puesto temporalmente para acceder a la app más rapido

        enterWithEnter();

    }

    /**
     * Método que cierra la ventana al presionar la tecla ESC o al hacer click
     * sobre la ventana.
     */
    private void enterWithEnter() {
        apLogin.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                usuarioPass();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                Stage stage2 = (Stage) bt_login.getScene().getWindow();
                stage2.close();
            }
        });
    }

    /**
     * Consulta en la base datos el si el usuario y contraseña ingresados
     * existen en ella.
     */
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
                    this.tipoUsuario = rs.getInt("tipo");
                    this.empleado = rs.getInt("empleado");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMainMenu.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    FXMLMainMenuController controller = fxmlLoader.<FXMLMainMenuController>getController();
                    controller.initVariable(this.tipoUsuario, this.empleado);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));

                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.getIcons().add(new Image("s_supermarket.png"));
                    stage.show();

                    stage.setTitle(" ·· Menú Principal·· ");

                    //cierra la ventana abierta anteriormente
                    Stage stage2 = (Stage) bt_login.getScene().getWindow();
                    stage2.close();

                } catch (IOException | SQLException ex) {
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
