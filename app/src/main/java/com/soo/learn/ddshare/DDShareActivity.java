package com.soo.learn.ddshare;

import android.app.Activity;
import android.os.Bundle;

import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory;
import com.android.dingtalk.share.ddsharemodule.IDDAPIEventHandler;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.android.dingtalk.share.ddsharemodule.message.BaseReq;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.soo.learn.Constant;
import com.soo.learn.util.L;

/**
 * Created by SongYuHai on 2017/1/4.
 */

public class DDShareActivity extends Activity implements IDDAPIEventHandler {
    private IDDShareApi mIDDShareApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("lzc" ,"onCreate==========>");
        try {
            //activity的export为true，try起来，防止第三方拒绝服务攻击
            mIDDShareApi = DDShareApiFactory.createDDShareApi(this, Constant.APP_ID, false);
            mIDDShareApi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
            L.d("lzc" , "e===========>"+e.toString());
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        L.d("lzc", "onReq=============>" );
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int errCode = baseResp.mErrCode;
        L.d("lzc", "errCode=============>" + errCode);
        L.d("lzc", "errCode=============>" + baseResp.mTransaction);
        switch (errCode){
            case BaseResp.ErrCode.ERR_OK:
                break;
        }
        finish();
    }
}
