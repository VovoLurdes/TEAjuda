package br.com.teajuda.teajuda.Classes;

import java.io.Serializable;

/**
 * Created by foo on 26/07/15.
 */
public class Rotina implements Serializable {

    private Long id;
    private String titulo;
    private int ordem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
}
