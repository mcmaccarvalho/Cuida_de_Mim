package com.ponto.ideal.solucoes.cuida_de_mim.Alertas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Medicao;
import com.ponto.ideal.solucoes.cuida_de_mim.ui.home.HomeFragment;
import com.ponto.ideal.solucoes.cuida_de_mim.ui.home.HomeViewModel;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;

public class Confirma_Excluir extends DialogFragment {

    private static final String TAG = "Coletas";

    private Medicao medicao;
    private Button bteditar,btexcluir;
    private ConstraintLayout clcoleta;
    private FirebaseAuth mAuth;

    private HomeViewModel homeViewModel;

    public Confirma_Excluir(Medicao medicao) {
        this.medicao=medicao;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alerta_exclui, container, false);
        mAuth=FirebaseAuth.getInstance();
        initViews(v);
        return v;
    }

    private void initViews(View v) {


        homeViewModel=HomeFragment.homeViewModel;
        bteditar    =v.findViewById(R.id.bteditar );
        btexcluir    =v.findViewById(R.id.btexcluir );
        clcoleta    =v.findViewById(R.id.clcoleta  );

        bteditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });//

        btexcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("/usuarios")
                        .document(mAuth.getUid())
                        .collection("medicoes")
                        .document(medicao.getKeymed())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                homeViewModel.setApdint(1);
                                HomeFragment.vpclass.setCurrentItem(0);
                                getDialog().dismiss();
                                util.showmessage(getContext(),"Coleta excluida com sucesso.");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("mediçao","problemas ao salvar med " + e);
                    }
                });


            }
        });





    }






}
