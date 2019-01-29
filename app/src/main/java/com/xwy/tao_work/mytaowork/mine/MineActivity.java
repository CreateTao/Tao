package com.xwy.tao_work.mytaowork.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.BaseActivity;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.login.LoginActivity;
import com.xwy.tao_work.mytaowork.mine.adapter.MineClassifyAdapter;
import com.xwy.tao_work.mytaowork.mine.adapter.MineWorkAdapter;
import com.xwy.tao_work.mytaowork.widget.MyCircleImageView;

import cn.bmob.v3.BmobUser;

public class MineActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_mineName;
    private MyCircleImageView iv_mineHead;

    private MineWorkAdapter adapter;
    private MineClassifyAdapter adapter1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        //初始化视图
        initView();
    }

    private void initView(){

        tv_mineName = findViewById(R.id.tv_mineName);
        tv_mineName.setOnClickListener(this);
        iv_mineHead = (MyCircleImageView) findViewById(R.id.iv_mineHead);

        RecyclerView rv_mineWork = findViewById(R.id.rv_mineWork);
        GridLayoutManager manager = new GridLayoutManager(MineActivity.this,4);
        rv_mineWork.setLayoutManager(manager);
        adapter = new MineWorkAdapter(MineActivity.this,getLLine());
        adapter.setOnItemListener(adapter);
        rv_mineWork.setAdapter(adapter);

        RecyclerView rv_mineClssify = findViewById(R.id.rv_mineClssify);
        LinearLayoutManager manager1 = new LinearLayoutManager(MineActivity.this,LinearLayout.VERTICAL,false);
        rv_mineClssify.setLayoutManager(manager1);
        rv_mineClssify.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter1 = new MineClassifyAdapter(MineActivity.this,getLLine());
        adapter1.setOnItemListener(adapter1);
        rv_mineClssify.setAdapter(adapter1);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_mineName){
            Intent intent = new Intent(MineActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = BmobUser.getCurrentUser(User.class);
        if(user != null){
            tv_mineName.setText(user.getUsername());
            ImageLoaderFactory.getLoader().load(iv_mineHead, user.getHead_portrait().getUrl(), R.drawable.default_head, new ImageLoadingListener() {
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

            adapter.getType(1);
            adapter.notifyDataSetChanged();
            adapter1.getType(1);
            adapter1.notifyDataSetChanged();
        }
    }
}
