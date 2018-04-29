/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

/**
 *
 * @author German
 */
public class Persona {

    private String nif;
    private String nombre;
    private String apellido;
    private String direccion;
    private String cp;
    private String telefono;
    private String email;

    public Persona(String nif, String nombre, String apellido, String direccion, String cp, String telefono, String email) {
        this.nif = nif;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.cp = cp;
        this.telefono = telefono;
        this.email = email;
    }
    public Persona(String nombre, String apellido, String telefono) {
     
        this.nombre = nombre;
        this.apellido = apellido;
  
        this.telefono = telefono;
  
    }
       public Persona(String nombre) {
           this.nombre = nombre;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
   
}
