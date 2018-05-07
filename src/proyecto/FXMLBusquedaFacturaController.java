/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author German
 */
public class FXMLBusquedaFacturaController implements Initializable {

    private ObservableList<Factura> listaFacturas = FXCollections.observableArrayList();
    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();
    private FilteredList<Factura> listaFiltrada = new FilteredList(listaFacturas, obj -> true);
    private FilteredList<Factura> listaFiltradaF = new FilteredList(listaFacturas, obj -> true);
    ObservableList<Factura> lista = FXCollections.observableArrayList();
    FilteredList<Factura> listaF = new FilteredList(lista, obj -> true);

    int posicionItem = 0;
    int numFactura = 0;
    @FXML
    private TableView<Factura> tablaBusquedaF, tablaBusqueda;
    @FXML
    private TableColumn<Factura, String> tcNum, tcCliente, tcFecha, tcNumF, tcClienteF, tcFechaF, tcTotal, tcTotalF;
    @FXML
    private TextField tfBusqueda;
    @FXML
    private DatePicker dpFechaFinal, dpFechaInicial;
    @FXML
    private Button btFiltrarFecha, btEditarFactura, btEditarFactura2;
    @FXML
    private Label lbTitulo;
  

    private final ListChangeListener<Factura> selectorItemFactura = new ListChangeListener<Factura>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Factura> p) {
            ponerItemSeleccionado();
        }
    };
    private final ListChangeListener<Factura> selectorItemFacturaF = new ListChangeListener<Factura>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Factura> p) {
            ponerItemSeleccionadoF();
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Factura.fillFacturasList(listaFacturas);

        tablaBusqueda.setItems(listaFiltrada);

        //busqueda en tiempo real por nombre, contacto, cargo contacto, ciudad. Tiene en cuenta las tildes 
        tfBusqueda.setOnKeyReleased(keyEvent -> {
            listaFiltrada.setPredicate(obj
                    -> obj.getDatosBusqueda().toLowerCase().contains(tfBusqueda.getText().toLowerCase().trim()));
        });

        tcNum.setCellValueFactory(new PropertyValueFactory<Factura, String>("numPedido"));
        tcCliente.setCellValueFactory(new PropertyValueFactory<Factura, String>("nombreCliente"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<Factura, String>("fecha"));
        tcTotal.setCellValueFactory(new PropertyValueFactory<Factura, String>("subtotal"));

        tablaBusquedaF.setItems(listaFiltradaF);

        tcNumF.setCellValueFactory(new PropertyValueFactory<Factura, String>("numPedido"));
        tcClienteF.setCellValueFactory(new PropertyValueFactory<Factura, String>("nombreCliente"));
        tcFechaF.setCellValueFactory(new PropertyValueFactory<Factura, String>("fecha"));
        tcTotal.setCellValueFactory(new PropertyValueFactory<Factura, String>("subtotal"));
        dpFechaInicial.setEditable(false);
        dpFechaFinal.setEditable(false);

        //listener para capturar la fila seleccionada de la linea de factura que se quiere borrar
        final ObservableList<Factura> itemFacturaSel
                = tablaBusqueda.getSelectionModel().getSelectedItems();
        itemFacturaSel.addListener(selectorItemFactura);

        //listener para capturar la fila seleccionada de la linea de factura que se quiere borrar
        final ObservableList<Factura> itemFacturaSelF
                = tablaBusquedaF.getSelectionModel().getSelectedItems();
        itemFacturaSelF.addListener(selectorItemFacturaF);

        tablaBusquedaF.setDisable(true);
    }

    @FXML
    public void filtroFechaFX() {

        listaF.clear();
        Fecha fechaMayor = Fecha.stringToFecha(dpFechaFinal.getValue().toString());
        Fecha fechaMenor = Fecha.stringToFecha(dpFechaInicial.getValue().toString());

        for (Factura obj : listaFacturas) {
            Fecha fechaObj = Fecha.stringToFecha(obj.getFecha());

            if (fechaObj.mayorIgualQue(fechaMenor) && fechaObj.menorIgualQue(fechaMayor)) {
                lista.add(obj);
            }
        }
        
        tablaBusquedaF.setItems(listaF);

        tcNumF.setCellValueFactory(new PropertyValueFactory<Factura, String>("numPedido"));
        tcClienteF.setCellValueFactory(new PropertyValueFactory<Factura, String>("nombreCliente"));
        tcFechaF.setCellValueFactory(new PropertyValueFactory<Factura, String>("fecha"));
        tcTotal.setCellValueFactory(new PropertyValueFactory<Factura, String>("subtotal"));

        tablaBusquedaF.setDisable(false);
        
    }

    @FXML
    public void editarFacturaFX() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLFactura.fxml"));
            
            Parent root1 = (Parent) fxmlLoader.load();
            FXMLFacturaController controller = fxmlLoader.<FXMLFacturaController>getController();
            controller.initVariable(numFactura);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setTitle("..:: Editar Factura " + numFactura + " ::..");

            //cierra la ventana abierta anteriormente
//            Stage stage2 = (Stage) bt_categorias.getScene().getWindow();
//            stage2.close();
        } catch (IOException ex) {
            System.out.println("categoriasFX: " + ex.getMessage());
        }
        System.out.println(numFactura);

    }

    //para borrar las lineas en la factura
    //Método que devuelve el objeto de la fila seleccionada
    public Factura getItemSeleccionado() { //de aqui va a los textfields

        Factura itemSeleccionado = null;
        if (tablaBusqueda != null) {
            List<Factura> tabla = tablaBusqueda.getSelectionModel().getSelectedItems();
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
        final Factura item = getItemSeleccionado();
        posicionItem = listaFacturas.indexOf(item);
        if (item != null) {
            numFactura = Integer.parseInt(item.getNumPedido()); //usado para agregar el producto a la lista como nuevo
            //tfLineaAActualizar.setText(Integer.toString(item.getNumLinea()));

        }
    }

    //para borrar las lineas en la factura
    //Método que devuelve el objeto de la fila seleccionada
    public Factura getItemSeleccionadoF() { //de aqui va a los textfields

        Factura itemSeleccionado = null;
        if (tablaBusquedaF != null) {
            List<Factura> tabla = tablaBusquedaF.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                itemSeleccionado = tabla.get(0);
                return itemSeleccionado;
            }
        }
        return itemSeleccionado;
    }

    //Método que a partir del objeto seleccionado lo muestra en el formulario
    //También puede habilitar/deshabilitar botones en el formualrio
    public void ponerItemSeleccionadoF() {
        final Factura item = getItemSeleccionadoF();
        posicionItem = listaF.indexOf(item);
        if (item != null) {
            numFactura = Integer.parseInt(item.getNumPedido());

        }
    }
}
