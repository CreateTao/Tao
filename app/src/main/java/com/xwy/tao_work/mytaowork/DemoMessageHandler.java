package com.xwy.tao_work.mytaowork;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.xwy.tao_work.mytaowork.messages.ChatActivity;
import com.xwy.tao_work.mytaowork.messages.MessageActivity;
import com.xwy.tao_work.mytaowork.utils.DateUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;

public class DemoMessageHandler extends BmobIMMessageHandler {
    private final String TAG = "DemoMessageHandler";
    private Context context;

    public DemoMessageHandler(Context context){
        this.context = context;
    }



    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息
        Log.e(TAG, "bindView:  getFromId "+event.getMessage().getFromId() );
        Log.e(TAG, "bindView:  getContent "+event.getMessage().getContent() );
        Log.e(TAG, "bindView:  getExtra "+event.getMessage().getExtra() );
        Log.e(TAG, "bindView:  getToId "+event.getMessage().getToId() );
        Log.e(TAG, "bindView:  getCreateTime "+ DateUtil.getDateFormat().format(event.getMessage().getCreateTime()));
        Log.e(TAG, "bindView:  getReceiveStatus "+event.getMessage().getReceiveStatus() );
        BmobIMMessage msg = event.getMessage();
        processSDKMessage(msg,event);
        ChatActivity.adapter.addMessage(event.getMessage());
    }



    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = event.getEventMap();
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            Log.e(TAG, "onOfflineReceive: "+"用户" + entry.getKey() + "发来" + size + "条消息" );
            for (int i = 0; i < size; i++) {
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getFromId "+list.get(i).getMessage().getFromId() );
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getContent "+list.get(i).getMessage().getContent() );
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getExtra "+list.get(i).getMessage().getExtra() );
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getToId "+list.get(i).getMessage().getToId() );
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getCreateTime "+DateUtil.getDateFormat().format(list.get(i).getMessage().getCreateTime()));
                Log.e(TAG, "bindView:  离线消息： "+i+ "  getReceiveStatus "+list.get(i).getMessage().getReceiveStatus() );
                BmobIMMessage msg = list.get(i).getMessage();
                processSDKMessage(msg,list.get(i));
            }
        }
    }

    /**
     * 处理消息
     *
     * @param event
     */
    private void processSDKMessage(BmobIMMessage msg, MessageEvent event) {
        if (BmobNotificationManager.getInstance(context).isShowNotification()) {
            //如果需要显示通知栏，SDK提供以下两种显示方式：
            Intent pendingIntent = new Intent(context, MessageActivity.class);
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


            //TODO 消息接收：8.5、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
            BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);

            //TODO 消息接收：8.6、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
            BmobIMUserInfo info = event.getFromUserInfo();
            //这里可以是应用图标，也可以将聊天头像转成bitmap
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            BmobNotificationManager.getInstance(context).showNotification(largeIcon,
                    info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
        } else {
            //直接发送消息事件
            EventBus.getDefault().post(event);
        }
    }

}
