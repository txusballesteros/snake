/*
 * Copyright Txus Ballesteros 2015 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
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
        snakeView.setMaximumNumberOfValues(5);
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
