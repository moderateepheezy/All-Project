package com.renyu.alumni.ucenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.renyu.alumni.R;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.ReceiverPrivateLetterModel;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MessageCenterActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	LinearLayout messagecenter_system=null;
	ImageView messagecenter_system_newmessage=null;
	ListView messagecenter_listview=null;
	MessageCenterAdapter adapter=null;
	ArrayList<ReceiverPrivateLetterModel> messageMap=null;
	
	//�Ƿ�����������
	boolean isMessageActionGet=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagecenter);
		
		messageMap=new ArrayList<ReceiverPrivateLetterModel>();
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�ҵ���Ϣ");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		messagecenter_system=(LinearLayout) findViewById(R.id.messagecenter_system);
		messagecenter_system.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				messagecenter_system_newmessage.setVisibility(View.GONE);
				Intent intent=new Intent(MessageCenterActivity.this, SystemMessageActivity.class);
				startActivityForResult(intent, ParamsManager.SYSTEM_MESSAGE_CLEAR);
				ParamsManager.isSystemShow=false;
			}});
		messagecenter_listview=(ListView) findViewById(R.id.messagecenter_listview);
		adapter=new MessageCenterAdapter(MessageCenterActivity.this, messageMap);
		messagecenter_listview.setAdapter(adapter);
		messagecenter_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MessageCenterActivity.this, MessageActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", messageMap.get(position).getUser_id());
				bundle.putString("username", messageMap.get(position).getUsername());
				bundle.putString("avatar_large", messageMap.get(position).getAvatar_large());
				intent.putExtras(bundle);
				startActivity(intent);
				System.out.println(intent.toUri(Intent.URI_INTENT_SCHEME));
			}
		});
		messagecenter_system_newmessage=(ImageView) findViewById(R.id.messagecenter_system_newmessage);
		if(ParamsManager.isSystemShow) {
			messagecenter_system_newmessage.setVisibility(View.VISIBLE);
		}
		
		IntentFilter filter=new IntentFilter();
		filter.addAction("PRIVATELETTER");
		filter.addAction("SYSTEMNOTIFICATION");
		registerReceiver(receiver, filter);
	}
	
	BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals("PRIVATELETTER")&&isMessageActionGet) {
				refreshList();
			}
			else if(intent.getAction().equals("SYSTEMNOTIFICATION")) {
				messagecenter_system_newmessage.setVisibility(View.VISIBLE);
			}
		}
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshList();
		StatService.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		
		Intent intent=new Intent();
		intent.setAction("PRIVATELETTERCLEAR");
		sendBroadcast(intent);
	}
	
	private void refreshList() {
		isMessageActionGet=false;
		messageMap.clear();
		messageMap.addAll(DB.getInstance(this).getProvateMessageList(this));
		adapter.notifyDataSetChanged();
		isMessageActionGet=true;
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&requestCode==ParamsManager.SYSTEM_MESSAGE_CLEAR) {
			messagecenter_system_newmessage.setVisibility(View.GONE);
		}
	}

}
