package com.monim.onlineattendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.monim.onlineattendance.R;
import com.monim.onlineattendance.databinding.ActivityAttendanceBinding;
import com.monim.onlineattendance.view_models.AttendanceViewModel;
import com.monim.onlineattendance.view_models.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashViewModel viewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        viewModel.getLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });


    }
}