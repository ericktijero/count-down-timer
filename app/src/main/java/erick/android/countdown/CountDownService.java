package erick.android.countdown;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import java.util.StringTokenizer;

/**
 * Created by erick on 8/8/17.
 */

public class CountDownService extends Service {
    private final static String TAG = "CountDownService";
    public static final String COUNTDOWN_BR = "erick.android.count_down_timer.CountDownService";
    Intent bi = new Intent(COUNTDOWN_BR);
    CountDownTimer cdt = null;
    private long minutes;
    private long seconds;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            String strMinutes = intent.getStringExtra("minutes");
            if (strMinutes != null) {
                StringTokenizer tokens = new StringTokenizer(strMinutes, ":");
                minutes = Long.parseLong(tokens.nextToken());
                seconds = Long.parseLong(tokens.nextToken());
            } else {
                minutes = 10;
            }
            cdt = new CountDownTimer(minutes * 60 * 1000 + seconds * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int sec = (int) (millisUntilFinished / 1000);
                    int min = sec / 60;
                    sec = sec % 60;
                    String strMin = min + "";
                    String strSec = sec + "";
                    if (min < 10) {
                        strMin = "0" + strMin;
                    }
                    if (sec < 10) {
                        strSec = "0" + strSec;
                    }
                    bi.putExtra(Constants.COUNTDOWN, strMin + ":" + strSec);
                    sendBroadcast(bi);
                }

                @Override
                public void onFinish() {
                    Log.i(TAG, "Timer finished");
                    bi.putExtra(Constants.COUNTDOWN, "00" + ":" + "00");
                    bi.putExtra(Constants.ISFINISHCOUNT, true);
                    sendBroadcast(bi);
                }
            };
            cdt.start();
        } catch (Exception e) {

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}