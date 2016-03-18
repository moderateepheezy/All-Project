package com.renyu.alumni.postsystem;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyeem.chips.BubbleSpan;
import com.eyeem.chips.ChipsTextView;
import com.eyeem.chips.DefaultBubbles;
import com.eyeem.chips.Linkify;
import com.eyeem.chips.Regex;
import com.eyeem.chips.Utils;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CameraBeforeActivity;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.LabelChoiceActivity;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.PostUploadManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.PostUploadModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;

import java.util.ArrayList;
import java.util.regex.Matcher;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SendPostActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	TextView sendpost_type1=null;
	TextView sendpost_type2=null;
	TextView sendpost_type3=null;
	TextView sendpost_type4=null;
	LinearLayout sendpost_label_layout=null;
	ChipsTextView sendpost_label_value=null;
	EditText sendpost_title=null;
	EditText sendpost_content=null;
	GridView sendpost_gridview=null;
	PostGridAdapter adapter=null;
	
	ArrayList<String> tags=null;
	ArrayList<String> pics=null;
	int choiceType=1;
	
	UserModel modelUser=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendpost);
		
		modelUser=DB.getInstance(SendPostActivity.this).getUserModel();
		
		tags=new ArrayList<String>();
		pics=new ArrayList<String>();
		pics.add("");
		
		init();
	}

	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
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
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setVisibility(View.VISIBLE);
		nav_right_item_text.setText("����");
		nav_right_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostUploadModel model=new PostUploadModel();
				model.setContent(sendpost_content.getText().toString());
				model.setTitle(sendpost_title.getText().toString());
				model.setPublish_author(modelUser.getUser_name());
				String tag="";
				for(int i=0;i<tags.size();i++) {
					if(i==tags.size()-1) {
						tag+=tags.get(i);
					}
					else {
						tag+=tags.get(i)+"&";
					}
				}
				model.setResource_tags(tag);
				if(choiceType==1) {
					model.setResource_type(3);
					model.setResource_category(1);
				}
				else if(choiceType==2) {
					model.setResource_type(4);
					model.setResource_category(1);
				}
				else if(choiceType==3) {
					model.setResource_type(5);
					model.setResource_category(1);
				}
				else if(choiceType==4) {
					model.setResource_type(6);
					model.setResource_category(2);
				}
				ArrayList<String> uploadList=new ArrayList<String>();
				for(int i=0;i<pics.size()-1;i++) {
					uploadList.add(pics.get(i));
				}
				model.setUploadList(uploadList);
				PostUploadManager.getInstance(SendPostActivity.this).start(model);
				finish();
			}});
		
		sendpost_type1=(TextView) findViewById(R.id.sendpost_type1);
		sendpost_type1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateType(1);
			}
		});
		sendpost_type2=(TextView) findViewById(R.id.sendpost_type2);
		sendpost_type2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateType(2);
			}
		});
		sendpost_type3=(TextView) findViewById(R.id.sendpost_type3);
		sendpost_type3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateType(3);
			}
		});
		sendpost_type4=(TextView) findViewById(R.id.sendpost_type4);
		sendpost_type4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateType(4);
			}
		});
		sendpost_label_layout=(LinearLayout) findViewById(R.id.sendpost_label_layout);
		sendpost_label_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SendPostActivity.this, LabelChoiceActivity.class);
				Bundle bundle=new Bundle();
				bundle.putStringArrayList("tags", tags);
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.POSTCOMMENT_LABELADD);
			}});
		sendpost_label_value=(ChipsTextView) findViewById(R.id.sendpost_label_value);
		TextPaint paint = new TextPaint();
	    Resources r = getResources();
	    float _dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
	    paint.setAntiAlias(true);
	    paint.setTextSize(_dp);
	    sendpost_label_value.setTextPaint(paint);
	    sendpost_label_value.setOnBubbleClickedListener(new ChipsTextView.OnBubbleClickedListener() {
	    	@Override
	        public void onBubbleClicked(View view, BubbleSpan bubbleSpan) {
	        	System.out.println(((Linkify.Entity) bubbleSpan.data()).text);
	         }
	    });

		sendpost_title=(EditText) findViewById(R.id.sendpost_title);
		sendpost_content=(EditText) findViewById(R.id.sendpost_content);
		sendpost_gridview=(GridView) findViewById(R.id.sendpost_gridview);
		adapter=new PostGridAdapter(SendPostActivity.this, pics);
		sendpost_gridview.setAdapter(adapter);
	}
	
	public void removePic(int pos) {
		pics.remove(pos);
		adapter.notifyDataSetChanged();
	}
	
	public void addPic() {
		// TODO Auto-generated method stub
		String[] strArray={"���", "���"};
		int[] imageArray={R.drawable.ic_photo, R.drawable.ic_camera};
		CommonUtils.showCustomAlertDialog(SendPostActivity.this, strArray, imageArray, new OnDialogItemClickListener() {

			@Override
			public void click(int pos) {
				// TODO Auto-generated method stub
				if(pos==0) {
					//����ϵͳ���
					Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(intent, ParamsManager.ALBUM);
				}
				else if(pos==1) {
					Intent intent=new Intent(SendPostActivity.this, CameraBeforeActivity.class);
					startActivityForResult(intent, ParamsManager.CAMERA);
				}
			}});
	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&requestCode==ParamsManager.CAMERA) {
			if(data==null) {
        		return;
        	}
        	String filePath=CommonUtils.getScalePicturePathName(data.getExtras().getString("path"));
        	updateGrid(filePath);
        }
        else if(resultCode==RESULT_OK&&requestCode==ParamsManager.ALBUM) {
        	if(data==null) {
        		return;
        	}
        	Uri uri=data.getData();
        	String filePath="";
			if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT) {
				filePath=CommonUtils.getPath(uri, SendPostActivity.this);
			}
			else {
				String[] projection={MediaStore.Images.Media.DATA};
				Cursor cs=managedQuery(uri, projection, null, null, null);
				int columnNumber=cs.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cs.moveToFirst();
				filePath=cs.getString(columnNumber);
				filePath.replaceAll("file:///", "/");
			}
			filePath=CommonUtils.getScalePicturePathName(filePath);
			updateGrid(filePath);
        }
        else if(resultCode==RESULT_OK&&requestCode==ParamsManager.POSTCOMMENT_LABELADD) {
        	tags=data.getExtras().getStringArrayList("tags");
        	update();
        }
	}
	
	private void updateGrid(String path) {
		pics.add(pics.size()-1, path);
		adapter.notifyDataSetChanged();
	}
	
	public void update() {
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
	    	Utils.bubblify(ssb, e.text, e.start, e.end, sendpost_label_value.getWidth() - sendpost_label_value.getPaddingLeft() - sendpost_label_value.getPaddingRight(), DefaultBubbles.get(DefaultBubbles.BLUE, this, sendpost_label_value.getTextSize()), null, e);
	    }
	    sendpost_label_value.setText(ssb);
	}
	
	private void updateType(int pos) {
		choiceType=pos;
		sendpost_type1.setBackgroundColor(Color.WHITE);
		sendpost_type2.setBackgroundColor(Color.WHITE);
		sendpost_type3.setBackgroundColor(Color.WHITE);
		sendpost_type4.setBackgroundColor(Color.WHITE);
		sendpost_type1.setTextColor(getResources().getColor(R.color.tab_text_press));
		sendpost_type2.setTextColor(getResources().getColor(R.color.tab_text_press));
		sendpost_type3.setTextColor(getResources().getColor(R.color.tab_text_press));
		sendpost_type4.setTextColor(getResources().getColor(R.color.tab_text_press));
		if(pos==1) {
			sendpost_type1.setBackgroundColor(getResources().getColor(R.color.tab_text_press));
			sendpost_type1.setTextColor(Color.WHITE);
		}
		else if(pos==2) {
			sendpost_type2.setBackgroundColor(getResources().getColor(R.color.tab_text_press));
			sendpost_type2.setTextColor(Color.WHITE);
		}
		else if(pos==3) {
			sendpost_type3.setBackgroundColor(getResources().getColor(R.color.tab_text_press));
			sendpost_type3.setTextColor(Color.WHITE);
		}
		else if(pos==4) {
			sendpost_type4.setBackgroundColor(getResources().getColor(R.color.tab_text_press));
			sendpost_type4.setTextColor(Color.WHITE);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
