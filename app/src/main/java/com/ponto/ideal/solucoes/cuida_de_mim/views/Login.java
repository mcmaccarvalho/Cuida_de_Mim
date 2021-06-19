package com.ponto.ideal.solucoes.cuida_de_mim.views;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.helper.ConfiguracaoFirebase;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Usuario;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;

public class Login extends AppCompatActivity {

    private SignInButton sign_in_button;
    private EditText edtnome,edtsenha;
    private TextView txtesquecisenha,textView50;
    private Button btcad,btlogar;
    private ImageView eyesenhablack;
    private ConstraintLayout cllogin;
    private ProgressBar mprogressBar;

    public static boolean newlogin=false;
    public static boolean blok;
    private boolean olho1=true;
    private boolean doubleBackToExitPressedOnce =false;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Context context;
    private Usuario usuLog = new Usuario();
    private Usuario usu = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=getBaseContext();
        mAuth = FirebaseAuth.getInstance();
        initViews();
    }

    private void initViews() {

        sign_in_button= findViewById(R.id.sign_in_button);
        edtnome = findViewById(R.id.edtnome);
        edtsenha = findViewById(R.id.edtsenha);
        txtesquecisenha = findViewById(R.id.txtesquecisenha);
        btcad = findViewById(R.id.btcad);
        btlogar = findViewById(R.id.btlogar);
        eyesenhablack = findViewById(R.id.eyesenhablack);
        cllogin = findViewById(R.id.cllogin);
        mprogressBar = findViewById(R.id.mprogressBar);

        textView50 = findViewById(R.id.textView50);

        showProgressBar(true);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestProfile()
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
                signIn();
            }
        });

        textView50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
            }
        });
        textView50.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                signOut();
                return true;
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

        cllogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        edtnome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    edtnome.setSelection(0);
                }else{
                    edtnome.setText(edtnome.getText().toString().toLowerCase());
                }
            }
        });

        edtsenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edtsenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                eyesenhablack.setImageResource(R.drawable.ic_noeye_black);
                if (!hasFocus) {

                } else {

                }
            }
        });

        btlogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uemail = edtnome.getText().toString();
                String usenha = edtsenha.getText().toString();

                if(uemail.equals("") || uemail.isEmpty() || uemail.equals(null)){
                    util.showSnackError(cllogin,"Informe email para Logim");
                    return;
                }else if(usenha.equals("") || usenha.isEmpty() || usenha.equals(null)){
                    util.showSnackError(cllogin,"Informe senha para Logim");
                    return;
                }else {
                    bloqueiaTD(true);
                    showProgressBar(true);
                    edtnome.setText(edtnome.getText().toString().toLowerCase());
                    validarLogin();
                }

            }
        });

        btcad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastrar_Usuario.class);
                startActivity(intent);
                finish();
            }
        });


        txtesquecisenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String emailAddress = edtnome.getText().toString();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent." + emailAddress);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Exception. "+e+" " + emailAddress);
                    }
                });
            }
        });
        //checausu();


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        showProgressBar(false);
        if (user != null) {
            Intent intent2 = new Intent(Login.this, TelaActivity.class);
            startActivity(intent2);
            Login.this.finish();
        }
    }

    public void getProviderData() {

        int ddd =0;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {



                String providerId = profile.getProviderId();
                Log.i(TAG, "providerId: " + providerId);
                final String uid = profile.getUid();
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
                Log.i(TAG, "Nome: " + name);
                Log.i(TAG, "email: " + email);
                Log.i(TAG, "uri: " + photoUrl.toString());

                String apelido = "";

                for (int i = 0; i < name.length(); i++) {
                    if (!name.substring(i, i + 1).equals(" ")) {
                        apelido += name.substring(i, i + 1);
                    } else {
                        break;
                    }
                }


                if(user.getUid().equals(uid)) {

                    usu = new Usuario();
                    usu.setNomeusu(name);
                    usu.setKeyusu(uid);
                    usu.setEmailusu(email);
                    usu.setImagemusuario(photoUrl.toString());
                    usu.setTimestamp(System.currentTimeMillis());
                    usu.setOnline(1);
                    usu.setToken("");
                    usu.setApelidousu(apelido);

                    checacad(usu);

                }

            }

        }

    }

    private void checacad(final Usuario usu) {
        Log.i(TAG, "usu time novo " + usu.getTimestamp());

        FirebaseFirestore.getInstance().collection("/usuarios")
                .document(usu.getKeyusu())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {
                            usuLog = documentSnapshot.toObject(Usuario.class);
                            //usu.setTimestamp(usuLog.getTimestamp());

                        }
                        cadastraUsuario(usuLog);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("retligas", "e " + e.getMessage() + ": " + e);
            }
        });

    }

    private void cadastraUsuario(Usuario nusu) {
        if(nusu!=null){
            usu.setTimestamp(nusu.getTimestamp());
        }
        Log.i(TAG, "usu time alterado " + usu.getTimestamp());
        FirebaseFirestore.getInstance().collection("/usuarios")
                .document(usu.getKeyusu())
                .set(usu)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        newlogin=true;
                        updateUI(mAuth.getCurrentUser());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "requestCode: " + requestCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Log.i(TAG, "entrou rsult");

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i(TAG, "firebaseAuthWithGoogle:" + account.getId());
                Log.i(TAG, "firebaseAuthWithGooglegetIdToken:" + account.getIdToken());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.i(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                newlogin=false;
                updateUI(null);
                // [END_EXCLUDE]
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        showProgressBar(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //checaCadastro(user.getUid());
                            Log.d(TAG, "cess " +user.getUid());
                            getProviderData();
                        } else {
                            // If sign in fails, display a message to the user.
                            showProgressBar(false);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            util.showSnackError(cllogin,"Authentication Failed.");
                            newlogin=false;
                            updateUI(null);
                        }


                        // ...
                    }
                });
    }

    private void validarLogin() {
        mAuth = FirebaseAuth.getInstance();
        final String uemail = edtnome.getText().toString();
        final String usenha = edtsenha.getText().toString();
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mAuth.signInWithEmailAndPassword(uemail, usenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    newlogin=true;
                    updateUI(mAuth.getCurrentUser());
                }else{
                    util.showSnackError(cllogin,"Usuario ou Senha Invalido");
                    bloqueiaTD(false);
                    showProgressBar(false);
                    newlogin=false;
                    updateUI(null);
                }

            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        if(mGoogleSignInClient==null)return;
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        newlogin=false;
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {

        mAuth.signOut();
        if(mGoogleSignInClient==null)return;
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        newlogin=false;
                        updateUI(null);
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
        btlogar.setClickable(!b);
        btlogar.setEnabled(!b);
        sign_in_button.setClickable(!b);
        sign_in_button.setEnabled(!b);
        btcad.setClickable(!b);
        btcad.setEnabled(!b);
        edtnome.setClickable(!b);
        edtnome.setEnabled(!b);
        edtsenha.setClickable(!b);
        edtsenha.setEnabled(!b);
        txtesquecisenha.setClickable(!b);
        txtesquecisenha.setEnabled(!b);
        blok=b;
    }

    public void onBackPressed() {
        if (!blok) {
            if (doubleBackToExitPressedOnce) {
                util.showmessage(Login.this, "Obrigado por utilizar o Cuida de Mim.");
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(Login.this, "Pressione novamente para sair.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }else{
            Toast.makeText(Login.this, "Aguarde o final do processo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideSoftKeyboard() {
        edtnome.setText(edtnome.getText().toString().toLowerCase());
        if (Login.this.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) Login.this.getSystemService(Login.this.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Login.this.getCurrentFocus().getWindowToken(), 0);
        }
    }

}