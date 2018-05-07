/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static proyecto.Producto.listaCategoria;

/**
 * FXML Controller class
 *
 * @author german
 */
public class FXMLFacturaController implements Initializable {

    private ObservableList<Factura> listaFacturas = FXCollections.observableArrayList();
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private ObservableList<LineaFactura> lineas = FXCollections.observableArrayList();
    private ObservableList<LineaFactura> mostrarLineas = FXCollections.observableArrayList();

    int posicionItem = 0;
    int posicionItemEnFactura = 0;
    int idProducto = 0;

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
    private Button btActualizar, btBorrar, btAgregar;

    private final ListChangeListener<LineaFactura> selectorItemLineaFactura = new ListChangeListener<LineaFactura>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends LineaFactura> p) {
            ponerItemSeleccionado();
        }
    };
    @FXML
    private TextField tfActCant,tfActDesc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
    
    public void initVariable(int numFactura){
         try {
            Producto.fillProductosList(listaProductos);
            Factura.fillFacturasList(listaFacturas);
            LineaFactura.fillLineaFacturasList(lineas);

            //int numFacturaTemporal = 10248;
            Iterator<Factura> ite = listaFacturas.iterator();
            Factura obj;

            boolean found = false;
            while (ite.hasNext() && found == false) {
                obj = ite.next();
                if (Integer.parseInt(obj.getNumPedido()) == numFactura) {
                    //mostrarLineas.add(obj);
                    lbNumFactura.setText(obj.getNumPedido());
                    lbFecha.setText(obj.getFecha());
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
                    tcDescuento.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("descuento"));
                }
            }
        } catch (Exception ex) {
            System.out.println("Columnas Factura " + ex.getMessage());
        }
        totales();
        tvFactura.setItems(mostrarLineas);
        listaComparacion();

        cbProductos.setItems(listaProductos);

        //listener para capturar la fila seleccionada de la linea de factura que se quiere borrar
        final ObservableList<LineaFactura> itemLineaFacturaSel
                = tvFactura.getSelectionModel().getSelectedItems();
        itemLineaFacturaSel.addListener(selectorItemLineaFactura);

        tfDescuento.setText("0");
        tfCantidad.setText("1");
    }

    public double sumaSubtotal() {
        double total = 0;

        for (LineaFactura obj : mostrarLineas) {
            //sumar los precios de todos los productos
            total += obj.getSubtotal();
        }

        return total;
    }

    public double descuentoTotal() {
        double desc = 0;

        Iterator<LineaFactura> iterator = mostrarLineas.iterator();
        LineaFactura e = null;

        while (iterator.hasNext()) {
            e = iterator.next();

            //sumar los descuentos de todos los productos
            desc += e.getPrecio() * e.getCantidad() * (e.getDescuento() / 100);
        }
        return desc;
    }

    //ImporteTotalImpuestos(): Devuelve el importe total con el iva(21%).
    public double importeTotalImpuestos() {
        //calculo del importe total más el IVA
        return (sumaSubtotal() - descuentoTotal()) * 0.21;
    }

    private void totales() {

        lbSubtotal.setText(dosDecimales(sumaSubtotal()));
        lbDescuentoTotal.setText(dosDecimales(descuentoTotal()));
        lbSubtotalDescuento.setText(dosDecimales(sumaSubtotal() - descuentoTotal()));
        lbIVA.setText(dosDecimales(importeTotalImpuestos()));
        lbTotal.setText(dosDecimales(sumaSubtotal() - descuentoTotal() + importeTotalImpuestos()));
    }

    //recibe un double y lo devuelve como String con 2 decimales
    //
    private String dosDecimales(double number) {
        return String.format("%.2f", number);
    }

    @FXML
    private void actualizarFX() {
  
         if (validateEmptyField("Debe ingresar la cantidad", tfActCant.getText().isEmpty())) {
            if (validateEmptyField("Debe ingresar el descuento", tfActDesc.getText().isEmpty())) {
                if (validateFormatNumber("Cantidad", tfActCant.getText())) {                   
                        int numLinea = setNumLinea();

                        double descuento = Double.parseDouble(tfActDesc.getText());
                        int cantidad = Integer.parseInt(tfActCant.getText());
                        int index = Integer.parseInt(tfLineaAActualizar.getText());
                        Producto obj = buscarProductoPorIndice(idProducto-1);

                        double total = 0;

                        if (descuento > 0) {
                            total = obj.getPrecio() * cantidad * ((100 - descuento) / 100);
                        } else {
                            total = obj.getPrecio() * cantidad;
                        }

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
            tvFactura.refresh(); //actualiza las lineas de la factura con cualquier cambio
            totales();
        }
        
    }

    //para borrar las lineas en la factura
    //Método que devuelve el objeto de la fila seleccionada
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

    //Método que a partir del objeto seleccionado lo muestra en el formulario
    //También puede habilitar/deshabilitar botones en el formualrio
    public void ponerItemSeleccionado() {
        final LineaFactura item = getItemSeleccionado();
        posicionItem = lineas.indexOf(item);
        if (item != null) {
            idProducto = item.getProducto(); 
            posicionItemEnFactura = item.getNumLinea()-1;
            tfLineaAActualizar.setText(Integer.toString(item.getNumLinea()));
            
        }
    }

    /**
     *
     * @return devuelve un array de números sacados de las lineas de las
     * facturas para luego compararlas con las modificaciones hechas y poder
     * almacenar el resto
     */
    public ArrayList<Integer> listaComparacion() {
        ArrayList<Integer> item = new ArrayList<Integer>();
        for (LineaFactura obj : mostrarLineas) {
            item.add(obj.getNumLinea());
        }
        return item;
    }

    @FXML
    public void borrarLineaDeFacturaFX() {
        if (validateEmptyField("Debe seleccionar un articulo para borrarlo", tfLineaAActualizar.getText().isEmpty())) {
            int al =  Integer.parseInt(tfLineaAActualizar.getText()) - 1;
            //mostrarLineas.remove(Integer.parseInt(tfLineaAActualizar.getText()) - 1);
            mostrarLineas.remove(posicionItemEnFactura);
            setNumLinea();
            tvFactura.refresh();
            tfLineaAActualizar.clear();
            totales();
        }
    }

    @FXML
    public void pasarItemAListaFacturaFX() {

        if (validateEmptyField("Debe ingresar la cantidad", tfCantidad.getText().isEmpty())) {
            if (validateEmptyField("Debe ingresar el descuento", tfDescuento.getText().isEmpty())) {
                if (validateFormatNumber("Cantidad", tfCantidad.getText())) {
                    if (validateFormatNumber("Descuento", tfDescuento.getText())) {
                        int numLinea = setNumLinea();

                        double descuento = Double.parseDouble(tfDescuento.getText());
                        int cantidad = Integer.parseInt(tfCantidad.getText());
                        int index = cbProductos.getSelectionModel().getSelectedIndex();

                        Producto obj = buscarProductoPorIndice(index);

                        double total = 0;

                        if (descuento > 0) {
                            total = obj.getPrecio() * cantidad * ((100 - descuento) / 100);
                        } else {
                            total = obj.getPrecio() * cantidad;
                        }

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

    public Producto buscarProductoPorIndice(int index) {
        Producto pro = listaProductos.get(index);
        return pro;
    }

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

            if (mostrarLineas.get(j).getDescuento() > 0) {

                //calcular subtotal con descuento
                mostrarLineas.get(j).setSubtotal(
                        lineaF.getPrecio()
                        * mostrarLineas.get(j).getCantidad()
                        * (100 - mostrarLineas.get(j).getDescuento()) / 100);
            } else {
                //calcular subtotal cuando el descuento es igual a cero
                mostrarLineas.get(j).setSubtotal(
                        lineaF.getPrecio() * mostrarLineas.get(j).getCantidad());
            }
        } else {
            mostrarLineas.add(lineaF);

        }

    }

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
     * genera un número de para las lineas de la factura. Comprueba primero si
     * el número ya se está usando
     */
    private int setNumLinea() {
        int numLinea = 1;
        for (LineaFactura obj : mostrarLineas) {
            obj.setNumLinea(numLinea);
            numLinea++;
        }
        return numLinea;
    }

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
    //Validar si el texto es un número

    private boolean validateFormatNumber(String campo, String numero) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(numero);
        if (m.find() && m.group().equals(numero)) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validación de números");
            alert.setHeaderText(null);
            alert.setContentText("El valor " + campo + " no es número");
            alert.showAndWait();
            return false;
        }
    }
}
//            tfPrecio.setText(Double.toString(item.getPrecio()));
//            tfCantidad.setText(Integer.toString(item.getCantidad()));
//            tfDescuento.setText(Double.toString(item.getDescuento()));
