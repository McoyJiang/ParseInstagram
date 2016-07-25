package com.parse.starter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName;
    private EditText editPwd;
    private TextView toRegister;
    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toRegister = ((TextView) findViewById(R.id.toRegister));
        toRegister.setOnClickListener(this);
        editName = ((EditText) findViewById(R.id.editUserName));
        editPwd = ((EditText) findViewById(R.id.editPassword));

        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

    }

    public void logIn(View view) {
        kProgressHUD.show();
        String name = editName.getText().toString();
        String password = editPwd.getText().toString();

        ParseUser user = new ParseUser();

        user.setUsername(name);
        user.setPassword(password);

        ParseUser.logInInBackground(name, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                kProgressHUD.dismiss();

                if (e == null && user != null) {
                    String username = user.getUsername();
                    Log.e("TAG", "登录成功");
                    Toast.makeText(LoginActivity.this, username + " 登录成功",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
