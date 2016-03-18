package com.renyu.alumni.ucenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renyu.alumni.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class UserLabelActivity extends SwipeBackActivity {

	@InjectView(R.id.nav_title) TextView nav_title;
	@InjectView(R.id.nav_left_item) ImageView nav_left_item;
	@InjectView(R.id.userinfolabel_layout) LinearLayout userinfolabel_layout;
	@InjectView(R.id.userinfolabel_commit) TextView userinfolabel_commit;
	
	SwipeBackLayout mSwipeBackLayout=null;
 	
	HashMap<String, String> map=null;
	
	int position=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userlabel);
		ButterKnife.inject(this);
		
		map=new HashMap<String, String>();
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title.setText("���˱�ǩ");
		nav_left_item.setVisibility(View.VISIBLE);
		
		String[] labels=getIntent().getExtras().getStringArray("labels");
		if(labels.length!=0) {
			for(int i=0;i<labels.length;i++) {
				addLabelView(labels[i], "position"+position);
			}
		}
		else {
			addLabelView("", "position"+position);
		}
	}
	
	@OnClick(R.id.nav_left_item)
	public void nav_left_item_click(View view) {
		Intent intent=new Intent();
		Bundle bundle=new Bundle();
		ArrayList<String> strs=new ArrayList<String>();
		for(int i=0;i<userinfolabel_layout.getChildCount();i++) {
			View view_=userinfolabel_layout.getChildAt(i);
			EditText view_userinfolabel_name=(EditText) view_.findViewById(R.id.view_userinfolabel_name);
			strs.add(view_userinfolabel_name.getText().toString());
		}
		bundle.putStringArrayList("labels", strs);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@OnClick(R.id.userinfolabel_commit)
	public void onClickCommit(View view) {
		if(map.size()==3) {
			
		}
		else {
			addLabelView("", "position"+position);
		}
	}
	
	private void addLabelView(String label, final String tag) {
		final View view=LayoutInflater.from(this).inflate(R.layout.view_userinfolabel, null);
		EditText view_userinfolabel_name=(EditText) view.findViewById(R.id.view_userinfolabel_name);
		view_userinfolabel_name.setText(label);
		view_userinfolabel_name.requestFocus();
		ImageView view_userinfolabel_delete=(ImageView) view.findViewById(R.id.view_userinfolabel_delete);
		view_userinfolabel_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map.remove(tag);
				userinfolabel_layout.removeView(view);
			}
		});
		userinfolabel_layout.addView(view);
		map.put(tag, label);
		position++;
	}
}
