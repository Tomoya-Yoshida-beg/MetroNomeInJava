package beg.yoshida.metronomeinjava;

import android.media.SoundPool;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private SoundPool soundPool;
    private int soundId;
    private ScheduledThreadPoolExecutor scheduler;
    private ScheduledFuture<?> scheduledTask;
    private boolean isRunning = false;

    int bpm;
    long bpmToMs;
    private NumberPicker bpmPicker;
    private int bpmI = 120; // 現在値



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SoundPool の初期化
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();
        soundId = soundPool.load(this, R.raw.click, 1);

        NumberPicker bpmPicker;
        int bpm = 120; // 現在値


        NumberPicker picker = findViewById(R.id.pickBpm);

        picker.setMinValue(40);     // 最小値
        picker.setMaxValue(240);    // 最大値
        picker.setValue(120);       // 初期値
        picker.setWrapSelectorWheel(true);  // 端でループさせる


        // ボタンの設定
        Button btn = findViewById(R.id.startButton);
        btn.setOnClickListener(v -> toggleMetronome());
    }

    private void toggleMetronome() {
        if (isRunning) {
            stopMetronome();
        } else {
            startMetronome();
        }
    }

    private void startMetronome() {

        bpm = Integer.parseInt(
                ((EditText) findViewById(R.id.inputBpm)).getText().toString()
        );
        bpmToMs = 60000/bpm;

        scheduler = new ScheduledThreadPoolExecutor(1);
        scheduledTask = scheduler.scheduleWithFixedDelay(
                () -> soundPool.play(soundId, 1f, 1f, 1, 0, 1f),
                0,
                bpmToMs,
                TimeUnit.MILLISECONDS
        );
        isRunning = true;
    }

    private void stopMetronome() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMetronome();
        soundPool.release();
    }
}
