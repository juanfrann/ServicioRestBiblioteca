package Servidor;

import javax.persistence.*;

/**
 *
 * @author juanFrancisco
 */

@Entity
public class Libro {

    @Id
    private String ISBN;
    private String titulo;
    private String autor;
    private int precio;

    public Libro() {
        titulo = "";
        autor = "";
        ISBN = "";
        precio = 0;
    }
    
    public Libro(String _titulo, String _autor, String _isbn, int _precio){
        titulo = _titulo;
        autor = _autor;
        ISBN = _isbn;
        precio = _precio;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * @return the ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * @param ISBN the ISBN to set
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * @return the precio
     */
    public int getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(int precio) {
        this.precio = precio;
    }
}

