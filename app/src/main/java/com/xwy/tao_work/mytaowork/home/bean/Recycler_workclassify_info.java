package com.xwy.tao_work.mytaowork.home.bean;

import com.xwy.tao_work.mytaowork.R;

import java.util.ArrayList;

public class Recycler_workclassify_info {

    public String title;
    public String classify1;
    public String classify2;
    public String classify3;
    public int pic;

    public Recycler_workclassify_info(int pic , String title ,String text1 ,String text2 ,String text3){
        this.pic = pic;
        this.title = title;
        this.classify1 = text1;
        this.classify2 = text2;
        this.classify3 = text3;
    }

    private static int [] workclassify_pic = {R.drawable.icon_classify1 ,R.drawable.icon_classify2 ,R.drawable.icon_classify3 ,R.drawable.icon_classify4 };
    private static String [] workclassify_title = {"办公应用" , "多媒体设计" ,"产品开发" ,"其他" };
    private static String [] workclassify_text1 = {"Word编写" ,"图片设计" ,"移动应用开发" ,"App注册" };
    private static String [] workclassify_text2 = {"PPT设计" ,"视频处理" ,"" ,"打字" };
    private static String [] workclassify_text3 = {"Excel数据处理" ,"音频剪辑" ,"微信小程序开发" ,"淘宝优惠券体验" };

    public static ArrayList<Recycler_workclassify_info> getWorkInfo(){
        ArrayList<Recycler_workclassify_info> list = new ArrayList<Recycler_workclassify_info>();
        for(int i =0; i < workclassify_pic.length ;i++){
            list.add(new Recycler_workclassify_info(workclassify_pic[i],workclassify_title[i],workclassify_text1[i],workclassify_text2[i],workclassify_text3[i]));
        }
        return list;
    }


}
