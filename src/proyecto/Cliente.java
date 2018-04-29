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
public class Cliente{
   
    private String id;
    private String nombre;
    private String contacto;
    private String cargoContacto;
    private String direccion;
    private String ciudad;
    private String pais;
    private String telefono;
    private String observaciones;

    public Cliente(String id, String nombre, String contacto, String cargoContacto, String direccion, String ciudad, String pais, String telefono, String observaciones) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
        this.cargoContacto = cargoContacto;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.telefono = telefono;
        this.observaciones = observaciones;
    }
    public Cliente(String id, String nombre, String contacto, String cargoContacto, String direccion, String ciudad, String pais, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
        this.cargoContacto = cargoContacto;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.telefono = telefono;
    }
    

//    public Cliente(String nombre, String ciudad, String pais, String telefono) {
//        this.nombre = nombre;
//        this.ciudad = ciudad;
//        this.pais = pais;
//        this.telefono = telefono;
//    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCargoContacto() {
        return cargoContacto;
    }

    public void setCargoContacto(String cargoContacto) {
        this.cargoContacto = cargoContacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

            stmt.executeQuery();
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
            System.out.println("fillClientesList: "+ex.getMessage());
        }finally{
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String getDatosBusqueda(){
        return nombre+" "+contacto+" "+cargoContacto+" "+direccion+" "+ciudad+" "+pais;
    }
       
}
