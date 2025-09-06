package beg.yoshida.metronomeinjava;

//メソッドチェーンには前のメソッドの返り値の型が次のメソッドを使える状態にある必要がある。
//豪華なコンストラクタ
public class NumberPickerConfig {
    public final int viewId;
    public final boolean wrap;
    public final Integer min, max;
    public final Integer initial;
    public final Integer step;
    public final String[] labels;


    public NumberPickerConfig(Builder b) {
        this.viewId = b.viewId;
        this.wrap   = b.wrap;
        this.min    = b.min;
        this.max    = b.max;
        this.initial= b.init;
        this.step   = b.step;
        this.labels = b.labels;
    }

    //ネスト。setterとの差異として、必須項目の強制、不変、生成時の整合性チェックを一か所にまとめれるらしい。
    public static class Builder {
        private final int viewId;
        private Boolean wrap = Boolean.TRUE;
        private Integer min, max, init, step;
        private String[] labels;

        public Builder(int viewId) { this.viewId = viewId; }

        public Builder range (int min, int max, int initValue) {
            this.min = min;
            this.max = max;
            this.init = initValue;
            return this;
        }

        public Builder step (int step) {
            this.step = step;
            return this;
        }

        public Builder wrap(boolean wrap) {
            this.wrap = wrap;
            return this;
        }

        public Builder labels(String[] labels, int initIndex) {
            this.labels = labels;
            this.init = initIndex;
            return this;
        }

            public NumberPickerConfig build() {
                return new NumberPickerConfig(this);
        }

    }
}
