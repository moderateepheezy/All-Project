package com.renyu.alumni.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final IWXAPI api=WXAPIFactory.createWXAPI(context, "wx437d0c8be3e42d4f");

		// ����appע�ᵽ΢��
		api.registerApp("wx437d0c8be3e42d4f");
	}

}
