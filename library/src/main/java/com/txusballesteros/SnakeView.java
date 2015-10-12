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
package com.txusballesteros;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SnakeView extends View {
    private final static int DEFAULT_MAX_VALUES = 10;
    private final static int DEFAULT_STROKE_COLOR = 0xff78c257;
    private final static int DEFAULT_STROKE_WIDTH_DP = 3;
    private int maximumNumberOfValues = DEFAULT_MAX_VALUES;
    private int strokeColor = DEFAULT_STROKE_COLOR;
    private int strokeWidth = DEFAULT_STROKE_WIDTH_DP;
    private RectF drawingArea;
    private Paint paint;
    private Queue<Float> values;
    private float scaleInX = 0f;
    private float scaleInY = 0f;
    private float minValue = 0f;
    private float maxValue = 1f;

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void addValue(float value) {
        if (values.size() == maximumNumberOfValues) {
            values.poll();
        }
        values.add(value);
        calculateScales();
        invalidate();
    }

    public SnakeView(Context context) {
        super(context);
        initializeView();
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    @TargetApi(21)
    public SnakeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeView();
    }

    private void initializeView() {
        values = new ConcurrentLinkedQueue<>();
        paint = new Paint();
        if (!isInEditMode()) {
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        }
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(dp2px(strokeWidth));
    }

    private float dp2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                                        getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        calculateDrawingArea(width, height);
    }

    private void calculateDrawingArea(int width, int height) {
        int left = strokeWidth + getPaddingLeft();
        int top = strokeWidth + getPaddingTop();
        int right = width - getPaddingRight() - strokeWidth;
        int bottom = height - getPaddingBottom() - strokeWidth;
        drawingArea = new RectF(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!values.isEmpty()) {
            Path path = buildPath();
            canvas.drawPath(path, paint);
        }
    }

    private Path buildPath() {
        Path path = new Path();
        Float[] currentValues = new Float[values.size()];
        values.toArray(currentValues);
        float previousX = 0;
        float previousY = 0;
        for (int index = 0; index < currentValues.length; index++) {
            int invertedIndex = ((currentValues.length - 1) - index);
            float currentValue = currentValues[invertedIndex];
            float x = drawingArea.right - (scaleInX * index);
            float y = drawingArea.bottom - ((currentValue - minValue) * scaleInY);
            if (index == 0) {
                path.moveTo(x, y);
            } else {
                path.cubicTo(x, previousY, previousX, y, x, y);
            }
            previousX = x;
            previousY = y;
        }
        return path;
    }

    private void calculateScales() {
        if (!values.isEmpty()) {
            scaleInY = 0f;
            scaleInX = (drawingArea.width() / (maximumNumberOfValues - 1));
            scaleInY = (drawingArea.height() / (maxValue - minValue));
        }
    }
}