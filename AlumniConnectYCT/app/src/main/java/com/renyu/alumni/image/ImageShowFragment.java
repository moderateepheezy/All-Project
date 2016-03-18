package com.renyu.alumni.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class ImageShowFragment extends Fragment {

	String imageUrl="";
	String content="";
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	PhotoView imageshow_big=null;
	TextView imageshow_content=null;
	
	public static ImageShowFragment newInstance(String imageUrl, String content) {
		ImageShowFragment fragment=new ImageShowFragment();
		Bundle bundle=new Bundle();
		bundle.putString("imageUrl", imageUrl);
		bundle.putString("content", content);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		imageUrl=getArguments().getString("imageUrl");
		content=getArguments().getString("content");
		
		bitmapUtils=BitmapHelp.getBitmapUtils(getActivity());
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		bitmapUtils.configDefaultAutoRotation(true);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getActivity().getResources().getDrawable(R.drawable.ic_launcher));
		config.setLoadingDrawable(getActivity().getResources().getDrawable(R.drawable.ic_launcher));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.view_showimage, null);
		imageshow_content=(TextView) view.findViewById(R.id.imageshow_content);
		imageshow_content.setText(content);
		imageshow_big=(PhotoView) view.findViewById(R.id.imageshow_big);
		bitmapUtils.display(imageshow_big, imageUrl, config);	
		imageshow_big.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				((ImageShowActivity) getActivity()).finish();
			}
		});
		return view;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(imageshow_big==null) {
			return;
		}
		else {
			imageshow_big.setScale(1, false);
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
}
