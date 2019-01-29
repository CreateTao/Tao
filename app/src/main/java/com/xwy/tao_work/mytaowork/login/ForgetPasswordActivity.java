package com.xwy.tao_work.mytaowork.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.utils.EditUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 忘记密码页面
 * 通过邮箱重置密码，然后返回到登录页面进行登录
 */
public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_forget_email;
    private Button btn_forSuccess;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        //初始化Bmob
        Bmob.initialize(this, "7c27e4e7e58fcfec555844eecb4a1dcb");
        //初始化控件
        initView();
    }

    //初始化控件
    public void initView(){
        et_forget_email = findViewById(R.id.et_forget_email);
        btn_forSuccess = findViewById(R.id.btn_forSuccess);
        Toolbar tl_findpassword_head = (Toolbar)findViewById(R.id.tl_findpassword_head);
        tl_findpassword_head.setTitle("");
        setSupportActionBar(tl_findpassword_head);
        tl_findpassword_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_forSuccess.setOnClickListener(this);
    }

    //检查邮箱是否输出，没问题就发邮箱修改密码
    public void ExamEmail(){
        final String email = et_forget_email.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(ForgetPasswordActivity.this,"请输入邮箱",Toast.LENGTH_SHORT).show();
        }else if(!EditUtil.Exame_mail(email)){
            Toast.makeText(ForgetPasswordActivity.this,"请输入正确邮箱",Toast.LENGTH_SHORT).show();
        }else{
            BmobUser.resetPasswordByEmail(email, new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if(e==null){
                       Toast.makeText(ForgetPasswordActivity.this,"重置密码请求成功，请到" + email + "邮箱进行密码重置操作",
                               Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this,"失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_forSuccess){
            ExamEmail();
        }
    }


}
