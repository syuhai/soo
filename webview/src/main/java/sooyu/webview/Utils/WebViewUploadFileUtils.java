package sooyu.webview.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;


public class WebViewUploadFileUtils {

	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;
	
	static String compressPath = "";
	private static String imagePaths;
	private static Uri cameraUri;
	
	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public final static boolean checkSDcard(Context context) {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			Toast.makeText(context, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
	
	/**
	 * 牌照/选择图片
	 * @param
	 */
	public static  final void selectImage(final Context context) {
		if (!checkSDcard(context))
			return;
		String[] selectPicTypeStr = { "camera","photo" };
		new AlertDialog.Builder(context)
				.setItems(selectPicTypeStr,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								// 相机拍摄
								case 0:
									openCarcme(context);
									break;
								// 手机相册
								case 1:
									chosePic(context);
									break;
								default:
									break;
								}
								compressPath = Environment
										.getExternalStorageDirectory()
										.getPath()
										+ "/fuiou_wmp/temp";
								new File(compressPath).mkdirs();
								compressPath = compressPath + File.separator
										+ "compress.jpg";
							}
						}).show();
	}
	
	/**
	 * 打开照相机
	 */
	private static void openCarcme(Context context) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imagePaths = Environment.getExternalStorageDirectory().getPath()
				+ "/fuiou_wmp/temp/"
				+ (System.currentTimeMillis() + ".jpg");
		// 必须确保文件夹路径存在，否则拍照后无法完成回调
		File vFile = new File(imagePaths);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		cameraUri = Uri.fromFile(vFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		if(context instanceof Activity){
			((Activity)context).startActivityForResult(intent, REQ_CAMERA);
		}
	}

	/**
	 * 拍照结束后
	 */
	public static void afterOpenCamera(Context context) {
		File f = new File(imagePaths);
		addImageGallery(f,context);
		File newFile = FileUtils.compressFile(f.getPath(), compressPath);
		String temp=newFile.getName();
		if(temp != null && (temp.endsWith(".png")||temp.endsWith(".jpg")||temp.endsWith(".apk"))){
			Uri.fromFile(newFile);
		}else{
			Toast.makeText(context, "上传的图片仅支持png或jpg格式,文件仅支持apk", Toast.LENGTH_SHORT).show();
		}
	}

	/** 解决拍照后在相册中找不到的问题 */
	private static void addImageGallery(File file, Context context) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		context.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	/**
	 * 本地相册选择图片
	 */
	private static void chosePic(Context context) {
		FileUtils.delFile(compressPath);
//		Intent i = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(i, REQ_CHOOSE);
        
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
		String IMAGE_UNSPECIFIED = "image/*";
		innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		if(context instanceof Activity){
			((Activity)context).startActivityForResult(wrapperIntent, REQ_CHOOSE);
		}
	}

	/**
	 * 选择照片后结束
	 * 
	 * @param data
	 */
	public static Uri afterChosePic(Intent data, Context context) {

//		// 获取图片的路径：
//		String[] proj = { MediaStore.Images.Media.DATA };
//		// 好像是android多媒体数据库的封装接口，具体的看Android文档
//		Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
//		if(cursor == null ){
//			Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
//			return null;
//		}
//		// 按我个人理解 这个是获得用户选择的图片的索引值
//		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
//		cursor.moveToFirst();
//		// 最后根据索引值获取图片路径
//		String path = cursor.getString(column_index);
		
		Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
		String temp=picturePath;
		temp=temp.toLowerCase();
		if(temp != null && (temp.endsWith(".png")||temp.endsWith(".jpg"))){
			File newFile = FileUtils.compressFile(picturePath, compressPath);
			return Uri.fromFile(newFile);
		}else if(temp != null && temp.endsWith(".apk")){
			File newFile = new File(picturePath);
			return Uri.fromFile(newFile);
		}else{
			Toast.makeText(context, "上传的图片仅支持png或jpg格式,文件仅支持apk", Toast.LENGTH_SHORT).show();
		}
		return null;
	}
}
