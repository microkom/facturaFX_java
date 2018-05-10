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
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import static proyecto.Empleado.listaEmpleado;

/**
 *
 * @author German
 */
public class Empleado extends Persona {

//    String dateString = "2016-10-27";
//    LocalDate localDate = LocalDate.parse(dateString);
    private String apellido;
    private String cargo;
    private String fNacimiento;
    private String fContrato;
    private int jefe;
    private String nomJefe;

    public static ObservableList<Empleado> listaEmpleadoLocal = FXCollections.observableArrayList();

    public Empleado(String apellido, String cargo, String fNacimiento,
            String fContrato, int jefe, String id, String nombre, String telefono, String nomJefe) {
        super(id, nombre, telefono);
        this.apellido = apellido;
        this.cargo = cargo;
        this.fNacimiento = fNacimiento;
        this.fContrato = fContrato;
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

    public String getfNacimiento() {
        return fNacimiento;
    }


    public void setfNacimiento(String fNacimiento) {
        this.fNacimiento = fNacimiento;
    }

    public String getfContrato() {
        return fContrato;
    }

    public void setfContrato(String fContrato) {
        this.fContrato = fContrato;
    }

    public int getJefe() {
        return jefe;
    }

    public void setJefe(int jefe) {
        this.jefe = jefe;
    }

    public String toString() {
        return nombre;
    }

    public String getDatosBusqueda() {
        return id + " " + nombre + " " + apellido + " " + telefono;
    }

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
                                /*
                                (String apellido, String cargo, fecha fNacimiento, 
            Fecha fContrato, String jefe, String id, String nombre, String telefono)
                                 */
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
