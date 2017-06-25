package com.oklab.gitjourney.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.asynctasks.AuthenticationAsyncTask;

/**
 * Created by olgakuklina on 2017-01-08.
 */

public class AuthenticationActivity extends AppCompatActivity {
    private EditText passwordText;
    private EditText loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        passwordText = (EditText) findViewById(R.id.user_password);
        loginText = (EditText) findViewById(R.id.user_id);
        Button button = (Button) findViewById(R.id.buttonSignIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginText.getText().toString();
                String password = passwordText.getText().toString();
                if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.empty_login_password), Toast.LENGTH_SHORT).show();
                } else {
                    new AuthenticationAsyncTask(AuthenticationActivity.this).execute(login, password);
                }
            }
        });
    }
}
