package br.com.teajuda.teajuda.Classes;

import java.io.Serializable;

/**
 * Created by foo on 26/07/15.
 */
public class Imagem implements Serializable {

    private Long id;
    private String caminho;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

}
