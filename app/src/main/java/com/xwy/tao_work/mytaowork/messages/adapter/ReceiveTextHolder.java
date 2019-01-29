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

public class ReceiveTextHolder extends RecyclerView.ViewHolder {

    private ImageView rv_Otherchat_ivHead;
    private TextView rv_chatReceive_tvTime,rv_Otherchat_tvChat;


    public ReceiveTextHolder(@NonNull Context context, ViewGroup root) {
        super(LayoutInflater.from(context).inflate(R.layout.item_receive_text,root,false));

        View v = LayoutInflater.from(context).inflate(R.layout.item_send_text,root,false);
        rv_Otherchat_ivHead = (ImageView)v.findViewById(R.id.rv_Otherchat_ivHead);
        rv_chatReceive_tvTime = v.findViewById(R.id.rv_chatReceive_tvTime);
        rv_Otherchat_tvChat = v.findViewById(R.id.rv_Otherchat_tvChat);
    }

    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        rv_chatReceive_tvTime.setText(time);
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        ImageLoaderFactory.getLoader().loadAvator(rv_Otherchat_ivHead, info != null ? info.getAvatar() : null, R.drawable.header);
        String content = message.getContent();
        rv_Otherchat_tvChat.setText(content);
    }
}
