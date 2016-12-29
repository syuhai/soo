package sooyu.webview.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import sooyu.webview.R;
import sooyu.webview.Utils.ScreenUtils;
import sooyu.webview.Utils.StringUtil;

/**
 * BaseWebViewActivity 上传图片，选择图片方式选择框
 * @author LangShaoPeng
 *
 */
public class YTUploadPicDialog extends Dialog {
	protected YTUploadPicDialog(Context context) {
		super(context); 
	}

	public YTUploadPicDialog(Context context, int theme) {
		super(context, theme);
	}
	

 public static class Builder implements View.OnClickListener {
	   private Context context;
	   private YTUploadPicDialog dialog;
	   /** 标题 **/
	   private String title;
	   private TextView titleTv;
	   /**默认倒计时时间，只有没有按钮的情况下 才会倒计时对话框自动消失**/
	   private volatile int countDownTime=3;
	   private  boolean isAutoDismiss=false;
	   /**倒计时**/
	   private final int DIALOG_DISMISS=10001;
	   private OnKeyListener onKeyListener;
	   
	   /** 单选 **/
	   private CharSequence[] items;
	   private int checkedItem=0;
	   private OnClickListener onClickListener;
	   private ListView itemLv;
	   private Button cancel;
	   private ItemAdapter itemAdapter;
	   
	   private boolean isCancelable=true;
	   
	   public Builder(Context context) {
			this.context = context;
		}
		
		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}
		
		public Builder setCancelable(boolean isCancelable){
			this.isCancelable=isCancelable;
			return this;
		}


		public Builder setCountDownTime(int countDownTime){
			if(countDownTime<=3 && countDownTime>0){
				this.countDownTime=countDownTime;
			}
			return this;
		}
		public Builder setOnKeyListener(OnKeyListener onkeyListener){
			this.onKeyListener=onkeyListener;
			return  this;
		}
		
	   public Builder  setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener){
		   this.items=items;
		   this.checkedItem=checkedItem;
		   this.onClickListener=listener;
		   return this;
	   }
		
		public YTUploadPicDialog getDialog(){
			return dialog;
		}
		
    private YTUploadPicDialog create(){
    	 YTUploadPicDialog mDialog=new YTUploadPicDialog(context, R.style.CDialog);
    	 View rootView= LayoutInflater.from(context).inflate(R.layout.alertdialog_common, null);
    	 initView(rootView);
         configView(mDialog);
         
         if(onKeyListener!=null){
        	 mDialog.setOnKeyListener(onKeyListener);
         }
         mDialog.setContentView(rootView);
         mDialog.setCancelable(isCancelable);
    	 return mDialog;
    }
    
	 private void initView(View rootView) {
		 titleTv=(TextView) rootView.findViewById(R.id.title);
    	 itemLv=(ListView) rootView.findViewById(R.id.itemLv);
    	 itemLv.setDividerHeight(1);
    	 cancel= (Button) rootView.findViewById(R.id.cancel);
    	 cancel.setVisibility(View.VISIBLE);
    	 cancel.setOnClickListener(this);
    	 rootView.findViewById(R.id.topDivideLine).setVisibility(View.VISIBLE);
    	 rootView.findViewById(R.id.content).setVisibility(View.GONE);
    	 rootView.findViewById(R.id.sure).setVisibility(View.GONE);
    	 rootView.findViewById(R.id.buttonView).setVisibility(View.VISIBLE);
    	 rootView.findViewById(R.id.btnDivide).setVisibility(View.GONE);
    	 rootView.findViewById(R.id.divideLine).setVisibility(View.VISIBLE);
	}
		/**
	  * 显示控制 赋值
	  * 
	  */
	private void configView(final YTUploadPicDialog mDialog) {
    	 itemLv.setVisibility(View.GONE);
    	 if(items!=null){
    		 itemLv.setVisibility(View.VISIBLE);
    		 if(!StringUtil.isEmpty(title)){
    			 titleTv.setText(title);
    		 }
    		 itemAdapter=mDialog.new ItemAdapter(items, checkedItem, context);
    		 itemLv.setAdapter(itemAdapter);
    		 itemLv.setVerticalScrollBarEnabled(false);
    		 itemLv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					itemAdapter.setCheckedItem(position);
					onClickListener.onClick(mDialog, position);
				}
			});
    		 
    	 } 
	}
	public YTUploadPicDialog show() {
		 dialog = create();
		 dialog.setCanceledOnTouchOutside(false);
		 dialog.setCancelable(false);
		 dialog.show();
		 Window dialogWindow = dialog.getWindow();
	     WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	     dialogWindow.setGravity(Gravity.CENTER);
	     lp.width = ScreenUtils.getScreenWidth(context)/2;
	     dialogWindow.setAttributes(lp);
	     if(isAutoDismiss){//开启倒计时
	    	 countDownTime();
	     }
		 return dialog;
	}
	private Handler myHandler=new Handler(){
		 public void handleMessage(android.os.Message msg) {
			 switch(msg.what){
			 case DIALOG_DISMISS:
				 if(dialog!=null && dialog.isShowing()){
					 dialog.dismiss();
				 }
				 break;
			 }
		 };
	};
	/**
	 * 开启倒计时
	 */
	private void countDownTime() {
		if(countDownTime<=0){
			return;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(countDownTime>0){
					SystemClock.sleep(1000);
					countDownTime--;
				}
				myHandler.sendEmptyMessage(DIALOG_DISMISS);
			}
		}).start();
		
	}
	
	@Override
	public void onClick(View v) {
		if(v==cancel){
			if(dialog!=null){
				dialog.dismiss();
			}
		}
	}
		
 }
 public class ItemAdapter extends BaseAdapter {

	 private CharSequence[] items;
	 private int checkedItem=0;
	 private Context context;
	 private LayoutInflater inflate;
	public ItemAdapter(CharSequence[] items, int checkedItem, Context context) {
		super();
		this.items = items;
		this.checkedItem = checkedItem;
		this.context = context;
		inflate= LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return items==null?0:items.length;
	}
	
	public void setCheckedItem(int position){
		this.checkedItem=position;
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflate.inflate(R.layout.dialog_choice_item, null);
			vh.choiceRbIv=(ImageView) convertView.findViewById(R.id.choiceRb);
			vh.itemTv=(TextView) convertView.findViewById(R.id.itemTxt);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		if(position==checkedItem){
			vh.choiceRbIv.setImageResource(R.mipmap.shop_cart_rd_selected);
		}else{
			vh.choiceRbIv.setImageResource(R.mipmap.shop_cart_rd_normal);
		}
		vh.itemTv.setText(items[position]);
		return convertView;
	}
	 
 }
  class ViewHolder {
		/** 复选按钮 */
		ImageView choiceRbIv;
		TextView itemTv;
  }
}
