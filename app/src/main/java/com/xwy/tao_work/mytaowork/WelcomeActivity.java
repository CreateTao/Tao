package com.xwy.tao_work.mytaowork;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.data.advertisement;
import com.xwy.tao_work.mytaowork.home.HomeActivity;
import com.xwy.tao_work.mytaowork.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

import static java.lang.Thread.sleep;


/**
 * 这个页面是欢迎页，打开APP最先显示的页面
 * 采用属间动画放大缩小APP图标，
 * 再根据共享参数，获得是否是第一次登录，实现哪种页面跳转
 *
 */
public class WelcomeActivity extends AppCompatActivity {
    private String isFirst = "true";
    private ImageView iv_welcome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //初始化Bmob
        Bmob.initialize(this, "7c27e4e7e58fcfec555844eecb4a1dcb");

        mhandler.postDelayed(mrun , 1500);
        initanimator();
    }

    public void initanimator(){
        iv_welcome = (ImageView)findViewById(R.id.iv_welcome);

        //给logo设置S，Y轴的属间动画，稍微放大再缩小
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(iv_welcome,"scaleX",1.0f,1.1f,1.0f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(iv_welcome,"scaleY",1.0f,1.1f,1.0f);
        //将两个动画组合起来
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1500);
        animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet.start();

    }


    @Override
    protected void onResume() {
        super.onResume();
        isFirst = ShareUtil.getInstence(WelcomeActivity.this).readShare("isFirst","true");
    }

    private Handler mhandler = new Handler();
    private Runnable mrun = new Runnable() {
        @Override
        public void run() {
            if(isFirst.equals("true")){
                ShareUtil.getInstence(WelcomeActivity.this).writeShare("isFirst","false");
                Intent intent = new Intent(WelcomeActivity.this,GuideActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent1 = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();
            }
        }
    };
}
