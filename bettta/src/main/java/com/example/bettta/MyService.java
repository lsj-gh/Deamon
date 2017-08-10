package com.example.bettta;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 这里面可以按照MyWatchService里面的内容，检测MyWatchService是否活着，如果死亡，拉起
 * 具体实现参照MyWatchService
 */
public class MyService extends Service {
    private static final String TAG = "MyService";
    JumpUtil jumpUtil = new JumpUtil();
    byte[] buf = new byte[512];
    private Socket socket = null;

    public MyService() {
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    jumpUtil.recevierMessage(MyService.this.getApplicationContext(), msg.arg1);
                    break;
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ------服务创建了");
        checkIsStop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketLink();
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return (IBinder) myAidlInterface;
    }

    private void socketLink() {
        BufferedInputStream bis = null;
        OutputStream out = null;
        while (true) {
            try {
                Log.d(TAG, "------socket link ");
                socket = new Socket("39.108.48.153", 10001);
//            out = socket.getOutputStream();
                bis = new BufferedInputStream(socket.getInputStream());
                while (true) {
                    Log.d(TAG, "------beforReadSocket");
                    int line = bis.read(buf);
                    Log.d(TAG, "------after Read socket");
                    out = socket.getOutputStream();
                    Log.d(TAG, "onBind: len %d------" + line + " data:" + (buf[0] & 0xff));
                    handler.obtainMessage(0, buf[0] & 0xff, 0).sendToTarget();
                    out.write("OK".getBytes());
//                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                try {
                    if (socket != null) {
                        Log.d(TAG, "socketLink: socket------null");
                        socket.close();
                    }
                    Log.d(TAG, "socketLink: socket------close");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Log.e(TAG, "socketLink: " + e.getMessage());
            } finally {
                try {
                    bis.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            System.exit(0);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ------MyService");
    }

    private IMyAidlInterface myAidlInterface = new IMyAidlInterface.Stub() {

        @Override
        public void startService() throws RemoteException {
            getBaseContext().startService(new Intent(getBaseContext(), MyWatchService.class));
        }
    };

    private void checkIsStop() {
        boolean isRun = CheckService.isProessRunning(MyService.this, "com.example.bettta:mywatchservice");
        if (isRun == false) {
            try {
                Log.d(TAG, "restart------MyWatchService");
                myAidlInterface.startService();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory: MyService-----onTrimMemory");
        checkIsStop();
    }
}
