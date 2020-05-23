package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private Button btnQr;
    private Button btnGetLoc;
    private Button btnPrueba;
    private Button btnExists;

    private TextView txtLat;
    private TextView txtLong;

    private FileManagement fm = new FileManagement();
    private int cont;

    final String myTag = "David en android Dice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //(NewAct)
        btnQr = (Button) findViewById(R.id.btnQR);
        btnGetLoc = (Button) findViewById(R.id.btnGetLoc);
        btnPrueba = (Button) findViewById(R.id.btnPrueba);
        btnExists = (Button) findViewById(R.id.btnExists);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLong = (TextView) findViewById(R.id.txtLong);

        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQrActivity();
            }
        });

        btnExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //BASURAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa

                fm.fileExistsInExternalStorage("Prueba.txt");
            }
        });

        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currTime = Calendar.getInstance().getTime();
                Locale locale = new Locale("en", "UK");

                SimpleDateFormat timeFormat = new SimpleDateFormat("EEE,dd/MM/yyyy,HH:mm:ss", locale);
                Toast.makeText(getApplicationContext(), timeFormat.format(currTime),Toast.LENGTH_SHORT).show();


            }
        });

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        btnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker g = new GPSTracker();
                Location l = g.getLocation();
                if (l != null){

                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    Toast.makeText(getApplicationContext(), "Latitud: "+lat+" \n Longitud: "+lon,Toast.LENGTH_LONG).show();

                    txtLat.setText(Double.toString(lat));
                    txtLong.setText(Double.toString(lon));

                }else{
                    Toast.makeText(getApplicationContext(), "No se muestra ubicacion",Toast.LENGTH_SHORT).show();

                }
            }
        }
        );

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);


    }

    //(NewAct)
    public void openQrActivity(){
        Intent intent = new Intent(this, QrActivity.class);
        startActivity(intent);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
























