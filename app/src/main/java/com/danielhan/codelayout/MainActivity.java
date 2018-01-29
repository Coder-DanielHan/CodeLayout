package com.danielhan.codelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private CodeLayout codelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        codelayout = (CodeLayout) findViewById(R.id.codelayout);
        // Add Lifecycle Observer
        getLifecycle().addObserver(codelayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove Lifecycle Observer
        getLifecycle().removeObserver(codelayout);
    }
}
