package com.renyu.alumni.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class AlumniLoginService extends Service {
	
	RemoteCallbackList<LoginCallBack> remotes=null;
	
	Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			int count=remotes.beginBroadcast();
			for(int i=0;i<count;i++) {
				try {
					remotes.getBroadcastItem(i).callBackResult("123");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			remotes.finishBroadcast();
		}
	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		remotes=new RemoteCallbackList<LoginCallBack>();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new AlumniLoginImpl();
	}

	public class AlumniLoginImpl extends AlumniLogin.Stub {

		@Override
		public void getLoginResult() throws RemoteException {
			// TODO Auto-generated method stub
			handler.sendMessageDelayed(new Message(), 3000);
		}

		@Override
		public void registerCallback(LoginCallBack callback)
				throws RemoteException {
			// TODO Auto-generated method stub
			remotes.register(callback);
		}

		@Override
		public void unregisterCallback(LoginCallBack callback)
				throws RemoteException {
			// TODO Auto-generated method stub
			remotes.unregister(callback);
		}

	};
}
