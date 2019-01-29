package com.xwy.tao_work.mytaowork.messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.base.BaseActivity;
import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.BmobIMUser;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.login.LoginActivity;
import com.xwy.tao_work.mytaowork.messages.adapter.MessageRecyAdapter;
import com.xwy.tao_work.mytaowork.utils.DateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MessageActivity extends BaseActivity implements View.OnClickListener{
    private final String TAG = "MessageActivity";
    private RecyclerView rv_content_message;
    private ArrayList<BmobIMUser> list = new ArrayList<>();         //给会话界面传的list
    private MessageRecyAdapter adapter;
    private LinearLayout onLineMes,offLineMes;

    //private int Line =  0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        onLineMes = findViewById(R.id.onLineMes);
        offLineMes =  findViewById(R.id.offLineMes);
        JudgeLine();//判断是否处于登录状态


    }

    //判断是否处于登录状态
    private void JudgeLine(){
        User user = BmobUser.getCurrentUser(User.class);
        if(user == null){
            onLineMes.setVisibility(View.GONE);
            offLineMes.setVisibility(View.VISIBLE);
            initOffLineView();
        }else {
            onLineMes.setVisibility(View.VISIBLE);
            offLineMes.setVisibility(View.GONE);
            initonLineView();
        }
    }

    //初始化离线控件
    public void initOffLineView(){
        Button btn_login_message = findViewById(R.id.btn_login_message);
        btn_login_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //初始化在线控件
    public void initonLineView(){
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        initConnect();

        //初始化上方标题栏
        Toolbar tl_Message_head = findViewById(R.id.tl_Message_head);
        tl_Message_head.setTitle("");
        setSupportActionBar(tl_Message_head);
        tl_Message_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_content_message = findViewById(R.id.rv_content_message);

        Log.d(TAG, "onCreate: 登录者的用户名："+BmobUser.getCurrentUser().getUsername());
        ifListNull();
    }


    private void initConnect(){
        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUser(user.getObjectId(),
                                        user.getUsername()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        Toast.makeText(MessageActivity.this,""+e.getErrorCode(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //查询所有会话
    private List<BmobIMConversation> quertCon(){
        List<BmobIMConversation> listConversation = BmobIM.getInstance().loadAllConversation();
        if(listConversation!=null && listConversation.size()>0){
            for(BmobIMConversation conversation : listConversation){
                BmobIMUser user = new BmobIMUser(conversation.getConversationId(),conversation.getConversationTitle());
                int i;
                int flat = 0;
                //再次出现页面时，更新消息记录
                for(i =0; i<list.size(); i++){
                    if(user.getUserId().equals(list.get(i).getUserId())){
                        flat = 1;
                        break;
                    }
                    flat = 0;
                }

                Log.d(TAG, "quertCon: 会话的ID："+conversation.getConversationId());
                Log.d(TAG, "quertCon: 会话的Title:"+conversation.getConversationTitle());
                Log.d(TAG, "quertCon: 会话的消息记录的长度:"+conversation.getMessages().size());
                //每个会话第一个消息,以及消息的个数
                BmobIMMessage message;         //第一条消息，用以查询
                int Size;                   //消息记录的长度，用以查询
                String  lastMessage;        //最后一条消息，用以展示在会话列表上
                String time;                //最后一条消息的时间
                if(conversation.getMessages() == null){
                    lastMessage = "没有记录";
                    Size = 0;
                    message = null;
                    time = "";
                    Log.d(TAG, "quertCon: 会话的消息记录的长度:"+Size);
                }else{
                    lastMessage = conversation.getMessages().get(0).getContent();
                    Size = conversation.getMessages().size();
                    message = conversation.getMessages().get(conversation.getMessages().size()-1);
                    time = DateUtil.getDateFormat().format(conversation.getMessages().get(0).getUpdateTime());
                    Log.d(TAG, "quertCon: 会话的消息记录的长度:"+Size);
                }


                if(flat == 0){
                    user.setBmobIMMessage(message);
                    assert message != null;
                    /*
                    if(message.getContent()!=null){
                        Log.d(TAG, "quertCon: 会话的第一个消息的内容："+message.getContent());
                    }*/
                    //每个会话的长度
                    user.setSize(Size);
                    Log.d(TAG, "quertCon: 会话的消息记录的长度:"+Size);
                    user.setLastMessage(lastMessage);
                    Log.d(TAG, "quertCon: 会话的最后一条消息:"+lastMessage);
                    user.setTime(time);
                    Log.d(TAG, "quertCon: 会话的最后一条消息时间:"+time);
                    list.add(user);
                }else{
                    list.get(i).setLastMessage(lastMessage);
                    list.get(i).setSize(Size);
                    list.get(i).setBmobIMMessage(message);
                    list.get(i).setTime(time);
                }
            }
        }
        return listConversation;
    }

    //判断读取的list是否为空,为空则展示文本显示，否则显示聊天记录
    private void ifListNull(){
        LinearLayoutManager manager = new LinearLayoutManager(MessageActivity.this,LinearLayout.VERTICAL,false);
        rv_content_message.setLayoutManager(manager);
        adapter = new MessageRecyAdapter(MessageActivity.this,list,quertCon());
        adapter.setOnItemListener(adapter);
        adapter.setOnItemLongListener(adapter);
        rv_content_message.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JudgeLine();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    //button的点击事件
    @Override
    public void onClick(View v) {
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),images[random]);
            //infos.setImage(Utils.img(bitmap));
    }

    /**注册离线消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表
        adapter.bindData(quertCon(),list);
        adapter.notifyDataSetChanged();
    }

    /**注册消息接收事件
     * @param event
     * 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     * 2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //重新获取本地消息并刷新列表
        adapter.bindData(quertCon(),list);
        adapter.notifyDataSetChanged();
    }



    //溢出菜单显示
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_logn){
            Intent intent = new Intent(MessageActivity.this,SearchUserActvity.class);
            startActivity(intent);
            /*
            final int sizes;
            if(list.isEmpty()){
                sizes = 0;
            }else{
                sizes = list.size();
            }
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("username", "1401488399@qq.com");
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> object, BmobException e) {
                    if(e==null){
                        Log.d("LoginActivity", "done: 获取的用户："+object.get(0).getUsername());
                        User user = object.get(0);
                        BmobIMUser imUser = new BmobIMUser(user.getObjectId(),user.getUsername());
                        list.add(sizes,imUser);

                        //这一步是直接跳转到聊天界面
                        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(imUser, null);
                        Intent intent = new Intent(MessageActivity.this , ChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user",imUser);
                        bundle.putSerializable("c",conversationEntrance);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }else{
                        Toast.makeText(MessageActivity.this,"" + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });*/
        }
        return super.onOptionsItemSelected(item);
    }
}
