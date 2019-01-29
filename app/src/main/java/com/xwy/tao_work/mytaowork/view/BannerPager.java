package com.xwy.tao_work.mytaowork.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;
import com.xwy.tao_work.mytaowork.data.advertisement;
import com.xwy.tao_work.mytaowork.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BannerPager extends RelativeLayout  {
    private final String TAG = "BannerPager";
    private Context context;
    private ViewPager vp_banner;
    private RadioGroup rg_indicator; // 声明一个单选组对象
    private ArrayList<ImageView> mViewList = new ArrayList<ImageView>(); // 声明一个图像视图队列
    private int mInterval = 2000; // 轮播的时间间隔，单位毫秒
    private List<advertisement> lists = new ArrayList<advertisement>();

    public BannerPager(Context context) {
        super(context);
    }

    public BannerPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initView();

    }

    //获取图片地址
    public void quertImg(final ImageView iv, final int position){
        lists.clear();
        Log.i(TAG, "quertImg: ");
        BmobQuery<advertisement> bmobQuery = new BmobQuery<advertisement>();
        bmobQuery.findObjects(new FindListener<advertisement>() {
            @Override
            public void done(List<advertisement> list, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "图片查询成功");
                    int n;
                    if(list != null && list.size() > 0){
                        n = list.size();
                    }else{
                        n = 0;
                    }

                    for (int i = 0; i < n; i++) {
                        lists.add( list.get(i) );
                    }

                    if(position > lists.size()){
                        iv.setImageResource(R.drawable.ic_empty);
                    }else{
                        ImageLoaderFactory.getLoader().load(iv, lists.get(position).getad_thumb(), R.drawable.ic_empty, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
                    }
                }else{
                    Log.i(TAG, e.getMessage());
                }
            }
        });
    }

    // 开始广告轮播
    public void start() {
        // 延迟若干秒后启动滚动任务
        mhandler.postDelayed(mrun, mInterval);
    }

    // 停止广告轮播
    public void stop() {
        // 移除滚动任务
        mhandler.removeCallbacks(mrun);
    }


    public void setImage(int sum){
        int dip_15 = Utils.dip2px(context, 15);
        //
        Log.i(TAG, "setImage: "+lists.size());
        for (int i = 0; i < sum; i++) {
            //Integer imageID = imageList.get(i);
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            quertImg(iv,i);

            //getBmobImage.getInstance().loadData(i,iv,lists);
            mViewList.add(iv);
        }

        vp_banner.setAdapter(new ImageAdapter());
        vp_banner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                // 高亮显示该位置的指示按钮
                setButton(position);
            }
        });
        // 根据图片队列生成指示按钮队列
        for (int i = 0; i < sum; i++) {
            RadioButton radio = new RadioButton(context);
            radio.setLayoutParams(new RadioGroup.LayoutParams(dip_15,dip_15));
            radio.setGravity(Gravity.CENTER);
            radio.setButtonDrawable(R.drawable.indicator_selector);
            rg_indicator.addView(radio);
        }
        vp_banner.setCurrentItem(0);
        setButton(0);
    }

    public void setButton(int position){
        ((RadioButton)rg_indicator.getChildAt(position)).setChecked(true);
    }



    public void initView(){
        // 根据布局文件banner_pager.xml生成视图对象
        View view = LayoutInflater.from(context).inflate(R.layout.banner_pager, null);
        vp_banner = (ViewPager)view.findViewById(R.id.vp_banner);

        rg_indicator = (RadioGroup)view.findViewById(R.id.rg_indicator);

        addView(view);
    }

    private Handler mhandler = new Handler();
    private Runnable mrun = new Runnable() {
        @Override
        public void run() {
            scrollToNext();
            mhandler.postDelayed(this,mInterval);
        }
    };

    // 滚动到下一张广告图
    public void scrollToNext() {
        // 获得下一张广告图的位置
        int index = vp_banner.getCurrentItem() + 1;
        if (index >= mViewList.size()) {
            index = 0;
        }
        // 设置翻页视图显示指定位置的页面
        vp_banner.setCurrentItem(index);
    }

    private class ImageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViewList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }
    }

}
