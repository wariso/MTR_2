package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;

    private GPSTracker g = new GPSTracker();
    private FileManagement fm = new FileManagement();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Init
        scannerView = (ZXingScannerView) findViewById(R.id.zxscan);

        //Request permission
        //https://www.androidhive.info/2017/12/android-easy-runtime-permissions-with-dexter/
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            scannerView.setResultHandler(QrActivity.this);
                            scannerView.startCamera();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            //showSettingsDialog(); //REVISARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR, es para mostrar una forma de cambiar los permisos
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

    }

    @Override
    protected void onDestroy(){
        scannerView.stopCamera();
        super.onDestroy();

    };

    @Override
    public void handleResult(Result rawResult) {
        String text = "";

        Date currTime = Calendar.getInstance().getTime();
        Locale locale = new Locale("en", "UK");

        SimpleDateFormat timeFormat = new SimpleDateFormat("EEE,dd/MM/yyyy,HH:mm:ss", locale);

        text += timeFormat.format(currTime) + ",";

        Location l = g.getLocation();
        if (l != null){

            double lat = l.getLatitude();
            double lon = l.getLongitude();

            text += lat + "," + lon + ",";

        }else{

            text += "N/A,N/A,";
        }

        text += rawResult.getText() + ",";

        SharedPreferences sh;
        sh = PreferenceManager.getDefaultSharedPreferences(App.getcontext());

        int age = sh.getInt("age", 0);

        if(age != 0){
            if(age < 65){
                text += "No,";

            }else{
                text += "Si,";
            }

        }else{
            text += "N/A,";

        }

        String payment = sh.getString("payment", "N/A");

        text += payment;

        fm.appendToFile(Environment.getExternalStorageDirectory(), App.databaseFileName, text);
        Toast.makeText(QrActivity.this, "Gracias por su colaboración", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    /**
     * Requesting camera permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);

    }
}
