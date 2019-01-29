package com.xwy.tao_work.mytaowork.mine.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.xwy.tao_work.mytaowork.R;

public class ChooseSexPopWindow extends PopupWindow {
    private Button MaleBtn, famaleBtn, cancelSexBtn;
    private View mMenuView;

    @SuppressLint("InflateParams")
    public ChooseSexPopWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.item_choosesex, null);
        MaleBtn = (Button) mMenuView.findViewById(R.id.MaleBtn);
        famaleBtn = (Button) mMenuView.findViewById(R.id.famaleBtn);
        cancelSexBtn = (Button) mMenuView.findViewById(R.id.cancelSexBtn);

        MaleBtn.setOnClickListener(itemsOnClick);
        famaleBtn.setOnClickListener(itemsOnClick);
        cancelSexBtn.setOnClickListener(itemsOnClick);

        //为choosePicPopWindow设置底部菜单视图
        this.setContentView(mMenuView);
        //设置宽度
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置高度
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //是否可点击
        this.setFocusable(true);
        //设置弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        //设置弹出窗口为半透明
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
