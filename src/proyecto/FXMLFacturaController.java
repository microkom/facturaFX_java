/*
finished commenting.
 */
package proyecto;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Clase controladora del archivo FXMLFactura.fxml
 *
 * @author German Navarro
 */
public class FXMLFacturaController implements Initializable {

    private ObservableList<Factura> listaFacturas = FXCollections.observableArrayList();
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private ObservableList<LineaFactura> lineas = FXCollections.observableArrayList();
    private ObservableList<LineaFactura> mostrarLineas = FXCollections.observableArrayList();

    /**
     * Variable de clase privada: número que identifica al tipo de usuario
     * conectado.
     */
    private int tipoUsuario;

    /**
     * Variable de clase privada: número que identifica al empleado.
     */
    private int empleado;
    
    /**
     * Variable de clase privada: posición del item en el listado.
     */
    int posicionItem = 0;
    int posicionItemEnFactura = 0;
    
    /**
     * Variable de clase privada: almacena el número que identifica al producto.
     */
    int idProducto = 0;
    
    /**
     * Variable de clase privada: número de factura.
     */
    private int numFactura = 0;
    
    /**
     * Variable de clase privada: IVA de la factura.
     */
    private double IVA = 0;

    @FXML
    private Label lbNumFactura, lbFecha, lbCliente, lbTotal, lbIVA, lbSubtotalDescuento, lbDescuentoTotal, lbSubtotal;

    @FXML
    private TableView tvFactura;

    @FXML
    private TableColumn tcNumLinea, tcProducto, tcPrecio, tcCantidad, tcDescuento;

    @FXML
    private TextField tfDescuento, tfCantidad, tfLineaAActualizar;

    @FXML
    private ComboBox cbProductos;

    @FXML
    private Button btActualizar, btBorrar, btAgregar, btGuardar;

    private final ListChangeListener<LineaFactura> selectorItemLineaFactura = new ListChangeListener<LineaFactura>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends LineaFactura> p) {
            ponerItemSeleccionado();
        }
    };
    @FXML
    private TextField tfActCant, tfActDesc;

    /**
     * Método que existe por defecto, NO USADO.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Método que se carga al iniciar el controlador de la clase.
     *
     * @param numFactura Número de factura que se va a editar.
     * @param tipoUsuario Identifica el tipo de usuario.
     * @param empleado Identifica al empleado.
     */
    public void initVariable(int numFactura, int tipoUsuario, int empleado) {
        this.numFactura = numFactura;
        this.tipoUsuario = tipoUsuario;
        this.empleado = empleado;

        leerIva();

        try {
            Producto.fillProductosList(listaProductos);
            Factura.fillFacturasList(listaFacturas);
            LineaFactura.fillLineaFacturasList(lineas);

            seguridadSegunTipoUsuario();

            //int numFacturaTemporal = 10248;
            Iterator<Factura> ite = listaFacturas.iterator();
            Factura obj;

            boolean found = false;
            while (ite.hasNext() && found == false) {
                obj = ite.next();
                if (Integer.parseInt(obj.getNumPedido()) == numFactura) {
                    //mostrarLineas.add(obj);
                    lbNumFactura.setText(obj.getNumPedido());
                    lbFecha.setText(Fecha.stringToFecha(obj.getFecha()).corta());
                    lbCliente.setText(obj.getNombreCliente());
                    found = true;
                }
            }

            for (LineaFactura l : lineas) {
                if (l.getNumPedido() == numFactura) {
                    mostrarLineas.add(l);

                    tcNumLinea.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("numLinea"));
                    tcProducto.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("nombreProducto"));
                    tcPrecio.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("precio"));
                    tcCantidad.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("cantidad"));
                    tcDescuento.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("descuentoMostrar"));
                }
            }
        } catch (Exception ex) {
            System.out.println("Columnas Factura " + ex.getMessage());
        }
        totales();
        tvFactura.setItems(mostrarLineas);

        cbProductos.setItems(listaProductos);

        //listener para capturar la fila seleccionada de la linea de factura que se quiere borrar
        final ObservableList<LineaFactura> itemLineaFacturaSel
                = tvFactura.getSelectionModel().getSelectedItems();
        itemLineaFacturaSel.addListener(selectorItemLineaFactura);

        tfDescuento.setText("0");   //Cantidad establecida por defecto
        tfCantidad.setText("1");    //Cantidad establecida por defecto
    }

    /**
     * Consulta el IVA almacenado en la base de datos.
     */
    private void leerIva() {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM iva");
            rs = stmt.executeQuery();
            rs.first();
            this.IVA = rs.getDouble("valor") / 100;

        } catch (SQLException ex) {
            System.out.println("initVariable: " + ex.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, initVariable: " + ex.getMessage());
            }
        }
    }

    /**
     * Guarda la factura actualizada en la base de datos.
     */
    @FXML
    private void guardarFacturaFX() {

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE from lineasPedido where numPedido=?");
            stmt.setInt(1, this.numFactura);
            stmt.executeUpdate();

            for (LineaFactura obj : mostrarLineas) {

                int numLinea = obj.getNumLinea();
                int producto = obj.getProducto();
                int cantidad = obj.getCantidad();
                double precio = obj.getPrecio();
                double descuento = obj.getDescuento();
                double iva = (sumaSubtotal() - (descuento * sumaSubtotal())) * IVA;
                double subtotal = (precio * cantidad) - (descuento * sumaSubtotal()) + iva;

                stmt = con.prepareStatement("INSERT INTO lineasPedido ( NumLinea, "
                        + "numPedido, producto, precio, cantidad, descuento, iva, subtotal) "
                        + " VALUES (" + numLinea + "," + this.numFactura + ","
                        + producto + "," + precio + "," + cantidad + "," + descuento + "," + iva + "," + subtotal + ")");
                stmt.executeUpdate();
            }
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Registros");
            alert.setHeaderText(null);
            alert.setContentText("La factura se ha guardado correctamente");
            alert.showAndWait();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.out.println("Finally, guardarFactura: " + ex.getMessage());
            }
        }

    }

    /**
     * Suma todos los subtotales de las lineas que se muestran
     *
     * @return Devuelve el subtotal
     */
    public double sumaSubtotal() {
        double total = 0;

        for (LineaFactura obj : mostrarLineas) {
            //sumar los precios de todos los productos
            total += obj.getSubtotal();
        }
        return total;
    }

    /**
     * Calcula el descuento total de la factura.
     * @return Descuento total de la factura.
     */
    public double descuentoTotal() {
        double desc = 0;

        Iterator<LineaFactura> iterator = mostrarLineas.iterator();
        LineaFactura e = null;

        while (iterator.hasNext()) {
            e = iterator.next();

            //sumar los descuentos de todos los productos
            desc += e.getPrecio() * e.getCantidad() * (e.getDescuento());
        }
        return desc;
    }

    /**
     * Calcula el importe total de la factura con el IVA.
     *
     * @return El cálculo del (Subtotal - Descuento) * IVA.
     */
    public double importeTotalImpuestos() {
        //calculo del importe total más el IVA
        return (sumaSubtotal() - descuentoTotal()) * 0.21;
    }

    /**
     * Cálcula los valores Subtotal, Descuento, IVA, y Total que se muestran en
     * la factura.
     */
    private void totales() {

        lbSubtotal.setText(dosDecimales(sumaSubtotal()));
        lbDescuentoTotal.setText(dosDecimales(descuentoTotal()));
        lbSubtotalDescuento.setText(dosDecimales(sumaSubtotal() - descuentoTotal()));
        lbIVA.setText(dosDecimales(importeTotalImpuestos()));
        lbTotal.setText(dosDecimales(sumaSubtotal() - descuentoTotal() + importeTotalImpuestos()));
    }

    /**
     * Recibe un número y lo devuelve con 2 decimales.
     *
     * @param number Un número.
     * @return El mismo número recibido con dos decimales.
     */
    private String dosDecimales(double number) {
        return String.format("%.2f", number);
    }

    /**
     * Actualiza los valores de la linea seleccionada en la factura.
     */
    @FXML
    private void actualizarFX() {

        if (validateEmptyField("Debe ingresar la cantidad", tfActCant.getText().isEmpty())) {
            if (validateEmptyField("Debe ingresar el descuento", tfActDesc.getText().isEmpty())) {
                if (validateFormatNumber("Cantidad", tfActCant.getText())) {
                    int numLinea = setNumLinea();

                    double descuento = Double.parseDouble(tfActDesc.getText()) / 100;
                    int cantidad = Integer.parseInt(tfActCant.getText());
                    //int index = Integer.parseInt(tfLineaAActualizar.getText());
                    Producto obj = buscarProductoPorIndice(idProducto - 1);

                    double total = obj.getPrecio() * cantidad;

                    //linea de factura para agregar al Array
                    actualizaLinea(new LineaFactura(
                            numLinea, //numero de linea
                            Integer.parseInt(lbNumFactura.getText()), //num Factura
                            obj.getId(), //id producto
                            obj.getPrecio(), //precio
                            cantidad, //cantidad
                            descuento, //descuento
                            obj.getNombreProducto(),//nombre producto
                            total
                    ));
                }
            }
            tvFactura.refresh(); //actualiza las lineas de la factura con cualquier cambio
            totales();
        }
    }

    /**
     * Método que captura el objeto seleccionado.
     *
     * @return Un objeto del tipo LineaFactura que ha sido seleccionado.
     */
    public LineaFactura getItemSeleccionado() { //de aqui va a los textfields

        LineaFactura itemSeleccionado = null;
        if (tvFactura != null) {
            List<LineaFactura> tabla = tvFactura.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                itemSeleccionado = tabla.get(0);
                return itemSeleccionado;
            }
        }
        return itemSeleccionado;
    }

    /**
     * Método que muestra en un formulario el objeto seleccionado.
     */
    public void ponerItemSeleccionado() {
        final LineaFactura item = getItemSeleccionado();
        posicionItem = lineas.indexOf(item);
        if (item != null) {
            idProducto = item.getProducto();
            posicionItemEnFactura = item.getNumLinea() - 1;
            tfLineaAActualizar.setText(Integer.toString(item.getNumLinea()));
        }
    }

    /**
     * Borra la linea de la factura seleccionada.
     */
    @FXML
    public void borrarLineaDeFacturaFX() {
        if (validateEmptyField("Debe seleccionar un articulo para borrarlo", tfLineaAActualizar.getText().isEmpty())) {
            int al = Integer.parseInt(tfLineaAActualizar.getText()) - 1;
            //mostrarLineas.remove(Integer.parseInt(tfLineaAActualizar.getText()) - 1);
            mostrarLineas.remove(posicionItemEnFactura);
            setNumLinea();
            tvFactura.refresh();
            tfLineaAActualizar.clear();
            totales();
        }
    }

    /**
     * Agrega una serie de datos de un producto a una linea de la factura.
     */
    @FXML
    public void pasarItemAListaFacturaFX() {

        if (validateEmptyField("Debe ingresar la cantidad", tfCantidad.getText().isEmpty())) {
            if (validateEmptyField("Debe ingresar el descuento", tfDescuento.getText().isEmpty())) {
                if (validateFormatNumber("Cantidad", tfCantidad.getText())) {
                    if (validateFormatNumber("Descuento", tfDescuento.getText())) {
                        int numLinea = setNumLinea();

                        double descuento = Double.parseDouble(tfDescuento.getText()) / 100;
                        int cantidad = Integer.parseInt(tfCantidad.getText());
                        int index = cbProductos.getSelectionModel().getSelectedIndex();

                        Producto obj = buscarProductoPorIndice(index);

                        double total = obj.getPrecio() * cantidad;

                        //linea de factura para agregar al Array
                        nuevaLinea(new LineaFactura(
                                numLinea, //numero de linea
                                Integer.parseInt(lbNumFactura.getText()), //num Factura
                                obj.getId(), //id producto
                                obj.getPrecio(), //precio
                                cantidad, //cantidad
                                descuento, //descuento
                                obj.getNombreProducto(),//nombre producto
                                total
                        ));
                    }
                }
            }
            tvFactura.refresh(); //actualiza las lineas de la factura con cualquier cambio
            totales();
        }
    }

    /**
     * Busca un producto en un listado usando el número del índice que se recibe
     * por parámetro.
     *
     * @param index Número que indica la posición del producto en un listado.
     * @return Un objeto de la Clase Producto.
     */
    public Producto buscarProductoPorIndice(int index) {
        Producto pro = listaProductos.get(index);
        return pro;
    }

    /**
     * Agrega datos a la línea de la factura. Antes de agregar un producto
     * comprueba primero si el producto ya existe, si es así, entonces lo suma a
     * la cantidad del producto ya existente.
     *
     * @param lineaF Objeto del tipo LineaFactura que contiene datos para
     * agregar a una linea.
     */
    public void nuevaLinea(LineaFactura lineaF) {

        int j = 0;

        //comprobar existencia del producto
        j = buscarProductoId(lineaF.getProducto());

        //comprobación de que el producto ya aparece en la factura o no 
        if (j != -1) {

            //suma de cantidades 
            mostrarLineas.get(j).setCantidad(mostrarLineas.get(j).getCantidad() + lineaF.getCantidad());

            //redefinir descuento con el que se recibe por teclado
            mostrarLineas.get(j).setDescuento(lineaF.getDescuento());
            mostrarLineas.get(j).setDescuentoMostrar(lineaF.getDescuento() * 100); //mostrar descuento en factura editable

            //calcular subtotal 
            mostrarLineas.get(j).setSubtotal(
                    lineaF.getPrecio() * mostrarLineas.get(j).getCantidad());
        } else {
            mostrarLineas.add(lineaF);
        }

    }

    /**
     * Método que actualiza un producto que ya está en la lista de las facturas.
     *
     * @param lineaF Objeto del tipo LineaFactura que contiene datos para
     * agregar a una linea.
     */
    public void actualizaLinea(LineaFactura lineaF) {

        int j = 0;

        //comprobar existencia del producto
        j = buscarProductoId(lineaF.getProducto());

        //comprobación de que el producto ya aparece en la factura o no 
        if (j != -1) {

            //suma de cantidades 
            mostrarLineas.get(j).setCantidad(lineaF.getCantidad());

            //redefinir descuento con el que se recibe por teclado
            mostrarLineas.get(j).setDescuento(lineaF.getDescuento());
            mostrarLineas.get(j).setDescuentoMostrar(lineaF.getDescuento() * 100); //mostrar descuento en factura editable

            //calcular subtotal 
            mostrarLineas.get(j).setSubtotal(
                    lineaF.getPrecio() * mostrarLineas.get(j).getCantidad());
        } else {
            mostrarLineas.add(lineaF);
        }

    }

    /**
     * Recibe un id de un producto, lo busca en el ArrayList y si lo encuentra
     * devuelve su posición en el array.
     *
     * @param producto Número de id del producto a buscar.
     * @return {@code} Un número que indica la posición del producto en el
     * array.
     */
    public int buscarProductoId(int producto) {
        int numIndex = -1;
        boolean found = false;

        Iterator<LineaFactura> iterator = mostrarLineas.iterator();
        LineaFactura obj = null;

        while (iterator.hasNext() && found == false) {
            obj = iterator.next();

            //comparar los nombres del producto con el que han pasado por parametro
            if (obj.getProducto() == producto) {

                //asignar el valor de la linea donde se encontró el nombre a la variable
                numIndex = mostrarLineas.indexOf(obj);

                //variable de control para salir del bucle
                found = true;
            }
        }
        return numIndex;
    }

    /**
     * Genera un número para las lineas de la factura de modo consecutivo.
     */
    private int setNumLinea() {
        int numLinea = 1;
        for (LineaFactura obj : mostrarLineas) {
            obj.setNumLinea(numLinea);
            numLinea++;
        }
        return numLinea;
    }

    /**
     * Comprueba que el campo evaluado está vacío
     *
     * @param text Texto que se muestra en la ventana de advertencia
     * @param field Campo que se comprueba
     * @return {@code false} si está vacío, {@code true} si contiene información
     */
    private boolean validateEmptyField(String text, boolean field) {
        if (field) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validación de campos");
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Valida si los caracteres recibidos son un número o no.
     *
     * @param texto Texto para mostrar en la ventana de advertencia.
     * @param numero Número que se comprueba
     * @return {@code false} si es un número, {@code true} si no lo es.
     */
    private boolean validateFormatNumber(String texto, String numero) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(numero);
        if (m.find() && m.group().equals(numero)) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validación de números");
            alert.setHeaderText(null);
            alert.setContentText("El valor " + texto + " no es número");
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Obtiene el número de la última factura.
     *
     * @return El número de la factura
     */
    public String ultimaFactura() {
        return listaFacturas.get(listaFacturas.size() - 1).getNumPedido();
    }

    /**
     * Compara el indice de la última factura con el valor del indice de la
     * factura recibida por parametro.
     *
     * @return {@code true} si los índices de las facturas son iguales,
     * {@code false} si no lo son.
     */
    public boolean facturaIsLast() {
        if (Integer.parseInt(ultimaFactura()) == this.numFactura) {
            return true;
        }
        return false;
    }

    /**
     * Comprueba si la factura es la última y el tipo de usuario para habilitar
     * funciones
     */
    public void seguridadSegunTipoUsuario() {
        switch (tipoUsuario) {
            case 1:
                if (!facturaIsLast()) {
                    btBorrar.setVisible(false);
                    btAgregar.setVisible(false);
                    btActualizar.setVisible(false);
                    btBorrar.setVisible(false);
                    btAgregar.setVisible(false);
                    tfDescuento.setVisible(false);
                    tfCantidad.setVisible(false);
                    tfLineaAActualizar.setVisible(false);
                    cbProductos.setVisible(false);
                    tfActCant.setVisible(false);
                    tfActDesc.setVisible(false);
                    btGuardar.setVisible(false);
                }
                ;
                break;
            case 2:
                if (!facturaIsLast()) {
                    btBorrar.setVisible(false);
                    btAgregar.setVisible(false);
                    btActualizar.setVisible(false);
                    btBorrar.setVisible(false);
                    btAgregar.setVisible(false);
                    tfDescuento.setVisible(false);
                    tfCantidad.setVisible(false);
                    tfLineaAActualizar.setVisible(false);
                    cbProductos.setVisible(false);
                    tfActCant.setVisible(false);
                    tfActDesc.setVisible(false);
                    btGuardar.setVisible(false);
                }
                ;
                break;
            case 3:
                btActualizar.setVisible(false);
                btBorrar.setVisible(false);
                btAgregar.setVisible(false);
                tfDescuento.setVisible(false);
                tfCantidad.setVisible(false);
                tfLineaAActualizar.setVisible(false);
                cbProductos.setVisible(false);
                tfActCant.setVisible(false);
                tfActDesc.setVisible(false);
                btGuardar.setVisible(false);
                break;
        }
    }
}
