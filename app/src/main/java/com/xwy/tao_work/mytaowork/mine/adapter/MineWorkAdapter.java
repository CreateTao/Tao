package com.xwy.tao_work.mytaowork.mine.adapter;

import android.content.Context;
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
import com.xwy.tao_work.mytaowork.mine.MineActivity;
import com.xwy.tao_work.mytaowork.widget.RecyclerListener;

public class MineWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
                        implements RecyclerListener.OnItemListener {

    private int [] pic = {R.drawable.mine_putwork,R.drawable.mine_apply,R.drawable.mine_hire,R.drawable.mine_complete};
    private  String [] title = {"发布的任务","已报名","已录取","已完成"};
    private Context context;
    private int type = 0;   //登录状态

    public MineWorkAdapter(Context context,int type){
        this.context = context;
        this.type = type;
    }

    public void getType(int type){
        this.type = type;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_mine_work,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ItemHolder holder = (ItemHolder)viewHolder;
        holder.tv_recyMineWork.setText(title[i]);
        holder.iv_recyMineWork.setImageResource(pic[i]);
        holder.lin_recyMineWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 0){
                    Toast.makeText(context,"请先登录！！",Toast.LENGTH_SHORT).show();
                }else{
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
        Toast.makeText(context,"你点击的是第"+position+"个",Toast.LENGTH_SHORT).show();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView iv_recyMineWork;
        private TextView tv_recyMineWork;
        private LinearLayout lin_recyMineWork;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            iv_recyMineWork = itemView.findViewById(R.id.iv_recyMineWork);
            tv_recyMineWork = itemView.findViewById(R.id.tv_recyMineWork);
            lin_recyMineWork = itemView.findViewById(R.id.lin_recyMineWork);
        }
    }
}
