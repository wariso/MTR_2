package com.example.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class Permissions {

    public Permissions() {
    }

    //https://www.youtube.com/watch?time_continue=2&v=7CEcevGbIZU&feature=emb_logo
    public static boolean checkPermission(Context context, String permission){
        int check = ContextCompat.checkSelfPermission(context, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


}
