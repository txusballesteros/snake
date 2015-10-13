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

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SnakeView extends View {
    private final static int DEFAULT_MAXIMUN_NUMBER_OF_VALUES_FOR_DESIGNER = 5;
    private final static int DEFAULT_MAXIMUN_NUMBER_OF_VALUES_FOR_RUNTIME = 10;
    private final static int DEFAULT_STROKE_COLOR = 0xff78c257;
    private final static int DEFAULT_STROKE_WIDTH_DP = 3;
    public static final int ANIMATION_DURATION = 300;
    private int maximumNumberOfValues = DEFAULT_MAXIMUN_NUMBER_OF_VALUES_FOR_RUNTIME;
    private int strokeColor = DEFAULT_STROKE_COLOR;
    private int strokeWidth = DEFAULT_STROKE_WIDTH_DP;
    private RectF drawingArea;
    private Paint paint;
    private Queue<Float> valuesCache;
    private List<Float> previousValuesCache;
    private List<Float> currentValuesCache;
    private float animationProgress = 1.0f;
    private float scaleInX = 0f;
    private float scaleInY = 0f;
    private float minValue = 0f;
    private float maxValue = 1f;

    public void setMinValue(float minValue) {
        this.minValue = minValue;
        calculateScales();
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        calculateScales();
    }

    public void addValue(float value) {
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException("The value is out of min or max valuesCache limits.");
        }
        previousValuesCache = reverseCache();
        if (valuesCache.size() == maximumNumberOfValues) {
            valuesCache.poll();
        }
        valuesCache.add(value);
        currentValuesCache = reverseCache();
        playAnimation();
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
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(dp2px(strokeWidth));
        if (isInEditMode()) {
            initializeCacheForDesigner();
        } else {
            initializeCacheForRuntime();
        }
    }

    private void initializeCacheForDesigner() {
        maximumNumberOfValues = DEFAULT_MAXIMUN_NUMBER_OF_VALUES_FOR_DESIGNER;
        valuesCache = new ConcurrentLinkedQueue<>();
        valuesCache.add(maxValue);
        valuesCache.add(minValue);
        valuesCache.add(maxValue);
        valuesCache.add(minValue);
        valuesCache.add(maxValue);
        previousValuesCache = reverseCache();
        currentValuesCache = reverseCache();
    }

    private void initializeCacheForRuntime() {
        valuesCache = new ConcurrentLinkedQueue<>();
        for (int counter = 0; counter < maximumNumberOfValues; counter++) {
            valuesCache.add(minValue);
        }
        previousValuesCache = reverseCache();
        currentValuesCache = reverseCache();
    }

    private List<Float> reverseCache() {
        List<Float> reversedList = new ArrayList<>(valuesCache);
        Collections.reverse(reversedList);
        return reversedList;
    }

    private float dp2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                                        getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        calculateDrawingArea(width, height);
        calculateScales();
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
        if (!valuesCache.isEmpty()) {
            Path path = buildPath();
            canvas.drawPath(path, paint);
        }
    }

    private Path buildPath() {
        Path path = new Path();
        float previousX = 0f;
        float previousY = 0f;
        for (int index = 0; index < currentValuesCache.size(); index++) {
            float previousValue = previousValuesCache.get(index);
            float currentValue = currentValuesCache.get(index);
            float pathValue = previousValue + ((currentValue - previousValue) * animationProgress);
            float x = drawingArea.right - (scaleInX * index);
            float y = drawingArea.bottom - ((pathValue - minValue) * scaleInY);
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
        if (drawingArea != null) {
            scaleInX = (drawingArea.width() / (maximumNumberOfValues - 1));
            scaleInY = (drawingArea.height() / (maxValue - minValue));
        } else {
            scaleInY = 0f;
            scaleInX = 0f;
        }
    }

    private void playAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 0.0f, 1.0f);
        animator.setTarget(this);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void setProgress(@FloatRange(from=0.0, to=1.0)  float progress) {
        this.animationProgress = progress;
        invalidate();
    }
}