package com.xwy.tao_work.mytaowork.messages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.BmobIMUser;
import com.xwy.tao_work.mytaowork.messages.adapter.ChatRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener ,MessageListHandler{

    private final String TAG = "Chat";
    private BmobIMUser user;
    private EditText et_chat;
    private TextView chat_toolbar_title;
    private ArrayList<BmobIMMessage> list = new ArrayList<>();
    private BmobIMConversation messageManager;                  //BmobIM消息的管理者
    @SuppressLint("StaticFieldLeak")
    public static ChatRecyclerAdapter adapter;                        //聊天的Rcycler适配器
    public static LinearLayoutManager manager;                        //Rcycler的线性布局管理器


    private String currentUid="";           //登录用户的ID
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //获取传过来的值，有接收方信息，和会话入口
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        user = bundle.getParcelable("user");
        BmobIMConversation conversationEntrance = (BmobIMConversation)bundle.getSerializable("c");
        //初始化会话界面管理员
        assert conversationEntrance != null;
        messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        try{
            currentUid = BmobUser.getCurrentUser().getObjectId();
        }catch (Exception e) {
            e.printStackTrace();
        }

        //初始化上方标题栏
        Toolbar tl_Message_head = (Toolbar)findViewById(R.id.tl_Message_head);
        tl_Message_head.setTitle("和"+messageManager.getConversationTitle()+"在聊天");
        setSupportActionBar(tl_Message_head);

        //初始化视图
        initView();
        //获得聊天记录
        initChatRecord();
    }

    private void initChatRecord(){
        messageManager.queryMessages(user.getBmobIMMessage(), user.getSize(), new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "done: list的长度"+list.size());
                    if (!list.isEmpty()) {
                        adapter.addMessages(list);
                        adapter.notifyDataSetChanged();
                        scrollToBottom();
                    }
                } else {
                    Toast.makeText(ChatActivity.this,e.getMessage() + "(" + e.getErrorCode() + ")",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initView(){
        et_chat = (EditText)findViewById(R.id.et_chat);
        Button btn_mine_send = (Button) findViewById(R.id.btn_mine_send);
        btn_mine_send.setOnClickListener(this);
        chat_toolbar_title = (TextView)findViewById(R.id.chat_toolbar_title);

        RecyclerView rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_chat.setLayoutManager(manager);
        adapter = new ChatRecyclerAdapter(this,list);
        rv_chat.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        String chats = et_chat.getText().toString();
        if(v.getId() == R.id.btn_mine_send){
            if(chats.equals("")){
                Toast.makeText(this,"不能发送空消息，请重新写消息",Toast.LENGTH_SHORT).show();

            }else if(BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                Toast.makeText(ChatActivity.this, "尚未连接IM服务器", Toast.LENGTH_LONG).show();

            }else{
                BmobIMTextMessage message = new BmobIMTextMessage();
                message.setContent(chats);
                message.setFromId(currentUid);
                messageManager.sendMessage(message,listener);
                Log.d("ChatActivity", "onClick: 时间："+message.getCreateTime());
            }
        }
    }


    /*消息发送监听器*/
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            Log.d(TAG, "onProgress: 进度"+value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            Log.d(TAG, "onStart: ");
            adapter.addMessage(msg);
            et_chat.setText("");
        }

        @Override
        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
            Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            et_chat.setText("");
            if (e != null) {
                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "done: " + e.getMessage());
            }
        }
    };

    /*RecyclerView滑到底部*/
    public static void scrollToBottom() {
        manager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }


    /*单页面消息接收监听*/
    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Log.i(TAG, "聊天页面接收到消息："+ list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }
    }

    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (messageManager != null  && messageManager.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if(adapter.findPosition(msg) < 0){
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                messageManager.updateReceiveStatus(msg);
            }

        } else {
            Log.i(TAG, "该消息不属于此对象");
        }
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
        //TODO 消息：5.4、更新此会话的所有消息为已读状态
        if (messageManager != null) {
            messageManager.updateLocalCache();
        }
        super.onDestroy();
    }
}
