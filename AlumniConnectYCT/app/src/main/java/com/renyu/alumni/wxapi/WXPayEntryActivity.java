package com.renyu.alumni.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.donation.DonationMessageActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		api=WXAPIFactory.createWXAPI(this, ParamsManager.APP_ID, false);  
		api.handleIntent(getIntent(), this);   
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		if (arg0.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(arg0.errCode==0) {
				CommonUtils.showCustomToast(WXPayEntryActivity.this, "֧���ɹ�", false);
				Intent intent=new Intent(WXPayEntryActivity.this, DonationMessageActivity.class);
				startActivity(intent);
			}
			else if(arg0.errCode==-1) {
				CommonUtils.showCustomToast(WXPayEntryActivity.this, "֧��ʧ��", false);
			}
			else if(arg0.errCode==-2) {
				CommonUtils.showCustomToast(WXPayEntryActivity.this, "֧��ȡ��", false);
			}
			else {
				CommonUtils.showCustomToast(WXPayEntryActivity.this, "֧��ʧ��", false);
			}
			finish();
		}
	}

}
