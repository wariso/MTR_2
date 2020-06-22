package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.Manifest;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private Button btnQr;

    private FileManagement fm = new FileManagement();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Widgets
        btnQr = (Button) findViewById(R.id.btnQR);

        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQrActivity();
            }
        });

    }

    //https://www.youtube.com/watch?v=dvWrniwBJUw
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_3_dots, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.config:
                openConfigActivity();
                return true;

//            case R.id.closeSession:
//                onBackPressed();

            default:
                return onOptionsItemSelected(item);

        }


    }


    //(NewAct)
    public void openQrActivity(){
        SharedPreferences sh;
        sh = PreferenceManager.getDefaultSharedPreferences(App.getcontext());

        int age = sh.getInt("age", 0);
        String payment = sh.getString("payment", "Sin indicar");



        if (age == 0 || payment.equals("Sin indicar")){
            if (age == 0){
                String tempText = "";

                if (payment.equals("Sin indicar")){
                    tempText += "y su método de pago ";

                }

                Toast.makeText(this, "Por favor ingrese primero su edad " + tempText, Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Por favor ingrese primero su método de pago", Toast.LENGTH_SHORT).show();

            }

            openConfigActivity();
            return;

        }

        if (!haveNetworkConnection()) {
            Toast.makeText(this, "No hay conexión a internet. Por favor asegúrese de que tiene activado el WiFi o los datos móviles", Toast.LENGTH_LONG).show();
            return;

        }

//      // Request permission
        //https://www.androidhive.info/2017/12/android-easy-runtime-permissions-with-dexter/
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Toast.makeText(MainActivity.this, "Entro al denied", Toast.LENGTH_SHORT).show();
                            showSettingsDialog(); //REVISARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR, es para mostrar una forma de cambiar los permisos
                            return;

                        }

                        if(report.getDeniedPermissionResponses().size() >= 1){
                            Toast.makeText(App.getcontext(), "Esta aplicación requiere permiso para usar estas funciones. Por favor acepte los permisos.", Toast.LENGTH_LONG).show();
                            return;

                        }




                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            LocationManager lm = (LocationManager) App.getcontext().getSystemService(Context.LOCATION_SERVICE);

                            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if(!isGPSEnabled){
                                Toast.makeText(App.getcontext(), "Por favor, habilite el servicio de GPS", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            openQRActivity();
                        }



                        // check for permanent denial of any permission

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();


    }

    //https://steemit.com/utopian-io/@fahrulhidayat/manage-permission-on-android-with-dexter-library
    public void openQRActivity(){
        Intent intent = new Intent(this, QrActivity.class);
        startActivityForResult(intent, 100);

    }

    public void openConfigActivity(){
        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }

    //https://stackoverflow.com/questions/25685755/ask-user-to-connect-to-internet-or-quit-app-android
    //https://developer.android.com/training/basics/network-ops/connecting
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void permissionIsGranted(String permission){

    }

    //https://steemit.com/utopian-io/@fahrulhidayat/manage-permission-on-android-with-dexter-library
    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Se necesita permiso");
        builder.setMessage("Esta aplicación requiere permiso para usar estas funciones. Puedes dar permiso en la configuración de la app.");
        builder.setPositiveButton("Ir a configuración", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        Toast.makeText(this, "Entro al show", Toast.LENGTH_SHORT).show();

        builder.show();

    }

    //https://steemit.com/utopian-io/@fahrulhidayat/manage-permission-on-android-with-dexter-library
    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);

    }

//    @Override
//    public void onBackPressed() {
//        // call super.onBackPressed();  at last.
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("position", 2);
//        startActivity(intent);
//        super.onBackPressed();
//
//    }


    public void postData(){

        /*
        String fullURL = "https://docs.google.com/forms/u/0/d/e/1FAIpQLScCjawTPDQ_CQTEJU3WD9bwkhKBsDs-e2dSlEzJqOVog_xDvQ/formResponse";
        HttpRequest mReq = new HttpRequest();

        String col1 = "Buenas";
        String col2 = "Tardes";
        String data = "entry.1991820914=" + URLEncoder.encode(col1) + "&" +
                        "entry.982355227=" + URLEncoder.encode(col2);

        String response = mReq.sendPost(fullURL, data);
        Log.i(myTag, response);*/
    }


}
























