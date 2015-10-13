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
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.txusballesteros.SnakeView;

public class MainActivity extends AppCompatActivity {
    private SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snakeView = (SnakeView)findViewById(R.id.snake);
        snakeView.setMinValue(0);
        snakeView.setMaxValue(1);
        snakeView.setMaximumNumberOfValues(50);

        findViewById(R.id.addMin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.addValue(0f);
            }
        });
        findViewById(R.id.addMid1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.addValue(0.25f);
            }
        });
        findViewById(R.id.addMid2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.addValue(0.5f);
            }
        });
        findViewById(R.id.addMid3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.addValue(0.75f);
            }
        });
        findViewById(R.id.addMax).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.addValue(1f);
            }
        });
        findViewById(R.id.addClr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.clear();
            }
        });
    }
}
