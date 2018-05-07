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
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author German
 */
public class LineaFactura {
    
    private int numLinea;
    private int numPedido;
    private int producto;
    private double precio;
    private int cantidad;
    private double descuento;
    private String nombreProducto;
    private double subtotal;
    

    public LineaFactura(int numLinea, int numPedido, int producto, double precio, int cantidad, double descuento, String nombreProducto, double subtotal) {
        this.numLinea = numLinea;
        this.numPedido = numPedido;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.nombreProducto = nombreProducto;
        this.subtotal = subtotal;
    }
    public LineaFactura(int numLinea, int numPedido, int producto, double precio, int cantidad, double descuento) {
        this.numLinea = numLinea;
        this.numPedido = numPedido;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        setNombreProducto();
 
    }
    public LineaFactura(int numLinea, int numPedido, int producto, double precio, int cantidad, double descuento,  double subtotal) {
        this.numLinea = numLinea;
        this.numPedido = numPedido;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.subtotal = subtotal;
        setNombreProducto();
 
    }

    public void setNombreProducto(){
        this.nombreProducto = Producto.getNombreProducto(this.producto);
    }
    public String getNombreProducto(){
        return Producto.getNombreProducto(this.producto);
    }
    
    public int getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(int numLinea) {
        this.numLinea = numLinea;
    }

    public int getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(int numPedido) {
        this.numPedido = numPedido;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    //recibe un double y lo devuelve como String con 2 decimales
    //
    private String dosDecimales(double number) {
        return String.format("%.2f", number);
    }
    
     public static void fillLineaFacturasList(ObservableList<LineaFactura> listaFacturas) {

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM lineaspedido");
            rs = stmt.executeQuery();
            while (rs.next()) {
                double p = rs.getDouble("precio");
                int c = rs.getInt("cantidad");
                double d = rs.getDouble("descuento");
                double subtotal =  0;
                if (d >0){
                    subtotal = p * c * ((100-d)/100);
                }else{
                    subtotal = p * c;
                }
                
                listaFacturas.add(
                        new LineaFactura( //int numLinea, int numPedido, int producto, double precio, 
                                //int cantidad, double descuento, String nombreProducto, double subtotal
                                rs.getInt("numLinea"),
                                rs.getInt("numPedido"),
                                rs.getInt("producto"),
                                rs.getDouble("precio"),
                                rs.getInt("cantidad"),
                                rs.getDouble("descuento"),
                                //rs.getString("nombreProducto"),
                                subtotal                                
                        ));
            }

        } catch (SQLException ex) {
            System.out.println("fillLineaFacturasList: " + ex.getMessage());
        } finally {
            try {
                stmt.close();
                rs.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, fillLineaFacturasList: " + ex.getMessage());
            }
        }
    }
}
