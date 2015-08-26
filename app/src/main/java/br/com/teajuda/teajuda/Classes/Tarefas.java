package br.com.teajuda.teajuda.Classes;

/**
 * Created by foo on 26/07/15.
 */
public class Tarefas {

    private Long id;
    private Long idImagem;
    private Long idRotina;
    private Long idAudio;
    private String titulo;
    private String ordem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdImagem() {
        return idImagem;
    }

    public void setIdImagem(Long idImagem) {
        this.idImagem = idImagem;
    }

    public Long getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(Long idRotina) {
        this.idRotina = idRotina;
    }

    public Long getIdAudio() {
        return idAudio;
    }

    public void setIdAudio(Long idAudio) {
        this.idAudio = idAudio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getOrdem() {
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }
}
