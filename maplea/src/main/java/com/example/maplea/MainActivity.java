package com.example.maplea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.ImageView;

public class MainActivity extends Activity {
    //    private String ima = "http://guolin.tech/api/bing_pic";
    private String ima = "http://cn.bing.com/az/hprichbg/rb/WilsonPeakWindow_ROW12065278843_1920x1080.jpg";
    private String imaGif = "http://p1.pstatp.com/large/166200019850062839d3";
    private int i = 0;
    private ImageView imageView;
    private SlidingDrawer SlidingDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadImage();

//                startActivity(new Intent(MainActivity.this, MyFlexActivity.class));
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
    }
    /**
     * loadImage
     */
//    private void loadImage() {
//        Glide.with(MainActivity.this).load(imaGif).placeholder(R.mipmap.ic_launcher).error(R.drawable.error).into
//                (imageView);
//    }
}
