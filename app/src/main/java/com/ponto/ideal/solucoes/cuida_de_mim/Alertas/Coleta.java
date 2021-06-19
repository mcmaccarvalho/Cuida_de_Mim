package com.ponto.ideal.solucoes.cuida_de_mim.Alertas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Medicao;
import com.ponto.ideal.solucoes.cuida_de_mim.ui.home.HomeFragment;
import com.ponto.ideal.solucoes.cuida_de_mim.ui.home.Nova_Medicao;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;

public class Coleta  extends DialogFragment {

    private static final String TAG = "Coletas";

    private Medicao medicao;
    private FirebaseAuth mAuth;
    private Button bteditar,btexcluir;
    private TextView txtdia,txthora,txtmax,txtmin,txtgli,txtjejum;
    private boolean excluir = false;
    private ConstraintLayout clcoleta;


    public Coleta (Medicao medicao) {
        this.medicao=medicao;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alerta_coleta, container, false);
        mAuth=FirebaseAuth.getInstance();
        initViews(v);
        return v;
    }

    private void initViews(View v) {


        bteditar    =v.findViewById(R.id.bteditar );
        btexcluir    =v.findViewById(R.id.btexcluir );
        txtdia    =v.findViewById(R.id.txtdia  );
        txthora    =v.findViewById(R.id.txthora  );
        txtmax    =v.findViewById(R.id.txtmax  );
        txtmin    =v.findViewById(R.id.txtmin  );
        txtgli    =v.findViewById(R.id.txtgli  );
        txtjejum    =v.findViewById(R.id.txtjejum  );
        clcoleta    =v.findViewById(R.id.clcoleta  );

        txtmax.setText(medicao.getPresmax());
        txtmin.setText(medicao.getPresmim());
        txtgli.setText(medicao.getGlicemia());
        if(medicao.getGlicemia().equals("-")){
            txtjejum.setText("");
        }else if(medicao.getJejum().equals("0")){
            txtjejum.setText("Jejum");
        }else{
            txtjejum.setText("Alimentado");
        }


        txtdia.setText(util.fdma(util.ld_X_long(medicao.getTimestamp())));
        txthora.setText(util.toDateStr(medicao.getTimestamp(), "HH:mm"));


        bteditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nova_Medicao.edit=true;
                Nova_Medicao.carregaMedicao();
                HomeFragment.vpclass.setCurrentItem(1);
                getDialog().dismiss();
            }
        });//

        btexcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirma_Excluir confirma_excluir = new Confirma_Excluir(medicao);
                confirma_excluir.show(getActivity().getSupportFragmentManager(), "DialogTeclado");
                getDialog().dismiss();

            }
        });





    }






}
