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
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupselect);
        mRequestQueue = Volley.newRequestQueue(this) ;

        Intent intent = getIntent();
        final int uid = intent.getIntExtra("uid",-1) ;
        if(uid!= -1) {
            String Groupurl = Constant.baseurl+ Constant.getActivity ;
            Log.i("test",Groupurl) ;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Groupurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                                Log.i("test",response);
                            try {
                                JSONArray jsonres = new JSONArray(response) ;
                                List<String> list = new ArrayList<String>();
                                for(int i = 0 ;i<jsonres.length();i++)
                                {
                                    list.add(jsonres.getString(i)) ;
                                }
                                ArrayAdapter adapter =new ArrayAdapter(GroupselectActivity.this,android.R.layout.simple_list_item_1,list);
                                setListAdapter(adapter);

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
                    map.put("uid", Integer.toString(uid));
                    return map;
                }
            };
            mRequestQueue.add(stringRequest) ;
        }

    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String s =((TextView)v).getText().toString();
        Toast.makeText(this,"提示"+position+s,Toast.LENGTH_LONG).show();
        super.onListItemClick(l, v, position, id);
    }
}