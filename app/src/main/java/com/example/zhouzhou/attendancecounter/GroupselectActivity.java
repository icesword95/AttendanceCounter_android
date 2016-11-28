package com.example.zhouzhou.attendancecounter;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzhou on 2016/11/1.
 */

public class GroupselectActivity extends ListActivity {
    @Override

    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupselect);
        List<String> ls = new ArrayList<String>();
        ls.add("软件工程") ;
        ls.add("软件工程实习");
        ls.add("ICS小班");
        ArrayAdapter adapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,ls);
        setListAdapter(adapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String s =((TextView)v).getText().toString();
        Toast.makeText(this,"提示"+position+s,Toast.LENGTH_LONG).show();
        super.onListItemClick(l, v, position, id);
    }
}