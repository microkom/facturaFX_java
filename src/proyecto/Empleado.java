/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.time.LocalDate;

/**
 *
 * @author German
 */
public class Empleado extends Persona {

    private String id;
    private String cargo;
    private LocalDate fNacimiento;
    private LocalDate fContrato;
    private String jefe;
    private String user;
    private String password;

//    public Empleado(String id, String cargo, LocalDate fNacimiento, LocalDate fContrato, String nombre, String apellido, String telefono) {
//        super(nombre, apellido, telefono);
//        this.id = id;
//        this.cargo = cargo;
//        this.fNacimiento = fNacimiento;
//        this.fContrato = fContrato;
//        this.user = null;
//        this.password = null;
//    }

    public Empleado(String id, String cargo, LocalDate fNacimiento, LocalDate fContrato, String user, String password, String nif, String nombre, String apellido, String direccion, String cp, String telefono, String email) {
        super(nif, nombre, apellido, direccion, cp, telefono, email);
        this.id = id;
        this.cargo = cargo;
        this.fNacimiento = fNacimiento;
        this.fContrato = fContrato;
        this.user = user;
        this.password = password;
    }

    public Empleado( String nombre) {
        super(nombre);
        
    }

    

   
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getfNacimiento() {
        return fNacimiento;
    }

    public void setfNacimiento(LocalDate fNacimiento) {
        this.fNacimiento = fNacimiento;
    }

    public LocalDate getfContrato() {
        return fContrato;
    }

    public void setfContrato(LocalDate fContrato) {
        this.fContrato = fContrato;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

}
