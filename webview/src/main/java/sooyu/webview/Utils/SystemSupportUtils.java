package sooyu.webview.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by ZhangYuanBo on 2016/8/24.
 */
public class SystemSupportUtils {

    public static void callSysGallery(Activity con, int requestCode) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT <19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        String IMAGE_UNSPECIFIED = "image/*";
        intent.setType(IMAGE_UNSPECIFIED); // 查看类型
        Intent wrapperIntent = Intent.createChooser(intent, null);
        con.startActivityForResult(wrapperIntent, requestCode);
    }
    public static String getSysImagePath(Context con, Uri selectedImage){
        if(con==null||selectedImage==null){
            return "";
        }
        String picturePath = ""; //关于Android4.4的图片路径获取，回来的Uri的格式有2种
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(con, selectedImage)) {
            String wholeID = DocumentsContract.getDocumentId(selectedImage);
            String id = wholeID.split(":")[1];
            String[] column = { MediaStore.Images.Media.DATA };
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = con.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[] { id }, null);
            if (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex(column[0]);
                picturePath = cursor.getString(columnIndex);
            }
            cursor.close();
        } else {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = con.getContentResolver().query(selectedImage, projection, null, null, null);
            if (cursor.moveToNext()) {
                int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                picturePath = cursor.getString(column_index);
            }
            cursor.close();
        }
        return picturePath;
    }
}
