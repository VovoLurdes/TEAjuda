package br.com.teajuda.teajuda;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MenuInicial_Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_inicial_);

        Button btnMinhasRotinas = (Button) findViewById(R.id.btnMinhasRotinas);
        Button btnEditarRotina = (Button) findViewById(R.id.btnEditarRotina);
        Button btnCriarRotinas  = (Button) findViewById(R.id.btnCriarRotinas);

        btnMinhasRotinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent minhasrotinas = new Intent(MenuInicial_Activity.this, MinhasRotinas_Activity.class);
                startActivity(minhasrotinas);
            }
        });

        btnEditarRotina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editarrotina = new Intent(MenuInicial_Activity.this, takepicture.class);
                startActivity(editarrotina);
            }
        });

        btnCriarRotinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent criarrotinas = new Intent(MenuInicial_Activity.this, CriarRotina.class);
                startActivity(criarrotinas);
            }
        });

        try {
        File yourFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "score.txt");
        if(!yourFile.exists()) {
            yourFile.createNewFile();
        }
        FileOutputStream oFile = new FileOutputStream(yourFile, false);
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        writeToFile();

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

    public void writeToFile() {
        File folderImage = new File(Environment.getExternalStorageDirectory().toString()+"/TEAjuda/Imagens");
        File folderAudio = new File(Environment.getExternalStorageDirectory().toString()+"/TEAjuda/Audios");
        folderImage.mkdirs();
        folderAudio.mkdirs();

    }
}
