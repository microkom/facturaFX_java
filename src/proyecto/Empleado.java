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

    private String apellido;
    private String cargo;
    private LocalDate fNacimiento;
    private LocalDate fContrato;
    private String jefe;
    private String user;
    private String password;

    public Empleado(String apellido, String cargo, LocalDate fNacimiento, LocalDate fContrato, String jefe, String user, String password, String id, String nombre, String telefono) {
        super(id, nombre, telefono);
        this.apellido = apellido;
        this.cargo = cargo;
        this.fNacimiento = fNacimiento;
        this.fContrato = fContrato;
        this.jefe = jefe;
        this.user = user;
        this.password = password;
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

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
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
    
    

}
