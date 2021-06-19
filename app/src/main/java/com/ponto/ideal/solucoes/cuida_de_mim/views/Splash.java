package com.ponto.ideal.solucoes.cuida_de_mim.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.helper.CheckApp;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {

    public static final int APP_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final int APP_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGEE = 1;
    private FirebaseAuth autenticacao;
    private ProgressBar mprogressBar;
    private CardView cvcancelar;
    private ConstraintLayout clsplash;
    private ImageView imgheart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
    }

    private void initViews() {


        mprogressBar = findViewById(R.id.mprogressBar);

        imgheart = findViewById(R.id.imgheart);
        mprogressBar.setVisibility(View.VISIBLE);
        imgheart.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        imgheart.startAnimation(AnimationUtils.loadAnimation(Splash.this, R.anim.abre_manual));

        if (CheckApp.verificarGooglePlayServices(Splash.this)) {
            checar_permissoes();
        } else {
            mprogressBar.setVisibility(View.INVISIBLE);
            util.showmessage(getApplicationContext(), "Google Play Service não instalado");
        }

    }

    private void checar_permissoes() {
        if (Build.VERSION.SDK_INT < 23) {
            carregausu();
        } else if (checkAndRequestPermissions()) {
            carregausu();
        } else {

        }
    }

    public boolean checkAndRequestPermissions() {

        boolean retorno = true;

        List<String> permissoesnecessarias = new ArrayList<>();

        int permissaoWrite =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissaoRead =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissaoWrite != PackageManager.PERMISSION_GRANTED) {
            permissoesnecessarias.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissaoRead != PackageManager.PERMISSION_GRANTED) {
            permissoesnecessarias.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!permissoesnecessarias.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissoesnecessarias.toArray(new String[permissoesnecessarias.size()]),
                    APP_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGEE);
            retorno = false;
        }

        return retorno;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case APP_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGEE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    carregausu();
                } else {
                    mprogressBar.setVisibility(View.INVISIBLE);
                  //  clsplash.addView(cvcancelar);

                }
            }

            case APP_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    carregausu();
                } else {
                }
            }
        }
    }

    public void carregausu() {


        final int SPLASH_TIME_OUT = 5000;

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                mprogressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

}