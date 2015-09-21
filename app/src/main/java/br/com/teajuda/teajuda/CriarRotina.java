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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import br.com.teajuda.teajuda.Classes.AudioRecord;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.Tarefa;
import br.com.teajuda.teajuda.Conexao.RotinaDao;


public class CriarRotina extends ActionBarActivity {

    AudioRecord voice = new AudioRecord();
    ImageView viewImage;
    FloatingActionButton chooseImage;
    String path;
    EditText tituloTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_rotina);

//        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        mFileName += "/audiorecordtest.3gp";

        FloatingActionButton gravar = (FloatingActionButton) findViewById(R.id.button_play);
        FloatingActionButton stop = (FloatingActionButton) findViewById(R.id.button_stop);

        tituloTarefa = (EditText) findViewById(R.id.edtTituloTarefa);

        final RotinaDao dbRotina = new RotinaDao(this);

        viewImage = (ImageView) findViewById(R.id.imagemTarefa);
        chooseImage = (FloatingActionButton) findViewById(R.id.button_slv_img);


        if(savedInstanceState == null) {

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
        }

        Button salvarVoltar = (Button) findViewById(R.id.btnSalVol);
        Button salvarNovo = (Button) findViewById(R.id.btnSalNov);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice.stopRecording();
                Toast.makeText(CriarRotina.this, "Audio Gravado", Toast.LENGTH_SHORT).show();
            }
        });

        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice.startRecording();
                Toast.makeText(CriarRotina.this, "Gravando", Toast.LENGTH_SHORT).show();
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
                Tarefa tarefa = new Tarefa();

                imagem.setCaminho(path);
                long idImagem = dbRotina.insere_imagem(imagem);

                tarefa.setIdImagem(idImagem);
                tarefa.setTitulo(tituloTarefa.toString());

                dbRotina.insere_tarefa(tarefa);

            }
        });

    }

    private void selectImage() {

        final CharSequence[] options = { "Tirar Foto", "Foto da Galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CriarRotina.this);
        builder.setTitle("Adicionar Foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tirar Foto"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
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
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    viewImage.setImageBitmap(bitmap);
                    path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/TEAjuda/Imagens/"+ System.currentTimeMillis()+".3gp";
                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
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
