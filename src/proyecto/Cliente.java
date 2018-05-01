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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 *
 * @author German
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
                //rs.getString("observaciones")
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

    public String getDatosBusqueda() {
        return super.getId() + " " + super.getNombre() + " " + super.getContacto() + " " + super.getCargoContacto()
                + " " + super.getDireccion() + " " + super.getCiudad() + " " + super.getPais() + " " + super.getTelefono();
    }

}
