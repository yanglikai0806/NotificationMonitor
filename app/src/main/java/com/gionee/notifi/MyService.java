package com.gionee.notifi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("OverrideAbstract")
public class MyService extends NotificationListenerService {

    private BufferedWriter bw;
    private SimpleDateFormat sdf;
    private MyHandler handler = new MyHandler();
    private String nMessage;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("px", "Srevice is open"+"-----");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("px", "Get Message"+"-----"+sbn.getNotification().tickerText.toString());
        try{
        nMessage=sbn.getNotification().tickerText.toString();
            init();
        if(nMessage.contains("数据网络服务已停用")) {
            Message message = handler.obtainMessage();
            message.what = 1;
            handler.sendMessage(message);

            writeData(sdf.format(new Date(System.currentTimeMillis()))+":"+nMessage);
        }
        } catch(Exception e){

        }

        //不能再这里更新UI，会报错，你可以试试，在子线程中不能更新UI
        //  Toast.makeText(MyNotificationService.this,"怎一个曹字了得！",Toast.LENGTH_SHORT).show();
    }
    private void writeData(String str){
        try {
//            bw.newLine();
//            bw.write("NOTE");
            bw.newLine();
            bw.write(str);
            bw.newLine();
//            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(){
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            FileOutputStream fos = new FileOutputStream(newFile(),true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
        }catch(IOException e){
            Log.d("KEVIN","BufferedWriter Initialization error");
        }
        Log.d("KEVIN","Initialization Successful");
    }

    private File newFile() {
//        try {
        File fileDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator  + "ANotification");
        fileDir.mkdir();
        String basePath = Environment.getExternalStorageDirectory() + File.separator + "ANotification" + File.separator + "record.txt";
        Log.d("KEVIN", basePath);
        return new File(basePath);

    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1 :
//                    Toast.makeText(MyService.this,"怎一个曹字了得，终于实现了！",Toast.LENGTH_SHORT).show();
            }
        }

    }
}