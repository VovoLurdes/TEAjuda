package br.com.teajuda.teajuda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.teajuda.teajuda.Classes.ListaRotinaAdapter;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Conexao.RotinaDao;

public class ListaEditarRotina_Activity extends ActionBarActivity{

    private List<Rotina> rotinas;
    private RotinaDao rotina;
    private ListView listaRotina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__editar__rotina);


        listaRotina = (ListView) findViewById(R.id.lista_rotina);
        registerForContextMenu(listaRotina);
        carregaLista();

        listaRotina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent edicao = new Intent(
                        ListaEditarRotina_Activity.this,
                        ListaEditarTarefa_Activity.class);

                Rotina selecionado = (Rotina) parent.getItemAtPosition(position);
                edicao.putExtra("rotinaselecionada", selecionado);
                startActivity(edicao);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Rotina rotinaSelecionado = (Rotina) listaRotina.getAdapter().getItem(info.position);


        MenuItem deletar = menu.add("Deletar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(ListaEditarRotina_Activity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar? ")
                        .setPositiveButton("Quero",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RotinaDao dao = new RotinaDao(ListaEditarRotina_Activity.this);
                                        dao.deletar_rotina(rotinaSelecionado);
                                        carregaLista();
                                        dao.close();
                                    }
                                }).setNegativeButton("Não",null).show();
                return false;
            }
        });

    }

    public void carregaLista(){
        RotinaDao dao = new RotinaDao (this);
        rotinas = dao.getListaRotina();
        dao.close();

        ListaRotinaAdapter adapter = new ListaRotinaAdapter(this, rotinas);

        listaRotina.setAdapter(adapter);
    }

}
