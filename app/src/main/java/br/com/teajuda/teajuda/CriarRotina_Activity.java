package br.com.teajuda.teajuda;


import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import br.com.teajuda.teajuda.Classes.Audio;
import br.com.teajuda.teajuda.Classes.AudioRecord;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Classes.Tarefa;
import br.com.teajuda.teajuda.Conexao.RotinaDao;


public class CriarRotina_Activity extends ActionBarActivity {

    AudioRecord voice;
    ImageView viewImage;
    FloatingActionButton chooseImage;
    String path;
    String pathAudio;
    EditText tituloTarefa;
    TextView tituloRotina;

    long idRotina = -1;
    long idImagem = -1;
    long idAudio = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_rotina);

        final int[] contador = {0};

//        mFileName = Environt.getExternalStorageDirectory().getAbsolutePath();
//        mFileName += "/audiorecordtest.3gp";

        final FloatingActionButton gravar = (FloatingActionButton) findViewById(R.id.button_play);
        final FloatingActionButton stop = (FloatingActionButton) findViewById(R.id.button_stop);

        stop.setVisibility(View.INVISIBLE);

        tituloRotina = (TextView) findViewById(R.id.tituloRotina);
        tituloTarefa = (EditText) findViewById(R.id.edtTituloTarefa);

        final RotinaDao dbRotina = new RotinaDao(this);

        viewImage = (ImageView) findViewById(R.id.imagemTarefa);
        chooseImage = (FloatingActionButton) findViewById(R.id.button_slv_img);


        if (savedInstanceState == null) {

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
                    if (nomeRotina.getText().toString().equals("")) {
                        Toast.makeText(CriarRotina_Activity.this, "NOME DA ROTINA OBRIGATORIO",
                                Toast.LENGTH_LONG).show();
                    } else {
                        tituloRotina.setText(nomeRotina.getText().toString());
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
        }

        Button salvarVoltar = (Button) findViewById(R.id.btnSalVol);
        Button salvarNovo = (Button) findViewById(R.id.btnSalNov);


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pathAudio = voice.stopRecording();
                Toast.makeText(CriarRotina_Activity.this, "Audio Gravado", Toast.LENGTH_SHORT).show();
                stop.setVisibility(View.INVISIBLE);
                gravar.setVisibility(View.VISIBLE);
            }
        });

        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice = new AudioRecord();
                voice.startRecording();
                Toast.makeText(CriarRotina_Activity.this, "Gravando", Toast.LENGTH_SHORT).show();
                stop.setVisibility(View.VISIBLE);
                gravar.setVisibility(View.INVISIBLE);
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        salvarVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagem imagem = new Imagem();
                Audio audio = new Audio();
                Tarefa tarefa = new Tarefa();
                Rotina rotina = new Rotina();


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

                    if (contador[0] == 0) {
                        rotina.setTitulo(tituloRotina.getText().toString());
                        rotina.setOrdem(1);
                        idRotina = dbRotina.insere_rotina(rotina);
                        contador[0] = 1;
                    }


                    if(idImagem != -1) {
                        tarefa.setIdImagem(idImagem);
                    }
                    if (idAudio != -1 ) {
                        tarefa.setIdAudio(idAudio);
                    }

                    tarefa.setIdRotina(idRotina);
                    tarefa.setOrdem(1);
                    tarefa.setTitulo(tituloTarefa.getText().toString());

                    dbRotina.insere_tarefa(tarefa);

                    dbRotina.close();

                    finish();
                } else {
                    Toast.makeText(CriarRotina_Activity.this, "NOME DA TAREFA OBRIGATORIO",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        salvarNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagem imagem = new Imagem();
                Audio audio = new Audio();
                Tarefa tarefa = new Tarefa();
                Rotina rotina = new Rotina();

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

                    if (contador[0] == 0) {
                        rotina.setTitulo(tituloRotina.getText().toString());
                        rotina.setOrdem(1);
                        idRotina = dbRotina.insere_rotina(rotina);
                        contador[0] = 1;
                    }

                    tarefa.setIdImagem(idImagem);
                    tarefa.setIdAudio(idAudio);
                    tarefa.setIdRotina(idRotina);
                    tarefa.setOrdem(1);
                    tarefa.setTitulo(tituloTarefa.getText().toString());

                    dbRotina.insere_tarefa(tarefa);


                    dbRotina.close();
                    tituloTarefa.setText("");
                    viewImage.setImageResource(R.drawable.ic_no_image);
                } else{
                    Toast.makeText(CriarRotina_Activity.this, "NOME DA TAREFA OBRIGATORIO",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void selectImage() {

        final CharSequence[] options = { "Tirar Foto", "Foto da Galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CriarRotina_Activity.this);
        builder.setTitle("Adicionar Foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tirar Foto"))
                {
                    path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/TEAjuda/Imagens/"+ System.currentTimeMillis()+".jpg";
                    Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri localFoto = Uri.fromFile(new File(path));
                    irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);
                    startActivityForResult(irParaCamera, 1);
                }
                else if (options[item].equals("Foto da Galeria"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }

                else if (options[item].equals("Cancelar")) {
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


 /*   @Override
    public void onPause() {
        super.onPause();
        if ( voice != null) {
            voice.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

    }*/

}
