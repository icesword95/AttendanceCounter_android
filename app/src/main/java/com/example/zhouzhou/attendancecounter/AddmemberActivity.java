package com.example.zhouzhou.attendancecounter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhouzhou.attendancecounter.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouzhou on 2016/11/1.
 */

public class AddmemberActivity extends AppCompatActivity{

    private RequestQueue mRequestQueue;
    private int managerId;
    private int attendeeId ;//这个是要识别器返回的识别代号
    private int activityId ;
    EditText editText ;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);
        mRequestQueue = Volley.newRequestQueue(this) ;
        Intent intent = getIntent();
        managerId = intent.getIntExtra("managerId",-1) ;
        activityId =intent.getIntExtra("activityId",-1) ;
        editText = (EditText)findViewById(R.id.input_addmember_1) ;

        Button uploadbtn = (Button)findViewById(R.id.button_addmember_1) ;
        uploadbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String attendeename = editText.getText().toString() ;
                View focusView = null ;
                if(TextUtils.isEmpty(attendeename))
                {
                    editText.setError("请输入要增加的用户名字");
                    focusView =editText ;
                    focusView.requestFocus() ;
                    return ;
                }
                String addmemberurl = Constant.baseurl+Constant.addmember;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, addmemberurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("TAG", response);
                                JSONObject jsonres = null;
                                int suc = -1 ;
                                try {
                                    jsonres = new JSONObject(response);
                                    suc =jsonres.getInt("res");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                if (suc == 1) {
                                    try {
                                        attendeeId = jsonres.getInt("attendeeId") ;//这是需要训练器返回的数据
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        String path = Environment.getExternalStorageDirectory()+"/temphoto/" ;
                                        File file = new File(path) ;
                                        file.mkdirs() ;
                                        String name = path+"test.jpg" ;
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(name)));
                                        startActivityForResult(intent,1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                        map.put("managerId", Integer.toString(managerId));
                        map.put("attendeeName", attendeename);
                        return map;
                    }
                };

                mRequestQueue.add(stringRequest);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK) {
            int ret = 0;
            if (ContextCompat.checkSelfPermission(AddmemberActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.i("face", "123");

                ActivityCompat.requestPermissions(AddmemberActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ret);
            }
            String path = Environment.getExternalStorageDirectory()+"/temphoto/" ;
            String name = path+"test.jpg" ;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
            Bitmap bitmap = BitmapFactory.decodeFile(name);//这个就是照相机的图片
            // 防止OOM发生
            options.inJustDecodeBounds = false;
            int mWidth = bitmap.getWidth();
            int mHeight = bitmap.getHeight();
            Log.i("debug",Integer.toString(attendeeId)) ;//这里把attendeeId绑定进去



            Log.i("size",Integer.toString(bitmap.getHeight())+" "+Integer.toString(bitmap.getWidth()));
            bitmap.recycle();//在离开前务必回收图片，其他代码写在这行之前
        }

    }

}
