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
public class Proveedor extends Persona{
    
    private Persona representante;

    public Proveedor(Persona personaContacto, String nif, String nombre, String apellido, String direccion, String cp, String telefono, String email) {
        super(nif, nombre, apellido, direccion, cp, telefono, email);
        this.representante = personaContacto;
    }

    public Persona getPersonaContacto() {
        return representante;
    }

    public void setPersonaContacto(Persona personaContacto) {
        this.representante = personaContacto;
    }

    
    
}
