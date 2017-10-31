package com.example.maplea;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MyFlexActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_flex);
        View mView = findViewById(R.id.view_gone);
        imageView = (ImageView) findViewById(R.id.imageView);
        Animatable animatable = ((Animatable) imageView.getDrawable());
        animatable.start();
        if (animatable.isRunning()) {
            mView.setVisibility(View.GONE);
        }else{
            mView.setVisibility(View.VISIBLE);
        }
    }
}
