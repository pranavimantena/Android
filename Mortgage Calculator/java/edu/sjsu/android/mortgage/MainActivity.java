package edu.sjsu.android.mortgage;

import androidx.appcompat.app.AppCompatActivity ;

import android.os.Bundle ;
import android.view.View ;
import android.widget.CheckBox ;
import android.widget.EditText ;
import android.widget.RadioButton ;
import android.widget.SeekBar ;
import android.widget.SeekBar.OnSeekBarChangeListener ;
import android.widget.TextView;
import android.widget.Toast ;
import static java.lang.Math.pow ;

public class MainActivity extends AppCompatActivity
{
    private EditText edittext ;
    private TextView textview ;
    private TextView textview1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main) ;
        edittext = (EditText) findViewById(R.id.amountEditText) ;
        textview = (TextView) findViewById(R.id.payment) ;
        textview1 = (TextView) findViewById(R.id.textView9);
        SeekBar bar = (SeekBar) findViewById(R.id.interestBar) ;
        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                textview1.setText(String.valueOf("Interest Rate is" + + seekBar.getProgress() / 10.0 + "%"));
            }
        }) ;
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.calculate:
                if (edittext.getText().length() == 0)
                {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_LONG).show() ;
                    return;
                }

                double amount = Double.parseDouble(edittext.getText().toString()) ;
                double rate = ((SeekBar) findViewById(R.id.interestBar)).getProgress() / 1200.0 ;
                boolean tax = ((CheckBox) findViewById(R.id.taxCheckBox)).isChecked() ;
                double payment = 0;
                if (((RadioButton) findViewById(R.id.radioButton15)).isChecked())
                {
                    if (rate == 0) {payment = amount / (15 * 12) ;}
                    else {payment = amount * rate / (1 - pow(1 + rate, 15 * -12)) ;}
                }
                else if (((RadioButton) findViewById(R.id.radioButton20)).isChecked())
                {
                    if (rate == 0) {payment = amount / (20 * 12) ;}
                    else {payment = amount * rate / (1 - pow(1 + rate, 20 * -12)) ;}
                }
                else
                {
                    if (rate == 0) {payment = amount / (30 * 12) ;}
                    else {payment = amount * rate / (1 - pow(1 + rate, 30 * -12)) ;}
                }

                if (tax) {payment += amount * 0.001 ;}
                textview.setText(String.valueOf("Monthly Payment: $" + ((int) (payment * 100)) / 100.0)) ;

                break;
        }
    }
}
