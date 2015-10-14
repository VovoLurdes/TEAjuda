package br.com.teajuda.teajuda.Conexao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.teajuda.teajuda.Classes.Audio;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Classes.Tarefa;

/**
 * Created by foo on 16/08/15.
 */
public class RotinaDao extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String TB_ROTINA = "Rotina";
    private static final String TB_TAREFA = "Tarefa";
    private static final String TB_IMAGEM = "Imagem";
    private static final String TB_AUDIO = "Audio";
    private static final String DATABASE = "TeAjuda.BD";


    private static final String TABELA_ROTINA =
            "CREATE TABLE " + TB_ROTINA + " (" +
                    "idRotina INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Descricao TEXT NOT NULL," +
                    "Ordem INTEGER NOT NULL" +
                    ");";

    private static final String TABELA_IMAGEM =
            "CREATE TABLE " + TB_IMAGEM + " (" +
                    "idImagem INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Caminho TEXT" +
                    ");";

    private static final String TABELA_AUDIO =
            "CREATE TABLE " + TB_AUDIO + " (" +
                    "idAudio INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Caminho TEXT" +
                    ");";

    private static final String TABELA_ATIVIDADE =
            "CREATE TABLE " + TB_TAREFA + " (" +
                    "idTarefa INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idImagem INTEGER," +
                    "idRotina INTEGER NOT NULL," +
                    "idAudio INTEGER," +
                    "Descricao TEXT NOT NULL," +
                    "Ordem INTEGER NOT NULL," +
                    "FOREIGN KEY(idAudio) REFERENCES Audio(idAudio)," +
                    "FOREIGN KEY(idImagem) REFERENCES Imagem(idImagem)," +
                    "FOREIGN KEY(idRotina) REFERENCES Rotina(idRotina)" +
                    ");";


    public RotinaDao (Context context){
        super(context, DATABASE,null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABELA_ROTINA);
        db.execSQL(TABELA_IMAGEM);
        db.execSQL(TABELA_AUDIO);
        db.execSQL(TABELA_ATIVIDADE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insere_tarefa (Tarefa tarefa){
        ContentValues values = new ContentValues();

        values.put("idImagem",tarefa.getIdImagem());
        values.put("idRotina",tarefa.getIdRotina());
        values.put("idAudio", tarefa.getIdAudio());
        values.put("Descricao", tarefa.getTitulo());
        values.put("Ordem", tarefa.getOrdem());

        getWritableDatabase().insert(TB_TAREFA, null, values);
    }

    public long insere_imagem (Imagem imagem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Caminho", imagem.getCaminho());

        long id = db.insert(TB_IMAGEM, null, values);

        return id;
    }

    public long insere_audio (Audio audio){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Caminho", audio.getCaminho());

        long id = db.insert(TB_IMAGEM, null, values);

        return id;
    }

    public long insere_rotina (Rotina rotina){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Descricao", rotina.getTitulo());
        values.put("Ordem", rotina.getOrdem());

        long id = db.insert(TB_ROTINA, null, values);

        return id;
    }

    public List<Rotina> getListaRotina(){
        List<Rotina> rotinas = new ArrayList<Rotina>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TB_ROTINA + ";", null);

        while (c.moveToNext()){
            Rotina rotina = new Rotina();

            rotina.setId(c.getLong(c.getColumnIndex("idRotina")));
            rotina.setTitulo(c.getString(c.getColumnIndex("Descricao")));
            rotina.setOrdem(c.getInt(c.getColumnIndex("Ordem")));
            rotinas.add(rotina);
        }

        c.close();
        return rotinas;
    }

    public List<Tarefa> getListaTarefa(Long id){
        List<Tarefa> tarefas = new ArrayList<Tarefa>();

       /* String sql = "SELECT * FROM " + TB_TAREFA + " t "
                + " JOIN " + TB_ROTINA + " r "
                + "ON t.idRotina = r.idRotina JOIN "
                + TB_IMAGEM + " i " + "ON t.idImagem = i.idImagem"
                + " JOIN " + TB_AUDIO + " a " + "ON t.idAudio = a.idAuido" +
                " WHERE r.IdRotina = ?";*/

        String sql = "SELECT * FROM " + TB_TAREFA + " t "
                + " JOIN " + TB_ROTINA + " r "
                + "ON t.idRotina = r.idRotina "
                + "WHERE r.IdRotina = " + id;

        Cursor c = getReadableDatabase().rawQuery(sql, null);

        while (c.moveToNext()){
            Tarefa tarefa = new Tarefa();

            tarefa.setIdImagem(c.getLong(c.getColumnIndex("idImagem")));
            tarefa.setIdRotina(c.getLong(c.getColumnIndex("idRotina")));
            tarefa.setIdAudio(c.getLong(c.getColumnIndex("idAudio")));
            tarefa.setTitulo(c.getString(c.getColumnIndex("Descricao")));
            tarefas.add(tarefa);
        }

        c.close();
        return tarefas;
    }

    public List<Imagem> getImage(Long id){
        List<Imagem> imagens = new ArrayList<Imagem>();

        String sql = "SELECT * FROM " + TB_IMAGEM + "WHERE idImagem = " + id;

        Cursor c = getReadableDatabase().rawQuery(sql, null);

        while (c.moveToNext()){
            Imagem imagem = new Imagem();

            imagem.setId(c.getLong(c.getColumnIndex("idImagem")));
            imagem.setCaminho(c.getString(c.getColumnIndex("Caminho")));
            imagens.add(imagem);
        }

        c.close();
        return imagens;
    }

    public List<Audio> getAudio(Long id){
        List<Audio> audios = new ArrayList<Audio>();

        String sql = "SELECT * FROM " + TB_AUDIO + "WHERE idAudio = " + id;

        Cursor c = getReadableDatabase().rawQuery(sql, null);

        while (c.moveToNext()){
            Audio audio = new Audio();

            audio.setId(c.getLong(c.getColumnIndex("idImagem")));
            audio.setCaminho(c.getString(c.getColumnIndex("Caminho")));
            audios.add(audio);
        }

        c.close();
        return audios;
    }

    public void deletar_rotina(Rotina rotina){
        String[] args = {rotina.getId().toString()};
        getWritableDatabase().delete(TB_ROTINA, "id=?", args);
    }

    public void deletar_imagem(Imagem imagem){
        String[] args = {imagem.getId().toString()};
        getWritableDatabase().delete(TB_IMAGEM,"id=?",args);
    }

    public void deletar_audio(Audio audio){
        String[] args = {audio.getId().toString()};
        getWritableDatabase().delete(TB_AUDIO,"id=?",args);
    }

    public void deletar_tarefa(Tarefa tarefa){
        String[] args = {tarefa.getId().toString()};
        getWritableDatabase().delete(TB_TAREFA,"id=?",args);
    }

  /*  public void altera (Aluno aluno){
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        String[] idParaSerAlterado = {aluno.getId().toString()};
        getWritableDatabase().update(TABELA, values, "id=?", idParaSerAlterado);

    }

    public void insereOuAtualiza(Aluno aluno){
        if (aluno.getId() == null){
            insere(aluno);
        } else {
            altera(aluno);
        }
    }*/

}
