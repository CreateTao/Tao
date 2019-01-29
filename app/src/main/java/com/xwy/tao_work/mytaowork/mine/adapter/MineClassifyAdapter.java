package com.xwy.tao_work.mytaowork.mine.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.home.PublishWorkActivity;
import com.xwy.tao_work.mytaowork.mine.AlterUserActivity;
import com.xwy.tao_work.mytaowork.mine.SendFeedActivity;
import com.xwy.tao_work.mytaowork.mine.UpdatePassword;
import com.xwy.tao_work.mytaowork.widget.RecyclerListener;

public class MineClassifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
             implements RecyclerListener.OnItemListener{

    private int [] pic = {R.drawable.mine_put,R.drawable.mine_learn,R.drawable.mine_advise,R.drawable.mine_user,
                            R.drawable.mine_alter, R.drawable.mine_about};
    private  String [] title = {"我要发任务","学习知识","意见反馈","修改用户信息","修改密码","关于我们"};
    private Context context;
    private int type = 0;       //登录状态

    public MineClassifyAdapter(Context context,int type){
        this.context = context;
        this.type = type;
    }

    public void getType(int type){
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_mine_classify,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ItemHolder holder = (ItemHolder)viewHolder;
        holder.tv_recyMineClassify.setText(title[i]);
        holder.iv_recyMineClassify.setImageResource(pic[i]);
        holder.lin_recyMineClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 0 && i <5){
                    Toast.makeText(context,"请先登录！！",Toast.LENGTH_SHORT).show();
                }else {
                    if(listener != null){
                        listener.onItemListener(i,v);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pic.length;
    }

    private RecyclerListener.OnItemListener listener;
    public void setOnItemListener(RecyclerListener.OnItemListener listener){
        this.listener = listener;
    }
    @Override
    public void onItemListener(int position, View view) {
        if(position == 0){
            Intent intent = new Intent(context, PublishWorkActivity.class);
            context.startActivity(intent);
        } else if(position == 3){
            Intent intent = new Intent(context, AlterUserActivity.class);
            context.startActivity(intent);
        }else if(position == 4){
            Intent intent = new Intent(context, UpdatePassword.class);
            context.startActivity(intent);
        }else if(position == 2){
            //反馈
            Intent intent = new Intent(context, SendFeedActivity.class);
            context.startActivity(intent);
        }else if(position == 5){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("关于我们：");
            builder.setMessage(R.string.introduce_porject);
            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
                }
            });
            builder.show();
        } else {
            Toast.makeText(context,"你点击的是第"+position+"个",Toast.LENGTH_SHORT).show();
        }

    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView iv_recyMineClassify;
        private TextView tv_recyMineClassify;
        private LinearLayout lin_recyMineClassify;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            iv_recyMineClassify = itemView.findViewById(R.id.iv_recyMineClassify);
            tv_recyMineClassify = itemView.findViewById(R.id.tv_recyMineClassify);
            lin_recyMineClassify = itemView.findViewById(R.id.lin_recyMineClassify);
        }
    }
}
