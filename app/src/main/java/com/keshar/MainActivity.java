package com.keshar;


import android.graphics.Color;
import android.os.Bundle;

import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.Toolbar;

import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    public final static String TAG = MainActivity.class.getSimpleName();
    private AppCompatSeekBar mRadiusSeekbar;
    private AppCompatSeekBar mSpacingSeekbar;
    private AppCompatSeekBar mAnimationDurationSeekbar;
    private AppCompatSeekBar mNumberDotsSeekbar;
    private AppCompatSeekBar mColorSeekbar;
    private AppCompatSeekBar mColor2Seekbar;
    private AppCompatSeekBar mScaleMultiplierSeekbar;
    private TextView mNumDotsTextView;
    private TextView mScaleMultiplierTextView;
    private TextView mRadiusTextView;
    private TextView mColorTextView;
    private TextView mColor2TextView;
    private TextView mSpacingTextView;
    private TextView mAnimationDurationTextView;
    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    private final float scaleMin = 1.2f;
    private final float scaleMax = 4.0f;
    private final float saturation = 0.75f;
    private final float value = 0.55f;
    private final int numDotsMin = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(DilatingDotsProgressBar.class.getSimpleName());
        setSupportActionBar(toolbar);

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        mDilatingDotsProgressBar.show(500);

        mNumberDotsSeekbar = (AppCompatSeekBar) findViewById(R.id.seekbar_number_dots);
        mRadiusSeekbar = (AppCompatSeekBar) findViewById(R.id.seekbar_radius);
        mSpacingSeekbar = (AppCompatSeekBar) findViewById(R.id.seekbar_spacing);
        mAnimationDurationSeekbar = (AppCompatSeekBar) findViewById(R.id.seekbar_animation_duration);
        mColorSeekbar = (AppCompatSeekBar) findViewById(R.id.seekbar_color);
        mColor2Seekbar = (AppCompatSeekBar) findViewById(R.id.seekbar_color_2);
        mScaleMultiplierSeekbar = (AppCompatSeekBar) findViewById(R.id.seekbar_scale_multiplier);

        mNumDotsTextView = (TextView) findViewById(R.id.textview_num_dots);
        mScaleMultiplierTextView = (TextView) findViewById(R.id.textview_scale_multiplier);
        mAnimationDurationTextView = (TextView) findViewById(R.id.textview_animation_duration);
        mColorTextView = (TextView) findViewById(R.id.textview_color);
        mColor2TextView = (TextView) findViewById(R.id.textview_color_2);
        mRadiusTextView = (TextView) findViewById(R.id.textview_radius);
        mSpacingTextView = (TextView) findViewById(R.id.textview_spacing);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupSeekbars();
    }

    private void setupSeekbars() {
        int dots = mDilatingDotsProgressBar.getNumberOfDots();
        dots = (dots < numDotsMin) ? numDotsMin : dots;
        mNumberDotsSeekbar.setProgress(dots - numDotsMin);
        mNumberDotsSeekbar.setOnSeekBarChangeListener(this);
        mNumDotsTextView.setText(String.valueOf(dots));

        mRadiusSeekbar.setProgress((int) mDilatingDotsProgressBar.getDotRadius());
        mRadiusSeekbar.setOnSeekBarChangeListener(this);
        mRadiusTextView.setText(String.valueOf(mDilatingDotsProgressBar.getDotRadius()));

        mSpacingSeekbar.setProgress((int) mDilatingDotsProgressBar.getHorizontalSpacing());
        mSpacingSeekbar.setOnSeekBarChangeListener(this);
        mSpacingTextView.setText(String.valueOf(mDilatingDotsProgressBar.getHorizontalSpacing()));

        mAnimationDurationSeekbar.setProgress(mDilatingDotsProgressBar.getDotGrowthSpeed());
        mAnimationDurationSeekbar.setOnSeekBarChangeListener(this);
        mAnimationDurationTextView.setText(String.valueOf(mDilatingDotsProgressBar.getDotGrowthSpeed()));

        int progress = seekbarProgressFromValue(scaleMin, scaleMax, mDilatingDotsProgressBar.getDotScaleMultiplier(),
                mScaleMultiplierSeekbar.getMax()
        );
        mScaleMultiplierSeekbar.setProgress(progress);
        mScaleMultiplierSeekbar.setOnSeekBarChangeListener(this);
        mScaleMultiplierTextView.setText(String.valueOf(mDilatingDotsProgressBar.getDotScaleMultiplier()));

        mColorSeekbar.setOnSeekBarChangeListener(this);
        mColor2Seekbar.setOnSeekBarChangeListener(this);
        mColorTextView.setText(Integer.toHexString(getHSVColor(0)));
        mColor2TextView.setText("0%");
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        if (seekBar == mRadiusSeekbar) {
            mDilatingDotsProgressBar.setDotRadius(progress);
            mRadiusTextView.setText(String.valueOf(progress));
        } else if (seekBar == mSpacingSeekbar) {
            mDilatingDotsProgressBar.setDotSpacing(progress);
            mSpacingTextView.setText(String.valueOf(progress));
        } else if (seekBar == mAnimationDurationSeekbar) {
            mDilatingDotsProgressBar.setGrowthSpeed(progress);
            mAnimationDurationTextView.setText(String.valueOf(progress));
        } else if (seekBar == mNumberDotsSeekbar) {
            mDilatingDotsProgressBar.setNumberOfDots(progress + numDotsMin);
            mNumDotsTextView.setText(String.valueOf(progress + numDotsMin));
        } else if (seekBar == mColorSeekbar) {
            mDilatingDotsProgressBar.setDotColor(getHSVColor(progress));
            mColorTextView.setText(Integer.toHexString(getHSVColor(progress)));
        } else if (seekBar == mColor2Seekbar) {
            int mainColor = getHSVColor(mColorSeekbar.getProgress());
            int secondaryColor = Color.argb(progress, Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor));
            mDilatingDotsProgressBar.setDotColor(mainColor);
            mColor2TextView.setText(String.valueOf((int) (progress / (float) seekBar.getMax() * 100)) + "%");
        } else if (seekBar == mScaleMultiplierSeekbar) {
            float scale = lerp(scaleMin, scaleMax, progress / 100f);
            mDilatingDotsProgressBar.setDotScaleMultpiplier(scale);
            mScaleMultiplierTextView.setText(String.format("%.2f", scale));
        }
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {

    }

    private int getHSVColor(int progress) {
        float[] hsvColor = {0, saturation, value};
        hsvColor[0] = 360f * progress / 100f;
        return Color.HSVToColor(hsvColor);
    }

    private float lerp(float min, float max, float progress) {
        return (min * (1.0f - progress)) + (max * progress);
    }

    private int seekbarProgressFromValue(float min, float max, float currentValue, int seekbarMax) {
        float progress = currentValue - min;
        float totalProgress = max - min;
        float progressPercent = progress / totalProgress;
        return (int) (progressPercent * seekbarMax);
    }
}