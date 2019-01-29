package com.xwy.tao_work.mytaowork.login.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.login.UpdataUserActivity;
import com.xwy.tao_work.mytaowork.utils.EditUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class EmailRegFragment extends Fragment implements View.OnClickListener{
    protected Context context;
    protected View mview;
    private Button btn_nameRegister;
    private EditText et_register_email ,et_register_namePassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        //初始化Bmob
        Bmob.initialize(context, "7c27e4e7e58fcfec555844eecb4a1dcb");
        mview = inflater.inflate(R.layout.fragment_regis_email,container,false);
        initView();
        return mview;
    }

    public void initView(){
        btn_nameRegister = mview.findViewById(R.id.btn_nameRegister);
        et_register_email = mview.findViewById(R.id.et_register_email);
        et_register_namePassword = mview.findViewById(R.id.et_register_namePassword);

        btn_nameRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_nameRegister){
            ExamInit();
        }
    }

    public void ExamInit(){
        String e_mail = et_register_email.getText().toString();
        String password = et_register_namePassword.getText().toString();

        if(TextUtils.isEmpty(e_mail)){
            Toast.makeText(context,"请输入邮箱号",Toast.LENGTH_LONG).show();
        }else if(!EditUtil.Exame_mail(e_mail)){
            Toast.makeText(context,"请输入正确格式邮箱号",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_LONG).show();
        }else{
            TestRegister(e_mail ,password);
        }
    }

    public void TestRegister(final String e_mail , final String password){
        BmobUser bu = new BmobUser();
        bu.setUsername(e_mail);
        bu.setPassword(password);
        bu.setEmail(e_mail);

        //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if(e==null){
                    EmailLogin(e_mail ,password);
                }else{
                    Log.i("smile", ""+e.getMessage());//用于后续的查询本次短信发送状态
                }
            }
        });
    }

    public void EmailLogin(String e_mail ,String password){
        BmobUser.loginByAccount(e_mail, password, new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if(user!=null){
                    Toast.makeText(context,"注册成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, UpdataUserActivity.class);
                    startActivity(intent);
                    getActivity().onBackPressed();
                }else{
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
