package br.com.teajuda.teajuda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import br.com.teajuda.teajuda.Classes.Audio;
import br.com.teajuda.teajuda.Classes.AudioRecord;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.Tarefa;
import br.com.teajuda.teajuda.Conexao.RotinaDao;

public class EditarTarefa_Activity extends ActionBarActivity{

    private Tarefa tarefa;
    AudioRecord voice;
    ImageView viewImage;
    FloatingActionButton chooseImage;
    String path;
    String pathAudio;
    EditText tituloTarefa;
    TextView tituloRotina;
    Imagem imagem;
    Audio audio;

    long idImagem = -1;
    long idAudio = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarefa_);

        final FloatingActionButton gravar = (FloatingActionButton) findViewById(R.id.button_play);
        final FloatingActionButton stop = (FloatingActionButton) findViewById(R.id.button_stop);

        viewImage = (ImageView) findViewById(R.id.imagemTarefa);
        chooseImage = (FloatingActionButton) findViewById(R.id.button_slv_img);
        tituloTarefa = (EditText) findViewById(R.id.edtTituloTarefa);

        Intent edicao = this.getIntent();
        tarefa = (Tarefa) edicao.getSerializableExtra("tarefaselecionada");


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathAudio = voice.stopRecording();
                Toast.makeText(EditarTarefa_Activity.this, "Audio Gravado", Toast.LENGTH_SHORT).show();
                stop.setVisibility(View.INVISIBLE);
                gravar.setVisibility(View.VISIBLE);
            }
        });

        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice = new AudioRecord();
                voice.startRecording();
                Toast.makeText(EditarTarefa_Activity.this, "Gravando", Toast.LENGTH_SHORT).show();
                stop.setVisibility(View.VISIBLE);
                gravar.setVisibility(View.INVISIBLE);
            }
        });

        RotinaDao dbRotina = new RotinaDao(this);

        tarefa = dbRotina.getTarefa(tarefa.getId());

        imagem = dbRotina.getImage(tarefa.getIdImagem());
        audio = dbRotina.getAudio(tarefa.getIdAudio());

        if (imagem.getCaminho() != null) {
            Bitmap imagemFoto = BitmapFactory.decodeFile(imagem.getCaminho());
            Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto, imagemFoto.getWidth(), 300, true);

            viewImage.setImageBitmap(imagemFotoReduzida);
            viewImage.setTag(imagem.getCaminho());
            viewImage.setScaleType(ImageView.ScaleType.FIT_XY);

        }

        tituloTarefa.setText(tarefa.getTitulo());
        dbRotina.close();
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("Maroto", "Form onResume");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Maroto","Form onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Maroto","Form onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Maroto","Form onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Maroto","Form onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Maroto","Form onRestart");
    }

    private void selectImage() {

        final CharSequence[] options = { "Tirar Foto", "Foto da Galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarTarefa_Activity.this);
        builder.setTitle("Adicionar Foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tirar Foto")) {
                    path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/TEAjuda/Imagens/" + System.currentTimeMillis() + ".jpg";
                    Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri localFoto = Uri.fromFile(new File(path));
                    irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);
                    startActivityForResult(irParaCamera, 1);
                } else if (options[item].equals("Foto da Galeria")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }

            }

        });

        builder.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Bitmap imagemFoto = BitmapFactory.decodeFile(path);
                Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto, imagemFoto.getWidth(), 300, true);

                viewImage.setImageBitmap(imagemFotoReduzida);
                viewImage.setTag(path);
                viewImage.setScaleType(ImageView.ScaleType.FIT_XY);

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                path = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(path));
                viewImage.setImageBitmap(thumbnail);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_formulario_ok:

                RotinaDao dbRotina = new RotinaDao(this);

                if (path != null) {
                    imagem.setCaminho(path);
                    if (tarefa.getIdImagem() != 0) {
                            dbRotina.alteraImagem(imagem);
                        }else {
                          idImagem = dbRotina.insere_imagem(imagem);
                          tarefa.setIdImagem(idImagem);
                    }

                    path = null;
                }

                if (pathAudio != null) {
                    audio.setCaminho(pathAudio);
                    if (tarefa.getIdAudio() != 0) {
                        dbRotina.alteraAudio(audio);
                    }else{
                        idAudio = dbRotina.insere_audio(audio);
                        tarefa.setIdAudio(idAudio);
                    }
                    pathAudio = null;
                }

                tarefa.setTitulo(tituloTarefa.getText().toString());

                dbRotina.alteraTarefa(tarefa);

                dbRotina.close();

                finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
