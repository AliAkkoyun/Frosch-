package com.aliakkoyun.froggerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean quite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,GameActivity2.class));
            }
        });
        TextView highscore = findViewById(R.id.highscore);


        SharedPreferences preferences = getSharedPreferences("game", MODE_PRIVATE);

        highscore.setText("Highscore:" + preferences.getInt("highscore",0));


        quite = preferences.getBoolean("quite", false);
        ImageView audioControl = findViewById(R.id.voiceControl);

        if(quite){
            audioControl.setImageResource(R.drawable.volume_off);
        } else {
            audioControl.setImageResource(R.drawable.volume_up);
        }

        audioControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quite = !quite;
                if(quite){
                    audioControl.setImageResource(R.drawable.volume_off);
                } else {
                    audioControl.setImageResource(R.drawable.volume_up);
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("quite", quite);
                editor.apply();
            }
        });
    }
}