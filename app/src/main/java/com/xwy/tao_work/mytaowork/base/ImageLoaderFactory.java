package com.xwy.tao_work.mytaowork.base;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ImageLoaderFactory {

    private static volatile ILoader sInstance;

    private ImageLoaderFactory() {}

    public static ILoader getLoader() {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    sInstance = new UniversalImageLoader();
                }
            }
        }
        return sInstance;
    }
}
