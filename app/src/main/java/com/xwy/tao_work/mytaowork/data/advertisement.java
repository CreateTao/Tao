package com.xwy.tao_work.mytaowork.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class advertisement extends BmobObject {
    private String id;
    private BmobFile ad_thumb;

    public void setid(String id){
        this.id = id;
    }

    public void setad_thumb(BmobFile ad_thumb){
        this.ad_thumb = ad_thumb;
    }

    public String getid(){
        return id;
    }

    public String getad_thumb(){
        return ad_thumb.getFileUrl();
    }
}
