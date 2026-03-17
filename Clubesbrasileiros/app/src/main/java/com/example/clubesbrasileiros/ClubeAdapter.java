package com.example.clubesbrasileiros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.clubesbrasileiros.modelo.Clube;
import com.example.clubesbrasileiros.utils.UtilsLocalDate;

import java.util.List;

public class ClubeAdapter extends BaseAdapter {

    private Context context;
    private List<Clube> listaClubes;

    private String[] estados;


    private static class ClubeHolder{
        public TextView textViewValorNome;
        public TextView textViewValorDataFundacao;
        public TextView textViewValorMundial;
        public TextView textViewValorEstado;
        public TextView textViewValorDivisao;

    }
    public ClubeAdapter(Context context, List<Clube> lsitaClubes) {
        this.context = context;
        this.listaClubes = lsitaClubes;

        estados = context.getResources().getStringArray(R.array.estados);
    }

    @Override
    public int getCount() {
        return listaClubes.size();
    }

    @Override
    public Object getItem(int i) {
        return listaClubes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ClubeHolder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_lista_clubes, viewGroup, false);

            holder = new ClubeHolder();

            holder.textViewValorNome = view.findViewById(R.id.textViewValorNomeClube);
            holder.textViewValorDataFundacao = view.findViewById(R.id.textViewValorDataFundacao);
            holder.textViewValorMundial = view.findViewById((R.id.textViewMundial));
            holder.textViewValorDivisao = view.findViewById((R.id.textViewDivisao));
            holder.textViewValorEstado = view.findViewById((R.id.textViewEstado));

            view.setTag(holder);
        }else{
            holder = (ClubeHolder) view.getTag();
        }

        Clube clube = listaClubes.get(i);

        holder.textViewValorNome.setText(clube.getNome());
        holder.textViewValorDataFundacao.setText(UtilsLocalDate.formatLocalDate(clube.getDataFundacao()));
        if(clube.isMundial()){
            holder.textViewValorMundial.setText(R.string.temMundial);
        } else{
            holder.textViewValorMundial.setText(R.string.nao_possui_mundial);
        }

        holder.textViewValorEstado.setText(estados[clube.getEstado()]);

        switch (clube.getDivisao()){
            case Serie_A:
                holder.textViewValorDivisao.setText(R.string.serieA);
                break;
            case Serie_B:
                holder.textViewValorDivisao.setText(R.string.serieB);
                break;
            case Serie_C:
                holder.textViewValorDivisao.setText(R.string.serieC);
                break;
            case Serie_D:
                holder.textViewValorDivisao.setText(R.string.serieD);
                break;
        }

        return view;
    }
}
