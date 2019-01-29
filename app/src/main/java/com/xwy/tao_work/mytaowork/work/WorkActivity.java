package com.xwy.tao_work.mytaowork.work;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.Work;
import com.xwy.tao_work.mytaowork.home.adpater.RecyWorkRecoAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WorkActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "WorkActivity";

    private TextView tv_workAllType,tv_workMoneyType;
    private RecyWorkRecoAdapter adapter;

    private int moneyType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        initView();
    }

    private void initView(){
        tv_workAllType = findViewById(R.id.tv_workAllType);
        tv_workMoneyType = findViewById(R.id.tv_workMoneyType);
        tv_workAllType.setOnClickListener(this);
        tv_workMoneyType.setOnClickListener(this);

        RecyclerView rv_workAll = findViewById(R.id.rv_workAll);
        LinearLayoutManager manager = new LinearLayoutManager(WorkActivity.this, LinearLayout.VERTICAL,false);
        rv_workAll.setLayoutManager(manager);
        adapter = new RecyWorkRecoAdapter(WorkActivity.this);
        adapter.setOnItemClickListener(adapter);
        rv_workAll.setAdapter(adapter);
        quertWorkType();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_workAllType){
            PopupMenu popupMenuType = new PopupMenu(WorkActivity.this, tv_workAllType);
            popupMenuType.getMenuInflater().inflate(R.menu.menu_work_alltype, popupMenuType.getMenu());
            popupMenuType.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int BigWorkType;
                    int SmallWorkType;
                    if(item.getOrder()> 9){
                        Toast.makeText(WorkActivity.this,item.getTitle(), Toast.LENGTH_SHORT).show();
                        BigWorkType = item.getOrder()/10 - 1;
                        Log.i(TAG, "onMenuItemClick: 大类别="+BigWorkType);
                        SmallWorkType = item.getOrder()%10;
                        Log.i(TAG, "onMenuItemClick: 小类别="+SmallWorkType);
                    }
                    return false;
                }
            });
            popupMenuType.show();
        }else if(v.getId() == R.id.tv_workMoneyType){
            PopupMenu popupMenuMoney = new PopupMenu(WorkActivity.this, tv_workMoneyType);
            popupMenuMoney.getMenuInflater().inflate(R.menu.menu_work_money, popupMenuMoney.getMenu());
            popupMenuMoney.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    moneyType = item.getOrder();
                    return false;
                }
            });
            popupMenuMoney.show();
        }
    }

    private void quertWorkType(){
        BmobQuery<Work> query = new BmobQuery<>();
        query.include("user");
        query.setLimit(5);
        query.findObjects(new FindListener<Work>() {
            @Override
            public void done(List<Work> list, BmobException e) {
                if(e == null){
                    Log.i(TAG, "done: list长度="+list.size());
                    Log.i(TAG, "done: 用户id="+list.get(0).getUser().getObjectId());
                    adapter.addItemAll(list);
                }
            }
        });
    }
}
