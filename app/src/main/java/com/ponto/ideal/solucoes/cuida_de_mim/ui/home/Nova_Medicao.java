package com.ponto.ideal.solucoes.cuida_de_mim.ui.home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Medicao;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.UUID;

public class Nova_Medicao extends Fragment {

    public static EditText edtmax,edtmin,edtglicemia;
    public static RadioButton rbjejum,rbalim;
    public static RadioGroup radioGroup;
    public static TextView txtdata,txthora;
    public static  Button btcad,btcancel;
    private ConstraintLayout clmedicao;
    private String shoje;
    private String thoje;
    public static  boolean edit=false;
    private FirebaseAuth mauth;

    private HomeViewModel homeViewModel;
    DateTimeFormatter dma = DateTimeFormatter
            .ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    DateTimeFormatter hh = DateTimeFormatter
            .ofPattern("HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    DateFormat HHH = new SimpleDateFormat("HH:mm");

    public Nova_Medicao() {

    }
    //sistólica MAX Diastólica Min Glicemia Jejum/apos duas horas
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nova__medicao, container, false);
        mauth=FirebaseAuth.getInstance();
        intiViews(v);
        return v;
    }

    private void intiViews(View v) {

        homeViewModel=HomeFragment.homeViewModel;
        edtmax = v.findViewById(R.id.etdmax);
        edtmin = v.findViewById(R.id.etdmin);
        edtglicemia = v.findViewById(R.id.etdglicemia);
        rbjejum = v.findViewById(R.id.rbjejum);
        rbalim = v.findViewById(R.id.rbalim);
        txtdata = v.findViewById(R.id.txtdata);
        txthora = v.findViewById(R.id.txthora);
        btcad = v.findViewById(R.id.btcad);
        btcancel = v.findViewById(R.id.btcancel);
        clmedicao = v.findViewById(R.id.clmedicao);
        radioGroup = v.findViewById(R.id.radioGroup);
        LocalDate hoje = util.ld_X_long(System.currentTimeMillis());

        shoje = hoje.format(dma);
        thoje = util.toDateStr(System.currentTimeMillis(), "HH:mm");

        txtdata.setText(shoje);
        txthora.setText(thoje);

        long ll = util.long_X_st_dmahm(shoje+" "+thoje);
        Log.i("horadia"," : " + ll);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                monthOfYear = monthOfYear + 1;
                String YEAR = String.valueOf(year);
                String MONTH =String.format("%02d", new Object[] { monthOfYear });
                String DAY = String.format("%02d", new Object[] { dayOfMonth });
                String date =  DAY+ "-" +MONTH+ "-" +YEAR;
                txtdata.setText(date);
            }};
        final TimePickerDialog.OnTimeSetListener  mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {
               // myCalendar.setTimeInMillis(System.currentTimeMillis());
                myCalendar.set(Calendar.HOUR_OF_DAY, hora);
                myCalendar.set(Calendar.MINUTE, minuto);
                String HORA = String.valueOf(hora);
                String MIN = String.valueOf(minuto);
                String tthh=HORA+":"+MIN;
                txthora.setText(tthh);
            }
        };

        txtdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                dialog.show();
            }
        });

        edtglicemia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(edtglicemia.getText().toString().equals(""))radioGroup.clearCheck();
            }
        });

        txthora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        mTimeSetListener,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE),
                        true);
                dialog.show();

            }
        });

        clmedicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        btcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalDate hoje2 = util.ld_X_long(System.currentTimeMillis());
                    shoje = hoje2.format(dma);
                    thoje = util.toDateStr(System.currentTimeMillis(), "HH:mm");
                    edtglicemia.setText("");
                    edtmin.setText("");
                    edtmax.setText("");
                    txtdata.setText(shoje);
                    txthora.setText(thoje);
                    radioGroup.clearCheck();

                }
            }
        );

        btcad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String smax = edtmax.getText().toString();
                String smin = edtmin.getText().toString();
                String sgli = edtglicemia.getText().toString();
                String srb ="";
                if(sgli.equals(""))radioGroup.clearCheck();
                if(rbjejum.isChecked())srb="0";
                if(rbalim.isChecked())srb="1";

                if(!smax.equals("") && smin.equals("")){
                    util.showSnackError(clmedicao,"Infome Pressão Máxima e Mínima");
                    return;
                }else if(!smin.equals("") && smax.equals("")){
                    util.showSnackError(clmedicao,"Infome Pressão Máxima e Mínima");
                    return;
                }else if(!sgli.equals("") && srb.equals("")){
                    util.showSnackError(clmedicao,"Infome se em Jejum ou Alimetado");
                    return;
                }else if(sgli.equals("") && smax.equals("")){
                    util.showSnackError(clmedicao,"Infome dados da Medição");
                    return;
                }else{

                    if (sgli.equals(""))sgli="-";
                    if (smin.equals(""))smin="-";
                    if (smax.equals(""))smax="-";
                    if (srb.equals(""))srb="-";

                    Medicao medicao = new Medicao();
                    String shoje = txtdata.getText().toString();
                    String thoje = txthora.getText().toString();

                    long ll = util.long_X_st_dmahm(shoje+" "+thoje);
                    Log.i("horadia"," : " + ll);

                    String key ="";
                    if(edit){
                       key=Lista_Medicao.oldmed.getKeymed();
                    }else {
                        key = UUID.randomUUID().toString();
                    }
                    medicao.setKeymed(key);
                    medicao.setPresmax(smax);
                    medicao.setPresmim(smin);
                    medicao.setGlicemia(sgli);
                    medicao.setJejum(srb);
                    medicao.setTimestamp(ll);

                    FirebaseFirestore .getInstance().collection("/usuarios")
                            .document(mauth.getUid())
                            .collection("medicoes")
                            .document(medicao.getKeymed())
                            .set(medicao)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    btcad.setText("Salvar");
                                    edit=false;
                                    LocalDate hoje2 = util.ld_X_long(System.currentTimeMillis());
                                    String sshoje = hoje2.format(dma);
                                    String tthoje = util.toDateStr(System.currentTimeMillis(), "HH:mm");
                                    edtglicemia.setText("");
                                    edtmin.setText("");
                                    edtmax.setText("");
                                    txtdata.setText(sshoje);
                                    txthora.setText(tthoje);
                                    radioGroup.clearCheck();
                                    homeViewModel.setApdint(1);
                                    HomeFragment.vpclass.setCurrentItem(0);
                                    util.showmessage(getContext(),"Coleta Salva com sucesso.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("mediçao","problemas ao salvar med " + e);
                        }
                    });

                    Log.i("medicao","smax: " + smax);
                    Log.i("medicao","smin: " + smin);
                    Log.i("medicao","sgli: " + sgli);
                    Log.i("medicao","srb: " + srb);
                }




            }
        });



    }

    public String ler ( ) {
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = getContext().openFileInput ( "relatorio.txt" ) ;
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
                datax.append(readString);
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
            System.out.println("ok");
        } catch ( IOException ioe ) {
            System.out.println("Deu erro");
            ioe.printStackTrace ( ) ;
        }
        return datax.toString() ;
    }

    public static void carregaMedicao(){

        edtmax.setText(Lista_Medicao.oldmed.getPresmax());
        edtmin.setText(Lista_Medicao.oldmed.getPresmim());
        edtglicemia.setText(Lista_Medicao.oldmed.getGlicemia());

        if(Lista_Medicao.oldmed.getGlicemia().equals("-")){
            radioGroup.clearCheck();
        }else if(Lista_Medicao.oldmed.getJejum().equals("0")){
            rbjejum.setChecked(true);
            rbalim.setChecked(false);
        }else{
            rbjejum.setChecked(false);
            rbalim.setChecked(true);
        }

        txtdata.setText(util.fdma(util.ld_X_long(Lista_Medicao.oldmed.getTimestamp())));
        txthora.setText(util.toDateStr(Lista_Medicao.oldmed.getTimestamp(), "HH:mm"));

        btcad.setText("Alterar");
    }

    private void hideSoftKeyboard() {
       if (getActivity().getCurrentFocus() != null) {

            InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            edtglicemia.clearFocus();
            edtmax.clearFocus();
            edtmin.clearFocus();
        }
    }

}