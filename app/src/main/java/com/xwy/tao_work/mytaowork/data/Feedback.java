package com.xwy.tao_work.mytaowork.data;

import cn.bmob.v3.BmobObject;
/**
 * 反馈信息表
 * contact  用户联系方式
 * content  反馈内容*/

public class Feedback extends BmobObject {

    private String Contact;
    private String Content;

    public Feedback setContact(String Contact) {
        this.Contact = Contact;
        return this;
    }

    public String getContact() {
        return Contact;
    }

    public Feedback setContent(String Content) {
        this.Content = Content;
        return this;
    }

    public String getContent() {
        return Content;
    }
}
