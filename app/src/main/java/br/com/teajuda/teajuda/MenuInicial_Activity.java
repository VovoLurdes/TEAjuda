package br.com.teajuda.teajuda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MenuInicial_Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial_);

        Button btnMinhasRotinas = (Button) findViewById(R.id.btnMinhasRotinas);
        Button btnCriarRotinas  = (Button) findViewById(R.id.btnCriarRotinas);

        btnMinhasRotinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent minhasrotinas = new Intent(MenuInicial_Activity.this, MinhasRotinas.class);
                startActivity(minhasrotinas);
            }
        });

        btnCriarRotinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent criarrotinas = new Intent(MenuInicial_Activity.this, CriarRotina.class);
                startActivity(criarrotinas);
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_inicial_, menu);
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
