package com.xwy.tao_work.mytaowork.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePassword extends AppCompatActivity implements View.OnClickListener{

    private EditText et_oldPassword,et_newPassword,et_sureMewPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        //初始化上方标题栏
        Toolbar tl_alterHead = (Toolbar)findViewById(R.id.tl_alterPassHead);
        tl_alterHead.setTitle("");
        setSupportActionBar(tl_alterHead);
        tl_alterHead.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
    }

    private void initView(){
        findViewById(R.id.btn_alterPassSuccess).setOnClickListener(this);
        et_oldPassword = findViewById(R.id.et_oldPassword);
        et_newPassword = findViewById(R.id.et_newPassword);
        et_sureMewPassword = findViewById(R.id.et_sureMewPassword);
    }

    @Override
    public void onClick(View v) {
        String oldPassword = et_oldPassword.getText().toString();
        String newPassword = et_newPassword.getText().toString();
        String sureNewPassword = et_sureMewPassword.getText().toString();

        if(v.getId() == R.id.btn_alterPassSuccess){
            if(!newPassword.equals(sureNewPassword)){
                Toast.makeText(UpdatePassword.this,"两次密码不正确，请重新输入",Toast.LENGTH_SHORT).show();
            }else{
                BmobUser.updateCurrentUserPassword(oldPassword, sureNewPassword, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(UpdatePassword.this,"修改成功！",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UpdatePassword.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
