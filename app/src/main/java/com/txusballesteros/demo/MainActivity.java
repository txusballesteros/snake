package com.txusballesteros.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.txusballesteros.SnakeView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private boolean stopPopulation = false;
    private Random random;
    private SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snakeView = (SnakeView)findViewById(R.id.snake);
        snakeView.setMinValue(0);
        snakeView.setMaxValue(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        random = new Random();
        stopPopulation = false;
        populateSnake();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPopulation = true;
    }

    private void populateSnake() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!stopPopulation) {
                    snakeView.addValue(1 * random.nextFloat());
                    populateSnake();
                }
            }
        }, 500);
    }
}
