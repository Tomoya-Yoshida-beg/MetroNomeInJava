package beg.yoshida.metronomeinjava;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import java.util.List;

//NumberPickerConfigの内容をViewに適用するコントローラ
public class NumberPickerInitializer {

    public static void initNumberPicker(Activity activity, List<NumberPickerConfig> configs){
        for (NumberPickerConfig config : configs){
            View root = activity.findViewById(android.R.id.content);
            applyOne(root,config);
        }
    }

    //NumberPickerConfigと適用先のViewを引数とする
    public static void applyOne(View root, NumberPickerConfig config){
        NumberPicker picker = (NumberPicker)root.findViewById(config.viewId);

        picker.setMaxValue(config.max);
        picker.setMinValue(config.min);
        picker.setValue(config.initial);
        //stepを設定するメソッドはNumberPickerにはないため、配列を生成してどうのこうの。後回し

        //wrapの指定があるならnull対策として丸め込み要。
        picker.setWrapSelectorWheel(true);

        if(config.labels != null){
            picker.setDisplayedValues(config.labels);
        }




        // キーボード抑止（内部 EditText へのフォーカスをブロック）
        picker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }
}
