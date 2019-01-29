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
import com.xwy.tao_work.mytaowork.login.ShowInfoActivity;
import com.xwy.tao_work.mytaowork.utils.EditUtil;

import java.util.Random;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class PhoneRegFragment extends Fragment implements View.OnClickListener{
    protected Context context;
    protected View mview;
    private Button btn_getNumber ,btn_register;
    private EditText et_register_phone ,et_register_number ,et_register_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        //初始化Bmob
        Bmob.initialize(context, "7c27e4e7e58fcfec555844eecb4a1dcb");

        mview = inflater.inflate(R.layout.fragment_regis_phone,container,false);
        initView();
        return mview;
    }

    public void initView(){
        btn_getNumber = mview.findViewById(R.id.btn_getNumber);
        btn_register = mview.findViewById(R.id.btn_register);
        et_register_number = mview.findViewById(R.id.et_register_number);
        et_register_phone = mview.findViewById(R.id.et_register_phone);
        et_register_password = mview.findViewById(R.id.et_register_password);

        btn_getNumber.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_getNumber){
            initPhone();
        }else if(v.getId() == R.id.btn_register){
            ExamineUser();
        }
    }

    //检查用户用户验证码是否正确，密码是否填写,最后成功会跳转到完善用户信息界面
    public void ExamineUser(){
        final String phone = et_register_phone.getText().toString();
        String code = et_register_number.getText().toString();
        final String password = et_register_password.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(context,"请输入手机号",Toast.LENGTH_LONG).show();
        }else if(!EditUtil.ExamPhone(phone)){
            Toast.makeText(context,"请输入正确的手机号",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(context,"请输入验证码",Toast.LENGTH_LONG).show();
        }else {
            BmobUser.loginBySMSCode(phone, code, new LogInListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                    if (user != null) {
                        Log.i("smile", "用户登陆成功");
                        //保存手机号和密码信息
                        initsave(phone ,password);
                        Intent intent = new Intent(context,ShowInfoActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(context,"验证码错误，请稍后重新获取！",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //保存注册用户的手机号和密码
    public void initsave(String phone ,String password){

        BmobUser bu = new BmobUser();
        bu.setUsername(getRandomString(10));
        bu.setPassword(password);
        bu.setMobilePhoneNumber(phone);
        bu.setMobilePhoneNumberVerified(true);

        //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if(e==null){
                    Toast.makeText(context,"注册成功",Toast.LENGTH_LONG).show();
                }else{
                    Log.i("smile", ""+e.getMessage());//用于后续的查询本次短信发送状态
                }
            }
        });
    }

    public String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    //检查用户手机号格式是否正确，并且获取验证码
    public void initPhone(){
        String phone = et_register_phone.getText().toString();


        if(TextUtils.isEmpty(phone)){
            Toast.makeText(context,"请输入手机号",Toast.LENGTH_LONG).show();
        }else if(!EditUtil.ExamPhone(phone)){
            Toast.makeText(context,"请输入正确手机号",Toast.LENGTH_LONG).show();
        }else{
            BmobSMS.requestSMSCode(phone,"职淘平台", new QueryListener<Integer>() {

                @Override
                public void done(Integer smsId,BmobException ex) {
                    if(ex==null){//验证码发送成功
                        Log.i("smile", "短信id："+smsId);//用于后续的查询本次短信发送状态
                        Toast.makeText(context,"短信已发送", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
