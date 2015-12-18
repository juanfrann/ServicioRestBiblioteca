/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import Excepciones.ImposibleEliminar;
import Excepciones.UsuarioExistente;
import Excepciones.UsuarioInexistente;
import Servidor.Biblioteca;
import Servidor.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author juanFrancisco
 */
@Controller
@RequestMapping("/admin")
public class RecursoAdmin {
    
    @Autowired
    Biblioteca biblio;
    
    @RequestMapping(method = RequestMethod.POST, value = "/nuevo/{dni}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void nuevoUsuario(@PathVariable String dni, @RequestBody Usuario _nuevo) {

        Boolean encontrado = biblio.buscarUsuario(dni);

        if (encontrado) {
            throw new UsuarioExistente();
        } else {
            biblio.nuevoUsuario(_nuevo);
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/tramitacion", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void tramitacionPedidos() throws InterruptedException {
        int nuevoSaldo = 200;
        biblio.tramitarPedido(nuevoSaldo); 
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/baja/{dni}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void bajaUsuario(@PathVariable String dni){
        Usuario usuario = biblio.devolverUsuario(dni);
        
        if(usuario == null){
            throw new UsuarioInexistente();
        }else{
            try{
                biblio.eliminarUsuario(dni);
            }catch(ImposibleEliminar e){
                
            }
        }
    }
}
