package com.renyu.alumni.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.renyu.alumni.common.ParamsManager;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayActivity extends Activity {
	
	PayReq req;
	IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		req = new PayReq();
		api=WXAPIFactory.createWXAPI(this, ParamsManager.APP_ID, false); 
		
		genPayReq();
		sendPayReq();
		
		finish();
	}
	
	private void genPayReq() {
		req.appId = ParamsManager.APP_ID;
		req.partnerId = getIntent().getExtras().getString("partnerid");
		req.prepayId = getIntent().getExtras().getString("prepayid");
		req.packageValue = getIntent().getExtras().getString("package");
		req.nonceStr = getIntent().getExtras().getString("noncestr");
		req.timeStamp = getIntent().getExtras().getString("timestamp");
		req.sign = getIntent().getExtras().getString("sign");
	}
	
	private void sendPayReq() {
		api.registerApp(ParamsManager.APP_ID);
		api.sendReq(req);
	}
}
