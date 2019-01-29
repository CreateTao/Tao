package com.xwy.tao_work.mytaowork.home.bean;

import com.xwy.tao_work.mytaowork.R;

import java.util.ArrayList;

public class Recycler_recommend_info {
    public int recommend_pic;
    public String recommend_title;

    public Recycler_recommend_info(int recommend_pic , String recommend_title){
        this.recommend_pic = recommend_pic;
        this.recommend_title = recommend_title;
    }

    private static int [] pic = {R.drawable.icon_relaxed_work1 ,R.drawable.icon_put_work1 ,R.drawable.icon_phone_work1 ,R.drawable.icon_study_book1};
    private static String [] title = {"轻松任务" , "发任务" ,"手机小任务" ,"学习资料"};

    public static ArrayList<Recycler_recommend_info> getRecommendInfo(){
        ArrayList<Recycler_recommend_info> list = new ArrayList<Recycler_recommend_info>();
        for(int i =0; i < pic.length ;i++){
            list.add(new Recycler_recommend_info(pic[i],title[i]));
        }
        return list;
    }

}
