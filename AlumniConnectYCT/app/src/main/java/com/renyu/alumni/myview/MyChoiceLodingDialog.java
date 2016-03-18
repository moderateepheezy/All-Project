package com.renyu.alumni.myview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.renyu.alumni.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyChoiceLodingDialog extends AlertDialog {
	
	String[] strArray;
	int[] imageArray;
	Context context=null;
	OnDialogItemClickListener lis=null;

	protected MyChoiceLodingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	protected MyChoiceLodingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	protected MyChoiceLodingDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyChoiceLodingDialog(Context context, int theme, String[] strArray, int[] imageArray , OnDialogItemClickListener lis) {
		super(context, theme);
		this.strArray=strArray;
		this.context=context;
		this.imageArray=imageArray;
		this.lis=lis;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view=LayoutInflater.from(context).inflate(R.layout.alertdialog_menu_layout, null);
		ArrayList<HashMap<String, Object>> lists=new ArrayList<HashMap<String, Object>>();
		SimpleAdapter adapter=null;
		if(imageArray!=null) {
			for(int i=0;i<strArray.length;i++) {
				HashMap<String, Object> map=new HashMap<String, Object>();
				map.put("image", imageArray[i]);
				map.put("text", strArray[i]);
				lists.add(map);
			}
			adapter=new SimpleAdapter(context, lists, R.layout.adapter_alertdialog_menu, new String[]{"image", "text"}, new int[]{R.id.alertdialog_image, R.id.alertdialog_text});			
		}
		else {
			for(int i=0;i<strArray.length;i++) {
				HashMap<String, Object> map=new HashMap<String, Object>();
				map.put("text", strArray[i]);
				lists.add(map);
			}
			adapter=new SimpleAdapter(context, lists, R.layout.adapter_alertdialog_menu_onlytext, new String[]{"text"}, new int[]{R.id.alertdialog_text});			
		}
		ListView dialog_list=(ListView) view.findViewById(R.id.dialog_list);
		dialog_list.setAdapter(adapter);
		dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				lis.click(position);
				dismiss();
			}
		});
		TextView dialog_cancel_text=(TextView) view.findViewById(R.id.dialog_cancel_text);
		dialog_cancel_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}});
		setContentView(view);
	}
	
	
	public interface OnDialogItemClickListener {
		public void click(int pos);
	}

}
