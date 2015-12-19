package br.com.teajuda.teajuda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.teajuda.teajuda.Classes.Audio;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.ListaTarefaAdapter;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Classes.Tarefa;
import br.com.teajuda.teajuda.Conexao.RotinaDao;

public class ListaEditarTarefa_Activity extends ActionBarActivity {


    private List<Tarefa> tarefas;
    private ListView listaTarefa;
    private Rotina rotina;
    private FloatingActionButton criarTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_editar_tarefa_);

        listaTarefa = (ListView) findViewById(R.id.lista_rotina);
        criarTarefa = (FloatingActionButton) findViewById(R.id.btnCriarTarefa);

        Intent edicao = this.getIntent();
        rotina = (Rotina) edicao.getSerializableExtra("rotinaselecionada");
        registerForContextMenu(listaTarefa);
        carregaLista();

        listaTarefa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent edicao = new Intent(
                        ListaEditarTarefa_Activity.this,
                        EditarTarefa_Activity.class);

                Tarefa selecionado = (Tarefa) parent.getItemAtPosition(position);
                edicao.putExtra("tarefaselecionada", selecionado);
                startActivity(edicao);

            }
        });

        criarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edicao = new Intent(
                        ListaEditarTarefa_Activity.this,
                        EditarTarefa_Activity2.class);

                edicao.putExtra("rotinaSelecionada", rotina);
                startActivity(edicao);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Tarefa tarefaSelecionada = (Tarefa) listaTarefa.getAdapter().getItem(info.position);


        MenuItem deletar = menu.add("Deletar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(ListaEditarTarefa_Activity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar? ")
                        .setPositiveButton("Quero",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RotinaDao dao = new RotinaDao(ListaEditarTarefa_Activity.this);

                                        Imagem imagem;
                                        Audio audio;

                                        imagem = dao.getImage(tarefaSelecionada.getIdImagem());
                                        audio = dao.getAudio(tarefaSelecionada.getIdAudio());

                                        if (imagem.getId() != null) {
                                            dao.deletar_imagem(imagem);
                                        }
                                        if (audio.getId() != null) {
                                            dao.deletar_audio(audio);
                                        }


                                        dao.deletar_tarefa(tarefaSelecionada);
                                        carregaLista();
                                        dao.close();
                                    }
                                }).setNegativeButton("Não", null).show();
                return false;
            }
        });

    }


    public void carregaLista(){
        RotinaDao dao = new RotinaDao (this);
         tarefas = dao.getListaTarefa(rotina.getId());
        dao.close();

        ListaTarefaAdapter adapter = new ListaTarefaAdapter(this, tarefas);

        listaTarefa.setAdapter(adapter);
    }

}
