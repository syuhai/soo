package com.soo.learn;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.soo.learn.util.FileUtils;
import com.soo.learn.util.T;

import java.io.File;
import java.io.IOException;

/**
 * Created by SongYuHai on 2017/1/4.
 */

public class RenameFileActivity extends Activity {
    private TextView createFileTv,renameTv;
    private Button createFileBtn,renameBtn;
    private String path="soo/rename/test.txt";
    private File file;
    private String renamePath="soo/rename/rename.txt";
    private Context mContext;
    String value="的减肥非借即将减肥法金额喀喀喀喀喀喀喀喀喀靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename);
        mContext=this;
        initView();
    }

    private void initView() {
        String aa="123.45";
        String bb="133.55";
        String cc="110.55";
        String dd="123.45";
        createFileTv= (TextView) findViewById(R.id.createFile);
        renameTv= (TextView) findViewById(R.id.renameFile);
        renameTv.setText( aa+" 对比 "+bb+"结果"+"\n"+aa.compareTo(bb)+"\n"+aa+" 对比 "+cc+"结果"+"\n"+aa.compareTo(cc)+"\n"+aa+" 对比 "+dd+"结果"+"\n"+aa.compareTo(dd));
        createFileBtn= (Button) findViewById(R.id.createFileBtn);
        renameBtn= (Button) findViewById(R.id.renameFileBtn);
        createFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file= FileUtils.getDiskDir(mContext,path);
                file.getParentFile().mkdirs();
               if(!file.exists()){
                   try {
                      boolean isSuccess= file.createNewFile();
                       if(isSuccess){
                           createFileTv.setText("创建文件成功："+"\n"+file.toString());
                       }
                   } catch (IOException e) {
                       e.printStackTrace();
                       createFileTv.setText(e.getMessage()+"\n"+file.toString());
                   }
               }else{
                   createFileTv.setText("创建文件成功："+"\n"+file.toString());
               }
            }
        });
        renameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(file!=null){
                   FileUtils.writeFile(file.toString(),value,true);
                   File renameFile=FileUtils.getDiskDir(mContext,renamePath);
                   boolean isDel=false;
                   if(renameFile.exists()){
                        isDel=renameFile.delete();
                   }
                   if(isDel){
                       T.showShort(mContext,"删除成功");
                   }
                   boolean isSuccess=file.renameTo(renameFile);
                   if(isSuccess){
                       renameTv.setText("修改文件成功："+"\n"+renameFile.toString());
                   }else{
                       renameTv.setText("修改文件失败");
                   }
               }
            }
        });
    }
}
