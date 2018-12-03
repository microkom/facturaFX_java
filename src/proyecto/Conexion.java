package proyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para realizar conexión y desconexión del servidor MySql.
 *
 * @author German Navarro
 */
public class Conexion {

    /**
     * Mëtodo para conectarse al servidor MySql
     * 
     * @return 
     */
    public Connection conectar() {

        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/tienda";
            String user = "root";
            String pass = "";
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return con;
    }

    /**
     * Método para desconectarse del servidor MySql.
     *
     * @param con Objeto de tipo Connection.
     */
    public void desconectar(Connection con) {

        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Ha sido imposible cerrar la conexion");
        }
    }
}
