package com.example.bettta;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class JumpUtil {
    private String[] pkNames = new String[]{"com.autonavi.amapauto",
            "com.semisky.bydbluetooth.bluetooth", "com.semisky.jlmultimedia",
            "com.semisky.jlradio", "net.easyconn", "com.semisky.settingnl", "com.semisky.jlinstructions", "com" +
            ".semisky.semiskylaunchernl"};
    private String[] classNames = new String[]{
            "com.autonavi.auto.remote.fill.UsbFillActivity",
            "com.semisky.bydbluetooth.bluetooth.MainActivity",
            "com.semisky.jlmultimedia.MultimediaActivity",
            "com.semisky.jlradio.RadioActivity",
            "net.easyconn.WelcomeActivity",
            "com.semisky.settingnl.SettingActivity", "com.semisky.jlinstructions.MainActivity", "com.semisky" +
            ".semiskylaunchernl.Launcher"};
    Instrumentation instrumentation = new Instrumentation();
    private static final String TAG = "JumpUtil";

    public void recevierMessage(Context context, int msg) {
        if (msg < 20) {
            keyClick(context, msg);
        } else if (msg >= 20 && msg < 50) {
            startApp(context, msg);
        } else if (msg >= 20 && msg < 70) {
            onMenuClick(msg);
        }
    }

    private void startApp(Context context, int msg) {
        int cor = msg - 20;
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkNames[cor], classNames[cor]));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void keyClick(Context context, int msg) {
        switch (msg) {
            case 2:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "run:-- " + e.getMessage());
                        }

                    }
                }).start();
                break;
            case 1:

                Intent intent = new Intent();
                intent.setComponent(new ComponentName(pkNames[7], classNames[7]));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                     instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_HOME);
//                    }
//                }).start();
                break;
            default:
                break;
        }


    }

    private void onMenuClick(int msg) {
        Class<?> a = null;
        Method setBackLightEnable = null;
        Object instance = null;
        try {
            a = Class.forName("android.os.ProtocolManager");
            setBackLightEnable = a.getMethod("setBackLightEnable", boolean.class);
            Method getInstance = a.getDeclaredMethod("getInstance");
            instance = getInstance.invoke(a);


        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        switch (msg) {
            case 51:

                try {
                    setBackLightEnable.invoke(instance, false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                break;
            case 52:
                //Object[] paramsss = new Object[1]{false};
                try {
                    setBackLightEnable.invoke(instance, true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
