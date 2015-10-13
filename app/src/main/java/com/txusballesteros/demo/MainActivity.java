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
import android.widget.TextView;

import com.txusballesteros.SnakeView;

public class MainActivity extends AppCompatActivity {
    private float[] values = new float[] { 60, 70, 80, 90, 100,
                                          150, 150, 160, 170, 175, 180,
                                          170, 140, 130, 110, 90, 80, 60};
    private TextView text;
    private SnakeView snakeView;
    private int position = 0;
    private boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView)findViewById(R.id.text);
        snakeView = (SnakeView)findViewById(R.id.snake);
    }

    @Override
    protected void onStart() {
        super.onStart();
        stop = false;
        generateValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop = true;
    }

    private void generateValue() {
        if (position < (values.length - 1)) {
            position++;
        } else {
            position = 0;
        }
        float value = values[position];
        snakeView.addValue(value);
        text.setText(Integer.toString((int)value));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!stop) {
                    generateValue();
                }
            }
        }, 1000);
    }
}
