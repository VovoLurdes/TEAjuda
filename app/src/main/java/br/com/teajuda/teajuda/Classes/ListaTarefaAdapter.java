package br.com.teajuda.teajuda.Classes;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.teajuda.teajuda.R;

/**
 * Created by foo on 19/10/15.
 */
public class ListaTarefaAdapter extends BaseAdapter {

    private final List<Tarefa> tarefas;
    private final Activity activity;

    public ListaTarefaAdapter (Activity activity, List<Tarefa> tarefas){
        this.activity = activity;
        this.tarefas = tarefas;
    }


    @Override
    public int getCount() {
        return this.tarefas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tarefas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.tarefas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item, parent, false);

        TextView nome = (TextView) view.findViewById(R.id.item_nome);
        nome.setText(tarefas.get(position).getTitulo());

        return view;
    }

}
