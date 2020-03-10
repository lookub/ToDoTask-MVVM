package com.uakgul.mvvm.todotask.view.activity;

import android.os.Bundle;

import com.uakgul.mvvm.todotask.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        navController.navigateUp();
    }

}
