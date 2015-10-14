package br.com.teajuda.teajuda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.teajuda.teajuda.Classes.ListaRotinaAdapter;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Conexao.RotinaDao;

public class MinhasRotinas_Activity extends AppCompatActivity {

    private List<Rotina> rotinas;
    private RotinaDao rotina;
    private ListView listaRotina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_rotinas);

        listaRotina = (ListView) findViewById(R.id.lista_alunos);

        carregaLista();

        listaRotina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent edicao = new Intent(
                        MinhasRotinas_Activity.this,
                        Atividades_activity.class);

                Rotina selecionado = (Rotina) parent.getItemAtPosition(position);
                edicao.putExtra("rotinaselecionada", selecionado);
                startActivity(edicao);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
