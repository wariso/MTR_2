package com.example.myapplication;

import android.app.Application;
import android.content.Context;


//https://stackoverflow.com/questions/24760457/how-to-use-context-of-one-class-into-another-class-android/24761002
public class App extends Application {
    public static Context context;
    public static String databaseFileName = "registro.txt";

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getcontext(){
        return context;
    }

}
