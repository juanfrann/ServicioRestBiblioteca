package Servidor;

import java.util.*;
import Servidor.Libro;
import javax.persistence.*;

/**
 *
 * @author juanFrancisco
 */
@Entity
public class Pedido {
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private int codPed;
    private String estado;    //Pendiente(P), Tramitado(T), en Espera(E)
    private String dniUsu;
    @OneToOne
    @JoinColumn(name="isbnLib")
    private Libro libro;
    @Temporal(TemporalType.DATE)
    private java.util.Date fecha;

    
    public Pedido() {

    }

    public Pedido(Libro _libro, String _dni) {
        //Por defecto el libro está pendiente de tramitación
        estado = "P";
        libro = _libro;
        dniUsu = _dni;
        fecha = new Date();
    }
    
    public Pedido(Libro _libro, String _dni, String _estado){
        estado = _estado;
        libro = _libro;
        dniUsu = _dni;
        fecha = new Date();
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the libro
     */
    public Libro getLibro() {
        return libro;
    }

    /**
     * @param libro the libro to set
     */
    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the dniUsu
     */
    public String getDniUsu() {
        return dniUsu;
    }

    /**
     * @param dniUsu the dniUsu to set
     */
    public void setDniUsu(String dniUsu) {
        this.dniUsu = dniUsu;
    }

}
