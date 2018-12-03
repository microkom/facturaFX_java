/*
finished commenting
 */
package proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javafx.collections.ObservableList;
import static proyecto.Producto.listaProveedor;

/**
 * Clase Proveedor, hereda de la clase Persona.
 *
 * @author German
 */
public class Proveedor extends Persona {

    public Proveedor(String id, String nombre, String contacto, String cargoContacto, String direccion, String ciudad, String pais, String telefono) {
        super(id, nombre, contacto, cargoContacto, direccion, ciudad, pais, telefono);
    }

      /**
     * Metodo que concatena todos los atributos de la clase para usarlos como 
     * datos de búsqueda
     * @return Texto que contiene valores de búsqueda.
     */
    public String getDatosBusqueda() {
        return super.id + " " + super.nombre + " " + super.contacto + " " + super.cargoContacto
                + " " + super.direccion + " " + super.ciudad + " " + super.pais + " " + super.telefono;
    }
    
    @Override
    public String toString() {
        return super.nombre;
    }

    /**
     * Método que rellena un ObservableList con los datos de los proveedores.
     * 
     * @param listaProveedor Contiene el listado de los proveedores almacenados en
     * la base de datos.
     */
    public static void fillProveedorList(ObservableList<Proveedor> listaProveedor) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM proveedores");
            rs = stmt.executeQuery();
            while (rs.next()) {
                listaProveedor.add(
                        new Proveedor(
                                rs.getString("idProveedor"),
                                rs.getString("Nombre"),
                                rs.getString("Contacto"),
                                rs.getString("CargoContacto"),
                                rs.getString("Direccion"),
                                rs.getString("Ciudad"),
                                rs.getString("Pais"),
                                rs.getString("Telefono")
                        ));
            }
        } catch (Exception ex) {
            System.out.println(" fillProveedorList :" + ex.getLocalizedMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(Proveedor.class.getName() + " Finally ->fillProveedorList :" + ex.getMessage());
            }
        }
    }

     /**
     * Método que averigua el nombre del proveedor usando como parámetro el id del
     * proveedor.
     * 
     * @param proveedor Número entero que identifica al proveedor.
     * @return Devuelve el nombre del proveedor como {@code String}
     */
    
    public static String getNombreProveedor(int proveedor) {

        boolean found = false;
        String nombre = "";
        Iterator<Proveedor> ite = listaProveedor.iterator();
        Proveedor obj;

        while (ite.hasNext() && found == false) {
            obj = ite.next();
            if (Integer.parseInt(obj.getId()) == proveedor) {
                nombre = obj.getNombre();
                found = true;
            }
        }
        return nombre;
    }

}
