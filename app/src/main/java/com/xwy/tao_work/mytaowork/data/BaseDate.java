package com.xwy.tao_work.mytaowork.data;

public class BaseDate {
    private static String [] BigClassify = {"办公应用","多媒体设计","产品开发","其他"};

    private static String [][] SmallClassify = {{"Word编写","PPT设计","Excel数据处理","其他"},
                                            {"图片设计","视频处理","音频剪辑","3D渲染"},
                                            {"移动应用开发","网页开发","微信小程序开发","更多"},
                                            {"打字","App用户注册","淘宝优惠券体验","更多"}};

    private static String [] WorkSex = {"男性","女性","无"};

    public static String[] getBigClassify(){
        return BigClassify;
    }

    public static String[][] getSmallClassify(){
        return SmallClassify;
    }

    public static String [] getWorkSex(){
        return WorkSex;
    }

}
