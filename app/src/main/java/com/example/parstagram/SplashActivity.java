package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread splashScreenStarter = new Thread(() -> {
            try {
                int delay = 0;
                while (delay < 1500) {
                    Thread.sleep(150);
                    delay = delay + 100;
                }
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                finish();
            }
        });
        splashScreenStarter.start();
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}