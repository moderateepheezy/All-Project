package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.alumni.R;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class CreateClassRejectActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	TextView nav_right_item_text=null;
	
	RelativeLayout createclassreject_layout1=null;
	CheckBox createclassreject_image1=null;
	RelativeLayout createclassreject_layout2=null;
	CheckBox createclassreject_image2=null;
	RelativeLayout createclassreject_layout3=null;
	CheckBox createclassreject_image3=null;
	RelativeLayout createclassreject_layout4=null;
	CheckBox createclassreject_image4=null;
	RelativeLayout createclassreject_other=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createclassreject);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("��˾ܾ�");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setText("���");
		nav_right_item_text.setVisibility(View.VISIBLE);
		nav_right_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(createclassreject_image1.isChecked()||createclassreject_image2.isChecked()||createclassreject_image3.isChecked()) {
					ArrayList<String> array=new ArrayList<String>();
					if(createclassreject_image1.isChecked()) {
						array.add("1");
					}
					if(createclassreject_image2.isChecked()) {
						array.add("2");
					}
					if(createclassreject_image3.isChecked()) {
						array.add("3");
					}
					Intent intent=new Intent(CreateClassRejectActivity.this, ReviewClassActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle bundle=new Bundle();
					bundle.putStringArrayList("refuse_types", array);
					bundle.putString("refusereason", "");
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}});
		
		createclassreject_layout1=(RelativeLayout) findViewById(R.id.createclassreject_layout1);
		createclassreject_layout1.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(createclassreject_image1.isChecked()) {
					createclassreject_image1.setChecked(false);
				}
				else {
					createclassreject_image1.setChecked(true);
				}
			}});
		createclassreject_image1=(CheckBox) findViewById(R.id.createclassreject_image1);
		createclassreject_layout2=(RelativeLayout) findViewById(R.id.createclassreject_layout2);
		createclassreject_layout2.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(createclassreject_image2.isChecked()) {
					createclassreject_image2.setChecked(false);
				}
				else {
					createclassreject_image2.setChecked(true);
				}
			}});
		createclassreject_image2=(CheckBox) findViewById(R.id.createclassreject_image2);
		createclassreject_layout3=(RelativeLayout) findViewById(R.id.createclassreject_layout3);
		createclassreject_layout3.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(createclassreject_image3.isChecked()) {
					createclassreject_image3.setChecked(false);
				}
				else {
					createclassreject_image3.setChecked(true);
				}
			}});
		createclassreject_image3=(CheckBox) findViewById(R.id.createclassreject_image3);
		createclassreject_layout4=(RelativeLayout) findViewById(R.id.createclassreject_layout4);
		createclassreject_layout4.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(createclassreject_image4.isChecked()) {
					createclassreject_image4.setChecked(false);
				}
				else {
					createclassreject_image4.setChecked(true);
				}
			}});
		createclassreject_image4=(CheckBox) findViewById(R.id.createclassreject_image4);
		createclassreject_other=(RelativeLayout) findViewById(R.id.createclassreject_other);
		createclassreject_other.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(CreateClassRejectActivity.this, CreateClassRejectMoreActivity.class);
				Bundle bundle=new Bundle();
				ArrayList<String> array=new ArrayList<String>();
				if(createclassreject_image1.isChecked()) {
					array.add("1");
				}
				if(createclassreject_image2.isChecked()) {
					array.add("2");
				}
				if(createclassreject_image3.isChecked()) {
					array.add("3");
				}
				if(createclassreject_image4.isChecked()) {
					array.add("4");
				}
				bundle.putStringArrayList("refuse_types", array);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
	}

}
