package com.example.yournote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());

        ActivityControler.addActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityControler.removeActivity(this);
    }
}
