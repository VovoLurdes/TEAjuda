package br.com.teajuda.teajuda.Classes;

/**
 * Created by foo on 26/07/15.
 */
public class Rotina {

    private Long id;
    private String Titulo;
    private long Ordem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public long getOrdem() {
        return Ordem;
    }

    public void setOrdem(long ordem) {
        Ordem = ordem;
    }
}
