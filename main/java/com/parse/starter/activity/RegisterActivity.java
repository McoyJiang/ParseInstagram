package com.parse.starter.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.R;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editName;
    private EditText editPwd;

    private KProgressHUD kProgressHUD;
    private Button sendSmsBtn;
    private EditText editVerifyCode;
    private TextInputLayout editUernameWrapper;
    private TextInputLayout editPasswordWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        SMSSDK.registerEventHandler(ev); //注册短信回调监听

    }

    private void initViews() {

        editName = ((EditText) findViewById(R.id.editUserName));
        editPwd = ((EditText) findViewById(R.id.editPassword));
        editUernameWrapper = ((TextInputLayout) findViewById(R.id.editUserNameWrapper));
        editUernameWrapper.setHint("请输入电话号码");
        editPasswordWrapper = ((TextInputLayout) findViewById(R.id.editPasswordWrapper));
        editPasswordWrapper.setHint("请输入密码");
        editVerifyCode = ((EditText) findViewById(R.id.editVerifyCode));

        sendSmsBtn = ((Button) findViewById(R.id.sendSMS));
        sendSmsBtn.setOnClickListener(this);

        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

    }

    public void signUp() {
        String name = editName.getText().toString();
        String password = editPwd.getText().toString();

        ParseUser user = new ParseUser();

        user.setUsername(name);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                kProgressHUD.dismiss();

                if (e == null) {
                    Log.e("TAG", "done: sign up successful user is " + ParseUser.getCurrentUser().getUsername());
                    Toast.makeText(RegisterActivity.this, "注册成功",
                            Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Log.e("TAG", "done: sign up failed--" + e.getMessage());
                    Toast.makeText(RegisterActivity.this, "注册失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int time = 60;
    private Handler handler_timer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 短信验证的回调监听
     */
    private EventHandler ev = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                //提交验证码成功,如果验证成功会在data里返回数据。data数据类型为HashMap<number,code>
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Log.e("TAG", "提交验证码成功" + data.toString());
                    HashMap<String, Object> mData = (HashMap<String, Object>) data;
                    String country = (String) mData.get("country");//返回的国家编号
                    String phone = (String) mData.get("phone");//返回用户注册的手机号

                    Log.e("TAG", country + "====" + phone);

                    if (phone.equals(editName.getText().toString())) {
                        runOnUiThread(new Runnable() {//更改ui的操作要放在主线程，实际可以发送hander
                            @Override
                            public void run() {
                                signUp();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                    Log.e("TAG", "获取验证码成功");
                    kProgressHUD.dismiss();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表

                }
            } else {
                ((Throwable) data).printStackTrace();
                kProgressHUD.dismiss();
            }
        }
    };

    public void testVerifyCode(View view) {
        kProgressHUD.show();

        String number = editName.getText().toString();
        String security = editVerifyCode.getText().toString();
        if (!TextUtils.isEmpty(number) && !TextUtils.isEmpty(security)) {
            Log.e("TAG", "testVerifyCode: 正在验证...");
            //提交短信验证码
            SMSSDK.submitVerificationCode("+86", number, security);//国家号，手机号码，验证码
        } else {
            kProgressHUD.dismiss();
            Toast.makeText(this, "手机号和验证码不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    public void getVerifyCode(View view) {
        String number = editName.getText().toString();

        if (!TextUtils.isEmpty(number)) {
            Log.e("TAG", "testVerifyCode: 正在发送验证码...");
            SMSSDK.getVerificationCode("+86", number, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    Log.e("TAG", "onSendMessage: s is " + s + " s1 is " + s1);
                    return false;
                }
            });

            sendSmsBtn.setOnClickListener(null);
            handler_timer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (time <= 0) {
                        sendSmsBtn.setOnClickListener(RegisterActivity.this);
                        sendSmsBtn.setText("获取验证码");
                        return;
                    } else {
                        handler_timer.postDelayed(this, 1000);
                    }

                    time--;
                    sendSmsBtn.setText("" + time);

                }
            }, 1000);
        } else {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    public void toLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendSMS:
                getVerifyCode(v);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //要在activity销毁时反注册，否侧会造成内存泄漏问题
        SMSSDK.unregisterAllEventHandler();
    }

}
