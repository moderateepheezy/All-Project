package com.renyu.alumni.myview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.renyu.alumni.R;

public class MyLoadingDialog extends AlertDialog {
	
	public static final int MESSAGESTYLE=0;
	public static final int PROGRESSSTYLE=1;
	
	private int style=-1;
	
	private String message="";
	private int imgRes=-1;
	private Context context=null;

	public MyLoadingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public MyLoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public MyLoadingDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyLoadingDialog(Context context, int theme, String message) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.message=message;
		this.context=context;
		this.style=PROGRESSSTYLE;
	}
	
	public MyLoadingDialog(Context context, int theme, String message, int imgRes) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.message=message;
		this.imgRes=imgRes;
		this.context=context;
		this.style=MESSAGESTYLE;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view=LayoutInflater.from(context).inflate(R.layout.alertdialog_message_layout, null);
		ImageView dialog_img=(ImageView) view.findViewById(R.id.dialog_img);
		TextView dialog_message=(TextView) view.findViewById(R.id.dialog_message);
		dialog_message.setText(message);
		ProgressBar dialog_pb=(ProgressBar) view.findViewById(R.id.dialog_pb);
		if(style==MESSAGESTYLE) {
			dialog_img.setImageResource(imgRes);
			dialog_img.setVisibility(View.VISIBLE);
			dialog_message.setVisibility(View.VISIBLE);
			dialog_pb.setVisibility(View.GONE);
		}
		else if(style==PROGRESSSTYLE) {
			dialog_img.setVisibility(View.GONE);
			dialog_message.setVisibility(View.VISIBLE);
			dialog_pb.setVisibility(View.VISIBLE);
		}
		setContentView(view);
	}

}
