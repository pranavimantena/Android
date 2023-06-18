package edu.sjsu.android.mybrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent receiver = getIntent();
        String url = receiver.getDataString();
        TextView text1 = findViewById(R.id.text);
        text1.setText(url);
    }
}
