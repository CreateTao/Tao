package com.xwy.tao_work.mytaowork.messages.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.utils.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;

    private final String TAG = "ChatAdapter";
    private Context context;
    private ArrayList<BmobIMMessage> list;


    public ChatRecyclerAdapter(Context context, ArrayList<BmobIMMessage> list) {
        this.context = context;
        this.list = list;
    }


    /*
    * 第一步：建立接收ViewHolder和发送的ViewHolder
    * 第二步：根据i来new不同的ViewHolder
    * 第三步：绑定数据*/
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_chat,viewGroup,false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final ItemHolder itemHolder = (ItemHolder) viewHolder;

        if(list.isEmpty()){
            itemHolder.lin_Other.setVisibility(View.GONE);
            itemHolder.lin_Mine.setVisibility(View.GONE);
        }else{
            itemHolder.rv_chat_tvTime.setText(DateUtil.getDateFormat().format(list.get(i).getCreateTime()));
            Log.i(TAG, "getFromId: "+list.get(i).getFromId());              //表明此会话来自于谁
            Log.i(TAG, "getBmobIMUserInfo: "+list.get(i).getBmobIMUserInfo().getName());        //会话来自于谁的姓名
            Log.i(TAG, "getBmobIMConversation: "+list.get(i).getBmobIMConversation().getConversationId());          //要给谁的
            //Log.i(TAG, "getId: "+list.get(i).getId());
            Log.i(TAG, "此会话是给谁的id: "+list.get(i).getConversationId());          //表明这个会话是要给谁
            Log.i(TAG, "当前登录的id: "+BmobUser.getCurrentUser().getObjectId());
            Log.i(TAG, "当前消息的内容: "+list.get(i).getContent());
            //查询此id的user
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId", list.get(i).getFromId());
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> object, BmobException e) {
                    if(e==null){
                        User user = object.get(0);
                        if(user.getObjectId().equals(BmobUser.getCurrentUser().getObjectId())){
                            itemHolder.lin_Other.setVisibility(View.GONE);
                            itemHolder.lin_Mine.setVisibility(View.VISIBLE);
                            itemHolder.rv_Minechat_tvChat.setText(list.get(i).getContent());
                            ImageLoaderFactory.getLoader().load(itemHolder.rv_Minechat_ivHead, user.getHead_portrait().getUrl(), R.drawable.header, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }else{
                            itemHolder.lin_Other.setVisibility(View.VISIBLE);
                            itemHolder.lin_Mine.setVisibility(View.GONE);
                            itemHolder.rv_Otherchat_tvChat.setText(list.get(i).getContent());
                            ImageLoaderFactory.getLoader().load(itemHolder.rv_Otherchat_ivHead, user.getHead_portrait().getUrl(), R.drawable.header, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }
                    }else{
                        Toast.makeText(context,"" + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    //找到传入的消息在list中的位置
    public int findPosition(BmobIMMessage message) {
        int index = this.getItemCount();
        int position = -1;
        while(index-- > 0) {
            if(message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    /**获取消息
     * @param position
     * @return
     */
    public BmobIMMessage getItem(int position){
        return this.list == null?null:(position >= this.list.size()?null:this.list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list.isEmpty()){
            return 0;
        }else{
            return list.size();
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private LinearLayout lin_Other, lin_Mine;
        private TextView rv_chat_tvTime, rv_Otherchat_tvChat, rv_Minechat_tvChat;
        private ImageView rv_Minechat_ivHead,rv_Otherchat_ivHead;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            lin_Other = itemView.findViewById(R.id.lin_Other);
            lin_Mine = itemView.findViewById(R.id.lin_Mine);
            rv_chat_tvTime = itemView.findViewById(R.id.rv_chat_tvTime);
            rv_Otherchat_tvChat = itemView.findViewById(R.id.rv_Otherchat_tvChat);
            rv_Minechat_tvChat = itemView.findViewById(R.id.rv_Minechat_tvChat);
            rv_Minechat_ivHead = itemView.findViewById(R.id.rv_Minechat_ivHead);
            rv_Otherchat_ivHead = itemView.findViewById(R.id.rv_Otherchat_ivHead);
        }
    }

    public void addMessage(BmobIMMessage message) {
        list.add(message);
        notifyDataSetChanged();
    }

    public void addMessages(List<BmobIMMessage> messages) {
        list.addAll(0, messages);
        notifyDataSetChanged();
    }
}

