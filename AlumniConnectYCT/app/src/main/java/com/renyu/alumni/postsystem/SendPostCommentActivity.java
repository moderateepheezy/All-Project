package com.renyu.alumni.postsystem;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SendPostCommentActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	EditText sendpostcomment_edit=null;
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendpostcomment);
		
		init();
	}

	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("��Ƭ");
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
				sendPubchannelComment();
			}});
		
		sendpostcomment_edit=(EditText) findViewById(R.id.sendpostcomment_edit);
	}
	
	private void sendPubchannelComment() {
		View view=getWindow().peekDecorView();
        if (view!=null) {
            InputMethodManager inputmanger=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(SendPostCommentActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getresource", model.getToken(), SendPostCommentActivity.this);
		Header[] headers_={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		JSONObject obj_upload=new JSONObject();
		try {
			obj_upload.put("resource_id", getIntent().getExtras().getInt("resource_id"));
			obj_upload.put("comment_content", sendpostcomment_edit.getText().toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj_upload.toString().getBytes("UTF-8"));
			client.post(SendPostCommentActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/pubchannel/comment", headers_, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(SendPostCommentActivity.this, new String(responseBody));
					setResult(RESULT_OK, getIntent());
					finish();
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(SendPostCommentActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(SendPostCommentActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog=CommonUtils.showCustomAlertProgressDialog(SendPostCommentActivity.this, "���ڼ���");
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
					finish();
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
