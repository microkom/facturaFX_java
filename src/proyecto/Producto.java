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
public class Producto {

    private int id;
    private String nombreProducto;
    private String proveedor;
    private String categoria;
    private int precio;
    private int existencia;

    public Producto(int id, String nombreProducto, String proveedor, String categoria, int precio, int existencia) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.precio = precio;
        this.existencia = existencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public String getDatosBusqueda() {
        return id + " " + nombreProducto + " " + proveedor + " " + categoria + " " + precio;
    }

    public static void fillProductosList(ObservableList<Producto> listaProductos) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs;
        PreparedStatement stmt;

        try {
            stmt = con.prepareStatement("SELECT * FROM productos");

            stmt.executeQuery();
            rs = stmt.executeQuery();
            while (rs.next()) {
                listaProductos.add(
                        new Producto( //int id, String nombreProducto, int proveedor, int categoria, double precio, int existencia) 
                                rs.getInt("idProducto"),
                                rs.getString("nomProducto"),
                                getNombreProveedor(rs.getInt("Proveedor")),
                                getNombreCategoria(rs.getInt("categoria")),
                                rs.getInt("precio"),
                                rs.getInt("existencias")));

            }
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            System.out.println("fillProductosList: " + ex.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String getNombreProveedor(int proveedor) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs;
        PreparedStatement stmt;
        String nombre = "";
        try {
            stmt = con.prepareStatement("SELECT nombre FROM proveedores where idproveedor=?");
            stmt.setInt(1, proveedor);
            rs = stmt.executeQuery();
            rs.first();
            nombre = rs.getString("nombre");
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getNombreProveedor: " + ex.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return nombre;
    }

    public static String getNombreCategoria(int categoria) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String nombre = "";
        try {
            stmt = con.prepareStatement("SELECT nomCategoria FROM categorias where idcategoria=?");
            stmt.setInt(1, categoria);
            rs = stmt.executeQuery();
            rs.first();
            nombre = rs.getString("nomCategoria");

        } catch (SQLException ex) {
            System.out.println("getNombreCategoria: " + ex.getMessage());
        } finally {
            try {
                stmt.close();
                rs.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("FINALLY: getNombreCategoria:  " + ex.getMessage());
            }
        }
        return nombre;
    }
}
