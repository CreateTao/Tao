package com.xwy.tao_work.mytaowork.home;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.BaseDate;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.data.Work;
import com.xwy.tao_work.mytaowork.home.adpater.RecyPublishAdapter;
import com.xwy.tao_work.mytaowork.widget.CustomDateDialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PublishWorkActivity extends AppCompatActivity implements View.OnClickListener,RecyPublishAdapter.SaveEditListener{

    private String TAG = "PublishWorkActivity";
    private Spinner sp_classifyBigWork,sp_classifySmallWork,sp_pubWorkSex;
    private int spBigPosition,spSmallPosition;      //记录下拉框字符串位置
    private int spWorkSexPosition;      //记录性别要求下拉框字符串位置
    private EditText et_pubWorkName,et_publishAsk,et_pubWorkNumber,et_pubWorkMoney,et_pubWorkTime;
    private Button btn_pubWorkDay,btn_pubWorkTime;

    private Calendar calendar;
    private RecyPublishAdapter adapter;

    //记录EditText输入的内容
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_work);

        //初始化上方标题栏
        Toolbar tl_publishHead = (Toolbar)findViewById(R.id.tl_publishHead);
        tl_publishHead.setTitle("");
        setSupportActionBar(tl_publishHead);
        tl_publishHead.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView(){
        calendar = Calendar.getInstance();
        sp_classifyBigWork = findViewById(R.id.sp_classifyBigWork);
        sp_classifySmallWork = findViewById(R.id.sp_classifySmallWork);
        sp_pubWorkSex = findViewById(R.id.sp_pubWorkSex);
        //初始化兼职工作的大分类
        initBigClassify();
        //初始化性别要求下拉框
        initWorkSex();

        findViewById(R.id.btn_publishAsk).setOnClickListener(this);
        //初始化要求的Recycler
        initRecycler();

        //工作名称
        et_pubWorkName = findViewById(R.id.et_pubWorkName);

        //具体要求和人数
        et_publishAsk = findViewById(R.id.et_publishAsk);
        et_pubWorkNumber = findViewById(R.id.et_pubWorkNumber);

        //时间和日期按钮
        btn_pubWorkDay = findViewById(R.id.btn_pubWorkDay);
        btn_pubWorkDay.setOnClickListener(this);
        btn_pubWorkTime = findViewById(R.id.btn_pubWorkTime);
        btn_pubWorkTime.setOnClickListener(this);

        //工作工资和大致工作时间
        et_pubWorkMoney = findViewById(R.id.et_pubWorkMoney);
        et_pubWorkTime = findViewById(R.id.et_pubWorkTime);

        //发布按钮
        findViewById(R.id.btn_pubWorkSure).setOnClickListener(this);

    }

    //初始化要求的Recycler
    private void initRecycler(){
        RecyclerView rv_publishWork = findViewById(R.id.rv_publishWork);
        LinearLayoutManager manager = new LinearLayoutManager(PublishWorkActivity.this,LinearLayout.VERTICAL,false);
        rv_publishWork.setLayoutManager(manager);
        adapter = new RecyPublishAdapter(PublishWorkActivity.this);
        adapter.addItem(1+"");
        list.add("");
        rv_publishWork.setAdapter(adapter);
        rv_publishWork.setItemAnimator(new DefaultItemAnimator());

    }

    //初始化兼职工作的大分类
    private void initBigClassify() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PublishWorkActivity.this, android.R.layout.simple_spinner_item,
                BaseDate.getBigClassify());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_classifyBigWork.setAdapter(adapter);
        sp_classifyBigWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spBigPosition = position;
                //初始化职务选择控件
                initSmallClassify(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //初始化兼职工作的小分类
    private void initSmallClassify(int positionSmall){
        ArrayAdapter<String> adapter_work = new ArrayAdapter<>(PublishWorkActivity.this,android.R.layout.simple_spinner_item,
                BaseDate.getSmallClassify()[positionSmall]);
        adapter_work.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_classifySmallWork.setAdapter(adapter_work);
        sp_classifySmallWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spSmallPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //初始化性别要求下拉框
    private void initWorkSex(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PublishWorkActivity.this, android.R.layout.simple_spinner_item,
                BaseDate.getWorkSex());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_pubWorkSex.setAdapter(adapter);
        sp_pubWorkSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spWorkSexPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_publishAsk){
            int length = list.size();
            Log.i(TAG, "onClick: list的length="+length);
            if(length <= 0 || TextUtils.isEmpty(list.get(length-1))){
                Toast.makeText(PublishWorkActivity.this,"请先输入此行再增加！",Toast.LENGTH_SHORT).show();
            }else {
                adapter.addItem(length+"");
                list.add("");
            }
        }else if(v.getId() == R.id.btn_pubWorkDay){
            showDays();
        }else if(v.getId() == R.id.btn_pubWorkTime){
            showTime();
        }else if(v.getId() == R.id.btn_pubWorkSure){
            publishWork();
        }
    }

    //展示日期控件
    private void showDays(){
        // 创建一个自定义的日期对话框实例
        CustomDateDialog dialog = new CustomDateDialog(this);
        // 设置日期对话框上面的年、月、日，并指定日期变更监听器
        dialog.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new DateListener());
        dialog.show(); // 显示日期对话框
    }

    // 定义一个日期变更监听器，一旦点击对话框的确定按钮，就触发监听器的onDateSet方法
    private class DateListener implements CustomDateDialog.OnDateSetListener {
        @Override
        public void onDateSet(int year, int month, int day) {
            String desc = String.format("%d年%d月%d日", year, month, day);
            btn_pubWorkDay.setText(desc);

        }
    }

    //展示时间控件
    private void showTime(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(PublishWorkActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                btn_pubWorkTime.setText(time);

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    //发布任务
    private void publishWork(){
        final String workName = et_pubWorkName.getText().toString();
        final String detailAsk = et_publishAsk.getText().toString();
        final String number = et_pubWorkNumber.getText().toString();
        final String stopData = btn_pubWorkDay.getText().toString();
        final String stopTime = btn_pubWorkTime.getText().toString();
        final String time = et_pubWorkTime.getText().toString();
        final String money = et_pubWorkMoney.getText().toString();

        if(TextUtils.isEmpty(workName)){
            Toast.makeText(PublishWorkActivity.this,"工作标题不能为空",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(detailAsk)){
            Toast.makeText(PublishWorkActivity.this,"工作具体内容不能为空",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(stopData)){
            Toast.makeText(PublishWorkActivity.this,"报名截止日期不能为空！",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(stopTime)){
            Toast.makeText(PublishWorkActivity.this,"报名截止时间不能为空！",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(money)){
            Toast.makeText(PublishWorkActivity.this,"工作工资不能为空！",Toast.LENGTH_SHORT).show();
        }else if(list.isEmpty()){
            Toast.makeText(PublishWorkActivity.this,"工作要求不能为空！",Toast.LENGTH_SHORT).show();
        }else if(time.equals("0")){
            Toast.makeText(PublishWorkActivity.this,"工作时间不能为0",Toast.LENGTH_SHORT).show();
            et_pubWorkTime.setText("");
        }else if(number.equals("0")){
            Toast.makeText(PublishWorkActivity.this,"兼职人员数量不能为0",Toast.LENGTH_SHORT).show();
            et_pubWorkMoney.setText("");
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(PublishWorkActivity.this);
            builder.setTitle("提示:");
            builder.setMessage("是否确定提交？");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String ask = "";
                    String Times = stopData+stopTime;
                    for(int i = 0; i < list.size(); i++){
                        ask = ask + list.get(i);
                    }
                    Work work = new Work();
                    work.setUser(BmobUser.getCurrentUser(User.class));
                    work.setWorkTitle(workName);
                    work.setWorkBigType(spBigPosition);
                    work.setWorkSmallType(spSmallPosition);
                    work.setWorkContent(detailAsk);
                    work.setWorkCondition(ask);
                    int workNumber;
                    if(number.equals("")){
                        workNumber = 0;
                    }else {
                        workNumber = Integer.parseInt(number);
                    }
                    work.setWorkNumber(workNumber);
                    work.setWorkSupNumber(workNumber);
                    work.setWorkStopTime(Times);
                    work.setWorkTime(time);
                    work.setWorkSex(spWorkSexPosition);
                    work.setWorkMoney(Integer.parseInt(money));

                    work.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e == null){
                                Toast.makeText(PublishWorkActivity.this,"发布成功!",Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(PublishWorkActivity.this,"发布失败，请检查网络或填入的数据",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void SaveEdit(int position, String string) {
        string = string+"。";
        Log.i(TAG, "SaveEdit: position="+position+"  string="+string);
        list.set(position,string);
    }
}
