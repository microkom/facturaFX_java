/*
finished commenting
*/
package proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;

/**
 * Clase LineaFactura.
 *
 * @author German
 */
public class LineaFactura {

    /**
     * Variable de clase privada: número que identifica la linea.
     */
    private int numLinea;

    /**
     * Variable de clase privada: número de la factura.
     */
    private int numPedido;

    /**
     * Variable de clase privada: ID que identifica al producto.
     */
    private int producto;

    /**
     * Variable de clase privada: precio del producto.
     */
    private double precio;

    /**
     * Variable de clase privada: cantidad del producto.
     */
    private int cantidad;

    /**
     * Variable de clase privada: descuento aplicado.
     */
    private double descuento;

    /**
     * Variable de clase privada: nombre del producto.
     */
    private String nombreProducto;

    /**
     * Variable de clase privada: subtotal de la linea.
     */
    private double subtotal;

    /**
     * Variable de clase privada: descuento que se muestra en la linea.
     */
    private double descuentoMostrar;

    public LineaFactura(int numLinea, int numPedido, int producto, double precio, int cantidad, double descuento, String nombreProducto, double subtotal) {
        this.numLinea = numLinea;
        this.numPedido = numPedido;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.nombreProducto = nombreProducto;
        this.subtotal = subtotal;
        this.descuentoMostrar = this.descuento * 100;
    }

    public LineaFactura(int numLinea, int numPedido, int producto, double precio, int cantidad, double descuento) {
        this.numLinea = numLinea;
        this.numPedido = numPedido;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        setNombreProducto();
        this.descuentoMostrar = this.descuento * 100;
    }

    public LineaFactura(int numLinea, int numPedido, int producto, double precio, int cantidad, double descuento, double subtotal) {
        this.numLinea = numLinea;
        this.numPedido = numPedido;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.subtotal = subtotal;
        setNombreProducto();
        this.descuentoMostrar = this.descuento * 100;
    }

    public double getDescuentoMostrar() {
        return descuentoMostrar;
    }

    public void setDescuentoMostrar(double descuentoMostrar) {
        this.descuentoMostrar = descuentoMostrar;
    }

    public void setNombreProducto() {
        this.nombreProducto = Producto.getNombreProducto(this.producto);
    }

    public String getNombreProducto() {
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

    /**
     * Muestra siempre los últimos dos decimales del número recibido por
     * parámetro.
     *
     * @param number Valor del que se quieren mostrar dos decimales
     * @return El mismo valor recibido con dos decimales.
     */
    private String dosDecimales(double number) {
        return String.format("%.2f", number);
    }

    /**
     * Método que rellena un ObservableList con los datos las lineas de facturas.
     *
     * @param listaFacturas Contiene el listado de las lineas de facturas
     * almacenados en la base de datos.
     */
    public static void fillLineaFacturasList(ObservableList<LineaFactura> listaFacturas) {

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM lineaspedido");
            rs = stmt.executeQuery();
            while (rs.next()) {
                double subtotalL = 0;
                subtotalL = rs.getDouble("precio") * rs.getInt("cantidad");

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
                                subtotalL
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
