package com.xwy.tao_work.mytaowork.home.adpater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.WorkInfoBaseActivity;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;
import com.xwy.tao_work.mytaowork.data.BaseDate;
import com.xwy.tao_work.mytaowork.data.Work;
import com.xwy.tao_work.mytaowork.home.RecyclerExtras;
import com.xwy.tao_work.mytaowork.widget.MyCircleImageView;
import com.xwy.tao_work.mytaowork.widget.RecyclerListener;

import java.util.ArrayList;
import java.util.List;

public class RecyWorkRecoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerExtras.OnItemClickListener{

    private String TAG = "HomeActivity";
    private Context context;
    private List<Work> list = new ArrayList<>();

    public RecyWorkRecoAdapter(Context context){
        this.context = context;
    }

    public void addItemAll(List<Work> arraylist){
        list.addAll(arraylist);
        Log.i(TAG, "addItemAll: list.size()="+list.size());
        notifyDataSetChanged();

    }

    public void addItem(Work work){
        list.add(list.size(),work);
        notifyItemInserted(list.size());
    }

    public int getListSize(){
        return list.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_search_work,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ItemHolder holder = (ItemHolder)viewHolder;
        holder.tv_recyHomeWorkTitle.setText(list.get(i).getWorkTitle());
        holder.tv_recyHomeWorkTime.setText("截止时间："+list.get(i).getWorkTime());
        holder.tv_recyHomeWorkNumber.setText("招聘"+list.get(i).getWorkNumber()+"人");
        holder.tv_recyHomeWorkMoney.setText(""+list.get(i).getWorkMoney()+"/人");
        ImageLoaderFactory.getLoader().load(holder.iv_recyHomeWorkHead, list.get(i).getUser().getHead_portrait().getUrl()
                , R.drawable.default_head, new ImageLoadingListener() {
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
        holder.tv_recyHomeWorkName.setText(list.get(i).getUser().getUsername());
        holder.tv_recyHomeWorkType.setText(BaseDate.getBigClassify()[list.get(i).getWorkBigType()]+"--"
                                    +BaseDate.getSmallClassify()[list.get(i).getWorkBigType()][list.get(i).getWorkSmallType()]);
        holder.lin_recyHomeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //接口定义在RecyclerExtras中，setOnItemClickListener在外部调用
    private RecyclerExtras.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(RecyclerExtras.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(context, WorkInfoBaseActivity.class);
        intent.putExtra("workInfo",(Parcelable) list.get(position));
        intent.putExtra("workPuberId",list.get(position).getUser().getObjectId());
        intent.putExtra("workPuberSex",list.get(position).getUser().getSexx());
        intent.putExtra("workPuberHead",list.get(position).getUser().getHead_portrait().getUrl());
        intent.putExtra("workPuberName",list.get(position).getUser().getUsername());
        context.startActivity(intent);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private LinearLayout lin_recyHomeWork;
        private TextView tv_recyHomeWorkTitle,tv_recyHomeWorkTime,tv_recyHomeWorkNumber;
        private TextView tv_recyHomeWorkMoney,tv_recyHomeWorkName,tv_recyHomeWorkType;
        private MyCircleImageView iv_recyHomeWorkHead;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            lin_recyHomeWork = itemView.findViewById(R.id.lin_recyHomeWork);
            tv_recyHomeWorkTitle = itemView.findViewById(R.id.tv_recyHomeWorkTitle);
            tv_recyHomeWorkTime = itemView.findViewById(R.id.tv_recyHomeWorkTime);
            tv_recyHomeWorkNumber = itemView.findViewById(R.id.tv_recyHomeWorkNumber);
            tv_recyHomeWorkMoney = itemView.findViewById(R.id.tv_recyHomeWorkMoney);
            tv_recyHomeWorkName = itemView.findViewById(R.id.tv_recyHomeWorkName);
            tv_recyHomeWorkType = itemView.findViewById(R.id.tv_recyHomeWorkType);
            iv_recyHomeWorkHead = itemView.findViewById(R.id.iv_recyHomeWorkHead);
        }
    }
}
