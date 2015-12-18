/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import Excepciones.*;
import Servidor.Biblioteca;
import Servidor.Libro;
import Servidor.Pedido;
import Servidor.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 *
 * @author juanFrancisco
 */
@Controller
@RequestMapping("/usuario")
public class RecursoUsuario {

    @Autowired
    Biblioteca biblio;

    @RequestMapping(value = "/{dni}", method = RequestMethod.GET, produces = "application/json; charset=utf8")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Usuario obtenerUsuario(@PathVariable String dni) {

        Usuario usuario = biblio.devolverUsuario(dni);

        if (usuario == null) {
            throw new UsuarioInexistente();
        }
        return usuario;
    }
    
    /**
     *
     * @param dni
     * @param libro
     * @return
     */
    @RequestMapping(value = "/pedido/{dni}", method = RequestMethod.POST, consumes = "application/json; charset=utf8", produces = "application/json; charset=utf8")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody void nuevoPedido(@PathVariable String dni, Libro libro){
        
        Usuario usuario = biblio.devolverUsuario(dni);
        
        //Comprobamos que el usuario existe
        if(usuario == null){
            throw new UsuarioInexistente();
        }else{
            biblio.realizarPedido(dni, libro);
        } 
    }
    
    //¿¿¿Poner varios atributos en la direccion???
    @RequestMapping(value = "/pedido/{dni}", method = RequestMethod.GET, produces = "application/json; charset=utf8")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody List<Pedido> obtenerPedidos(@PathVariable String dni){
        
        List<Pedido> listado;
        if(dni==null){
            throw new DatosInsuficientes();
        }else{
            listado = biblio.verPedidos(dni,"P"); 
        } 
        return listado;
    }
    
    @RequestMapping(value = "/saldo/{dni}", method = RequestMethod.GET, produces = "application/json; charset=utf8")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody int saldoDisponible(@PathVariable String dni){
        
        int saldo = biblio.verSaldo(dni);
        
        return saldo;
    }
    
    @RequestMapping(value = "/contra/{dni}", method = RequestMethod.PUT, produces = "application/json; charset=utf8")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody boolean cambiarContrase(@PathVariable String dni, @RequestBody String nuevaContra){
        
        try{
            biblio.cambiarContraseña(dni, nuevaContra);
        }catch (ErrorDeCambio e){
            return false;
        }
        return true;
    }   
}
