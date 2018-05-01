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
import javafx.collections.ObservableList;

/**
 *
 * @author German
 */
public class Categoria {

    private String id;
    private String nombre;
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
}
