/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import Excepciones.LibroInexistente;
import Servidor.Biblioteca;
import Servidor.Libro;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author juanFrancisco
 */
@Controller
@RequestMapping("/libro")
public class RecursoLibro {

    @Autowired
    Biblioteca biblio;

    /**
     *
     * @param isbn
     * @return
     * @throws org.json.simple.parser.ParseException
     */
    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Libro obtenerLibro(@PathVariable String isbn) throws JSONException {

        //Buscamos el libro por si está en nuestra base de datos
        Libro libro = biblio.comprobarLibro(isbn);
        String _isbn = "";
        String _titulo = "";
        String _autor = "";
        
        if (libro == null) {
            String url = "http://isbndb.com/api/v2/json/6PZ8OE1A/book/" + isbn;
            String datosLibro = callURL(url);

            try {
                JSONObject json = new JSONObject(datosLibro);
                JSONArray array = json.getJSONArray("data");
                
                for(int i=0; i<array.length(); i++){
                    _isbn = (String) array.getJSONObject(i).get("isbn10");
                    _titulo = (String) array.getJSONObject(i).get("title_long");
                    _autor = (String) array.getJSONObject(i).getJSONArray("author_data").getJSONObject(i).get("name");
                }
                
            } catch (LibroInexistente e) {

            }
            
            Libro libroNuevo = new Libro(_titulo, _autor, _isbn, 50);
            biblio.nuevoLibro(libroNuevo);
            return libroNuevo;
        }
        return libro;
    }

    @RequestMapping(value = "/{isbn}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody void eliminarLibro(@PathVariable String isbn) {

        Libro libro = biblio.devolverLibro(isbn);

        if (libro == null) {
            throw new LibroInexistente();
        } else {
            biblio.eliminarLibro(isbn);
        }
    }

    //Método para hacer la llamada a la URL
    public static String callURL(String myURL) {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(myURL);
            urlConn = url.openConnection();
            if (urlConn != null) {
                urlConn.setReadTimeout(60 * 1000);
            }
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:" + myURL, e);
        }

        return sb.toString();
    }
}
