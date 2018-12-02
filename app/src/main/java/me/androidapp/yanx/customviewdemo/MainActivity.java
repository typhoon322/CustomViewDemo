package me.androidapp.yanx.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CustomButtonComponent buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons = findViewById(R.id.buttons);
        buttons.setOnButtonClickListener(new CustomButtonComponent.OnButtonClick() {
            @Override
            public void onClick(int index) {
                Log.w(TAG, "wocao , click " + (index == 0 ? "left" : "right"));
            }
        });
    }
}
