package com.xwy.tao_work.mytaowork.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.BaseActivity;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.messages.adapter.SearchUserRecyAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchUserActvity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "SearchUserActvity";
    private EditText et_Sreach_user;
    private SwipeRefreshLayout sw_refresh_Search;
    private RecyclerView rc_Search;

    private SearchUserRecyAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sreachuser);

        //初始化控件
        initView();
    }

    private void initView(){
        Button btn_Search_user = findViewById(R.id.btn_Search_user);
        btn_Search_user.setOnClickListener(this);

        et_Sreach_user = findViewById(R.id.et_Sreach_user);

        sw_refresh_Search = findViewById(R.id.sw_refresh_Search);
        sw_refresh_Search.setProgressBackgroundColorSchemeResource(R.color.btn_backgrounp);
        sw_refresh_Search.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryUser();
            }
        });

        rc_Search = findViewById(R.id.rc_Search);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayout.VERTICAL,false);
        rc_Search.setLayoutManager(manager);
        adapter = new SearchUserRecyAdapter(SearchUserActvity.this);
        adapter.setOnItemListener(adapter);
        rc_Search.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Search_user){
            sw_refresh_Search.setRefreshing(true);
            queryUser();
        }
    }

    private void queryUser(){
        String user = et_Sreach_user.getText().toString();
        if(TextUtils.isEmpty(user)){
            Toast.makeText(SearchUserActvity.this,"请输入用户名！",Toast.LENGTH_LONG).show();
            sw_refresh_Search.setRefreshing(false);
        }else{
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("username", user);
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> object, BmobException e) {
                    if(e==null){
                        sw_refresh_Search.setRefreshing(false);
                        if(object.isEmpty()){
                            adapter.setData(null);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(SearchUserActvity.this,"没有此人，请重新搜索！",Toast.LENGTH_LONG).show();
                            et_Sreach_user.setText("");
                        }else{
                            adapter.setData(object);
                            adapter.notifyDataSetChanged();
                        }


                    }else{
                        sw_refresh_Search.setRefreshing(false);
                        Toast.makeText(SearchUserActvity.this,"" + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
