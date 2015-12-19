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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;

import br.com.teajuda.teajuda.Classes.Audio;
import br.com.teajuda.teajuda.Classes.AudioRecord;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Classes.Tarefa;
import br.com.teajuda.teajuda.Conexao.RotinaDao;

public class EditarTarefa_Activity2 extends ActionBarActivity{

    private Rotina rotina;
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
        setContentView(R.layout.activity_editar_tarefa_2);


        viewImage = (ImageView) findViewById(R.id.imagemTarefa);
        chooseImage = (FloatingActionButton) findViewById(R.id.button_slv_img);
        tituloTarefa = (EditText) findViewById(R.id.edtTituloTarefa);

        Intent edicao = this.getIntent();
        rotina = (Rotina) edicao.getSerializableExtra("rotinaSelecionada");

        ToggleButton btn_toggle_gravar = (ToggleButton) findViewById(R.id.btn_toggle_gravar);

        btn_toggle_gravar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    voice = new AudioRecord();
                    voice.startRecording();
                    Toast.makeText(EditarTarefa_Activity2.this, "Gravando", Toast.LENGTH_SHORT).show();
                } else {
                    pathAudio = voice.stopRecording();
                    Toast.makeText(EditarTarefa_Activity2.this, "Audio Gravado", Toast.LENGTH_SHORT).show();
                }
            }
        });


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }


    private void selectImage() {

        final CharSequence[] options = { "Tirar Foto", "Foto da Galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarTarefa_Activity2.this);
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
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

                Imagem imagem = new Imagem();
                Audio audio = new Audio();

                tarefa = new Tarefa();

                if (!tituloTarefa.getText().toString().equals("")) {
                    if (path != null) {
                        imagem.setCaminho(path);
                        idImagem = dbRotina.insere_imagem(imagem);
                        path = null;
                    }

                    if (pathAudio != null) {
                        audio.setCaminho(pathAudio);
                        idAudio = dbRotina.insere_audio(audio);
                        pathAudio = null;
                    }


                    if(idImagem != -1) {
                        tarefa.setIdImagem(idImagem);
                    }
                    if (idAudio != -1 ) {
                        tarefa.setIdAudio(idAudio);
                    }

                    tarefa.setIdRotina(rotina.getId());
                    tarefa.setOrdem(1);
                    tarefa.setTitulo(tituloTarefa.getText().toString());

                    dbRotina.insere_tarefa(tarefa);

                    dbRotina.close();

                    finish();
                } else {
                    Toast.makeText(EditarTarefa_Activity2.this, "NOME DA TAREFA OBRIGATORIO",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
