package proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Factura {

    private String numPedido;
    private String cliente;
    private int empleado;
    private String nombreEmpleado;
    private String nombreCliente;
    private String fecha;
    private double total;
    private ArrayList<LineaFactura> lineas = new ArrayList<LineaFactura>();

//    Textos texto = new Textos();
    public Factura(String numPedido, String cliente, int empleado, String fecha) {
        this.numPedido = numPedido;
        this.cliente = cliente;
        this.empleado = empleado;
        this.fecha = fecha;
        //    setNombreEmpleado();
        setNombreCliente();
        setTotal();

    }
//pendiente de arreglar

    public void setTotal() {

        double total = 0;
        for (LineaFactura obj : lineas) {
            total = obj.getSubtotal() - (obj.getPrecio() * obj.getCantidad() * (obj.getDescuento() / 100) * 0.21);
        }
        this.total = total;
    }
//    public String getNombreEmpleado() {
//        return Empleado.JefeList(this.empleado);
//    }
//
//    public void setNombreEmpleado() {
//        this.nombreEmpleado = Empleado.JefeList(this.empleado);
//    }

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

//    public boolean getFechaTipoFecha(Fecha f1, Fecha f2) {
//        if (f1.mayorIgualQue(Fecha.stringToFecha(this.fecha)) && f2.menorIgualQue(Fecha.stringToFecha(this.fecha))) {
//            return true;
//        }
//        return false;
//    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<LineaFactura> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<LineaFactura> lineas) {
        this.lineas = lineas;
    }

    public String getDatosBusqueda() {
        return numPedido + " " + cliente;
    }

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

//    public void fetchLineas(){
//        for(LineaFactura obj:lineas){
//            this.obj.getNumPedido()
//        }
//    }
}
