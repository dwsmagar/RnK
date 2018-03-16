package com.susankya.rnk;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ajay on 10/24/2015.
 */
public class CountdownTest extends Fragment {

    private TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.countdowntest_layout,null);
        tv=(TextView)v.findViewById(R.id.countdown);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tv.setText("done!");
            }
        }.start();

        return  v;
    }
}
