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
                    "DescricaoRotina TEXT NOT NULL," +
                    "OrdemRotina INTEGER NOT NULL" +
                    ");";

    private static final String TABELA_IMAGEM =
            "CREATE TABLE " + TB_IMAGEM + " (" +
                    "idImagem INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "CaminhoImagem TEXT" +
                    ");";

    private static final String TABELA_AUDIO =
            "CREATE TABLE " + TB_AUDIO + " (" +
                    "idAudio INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "CaminhoAudio TEXT" +
                    ");";

    private static final String TABELA_ATIVIDADE =
            "CREATE TABLE " + TB_TAREFA + " (" +
                    "idTarefa INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idImagem INTEGER," +
                    "idRotina INTEGER NOT NULL," +
                    "idAudio INTEGER," +
                    "DescricaoAtividade TEXT NOT NULL," +
                    "OrdemTarefa INTEGER NOT NULL," +
                    "FOREIGN KEY(idAudio) REFERENCES Audio(idAudio)," +
                    "FOREIGN KEY(idImagem) REFERENCES Imagem(idImagem)," +
                    "FOREIGN KEY(idRotina) REFERENCES Rotina(idRotina)" +
                    ");";


    public RotinaDao (Context context){
        super(context, DATABASE, null, VERSAO);
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
        values.put("DescricaoAtividade", tarefa.getTitulo());
        values.put("OrdemTarefa", tarefa.getOrdem());

        getWritableDatabase().insert(TB_TAREFA, null, values);
    }

    public long insere_imagem (Imagem imagem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("CaminhoImagem", imagem.getCaminho());

        long id = db.insert(TB_IMAGEM, null, values);

        return id;
    }

    public long insere_audio (Audio audio){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("CaminhoAudio", audio.getCaminho());

        long id = db.insert(TB_AUDIO, null, values);

        return id;
    }

    public long insere_rotina (Rotina rotina){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("DescricaoRotina", rotina.getTitulo());
        values.put("OrdemRotina", rotina.getOrdem());

        long id = db.insert(TB_ROTINA, null, values);

        return id;
    }

    public List<Rotina> getListaRotina(){
        List<Rotina> rotinas = new ArrayList<Rotina>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TB_ROTINA + ";", null);

        while (c.moveToNext()){
            Rotina rotina = new Rotina();

            rotina.setId(c.getLong(c.getColumnIndex("idRotina")));
            rotina.setTitulo(c.getString(c.getColumnIndex("DescricaoRotina")));
            rotina.setOrdem(c.getInt(c.getColumnIndex("OrdemRotina")));
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

            tarefa.setId(c.getLong(c.getColumnIndex("idTarefa")));
            tarefa.setIdImagem(c.getLong(c.getColumnIndex("idImagem")));
            tarefa.setIdRotina(c.getLong(c.getColumnIndex("idRotina")));
            tarefa.setIdAudio(c.getLong(c.getColumnIndex("idAudio")));
            tarefa.setTitulo(c.getString(c.getColumnIndex("DescricaoAtividade")));
            tarefas.add(tarefa);
        }

        c.close();
        return tarefas;
    }


    public Imagem getImage(Long id){
        Imagem imagem = new Imagem();

        String sql = "SELECT * FROM " + TB_IMAGEM + " WHERE idImagem = " + id + ";";

        Cursor c = getReadableDatabase().rawQuery(sql, null);

        while (c.moveToNext()) {
            imagem.setId(c.getLong(c.getColumnIndex("idImagem")));
            imagem.setCaminho(c.getString(c.getColumnIndex("CaminhoImagem")));

        }

        c.close();
        return imagem;
    }


    public Audio getAudio(Long id){
        Audio audio = new Audio();

        String sql = "SELECT * FROM " + TB_AUDIO + " WHERE idAudio = " + id;

        Cursor c = getReadableDatabase().rawQuery(sql, null);

        while (c.moveToNext()) {
            audio.setId(c.getLong(c.getColumnIndex("idAudio")));
            audio.setCaminho(c.getString(c.getColumnIndex("CaminhoAudio")));
        }

        c.close();
        return audio;
    }

    public Tarefa getTarefa(Long id){
        Tarefa tarefa = new Tarefa();

        String sql = "SELECT * FROM " + TB_TAREFA + " WHERE idTarefa = " + id + ";";

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        while (c.moveToNext()) {
            tarefa.setId(c.getLong(c.getColumnIndex("idTarefa")));
            tarefa.setIdImagem(c.getLong(c.getColumnIndex("idImagem")));
            tarefa.setIdRotina(c.getLong(c.getColumnIndex("idRotina")));
            tarefa.setIdAudio(c.getLong(c.getColumnIndex("idAudio")));
            tarefa.setTitulo(c.getString(c.getColumnIndex("DescricaoAtividade")));
        }

        c.close();

        return tarefa;
    }

    public void deletar_rotina(Rotina rotina){
        String[] args = {rotina.getId().toString()};
        getWritableDatabase().delete(TB_ROTINA, "idRotina=?", args);
    }

    public void deletar_imagem(Imagem imagem){
        String[] args = {imagem.getId().toString()};
        getWritableDatabase().delete(TB_IMAGEM,"idImagem=?",args);
    }

    public void deletar_audio(Audio audio){
        String[] args = {audio.getId().toString()};
        getWritableDatabase().delete(TB_AUDIO,"idAudio=?",args);
    }

    public void deletar_tarefa(Tarefa tarefa){
        String[] args = {tarefa.getId().toString()};
        getWritableDatabase().delete(TB_TAREFA,"idTarefa=?",args);
    }


    
    public void alteraTarefa (Tarefa tarefa){
        ContentValues values = new ContentValues();

        values.put("idImagem", tarefa.getIdImagem());
        values.put("idAudio", tarefa.getIdAudio());
        values.put("DescricaoAtividade", tarefa.getTitulo());

        String[] idParaSerAlterado = {tarefa.getId().toString()};

        getWritableDatabase().update(TB_TAREFA, values, "idTarefa=?", idParaSerAlterado);

    }

    public void alteraImagem (Imagem imagem){
        ContentValues values = new ContentValues();
        values.put("CaminhoImagem", imagem.getCaminho());

        String[] idParaSerAlterado = {imagem.getId().toString()};

        getWritableDatabase().update(TB_IMAGEM, values, "idImagem=?", idParaSerAlterado);

    }

    public void alteraAudio (Audio audio){
        ContentValues values = new ContentValues();
        values.put("CaminhoAudio", audio.getCaminho());

        String[] idParaSerAlterado = {audio.getId().toString()};

        getWritableDatabase().update(TB_AUDIO, values, "idAudio=?", idParaSerAlterado);

    }

}
