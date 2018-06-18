package com.easterbrook.flood_it.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.easterbrook.flood_it.R;

public class AboutTheAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_the_app);
        setTitle("About the App");
    }
}
