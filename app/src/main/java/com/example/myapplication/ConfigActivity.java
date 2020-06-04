package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigActivity extends AppCompatActivity {

    private Button btnChange;
    private TextView txvAge;
    private Spinner spnPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txvAge = (TextView) findViewById(R.id.txvAge);

        btnChange = (Button) findViewById(R.id.btnChange);

        spnPayment = (Spinner) findViewById(R.id.spnPayment);

        //https://developer.android.com/guide/topics/ui/controls/spinner#java
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_method, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spnPayment.setAdapter(adapter);

        setInitialValues();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInputDialog();

            }
        });

        spnPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spnPayment = (Spinner) findViewById(R.id.spnPayment);
                String payment = spnPayment.getSelectedItem().toString();

                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(App.getcontext());
                SharedPreferences.Editor edt = sh.edit();

                edt.putString("payment", payment);

                if (edt.commit()){
                    Toast.makeText(App.getcontext(), "Se logró guardar su método de pago", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(App.getcontext(), "No se logró guardar su método de pago", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setInitialValues(){
        SharedPreferences sh;
        sh = PreferenceManager.getDefaultSharedPreferences(App.getcontext());

        int age = sh.getInt("age", 3);

        if(age != 0){
            txvAge.setText(Integer.toString(age));

        }else{
            txvAge.setText("Sin indicar");

        }

        String payment = sh.getString("payment", "Sin indicar");

        //https://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position?answertab=oldest#tab-top
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payment_method, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPayment.setAdapter(adapter);

        if (payment != null) {
            int spinnerPosition = adapter.getPosition(payment);
            spnPayment.setSelection(spinnerPosition);

        }
    }

    private void openInputDialog(){
        //https://developer.android.com/training/data-storage/shared-preferences#java
        //https://guides.codepath.com/android/Storing-and-Accessing-SharedPreferences
        final SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(App.getcontext());;
        final SharedPreferences.Editor edt = sh.edit();

        //https://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambio de Edad");

        // Set up the input
        final EditText input = new EditText(App.getcontext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //https://developer.android.com/reference/android/text/InputType
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer age = Integer.parseInt(input.getText().toString());

                if(age >= 0 && age <= 120){
                    edt.putInt("age", age);

                    if (edt.commit()){
                        if (age != 0){
                            txvAge.setText(age.toString());
                            Toast.makeText(App.getcontext(), "Se logró guardar su edad", Toast.LENGTH_SHORT).show();

                        }else{
                            txvAge.setText("Sin indicar");
                            Toast.makeText(App.getcontext(), "Edad ingresada no puede ser igual a cero", Toast.LENGTH_SHORT).show();

                        }

                    }else{
                        Toast.makeText(App.getcontext(), "No se logró guardar su edad", Toast.LENGTH_SHORT).show();

                    }

                }else{

                    Toast.makeText(App.getcontext(), "Edad ingresada fuera del rango permisible", Toast.LENGTH_SHORT).show();

                }
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

}
