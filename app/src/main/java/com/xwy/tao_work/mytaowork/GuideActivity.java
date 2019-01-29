package com.xwy.tao_work.mytaowork;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xwy.tao_work.mytaowork.view.CircleIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * 这时引导页面，是APP第一次打开时会出现的页面
 * 图片下方的一排圆点是自定义组件
 * 采用共享参数存储是否是第一次登录情况
 */
public class GuideActivity extends AppCompatActivity {
    private ViewPager view_pager;
    private TextView tv_guide;
    private CircleIndicator circle_indicator;
    private List<Integer> surprise_back ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        init();
    }

    public void init(){
        //添加图片的序号
        surprise_back = new ArrayList<Integer>();
        surprise_back.add(R.mipmap.surprise_background_1);
        surprise_back.add(R.mipmap.surprise_background_2);
        surprise_back.add(R.mipmap.surprise_background_3);
        surprise_back.add(R.mipmap.surprise_background_4);
        //绑定相应图片
        List<ImageView> imageViewList = new ArrayList<ImageView>();
        for(Integer ints : surprise_back) {
            ImageView imageView = new ImageView(this);
            imageViewList.add(imageView);
        }

        view_pager = (ViewPager)findViewById(R.id.view_pager);
        vp_guide_adapter adapter = new vp_guide_adapter(this,surprise_back,imageViewList);
        view_pager.setAdapter(adapter);

        circle_indicator = (CircleIndicator)findViewById(R.id.circle_indicator);
        //绑定页码
        circle_indicator.setViewPager(view_pager);

        //设置TextView的监听事件，和判断是否划到最后一页
        tv_guide = (TextView) findViewById(R.id.tv_guide);
        tv_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动时
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            //滑动结束
            @Override
            public void onPageSelected(int i) {
                if(i == surprise_back.size()-1) {
                    tv_guide.setVisibility(View.VISIBLE);
                }else {
                    tv_guide.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}



