package com.example.zhouzhou.attendancecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
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

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouzhou on 2016/11/1.
 */

public class RegisterActivity extends AppCompatActivity {
    private AutoCompleteTextView username ;
    private EditText password ;
    private RequestQueue mRequestQueue;
    private TextView HintText ;


    public String toMD5(String plainText) {
        StringBuffer buf = new StringBuffer("");
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            Log.i("md5","32位: " + buf.toString());// 32位的加密

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString() ;
    }
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRequestQueue = Volley.newRequestQueue(this) ;
        username = (AutoCompleteTextView)findViewById(R.id.email_register) ;
        password = (EditText)findViewById(R.id.password_register) ;
        HintText = (TextView)findViewById(R.id.register_hint) ;

        Button registerbtn = (Button)findViewById(R.id.email_register_button) ;
        registerbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String mUser = username.getText().toString() ;
                String mPass = password.getText().toString() ;
                final String muser = mUser ;
                final String mpass = mPass ;
                View focusView = null ;
                Log.i("tomd5",toMD5(mUser));
                if(TextUtils.isEmpty(mUser))
                {
                    username.setError("请输入用户名");
                    focusView = username ;
                    focusView.requestFocus();
                    return ;
                }
                if(TextUtils.isEmpty(mPass))
                {
                    password.setError("请输入密码");
                    focusView = password ;
                    focusView.requestFocus();
                    return ;
                }
                String registerurl = Constant.baseurl+Constant.register ;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, registerurl,
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
                                try {
                                    suc =jsonres.getInt("res");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                if (suc == 1) {
                                    HintText.setText("注册成功！");
                                }
                                else{
                                    String temhint ="";
                                    try {
                                        temhint = jsonres.getString("msg") ;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("TAG", temhint );
                                    if(temhint.equals("username exits")){
                                        username.setError("该用户名已存在");
                                        View viewfocus = username ;
                                        viewfocus.requestFocus() ;

                                    }
                                    HintText.setText("注册失败！");
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
                        map.put("username", toMD5(muser));
                        map.put("password", toMD5(mpass));
                        return map;
                    }
                };

                mRequestQueue.add(stringRequest);


            }
        });
    }
}