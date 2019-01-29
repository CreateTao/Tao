package com.xwy.tao_work.mytaowork.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.BaseActivity;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.utils.EditUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener{


    private TextInputLayout input_phone, input_password;
    private EditText tv_phone ,tv_password;
    private Button btn_login ,btn_register ,btn_forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化Bmob
        Bmob.initialize(this, "7c27e4e7e58fcfec555844eecb4a1dcb");

        //初始化上方标题栏
        Toolbar tl_login_head = (Toolbar)findViewById(R.id.tl_login_head);
        tl_login_head.setTitle("");
        setSupportActionBar(tl_login_head);
        tl_login_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //初始化各种控件
        initView();

    }

    //初始化控件
    public void initView(){
        input_password = findViewById(R.id.input_password);
        input_phone = findViewById(R.id.input_phone);
        tv_phone = findViewById(R.id.et_phone);
        tv_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_forget_password = findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(this);

        //判断EditText的内容是否发生变化
        tv_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_phone.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //判断EditText的内容是否发生变化
        tv_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_password.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //判断   登录  时出现的各种错误情况，如：输入不合法等
    public void initEdits(){
        input_phone.setError(null);
        input_password.setError(null);

        String userName = tv_phone.getText().toString();
        String password = tv_password.getText().toString();

        if(TextUtils.isEmpty(userName)){
            input_phone.setError("请输入手机号");
        }else if(!EditUtil.ExamPhone(userName) && !EditUtil.Exame_mail(userName)){
            input_phone.setError("请输入正确手机号或邮箱");
        }else if(TextUtils.isEmpty(password)){
            input_password.setError("请输入密码");
        }else{
            BmobUser.loginByAccount(userName, password, new LogInListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                    if(user!=null){
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("com.xwy.tao_work_mytaowork.FORCE_ONLINE");
                        sendBroadcast(intent);
                        finish();
                        //登录后需要获取用户的相关信息用于展示
                        Log.i("smile","用户登陆成功");
                    }else{
                        Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //三个按钮的点击效果
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login){
            initEdits();
        }else if(v.getId() == R.id.btn_register){
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_forget_password){
            Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(intent);
            //不需要finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int Line = getLLine();
        if(Line == 1){
            finish();
        }
    }
}
