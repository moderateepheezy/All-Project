package com.renyu.alumni;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.ReceiverPrivateLetterModel;
import com.renyu.alumni.ucenter.MessageActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ScreenOnActivity extends Activity {
	
	ListView screen_on_listview=null;
	SimpleAdapter adapter=null;
	
	PowerManager.WakeLock mWakelock=null;
	
	ArrayList<HashMap<String, Object>> objs=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_screenon);
		
		objs=new ArrayList<HashMap<String, Object>>();		
		
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.renyu.alumni.receiver.ScreenOnReceiver2");
		registerReceiver(receiver, filter);
		
		init();
	}
	
	private void init() {
		screen_on_listview=(ListView) findViewById(R.id.screen_on_listview);
		screen_on_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);  
				KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");  
				keyguardLock.disableKeyguard();
				
				Intent intent=new Intent(ScreenOnActivity.this, MessageActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", Integer.parseInt(objs.get(position).get("user_id").toString()));
				bundle.putString("username", objs.get(position).get("username").toString());
				bundle.putString("avatar_large", objs.get(position).get("avatar_large").toString());
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		adapter=new SimpleAdapter(ScreenOnActivity.this, objs, R.layout.adapter_screenon, new String[]{"desp"}, new int[]{R.id.adapter_screenon_text});
		screen_on_listview.setAdapter(adapter);
		loadData();
	}
	
	private void loadData() {
		objs.clear();
		ArrayList<ReceiverPrivateLetterModel> models=DB.getInstance(ScreenOnActivity.this).getProvateMessageList(ScreenOnActivity.this);
		for(int i=0;i<models.size();i++) {
			HashMap<String, Object> map=new HashMap<String, Object>();
			if(models.get(i).getNoReadCount()>0) {
				map.put("desp", models.get(i).getUsername()+":"+models.get(i).getContent());
				map.put("user_id", models.get(i).getUser_id());
				map.put("username", models.get(i).getUsername());
				map.put("avatar_large", models.get(i).getAvatar_large());
				objs.add(map);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	BroadcastReceiver receiver=new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals("com.renyu.alumni.receiver.ScreenOnReceiver2")) {
				//��������
				PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
				mWakelock=pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "alumni");
				mWakelock.setReferenceCounted(false);
			    if (mWakelock!=null) {
			    	mWakelock.acquire(5000);
			    }
				loadData();
			}
		}
		
	};
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		if (mWakelock != null) {
			mWakelock.release();
		}
	};
}
