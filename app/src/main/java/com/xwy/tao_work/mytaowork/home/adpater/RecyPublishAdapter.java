package com.xwy.tao_work.mytaowork.home.adpater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xwy.tao_work.mytaowork.R;

import java.util.ArrayList;
import java.util.List;

public class RecyPublishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "PublishWorkActivity";
    private Context context;
    private List<String> list = new ArrayList<>();

    //监听EditText接口
    public interface SaveEditListener {
        void SaveEdit(int position, String string);
    }

    public RecyPublishAdapter(Context context){
        this.context = context;
    }

    public void addItem(String s){
        Log.i(TAG, "addItem: list长度="+list.size());
        list.add(list.size(),s);
        notifyItemInserted(list.size());
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_publish,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ItemHolder holder = (ItemHolder)viewHolder;
        holder.tv_recyPublishNumber.setText((i+1)+".");
        holder.et_recyPublish.setTag(i);
        holder.et_recyPublish.addTextChangedListener(new MyEditText(holder));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        private EditText et_recyPublish;
        private TextView tv_recyPublishNumber;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            et_recyPublish = itemView.findViewById(R.id.et_recyPublish);

            tv_recyPublishNumber = itemView.findViewById(R.id.tv_recyPublishNumber);
        }
    }

    public class MyEditText implements TextWatcher {

        private ItemHolder holder;
        public MyEditText(ItemHolder holder) {
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            SaveEditListener listener= (SaveEditListener) context;
            if(s!=null){
                listener.SaveEdit(Integer.parseInt(holder.et_recyPublish.getTag().toString()),
                        s.toString());
            }
        }
    }
}
