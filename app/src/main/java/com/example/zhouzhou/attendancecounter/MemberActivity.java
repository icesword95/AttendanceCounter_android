package com.example.zhouzhou.attendancecounter;

import android.app.IntentService;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent ;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzhou on 2016/11/1.
 */

public class MemberActivity extends ListActivity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        List<String> ls = new ArrayList<String>();
        ls.add("汪思学") ;
        ls.add("周周");
        ls.add("万炽洋");
        ArrayAdapter adapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,ls);
        setListAdapter(adapter);

        Button btn1 = (Button)findViewById(R.id.button_member_1) ;
        Button btn2 = (Button)findViewById(R.id.button_member_2) ;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this,AddmemberActivity.class) ;
                startActivity(intent) ;
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this,DeletememberActivity.class) ;
                startActivity(intent) ;
            }
        });

    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String s =((TextView)v).getText().toString();
        Toast.makeText(this,"提示"+position+s,Toast.LENGTH_LONG).show();
        super.onListItemClick(l, v, position, id);
    }
}