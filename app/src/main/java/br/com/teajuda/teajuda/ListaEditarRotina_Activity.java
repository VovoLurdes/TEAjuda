package br.com.teajuda.teajuda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.teajuda.teajuda.Classes.Audio;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.ListaRotinaAdapter;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Classes.Tarefa;
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
        MenuItem alterar = menu.add("Alterar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(ListaEditarRotina_Activity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar? ")
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RotinaDao dao = new RotinaDao(ListaEditarRotina_Activity.this);

                                        List<Tarefa> tarefa = new ArrayList<Tarefa>();
                                        Imagem imagem = new Imagem();
                                        Audio audio = new Audio();

                                        tarefa = dao.getListaTarefa(rotinaSelecionado.getId());

                                        for (int i = 0; i < tarefa.size(); i++) {
                                            imagem = dao.getImage(tarefa.get(i).getIdImagem());
                                            audio = dao.getAudio(tarefa.get(i).getIdAudio());

                                            if (imagem.getId() != null) {
                                                dao.deletar_imagem(imagem);
                                            }
                                            if (audio.getId() != null) {
                                                dao.deletar_audio(audio);
                                            }
                                            dao.deletar_tarefa(tarefa.get(i));
                                        }

                                        dao.deletar_rotina(rotinaSelecionado);
                                        carregaLista();
                                        dao.close();
                                    }
                                }).setNegativeButton("Não", null).show();
                return false;
            }
        });

        alterar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final Dialog dialog = new Dialog(ListaEditarRotina_Activity.this);
                dialog.setTitle("Alterar Nome Rotina");
                dialog.setContentView(R.layout.dialog_rotina);
                dialog.setCancelable(false);

                Button criarRotina = (Button) dialog.findViewById(R.id.btnCriar);
                Button cancelarRotina = (Button) dialog.findViewById(R.id.btnCancelar);
                final EditText nomeRotina = (EditText) dialog.findViewById(R.id.edtNomeRotina);

                criarRotina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nomeRotina.getText().toString().equals("")) {
                            Toast.makeText(ListaEditarRotina_Activity.this, "NOME DA ROTINA OBRIGATORIO",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            RotinaDao dao = new RotinaDao(ListaEditarRotina_Activity.this);
                            rotinaSelecionado.setTitulo(nomeRotina.getText().toString());
                            dao.alterarRotina(rotinaSelecionado);
                            dialog.dismiss();
                        }
                    }
                });

                cancelarRotina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });

                dialog.show();


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
