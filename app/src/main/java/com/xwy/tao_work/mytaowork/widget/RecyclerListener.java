package com.xwy.tao_work.mytaowork.widget;

import android.view.View;

public class RecyclerListener {

    public interface OnItemListener{
        public void onItemListener(int position, View view);
    }

    public interface OnItemLongListener{
        public void onItemLongListener(int position, View view);
    }
}
