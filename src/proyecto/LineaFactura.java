/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
    
    
    public String getNombreProducto(){
        
        ArrayList<Producto> productos = new ArrayList<Producto>();
        
        boolean found = false;
        Iterator<Producto> pro = productos.iterator();
        Producto obj = null;
        while (pro.hasNext() && found == false) {
           if (obj.getId() == this.producto){
               this.nombreProducto = obj.getNombreProducto();
               found = true;
           }
        }
        return this.nombreProducto;
    }
    
}
