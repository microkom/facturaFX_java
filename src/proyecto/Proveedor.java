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
import javafx.collections.ObservableList;

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
        return super.getId() + " " + super.getNombre() + " " + super.getContacto() + " " + super.getCargoContacto()
                + " " + super.getDireccion() + " " + super.getCiudad() + " " + super.getPais() + " " + super.getTelefono();
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
                                //id, nombre, contacto, cargoContacto, direccion, ciudad, pais, telefono
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

}
