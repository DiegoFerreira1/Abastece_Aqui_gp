package com.example.abasteceaqui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity_splash extends AppCompatActivity {

    ImageView img_logo;
    TextView text_title, text_tittle2;
    Animation top, bottom;

    private static final int splash_duration = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAGS_CHANGED, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_splash);
        img_logo = findViewById(R.id.id_img_logo);
        text_title = findViewById(R.id.id_text_tittle);
        text_tittle2 = findViewById(R.id.id_text_social);

        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);

        img_logo.setAnimation(top);
        text_title.setAnimation(bottom);
        text_tittle2.setAnimation(bottom);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity_splash.this, MainActivity_login.class);
            startActivity(intent);
            finish();
        }, splash_duration);

    }
}