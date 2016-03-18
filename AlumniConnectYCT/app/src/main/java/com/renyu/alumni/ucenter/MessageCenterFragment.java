package com.renyu.alumni.ucenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.login.LoginActivity;
import com.renyu.alumni.main2.MainActivity2;
import com.renyu.alumni.model.ReceiverPrivateLetterModel;
import com.renyu.alumni.model.UserModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class MessageCenterFragment extends Fragment {
		
	ListView fragment_messagecenter_listview=null;
	MessageCenterAdapter adapter=null;
	RelativeLayout fragment_messagecenter_login_layout=null;
	
	ArrayList<ReceiverPrivateLetterModel> messageMap=null;
	
	BitmapUtils bitmapUtils=null;
	BitmapDisplayConfig config=null;
		
	//�Ƿ�����������
	boolean isMessageActionGet=false;
	
	View view=null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		bitmapUtils=BitmapHelp.getBitmapUtils(getActivity());
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getActivity().getResources().getDrawable(R.drawable.ic_people_open));
		config.setLoadingDrawable(getActivity().getResources().getDrawable(R.drawable.ic_people_open));	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null) {
			messageMap=new ArrayList<ReceiverPrivateLetterModel>();
			view=inflater.inflate(R.layout.fragment_messagecenter, container, false);
			
			fragment_messagecenter_login_layout=(RelativeLayout) view.findViewById(R.id.fragment_messagecenter_login_layout);
			fragment_messagecenter_login_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
				}
			});
			
			fragment_messagecenter_listview=(ListView) view.findViewById(R.id.fragment_messagecenter_listview);
			adapter=new MessageCenterAdapter(getActivity(), messageMap);
			fragment_messagecenter_listview.setAdapter(adapter);
			fragment_messagecenter_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), MessageActivity.class);
					Bundle bundle=new Bundle();
					bundle.putInt("user_id", messageMap.get(position).getUser_id());
					bundle.putString("username", messageMap.get(position).getUsername());
					bundle.putString("avatar_large", messageMap.get(position).getAvatar_large());
					intent.putExtras(bundle);
					startActivity(intent);
					System.out.println(intent.toUri(Intent.URI_INTENT_SCHEME));
				}
			});
		}
		
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null) {
			parent.removeView(view);
		}

		((MainActivity2) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		if(!EventBus.getDefault().isRegistered(MessageCenterFragment.this)) {
			EventBus.getDefault().register(MessageCenterFragment.this);
		}
		
		return view;
	}
	
	public void onEventMainThread(String result) {
		if(result.equals("PRIVATELETTER")&&isMessageActionGet) {
			refreshList();
		}
	}
	
	public void onResume() {
		super.onResume();

		UserModel umodel=DB.getInstance(getActivity()).getUserModel();
		if(umodel==null) {
			fragment_messagecenter_login_layout.setVisibility(View.VISIBLE);
		}
		else {
			fragment_messagecenter_login_layout.setVisibility(View.GONE);
			refreshList();
		}
		
	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
		EventBus.getDefault().unregister(MessageCenterFragment.this);
	}
	
	private void refreshList() {
		isMessageActionGet=false;
		messageMap.clear();
		messageMap.addAll(DB.getInstance(getActivity()).getProvateMessageList(getActivity()));
		adapter.notifyDataSetChanged();
		isMessageActionGet=true;
	}
}
