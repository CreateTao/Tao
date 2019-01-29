package com.xwy.tao_work.mytaowork.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtil {
    private static ShareUtil mUtils;
    private static SharedPreferences sps;

    public static ShareUtil getInstence(Context ctx){
        if(mUtils == null){
            mUtils = new ShareUtil();
        }
        sps = ctx.getSharedPreferences("shared",Context.MODE_PRIVATE);
        return mUtils;
    }

    public void writeShare(String key , String values){
        SharedPreferences.Editor editor = sps.edit();
        editor.putString(key,values);
        editor.commit();
    }

    public String readShare(String key , String defValues){
        return sps.getString(key, defValues);
    }
}
