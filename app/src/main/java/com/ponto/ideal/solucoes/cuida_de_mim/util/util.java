package com.ponto.ideal.solucoes.cuida_de_mim.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.StorageReference;
import com.ponto.ideal.solucoes.cuida_de_mim.helper.ConfiguracaoFirebase;
import com.ponto.ideal.solucoes.cuida_de_mim.ui.home.HomeFragment;
import com.ponto.ideal.solucoes.cuida_de_mim.ui.home.HomeViewModel;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Locale;

public class util {


    public static final String URL_WEB_SERVICE = "http://192.168.15.10:8085/";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static Snackbar snackbar;


    public static String formataTextoValor(double ndd) {


        String vld = String.valueOf(ndd);
        Double dd = Double.parseDouble(vld);
        vld = util.formatarValorDecimal(dd);

        String nvld = "";

        switch (vld.length()) {

            case 4:
                nvld = vld.substring(vld.length() - 4, vld.length() - 3) +
                        "," +
                        vld.substring(vld.length() - 2, vld.length() - 1) +
                        vld.substring(vld.length() - 1, vld.length());
                break;
            case 5:
                nvld =
                        vld.substring(vld.length() - 5, vld.length() - 4) +
                                vld.substring(vld.length() - 4, vld.length() - 3) +
                                "," +
                                vld.substring(vld.length() - 2, vld.length() - 1) +
                                vld.substring(vld.length() - 1, vld.length());
                break;
            case 6:
                nvld =
                        vld.substring(vld.length() - 6, vld.length() - 5) +
                                vld.substring(vld.length() - 5, vld.length() - 4) +
                                vld.substring(vld.length() - 4, vld.length() - 3) +
                                "," +
                                vld.substring(vld.length() - 2, vld.length() - 1) +
                                vld.substring(vld.length() - 1, vld.length());
                break;
            case 8:
                nvld =
                        vld.substring(vld.length() - 8, vld.length() - 7) +
                                "." +
                                vld.substring(vld.length() - 6, vld.length() - 5) +
                                vld.substring(vld.length() - 5, vld.length() - 4) +
                                vld.substring(vld.length() - 4, vld.length() - 3) +
                                "," +
                                vld.substring(vld.length() - 2, vld.length() - 1) +
                                vld.substring(vld.length() - 1, vld.length());
                break;
            case 9:
                nvld =
                        vld.substring(vld.length() - 9, vld.length() - 8) +
                                vld.substring(vld.length() - 8, vld.length() - 7) +
                                "." +
                                vld.substring(vld.length() - 6, vld.length() - 5) +
                                vld.substring(vld.length() - 5, vld.length() - 4) +
                                vld.substring(vld.length() - 4, vld.length() - 3) +
                                "," +
                                vld.substring(vld.length() - 2, vld.length() - 1) +
                                vld.substring(vld.length() - 1, vld.length());
                break;

        }

        return nvld;
    }

    public static String formatarValorDecimal(Double valor) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        return df.format(valor);
    }

    public static String formatarBigDecimal_to_String(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        Log.i("locale valor", ": " + valor);
        df.format(valor);
        String nval = df.format(valor);
        Log.i("locale nval", ": " + nval);
        if (Locale.getDefault().toString().equals("pt_BR")) {
            Log.i("locale", ": igual");
            nval = nval.replace(".", " ");
            nval = nval.replace(",", ".");
            nval = nval.replace(" ", ",");
        } else {
            Log.i("locale", ": diferente");
            nval = nval.replace(",", " ");
            nval = nval.replace(".", ",");
            nval = nval.replace(" ", ".");
        }

        return nval;
    }

    public static Double formatarValorDouble(Double valor) {
        double dd = valor % .0f;
        return dd;

    }

    public static void showmessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showSnackOk(View view, String message) {


        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View vsnac = snackbar.getView();
        vsnac.setBackgroundColor(Color.parseColor("#3F51B5"));
        TextView textView;
        textView = (TextView) vsnac.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(18);
        snackbar.show();


    }

    public static void salvarperfs(Context context, String tipoPrefs, String tipoId) {

        Log.i("testeprefs", ": " + tipoPrefs + " : " + tipoId);

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences("TabCamp", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(tipoPrefs, tipoId);
        editor.apply();

    }

    public static String lerperfs(Context context, String tipoPrefs) {

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences("TabCamp", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(tipoPrefs, null);
        Log.i("testeprefs", ": " + tipoPrefs + " : " + result);

        return result;

    }

    public static void showSnackError(View view, String message) {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View vsnac = snackbar.getView();
        vsnac.setBackgroundColor(Color.parseColor("#FD0303"));
        TextView textView;
        textView = (TextView) vsnac.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(18);
        snackbar.show();

    }

    public static void showSnackCampo(View view, String message) {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View vsnac = snackbar.getView();
        vsnac.setBackgroundColor(Color.parseColor("#86C964"));
        TextView textView;
        textView = (TextView) vsnac.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(18);
        snackbar.show();

    }

    public static File LoadImageFile(String nomearquivo) {
        File file = null;
//        "Gruber/"+
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nomearquivo);
        file.getAbsoluteFile();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer StringBuffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                StringBuffer.append(line);
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean SaveRelatorio( Context context, String relat) throws IOException {

        String filename = "reloatroioteste";
        String fileContents = relat;


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(relat);
        int b; while((b = in.read())>-1){ out.write(b); } out.close(); in.close();
        byte[] array = out.toByteArray();

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(array);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return  false;

        }


    }




    public static boolean SaveImageFile(String nomearquivo, Bitmap bitmap){

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] data = byteArray.toByteArray();

        FileOutputStream salvar = null;
        File arquivo = new File(Environment.DIRECTORY_PICTURES,nomearquivo);
        if(arquivo.exists()){
            arquivo.delete();
        }
        try {
            salvar = new FileOutputStream(arquivo);
            salvar.write(data);
            salvar.close();
            Log.e("salvaimg","salvo com sucesso");
            return  true;
        }catch (FileNotFoundException e){
            Log.e("salvaimg","Arquivo não encontrdo " + e.getMessage());
            return  false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("salvaimg","Erro ao salvar arquivo " + e.getMessage());
            return  false;
        }


    }

    public  static Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();

        } catch (Exception e) {
            Log.e("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String [] listafile(Context context) {


        try {
            String[] llfile = context.fileList();


            return llfile;
        } catch (Exception e) {
            Log.e("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();

        }

        return null;

    }

    public static boolean saveImage(Context context, Bitmap b, String imageName) {

        FileOutputStream foStream;
        try
        {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();

            return true;
        }
        catch (Exception e)
        {
            Log.e("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
            return false;
        }
    }

    public static String saveurl(String uuid){

        String url =
                "/fotoPerfilUsuario/"
                + uuid;

        return url;
    }

    public static String loadurl(String uuid){
        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorageReference();
        String url = storageReference+ "fotoPerfilUsuario/"
                + uuid;

        return url;
    }

    public static int geranumero(){

                    int numAleatorio = (int)(Math.random() * 100000 ) + 1;

        return numAleatorio;
    }

    public static final void vibratePhone(Context context, short vibrateMilliSeconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrateMilliSeconds);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable)
        { return ((BitmapDrawable) drawable).getBitmap(); }

        final int width = !drawable.getBounds().isEmpty() ? drawable .getBounds().width() : drawable.getIntrinsicWidth();
        final int height = !drawable.getBounds().isEmpty() ? drawable .getBounds().height() : drawable.getIntrinsicHeight();
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height, Bitmap.Config.ARGB_8888);
        Log.v("Bitmap width - Height :", width + " : " + height);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight()); drawable.draw(canvas);
        return bitmap;
    }

    public static int sorteia(){

        int s1 = util.geranumero();

        int s2 = util.geranumero();

        while (s2 == s1){
            s2 = util.geranumero();
        }

        int res = 0;

        if(s1>s2){return 1;}else{ return -1;}

    }

    public static String retStatus(int tipo){
        String s = "";
        switch (tipo){
            case 0: s="Boqueado";break;
            case 1: s="Assistir";break;
            case 2: s="Inserir/Alterar resultados";break;
            case 3: s="Iniciar/Encerrar campeonatos";break;
            case 10: s="Admnistrador";break;
            case 100: s="Visitante";break;
            default:s="Assistir";
        }
        return s;
    }

    public static String retTipoCampeonato(int tipo){
        String s = "";
        switch (tipo){
            case 1: s="Pontos Corridos 1 turno (só ida)";break;
            case 2: s="Pontos Corridos 2 turnos (ida e volta)";break;
            case 3: s="1 turno + final (só ida)";break;
            case 4: s="2 turnos + final (ida e volta)";break;
        }
        return s;
    }

    public static LocalDate ld_X_long(long ll){

        Instant it = Instant.ofEpochMilli(ll);
        ZonedDateTime zdt = it.atZone(ZoneId.systemDefault());
        LocalDate ld = zdt.toLocalDate();

        return ld;
    }

    public static String toDateStr(long milliseconds, String format)
    {
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }

    public static long long_X_st_dmahm(String st){

        long longmesbase=0;
        try {
            String str_date= st;
            SimpleDateFormat formatter ;
            Date date ;
            formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            date = (Date) formatter.parse(str_date);
            longmesbase=date.getTime();
        } catch (Exception e) {
            System.out.println("Exception :"+e);
        }
        return longmesbase;
    }

    public static String fdma(LocalDate ld){
        DateTimeFormatter fmt = DateTimeFormatter
                .ofPattern("dd-MM-yyyy")
                .withResolverStyle(ResolverStyle.STRICT);
        return ld.format(fmt);
    }

    public static void showSnackAsk(View view, String message){

        final HomeViewModel homeViewModel = HomeFragment.homeViewModel;

        final Snackbar snackbar= Snackbar.make(view, message,
                Snackbar.LENGTH_INDEFINITE);
        View vsnac = snackbar.getView();
        vsnac.setBackgroundColor(Color.parseColor("#000000"));
        TextView textView;
        textView = (TextView) vsnac.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(18);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.setApdint(5);
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }

}

