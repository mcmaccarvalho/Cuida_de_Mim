package com.ponto.ideal.solucoes.cuida_de_mim.views;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.helper.ConfiguracaoFirebase;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Usuario;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;

public class Cadastrar_Usuario extends AppCompatActivity {

    boolean olho1 = true;
    boolean olho2 = true;
    private EditText edtnome, edtemail, edtsenha, edtrepsenha;
    private ImageView eyesenhablack, eyerepsenhablack;
    private Button btcad, btcancel;
    private  static ConstraintLayout clcad;
    private  static ProgressBar mprogressBar;
    public static boolean blok;
    private boolean doubleBackToExitPressedOnce =false;
    public static String CAD_EMAIL, CAD_NOME, CAD_SENHA, CAD_KEYUSU;
    private FirebaseAuth autenticacao;
    private String photourl;
    private Uri photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar__usuario);
        initViews();
    }

    private void initViews() {
        mprogressBar = findViewById(R.id.mprogressBar);
        edtnome = findViewById(R.id.edtnome);
        edtemail = findViewById(R.id.edtemail);
        edtsenha = findViewById(R.id.edtsenha);
        edtrepsenha = findViewById(R.id.edtrepsenha);
        btcad = findViewById(R.id.btcad);
        btcancel = findViewById(R.id.btcancel);
        eyesenhablack = findViewById(R.id.eyesenhablack);
        eyerepsenhablack = findViewById(R.id.eyerepsenhablack);

        clcad = findViewById(R.id.clcad);

        clcad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });




        eyesenhablack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (olho1) {
                    edtsenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyesenhablack.setImageResource(R.drawable.ic_eye_red);
                } else {
                    edtsenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyesenhablack.setImageResource(R.drawable.ic_noeye_black);
                }

                olho1 = !olho1;

            }
        });

        eyerepsenhablack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (olho2) {
                    edtrepsenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyerepsenhablack.setImageResource(R.drawable.ic_eye_red);
                } else {
                    edtrepsenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyerepsenhablack.setImageResource(R.drawable.ic_noeye_black);
                }

                olho2 = !olho2;

                showProgressBar(false);

            }
        });


        btcad.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                edtnome.setText(edtnome.getText().toString().trim().toUpperCase());
                edtemail.setText(edtemail.getText().toString().trim().toLowerCase());
                cadastrarUsuario();
            }
        });




        edtnome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edtnome.setText(edtnome.getText().toString().trim().toUpperCase());
                if (hasFocus) {
                    edtnome.setSelection(edtnome.getText().toString().length());
                }
            }
        });


        edtemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                edtemail.setText(edtemail.getText().toString().trim().toLowerCase());
                if (hasFocus) {
                    edtemail.setSelection(edtemail.getText().toString().length());
                }
            }
        });

        edtsenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edtsenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                eyesenhablack.setImageResource(R.drawable.ic_noeye_black);
                edtsenha.setSelection(edtsenha.getText().toString().length());

            }
        });

        edtrepsenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edtrepsenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                eyerepsenhablack.setImageResource(R.drawable.ic_noeye_black);
                edtrepsenha.setSelection(edtrepsenha.getText().toString().length());
            }
        });



        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
//                        startActivity(intent2);
//                        getActivity().finish();

            }
        });

    }

    private void cadastrarUsuario() {

        hideSoftKeyboard();

        if (edtnome.getText().toString().isEmpty() ) {
            util.showSnackError(clcad, "Preencha campo Nome.");
            edtnome.requestFocus();
            return;
        } else if (!edtemail.getText().toString().contains("@") || !edtemail.getText().toString().contains(".")) {
            util.showSnackError(clcad, "Insira um e_mail válido.");
            edtemail.requestFocus();
            return;
        } else if (edtsenha.getText().toString().trim().isEmpty()
            ||edtsenha.getText().toString().trim().equals("")) {
            util.showSnackError(clcad, "Preencha campo Senha.");
            edtsenha.requestFocus();
            return;
        } else if (!edtsenha.getText().toString().trim().equals(edtrepsenha.getText().toString().trim())
                ) {
            util.showSnackError(clcad, "Os campos de Senha não são iguais.");
            edtsenha.requestFocus();
            return;
        } else {

            CAD_EMAIL = edtemail.getText().toString().toLowerCase().trim();
            CAD_NOME = edtnome.getText().toString().trim();
            CAD_SENHA = edtsenha.getText().toString().trim();
            bloqueiaTD(true);
            showProgressBar(true);
            criaCredenciais();
        }

    }

    public void criaCredenciais(){

        String senha = CAD_SENHA;
        final String email = CAD_EMAIL;

        autenticacao = FirebaseAuth.getInstance();
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(
                email,
                senha
        ).addOnCompleteListener(Cadastrar_Usuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    CAD_KEYUSU = autenticacao.getUid();
                    cadastraUsu();
                } else {
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        Log.e("login 1",": " + e.getMessage());
                        erroExcecao = "Esse e-mail já está cadastrado!";
                    } catch (FirebaseAuthWeakPasswordException e) {
                        Log.e("login 2",": " + e.getMessage());
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e que contenha letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Log.e("login 3",": " + e.getMessage());
                        erroExcecao = "O e-mail digitado é invalido, digite um novo e-mail";
                    } catch (Exception e) {
                        Log.e("login 4",": " + e.getMessage());
                        erroExcecao = "Erro ao efetuar o cadastro!\nVerfique sua conexão.";
                        e.printStackTrace();
                    }
                    bloqueiaTD(false);
                    showProgressBar(false);
                    util.showSnackError(clcad,erroExcecao);

                }

            }
        });

    }

    private void cadastraUsu() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(CAD_NOME)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });


        String apelido = "";

        for (int i = 0; i < CAD_NOME.length(); i++) {
            if (!CAD_NOME.substring(i, i + 1).equals(" ")) {
                apelido += CAD_NOME.substring(i, i + 1);
            } else {
                break;
            }
        }

        Usuario usu = new Usuario();
        usu.setNomeusu(CAD_NOME);
        usu.setKeyusu(CAD_KEYUSU);
        usu.setEmailusu(CAD_EMAIL);
        usu.setImagemusuario("gs://mycare-82ab2.appspot.com/care_512.png");
        usu.setTimestamp(System.currentTimeMillis());
        usu.setOnline(1);
        usu.setToken("");
        usu.setApelidousu(apelido);

        FirebaseFirestore.getInstance().collection("/usuarios")
                .document(usu.getKeyusu())
                .set(usu)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showProgressBar(false);
                        bloqueiaTD(false);
                        util.showmessage(Cadastrar_Usuario.this,"Usuário salvo com sucesso.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                util.showmessage(Cadastrar_Usuario.this,"Problemas ao salvar Usuário.");
                showProgressBar(false);
                bloqueiaTD(false);
            }
        });

    }

    private void showProgressBar(boolean b) {
        if(b){
            mprogressBar.setVisibility(View.VISIBLE);
        }else{
            mprogressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void bloqueiaTD(boolean b) {
        btcancel.setClickable(!b);
        btcancel.setEnabled(!b);
        btcad.setClickable(!b);
        btcad.setEnabled(!b);
        edtnome.setClickable(!b);
        edtnome.setEnabled(!b);
        edtemail.setClickable(!b);
        edtemail.setEnabled(!b);
        edtsenha.setClickable(!b);
        edtsenha.setEnabled(!b);
        edtrepsenha.setClickable(!b);
        edtrepsenha.setEnabled(!b);
        blok=b;
    }

    public void onBackPressed() {
        if (!blok) {
            if (doubleBackToExitPressedOnce) {
                util.showmessage(Cadastrar_Usuario.this, "Obrigado por utilizar o Cuida de Mim.");
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(Cadastrar_Usuario.this, "Pressione novamente para sair.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }else{
            Toast.makeText(Cadastrar_Usuario.this, "Aguarde o final do processo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideSoftKeyboard() {
        edtnome.setText(edtnome.getText().toString().trim().toUpperCase());
        edtemail.setText(edtemail.getText().toString().trim().toLowerCase());
        if (Cadastrar_Usuario.this.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)Cadastrar_Usuario.this.getSystemService(Cadastrar_Usuario.this.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Cadastrar_Usuario.this.getCurrentFocus().getWindowToken(), 0);
        }
    }

}