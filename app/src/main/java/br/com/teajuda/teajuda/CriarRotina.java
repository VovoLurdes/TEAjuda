package br.com.teajuda.teajuda;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CriarRotina extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_rotina);

        final TextView tituloRotina = (TextView) findViewById(R.id.tituloRotina);

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Criar Rotina");
        dialog.setContentView(R.layout.dialog_rotina);
        dialog.setCancelable(false);

        Button criarRotina = (Button) dialog.findViewById(R.id.btnCriar);
        Button cancelarRotina = (Button) dialog.findViewById(R.id.btnCancelar);
        final EditText nomeRotina = (EditText) dialog.findViewById(R.id.edtNomeRotina);

        criarRotina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tituloRotina.setText(nomeRotina.getText().toString());
                dialog.dismiss();
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


        Button salvarVoltar = (Button) findViewById(R.id.btnSalVol);
        Button salvarNovo = (Button) findViewById(R.id.btnSalNov);

        salvarVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(CriarRotina.this, "Salvo com Sucesso", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_criar_rotina, menu);
        return true;
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
