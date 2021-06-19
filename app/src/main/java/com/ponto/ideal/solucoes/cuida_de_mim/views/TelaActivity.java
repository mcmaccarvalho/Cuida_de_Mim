package com.ponto.ideal.solucoes.cuida_de_mim.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ponto.ideal.solucoes.cuida_de_mim.R;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Medicao;
import com.ponto.ideal.solucoes.cuida_de_mim.model.Usuario;
import com.ponto.ideal.solucoes.cuida_de_mim.util.util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TelaActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private boolean newlogin;
    public static Usuario usulog;
    public static ArrayList<Medicao> baseMed = new ArrayList();
    private AppBarConfiguration mAppBarConfiguration;
    private GoogleSignInClient mGoogleSignInClient;
    DrawerLayout drawer;
    int destinationId;
    boolean doubleBackToExitPressedOnce=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(TelaActivity.this, gso);
        mAuth = FirebaseAuth.getInstance();
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                destinationId = destination.getId();
            }

        });


                        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int mMenu = item.getItemId();

                switch (mMenu) {

                    case R.id.deslogar:
                        mAuth.signOut();
                        if (mGoogleSignInClient == null) {
                            Log.i("telamain", " goole null");
                        } else {
                            mGoogleSignInClient.signOut();
                            Log.i("telamain", " goole deslogado");
                        }
                        Intent intent2 = new Intent(TelaActivity.this, Login.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.send_email:
                        drawer.closeDrawer(GravityCompat.START);
                        if(usulog.getEmailusu()==null){
                            util.showmessage(TelaActivity.this,"Usuário não possui e-mail cadastrado.");
                        }else {
                            click_Salvar();
                        }
                        break;
                    case R.id.nav_sair:
                            util.showmessage(TelaActivity.this,"Obrigado por utilizar o Cuida de Mim.");
                            finish();
                        break;

                }
                return false;
            }

        });

        final ImageView imglogo = navigationView.getHeaderView(0).findViewById(R.id.imglogo);

        TextView txtnome = navigationView.getHeaderView(0).findViewById(R.id.txtnome);
        TextView txtemail = navigationView.getHeaderView(0).findViewById(R.id.txtemail);

        txtnome.setText(mAuth.getCurrentUser().getDisplayName());
        txtemail.setText(mAuth.getCurrentUser().getEmail());

        if (mAuth.getCurrentUser().getPhotoUrl() != null) {

            Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl().toString()).resize(200, 200).centerCrop().into(imglogo);

        } else {

            FirebaseStorage storage = FirebaseStorage.getInstance();

            final StorageReference storageReference = storage.getReferenceFromUrl("gs://mycare-82ab2.appspot.com/care_512.png");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri.toString()).resize(200, 200).centerCrop().into(imglogo);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }


        Intent intent = getIntent();
        newlogin = intent.getBooleanExtra("newlogin", false);
        initViews();

    }

    private void initViews() {



        FirebaseFirestore.getInstance().collection("/usuarios")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            usulog=task.getResult().toObject(Usuario.class);
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("/usuarios")
                .document(mAuth.getUid())
                .collection("medicoes")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        baseMed.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Medicao imed = doc.toObject(Medicao.class);
                            baseMed.add(imed);

                        }
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onBackPressed(){

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            switch (destinationId) {
                case R.id.nav_home:
                    if (doubleBackToExitPressedOnce) {
                        util.showmessage(TelaActivity.this,"Obrigado por utilizar o Cuida de Mim.");
                        finish();
                        return;
                    }
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(TelaActivity.this, "Pressione novamente para sair.", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);

            }
                getSupportFragmentManager().popBackStack();

        }

    }

    private String ObterDiretorio() {
        File root = android.os.Environment.getExternalStorageDirectory();
//        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return root.toString();
    }

    private void enviar() {

        Log.i("isonline"," : " + mAuth.getCurrentUser().getEmail() );
        String to = usulog.getEmailusu();
        String asunto = "Relatório Coleta de Dados";
        String mensaje = "Segue anexo relatório de coleta de dados\n\n"+
                usulog.getNomeusu();

        Intent email = new Intent(Intent.ACTION_SEND);

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(ObterDiretorio() + "/relatorio.txt"));

            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(email, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }else {
            uri = Uri.fromFile(new File( ObterDiretorio() + "/relatorio.txt"));
        }
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, asunto);
        email.putExtra(Intent.EXTRA_TEXT, mensaje);
        email.setDataAndType(uri, "application/vnd.android.package-archive");
        email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        email.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(email, "Selecione Destino:"));

    }

    public void click_Salvar() {
        String lstrNomeArq;
        File arq;
        byte[] dados;
        try
        {
            lstrNomeArq = "relatorio.txt";
            arq = new File(ObterDiretorio(), lstrNomeArq);
            FileOutputStream fos;

            dados = relatorio().getBytes();

            fos = new FileOutputStream(arq);
            fos.write(dados);
            fos.flush();
            fos.close();
            enviar();
            Log.i("isonline","Texto Salvo com sucesso!" );

        }
        catch (Exception e)
        {
            Log.i("isonline","erro salvar!: "  + e);
        }
    }

    private String relatorio(){
        StringBuilder str = null;
        str = new StringBuilder();

        String tit = "Relatório de Coleta de Dados.\n";
        str.append(tit);
        String nn = usulog.getNomeusu()+"\n";
        str.append(nn);
        String emit = "Gerado em: " + util.fdma(util.ld_X_long(System.currentTimeMillis()))+"\n\n";
        str.append(emit);

        String dat = " --Data-- ";
        String hor = " --Hora-- ";
        String max = String.format("%10.10s",  " -Máxima- ");
        String min = String.format("%10.10s",  " -Mínima- " );
        String gli = String.format("%10.10s",  "-Glicemia-");
        String tip = " --Tipo-- ";
        String tt =dat + "|"+hor+ "|" + max+ "|" + min + "|" +gli + "|" + tip +"|\n";
        str.append(tt);

        for(int i=0;i<baseMed.size();i++){

            String data = util.fdma(util.ld_X_long(baseMed.get(i).getTimestamp()));
            String hora = util.toDateStr(baseMed.get(i).getTimestamp(), "HH:mm");
            String tipo = baseMed.get(i).getJejum();
            if(tipo==null){
                tipo = " - ";
            }else if(tipo.equals("0")){
                tipo = "Jejum";
            }else{
                tipo = "Alimentado";
            }

            dat = String.format("%10.10s",  new Object[] {data });
            hor = String.format("%10.10s",  new Object[] { hora });
            max = String.format("%10.10s",  new Object[] { baseMed.get(i).getPresmax() });
            min = String.format("%10.10s", new Object[] { baseMed.get(i).getPresmim() });
            gli = String.format("%10.10s", new Object[] { baseMed.get(i).getGlicemia() });
            tip = String.format("%10.10s",  new Object[] {tipo });
            String dd =dat + "|"+hor+ "|" + max+ "|" + min + "|" +gli + "|" + tip +"|\n";
            str.append(dd);
        }
        String result = str.toString();
        Log.i("Stringresult" ," : \n" + result);
        return result;
    }


}