package com.xwy.tao_work.mytaowork.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.Feedback;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SendFeedActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "SendFeedActivity";
    private EditText et_sendFeedContent,et_sendFeedContact;

    static String msg;      //记录之前发的反馈内容

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feed);

        //初始化上方标题栏
        Toolbar tl_sendFeedHead = (Toolbar)findViewById(R.id.tl_sendFeedHead);
        tl_sendFeedHead.setTitle("");
        setSupportActionBar(tl_sendFeedHead);
        tl_sendFeedHead.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
    }

    private void initView(){
        findViewById(R.id.btn_sendFeed).setOnClickListener(this);

        et_sendFeedContact = findViewById(R.id.et_sendFeedContact);
        et_sendFeedContent = findViewById(R.id.et_sendFeedContent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_sendFeed){
            String contact = et_sendFeedContact.getText().toString();
            String content = et_sendFeedContent.getText().toString();

            if(TextUtils.isEmpty(contact)){
                Toast.makeText(SendFeedActivity.this,"联系方式不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(content)){
                Toast.makeText(SendFeedActivity.this,"反馈内容不能为空",Toast.LENGTH_SHORT).show();
            }else if(content.equals(msg)){
                Toast.makeText(SendFeedActivity.this,"请不反馈重复的内容",Toast.LENGTH_SHORT).show();
            } else{
                sendFeed(contact,content);
                Log.i(TAG, "onClick: 可以保存");
            }
        }

    }

    private void sendFeed(final String contact, final String content){
        Feedback feedback = new Feedback();
        feedback.setContact(contact);
        feedback.setContent(content);

        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                     msg = content;
                     Toast.makeText(SendFeedActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                     et_sendFeedContact.setText("");
                     et_sendFeedContent.setText("");
                }else {
                    Toast.makeText(SendFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
        feedback.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    msg =  content;
                    Toast.makeText(SendFeedActivity.this,"反馈成功",Toast.LENGTH_SHORT).show();
                    et_sendFeedContact.setText("");
                    et_sendFeedContent.setText("");
                }else {
                    Toast.makeText(SendFeedActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
}
