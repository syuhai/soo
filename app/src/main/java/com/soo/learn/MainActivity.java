package com.soo.learn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.android.dingtalk.share.ddsharemodule.message.DDMediaMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDWebpageMessage;
import com.android.dingtalk.share.ddsharemodule.message.SendMessageToDD;
import com.soo.learn.dagger2.DaggerActivity;
import com.soo.learn.favoranimation.AnimationActivity;
import com.soo.learn.favoranimation.AnimatorActivity;
import com.soo.learn.favoranimation.FavorAnimationActivity;
import com.soo.learn.floatview.FloatVewActivity;
import com.soo.learn.recylerview.RecyclerViewActivity;
import com.soo.learn.util.JumpUtil;
import com.soo.learn.util.T;
import com.soo.learn.view.ProgressView;
import com.soothe.baseadapter.CommBaseAdapter;
import com.soothe.baseadapter.ViewHolder;

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
            }else if(i==5){
                itemList.add("分享到钉钉 "+i);
            }else if(i==6){
                itemList.add("自定义view "+i);
            }else if(i==7){
                itemList.add("文件重命名测试 "+i);
            }else if(i==8){
                itemList.add("注解dagger2 "+i);
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
       }else if(i==5){
           shareToDingding();
       } else if(i==6){
           JumpUtil.JumpToActivity(MainActivity.this,CustomActivity.class);
       }else if(i==7){
           JumpUtil.JumpToActivity(MainActivity.this,RenameFileActivity.class);
       }else if(i==8){
           JumpUtil.JumpToActivity(MainActivity.this,DaggerActivity.class);
       }
    }

    private void shareToDingding() {
        IDDShareApi iddShareApi = DDShareApiFactory.createDDShareApi(this, Constant.APP_ID, true);
        boolean isInstalled = iddShareApi.isDDAppInstalled();
        if(!isInstalled){
            T.showShort(this,"请先安装钉钉");
            return;
        }
        boolean isSupport = iddShareApi.isDDSupportAPI();
        if(!isSupport){
            T.showShort(this,"所钉钉版本不支持分享，请先升级");
            return;
        }
        //初始化一个DDWebpageMessage并填充网页链接地址
        DDWebpageMessage webPageObject = new DDWebpageMessage();
        webPageObject.mUrl = "https://www.hao123.com/?tn=97974706_hao_pg";

        //构造一个DDMediaMessage对象
        DDMediaMessage webMessage = new DDMediaMessage();
        webMessage.mMediaObject = webPageObject;

        //填充网页分享必需参数，开发者需按照自己的数据进行填充
        webMessage.mTitle = "测试标题";
        webMessage.mContent = "能分享了啊 ，赶快试试";
        webMessage.mThumbUrl = "http://d.ifengimg.com/mw978_mh598/p3.ifengimg.com/cmpp/2017/01/04/10/f6793def-2a1d-43e4-a4e1-23705185ee8c_size511_w1000_h640.jpg";
        // 网页分享的缩略图也可以使用bitmap形式传输
        // webMessage.setThumbImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        //构造一个Req
        SendMessageToDD.Req webReq = new SendMessageToDD.Req();
        webReq.mMediaMessage = webMessage;
//      webReq.transaction = buildTransaction("webpage");

        //调用api接口发送消息到支付宝
        iddShareApi.sendReq(webReq);
    }
}
