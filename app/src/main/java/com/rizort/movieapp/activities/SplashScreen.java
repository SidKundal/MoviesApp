package com.rizort.movieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent openSearchScreenIntent = new Intent(this, SearchScreen.class);
        startActivity(openSearchScreenIntent);
        finish();
    }
}
