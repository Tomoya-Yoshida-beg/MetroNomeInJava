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

    int userBpm;
    int numerator;
    int denominator;
    int repeatBar;
    int changeAmountBar;
    String upAndDown;
    int endBpm;
    int repeatTimesAll;


    long bpmToMs;

    NumberPicker pickBpm;
    NumberPicker pickBeatNumerator;
    NumberPicker pickBeatDenominator;
    NumberPicker pickRepeatBar;
    NumberPicker pickChangeAmountBpm;
    NumberPicker pickUpAndDown;
    NumberPicker pickEndBpm;
    NumberPicker pickRepeatTimesAll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickBpm             = findViewById(R.id.pickBpm);
        pickBeatNumerator   = findViewById(R.id.pickBeatNumerator);
        pickBeatDenominator = findViewById(R.id.pickBeatDenominator);
        pickRepeatBar       = findViewById(R.id.pickRepeatBar);
        pickChangeAmountBpm = findViewById(R.id.pickChangeAmountBpm);
        pickUpAndDown       = findViewById(R.id.pickUpAndDown);
        pickEndBpm          = findViewById(R.id.pickEndBpm);
        pickRepeatTimesAll  = findViewById(R.id.pickRepeatTimesAll);

        // SoundPool の初期化
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();
        soundId = soundPool.load(this, R.raw.click, 1);

        List<NumberPickerConfig> configs = Arrays.asList(
                new NumberPickerConfig.Builder(R.id.pickBpm)
                        .range(40,300,120).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickBeatNumerator)
                        .range(2,8,4).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickBeatDenominator)
                        .range(2,19,4).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickRepeatBar)
                        .range(2,40,4).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickChangeAmountBpm)
                        .range(1,40,8).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickUpAndDown)
                        .labels(new String[]{"上げる","下げる"},0).build(),
                new NumberPickerConfig.Builder(R.id.pickEndBpm)
                        .range(40,300,120).wrap(true).build(),
                new NumberPickerConfig.Builder(R.id.pickRepeatTimesAll)
                        .range(1,40,4).wrap(true).build()
        );

        NumberPickerInitializer initializer = new NumberPickerInitializer();
        initializer.initNumberPicker(this,configs);


        Button btn = findViewById(R.id.startButton);
        btn.setOnClickListener(v -> toggleMetronome());

        //リスナー設定
        NumberPicker.OnValueChangeListener common = (np, oldVal, newVal) -> {
            String[] displayValue = np.getDisplayedValues();
            if (np.getId() == R.id.pickBpm){
                userBpm = Integer.valueOf(displayValue[newVal]);
            } else if (np.getId() == R.id.pickBeatNumerator) {
                numerator = Integer.valueOf(displayValue[newVal]);
            } else if (np.getId() == R.id.pickBeatDenominator) {
                denominator = Integer.valueOf(displayValue[newVal]);
            } else if (np.getId() == R.id.pickRepeatBar) {
                repeatBar = Integer.valueOf(displayValue[newVal]);
            } else if (np.getId() == R.id.pickChangeAmountBpm) {
                changeAmountBar = Integer.valueOf(displayValue[newVal]);
            } else if (np.getId() == R.id.pickUpAndDown) {
                upAndDown = displayValue[newVal];
            } else if (np.getId() == R.id.pickEndBpm) {
                endBpm = Integer.valueOf(displayValue[newVal]);
            } else if (np.getId() == R.id.pickRepeatTimesAll) {
                repeatTimesAll = Integer.valueOf(displayValue[newVal]);
            } else {}
        };

        //リスナー登録
        for (NumberPicker np : new NumberPicker[]{
                pickBpm, pickBeatNumerator, pickBeatDenominator, pickRepeatBar,
                pickChangeAmountBpm, pickUpAndDown, pickEndBpm, pickRepeatTimesAll
        }) {
            if (np != null) np.setOnValueChangedListener(common);
        }

    }

    private void toggleMetronome() {
        if (isRunning) {
            stopMetronome();
        } else {
            startMetronome();
        }
    }

    private void startMetronome() {

        bpmToMs = 60000/userBpm;

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





    //ライフサイクル
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMetronome();
        soundPool.release();
    }
}
