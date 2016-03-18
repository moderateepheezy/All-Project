package com.renyu.alumni.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.renyu.alumni.ScreenOnActivity;

public class ScreenOnReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("com.renyu.alumni.receiver.ScreenOnReceiver")) {
			Intent intent_=new Intent(context, ScreenOnActivity.class); 
			intent_.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);          
			context.startActivity(intent_);  
		}
	}

}
