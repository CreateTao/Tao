package com.xwy.tao_work.mytaowork.home.adpater;

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
import com.xwy.tao_work.mytaowork.home.RecyclerExtras;
import com.xwy.tao_work.mytaowork.home.bean.Recycler_workclassify_info;

import java.util.ArrayList;

public class RecyvlerAdpater_workclassify extends RecyclerView.Adapter implements RecyclerExtras.OnItemClickListener{
    private Context context;
    private ArrayList<Recycler_workclassify_info> list;

    public RecyvlerAdpater_workclassify(Context context , ArrayList<Recycler_workclassify_info> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recycler_workclassify ,viewGroup ,false);
        return new ItemView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ItemView itemView = (ItemView) viewHolder;
        itemView.tv_workclassify.setText(list.get(i).title);
        itemView.tv_classify1.setText(list.get(i).classify1);
        itemView.tv_classify2.setText(list.get(i).classify2);
        itemView.tv_classify3.setText(list.get(i).classify3);
        itemView.iv_workclassify.setImageResource(list.get(i).pic);
        itemView.lin_workclassify.setOnClickListener(new View.OnClickListener() {
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

    private class ItemView extends RecyclerView.ViewHolder{
        private TextView tv_workclassify ,tv_classify1 ,tv_classify2 ,tv_classify3;
        private ImageView iv_workclassify;
        private LinearLayout lin_workclassify;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            tv_workclassify = itemView.findViewById(R.id.tv_workclassify);
            tv_classify1 = itemView.findViewById(R.id.tv_classify1);
            tv_classify2 = itemView.findViewById(R.id.tv_classify2);
            tv_classify3 = itemView.findViewById(R.id.tv_classify3);
            iv_workclassify = itemView.findViewById(R.id.iv_workclassify);
            lin_workclassify = itemView.findViewById(R.id.lin_workclassify);

        }
    }

    //接口定义在RecyclerExtras中，setOnItemClickListener在外部调用
    private RecyclerExtras.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(RecyclerExtras.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(context,"点击的是"+list.get(position).title,Toast.LENGTH_LONG).show();
    }
}
