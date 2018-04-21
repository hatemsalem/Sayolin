package com.zobonapp.gallery.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.alexvasilkov.android.commons.state.InstanceState;
import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.interfaces.GestureView;

/**
 * Created by Admin on 4/17/2018.
 */

public class DefaultSettingsController implements SettingsController
{
    private static final float OVERSCROLL = 32f;
    private static final long SLOW_ANIMATIONS = 1500L;
    @InstanceState
    private boolean isPanEnabled = true;
    @InstanceState
    private boolean isZoomEnabled = true;
    @InstanceState
    private boolean isRotationEnabled = true;
    @InstanceState
    private boolean isRestrictRotation = true;
    @InstanceState
    private boolean isOverscrollEnabled = false;
    @InstanceState
    private boolean isOverzoomEnabled = true;
    @InstanceState
    private boolean isFillViewport = true;
    @InstanceState
    private Settings.Fit fitMethod = Settings.Fit.INSIDE;
    @InstanceState
    private Settings.Bounds boundsType = Settings.Bounds.NORMAL;
    @InstanceState
    private int gravity = Gravity.CENTER;
    @InstanceState
    private boolean isSlow = false;
    @Override
    public void apply(GestureView view) {
        Context context = ((View) view).getContext();
        float overscroll = isOverscrollEnabled ? OVERSCROLL : 0f;
        float overzoom = isOverzoomEnabled ? Settings.OVERZOOM_FACTOR : 1f;

        view.getController().getSettings()
                .setPanEnabled(isPanEnabled)
                .setZoomEnabled(isZoomEnabled)
                .setDoubleTapEnabled(isZoomEnabled)
                .setRotationEnabled(isRotationEnabled)
                .setRestrictRotation(isRestrictRotation)
                .setOverscrollDistance(context, overscroll, overscroll)
                .setOverzoomFactor(overzoom)
                .setFillViewport(isFillViewport)
                .setFitMethod(fitMethod)
                .setBoundsType(boundsType)
                .setGravity(gravity)
                .setAnimationsDuration(isSlow ? SLOW_ANIMATIONS : Settings.ANIMATIONS_DURATION);
    }
}
