package com.example.selfitjava2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Scanner;

public class ChatRoom extends Activity {
    private Socket s;
    private Handler networkdHandler;
    private EditText chatMsg;
    private Button chatBtn;
    private PrintWriter pw;
    private BufferedReader in;
    String namev;
    @SuppressLint("NewApi")
    private ArrayList<String> msgPool;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
//        setContentView(R.layout.chatroom);
//
//        if(Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
//
//        list = (ListView)findViewById(R.id.chat_list);
//        chatMsg = (EditText)findViewById(R.id.chat_msg);
//        chatBtn = (Button)findViewById(R.id.chat_msgBtn);
//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        String hostv = bundle.getString("host");
//        namev = bundle.getString("name");
//        int portv = bundle.getInt("port");
//        msgPool = new ArrayList<>();
//        networkdHandler = new Handler();
//        testServer(hostv, namev, portv);
//        chatResponse();
//
//
//        chatBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String msg = chatMsg.getText().toString().trim();
//                pw.println("talk/chat/all/["+namev+"]"+msg);
//                chatMsg.setText("");
//                chatMsg.requestFocus();
//            }
//        });
    }

//    private boolean testServer(String hostv, String namev, int portv) {
//        try {
//            s = new Socket(hostv,portv);
//            pw = new PrintWriter(new BufferedOutputStream(s.getOutputStream()),true);
//            pw.println("room/chat/all/"+namev+"/");
//            return true;
//        }catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this,"Error!",Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//
//    }

    private void chatResponse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    in = new BufferedReader(new InputStreamReader(s.getInputStream(),"UTF-8"));
                    while(true) {
                        final String protocol = in.readLine();
                        System.out.println("--------------- received from server : " + protocol + "--------------");
                        Log.d("Msg", protocol);
                        StringTokenizer stz = new StringTokenizer(protocol,"/");
                        final String protocol1 = stz.nextToken();
                        String protocol2 = stz.nextToken();
                        String protocol3 = stz.nextToken();
                        String protocol4 = stz.nextToken();
                        String msgv = protocol2 + " ] " + protocol3  + " " + protocol4;
                        msgPool.add(msgv);
                        networkdHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(protocol1.equals("talk")) {
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChatRoom.this, android.R.layout.simple_list_item_1, msgPool);
                                    list.setAdapter(adapter);
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}