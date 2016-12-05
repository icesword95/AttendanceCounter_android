package com.example.zhouzhou.attendancecounter;

import android.app.IntentService;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent ;

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

public class MemberActivity extends ListActivity {
    private RequestQueue mRequestQueue;
    private int managerId;
    private int activityId ;
    @Override
    protected void  onResume(){
        super.onResume();
        String Attendeeurl = Constant.baseurl+ Constant.getAttendee ;
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
                                List<String> list = new ArrayList<String>();
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject temjson = jsonarray.getJSONObject(i) ;
                                    list.add(temjson.getString("attendeeName"));
                                }
                                ArrayAdapter adapter = new ArrayAdapter(MemberActivity.this , android.R.layout.simple_list_item_1, list);
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
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        mRequestQueue = Volley.newRequestQueue(this) ;
        Intent intent = getIntent();
        managerId = intent.getIntExtra("managerId",-1) ;
        activityId =intent.getIntExtra("activityId",-1) ;
        Button btn1 = (Button)findViewById(R.id.button_member_1) ;
        Button btn2 = (Button)findViewById(R.id.button_member_2) ;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this,AddmemberActivity.class) ;
                intent.putExtra("managerId",managerId) ;
                intent.putExtra("activityId",activityId);
                startActivity(intent) ;
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this,DeletememberActivity.class) ;
                intent.putExtra("managerId",managerId) ;
                intent.putExtra("activityId",activityId);
                startActivity(intent) ;
            }
        });

    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String s =((TextView)v).getText().toString();
        super.onListItemClick(l, v, position, id);
    }
}