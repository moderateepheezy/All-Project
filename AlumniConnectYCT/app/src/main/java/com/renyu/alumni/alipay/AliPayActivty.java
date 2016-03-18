package com.renyu.alumni.alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;

import com.alipay.sdk.app.PayTask;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.donation.DonationMessageActivity;

public class AliPayActivty extends Activity {
	
	static int SDK_PAY_FLAG = 1;
	static int SDK_CHECK_FLAG = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		check();
	}
	
	private void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask payTask = new PayTask(AliPayActivty.this);
				// ���ò�ѯ�ӿڣ���ȡ��ѯ���
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist?"OK":"FAIL";
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}
	
	private void pay() {
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask(AliPayActivty.this);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(getIntent().getExtras().getString("payInfo"));

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==SDK_PAY_FLAG) {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();
				System.out.println("resultInfo:"+resultStatus);

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					CommonUtils.showCustomToast(AliPayActivty.this, "֧���ɹ�", false);
					Intent intent=new Intent(AliPayActivty.this, DonationMessageActivity.class);
					startActivity(intent);
				} 
				else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						CommonUtils.showCustomToast(AliPayActivty.this, "֧�����ȷ����", false);

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						CommonUtils.showCustomToast(AliPayActivty.this, "֧��ʧ��", false);
					}
				}
				finish();
			}
			else if(msg.what==SDK_CHECK_FLAG) {
				if(String.valueOf(msg.obj).equals("OK")) {
					pay();
				}
				else {
					CommonUtils.showCustomToast(AliPayActivty.this, "�����ֻ�����֧������֤�˻�������֧�����ͻ�������Ӻ�����", false);
				}
			}
		}
	};
}
