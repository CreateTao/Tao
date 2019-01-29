package com.xwy.tao_work.mytaowork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;
import com.xwy.tao_work.mytaowork.data.BaseDate;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.data.Work;
import com.xwy.tao_work.mytaowork.widget.MyCircleImageView;

import cn.bmob.v3.BmobUser;

public class WorkInfoBaseActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "HomeActivity";
    private TextView tv_workInfoTitle,tv_workInfoMoney;
    private TextView tv_workInfoBigType,tv_workInfoSmallType,tv_workInfoSex;
    private TextView tv_workInfoNumber,tv_workInfoTime,tv_workInfoStopTime;
    private TextView tv_workInfoAsk,tv_workInfoContent,tv_showSurpWorkNum;

    private MyCircleImageView iv_workPuberHead;
    private TextView tv_workPuberName;

    private Work work;
    private String headUrl;
    private String UserName;
    private String UserId;
    private boolean UserSex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_info);

        //获得传来的work信息
        Intent intent = getIntent();
        work = intent.getParcelableExtra("workInfo");
        headUrl = intent.getStringExtra("workPuberHead");
        UserName = intent.getStringExtra("workPuberName");
        UserId = intent.getStringExtra("workPuberId");
        UserSex = intent.getBooleanExtra("workPuberSex",false);

        //初始化上方标题栏
        Toolbar tl_workInfo = (Toolbar)findViewById(R.id.tl_workInfo);
        tl_workInfo.setTitle("");
        setSupportActionBar(tl_workInfo);
        tl_workInfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        //给相应控件赋值
        getInfo();
    }

    //初始化各控件
    private void initView(){
        tv_workInfoTitle = findViewById(R.id.tv_workInfoTitle);
        tv_workInfoMoney = findViewById(R.id.tv_workInfoMoney);
        iv_workPuberHead = findViewById(R.id.iv_workPuberHead);
        tv_workPuberName = findViewById(R.id.tv_workPuberName);
        tv_workInfoBigType = findViewById(R.id.tv_workInfoBigType);
        tv_workInfoSmallType = findViewById(R.id.tv_workInfoSmallType);
        tv_workInfoSex = findViewById(R.id.tv_workInfoSex);
        tv_workInfoNumber = findViewById(R.id.tv_workInfoNumber);
        tv_workInfoTime = findViewById(R.id.tv_workInfoTime);
        tv_workInfoStopTime = findViewById(R.id.tv_workInfoStopTime);
        tv_workInfoAsk = findViewById(R.id.tv_workInfoAsk);
        tv_workInfoContent = findViewById(R.id.tv_workInfoContent);
        tv_showSurpWorkNum = findViewById(R.id.tv_showSurpWorkNum);

        TextView tv_chatWorkPuber = findViewById(R.id.tv_chatWorkPuber);
        tv_chatWorkPuber.setOnClickListener(this);
        LinearLayout lin_workJoin = findViewById(R.id.lin_workJoin);
        lin_workJoin.setOnClickListener(this);
    }

    private void getInfo(){
        tv_workInfoTitle.setText(work.getWorkTitle());
        tv_workInfoMoney.setText("工资：   "+work.getWorkMoney()+"/人");
        ImageLoaderFactory.getLoader().load(iv_workPuberHead, headUrl,
                R.drawable.default_head, new ImageLoadingListener() {
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
        tv_workPuberName.setText(UserName);
        tv_workInfoBigType.setText(BaseDate.getBigClassify()[work.getWorkBigType()]);
        tv_workInfoSmallType.setText(BaseDate.getSmallClassify()[work.getWorkBigType()][work.getWorkSmallType()]);
        tv_workInfoSex.setText(BaseDate.getWorkSex()[work.getWorkSex()]);
        tv_workInfoNumber.setText(""+work.getWorkNumber());
        tv_workInfoTime.setText(work.getWorkTime());
        tv_workInfoStopTime.setText(work.getWorkStopTime());
        initWorkCondition();
        tv_workInfoContent.setText(work.getWorkContent());
        tv_showSurpWorkNum.setText("还剩下"+work.getWorkNumber()+"个名额");
    }

    //将兼职要求转为为序号
    private void initWorkCondition(){
        String s = work.getWorkCondition();
        String workCondition = "";
        int position;
        int i = 1;
        while(!s.equals("")) {
            position = s.indexOf("。");
            if(position != -1) {
                workCondition = workCondition + i + "."+s.substring(0, position)+"\n";
                i++;
                s = s.substring(position+1);
            }
        }
        tv_workInfoAsk.setText(workCondition);
    }

    @Override
    public void onClick(View v) {
        User u =  BmobUser.getCurrentUser(User.class);
        if(v.getId() == R.id.tv_chatWorkPuber){
            if(UserId.equals(u.getObjectId())){
                Toast.makeText(WorkInfoBaseActivity.this,"此工作为本人发布的！",Toast.LENGTH_SHORT).show();
            }
            /*建立即时通信*/
        }else if(v.getId() == R.id.lin_workJoin){
            if(UserId.equals(u.getObjectId())){
                Toast.makeText(WorkInfoBaseActivity.this,"此工作为本人发布的！",Toast.LENGTH_SHORT).show();
            }else if(!Sex()){
                Toast.makeText(WorkInfoBaseActivity.this,"你不符合此工作要求，可以再选择其他工作" +
                        "！！",Toast.LENGTH_SHORT).show();
            }else if(work.getWorkSupNumber() == 0){
                Toast.makeText(WorkInfoBaseActivity.this,"报名人数已满，请再选择其他工作哦！" ,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(WorkInfoBaseActivity.this,"报名成功！等待通知哦！！" ,Toast.LENGTH_SHORT).show();
                finish();
                /*进行工作信息的更新(主要是报名人数进行改变)*/
            }

        }
    }

    //判断性别是否符合要求
    private boolean Sex(){
        int workSex = work.getWorkSex();
        if(workSex == 2){
            return true;
        }else if(workSex == 0){
            return !UserSex;
        }else  if(workSex == 1){
            return UserSex;
        }
        return false;
    }
}
