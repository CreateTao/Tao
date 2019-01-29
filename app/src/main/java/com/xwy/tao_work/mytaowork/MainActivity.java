package com.xwy.tao_work.mytaowork;

import android.app.ActivityGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xwy.tao_work.mytaowork.home.HomeActivity;
import com.xwy.tao_work.mytaowork.messages.MessageActivity;
import com.xwy.tao_work.mytaowork.mine.MineActivity;
import com.xwy.tao_work.mytaowork.work.WorkActivity;

public class MainActivity extends ActivityGroup implements View.OnClickListener{

    private LinearLayout lin_container,lin_home,lin_work,lin_messages,lin_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
    }

    public void initView(){
        lin_container = (LinearLayout)findViewById(R.id.lin_container);
        lin_home = (LinearLayout)findViewById(R.id.lin_home);
        lin_work = (LinearLayout)findViewById(R.id.lin_work);
        lin_messages = (LinearLayout)findViewById(R.id.lin_messages);
        lin_mine = (LinearLayout)findViewById(R.id.lin_mine);

        lin_home.setOnClickListener(this);
        lin_work.setOnClickListener(this);
        lin_messages.setOnClickListener(this);
        lin_mine.setOnClickListener(this);

        changeContainer(lin_home);
    }

    //下标签点击事件
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.lin_home || v.getId() == R.id.lin_work || v.getId() == R.id.lin_messages || v.getId() == R.id.lin_mine){
            changeContainer(v);
        }
    }

    //对选中选中的下标签，进行跳转页面
    private void changeContainer(View v){
        //先将所有下标签设置为未选中
        lin_home.setSelected(false);
        lin_work.setSelected(false);
        lin_messages.setSelected(false);
        lin_mine.setSelected(false);
        //将传来的视图控件设置为选中状态
        v.setSelected(true);

        //跳转页面
        if(v == lin_home){
            toActivity("home", HomeActivity.class);
        }else if(v == lin_work){
            toActivity("work", WorkActivity.class);
        }else if(v == lin_messages){
            toActivity("messages", MessageActivity.class);
        }else if(v == lin_mine){
            toActivity("mine", MineActivity.class);
        }
    }

    private void toActivity(String label,Class<?> cls){
        Intent intent = new Intent(this,cls);
        lin_container.removeAllViews();
        View v = getLocalActivityManager().startActivity(label,intent).getDecorView();
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lin_container.addView(v);
    }
}
