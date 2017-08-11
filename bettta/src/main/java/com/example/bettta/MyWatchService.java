package com.example.bettta;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyWatchService extends Service {
    private static final String TAG = "MyWatchService";
    private static Thread mThread;

    public MyWatchService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkIsStop();
        Log.d(TAG, "onCreate: ------服务创建了");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return (IBinder) myAidlInterface;
    }

    //把服务MyService拉起来
    private IMyAidlInterface myAidlInterface = new IMyAidlInterface.Stub() {

        @Override
        public void startService() throws RemoteException {
            getBaseContext().startService(new Intent(getBaseContext(), MyService.class));
        }
    };

    //检测MyService是否存活，5秒检测一次
    private void checkIsStop() {
        if (mThread == null || !mThread.isAlive())
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        boolean isRun = CheckService.isProessRunning(MyWatchService.this, "com.example" +
                                ".bettta:myservice");
                        if (isRun == false) {
                        //  如果MyService已死，就把它拉起来
                            try {
                                Log.d(TAG, "checkIsStop------restart");
                                myAidlInterface.startService();

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "checkIsStop------check------" + isRun);
                    }
                }
            });
        mThread.start();
//        boolean isRun = CheckService.isProessRunning(MyWatchService.this, "com.example.bettta:myservice");
//        if (isRun == false) {
//            try {
//                Log.d(TAG, "--------------restart----MyService");
//                myAidlInterface.startService();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory: -----MyWatchService-----onTrimMemory");
        checkIsStop();
    }
}
