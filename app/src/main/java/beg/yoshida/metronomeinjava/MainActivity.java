package beg.yoshida.metronomeinjava;

import android.media.SoundPool;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
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


        NumberPicker pickBpm = findViewById(R.id.pickBpm);
        NumberPicker pickBeatNumerator = findViewById(R.id.pickBeatNumerator);
        NumberPicker pickBeatDenominator = findViewById(R.id.pickBeatDenominator);
        NumberPicker pickRepeatBar = findViewById(R.id.pickRepeatBar);
        NumberPicker pickChangeAmountBpm = findViewById(R.id.pickChangeAmountBpm);
        NumberPicker pickUpAndDown = findViewById(R.id.pickUpAndDown);
        NumberPicker pickEndBpm = findViewById(R.id.pickEndBpm);
        NumberPicker pickRepeatTimesAll = findViewById(R.id.pickRepeatTimesAll);



        List<NumberPickerConfig> configs = Arrays.asList(
                new NumberPickerConfig.Builder(R.id.pickBpm)
                        .range(40,300,120).step(1).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickBeatNumerator)
                        .range(2,8,4).step(1).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickBeatDenominator)
                        .range(2,19,4).step(1).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickRepeatBar)
                        .range(2,40,4).step(1).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickChangeAmountBpm)
                        .range(1,40,8).step(1).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickUpAndDown)
                        .labels(new String[]{"上げる","下げる"},0).build(),
                new NumberPickerConfig.Builder(R.id.pickEndBpm)
                        .range(40,300,120).step(1).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickRepeatTimesAll)
                        .range(1,40,4).step(1).wrap(true).build()
        );


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
