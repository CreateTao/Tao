package com.xwy.tao_work.mytaowork.home.adpater;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.home.PublishWorkActivity;
import com.xwy.tao_work.mytaowork.home.RecyclerExtras;
import com.xwy.tao_work.mytaowork.home.bean.Recycler_recommend_info;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

public class RecyclerAdapter_recommend extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerExtras.OnItemClickListener{

    private Context context;
    private ArrayList<Recycler_recommend_info> list;

    public RecyclerAdapter_recommend(Context context , ArrayList<Recycler_recommend_info> list){
        this.context = context;
        this.list = list;
    }
    //创建视图持有者
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recycler_remmend,viewGroup,false);
        return new ItemHolder(v);
    }

    //绑定视图持有者
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ItemHolder itemHolder = (ItemHolder)viewHolder;
        itemHolder.iv_recommend.setImageResource(list.get(i).recommend_pic);
        itemHolder.tv_recommend.setText(list.get(i).recommend_title);
        itemHolder.lin_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v , i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 获取列表项的编号
    public long getItemId(int position) {
        return position;
    }



    //定义视图里的控件对象
    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView iv_recommend;
        private TextView tv_recommend;
        private LinearLayout lin_recommend;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            iv_recommend = itemView.findViewById(R.id.iv_recommend);
            tv_recommend = itemView.findViewById(R.id.tv_recommend);
            lin_recommend = itemView.findViewById(R.id.lin_recommend);

        }
    }

    //接口定义在RecyclerExtras中，setOnItemClickListener在外部调用
    private RecyclerExtras.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(RecyclerExtras.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    @Override
    public void onItemClick(View view, int position) {
        if(position == 1){
            User user = BmobUser.getCurrentUser(User.class);
            if(user != null){
                Intent intent = new Intent(context, PublishWorkActivity.class);
                context.startActivity(intent);
            }else {
                Toast.makeText(context,"请先登录！！",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(context,"点击的是"+list.get(position).recommend_title,Toast.LENGTH_LONG).show();
        }
    }
}
