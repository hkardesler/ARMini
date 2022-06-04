/*
 * *
 *  * Created by Haydar Kardesler on 22.05.2022 05:49
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 22.05.2022 05:49
 *
 */

package com.hkardesler.armini.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hkardesler.armini.R;
import com.hkardesler.armini.models.User;
import com.hkardesler.armini.helpers.AppUtils;
import com.hkardesler.armini.helpers.Global;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().hide(
                    WindowInsets.Type.statusBars()
            );
        }else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        SharedPreferences prefs = AppUtils.getPrefs(this);
        int appLanguageId = prefs.getInt(Global.APP_LANGUAGE_ID_KEY, 0);
        String langCode = getResources().obtainTypedArray(R.array.language_code).getText(appLanguageId).toString();
        AppUtils.setLocale(this, langCode);


        boolean firstSession = prefs.getBoolean(Global.FIRST_SESSION_KEY, true);

        if(firstSession){
            Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(i);
            finish();
        }else{
            boolean signedIn = prefs.getBoolean(Global.SIGNED_IN_KEY, false);
            if(signedIn){

                User user = AppUtils.getUser(SplashActivity.this);
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }else{
                new Handler().postDelayed(() -> {
                    Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }, 1000);
            }
        }
    }
}