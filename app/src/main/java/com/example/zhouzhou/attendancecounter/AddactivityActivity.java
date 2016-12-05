package com.example.zhouzhou.attendancecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhouzhou.attendancecounter.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouzhou on 2016/12/4.
 */

public class AddactivityActivity  extends AppCompatActivity {
    private EditText text;
    private RequestQueue mRequestQueue;
    private int uid ;
    private EditText input ;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addactivity);
        mRequestQueue = Volley.newRequestQueue(this) ;
        Intent intent = getIntent() ;
        uid = intent.getIntExtra("uid",-1) ;
        Log.i("test",Integer.toString(uid)) ;
        Button btn1  =(Button)findViewById(R.id.button_addactivity) ;
        final TextView textView = (TextView)findViewById(R.id.textView_addactivity_2);
        input = (EditText)findViewById(R.id.input_addactivity_2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temstr = input.getText().toString();
                if(TextUtils.isEmpty(temstr)){
                    input.setError("请输入小组名字");
                    View focusview =input ;
                    focusview.requestFocus() ;
                    return ;
                }
                String addactivityurl = Constant.baseurl+Constant.addActivity ;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, addactivityurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("TAG", response);
                                JSONObject jsonres = null;
                                try {
                                    jsonres = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                int suc = -1 ;
                                String temstr ;
                                try {
                                    suc =jsonres.getInt("res");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(suc ==0)
                                {
                                    String msg = "" ;
                                    try {
                                        msg = jsonres.getString("msg") ;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(msg.equals("activity exists"))
                                    {
                                        input.setError("小组名已存在");
                                        View temview = input ;
                                        temview.requestFocus() ;
                                    }
                                    textView.setText("创建小组失败");

                                }
                                else{
                                    textView.setText("创建小组成功");
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        //在这里设置需要post的参数

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("managerId", Integer.toString(uid));
                        map.put("activityName", input.getText().toString());
                        return map;
                    }
                };

                mRequestQueue.add(stringRequest);


            }
        });
    }
}
