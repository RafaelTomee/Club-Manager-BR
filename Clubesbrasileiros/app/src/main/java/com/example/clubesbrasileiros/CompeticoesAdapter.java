package com.example.clubesbrasileiros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.clubesbrasileiros.modelo.Competicoes;
import com.example.clubesbrasileiros.utils.UtilsLocalDateTime;

import java.util.List;

public class CompeticoesAdapter extends BaseAdapter {

    private Context context;
    private List<Competicoes> listaCompeticoes;


    private static class CompeticoesHolder{
        public TextView textViewValorDiaHoraCriacao;
        public TextView textViewValorTexto;


    }
    public CompeticoesAdapter(Context context, List<Competicoes> lsitaCompeticoes) {
        this.context = context;
        this.listaCompeticoes = lsitaCompeticoes;

    }

    @Override
    public int getCount() {
        return listaCompeticoes.size();
    }

    @Override
    public Object getItem(int i) {
        return listaCompeticoes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        CompeticoesAdapter.CompeticoesHolder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_lista_competicoes, viewGroup, false);

            holder = new CompeticoesAdapter.CompeticoesHolder();

            holder.textViewValorDiaHoraCriacao = view.findViewById(R.id.textViewValorDiaHoraCriacao);
            holder.textViewValorTexto = view.findViewById(R.id.textViewValorTexto);

            view.setTag(holder);
        }else{
            holder = (CompeticoesAdapter.CompeticoesHolder) view.getTag();
        }

        Competicoes competicoes = listaCompeticoes.get(i);

        holder.textViewValorDiaHoraCriacao.setText(UtilsLocalDateTime.formatLocalDateTime(competicoes.getDiaHoraCriacao()));
        holder.textViewValorTexto.setText(competicoes.getText());


        return view;
    }
}
