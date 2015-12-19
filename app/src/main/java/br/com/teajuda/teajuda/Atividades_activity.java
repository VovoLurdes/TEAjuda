package br.com.teajuda.teajuda;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.List;

import br.com.teajuda.teajuda.Classes.Audio;
import br.com.teajuda.teajuda.Classes.Imagem;
import br.com.teajuda.teajuda.Classes.Rotina;
import br.com.teajuda.teajuda.Classes.Tarefa;
import br.com.teajuda.teajuda.Conexao.RotinaDao;

public class Atividades_activity extends ActionBarActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;



    private List<Imagem> imagem;
    private List<Audio> audio;

    private ViewPager mViewPager;

    private RotinaDao rotinaDao;
    private List<Tarefa> tarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades_activity);


        rotinaDao = new RotinaDao (this);

        //audio = rotinaDao.getAudio2();

        Intent edicao = this.getIntent();
        Rotina rotina = (Rotina) edicao.getSerializableExtra("rotinaselecionada");

        tarefas = rotinaDao.getListaTarefa(rotina.getId());

        rotinaDao.close();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atividades_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.getNewInstance(tarefas.get(position), Atividades_activity.this);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if(tarefas != null) {
                return tarefas.size();
            } else {
                return 0;
            }
        }

    }


    public static final class PlaceholderFragment extends Fragment {

        private Tarefa tarefa;
        private Imagem imagem = null;
        private Audio audio;
        private Context context;
        private Rotina rotina;

        public static PlaceholderFragment getNewInstance(Tarefa tarefa, Context context) {
            PlaceholderFragment instance = new PlaceholderFragment();
            instance.newInstance(tarefa, context);

            return instance;
        }

        public PlaceholderFragment() {
        }

        private PlaceholderFragment newInstance(Tarefa tarefa, Context context) {
            this.tarefa = tarefa;
            this.context = context;

            return this;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_atividades_activity, container, false);

            ImageView imgAtividade = (ImageView) rootView.findViewById(R.id.imgAtividade);
            ImageView imgAudioAtividade = (ImageView) rootView.findViewById(R.id.imgAudioAtividade);
            TextView  txtAtividade = (TextView) rootView.findViewById(R.id.txtAtividade);
            TextView txtRotina     = (TextView) rootView.findViewById(R.id.txtRotina);

            RotinaDao dao = new RotinaDao(context);

            imagem = dao.getImage(tarefa.getIdImagem());
            audio = dao.getAudio(tarefa.getIdAudio());
            rotina = dao.getRotina(tarefa.getIdRotina());

            dao.close();

            txtAtividade.setText(tarefa.getTitulo());
            txtRotina.setText(rotina.getTitulo());

            try {
                if (imagem.getCaminho() != null) {
                    Bitmap imagemFoto = BitmapFactory.decodeFile(imagem.getCaminho());
                    Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto, imagemFoto.getWidth(), 200, true);


                    imgAtividade.setImageBitmap(imagemFotoReduzida);
                    imgAtividade.setTag(imagem.getCaminho());
                    imgAtividade.setScaleType(ImageView.ScaleType.FIT_XY);

                }
            }catch (Exception e){
                Toast.makeText(context, "VERIFIQUE SE A IMAGEM NÃO FOI DELETADA", Toast.LENGTH_SHORT).show();
            }



                    imgAudioAtividade.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (audio.getCaminho() != null) {
                                MediaPlayer mPlayer = new MediaPlayer();
                                try {
                                    mPlayer.setDataSource(audio.getCaminho());
                                    mPlayer.prepare();
                                    mPlayer.start();
                                } catch (IOException e) {
                                    Log.e("LOG", "prepare() failed");
                                    Toast.makeText(context, "VERIFIQUE SE O AUDIO NÃO FOI DELETADO", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(context, "SEM AUDIO GRAVADO!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });




            //imgAtividade.setImageURI(Uri.fromFile(new File(imagem.get(0).getCaminho())));
            return rootView;
        }
    }
}
