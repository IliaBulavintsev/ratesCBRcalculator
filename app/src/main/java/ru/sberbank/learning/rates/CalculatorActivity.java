package ru.sberbank.learning.rates;


import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity  extends Activity{

    public static final String  INIT_VAL = "INIT_VAL";
    public static final String  CODE = "CODE";
    public static final String  RATE = "RATE";

    private EditText editText;
    private TextView code;
    private TextView rateText;
    private double rate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        rate = getIntent().getDoubleExtra(RATE, 1d);

        editText = (EditText)findViewById(R.id.text_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.length() == 0){
                    editText.setText(Double.toString(getIntent().getDoubleExtra(INIT_VAL, 1d)));
                }
                if (rateText != null) {
                    double calculate = Double.parseDouble(editText.getText().toString()) * rate;
                    rateText.setText(Double.toString(calculate));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        editText.setText(Double.toString(getIntent().getDoubleExtra(INIT_VAL, 1d)));

        code = (TextView)findViewById(R.id.text_abbr);
        code.setText(getIntent().getStringExtra(CODE));

        rateText = (TextView) findViewById(R.id.text_result);
        rateText.setText(Double.toString(rate));

    }
}
