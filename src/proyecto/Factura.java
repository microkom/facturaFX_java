package proyecto;

import java.util.ArrayList;
import java.util.Iterator;


public class Factura {

    private int numPedido;
    private String cliente;
    private String empleado;
    private String fecha;
    private ArrayList<LineaFactura> lineas = new ArrayList<LineaFactura>();

//    Textos texto = new Textos();
    public Factura(int numPedido, String cliente, String empleado, String fecha) {
        this.numPedido = numPedido;
        this.cliente = cliente;
        this.empleado = empleado;
        this.fecha = fecha;
    }

    public int getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(int numPedido) {
        this.numPedido = numPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<LineaFactura> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<LineaFactura> lineas) {
        this.lineas = lineas;
    }
    
    
}

