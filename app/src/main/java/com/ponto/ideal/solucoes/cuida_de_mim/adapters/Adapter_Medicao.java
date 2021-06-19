package com.ponto.ideal.solucoes.cuida_de_mim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Medicao;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Adapter_Medicao extends RecyclerView.Adapter<Adapter_Medicao.ViewHolder> {

    private List<Medicao>  medlist = new ArrayList() ;
    private Context context;

    public Adapter_Medicao(List<Medicao> listmed
            , Context ctx) {
        medlist = listmed;
        context = ctx;
    }

    @Override
    public Adapter_Medicao.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rvmedicao, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(position);
//            Jogadores_Inscritos jogadoresins = joglist.get(position);
//            holder.txtnomeliga.setText(jogadoresins.getNomeins());
//            Bitmap bitmap = util.loadImageBitmap(context,jogadoresins.getIdins());
//            holder.imgshare.setImageBitmap(bitmap);
//
//
//            if (jogadoresins.getSelected()) {
//                holder.selectionState.setChecked(true);
//            } else {
//                holder.selectionState.setChecked(false);
//            }

    }



    @Override
    public int getItemCount() {
        return medlist.size();
    }

    void loaditem(List<Medicao> jogins){
        this.medlist =jogins;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtdata;
        public TextView txtmax;
        public TextView txtmin;
        public TextView txtgli;
        public TextView txtjej;



        public ViewHolder(View view) {
            super(view);
            txtdata =  view.findViewById(R.id.txtdata);
            txtmax =  view.findViewById(R.id.txtmax);
            txtmin =  view.findViewById(R.id.txtmin);
            txtgli = view.findViewById(R.id.txtgli);
            txtjej = view.findViewById(R.id.txtjej);
        }

        void bind(int position) {

            txtmax.setText(medlist.get(position).getPresmax());
            txtmin.setText(medlist.get(position).getPresmim());
            txtgli.setText(medlist.get(position).getGlicemia());
            LocalDate ld = util.ld_X_long(medlist.get(position).getTimestamp());
            txtdata.setText(util.fdma(ld));
            String jej = medlist.get(position).getJejum();
            if(jej.equals("0")){
                jej="J";
            }else if(jej.equals("1")){
                jej="A";
            }else{
                jej="-";
            }
            txtjej.setText(jej);

        }


        @Override
        public void onClick(View v) {



        }
    }
}
