package com.renyu.alumni.qqapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;

public class QQActivity extends Activity {

	public Tencent mTencent;
	public static String mAppid="1101696381";

	private int shareType=Tencent.SHARE_TO_QQ_TYPE_DEFAULT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mTencent=Tencent.createInstance(mAppid, getApplicationContext());
		if(!mTencent.isSupportSSOLogin(QQActivity.this)) {
			CommonUtils.showCustomToast(QQActivity.this, "���Ȱ�װQQ�ֻ��ͻ��ˣ��ٷ���", false);
			finish();
			return ;
		}

		if(getIntent().getExtras().getString("type").equals("qqkj")) {
			shareType=Tencent.SHARE_TO_QQ_NO_SHARE_TYPE;
			sendUrlKJ(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("send_imageUrl"), getIntent().getExtras().getString("title"), getIntent().getExtras().getString("url"));
		}
		else if(getIntent().getExtras().getString("type").equals("qq")) {
			shareType=Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
			sendUrl(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("send_imageUrl"), getIntent().getExtras().getString("title"), getIntent().getExtras().getString("url"));
		}
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // must call mTencent.onActivityResult.
    	if(resultCode==RESULT_CANCELED) {
    		finish();
    		return;
    	}
    }

	public void sendUrl(String text, String imageUrl, String title, String url) {
		final Bundle params=new Bundle();
		params.putString(Tencent.SHARE_TO_QQ_TITLE, title);
        params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(Tencent.SHARE_TO_QQ_SUMMARY, text);
        params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putString(Tencent.SHARE_TO_QQ_APP_NAME, getResources().getString(R.string.app_name));
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(Tencent.SHARE_TO_QQ_EXT_INT, 0x00);
        doShareToQQ(params);
	}

	public void sendUrlKJ(String text, String imageUrl, String title, String url) {
        ArrayList<String> imageUrls=new ArrayList<String>();
        imageUrls.add(imageUrl);
		final Bundle params=new Bundle();
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putString(Tencent.SHARE_TO_QQ_TITLE, title);
        params.putString(Tencent.SHARE_TO_QQ_SUMMARY, text);
        params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, url);
        params.putStringArrayList(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        doShareToQzone(params);
	}

	/**
     * ���첽��ʽ��������QQ
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent=Tencent.createInstance(mAppid, QQActivity.this);        
        final Activity activity=QQActivity.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                tencent.shareToQQ(activity, params, new IUiListener() {

                    @Override
                    public void onComplete(JSONObject response) {
                        // TODO Auto-generated method stub
                    	CommonUtils.showCustomToast(QQActivity.this, "����ɹ�", true);
                        finish();
                    }

                    @Override
                    public void onError(UiError e) {
                    	CommonUtils.showCustomToast(QQActivity.this, "����ʧ��", false);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                    	if(shareType!=Tencent.SHARE_TO_QQ_TYPE_IMAGE){
                    		CommonUtils.showCustomToast(QQActivity.this, "����ȡ��", false);
                    		finish();
                    	}
                    }

                });
            }
        }).start();
    }
    
    /**
     * ���첽��ʽ��������QQ�ռ�
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
        final Activity activity=QQActivity.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
            	mTencent.shareToQzone(activity, params, new IUiListener() {

                    @Override
                    public void onCancel() {
                    	CommonUtils.showCustomToast(QQActivity.this, "����ȡ��", false);
                    	finish();
                    }

                    @Override
                    public void onComplete(JSONObject response) {
                        // TODO Auto-generated method stub
                    	CommonUtils.showCustomToast(QQActivity.this, "����ɹ�", true);
                    	System.out.println("OK");
                    	finish();
                    }

                    @Override
                    public void onError(UiError e) {
                        // TODO Auto-generated method stub
                    	CommonUtils.showCustomToast(QQActivity.this, "����ʧ��"+e.errorMessage, false);
                    	finish();
                    }

                });
            }
        }).start();
    }
}