package com.xwy.tao_work.mytaowork.messages.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.xwy.tao_work.mytaowork.data.BmobIMUser;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.messages.ChatActivity;
import com.xwy.tao_work.mytaowork.widget.RecyclerListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MessageRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerListener.OnItemListener
                            ,RecyclerListener.OnItemLongListener{

    private final String TAG = "MessageAdapter";
    private Context context;
    private ArrayList<BmobIMUser> list;
    private List<BmobIMConversation> listConversation;

    public MessageRecyAdapter(Context context, ArrayList<BmobIMUser> list, List<BmobIMConversation> listConversation){
        this.context = context;
        this.list = list;
        this.listConversation = listConversation;
        Log.i(TAG, "list的长度："+list.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_message,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        final ItemHolder itemHolder = (ItemHolder)viewHolder;

        //根据List的image数组是否为空，选择相应的界面
        if(list.isEmpty()){
            itemHolder.EmpryList.setVisibility(View.VISIBLE);
            itemHolder.chatList.setVisibility(View.GONE);
        }else{
            itemHolder.EmpryList.setVisibility(View.GONE);
            itemHolder.chatList.setVisibility(View.VISIBLE);
            //会话列表的单击事件
            itemHolder.chatList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(monItemListener != null){
                        monItemListener.onItemListener(i, v);
                    }
                }
            });
            itemHolder.chatList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(monItemLongListener != null){
                        monItemLongListener.onItemLongListener(i, v);
                    }
                    return true;
                }
            });
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId", list.get(i).getUserId());
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> object, BmobException e) {
                    if (e == null) {
                        User user = object.get(0);
                        itemHolder.textTitleFrag.setText(user.getUsername());
                        itemHolder.textMessageFrag.setText(list.get(i).getLastMessage());
                        itemHolder.textTime.setText(list.get(i).getTime());
                        ImageLoaderFactory.getLoader().load(itemHolder.iv_header, user.getHead_portrait().getUrl()
                                , R.drawable.ic_empty, new ImageLoadingListener() {
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
                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(list.isEmpty()){
            return 0;
        }else{
            return list.size();
        }

    }




    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textTitleFrag,textMessageFrag,textTime;
        private LinearLayout chatList,EmpryList;
        private ImageView iv_header;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textTitleFrag = (TextView)itemView.findViewById(R.id.textTitleFrag);
            textMessageFrag = (TextView)itemView.findViewById(R.id.textMessageFrag);
            textTime = (TextView)itemView.findViewById(R.id.textTime);
            chatList = (LinearLayout)itemView.findViewById(R.id.chatList);
            iv_header = itemView.findViewById(R.id.iv_header);
            EmpryList = (LinearLayout)itemView.findViewById(R.id.EmpryList);
        }
    }

    /*列表项的单击点击事件*/
    private RecyclerListener.OnItemListener monItemListener;
    public void setOnItemListener(RecyclerListener.OnItemListener monItemListener){
        this.monItemListener = monItemListener;
    }
    @Override
    public void onItemListener(int position, View view) {
        BmobIMUser user = list.get(position);
        Log.d("MesFragAdapter", "onItemListener: position"+position);
        Log.d("MesFragAdapter", "onItemListener: title"+user.getName());

        Intent intent = new Intent(context , ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user",user);
        bundle.putSerializable("c",listConversation.get(position));
        intent.putExtras(bundle);
        //intent.put
        context.startActivity(intent);
    }

    /*列表项的长按点击事件，删除*/
    private RecyclerListener.OnItemLongListener monItemLongListener;
    public void setOnItemLongListener(RecyclerListener.OnItemLongListener monItemLongListener){
        this.monItemLongListener = monItemLongListener;
    }
    @Override
    public void onItemLongListener(int position, View view) {

        BmobIM.getInstance().deleteConversation(listConversation.get(position));
        list.remove(position);
        notifyDataSetChanged();
    }


    public void bindData(List<BmobIMConversation> listConversation, ArrayList<BmobIMUser> list){
        this.list = list;
        this.listConversation = listConversation;
        notifyDataSetChanged();
    }
}
