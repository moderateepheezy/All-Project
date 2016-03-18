package com.renyu.alumni.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.renyu.alumni.common.CommonUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	 
	private IWXAPI api;  
	
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		api=WXAPIFactory.createWXAPI(this, "wx437d0c8be3e42d4f", false);  
		api.handleIntent(getIntent(), this);   
	} 
	
	@Override  
	public void onReq(BaseReq arg0) {  
 
	}  
 
	@Override  
	public void onResp(BaseResp resp) {  
		
		switch (resp.errCode) {  
		case BaseResp.ErrCode.ERR_OK:
			CommonUtils.showCustomToast(WXEntryActivity.this, "����ɹ�", true);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			CommonUtils.showCustomToast(WXEntryActivity.this, "����ȡ��", false);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			CommonUtils.showCustomToast(WXEntryActivity.this, "��û��΢�ŷ���Ȩ��", false);
			break;
		default:
			CommonUtils.showCustomToast(WXEntryActivity.this, "δ֪���󣬷���ʧ��", false);
			break;
		}
 
       // TODO ΢�ŷ��� �ɹ�֮����ýӿ�  
       this.finish();  
   }  

}
