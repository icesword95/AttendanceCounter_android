package com.example.zhouzhou.attendancecounter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class DeletememberActivity extends ListActivity {
    private RequestQueue mRequestQueue;
    private int managerId;
    private int activityId ;
    List<Integer> attendeeId ;
    List<String> list ;
    ArrayAdapter adapter ;
    protected void onResume(){
       super.onResume();
        String Attendeeurl = Constant.baseurl+ Constant.getAttendee ;
        Log.i("test","geting");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Attendeeurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("test",response);
                        try {
                            JSONObject jsonres = new JSONObject(response) ;
                            int suc = jsonres.getInt("res");
                            if(suc ==1) {
                                JSONArray jsonarray = jsonres.getJSONArray("attendee") ;

                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject temjson = jsonarray.getJSONObject(i) ;
                                    list.add(temjson.getString("attendeeName"));
                                    attendeeId.add(temjson.getInt("attendeeId")) ;
                                }
                                Log.i("test",attendeeId.toString());
                                adapter = new ArrayAdapter(DeletememberActivity.this , android.R.layout.simple_list_item_1, list);
                                setListAdapter(adapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                return map;
            }
        };
        mRequestQueue.add(stringRequest) ;

    }
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletemember);
        mRequestQueue = Volley.newRequestQueue(this) ;
        Intent intent = getIntent();
        managerId = intent.getIntExtra("managerId",-1) ;
        activityId =intent.getIntExtra("activityId",-1) ;
        attendeeId = new ArrayList<Integer>() ;
        list= new ArrayList<String>();
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final int pos = position ;
        AlertDialog.Builder builder = new AlertDialog.Builder(DeletememberActivity.this);
        //    设置Title的图标
        builder.setTitle("删除确认");
        //    设置Content来显示一个信息
        builder.setMessage("确定删除吗？");
        //    设置一个PositiveButton
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String deletememberurl =Constant.baseurl+Constant.deletememeber ;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, deletememberurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("test",response);
                                try {
                                    JSONObject jsonres = new JSONObject(response) ;
                                    int suc = jsonres.getInt("res");


                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                        int temnum = attendeeId.get(pos) ;
                        map.put("attendeeId", Integer.toString(temnum));
                        attendeeId.remove(pos) ;
                        Log.i("test",attendeeId.toString()) ;
                        return map;
                    }
                };
                mRequestQueue.add(stringRequest) ;
                list.remove(pos) ;
                adapter.notifyDataSetChanged();
                setListAdapter(adapter);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}