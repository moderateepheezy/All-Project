package com.renyu.alumni.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

public class CameraBeforeActivity extends Activity {
	
	String filePath="";
	File cameraFile=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState!=null&&savedInstanceState.getString("filePath")!=null) {
			Intent intent=getIntent();
			Bundle bundle=new Bundle();
			bundle.putString("path", savedInstanceState.getString("filePath"));
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			return;
		}
		
		String dirPath=Environment.getExternalStorageDirectory().getPath()+"/Alumni/Thumb";
		cameraFile=new File(dirPath+"/"+System.currentTimeMillis()+".jpg");
		if(!cameraFile.exists()) {
			try {
				cameraFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Uri cameraUrl=Uri.fromFile(cameraFile);
		filePath=cameraFile.getPath();
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraUrl);
		startActivityForResult(intent, ParamsManager.CAMERA);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("filePath", filePath);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1==RESULT_OK&&arg0==ParamsManager.CAMERA) {
			if(cameraFile!=null) {
				filePath=cameraFile.getPath();
				Intent intent=getIntent();
				Bundle bundle=new Bundle();
				bundle.putString("path", filePath);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
		else {
			finish();
			return ;
		}
	}
}
