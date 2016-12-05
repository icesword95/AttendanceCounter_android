package com.example.zhouzhou.attendancecounter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhouzhou.attendancecounter.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouzhou on 2016/11/1.
 */

public class GroupselectActivity extends ListActivity {
    private RequestQueue mRequestQueue;
    private int uid ;
    protected  void onResume(){
        super.onResume();
        mRequestQueue = Volley.newRequestQueue(this) ;
        String Groupurl = Constant.baseurl+ Constant.getActivity ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Groupurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("test",response);
                        try {
                            JSONObject jsonres = new JSONObject(response) ;
                            int suc = jsonres.getInt("res");
                            if(suc ==1) {
                                JSONArray jsonarray = jsonres.getJSONArray("activity") ;
                                List<String> list = new ArrayList<String>();
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject temjson = jsonarray.getJSONObject(i) ;
                                    list.add(temjson.getString("activityName"));
                                }
                                ArrayAdapter adapter = new ArrayAdapter(GroupselectActivity.this , android.R.layout.simple_list_item_1, list);
                                setListAdapter(adapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //JSONObject jsonres = new JSONObject(response) ;
                        //jsonres.getJSONArray("")

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
                return map;
            }
        };
        mRequestQueue.add(stringRequest) ;
    };
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupselect);

        mRequestQueue = Volley.newRequestQueue(this) ;
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid",-1) ;
        Button btn1 = (Button)findViewById(R.id.button_groupselect) ;
        btn1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupselectActivity.this,AddactivityActivity.class);
                intent.putExtra("uid",uid) ;
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String Groupurl = Constant.baseurl+ Constant.getActivity ;
        Log.i("test",Groupurl) ;
        final int pos = position ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Groupurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("test",response);
                        try {
                            JSONObject jsonres = new JSONObject(response) ;
                            int suc = jsonres.getInt("res");
                            if(suc ==1) {
                                JSONArray jsonarray = jsonres.getJSONArray("activity") ;
                                JSONObject temjson= jsonarray.getJSONObject(pos) ;
                                String activityName = temjson.getString("activityName");
                                int activityId  = temjson.getInt("activityId") ;
                                Intent intent =new Intent(GroupselectActivity.this,GroupActivity.class);
                                Log.i("uid",Integer.toString(uid)) ;
                                intent.putExtra("managerId",uid) ;
                                intent.putExtra("activityId",activityId);
                                intent.putExtra("activityName",activityName) ;
                                startActivity(intent);



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //JSONObject jsonres = new JSONObject(response) ;
                        //jsonres.getJSONArray("")

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
                return map;
            }
        };
        mRequestQueue.add(stringRequest) ;
    }
}