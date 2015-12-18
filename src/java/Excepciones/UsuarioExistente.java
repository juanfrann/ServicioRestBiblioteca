/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author juanFrancisco
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsuarioExistente extends RuntimeException{
    
}
