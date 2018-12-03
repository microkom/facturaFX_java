/*
finished commenting.
*/
package proyecto;

/**
 * Clase Persona
 *
 * @author German Navarro
 */
public class Persona {

    /**
     * Variable de clase privada: número de identificación.
     */
    protected String id;

    /**
     * Variable de clase privada: nombre de la persona.
     */
    protected String nombre;
    
    /**
     * Variable de clase privada: nombre del contacto.
     */
    protected String contacto;
    
    /**
     * Variable de clase privada: cargo del contacto.
     */
    protected String cargoContacto;
    
    /**
     * Variable de clase privada: dirección.
     */
    protected String direccion;
    
    /**
     * Variable de clase privada: ciudad.
     */
    protected String ciudad;
    
    /**
     * Variable de clase privada: país.
     */
    protected String pais;
    
    /**
     * Variable de clase privada: número de telefono.
     */
    protected String telefono;

    public Persona(String id, String nombre, String contacto, String cargoContacto, String direccion, String ciudad, String pais, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
        this.cargoContacto = cargoContacto;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.telefono = telefono;
    }

    /**
     * Constructor con tres parametros.
     *
     * @param id "id" de la persona
     * @param nombre nombre de la persona
     * @param telefono número de teléfono de la persona.
     */
    public Persona(String id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    /**
     * Constructor con dos parametros.
     *
     * @param id "id" de la persona
     * @param nombre nombre de la persona
     */
    public Persona(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCargoContacto() {
        return cargoContacto;
    }

    public void setCargoContacto(String cargoContacto) {
        this.cargoContacto = cargoContacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
