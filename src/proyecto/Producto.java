/*
Finished commenting
 */
package proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase Producto.
 *
 * author German German Navarro.
 */
public class Producto {

    /**
     * Variable de clase privada: número de identificación del producto.
     */
    private int id;

    /**
     * Variable de clase privada: nombre del producto.
     */
    private String nombreProducto;

    /**
     * Variable de clase privada: número que identifica al proveedor.
     */
    private int proveedor;

    /**
     * Variable de clase privada: nombre del proveedor.
     */
    private String nomProveedor;

    /**
     * Variable de clase privada: nombre de la categoría.
     */
    private String nomCategoria;

    /**
     * Variable de clase privada: número que identifica la categoría.
     */
    private int categoria;

    /**
     * Variable de clase privada: precio del producto.
     */
    private int precio;

    /**
     * Variable de clase privada: cantidad del producto en inventario.
     */
    private int existencias;

    public static ObservableList<Proveedor> listaProveedor = FXCollections.observableArrayList();
    public static ObservableList<Categoria> listaCategoria = FXCollections.observableArrayList();

    public Producto(int id, String nombreProducto, int proveedor, int categoria, int precio, int existencias) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.proveedor = proveedor;
        setNomProveedor();
        setNomCategoria();
        this.categoria = categoria;
        this.precio = precio;
        this.existencias = existencias;
    }

    public String getNomProveedor() {
        return Proveedor.getNombreProveedor(proveedor);

    }

    public void setNomProveedor() {
        this.nomProveedor = Proveedor.getNombreProveedor(this.proveedor);
    }

    public String getNomCategoria() {
        return Categoria.getNombreCategoria(categoria);
    }

    public void setNomCategoria() {
        this.nomCategoria = Categoria.getNombreCategoria(this.categoria);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getProveedor() {
        return proveedor;
    }

    public void setProveedor(int proveedor) {
        this.proveedor = proveedor;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public static ObservableList<Proveedor> getListaProveedor() {
        return listaProveedor;
    }

    public static void setListaProveedor(ObservableList<Proveedor> listaProveedor) {
        Producto.listaProveedor = listaProveedor;
    }

    public static ObservableList<Categoria> getListaCategoria() {
        return listaCategoria;
    }

    public static void setListaCategoria(ObservableList<Categoria> listaCategoria) {
        Producto.listaCategoria = listaCategoria;
    }

    @Override
    public String toString() {
        return nombreProducto;
    }

     /**
     * Metodo que concatena los valores: 'id, nombre del producto, precio' para 
     * usarlos como datos de búsqueda
     * @return Texto que contiene valores de búsqueda.
     */
    public String getDatosBusqueda() {
        return id + " " + nombreProducto + " " + precio;
    }

    static ObservableList<Producto> nombreProdList = FXCollections.observableArrayList();

    /**
     * Método que averigua el nombre del producto usando como parámetro el id del
     * producto.
     * 
     * @param idRecibido Número entero que identifica al producto.
     * @return Devuelve el nombre del producto como {@code String}
     */
    public static String getNombreProducto(int idRecibido) {

        String nombre = "";
        boolean found = false;
        Iterator<Producto> pro = nombreProdList.iterator();
        Producto obj = null;
        while (pro.hasNext() && found == false) {
            obj = pro.next();
            if (obj.id == idRecibido) {
                nombre = obj.nombreProducto;
                found = true;
            }
        }
        return nombre;
    }

    /**
     * Método que rellena un ObservableList con los datos de los productos.
     * 
     * @param listaProductos Contiene el listado de los productos almacenados en
     * la base de datos.
     */
    public static void fillProductosList(ObservableList<Producto> listaProductos) {

        //Necesarios para mostrar los valores en el tableview en la tabla Productos
        Proveedor.fillProveedorList(listaProveedor);
        Categoria.fillCategoriaList(listaCategoria);

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs;
        PreparedStatement stmt;

        try {
            stmt = con.prepareStatement("SELECT * FROM productos");
            rs = stmt.executeQuery();
            while (rs.next()) {
                listaProductos.add(
                        new Producto( //int id, String nombreProducto, int proveedor, int categoria, double precio, int existencias) 
                                rs.getInt("IdProducto"),
                                rs.getString("NomProducto"),
                                rs.getInt("Proveedor"),
                                rs.getInt("Categoria"),
                                rs.getInt("Precio"),
                                rs.getInt("Existencias")));

            }
            nombreProdList = listaProductos;
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("fillProductosList: " + ex.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println("Finally, fillProductosList: " + ex.getMessage());
            }
        }
    }
}
