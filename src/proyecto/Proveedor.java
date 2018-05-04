/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 *
 * @author German
 */
public class Proveedor extends Persona {

    public Proveedor(String id, String nombre, String contacto, String cargoContacto, String direccion, String ciudad, String pais, String telefono) {
        super(id, nombre, contacto, cargoContacto, direccion, ciudad, pais, telefono);
    }

    public String getDatosBusqueda() {
        return super.id + " " + super.nombre + " " + super.contacto + " " + super.cargoContacto
                + " " + super.direccion + " " + super.ciudad + " " + super.pais + " " + super.telefono;
    }

    public String toString() {
        return super.nombre;
    }

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
