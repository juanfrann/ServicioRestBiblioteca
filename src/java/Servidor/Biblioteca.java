package Servidor;

import Excepciones.ErrorDeInserccion;
import Excepciones.ImposibleEliminar;
import Excepciones.UsuarioInexistente;
import Excepciones.UsuarioExistente;
import Excepciones.sinProductosParaTramitar;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Servidor.Libro;
import Servidor.Pedido;
import Servidor.Usuario;
import Servidor.Tramitacion;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author juanFrancisco
 */
@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Throwable.class)
public class Biblioteca {

    @PersistenceContext
    EntityManager em;

    //Datos de la biblioteca
    private String nombre;
    private String direccion;

    //Relaciones entre clases
    Map<String, Usuario> usuarios;  //Mapa con los usuarios del sistema
    List<Libro> listadoLibros;      //Listado con libros

    //Constructores de la clase Biblioteca
    public Biblioteca() {
        nombre = "";
        direccion = "";
        usuarios = new TreeMap<>();
        listadoLibros = new ArrayList<>();
    }

    public Biblioteca(String _nom, String _dir) {
        nombre = _nom;
        direccion = _dir;
        usuarios = new TreeMap<>();
        listadoLibros = new ArrayList<>();
    }
    
    //-----------------------------------------------------------------//
    //Metodo para realizar loggin
    //-----------------------------------------------------------------//
    public Usuario loggin(String _dni, String _contra) {
        
        Usuario comprobarUsuario = null;
        try {
            comprobarUsuario = em.createQuery("SELECT usu FROM Usuario usu WHERE usu.dni = ?1 and usu.contraseña = ?2", Usuario.class)
                    .setParameter(1, _dni)
                    .setParameter(2, _contra)
                    .getSingleResult();

        } catch (NoResultException nre) {
            throw new UsuarioInexistente();
        }

        em.close();
        return comprobarUsuario;
    }
    

    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
    public void nuevoLibro(Libro _nuev){
        em.persist(_nuev);
    }

    //-----------------------------------------------------------------//
    //Método para buscar un usuario
    //-----------------------------------------------------------------//
    public boolean buscarUsuario(String _dni) {

        Usuario usuAxu = em.find(Usuario.class, _dni);

        if (usuAxu == null) {
            return false;
        }
        return true;
    }

    //-----------------------------------------------------------------//
    //Método para insertar un nuevo usuario (solo administrador)
    //-----------------------------------------------------------------//
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
    public void nuevoUsuario(Usuario _usu) {

        try {
            em.persist(_usu);
        } catch (ErrorDeInserccion e) {

        }
    }

    //-----------------------------------------------------------------//
    //Método para eliminar un usuario (solo administrador)
    //-----------------------------------------------------------------//
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
    public void eliminarUsuario(String _dni) {
        
        Usuario aux = em.find(Usuario.class, _dni);
        
        if(aux == null){
            throw new ImposibleEliminar();
        }
        
        em.remove(aux);
        em.close();
    }

    //-----------------------------------------------------------------//
    //Método para cambiar contraseña de un usuario
    //-----------------------------------------------------------------//
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
    public void cambiarContraseña(String _dniUsu, String _contrNueva) {

        //Obtenemos el usuario que nos pasa el dni
        Usuario auxiliar = em.find(Usuario.class, _dniUsu);
        
        auxiliar.setclave(_contrNueva);
        em.merge(auxiliar);

        em.close();
    }

    //-----------------------------------------------------------------//
    //Método para ver el saldo disponible del usuario
    //-----------------------------------------------------------------//
    public int verSaldo(String _usuDni) {

        int saldo = em.find(Usuario.class, _usuDni).getSaldoDisponible();

        return saldo;
    }

    //-----------------------------------------------------------------//
    //Método para comprobar si existe un libro en nuestra base de datos
    //-----------------------------------------------------------------//
    public Libro comprobarLibro(String _isbn) {

        //Buscamos el libro y lo devolvemos        
        Libro aux = em.find(Libro.class, _isbn);

        return aux;
    }

    //-----------------------------------------------------------------//
    //Método para realizar un pedido (sin datos)
    //-----------------------------------------------------------------//
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
    public void realizarPedido(String _usu, Libro _lib) {

        //Buscamos el libro para si no está insertarlo también
        Libro libroAux = em.find(Libro.class, _lib.getISBN());

        if (libroAux == null) {
            em.persist(_lib);
        }

        Usuario usuEntrante = em.find(Usuario.class, _usu);

        //Si el precio es menor al saldo disponible
        if (usuEntrante.getSaldoDisponible() > _lib.getPrecio()) {
            //Creamos el pedido con el libro entrante
            Pedido nuevoPedido = new Pedido(_lib, usuEntrante.getDni());
            usuEntrante.anadirPedido(nuevoPedido);
            int saldoRestante = usuEntrante.getSaldoDisponible() - _lib.getPrecio();
            usuEntrante.setSaldoDisponible(saldoRestante);

            em.persist(usuEntrante);
        } else {
            //Creamos un pedido con el estado de "En Espera"
            String estado = "E";
            Pedido nuevoPedido = new Pedido(_lib, _usu, estado);
            usuEntrante.anadirPedido(nuevoPedido);
            em.persist(usuEntrante);
        }

        em.close();
    }

    //-----------------------------------------------------------------//
    //Método para ver los pedidos (segun el estado de entrada)
    //-----------------------------------------------------------------//
    public List<Pedido> verPedidos(String _dni, String _estado) {

        List<Pedido> pedidos = null;

        try {
            pedidos = em.createQuery("SELECT ped FROM Pedido ped WHERE ped.dniUsu = ?1 AND ped.estado = ?2", Pedido.class)
                    .setParameter(1, _dni)
                    .setParameter(2, _estado)
                    .getResultList();
        } catch (NoResultException nre) {
        }
        
        em.close();
        
        return pedidos;
    }

    //-----------------------------------------------------------------//
    //Método para cambiar el saldo a los usuarios
    //-----------------------------------------------------------------//
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
    public void cambiarSaldo(int _saldoNuevo){
        
        Query query = em.createQuery("UPDATE Usuario usu SET usu.saldoDisponible = ?1")
                .setParameter(1, _saldoNuevo);
        
        //Variable para devolver el número de cambios realizados
        int cambios = query.executeUpdate();
        
        
        em.close();
    }

    //-----------------------------------------------------------------//
    //Método para tramitar los pedidos
    //-----------------------------------------------------------------//
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
    public void tramitarPedido(int _nuevoSaldo) throws InterruptedException{
        
        //Obtenemos un listado con todos los pedidos pendientes
        List<Pedido> pedidosPendientes = em.createQuery("SELECT ped FROM Pedido ped WHERE ped.estado = 'P'").getResultList();
        
        if(pedidosPendientes.isEmpty()){
            throw new sinProductosParaTramitar();
        }
        
        String aux;
        Tramitacion nuevaTramitacion = new Tramitacion();
        //Pasamos a comprobar todos los pedidos y realizar tramitaciones
        for(Pedido ped : pedidosPendientes){
            //Obtenemos los datos de los pedidos para convertirlos en tramitaciones
            
            
            nuevaTramitacion.anadirTramitacion(ped);
            
            ped.setEstado("T");
            em.merge(ped);
        }
        em.persist(nuevaTramitacion);
        
        //Reiniciamos el saldo a todos los usuarios
        cambiarSaldo(_nuevoSaldo);
        
        //Pasamos los pedidos que se puedan de estado "E" a estado "P"
        List<Pedido> pedidosEspera = em.createQuery("SELECT ped FROM Pedido ped WHERE ped.estado = 'E'").getResultList();
        
        Usuario usuComprobacion = null;
        //Recorremos todos los pedidos
        for(Pedido pedEspera : pedidosEspera){
            
            usuComprobacion = em.find(Usuario.class, pedEspera.getDniUsu());
            
            if(usuComprobacion.getSaldoDisponible()>pedEspera.getLibro().getPrecio()){
                pedEspera.setEstado("P");
                usuComprobacion.setSaldoDisponible(usuComprobacion.getSaldoDisponible()-pedEspera.getLibro().getPrecio());
                em.merge(pedEspera);
                em.merge(usuComprobacion);
            }
        }
//        ¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿MAS EFICIENTE???????
//        //Primer paso es poner todos los pedidos P en el estado T
//        Query query = em.createQuery("UPDATE Pedido ped SET ped.estado = 'T' WHERE ped.estado = 'P'");
//        query.executeUpdate();
        em.close();
    }
    
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Throwable.class)
    public List<Tramitacion> buscarTramitacion(int _numTramitacion){
        
        List<Tramitacion> resultados = null;
        
        resultados = em.createQuery("SELECT tram FROM Tramitacion tram WHERE tram.idTramitacion = ?1")
                .setParameter(1, _numTramitacion)
                .getResultList();
        
        System.out.println(resultados.size());

        return resultados;
    }
    
    //-----------------------------------------------------------------//
    //Método para eliminar un usuario (solo administrador)
    //-----------------------------------------------------------------//
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
    public void eliminarLibro(String _isbn) {
        
        Libro aux = em.find(Libro.class, _isbn);
        
        if(aux == null){
            throw new ImposibleEliminar();
        }
        
        em.remove(aux);
        em.close();
    }
    
    /////////////////////----------------------------------/////////////////////
    /////////////////////----------------------------------/////////////////////
    /////////////////////----------------------------------/////////////////////
    
    public Libro devolverLibro(String _isbn){
        
        Libro nuevo = em.find(Libro.class, _isbn);
        return nuevo;
    }
    
    public Usuario devolverUsuario(String _dni){
        
        Usuario usuario = em.find(Usuario.class, _dni);
        return usuario;
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
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
