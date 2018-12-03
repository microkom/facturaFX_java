/*
finished commenting
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
 * Clase Empleado, hereda de la clase Persona.
 *
 * @author German Navarro
 */
public class Empleado extends Persona {

    /**
     * Variable de clase privada: Apellido de la persona
     */
    private String apellido;
    /**
     * Variable de clase privada: Cargo de la persona
     */
    private String cargo;
    /**
     * Variable de clase privada: Fecha de nacimiento de la persona.
     */
    private String fnacimiento;
    /**
     * Variable de clase privada: Fecha de contrato de la persona.
     */
    private String fcontrato;
    /**
     * Variable de clase privada: Número de identificación de un empleado que se
     * identifica como jefe.
     */
    private int jefe;
    /**
     * Variable de clase privada: Nombre del jefe.
     */
    private String nomJefe;

    /**
     * Lista de los empleados.
     */
    private static ObservableList<Empleado> listaEmpleadoLocal = FXCollections.observableArrayList();

    public Empleado(String apellido, String cargo, String fNacimiento,
            String fContrato, int jefe, String id, String nombre, String telefono, String nomJefe) {
        super(id, nombre, telefono);
        this.apellido = apellido;
        this.cargo = cargo;
        this.fnacimiento = fNacimiento;
        this.fcontrato = fContrato;
        this.jefe = jefe;
        this.nomJefe = nomJefe;
    }

    public Empleado(String id, String nombre, String apellido, int jefe) {
        super(id, nombre);
        this.apellido = apellido;
        this.jefe = jefe;
    }

    public String getNomJefe() {
        return this.nomJefe;
    }

    public void setNomJefe(String nomJefe) {
        this.nomJefe = nomJefe;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFnacimiento() {
        return fnacimiento;
    }

    public void setFnacimiento(String fnacimiento) {
        this.fnacimiento = fnacimiento;
    }

    public String getFcontrato() {
        return fcontrato;
    }

    public void setFcontrato(String fcontrato) {
        this.fcontrato = fcontrato;
    }

    public int getJefe() {
        return jefe;
    }

    public void setJefe(int jefe) {
        this.jefe = jefe;
    }

    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Metodo que concatena los valores: 'id, nombre, apellido, telefono' para 
     * usarlos como datos de búsqueda
     * @return Texto que contiene valores de búsqueda.
     */
    public String getDatosBusqueda() {
        return id + " " + nombre + " " + apellido + " " + telefono;
    }

    /**
     * Método que rellena un ObservableList con los datos del empleado
     *
     * @param listaEmpleado Contiene el listado de los empleados almacenados en 
     * la base de datos.
     */
    public static void fillEmpleadoList(ObservableList<Empleado> listaEmpleado) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM empleados");
            rs = stmt.executeQuery();
            while (rs.next()) {

                listaEmpleadoLocal.add(
                        new Empleado(
                                rs.getString("idEmpleado"),
                                rs.getString("Nombre"),
                                rs.getString("Apellidos"),
                                rs.getInt("Jefe")));
            }
            rs.close();
            stmt.close();

            stmt = con.prepareStatement("SELECT * FROM empleados");
            rs = stmt.executeQuery();
            while (rs.next()) {
                listaEmpleado.add(
                        new Empleado(
                                rs.getString("Apellidos"),
                                rs.getString("Cargo"),
                                rs.getString("FNacimiento"),
                                rs.getString("FContrato"),
                                rs.getInt("Jefe"),
                                rs.getString("idEmpleado"),
                                rs.getString("Nombre"),
                                rs.getString("Telefono"),
                                JefeList(rs.getInt("Jefe"))
                        ));
            }

        } catch (SQLException ex) {
            System.out.println(" fillEmpleadoList :" + ex.getLocalizedMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(Empleado.class.getName() + " Finally ->fillEmpleadoList :" + ex.getMessage());
            }
        }
    }

    /**
     * Método que averigua el nombre del jefe usando como parámetro el id del
     * empleado.
     *
     * @param empleado Número entero que identifica al empleado.
     * @return Devuelve el nombre del jefe como {@code String}.
     */
    public static String JefeList(int empleado) {

        boolean found = false;
        String nombre = "";

        Iterator<Empleado> ite = listaEmpleadoLocal.iterator();
        Empleado obj;

        while (ite.hasNext() && found == false) {
            obj = ite.next();
            if (Integer.parseInt(obj.getId()) == empleado) {
                nombre = obj.getNombre();
                found = true;
            }
        }
        return nombre;
    }
}
