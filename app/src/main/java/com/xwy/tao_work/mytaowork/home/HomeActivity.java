package com.xwy.tao_work.mytaowork.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xwy.tao_work.mytaowork.base.BaseActivity;
import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.data.Work;
import com.xwy.tao_work.mytaowork.home.adpater.RecyWorkRecoAdapter;
import com.xwy.tao_work.mytaowork.home.adpater.RecyclerAdapter_recommend;
import com.xwy.tao_work.mytaowork.home.adpater.RecyvlerAdpater_workclassify;
import com.xwy.tao_work.mytaowork.home.bean.Recycler_recommend_info;
import com.xwy.tao_work.mytaowork.home.bean.Recycler_workclassify_info;
import com.xwy.tao_work.mytaowork.login.LoginActivity;
import com.xwy.tao_work.mytaowork.login.RegisterActivity;
import com.xwy.tao_work.mytaowork.utils.Utils;
import com.xwy.tao_work.mytaowork.view.BannerPager;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private String TAG = "HomeActivity";
    private TextView tv_homeLogin,tv_homeRegsister,tv_homeLineUserNname;
    private LinearLayout lin_homeOnLineTL,lin_homeOffLineTL;
    private ImageView iv_homeLineHeader;
    private SwipeRefreshLayout sw_homeRefresh;
    private RecyWorkRecoAdapter adapterWork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化Bmob
        Bmob.initialize(this, "7c27e4e7e58fcfec555844eecb4a1dcb");

        //初始化上方工具栏
        Toolbar tl_head =  findViewById(R.id.tl_head);
        tl_head.setTitle("");
        setSupportActionBar(tl_head);

        sw_homeRefresh = findViewById(R.id.sw_homeRefresh);
        sw_homeRefresh.setProgressBackgroundColorSchemeResource(R.color.btn_backgrounp);
        sw_homeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });

        //初始化头部的视图组件
        initTlView();
        //判断是否有用户登录
        ifOnLine();
        //初始化横幅轮播条
        initbannerPager();

        //初始化四大推荐一栏
        initRecyclerRecommend();
        //初始化工作分类一栏
        initRecyclerWorkClassify();
        //初始化工作推荐一栏
        initWorkRecommend();
    }

    private void initView(){
        //初始化头部的视图组件
        initTlView();
        //判断是否有用户登录
        ifOnLine();

        //初始化四大推荐一栏
        initRecyclerRecommend();
        //初始化工作分类一栏
        initRecyclerWorkClassify();
        //初始化工作推荐一栏
        initWorkRecommend();
        sw_homeRefresh.setRefreshing(false);
    }

    //初始化头部的视图组件
    private void initTlView(){
        tv_homeLogin = findViewById(R.id.tv_homeLogin);
        tv_homeRegsister = findViewById(R.id.tv_homeRegsister);
        tv_homeLineUserNname = findViewById(R.id.tv_homeLineUserNname);
        lin_homeOnLineTL = findViewById(R.id.lin_homeOnLineTL);
        lin_homeOffLineTL = findViewById(R.id.lin_homeOffLineTL);
        iv_homeLineHeader = findViewById(R.id.iv_homeLineHeader);
    }

    //判断当前是否有用户登录
    private void ifOnLine(){
        User user = BmobUser.getCurrentUser(User.class);
        if(user == null){
            lin_homeOnLineTL.setVisibility(View.GONE);
            lin_homeOffLineTL.setVisibility(View.VISIBLE);
            //未登录状态组件的相应操作
            offLineViewOnClick();
        }else{
            lin_homeOnLineTL.setVisibility(View.VISIBLE);
            lin_homeOffLineTL.setVisibility(View.GONE);
            //登录状态组件的相应操作
            onLineViewOnClick();
        }
    }

    //未登录状态组件的相应操作
    private void offLineViewOnClick(){
        tv_homeLogin.setOnClickListener(this);
        tv_homeRegsister.setOnClickListener(this);
    }

    //登录状态组件的相应操作
    private void onLineViewOnClick(){
        lin_homeOnLineTL.setOnClickListener(this);
        User user = BmobUser.getCurrentUser(User.class);
        tv_homeLineUserNname.setText(user.getUsername());
        if(user.getHead_portrait() == null){
            iv_homeLineHeader.setImageResource(R.drawable.header);
        }else{
            ImageLoaderFactory.getLoader().load(iv_homeLineHeader, user.getHead_portrait().getUrl(), R.drawable.default_head, new ImageLoadingListener() {
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
        }
    }

    //初始化推荐一栏
    public void initRecyclerRecommend(){
        // 从布局文件中获取名叫rv_grid的循环视图
        RecyclerView rv_recommend = findViewById(R.id.rv_recommend);
        // 创建一个垂直方向的网格布局管理器
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        // 设置循环视图的布局管理器
        rv_recommend.setLayoutManager(manager);
        // 构建一个市场列表的网格适配器
        RecyclerAdapter_recommend adapter = new RecyclerAdapter_recommend(this, Recycler_recommend_info.getRecommendInfo());
        // 设置网格列表的点击监听器
        adapter.setOnItemClickListener(adapter);
        // 给rv_grid设置市场网格适配器
        rv_recommend.setAdapter(adapter);
        // 设置rv_grid的默认动画效果
        rv_recommend.setItemAnimator(new DefaultItemAnimator());
    }

    //初始化工作分类一栏
    public void initRecyclerWorkClassify(){
        // 从布局文件中获取名叫rv_grid的循环视图
        RecyclerView rv_workClassify = findViewById(R.id.rv_workClassify);
        // 创建一个垂直方向的网格布局管理器
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        // 设置循环视图的布局管理器
        rv_workClassify.setLayoutManager(manager);
        // 构建一个市场列表的网格适配器
        RecyvlerAdpater_workclassify adapter = new RecyvlerAdpater_workclassify(this, Recycler_workclassify_info.getWorkInfo());
        // 设置网格列表的点击监听器
        adapter.setOnItemClickListener(adapter);
        // 给rv_grid设置市场网格适配器
        rv_workClassify.setAdapter(adapter);
        // 设置rv_grid的默认动画效果
        rv_workClassify.setItemAnimator(new DefaultItemAnimator());
        // 给rv_grid添加列表项之间的空白装饰
        rv_workClassify.addItemDecoration(new DividerGridItemDecoration(this));
    }


    //初始化横幅轮播条
    public void initbannerPager(){
        BannerPager banner = findViewById(R.id.banner_pager);
        // 获取横幅轮播条的布局参数
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
        params.height = (int) (Utils.getScreenWidth(this) * 250f / 640f);
        // 设置横幅轮播条的布局参数
        banner.setLayoutParams(params);
        // 设置横幅轮播条的广告图片队列
        banner.setImage(5);

        // 设置横幅轮播条的广告点击监听器
        //banner.setOnBannerListener(this);
        // 开始广告图片的轮播滚动
        banner.start();
    }

    //初始化工作推荐一栏
    public void initWorkRecommend(){
        RecyclerView rv_searchWork = findViewById(R.id.rv_searchWork);
        LinearLayoutManager manager = new LinearLayoutManager(HomeActivity.this,LinearLayout.VERTICAL,false);
        rv_searchWork.setLayoutManager(manager);
        adapterWork = new RecyWorkRecoAdapter(HomeActivity.this);
        adapterWork.setOnItemClickListener(adapterWork);
        rv_searchWork.setAdapter(adapterWork);
        queryWork();

    }

    //查询推荐工作
    private void queryWork(){
        /*
        String bql = "select include user, * from Work";
        new BmobQuery<Work>().doSQLQuery(bql, new SQLQueryListener<Work>() {
            @Override
            public void done(BmobQueryResult<Work> bmobQueryResult, BmobException e) {
                if(e == null){
                    List<Work> result = (List<Work>)bmobQueryResult.getResults();
                    Log.i(TAG, "done: list长度="+result.size());
                    adapterWork.addItemAll(result);
                }
            }
        });*/

        BmobQuery<Work> query = new BmobQuery<>();
        query.include("user");
        query.setLimit(5);
        query.findObjects(new FindListener<Work>() {
            @Override
            public void done(List<Work> list, BmobException e) {
                if(e == null){
                    Log.i(TAG, "done: list长度="+list.size());
                    adapterWork.addItemAll(list);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_homeLogin){
            Intent intent1 = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent1);
        }else if(v.getId() == R.id.tv_homeRegsister){
            Intent intent2 = new Intent(HomeActivity.this, RegisterActivity.class);
            startActivity(intent2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ifOnLine();
    }
}
