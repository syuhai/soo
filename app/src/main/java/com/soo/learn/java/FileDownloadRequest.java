/*
 * Copyright (C) 2015 Vince Styling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*

package com.soo.learn.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.soo.learn.SApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

*/
/**
 *//*

public class FileDownloadRequest {
    private File mStoreFile;
    private File mTemporaryFile;
    private HttpClient mHttpCient = null;
    private HttpGet mHttpGet= null;
    private String mRequestUrl;
    public static  final int MSG_REFRESH=1001;
    public static final String KEY_FILESIZE="filesize";
    public static final String KEY_DOWNLOADSIZE="downloadsize";
    public boolean isExitApp=false;
    public FileDownloadRequest(String storeFilePath, String url) {
        this(new File(storeFilePath), url);
    }

    public FileDownloadRequest(File storeFile, String url) {
        mStoreFile = storeFile;
        mTemporaryFile = new File(storeFile + ".tmp");
        try {
            mTemporaryFile.getParentFile().mkdirs();
            if(!mTemporaryFile.exists()){
                mTemporaryFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRequestUrl=url;
    }

    */
/**
     * In this method, we got the Content-Length, with the TemporaryFile length,
     * we can calculate the actually size of the whole file, if TemporaryFile not exists,
     * we'll take the store file length then compare to actually size, and if equals,
     * we consider this download was already done.
     * We used {@link RandomAccessFile} to continue download, when download success,
     * the TemporaryFile will be rename to StoreFile.
     * @param  isAllow3G
     *      is can download in 3g env. if false, just download in wifi
     *//*

    public File startDownloadFile(boolean isAllow3G){
         return startDownloadFile(isAllow3G,null);
    }

    public File startDownloadFile(boolean isAllow3G, Handler handler){
        InputStream in = null;
        HttpEntity entity =null;
        RandomAccessFile tmpFileRaf =null;
        boolean isSuccess = true;
        try {
            mHttpCient = new DefaultHttpClient();
            mHttpGet = new HttpGet(mRequestUrl);
            mHttpGet.addHeader("Range", "bytes=" + mTemporaryFile.length() + "-");
            //mHttpGet.addHeader("Accept-Encoding", "identity");
            HttpResponse response = mHttpCient.execute(mHttpGet);
            int respStatus = response.getStatusLine().getStatusCode();
            if(200==respStatus || 206 == respStatus){
                // Content-Length might be negative when use HttpURLConnection because it default header Accept-Encoding is gzip,
                // we can force set the Accept-Encoding as identity in prepare() method to slove this problem but also disable gzip response.
                entity = response.getEntity();
                long fileSize = entity.getContentLength();
                long downloadedSize = mTemporaryFile.length();
                boolean isSupportRange = isSupportRange(response);
                if (isSupportRange) {
                    fileSize += downloadedSize;
                    // Verify the Content-Range Header, to ensure temporary file is part of the whole file.
                    // Sometime, temporary file length add response content-length might greater than actual file length,
                    // in this situation, we consider the temporary file is invalid, then throw an exception.
                    String realRangeValue = getHeader(response, "Content-Range");
                    // response Content-Range may be null when "Range=bytes=0-"
                    if (!TextUtils.isEmpty(realRangeValue)) {
                        String assumeRangeValue = "bytes " + downloadedSize + "-" + (fileSize - 1);
                        if (TextUtils.indexOf(realRangeValue, assumeRangeValue) == -1) {
                            return null;
                        }
                    }
                }
                handlerSend(fileSize,downloadedSize,handler);
                L.d("downloaded begin");
                // Compare the store file size(after download successes have) to server-side Content-Length.
                // temporary file will rename to store file after download success, so we compare the
                // Content-Length to ensure this request already download or not.
                if (fileSize > 0 && mStoreFile.length() == fileSize) {
                    // Rename the store file to temporary file, mock the download success. ^_^
                    //mStoreFile.renameTo(mTemporaryFile);
                    L.d("downloaded has finished"+fileSize);
                    return mStoreFile;
                }

                tmpFileRaf = new RandomAccessFile(mTemporaryFile, "rw");

                // If server-side support range download, we seek to last point of the temporary file.
                if (isSupportRange) {
                    tmpFileRaf.seek(downloadedSize);
                } else {
                    // If not, truncate the temporary file then start download from beginning.
                    tmpFileRaf.setLength(0);
                    downloadedSize = 0;
                }

                in = entity.getContent();
                // Determine the response gzip encoding, support for HttpClientStack download.
                if (isGzipContent(response) && !(in instanceof GZIPInputStream)) {
                    in = new GZIPInputStream(in);
                }
                byte[] buffer = new byte[128 * 1024]; // 6K buffer
                int offset;

                while ((offset = in.read(buffer)) != -1) {
                    //当前环境为wifi, 或者允许3g下载时,开始写文件
                    if( (isAllow3G ||
                            NetUtils.isWifi(SApplication.getInstance())) && !isExitApp ){
                        tmpFileRaf.write(buffer, 0, offset);
                    }else{
                        isSuccess=false;
                        break;
                    }
                    downloadedSize += offset;
                    handlerSend(fileSize,downloadedSize,handler);
                    L.d("downloadedSize="+downloadedSize);
                }
                //下载完毕
                if(isSuccess){
                    if (mTemporaryFile.canRead() && mTemporaryFile.length() > 0) {
                        boolean isExist = mStoreFile.exists();
                        if( isExist ){
                            //如果文件存在, 先删除
                            mStoreFile.delete();
                        }
                        boolean success = mTemporaryFile.renameTo(mStoreFile);
                        if (success) {
                            return mStoreFile;
                        }
                    }
                }
            }
        } catch (Exception e) {
            L.e(e);
            e.printStackTrace();
        }finally {
            try {
                // Close the InputStream
                if (in != null) in.close();
            } catch (Exception e) {
                L.e(e);
                e.printStackTrace();
            }
           */
/*if(null!=entity){//耗时30s+ ,屏蔽掉
                try {
                    // release the resources by "consuming the content".
                    //entity.consumeContent();
                } catch (Exception e) {
                    YTLog.e(e);
                    e.printStackTrace();
                }
            }*//*

            if(null!=tmpFileRaf){
                try {
                    tmpFileRaf.close();
                } catch (IOException e) {
                    L.e(e);
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    */
/**
     * 用于实时显示下载进度
     * @param fileSize
     * @param downloadSize
     * @param handler
     *//*

    private void handlerSend(long fileSize,long downloadSize,Handler handler){
        if(handler==null){
            return;
        }
        Message msg=handler.obtainMessage();
        msg.what= MSG_REFRESH;
        Bundle bundle=new Bundle();
        bundle.putLong(KEY_FILESIZE,fileSize);
        bundle.putLong(KEY_DOWNLOADSIZE,downloadSize);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public static String getHeader(HttpResponse response, String key) {
        Header header = response.getFirstHeader(key);
        return header == null ? null : header.getValue();
    }

    public static boolean isSupportRange(HttpResponse response) {
        if (TextUtils.equals(getHeader(response, "Accept-Ranges"), "bytes")) {
            return true;
        }
        String value = getHeader(response, "Content-Range");
        return value != null && value.startsWith("bytes");
    }

    public static boolean isGzipContent(HttpResponse response) {
        return TextUtils.equals(getHeader(response, "Content-Encoding"), "gzip");
    }
}
*/
