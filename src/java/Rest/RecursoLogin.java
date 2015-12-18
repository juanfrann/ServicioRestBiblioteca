/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import Excepciones.DatosInsuficientes;
import Excepciones.UsuarioInexistente;
import Servidor.Biblioteca;
import Servidor.Usuario;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author juanFrancisco
 */
@Controller
@RequestMapping("/login")
public class RecursoLogin {
    
    @Autowired
    Biblioteca biblio;
    
    /**
     *
     * @param cadUsuario
     * @param login
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CONTINUE)
    //public @ResponseBody Usuario RecursoLogin(@QueryParam("usuario") String usuario, @QueryParam("login") String login){
    public @ResponseBody Usuario RecursoLogin(String cadUsuario, String login){
    
        //Comprobamos los parametros de entrada
        if(cadUsuario == null){
            throw new DatosInsuficientes();
        }else{
            Usuario usu = biblio.loggin(cadUsuario, login);
            if(usu == null){
                throw new UsuarioInexistente();
            }
            return usu;
        }      
    }
}