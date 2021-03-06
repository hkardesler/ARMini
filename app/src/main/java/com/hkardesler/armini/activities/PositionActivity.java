/*
 * *
 *  * Created by Haydar Kardesler on 5.06.2022 03:05
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.hkardesler.armini.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hkardesler.armini.R;
import com.hkardesler.armini.databinding.ActivityMainBinding;
import com.hkardesler.armini.databinding.ActivityPositionBinding;
import com.hkardesler.armini.databinding.FragmentSliderBinding;
import com.hkardesler.armini.fragments.JoystickFragment;
import com.hkardesler.armini.fragments.SliderFragment;
import com.hkardesler.armini.helpers.AppUtils;
import com.hkardesler.armini.helpers.Global;
import com.hkardesler.armini.impls.ControllerModeChangeListener;
import com.hkardesler.armini.models.ControllerMode;
import com.hkardesler.armini.models.MotorSpeed;
import com.hkardesler.armini.models.Scenario;

import java.lang.reflect.Type;

public class PositionActivity extends BaseActivity implements ControllerModeChangeListener {

    ActivityPositionBinding binding;
    String jsonPosition;
    boolean isNewPosition;
    String scenarioId;
    SliderFragment fragmentSlider;
    JoystickFragment fragmentJoystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPositionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int controllerModeInt = prefs.getInt(Global.CONTROLLER_MODE_POSITION_KEY, Global.CONTROLLER_MODE_POSITION_VALUE.getIntValue());
        if(controllerModeInt == ControllerMode.JOYSTICK.getIntValue()){
            Global.CONTROLLER_MODE_POSITION_VALUE = ControllerMode.JOYSTICK;
        }else{
            Global.CONTROLLER_MODE_POSITION_VALUE = ControllerMode.SLIDER;
        }

        jsonPosition = getIntent().getExtras().getString(Global.POSITION_KEY);
        isNewPosition = getIntent().getExtras().getBoolean(Global.NEW_POSITION_KEY);
        scenarioId = getIntent().getExtras().getString(Global.SCENARIO_ID_KEY);
        fragmentJoystick = JoystickFragment.newInstance(jsonPosition, isNewPosition, scenarioId, this);
        fragmentSlider = SliderFragment.newInstance(jsonPosition, isNewPosition, scenarioId, this);

        if (savedInstanceState == null) {

            if(Global.CONTROLLER_MODE_POSITION_VALUE == ControllerMode.JOYSTICK){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragmentJoystick)
                        .commitNow();

            }else{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragmentSlider)
                        .commitNow();
            }

        }

    }

    @Override
    protected void setListeners() {

    }


    @Override
    public void onControllerModeChanged() {
        if(Global.CONTROLLER_MODE_POSITION_VALUE == ControllerMode.JOYSTICK){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentJoystick)
                    .addToBackStack(null)
                    .commit();
        }else if(Global.CONTROLLER_MODE_POSITION_VALUE == ControllerMode.SLIDER){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentSlider)
                    .addToBackStack(null)
                    .commit();

        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}