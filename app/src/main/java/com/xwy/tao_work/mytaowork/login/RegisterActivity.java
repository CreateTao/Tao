package com.xwy.tao_work.mytaowork.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.login.adapter.MyAdapter;

/**
 * 注册页面
 */

public class RegisterActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    /*
    private Button btn_getNumber ,btn_register;
    private EditText et_register_phone ,et_register_number ,et_register_password;*/
    private String [] tab_title = {"手机号注册","邮箱注册"};
    private ViewPager vp_content_resgister;
    private TabLayout tab_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化上方标题栏
        Toolbar tl_register_head = (Toolbar)findViewById(R.id.tl_register_head);
        tl_register_head.setTitle("");
        setSupportActionBar(tl_register_head);
        tl_register_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tab_text = (TabLayout)findViewById(R.id.tab_typetext);
        tab_text.addTab(tab_text.newTab().setText(tab_title[0]));
        tab_text.addTab(tab_text.newTab().setText(tab_title[1]));
        tab_text.setOnTabSelectedListener(this);


        vp_content_resgister = (ViewPager)findViewById(R.id.vp_content_resgister);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager() ,tab_title);
        vp_content_resgister.setAdapter(adapter);
        vp_content_resgister.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                tab_text.getTabAt(position).select();
            }
        });
        /*
        */

        //初始化控件
        //initView();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vp_content_resgister.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



/*
    //初始化控件
    public void initView(){
        btn_getNumber = findViewById(R.id.btn_getNumber);
        btn_register = findViewById(R.id.btn_register);
        et_register_number = findViewById(R.id.et_register_number);
        et_register_phone = findViewById(R.id.et_register_phone);
        et_register_password = findViewById(R.id.et_register_password);

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
            Toast.makeText(RegisterActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
        }else if(isPhoneValid(phone)){
            Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"请输入验证码",Toast.LENGTH_LONG).show();
        }else {
            BmobUser.loginBySMSCode(phone, code, new LogInListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                    if (user != null) {
                        Log.i("smile", "用户登陆成功");
                        //保存手机号和密码信息
                        initsave(phone ,password);
                        Intent intent = new Intent(RegisterActivity.this,ShowInfoActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this,"验证码错误，请稍后重新获取！",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //保存注册用户的手机号和密码
    public void initsave(String phone ,String password){

        BmobUser bu = new BmobUser();
        bu.setPassword(password);
        bu.setMobilePhoneNumber(phone);
        bu.setMobilePhoneNumberVerified(true);

        //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if(e==null){
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                }else{
                    Log.i("smile", ""+e.getMessage());//用于后续的查询本次短信发送状态
                }
            }
        });
    }

    //检查用户手机号格式是否正确，并且获取验证码
    public void initPhone(){
        String phone = et_register_phone.getText().toString();


        if(TextUtils.isEmpty(phone)){
            Toast.makeText(RegisterActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
        }else if(!isPhoneValid(phone)){
            Toast.makeText(RegisterActivity.this,"请输入正确手机号",Toast.LENGTH_LONG).show();
        }else{
            BmobSMS.requestSMSCode(phone,"职淘平台", new QueryListener<Integer>() {

                @Override
                public void done(Integer smsId,BmobException ex) {
                    if(ex==null){//验证码发送成功
                        Log.i("smile", "短信id："+smsId);//用于后续的查询本次短信发送状态
                        Toast.makeText(RegisterActivity.this,"短信已发送", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this,""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //判断输入的手机号长度是否正确
    private boolean isPhoneValid(String userName) {
        if(userName.length()>6 && userName.length()<11){
            return true;
        }
        return false;
    }*/
}
