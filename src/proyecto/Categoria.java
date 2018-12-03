/*
Finished commenting
*/
package proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javafx.collections.ObservableList;
import static proyecto.Producto.listaCategoria;

/**
 * Clase Categoria.
 * @author German Navarro
 */
public class Categoria {

    /**
     * Variable de clase: identificador de la categoría
     */
    private String id;
    
    /**
     * Variable de clase: nombre de la categoría.
     */
    
    private String nombre;
    
    /**
     * Variable de clase: descripción de la categoría.
     */
    private String descripcion;

    public Categoria(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Método usado para agrupar los datos que se usan en una búsqueda de datos
     * de esta clase.
     * @return La concatenación del {@code id} el {@code nombre} y la {@code descripción}. 
     */
    public String getDatosBusqueda() {
        return id + " " + nombre + " " + descripcion;
    }

    public String toString() {
        return nombre;
    }

    /**
     * Método para rellenar un ObservableList con las categorías.
     * @param listaCagegoria ObservableList que ha de rellenarse usando datos de
     * la base de datos
     */
    public static void fillCategoriaList(ObservableList<Categoria> listaCagegoria) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM categorias");
            rs = stmt.executeQuery();
            while (rs.next()) {
                listaCagegoria.add(
                        new Categoria(
                                //int id, String nombre, String descripcion
                                rs.getString("idCategoria"),
                                rs.getString("NomCategoria"),
                                rs.getString("Descripcion")
                        ));
            }
        } catch (SQLException ex) {
            System.out.println(Categoria.class.getName() + " fillCategoriaList :" + ex.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(Categoria.class.getName() + " Finally->fillCategoriaList :" + ex.getMessage());
            }
        }
    }

    /**
     * Método que busca el nombre de una categoría basandose en un número entero 
     * recibido por parámetro.
     * @param categoria Número entero que indica la categoría.
     * @return Devuelve el nombre de la categoría como un {@code String}.
     */
    public static String getNombreCategoria(int categoria) {

        /**
         * Variable boolean: usada para controlar
         */
        boolean found = false;
        String nombre = "";
        Iterator<Categoria> ite = listaCategoria.iterator();
        Categoria obj;

        while (ite.hasNext() && found == false) {
            obj = ite.next();
            if (Integer.parseInt(obj.getId()) == categoria) {
                nombre = obj.getNombre();
                found = true;
            }
        }
        return nombre;
    }
}
