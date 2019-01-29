package com.xwy.tao_work.mytaowork.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private int forceLine = 0;      //是否有用户上线的变量，为0表示没上线
    private ForceLineReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xwy.tao_work_mytaowork.FORCE_ONLINE");
        intentFilter.addAction("com.xwy.tao_work_mytaowork.FORCE_OFFLINE");
        receiver = new ForceLineReceiver();
        registerReceiver(receiver,intentFilter);
    }

    public int getLLine(){
        return this.forceLine;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    class ForceLineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction()!=null){
                if(intent.getAction().equals("com.xwy.tao_work_mytaowork.FORCE_ONLINE")){
                    forceLine = 1;
                }else{
                    forceLine = 0;
                }
            }
        }
    }
}
