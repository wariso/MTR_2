package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManagement {
    private Context context;

    public FileManagement() {
        this.context = App.getcontext();
    }


    private boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Yes, it is writable!");
            return true;
        }else{
            return false;
        }
    }


    //Metodo que indica si archivo existe en directorio raiz de almacenamiento externo
    public void fileExistsInExternalStorage(String fileName){
        File tmp = new File(Environment.getExternalStorageDirectory(), fileName);
        boolean exists = tmp.exists();

        if (exists){
            Toast.makeText(context, "Archivo existe", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Archivo no existe", Toast.LENGTH_SHORT).show();
        }
    }

    //https://www.youtube.com/watch?time_continue=2&v=7CEcevGbIZU&feature=emb_logo
    public void appendToFile(File directory, String fileName, String toAppend){
        if(isExternalStorageWritable()){
            if(Permissions.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                File textFile = new File(Environment.getExternalStorageDirectory(), fileName);

                try {
                    FileOutputStream fos = new FileOutputStream(textFile, true);

                    //https://stackoverflow.com/questions/24192586/go-to-newline-in-android-with-htmlviewer
                    toAppend += "\n";

                    fos.write(toAppend.getBytes());
                    fos.close();
                    //Toast.makeText(, "File Saved.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(context, "No se cuenta con permiso para escribir en almacenamiento externo", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "No se puede escribir en almacenamiento externo", Toast.LENGTH_SHORT).show();
        }
    }


}
