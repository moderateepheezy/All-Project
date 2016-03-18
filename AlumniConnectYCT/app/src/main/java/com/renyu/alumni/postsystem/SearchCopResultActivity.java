package com.renyu.alumni.postsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyeem.chips.ChipsTextView;
import com.eyeem.chips.DefaultBubbles;
import com.eyeem.chips.Linkify;
import com.eyeem.chips.Regex;
import com.eyeem.chips.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.login.LoginActivity;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.ucenter.MessageActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchCopResultActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	LinearLayout searchcop_scrollview=null;
	
	ArrayList<UserModel> models=new ArrayList<UserModel>();
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchcopresult);
		
		bitmapUtils=BitmapHelp.getBitmapUtils(this);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));
		config.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));	
		
		models=(ArrayList<UserModel>) getIntent().getExtras().getSerializable("searchman");
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("������");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		searchcop_scrollview=(LinearLayout) findViewById(R.id.searchcop_scrollview);
		for(int j=0;j<models.size();j++) {
			final int j_=j;
			View convertView=LayoutInflater.from(this).inflate(R.layout.adapter_searchcopresult, null);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UserModel model=DB.getInstance(SearchCopResultActivity.this).getUserModel();
					if(model==null) {
						Intent intent=new Intent(SearchCopResultActivity.this, LoginActivity.class);
						startActivity(intent);
					}
					else {
						Intent intent=new Intent(SearchCopResultActivity.this, MessageActivity.class);
						Bundle bundle=new Bundle();
						bundle.putInt("user_id", models.get(j_).getUser_id());
						bundle.putString("username", models.get(j_).getUser_name());
						bundle.putString("avatar_large", models.get(j_).getAvatar_large());
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			});
//			ChipsTextView adapter_searchcopresult_lable_value=(ChipsTextView) convertView.findViewById(R.id.adapter_searchcopresult_lable_value);
//			TextPaint paint = new TextPaint();
//		    Resources r = getResources();
//		    float _dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, r.getDisplayMetrics());
//		    paint.setAntiAlias(true);
//		    paint.setTextSize(_dp);
//		    adapter_searchcopresult_lable_value.setTextPaint(paint);
//		    adapter_searchcopresult_lable_value.setOnBubbleClickedListener(new ChipsTextView.OnBubbleClickedListener() {
//		    	@Override
//		        public void onBubbleClicked(View view, BubbleSpan bubbleSpan) {
//		        	System.out.println(((Linkify.Entity) bubbleSpan.data()).text);
//		         }
//		    });
//		    ArrayList<String> tags=new ArrayList<String>();
//		    String tag="���"+j+",HAHA";
//		    if(tag.indexOf(",")!=-1) {
//		    	for(int i=0;i<tag.split(",").length;i++) {
//			    	tags.add(tag.split(",")[i]);
//			    }
//		    	update(adapter_searchcopresult_lable_value, tags);
//		    }
		    ImageView adapter_searchcopresult_image=(ImageView) convertView.findViewById(R.id.adapter_searchcopresult_image);
			bitmapUtils.display(adapter_searchcopresult_image, ParamsManager.bucketName+models.get(j).getAvatar_large(), config);
			TextView adapter_searchcopresult_name=(TextView) convertView.findViewById(R.id.adapter_searchcopresult_name);
			adapter_searchcopresult_name.setText(models.get(j).getUser_name());
			TextView adapter_searchcopresult_classinfo=(TextView) convertView.findViewById(R.id.adapter_searchcopresult_classinfo);
			if(models.get(j).getYear()==0&&models.get(j).getClass_name().equals("")) {
				adapter_searchcopresult_classinfo.setVisibility(View.GONE);
			}
			else {
				adapter_searchcopresult_classinfo.setVisibility(View.VISIBLE);
				adapter_searchcopresult_classinfo.setText(models.get(j).getYear()+"��"+models.get(j).getClass_name());
			}
			TextView adapter_searchcopresult_industry=(TextView) convertView.findViewById(R.id.adapter_searchcopresult_industry);
			String city=models.get(j).getCity_name();
			String industry=models.get(j).getIndustry();
			String companyname=models.get(j).getCompanyname();
			if(city.equals("")&&industry.equals("")&&companyname.equals("")) {
				adapter_searchcopresult_industry.setVisibility(View.GONE);
			}
			else {
				adapter_searchcopresult_industry.setVisibility(View.VISIBLE);
				adapter_searchcopresult_industry.setText((city+" "+industry+" "+companyname).trim());
			}
		    searchcop_scrollview.addView(convertView);
		}
	}
	
	public void update(ChipsTextView view_activity_detail_lable_value, ArrayList<String> tags) {
		// scan to find bubble matches and populate text view accordingly
		String flattenedText="";
		for(int i=0;i<tags.size();i++) {
			flattenedText+="[a:"+tags.get(i)+"] ";
		}
		Linkify.Entities entities = new Linkify.Entities();
		if (!TextUtils.isEmpty(flattenedText)) {
			Matcher matcher = Regex.VALID_BUBBLE.matcher(flattenedText);
	        while (matcher.find()) {
	        	String bubbleText = matcher.group(1);
	            Linkify.Entity entity = new Linkify.Entity(matcher.start(), matcher.end(), bubbleText, bubbleText, Linkify.Entity.ALBUM);
	            entities.add(entity);
	        }
	    }
		// now bubblify text edit
	    SpannableStringBuilder ssb = new SpannableStringBuilder(flattenedText);
	    for (Linkify.Entity e : entities) {
	    	Utils.bubblify(ssb, e.text, e.start, e.end, view_activity_detail_lable_value.getWidth() - view_activity_detail_lable_value.getPaddingLeft() - view_activity_detail_lable_value.getPaddingRight(), DefaultBubbles.get(DefaultBubbles.BLUE, this, view_activity_detail_lable_value.getTextSize()), null, e);
	    }
	    view_activity_detail_lable_value.setText(ssb);
	}
}
