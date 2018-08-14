package com.stingerzou.keyboardcoverdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import java.net.DatagramPacket;

public class MainActivity extends AppCompatActivity {

    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScrollView = findViewById(R.id.scrollView);

        final KeyboardStatusDetector keyboardStatusDetector = new KeyboardStatusDetector();

        keyboardStatusDetector
                .registerActivity(this)
                .setVisibilityListener(new KeyboardStatusDetector.KeyboardVisibilityListener() {
                    @Override
                    public void onVisibilityChanged(boolean keyboardVisible, final View view) {
                        if (keyboardVisible) {
                            mScrollView.post(new Runnable() {
                                @Override
                                public void run() {

                                    if (keyboardStatusDetector.isViewUnderKeyBoard(view, dp2px(50))) {
                                        mScrollView.smoothScrollBy(0, dp2px(50));
                                    }
                                }
                            });

                        }
                    }
                });

    }

    public int dp2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
