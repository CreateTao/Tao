package com.xwy.tao_work.mytaowork.messages.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;
import com.xwy.tao_work.mytaowork.data.BmobIMUser;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.messages.ChatActivity;
import com.xwy.tao_work.mytaowork.messages.SearchUserActvity;
import com.xwy.tao_work.mytaowork.widget.RecyclerListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;

public class SearchUserRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements RecyclerListener.OnItemListener{

    private Context context;
    private List<User> userList = new ArrayList<>();

    public void setData(List<User> list){
        userList.clear();
        if(list != null){
            userList.addAll(list);
        }
    }

    public SearchUserRecyAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recycler_searchuser,viewGroup,false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ItemHolder holder = (ItemHolder)viewHolder;
        ImageLoaderFactory.getLoader().load(holder.iv_sreach_avagar, userList.get(i).getHead_portrait().getUrl(), R.drawable.ic_empty
                , new ImageLoadingListener() {
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
        holder.tv_search_userName.setText(userList.get(i).getUsername());
        holder.lin_search_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(monItemListener != null){
                   monItemListener.onItemListener(i,v);
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private RecyclerListener.OnItemListener monItemListener;
    public void setOnItemListener(RecyclerListener.OnItemListener monItemListener){
        this.monItemListener = monItemListener;
    }
    @Override
    public void onItemListener(int position, View view) {
        User user = userList.get(position);
        BmobIMUser imUser = new BmobIMUser(user.getObjectId(),user.getUsername());

        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(imUser, null);
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user",imUser);
        bundle.putSerializable("c",conversationEntrance);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private ImageView iv_sreach_avagar;
        private TextView tv_search_userName;
        private LinearLayout lin_search_user;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            iv_sreach_avagar = itemView.findViewById(R.id.iv_sreach_avagar);
            tv_search_userName = itemView.findViewById(R.id.tv_search_userName);
            lin_search_user = itemView.findViewById(R.id.lin_search_user);
        }
    }
}
