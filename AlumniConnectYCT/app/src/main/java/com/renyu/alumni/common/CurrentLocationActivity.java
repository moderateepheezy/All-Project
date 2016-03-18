package com.renyu.alumni.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.renyu.alumni.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentLocationActivity extends Activity implements OnGetGeoCoderResultListener {
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	ListView current_location_listview=null;
	CurrentLocationAdapter adapter=null;
	MapView current_location_bmapView=null;
	BaiduMap mBaiduMap=null;
	TextView current_location_tip=null;
	
	LocationClient mLocClient;
	MyLocationListenner myListener=new MyLocationListenner();
	GeoCoder mSearch=null;
	
	ArrayList<HashMap<String, String>> maps=null;
	//���μ���
	boolean isFirstLoad=false;
	//�Ƿ���
	boolean isMutiPoint=false;
	int choicePosition=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_currentlocation);
		
		maps=new ArrayList<HashMap<String, String>>();
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("ѡ��λ��");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setVisibility(View.VISIBLE);
		nav_right_item_text.setText("����");
		nav_right_item_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(choicePosition==-1) {
					CommonUtils.showCustomToast(CurrentLocationActivity.this, "��ѡ��һ��λ�õ�", false);
					return;
				}
				Intent intent=getIntent();
				Bundle bundle=new Bundle();
				bundle.putString("latitude", maps.get(choicePosition).get("latitude"));
				bundle.putString("longitude", maps.get(choicePosition).get("longitude"));
				bundle.putString("title", maps.get(choicePosition).get("title"));
				bundle.putString("content", maps.get(choicePosition).get("content"));
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		current_location_tip=(TextView) findViewById(R.id.current_location_tip);
		current_location_listview=(ListView) findViewById(R.id.current_location_listview);
		current_location_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				choicePosition=position;
				MapStatusUpdate u=MapStatusUpdateFactory.newLatLng(new LatLng(Double.parseDouble(maps.get(position).get("latitude")), Double.parseDouble(maps.get(position).get("longitude"))));
				mBaiduMap.animateMapStatus(u);
				adapter.notifyDataSetChanged();
			}
		});
		adapter=new CurrentLocationAdapter(this, maps);
		current_location_listview.setAdapter(adapter);
		current_location_bmapView=(MapView) findViewById(R.id.current_location_bmapView);
		current_location_bmapView.showZoomControls(false);
		current_location_bmapView.showScaleControl(false);
		mBaiduMap=current_location_bmapView.getMap();
		mBaiduMap.setMaxAndMinZoomLevel(19, 16);
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {
			
			@Override
			public void onTouch(MotionEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getAction()==MotionEvent.ACTION_DOWN) {
					isMutiPoint=false;
				}
				if(arg0.getAction()==MotionEvent.ACTION_MOVE) {
					if(arg0.getPointerCount()>1) {
						isMutiPoint=true;
					}
				}
				if(arg0.getAction()==MotionEvent.ACTION_UP&&!isMutiPoint) {
					choicePosition=-1;
					mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mBaiduMap.getMapStatus().target));
					current_location_tip.setVisibility(View.VISIBLE);
				}
			}
		});
		mLocClient=new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		mSearch=GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
	}
	
	public int getChoicePosition() {
		return choicePosition;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		current_location_bmapView.onPause();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		current_location_bmapView.onResume();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		current_location_bmapView.onDestroy();
		mSearch.destroy();
		current_location_bmapView=null;
		super.onDestroy();
	}
	
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location==null||current_location_bmapView==null) {
				return;
			}
			MyLocationData locData=new MyLocationData.Builder().accuracy(location.getRadius()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			LatLng ll=new LatLng(location.getLatitude(), location.getLongitude());
			MapStatusUpdate u=MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			mLocClient.stop();
			
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("title", "��ǰλ��");
			map.put("content", location.getAddrStr());
			map.put("latitude", ""+location.getLatitude());
			map.put("longitude", ""+location.getLongitude());
			maps.add(map);
			
			isFirstLoad=true;
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
			current_location_tip.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result==null||result.error!=SearchResult.ERRORNO.NO_ERROR) {
			
		}
		if(!isFirstLoad) {
			maps.clear();
		}
		isFirstLoad=false;
		for(int i=0;i<result.getPoiList().size();i++) {
			PoiInfo poi=result.getPoiList().get(i);
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("title", poi.name);
			map.put("content", poi.address);
			map.put("latitude", ""+poi.location.latitude);
			map.put("longitude", ""+poi.location.longitude);
			maps.add(map);
		}
		if(maps.size()>0) {
			choicePosition=0;
		}
		adapter.notifyDataSetChanged();
		current_location_listview.setSelection(0);
		current_location_tip.setVisibility(View.GONE);
	}
}
