package br.com.teajuda.teajuda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.teajuda.teajuda.Classes.ListaTarefaAdapter;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Classes.Tarefa;
import br.com.teajuda.teajuda.Conexao.RotinaDao;

public class ListaEditarTarefa_Activity extends ActionBarActivity {


    private List<Tarefa> tarefas;
    private ListView listaRotina;
    private Rotina rotina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_editar_tarefa_);

        listaRotina = (ListView) findViewById(R.id.lista_rotina);

        Intent edicao = this.getIntent();
        rotina = (Rotina) edicao.getSerializableExtra("rotinaselecionada");

        carregaLista();

        listaRotina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    }

    public void carregaLista(){
        RotinaDao dao = new RotinaDao (this);
         tarefas = dao.getListaTarefa(rotina.getId());
        dao.close();

        ListaTarefaAdapter adapter = new ListaTarefaAdapter(this, tarefas);

        listaRotina.setAdapter(adapter);
    }

}
