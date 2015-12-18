/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Servidor.Pedido;

import java.util.*;
import javax.persistence.*;

/**
 *
 * @author juanFrancisco
 */

@Entity
public class Tramitacion {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.TABLE)   
    private int idTramitacion;
    @Temporal(TemporalType.DATE)
    private java.util.Date fechaTramitacion;
    @OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Pedido> pedidoTramitado;
    
    public Tramitacion(){        
        fechaTramitacion = new Date();
        pedidoTramitado = new ArrayList<>();
    }
    
    
    //Método para añadir pedidos que hemos tramitado
    public void anadir(Pedido _ped){
        getPedidoTramitado().add(_ped);
    }

    /**
     * @return the idTramitacion
     */
    public int getIdTramitacion() {
        return idTramitacion;
    }

    /**
     * @param idTramitacion the idTramitacion to set
     */
    public void setIdTramitacion(int idTramitacion) {
        this.idTramitacion = idTramitacion;
    }

    /**
     * @return the fechaTramitacion
     */
    public Date getFechaTramitacion() {
        return fechaTramitacion;
    }

    /**
     * @param fechaTramitacion the fechaTramitacion to set
     */
    public void setFechaTramitacion(Date fechaTramitacion) {
        this.fechaTramitacion = fechaTramitacion;
    }

    /**
     * @return the pedidoTramitado
     */
    public List<Pedido> getPedidoTramitado() {
        return pedidoTramitado;
    }

    /**
     * @param pedidoTramitado the pedidoTramitado to set
     */
    public void setPedidoTramitado(List<Pedido> pedidoTramitado) {
        this.pedidoTramitado = pedidoTramitado;
    }
    
    public void anadirTramitacion(Pedido _ped){
        getPedidoTramitado().add(_ped);
    }    
}
