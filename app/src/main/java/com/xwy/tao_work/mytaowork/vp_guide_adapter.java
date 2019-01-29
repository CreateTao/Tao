package com.xwy.tao_work.mytaowork;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class vp_guide_adapter extends PagerAdapter {

    private Context context;
    private List<Integer> imageList;
    private List<ImageView> imageViewList;

    public vp_guide_adapter(Context context, List<Integer> imageList, List<ImageView> imageViewList){
        this.context = context;
        this.imageList = imageList;
        this.imageViewList = imageViewList;
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    //当前页
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    //绑定相应页面
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = imageViewList.get(position);
        imageView.setImageResource(imageList.get(position));
        //填充视图,超过的部分我就截取掉---而且图片不变形,保持原样
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //绑定视图
        container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    //销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }
}
