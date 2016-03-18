package com.renyu.alumni.sinaweiboapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.renyu.alumni.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.exception.WeiboShareException;
import com.sina.weibo.sdk.utils.Utility;

public class WBMainActivity extends Activity implements IWeiboHandler.Response {

	/** ΢�� Web ��Ȩ�࣬�ṩ��½�ȹ���  */
    private WeiboAuth mWeiboAuth;
    /** ��װ�� "access_token"��"expires_in"��"refresh_token"�����ṩ�����ǵĹ�����  */
    private Oauth2AccessToken mAccessToken;
    /** ע�⣺SsoHandler ���� SDK ֧�� SSO ʱ��Ч */
    private SsoHandler mSsoHandler;
    
    public static final String app_key="3115426769";
    public static final String scope= 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    
    /** ΢��΢������ӿ�ʵ�� */
    private IWeiboShareAPI  mWeiboShareAPI=null;
    
    Bitmap bmp=null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	// ����΢��ʵ��
        mWeiboAuth=new WeiboAuth(this, app_key, "https://api.weibo.com/oauth2/default.html", scope);
        
        // �� SharedPreferences �ж�ȡ�ϴ��ѱ���� AccessToken ����Ϣ��
        // ��һ��������Ӧ�ã�AccessToken ������
        mAccessToken=readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
        	sendBefore();
        }
        else {
        	mSsoHandler=new SsoHandler(WBMainActivity.this, mWeiboAuth);
            mSsoHandler.authorize(new AuthListener());
        }
    }
    
    /**
     * ΢����֤��Ȩ�ص��ࡣ
     * 1. SSO ��Ȩʱ����Ҫ�� {@link #onActivityResult} �е��� {@link SsoHandler#authorizeCallBack} ��
     *    �ûص��Żᱻִ�С�
     * 2. �� SSO ��Ȩʱ������Ȩ�����󣬸ûص��ͻᱻִ�С�
     * ����Ȩ�ɹ����뱣��� access_token��expires_in��uid ����Ϣ�� SharedPreferences �С�
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // �� Bundle �н��� Token
            mAccessToken=Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // ���� Token �� SharedPreferences
            	writeAccessToken(WBMainActivity.this, mAccessToken);
            	Toast.makeText(WBMainActivity.this, "��Ȩ�ɹ�", 3000);
            	sendBefore();
            } else {
                // ����ע���Ӧ�ó���ǩ������ȷʱ���ͻ��յ� Code����ȷ��ǩ����ȷ
                String code=values.getString("code");
                Toast.makeText(WBMainActivity.this, "��Ȩʧ��\nObtained the code: " + code, 3000).show();
                finish();
            }
        }

        @Override
        public void onCancel() {
        	Toast.makeText(WBMainActivity.this, "ȡ����Ȩ", 3000).show();
        	finish();
        }

        @Override
        public void onWeiboException(WeiboException e) {
        	Toast.makeText(WBMainActivity.this, "Auth exception : " + e.getMessage(), 3000).show();
        	finish();
        }
    }
    
    /**
     * ���� Token ���� SharedPreferences��
     * 
     * @param context Ӧ�ó��������Ļ���
     * @param token   Token ����
     */
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        SharedPreferences pref=context.getSharedPreferences("com_weibo_sdk_android", Context.MODE_APPEND);
        Editor editor=pref.edit();
        editor.putString("uid", token.getUid());
        editor.putString("access_token", token.getToken());
        editor.putLong("expires_in", token.getExpiresTime());
        editor.commit();
    }
    
    /**
     * �� SharedPreferences ��ȡ Token ��Ϣ��
     * 
     * @param context Ӧ�ó��������Ļ���
     * 
     * @return ���� Token ����
     */
    public static Oauth2AccessToken readAccessToken(Context context) {
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences("com_weibo_sdk_android", Context.MODE_APPEND);
        token.setUid(pref.getString("uid", ""));
        token.setToken(pref.getString("access_token", ""));
        token.setExpiresTime(pref.getLong("expires_in", 0));
        return token;
    }
    
    /**
     * �� SSO ��Ȩ Activity �˳�ʱ���ú��������á�
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO ��Ȩ�ص�
        // ��Ҫ������ SSO ��½�� Activity ������д onActivityResult
        if (mSsoHandler!=null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    
    private void sendBefore() {
    	// ����΢������ӿ�ʵ��
        mWeiboShareAPI=WeiboShareSDK.createWeiboAPI(WBMainActivity.this, app_key);
        
        // ���δ��װ΢���ͻ��ˣ���������΢����Ӧ�Ļص�
        if(!mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                @Override
                public void onCancel() {
                	Toast.makeText(WBMainActivity.this, "ȡ������", 3000).show();
                }
            });
        }
        
        try {
            // ���΢���ͻ��˻����Ƿ����������δ��װ΢���������Ի���ѯ���û�����΢���ͻ���
            if (mWeiboShareAPI.checkEnvironment(true)) {
                
                // ע�������Ӧ�� ��΢���ͻ����У�ע��ɹ����Ӧ�ý���ʾ��΢����Ӧ���б��С�
                // ���ø��������ɷ���Ȩ����Ҫ�������룬������鿴 Demo ��ʾ
                mWeiboShareAPI.registerApp();
                
                sendMessage();
            }
        } catch (WeiboShareException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ������Ӧ�÷���������Ϣ��΢��������΢��������档
     * @see {@link #sendMultiMessage} ���� {@link #sendSingleMessage}
     */
    private void sendMessage() {        
        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            int supportApi=mWeiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi>=10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                sendMultiMessage();
            } else {
                sendSingleMessage();
            }
        } else {
        	Toast.makeText(WBMainActivity.this, "΢���ͻ��˲�֧��SDK�����΢���ͻ���δ��װ��΢���ͻ����Ƿǹٷ��汾��", 3000).show();
        }
    }
    
    /**
     * ������Ӧ�÷���������Ϣ��΢��������΢��������档
     * ע�⣺�� {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 ʱ��֧��ͬʱ���������Ϣ��
     * ͬʱ���Է����ı���ͼƬ�Լ�����ý����Դ����ҳ�����֡���Ƶ�������е�һ�֣���
     * 
     * @param hasText    ����������Ƿ����ı�
     * @param hasImage   ����������Ƿ���ͼƬ
     * @param hasWebpage ����������Ƿ�����ҳ
     * @param hasMusic   ����������Ƿ�������
     * @param hasVideo   ����������Ƿ�����Ƶ
     * @param hasVoice   ����������Ƿ�������
     */
    private void sendMultiMessage() {        
        // 1. ��ʼ��΢���ķ�����Ϣ
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject=getTextObj(getIntent().getExtras().getString("title")+" "+getIntent().getExtras().getString("text"));
        weiboMessage.imageObject=getImageObj(getIntent().getExtras().getString("send_imageUrl")!=null?getIntent().getExtras().getString("send_imageUrl"):"");
        weiboMessage.mediaObject=getWebpageObj();
        
        // 2. ��ʼ���ӵ�������΢������Ϣ����
        SendMultiMessageToWeiboRequest request=new SendMultiMessageToWeiboRequest();
        // ��transactionΨһ��ʶһ������
        request.transaction=String.valueOf(System.currentTimeMillis());
        request.multiMessage=weiboMessage;
        
        // 3. ����������Ϣ��΢��������΢���������
        mWeiboShareAPI.sendRequest(request);
    }

    /**
     * ������Ӧ�÷���������Ϣ��΢��������΢��������档
     * ��{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 ʱ��ֻ֧�ַ�������Ϣ����
     * �ı���ͼƬ����ҳ�����֡���Ƶ�е�һ�֣���֧��Voice��Ϣ��
     */
    private void sendSingleMessage() {
        
        // 1. ��ʼ��΢���ķ�����Ϣ
        // �û����Է����ı���ͼƬ����ҳ�����֡���Ƶ�е�һ��
        WeiboMessage weiboMessage = new WeiboMessage();
        weiboMessage.mediaObject = getWebpageObj();
        
        // 2. ��ʼ���ӵ�������΢������Ϣ����
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // ��transactionΨһ��ʶһ������
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        
        // 3. ����������Ϣ��΢��������΢���������
        mWeiboShareAPI.sendRequest(request);
    }
    
    /**
     * �����ı���Ϣ����
     * 
     * @return �ı���Ϣ����
     */
    private TextObject getTextObj(String text) {
        TextObject textObject=new TextObject();
        textObject.text=text;
        return textObject;
    }
    
    /**
     * ����ͼƬ��Ϣ����
     * 
     * @return ͼƬ��Ϣ����
     */
    private ImageObject getImageObj(String image) {
        ImageObject imageObject=new ImageObject();
        // ���� Bitmap ���͵�ͼƬ����Ƶ������
        bmp=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        imageObject.setImageObject(bmp);
        return imageObject;
    }

    
    /**
     * ������ý�壨��ҳ����Ϣ����
     * 
     * @return ��ý�壨��ҳ����Ϣ����
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject=new WebpageObject();
        mediaObject.identify=Utility.generateGUID();
        mediaObject.title=getIntent().getExtras().getString("title");
        mediaObject.description=getIntent().getExtras().getString("text");        
        mediaObject.setThumbImage(bmp);
        mediaObject.actionUrl=getIntent().getExtras().getString("url");
        mediaObject.defaultText=getIntent().getExtras().getString("defaultText");
        return mediaObject;
    }
    
    /**
     * @see {@link Activity#onNewIntent}
     */	
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        // �ӵ�ǰӦ�û���΢�������з���󣬷��ص���ǰӦ��ʱ����Ҫ�ڴ˴����øú���
        // ������΢���ͻ��˷��ص����ݣ�ִ�гɹ������� true��������
        // {@link IWeiboHandler.Response#onResponse}��ʧ�ܷ��� false�������������ص�
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    /**
     * ����΢�ͻ��˲���������ݡ�
     * ��΢���ͻ��˻���ǰӦ�ò����з���ʱ���÷��������á�
     * 
     * @param baseRequest ΢���������ݶ���
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
        case WBConstants.ErrorCode.ERR_OK:
        	Toast.makeText(WBMainActivity.this, "����ɹ�", 3000).show();
            break;
        case WBConstants.ErrorCode.ERR_CANCEL:
        	Toast.makeText(WBMainActivity.this, "ȡ������", 3000).show();
            break;
        case WBConstants.ErrorCode.ERR_FAIL:
        	Toast.makeText(WBMainActivity.this, "����ʧ�� "+"Error Message:"+baseResp.errMsg, 3000).show();
            break;
        }
        if(bmp!=null&&!bmp.isRecycled()) {
        	bmp.recycle();
        	bmp=null;
        }
        finish();
    }
}
