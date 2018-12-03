/*
finished commenting
*/
package proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 * Clase Cliente, hereda de la clase Persona.
 *
 * @author German Navarro
 * @see Persona
 */
public class Cliente extends Persona {

    private String observaciones;

    public Cliente(String observaciones, String id, String nombre, String contacto, String cargoContacto, String direccion, String ciudad, String pais, String telefono) {
        super(id, nombre, contacto, cargoContacto, direccion, ciudad, pais, telefono);
        this.observaciones = observaciones;
    }

    public Cliente(String id, String nombre, String contacto, String cargoContacto, String direccion, String ciudad, String pais, String telefono) {
        super(id, nombre, contacto, cargoContacto, direccion, ciudad, pais, telefono);
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Método para rellenar un ObservableList con los clientes.
     *
     * @param listaClientes ObservableList que ha de rellenarse usando datos de
     * la base de datos
     */
    public static void fillClientesList(ObservableList<Cliente> listaClientes) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM Clientes");
            rs = stmt.executeQuery();
            while (rs.next()) {
                listaClientes.add(
                        new Cliente(
                                rs.getString("IdCliente"),
                                rs.getString("Nombre"),
                                rs.getString("Contacto"),
                                rs.getString("CargoContacto"),
                                rs.getString("Direccion"),
                                rs.getString("Ciudad"),
                                rs.getString("Pais"),
                                rs.getString("Telefono")));
            }

        } catch (SQLException ex) {
            System.out.println("fillClientesList: " + ex.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metodo que concatena los valores: 'id, nombre, contacto, direccion, ciudad
     * pais, telefono' para usarlos como datos de búsqueda
     * @return Texto que contiene valores de búsqueda.
     */
    public String getDatosBusqueda() {
        return super.getId() + " " + super.getNombre() + " " + super.getContacto() 
                + " " + super.getCargoContacto() + " " + super.getDireccion() + " " 
                + super.getCiudad() + " " + super.getPais() + " " 
                + super.getTelefono();
    }

    /**
     * Método que averigua el nombre de un cliente usando el id del cliente.
     * @param cliente Texto que identifica al cliente.
     * @return Devuelve el nombre del cliente
     */
    public static String nombreCliente(String cliente) {

        ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();
        boolean found = false;
        String nombre = "";
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM Clientes");
            rs = stmt.executeQuery();
            while (rs.next()) {
                listaClientes.add(
                        new Cliente(
                                rs.getString("IdCliente"),
                                rs.getString("Nombre"),
                                rs.getString("Contacto"),
                                rs.getString("CargoContacto"),
                                rs.getString("Direccion"),
                                rs.getString("Ciudad"),
                                rs.getString("Pais"),
                                rs.getString("Telefono")));
            }
        } catch (SQLException ex) {
            System.out.println("nombreCliente: " + ex.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, nombreCliente: " + ex.getMessage());
            }
        }
        Iterator<Cliente> ite = listaClientes.iterator();
        Cliente obj;

        while (ite.hasNext() && found == false) {
            obj = ite.next();
            if (obj.getId().equals(cliente)) {
                nombre = obj.nombre;
                found = true;
            }
        }
        return nombre;
    }
}
