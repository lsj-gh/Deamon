package com.example.maplea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main2Activity extends Activity {
    private ProgressBar progressBar;
    private String path = "/sdcard";
    private String packageFile = "/sdcard/update.zip";
    private static final String TAG = "Main2Activity";
    private TextView tv_progess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button downButton = (Button) findViewById(R.id.bt_download);
        Button back = (Button) findViewById(R.id.bt_back);
        tv_progess = (TextView) findViewById(R.id.tv_progess);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownAsynecSingle downAsynecSingle = new DownAsynecSingle();
                downAsynecSingle.execute();
            }
        });
        progressBar.setMax(100);
    }

    class DownAsynecSingle extends AsyncTask<String, Integer, String> {
        int lenthKb = 0;


        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://39.108.48.153:10003/files/update.zip");
                URLConnection connection = url.openConnection();
                int lenth = connection.getContentLength();
                lenthKb = lenth / 1000;
                Log.d(TAG, "doInBackground: lenth = " + lenth);
                InputStream is = connection.getInputStream();
                File file = new File(path, "update.zip");
                FileOutputStream os = new FileOutputStream(file);
                byte[] array = new byte[100 * 1024];
                int sum = 0;
                int index = is.read(array);
                while (index != -1) {

//                    os.write(array, 0, index);
                    sum += index;
                    publishProgress(sum / 1000, lenth);
                    index = is.read(array);
                    Log.d(TAG, "doInBackground: sun = " + sum);
                }
                os.flush();
                os.close();
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress((int) (values[0] * 100 / lenthKb));
            Log.d(TAG, "onProgressUpdate: " + (values[0] * 100 / values[1]));
            tv_progess.setText((values[0]) + "/" + lenthKb + " KB");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Main2Activity.this, "开始下载", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Main2Activity.this, "下载结束", Toast.LENGTH_SHORT).show();
            showDialog();
        }
    }

    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("是否进行升级");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: in");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(TAG, "run: befor");
                           RecoverySystem.installPackage(Main2Activity.this, new File(path, "update.zip"));
                            Log.d(TAG, "run: after");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
//                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Main2Activity.this, "已取消升级", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }
}
