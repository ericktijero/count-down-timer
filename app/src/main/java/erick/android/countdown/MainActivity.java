package erick.android.countdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean isFinishCountDownTimer = false;
    private TextView tvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTimer = (TextView)findViewById(R.id.tvTimer);
        startService(new Intent(this, CountDownService.class).putExtra("minutes", "01:10"));
        Log.i(TAG, "Started service");
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(CountDownService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, CountDownService.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            String millisUntilFinished = intent.getStringExtra(Constants.COUNTDOWN);
            tvTimer.setText(millisUntilFinished);
            isFinishCountDownTimer = intent.getBooleanExtra(Constants.ISFINISHCOUNT, false);
            if (isFinishCountDownTimer) {
                Toast.makeText(this, "Timer finished", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished);
        }
    }
}

