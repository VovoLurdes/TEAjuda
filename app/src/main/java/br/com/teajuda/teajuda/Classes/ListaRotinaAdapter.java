package br.com.teajuda.teajuda.Classes;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.teajuda.teajuda.R;

/**
 * Created by foo on 04/10/15.
 */
public class ListaRotinaAdapter extends BaseAdapter{

    private final List<Rotina> rotinas;
    private final Activity activity;

    public ListaRotinaAdapter (Activity activity, List<Rotina> rotinas){
        this.activity = activity;
        this.rotinas = rotinas;
    }


    @Override
    public int getCount() {
        return this.rotinas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.rotinas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.rotinas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item, parent, false);

        TextView nome = (TextView) view.findViewById(R.id.item_nome);
        nome.setText(rotinas.get(position).getTitulo());

        return view;
    }


}
