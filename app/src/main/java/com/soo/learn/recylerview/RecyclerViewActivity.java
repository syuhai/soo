package com.soo.learn.recylerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.soo.learn.R;
import com.soo.learn.util.T;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SongYuHai on 2016/11/28.
 */
public class RecyclerViewActivity extends Activity implements RecyclerAdapter.IOnItemClickListener {
    private RecyclerView mRecyclerView;
    private ArrayList<String> mDatas=new ArrayList<>();
    private HashMap<Integer,String> map=new HashMap<>();
    private SparseArray<String>  mSparseArray=new SparseArray<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recylerview_activity);
        initView();
    }


    private synchronized void initView() {
        initTestData();
        mRecyclerView= (RecyclerView) this.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
       // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        RecyclerAdapter adapter=new RecyclerAdapter<String,BaseViewHolder>(mDatas,R.layout.recycler_item_list){

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.id_item_list_title,item);
            }
        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter.setOnItemClickListener(this);
    }

    private void initTestData() {
        for(int i='a';i<='z';i++){
            mDatas.add((char) i+"            ee");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        T.showShort(RecyclerViewActivity.this,""+position);
    }
}
