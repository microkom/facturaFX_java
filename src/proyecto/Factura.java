/*
finished commenting
*/
package proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase Factura.
 * 
 * @author German Navarro.
 */
public class Factura {

    /**
     * Variable de clase privada: Número de la factura, se guarda como numPedido
     * en la base de datos.
     */
    private String numPedido;

    /**
     * Variable de clase privada: Nombre del cliente.
     */
    private String cliente;

    /**
     * Variable de clase privada: ID del empleado.
     */
    private int empleado;

    /**
     * Variable de clase privada: Nombre del empleado.
     */
    private String nombreEmpleado;

    /**
     * Variablde de clase privada: Nombre del cliente.
     */
    private String nombreCliente;

    /**
     * Variable de clase privada: Fecha de la factura.
     */
    private String fecha;

    /**
     * Variable de clase privada: Valor total de la factura.
     */
    private double total;

    /**
     * Variable de clase privada: Lista de las facturas.
     */
    private ObservableList<LineaFactura> listaF = FXCollections.observableArrayList();

    public Factura(String numPedido, String cliente, int empleado, String fecha) {
        this.numPedido = numPedido;
        this.cliente = cliente;
        this.empleado = empleado;
        this.fecha = fecha;
        setNombreCliente();
    }

    public String getNombreCliente() {
        return Cliente.nombreCliente(this.cliente);
    }

    public void setNombreCliente() {
        this.nombreCliente = Cliente.nombreCliente(this.cliente);
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getEmpleado() {
        return empleado;
    }

    public void setEmpleado(int empleado) {
        this.empleado = empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Metodo que concatena los valores: 'numPedido, cliente' para usarlos como
     * datos de búsqueda
     *
     * @return Texto que contiene valores de búsqueda.
     */
    public String getDatosBusqueda() {
        return numPedido + " " + cliente;
    }

    /**
     * Método que rellena un ObservableList con las facturas existentes.
     *
     * @param listaFacturas Contiene el listado de las facturas almacenadas en
     * la base de datos.
     */
    public static void fillFacturasList(ObservableList<Factura> listaFacturas) {

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM pedidos");
            rs = stmt.executeQuery();
            while (rs.next()) {
                listaFacturas.add(
                        new Factura( //int numPedido, String cliente, int empleado, String fecha 
                                rs.getString("numPedido"),
                                rs.getString("cliente"),
                                rs.getInt("empleado"),
                                rs.getString("fecha")
                        ));
            }
        } catch (SQLException ex) {
            System.out.println("fillFacturasList: " + ex.getMessage());
        } finally {
            try {
                stmt.close();
                rs.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, fillFacturasList: " + ex.getMessage());
            }
        }
    }
}
