/*
finished commenting.
 */
package proyecto;

import java.io.IOException;
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
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase controladora del archivo FXMLFacturacion.fxml
 *
 * @author German Navarro
 */
public class FXMLFacturacionController implements Initializable {

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
     * Variable de clase privada: IVA de la factura.
     */
    private double IVA = 0;

    /**
     * Variable de clase privada: número que identifica el cliente.
     */
    private String idCliente = "";

    /**
     * Variable de clase privada: número que identifica el producto.
     */
    private int idProducto = 0;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private FilteredList<Cliente> listaClientesFiltrada = new FilteredList(listaClientes, obj -> true);
    int posicionCliente = 0;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private FilteredList<Producto> listaProductosFiltrada = new FilteredList(listaProductos, obj -> true);
    int posicionProducto = 0;

    private ObservableList<LineaFactura> lineaFacturaList = FXCollections.observableArrayList();
    int posicionItem = 0;

    @FXML
    private Label lbFecha, lbNumPedido, lbIVA, lbDescuentoTotal, lbTotal, lbSubtotal, lbSubtotalDescuento, lbTextoFecha;

    @FXML
    private TableView<Cliente> tablaBusquedaCliente;
    @FXML
    private TableColumn<Cliente, String> tCnombre;

    @FXML
    private TableView<Producto> tablaBusquedaProducto;
    @FXML
    private TableColumn<Producto, String> tCnombreProducto;

    @FXML
    private TableView<LineaFactura> tablaLineaFactura;
    @FXML
    private TableColumn<LineaFactura, String> tCnumLinea, tCnombreProductoFactura, tCprecio, tCcantidad, tCdescuento, tCsubtotal;

    @FXML
    private TextField tfNombreCliente, tfBusquedaClientes,
            tfBusquedaProductos, tfNombreProducto, tfCantidadLineaFactura, tfDescuentoLineaFactura,
            tfLineaABorrar;

    @FXML
    private Button btAgregarItemALineaFactura, btBorrarLinea, btGuardarFactura, bt_menuPrincipal;
    ;

    private final ListChangeListener<Cliente> selectorTablaClientes = new ListChangeListener<Cliente>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Cliente> c) {
            ponerClienteSeleccionado();
        }
    };

    private final ListChangeListener<Producto> selectorTablaProductos = new ListChangeListener<Producto>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Producto> p) {
            ponerProductoSeleccionado();
        }
    };
    private final ListChangeListener<LineaFactura> selectorItemLineaFactura = new ListChangeListener<LineaFactura>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends LineaFactura> p) {
            ponerItemSeleccionado();
        }
    };
    @FXML
    private Pane pnRightFact, pnLeftFact;
    @FXML
    private AnchorPane pnAnchor3;

    /**
     * Método que existe por defecto, NO USADO.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Método que carga al iniciar el controlador de la clase.
     *
     * @param tipoUsuario Identifica el tipo de usuario.
     * @param empleado Identifica al empleado.
     */
    public void initVariable(int tipoUsuario, int empleado) {
        this.tipoUsuario = tipoUsuario;
        this.empleado = empleado;

        leerIva();

        try {
            //CLIENTES
            //rellenar lista de clientes en listado
            Cliente.fillClientesList(listaClientes);

            tablaBusquedaCliente.setItems(listaClientesFiltrada);

            //busqueda en tiempo real por nombre, contacto, cargo contacto, ciudad. Tiene en cuenta las tildes 
            tfBusquedaClientes.setOnKeyReleased(keyEvent -> {
                listaClientesFiltrada.setPredicate(obj -> obj.getDatosBusqueda().toLowerCase().contains(tfBusquedaClientes.getText().toLowerCase().trim()));
            });
            //Valores para rellenar la vista de la tabla
            tCnombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));

            //En el initialize añadimos el «Listener» al TableView
            final ObservableList<Cliente> tablaClienteSel
                    = tablaBusquedaCliente.getSelectionModel().getSelectedItems();

            tablaClienteSel.addListener(selectorTablaClientes);
        } catch (Exception ex) {
            System.out.println("Relacionado con Clientes en el initialize: " + ex.getMessage());
        }

        try {
            //PRODUCTOS
            //rellenar lista de productos
            Producto.fillProductosList(listaProductos);

            //lista de productos para filtrar
            tablaBusquedaProducto.setItems(listaProductosFiltrada);

            //BUSQUEDA EN TIEMPO REAL POR NOMBRE
            tfBusquedaProductos.setOnKeyReleased(keyEvent -> {
                listaProductosFiltrada.setPredicate(obj -> obj.getNombreProducto().toLowerCase().contains(tfBusquedaProductos.getText().toLowerCase().trim()));
            });
            //Valores para rellenar la vista de la tabla
            tCnombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));

            //En el initialize añadimos el «Listener» al TableView
            final ObservableList<Producto> tablaProductoSel
                    = tablaBusquedaProducto.getSelectionModel().getSelectedItems();
            tablaProductoSel.addListener(selectorTablaProductos);
        } catch (Exception ex) {
            System.out.println("Relacionado con Productos en el initialize: " + ex.getMessage());
        }

        //LINEA FACTURA
        //cargar número de factura a pantalla
        lbNumPedido.setText(Integer.toString(nuevoNumeroFactura()));

        //carga de datos a tabla de lineas de factura
        tablaLineaFactura.setItems(lineaFacturaList);
        tCnumLinea.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("numLinea"));
        tCnombreProductoFactura.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("nombreProducto"));
        tCprecio.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("precio"));
        tCcantidad.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("cantidad"));
        tCdescuento.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("descuento"));
        tCsubtotal.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("subtotal"));

        //listener para capturar la fila seleccionada de la linea de factura que se quiere borrar
        final ObservableList<LineaFactura> itemLineaFacturaSel
                = tablaLineaFactura.getSelectionModel().getSelectedItems();
        itemLineaFacturaSel.addListener(selectorItemLineaFactura);

        //texto por defecto al iniciar la factura
        tfCantidadLineaFactura.setText("1");
        tfDescuentoLineaFactura.setText("0");

        //********************************
        //  FECHA
        //********************************
        lbFecha.setText(Fecha.hoyDiaMesAnyo());

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

    //************************************************
    //  CLIENTES
    //************************************************
    /**
     * Método, que se cuando realiza una búsqueda, captura el objeto
     * seleccionado
     *
     * @return Un objeto del tipo Cliente que ha sido seleccionado.
     */
    public Cliente getTablaClientesSeleccionado() { //de aqui va a los textfields

        Cliente clienteSeleccionado = null;
        if (tablaBusquedaCliente != null) {
            List<Cliente> tabla = tablaBusquedaCliente.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                clienteSeleccionado = tabla.get(0);
                return clienteSeleccionado;
            }
        }
        return clienteSeleccionado;
    }

    /**
     * Método, que cuando se realiza una búsqueda, muestra en el formulario el
     * objeto seleccionado.
     */
    public void ponerClienteSeleccionado() {
        final Cliente cliente = getTablaClientesSeleccionado();
        posicionCliente = listaClientes.indexOf(cliente);
        if (cliente != null) {
            idCliente = cliente.getId();
            tfNombreCliente.setText(cliente.getNombre());
        }
    }

    //************************************************
    //  PRODUCTOS
    //************************************************
    /**
     * Método, que se cuando realiza una búsqueda, captura el objeto
     * seleccionado
     *
     * @return Un objeto del tipo Producto que ha sido seleccionado.
     */
    public Producto getTablaProductoSeleccionado() { //de aqui va a los textfields

        Producto productoSeleccionado = null;
        try {
            if (tablaBusquedaProducto != null) {
                List<Producto> tabla = tablaBusquedaProducto.getSelectionModel().getSelectedItems();
                if (tabla.size() == 1) {
                    productoSeleccionado = tabla.get(0);
                    return productoSeleccionado;
                }
            }
        } catch (Exception ex) {
            System.out.println("getTablaProductoSeleccionado: " + ex.getMessage());
        }
        return productoSeleccionado;
    }

    /**
     * Método, que cuando se realiza una búsqueda, muestra en el formulario el
     * objeto seleccionado.
     */
    public void ponerProductoSeleccionado() {
        try {
            final Producto producto = getTablaProductoSeleccionado();
            posicionProducto = listaProductos.indexOf(producto);
            if (producto != null) {
                idProducto = producto.getId();
                tfNombreProducto.setText(producto.getNombreProducto());

            }
        } catch (Exception ex) {
            System.out.println("getTablaProductoSeleccionado: " + ex.getMessage());
        }
    }

    //************************************************
    //  LINEA FACTURA
    //************************************************
    /**
     * Agrega una serie de datos de un producto a una linea de la factura.
     */
    @FXML
    public void pasarItemAListaFactura() {

        if (validateEmptyField("Debe seleccionar un producto", idProducto == 0)) {
            if (validateEmptyField("Debe ingresar la cantidad", tfCantidadLineaFactura.getText().isEmpty())) {
                if (validateEmptyField("Debe ingresar el descuento", tfDescuentoLineaFactura.getText().isEmpty())) {
                    if (validateFormatNumber("Cantidad", tfCantidadLineaFactura.getText())) {
                        if (validateFormatNumber("Descuento", tfDescuentoLineaFactura.getText())) {
                            int numLinea = lineaFacturaList.size();

                            double descuento = Double.parseDouble(tfDescuentoLineaFactura.getText());
                            int cantidad = Integer.parseInt(tfCantidadLineaFactura.getText());

                            if (idProducto != 0) {

                                numLinea++;

                                for (Producto obj : listaProductos) {

                                    //comparacion id ObservableList con id en textField
                                    if (obj.getId() == idProducto) {
                                        double total = 0;
                                        total = obj.getPrecio() * cantidad;

                                        //linea de factura para agregar al Array
                                        nuevaLinea(new LineaFactura(
                                                numLinea, //numero de linea
                                                Integer.parseInt(lbNumPedido.getText()), //num Factura
                                                obj.getId(), //id producto
                                                obj.getPrecio(), //precio
                                                cantidad, //cantidad
                                                descuento, //descuento
                                                obj.getNombreProducto(),//nombre producto
                                                total
                                        ));
                                    }
                                }
                            } else {
                                System.out.println("No hay linea de factura");
                            }
                        }
                    }
                }
                tablaLineaFactura.refresh(); //actualiza las lineas de la factura con cualquier cambio
                totales();
            }
        }
    }

    /**
     * Cálcula los valores Subtotal, Descuento, IVA, y Total que se muestran en
     * la factura.
     */
    private void totales() {

        lbSubtotal.setText(dosDecimales(sumaSubtotalSinDescuento()));
        lbDescuentoTotal.setText(dosDecimales(descuentoTotal()));
        lbSubtotalDescuento.setText(dosDecimales(sumaSubtotalSinDescuento() - descuentoTotal()));
        lbIVA.setText(dosDecimales(importeTotalImpuestos()));
        lbTotal.setText(dosDecimales(sumaSubtotalSinDescuento() - descuentoTotal() + importeTotalImpuestos()));
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
     * Guarda la factura en la base de datos.
     */
    @FXML
    private void guardarFactura() {
        lbNumPedido.setText(Integer.toString(nuevoNumeroFactura()));
        if (validateEmptyField("No hay articulos agregados a la factura", tablaLineaFactura.getItems().isEmpty())) {
            if (validateEmptyField("No ha seleccionado ningún cliente", idCliente.isEmpty())) {
                Conexion conexion = new Conexion();
                Connection con = conexion.conectar();
                ResultSet rs = null;
                PreparedStatement stmt = null;

                String numFactura = Integer.toString(nuevoNumeroFactura());

                try {
                    stmt = con.prepareStatement("INSERT INTO pedidos set numPedido="
                            + numFactura + ", Cliente=\"" + this.idCliente + "\", empleado="
                            + this.empleado + " ,Fecha=\"" + Fecha.hoyAnyoMesDia() + "\"");

                    stmt.executeUpdate();

                    for (LineaFactura obj : lineaFacturaList) {

                        int numLinea = obj.getNumLinea();
                        int producto = obj.getProducto();
                        int cantidad = obj.getCantidad();
                        double precio = obj.getPrecio();
                        double descuento = obj.getDescuento() / 100;
                        double iva = (sumaSubtotalSinDescuento() - (descuento * sumaSubtotalSinDescuento())) * IVA;
                        double subtotal = (precio * cantidad) - (descuento * sumaSubtotalSinDescuento()) + iva;

                        stmt = con.prepareStatement("INSERT INTO lineasPedido ( NumLinea, "
                                + "numPedido, producto, precio, cantidad, descuento,iva, subtotal) "
                                + " VALUES (" + numLinea + "," + numFactura + ","
                                + producto + "," + precio + "," + cantidad + "," + descuento + "," + iva + "," + subtotal + ")");
                        stmt.executeUpdate();
                    }
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Registros");
                    alert.setHeaderText(null);
                    alert.setContentText("La factura se ha guardado correctamente");
                    alert.showAndWait();

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        System.out.println("Finally, guardarFactura: " + ex.getMessage());
                    }
                }
                //cargar número de factura a pantalla

                lbNumPedido.setText(Integer.toString(nuevoNumeroFactura()));
                limpiarFactura();
            }
        }
    }

    /**
     * Limpia los campos o reinicia a ceros los campos o valores para crear una
     * nueva factura.
     */
    private void limpiarFactura() {
        tfNombreCliente.clear();
        tfNombreProducto.clear();
        //tfIdProducto.clear();
        lineaFacturaList.clear();
        lbIVA.setText("****");
        lbDescuentoTotal.setText("****");
        lbTotal.setText("****");
        lbSubtotal.setText("****");
        lbSubtotalDescuento.setText("****");

    }

    /**
     * Método que inserta un producto a la lista de las facturas.
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
            lineaFacturaList.get(j).setCantidad(lineaFacturaList.get(j).getCantidad() + lineaF.getCantidad());

            //redefinir descuento con el que se recibe por teclado
            lineaFacturaList.get(j).setDescuento(lineaF.getDescuento());

            /*            if (lineaFacturaList.get(j).getDescuento() > 0) {

                //calcular subtotal con descuento
                lineaFacturaList.get(j).setSubtotal(
                        lineaF.getPrecio()
                        * lineaFacturaList.get(j).getCantidad()
                        * (lineaFacturaList.get(j).getDescuento() / 100));
            } else {**/
            //calcular subtotal cuando el descuento es igual a cero
            lineaFacturaList.get(j).setSubtotal(
                    lineaF.getPrecio() * lineaFacturaList.get(j).getCantidad());
            //    }
        } else {
            lineaFacturaList.add(lineaF);
        }
    }

    /**
     * Busca un producto basándose en un número recibido.
     *
     * @param producto Número que identifica el producto a buscar.
     * @return Número que identifica la posición del objeto en un listado.
     */
    public int buscarProductoId(int producto) {
        int numIndex = -1;
        boolean found = false;

        Iterator<LineaFactura> iterator = lineaFacturaList.iterator();
        LineaFactura obj = null;

        while (iterator.hasNext() && found == false) {
            obj = iterator.next();

            //comparar los nombres del producto con el que han pasado por parametro
            if (obj.getProducto() == producto) {

                //asignar el valor de la linea donde se encontró el nombre a la variable
                numIndex = lineaFacturaList.indexOf(obj);

                //variable de control para salir del bucle
                found = true;
            }
        }
        return numIndex;
    }

    /**
     * Borra la linea seleccionada de la factura.
     */
    @FXML
    public void borrarLineaNumero() {
        if (validateEmptyField("Debe seleccionar un articulo para borrarlo", tfLineaABorrar.getText().isEmpty())) {
            if (validateFormatNumber("\"Número para borrar\"", tfLineaABorrar.getText())) {
                lineaFacturaList.remove(Integer.parseInt(tfLineaABorrar.getText()) - 1);
                setNumLinea();
                tablaLineaFactura.refresh();
                tfLineaABorrar.clear();
                totales();
            }
        }
    }

    /**
     * Genera un número nuevo para cada línea de la factura.
     */
    private void setNumLinea() {
        int numLinea = 0;
        for (LineaFactura obj : lineaFacturaList) {
            obj.setNumLinea(numLinea + 1);
            numLinea++;
        }
    }

    /**
     * Método que captura el objeto seleccionado de una lista.
     *
     * @return Un objeto de la Clase LineaFactura que ha sido seleccionado.
     */
    public LineaFactura getItemSeleccionado() { //de aqui va a los textfields

        LineaFactura itemSeleccionado = null;
        if (tablaLineaFactura != null) {
            List<LineaFactura> tabla = tablaLineaFactura.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                itemSeleccionado = tabla.get(0);
                return itemSeleccionado;
            }
        }
        return itemSeleccionado;
    }

    /**
     * Método que muestra en el formulario el objeto seleccionado.
     */
    public void ponerItemSeleccionado() {
        final LineaFactura item = getItemSeleccionado();
        posicionItem = lineaFacturaList.indexOf(item);
        if (item != null) {
            tfLineaABorrar.setText(Integer.toString(item.getNumLinea()));
        }
    }

    /**
     * Suma de los valores de los artículos de la factura antes de aplicar el
     * descuento.
     *
     * @return Valor de la suma de los subtotales sin descuentos.
     */
    public double sumaSubtotalSinDescuento() {
        double total = 0;

        for (LineaFactura obj : lineaFacturaList) {
            //sumar los precios de todos los productos
            total += obj.getSubtotal();
        }
        return total;
    }

    /**
     * Suma de los descuentos de todos los artículos de la factura.
     *
     * @return Descuento total de la factura.
     */
    public double descuentoTotal() {
        double desc = 0;

        Iterator<LineaFactura> iterator = lineaFacturaList.iterator();
        LineaFactura e = null;

        while (iterator.hasNext()) {
            e = iterator.next();

            //sumar los descuentos de todos los productos
            desc += e.getPrecio() * e.getCantidad() * (e.getDescuento() / 100);
        }
        return desc;
    }

    /**
     * Importe total de los impuestos de la factura.
     *
     * @return Valor total del IVA de la factura.
     */
    public double importeTotalImpuestos() {
        //calculo del importe total más el IVA
        return (sumaSubtotalSinDescuento() - descuentoTotal()) * IVA;
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
            Alert alert = new Alert(AlertType.WARNING);
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
    private boolean validateFormatNumber(String campo, String numero) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(numero);
        if (m.find() && m.group().equals(numero)) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Validación de números");
            alert.setHeaderText(null);
            alert.setContentText("El valor " + campo + " no es número");
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Abre el menú principal.
     */
    @FXML
    public void menuPrincipalFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMainMenu.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLMainMenuController controller = fxmlLoader.<FXMLMainMenuController>getController();
            controller.initVariable(tipoUsuario, empleado);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.getIcons().add(new Image("s_supermarket.png"));
            stage.setTitle("..::Menú Principal::..");

            //cierra la ventana abierta anteriormente
            Stage stage2 = (Stage) bt_menuPrincipal.getScene().getWindow();
            stage2.close();

        } catch (IOException ex) {
            ex.getMessage();
        }
    }

    //Sacar el último número de la factura de la base de datos
    /**
     * Genera un número nuevo para la factura consultando el último número
     * almacenado en la base de datos.
     *
     * @return Número de factura.
     */
    private int nuevoNumeroFactura() {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int numFactura = 0;
        try {
            stmt = con.prepareStatement("SELECT max(numpedido) FROM pedidos");
            stmt.executeQuery();
            rs = stmt.executeQuery();
            rs.first();
            numFactura = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("fillProductosList: " + ex.getMessage());
        } finally {
            try {
                stmt.close();
                rs.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, fillProductosList: " + ex.getMessage());
            }
        }
        return numFactura + 1;
    }
}
