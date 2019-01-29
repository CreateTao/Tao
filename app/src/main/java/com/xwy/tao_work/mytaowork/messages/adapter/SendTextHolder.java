package com.xwy.tao_work.mytaowork.messages.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

public class SendTextHolder extends RecyclerView.ViewHolder {


    private ImageView rv_Minechat_ivHead;
    private TextView rv_chatSend_tvTime,rv_Minechat_tvChat;

    public SendTextHolder(@NonNull Context context, ViewGroup root) {
        super(LayoutInflater.from(context).inflate(R.layout.item_send_text,root,false));

        View v = LayoutInflater.from(context).inflate(R.layout.item_send_text,root,false);
        rv_Minechat_ivHead = (ImageView)v.findViewById(R.id.rv_Minechat_ivHead);
        rv_chatSend_tvTime = v.findViewById(R.id.rv_chatSend_tvTime);
        rv_Minechat_tvChat = v.findViewById(R.id.rv_Minechat_tvChat);
    }

    public void bindData(Object o){

        final BmobIMMessage message = (BmobIMMessage)o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        ImageLoaderFactory.getLoader().loadAvator(rv_Minechat_ivHead,info != null ? info.getAvatar() : null, R.drawable.header);
        String time = dateFormat.format(message.getCreateTime());
        String content = message.getContent();
        rv_Minechat_tvChat.setText(content);
        rv_chatSend_tvTime.setText(time);
    }
}
