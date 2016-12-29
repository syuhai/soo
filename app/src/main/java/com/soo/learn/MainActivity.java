package com.soo.learn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.soo.learn.BaseCommonAdapter.CommBaseAdapter;
import com.soo.learn.BaseCommonAdapter.ViewHolder;
import com.soo.learn.favoranimation.AnimationActivity;
import com.soo.learn.favoranimation.AnimatorActivity;
import com.soo.learn.favoranimation.FavorAnimationActivity;
import com.soo.learn.floatview.FloatVewActivity;
import com.soo.learn.recylerview.RecyclerViewActivity;
import com.soo.learn.util.JumpUtil;
import com.soo.learn.view.ProgressView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList<String> itemList;
    private ProgressView progressView;
    private  ListView pageLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Field[] fileds=MainActivity.class.getFields();
        Field[] filedss=MainActivity.class.getDeclaredFields();
        initDate();
        initView();
    }

    private void initDate() {
        if(itemList==null){
            itemList=new ArrayList<>();
        }
        for(int i=0;i<100;i++){
            if(i==0){
                itemList.add("Recyclerview "+i);
            }else if(i==1){
                itemList.add("floatview "+i);
            }else if(i==2){
                itemList.add("favorAnimation "+i);
            }else if(i==3){
                itemList.add("视图Animation "+i);
            }else if(i==4){
                itemList.add("属性Animation "+i);
            }else{
                itemList.add("adapter  "+i);
            }
        }
    }

    private void initView() {
        progressView= (ProgressView) this.findViewById(R.id.progress);
        pageLv= (ListView) this.findViewById(R.id.pageLv);
        //CommonBaseAdapter pageAdapter=new CommonBaseAdapter(itemList,new PageViewhold(this));
        //PageAdapter pageAdapter=new PageAdapter();
       // pageLv.setAdapter(pageAdapter);
       // pageAdapter.refreshView(itemList);
        pageLv.setAdapter(new CommBaseAdapter<String>(itemList,R.layout.page_item) {
            @Override
            protected void convert(ViewHolder vh, String item) {
               vh.setText(R.id.title,item);
            }
        });
        myHandler.sendEmptyMessageDelayed(1,2000);
        pageLv.setVisibility(View.GONE);
        pageLv.setOnItemClickListener(this);
    }

    public Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressView.setVisibility(View.GONE);
            pageLv.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       if(i==0){
         JumpUtil.JumpToActivity(MainActivity.this,RecyclerViewActivity.class);
       }else if(i==1){
           JumpUtil.JumpToActivity(MainActivity.this,FloatVewActivity.class);
       }else if(i==2){
           JumpUtil.JumpToActivity(MainActivity.this,FavorAnimationActivity.class);
       }else if(i==3){
           JumpUtil.JumpToActivity(MainActivity.this,AnimationActivity.class);
       }else if(i==4){
           JumpUtil.JumpToActivity(MainActivity.this, AnimatorActivity.class);
       }
    }
}
