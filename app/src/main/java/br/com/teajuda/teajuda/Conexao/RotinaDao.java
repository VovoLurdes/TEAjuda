package br.com.teajuda.teajuda.Conexao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.teajuda.teajuda.Classes.Audio;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Classes.Tarefas;

/**
 * Created by foo on 16/08/15.
 */
public class RotinaDao extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String TABELA = "ROTINA";
    private static final String DATABASE = "TeAjuda.BD";


    private static final String TABELA_ROTINA =
            "CREATE TABLE Rotina (" +
                    "idRotina INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Descricao TEXT NULL," +
                    "Ordem INTEGER NULL," +
                    ");";

    private static final String TABELA_IMAGEM =
            "CREATE TABLE Imagem (" +
                    "idImagem INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Caminho VARCHAR NOT NULL," +
                    "TituloImagem Varchar NOT NULL," +
                    ");";

    private static final String TABELA_AUDIO =
            "CREATE TABLE Audio (" +
                    "idAudio INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Caminho VARCHAR NOT NULL," +
                    "TituloAudio NOT NULL," +
                    ");";

    private static final String TABELA_ATIVIDADE =
            "CREATE TABLE Tarefa (" +
                    "idTarefa INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idImagem INTEGER NOT NULL," +
                    "idRotina INTEGER NOT NULL," +
                    "idAudio INTEGER NOT NULL," +
                    "Descricao VARCHAR NOT NULL," +
                    "Ordem VARCHAR NOT NULL," +
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

 /*   public void insere (Aluno aluno){
        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        getWritableDatabase().insert(TABELA, null, values);
    }
*/
    public List<Rotina> getLista(){
        List<Rotina> rotina = new ArrayList<Rotina>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + ";", null);

        while (c.moveToNext()){
            Rotina rotinas = new Rotina();

            rotinas.setId(c.getLong(c.getColumnIndex("idRotina")));
            rotinas.setTitulo(c.getString(c.getColumnIndex("Descricao")));
            rotinas.setOrdem(c.getInt(c.getColumnIndex("Ordem")));
            rotina.add(rotinas);
        }

        c.close();
        return rotina;
    }

    public void deletar_rotina(Rotina rotina){
        String[] args = {rotina.getId().toString()};
        getWritableDatabase().delete(TABELA, "id=?", args);
    }

    public void deletar_imagem(Imagem imagem){
        String[] args = {imagem.getId().toString()};
        getWritableDatabase().delete(TABELA,"id=?",args);
    }

    public void deletar_audio(Audio audio){
        String[] args = {audio.getId().toString()};
        getWritableDatabase().delete(TABELA,"id=?",args);
    }

    public void deletar_tarefa(Tarefas tarefa){
        String[] args = {tarefa.getId().toString()};
        getWritableDatabase().delete(TABELA,"id=?",args);
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
