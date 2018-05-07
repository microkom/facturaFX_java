/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author German
 */
public class Producto {

    private int id;
    private String nombreProducto;
    private int proveedor;
    private String nomProveedor;
    private String nomCategoria;
    private int categoria;
    private int precio;
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
 
    public String toString(){
        return nombreProducto;
    }


//    public String getProveedor() {
//        String prov = "";
//        Proveedor proveedor = null;
//        prov = proveedor.getNombre();
//        return prov;
//    }
//    public String getCategoria() {
//        String cat = "";
//        Categoria categoria = null;
//        cat = categoria.getNombre();
//        return cat;
//    }
    public String getDatosBusqueda() {
        return id + " " + nombreProducto + " " + precio;
    }
    
    static ObservableList<Producto> nombreProdList = FXCollections.observableArrayList();

    public static String getNombreProducto(int idRecibido){
        
        String nombre = "";
        boolean found = false;
        Iterator<Producto> pro = nombreProdList.iterator();
        Producto obj = null;
        while (pro.hasNext() && found == false) {
           obj = pro.next();
            if (obj.id == idRecibido){
               nombre = obj.nombreProducto;
               found = true;
           }
        }
        return nombre;
    }

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
  /*  
    public static String getNombreProducto(int producto) {

        boolean found = false;
        String nombre = "";
        Iterator<Proveedor> ite = listaProductos.iterator();
        Proveedor obj;

        while (ite.hasNext() && found == false) {
            obj = ite.next();
            if (Integer.parseInt(obj.getId()) == producto) {
                nombre = obj.getNombre();
                found = true;
            }
        }
        return nombre;
    }
*/
//    public String getNombreProveedor(int proveedor) {
//         
//       boolean found = false;
//        String nombre = "";
//        Iterator<Proveedor> ite = listaProveedor.iterator();
//        Proveedor obj;
//
//        while (ite.hasNext() && found == false) {
//            obj = ite.next();
//            if (Integer.parseInt(obj.getId()) == proveedor) {
//                nombre = obj.getNombre();
//                found = true;
//            }
//        }
//        return nombre;
//    }
//
//    public static String getNombreCategoria(int categoria) {
//
//        boolean found = false;
//        String nombre = "";
//        Iterator<Categoria> ite = listaCategoria.iterator();
//        Categoria obj;
//
//        while (ite.hasNext() && found == false) {
//            obj = ite.next();
//            if (Integer.parseInt(obj.getId()) == categoria) {
//                nombre = obj.getNombre();
//                found = true;
//            }
//        }
//        return nombre;
//    }
//    public static String getNombreProveedor(int proveedor) {
//
//        Conexion conexion = new Conexion();
//        Connection con = conexion.conectar();
//        ResultSet rs;
//        PreparedStatement stmt;
//        String nombre = "";
//        try {
//            stmt = con.prepareStatement("SELECT nombre FROM proveedores where idproveedor=?");
//            stmt.setInt(1, proveedor);
//            rs = stmt.executeQuery();
//            rs.first();
//            nombre = rs.getString("nombre");
//            stmt.close();
//            rs.close();
//        } catch (SQLException ex) {
//            System.out.println("getNombreProveedor: " + ex.getMessage());
//        } finally {
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return nombre;
//    }

//    public static String getNombreCategoria(int categoria) {
//        Conexion conexion = new Conexion();
//        Connection con = conexion.conectar();
//        ResultSet rs = null;
//        PreparedStatement stmt = null;
//        String nombre = "";
//        try {
//            stmt = con.prepareStatement("SELECT nomCategoria FROM categorias where idcategoria=?");
//            stmt.setInt(1, categoria);
//            rs = stmt.executeQuery();
//            rs.first();
//            nombre = rs.getString("nomCategoria");
//
//        } catch (SQLException ex) {
//            System.out.println("getNombreCategoria: " + ex.getMessage());
//        } finally {
//            try {
//                stmt.close();
//                rs.close();
//                con.close();
//            } catch (SQLException ex) {
//                System.out.println("FINALLY: getNombreCategoria:  " + ex.getMessage());
//            }
//        }
//        return nombre;
//    }
}
