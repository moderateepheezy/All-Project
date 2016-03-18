package com.renyu.alumni.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.renyu.alumni.R;

public class CurrentLocationDetailActivity extends Activity {
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	
	MapView current_location_detail_bmapView=null;
	BaiduMap mBaiduMap=null;
	InfoWindow mInfoWindow=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_currentlocationdetail);
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("Hello");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		current_location_detail_bmapView=(MapView) findViewById(R.id.current_location_detail_bmapView);
		mBaiduMap=current_location_detail_bmapView.getMap();
		mBaiduMap.setMaxAndMinZoomLevel(19, 16);
		LatLng ll=new LatLng(getIntent().getExtras().getDouble("lat"), getIntent().getExtras().getDouble("lng"));
		MapStatusUpdate u=MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
		OverlayOptions ooA=new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)).zIndex(100).draggable(true);
		mBaiduMap.addOverlay(ooA);
		mInfoWindow=new InfoWindow(BitmapDescriptorFactory.fromView(getInfoWindowView()), ll, -60, new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick() {
				// TODO Auto-generated method stub
				
			}
		});
		mBaiduMap.showInfoWindow(mInfoWindow);
	}
	
	public View getInfoWindowView() {
		View view=LayoutInflater.from(this).inflate(R.layout.view_text, null);
		TextView view_text_text=(TextView) view.findViewById(R.id.view_text_text);
		view_text_text.setText(getIntent().getExtras().getString("address"));
		return view;
	}
}
