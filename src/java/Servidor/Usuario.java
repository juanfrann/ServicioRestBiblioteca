package Servidor;

import java.util.*;
import Servidor.Pedido;
import javax.persistence.*;

/**
 *
 * @author juanFrancisco
 */
@Entity
public class Usuario {

    //Atributos de la clase Usuario
    @Id
    private String dni;
    private String nombre;
    private String apellido;
    private String tipoUsuario;
    private String clave;
    private int saldoDisponible;
    @OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE}, targetEntity = Pedido.class)
    @JoinColumn(name="IdUsuario")
    private List<Pedido> listadoPedido;
    
    public Usuario() {
        dni = "";
        nombre = "";
        apellido = "";
        tipoUsuario = "";
        clave = "0000";
        saldoDisponible = 200;
        listadoPedido = new ArrayList<>();
    }

    public Usuario(String _dni, String _nombre, String _apellido, String _tipo, String _contrase, int _saldoDisponbile) {
        dni = _dni;
        nombre = _nombre;
        apellido = _apellido;
        tipoUsuario = _tipo;
        clave = _contrase;
        saldoDisponible = _saldoDisponbile;
        listadoPedido = new ArrayList<>();
    }

    /**
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the tipoUsuario
     */
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * @param tipoUsuario the tipoUsuario to set
     */
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * @return the contraseña
     */
    public String getclave() {
        return clave;
    }

    /**
     * @param contraseña the contraseña to set
     */
    public void setclave(String contraseña) {
        this.clave = contraseña;
    }

    /**
     * @return the saldoDisponible
     */
    public int getSaldoDisponible() {
        return saldoDisponible;
    }

    /**
     * @param saldoDisponible the saldoDisponible to set
     */
    public void setSaldoDisponible(int saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    /**
     * @return the listadoPedido
     */
    public List<Pedido> getListadoPedido() {
        return listadoPedido;
    }

    /**
     * @param listadoPedido the listadoPedido to set
     */
    public void setListadoPedido(List<Pedido> listadoPedido) {
        this.listadoPedido = listadoPedido;
    }  
    
    public void anadirPedido(Pedido _ped){
        listadoPedido.add(_ped);
    }
    
    public int cambiarEstado(){
        
        int contador = 0;
        int saldo = 0;
        
        if(!listadoPedido.isEmpty()){
            Pedido aux;
            Iterator<Pedido> it = listadoPedido.iterator();           
            
            while(it.hasNext()){
                aux = it.next();
                if("P".equals(aux.getEstado())){
                    aux.setEstado("T");
                    saldoDisponible = saldoDisponible +aux.getLibro().getPrecio();
                    contador++;
                }
            }
            
            //Volvemos a colocar el iterador al principio de la lista
            it = listadoPedido.iterator();
            while(it.hasNext()){
                aux = it.next();
                if("E".equals(aux.getEstado())){
                    saldo = aux.getLibro().getPrecio();
                    if(saldoDisponible > saldo){
                        //Si el usuario le queda saldo pasamos su estao a pendiente
                        aux.setEstado("P");
                        //Decrementamos su saldo
                        saldoDisponible = saldoDisponible - saldo;
                    }
                }
            }
        }
        return contador;
    }
}

