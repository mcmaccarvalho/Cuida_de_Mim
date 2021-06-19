package com.ponto.ideal.solucoes.cuida_de_mim.ui.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.adapters.Adapter_Medicao;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Medicao;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;

import java.io.File;
import java.util.ArrayList;

public class Lista_Medicao extends Fragment {


    private RecyclerView rvmedicao;
    private ConstraintLayout cllista;
    //public static GroupAdapter adapter;
    private ArrayList<Medicao> arraymed=new ArrayList<>();
    private Adapter_Medicao adapterMedicao;
    private HomeViewModel homeViewModel;
    public static Medicao oldmed;
    public static boolean edit=false;
    private ProgressBar mprogressBar;


    private File diretorio;
    private String nomeDiretorio;
    private String diretorioApp;
    private ArrayList<String> Arquivos = new ArrayList<String>();
    private Uri uri2;

    private String uuu;
    String[] filesdir;
    private FirebaseAuth mAuth;
    private ArrayList<Medicao> baseMed = new ArrayList<>();
    public Lista_Medicao() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista__medicao, container, false);
        mAuth=FirebaseAuth.getInstance();
        initViews(v);
        return v;
    }

    private void initViews(View v) {

        Log.i("medicao"," new login " + mAuth.getCurrentUser().getDisplayName());

        homeViewModel=HomeFragment.homeViewModel;
        homeViewModel.getApdint().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                if (i == 1) {
                    homeViewModel.setApdint(0);
                    carregaLista();
                }
            }
        });

        mprogressBar   = v.findViewById(R.id.mprogressBar      );
        rvmedicao      = v.findViewById(R.id.rvmedicao      );
        cllista   = v.findViewById(R.id.cllista   );
        mprogressBar.setVisibility(View.VISIBLE);

      //  adapter = new GroupAdapter();
     //   carregaAdapter();



//
//        adapterMedicao = new Adapter_Medicao(getContext(),arraymed);
//        adapterMedicao.notifyDataSetChanged();
//        rvmedicao.setAdapter(adapterMedicao);


//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull Item item, @NonNull View view) {
//
//            util.vibratePhone(getContext(), (short) 20);
//
//                ListaMed listaMed = (ListaMed) item;
//                Medicao med = listaMed.medicao;
//                oldmed = med;
//                Coleta coleta = new Coleta(med);
//                coleta.show(getActivity().getSupportFragmentManager(), "DialogTeclado");
//
//            }
//        });

        carregaLista();

    }


    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        catch(Exception ex){
             return false;
        }
    }

    private void carregaLista() {

        FirebaseFirestore.getInstance().collection("/usuarios")
                .document(mAuth.getUid())
                .collection("medicoes")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            baseMed.clear();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Medicao imed = doc.toObject(Medicao.class);
                                baseMed.add(imed);

                            }
                            carregaAdapter();
                        }else {
                            Log.d("medicao", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    private void carregaAdapter() {
        if(baseMed.size()==0){
            util.showmessage(getContext(),"Não existem dados para exibir");
            mprogressBar.setVisibility(View.INVISIBLE);
        }


        mprogressBar.setVisibility(View.INVISIBLE);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getContext());
        rvmedicao.setLayoutManager(recyclerLayoutManager);

        Adapter_Medicao recyclerViewAdapter = new
                Adapter_Medicao(baseMed,getContext());
        rvmedicao.setAdapter(recyclerViewAdapter);

    }


}

//sistólica MAX Diastólica Min Glicemia Jejum/apos duas horas