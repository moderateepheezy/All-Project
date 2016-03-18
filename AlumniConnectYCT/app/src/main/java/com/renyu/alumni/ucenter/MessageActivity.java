package com.renyu.alumni.ucenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView.OnResizeListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CameraBeforeActivity;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.CurrentLocationActivity;
import com.renyu.alumni.common.MessageManager;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.PrivateMessageImageManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.PrivateLetterModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.ResizeLayout;
import com.renyu.alumni.receiver.CustomPushReceiver;
import com.renyu.alumni.security.Security;
import com.rockerhieu.emojicon.EmojiconEditText;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MessageActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	int user_id=0;
	
	ResizeLayout message_resize=null;
	PullToRefreshListView message_list=null;
	ListView actualListView=null;
	MessageAdapter adapter=null;
	ArrayList<PrivateLetterModel> models=null;
	EmojiconEditText message_content=null;
	TextView message_commit=null;
	ImageView message_type_change=null;
	LinearLayout message_tool_layout=null;
	LinearLayout message_tool_pic=null;
	LinearLayout message_tool_camera=null;
	LinearLayout message_tool_bussinesscard=null;
	LinearLayout message_tool_location=null;
	
	final static int ROLL_TO_NONE=0;
	final static int ROLL_TO_LASTPOSITION=1;
	final static int ROLL_TO_CURRENTPOSITION=2;
	
	//���������ж�
	final static int BIG=1;
	final static int SMALL=0;
	
	int firstVisibleItem=-1;
	int visibleItemCount=-1; 
	int totalItemCount=-1;
	boolean isLast=false;
	//�Ƿ�Ϊ��һ�μ����ж�
	boolean isFirstKeyBoard=true;
	//ͨ������л��������͵��µļ��̲��ֱ䶯
	boolean isClickTypeChnage=false;
	int currentKeyBoardState=BIG;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		user_id=getIntent().getExtras().getInt("user_id");
		models=new ArrayList<PrivateLetterModel>();
		
		init();
		
		IntentFilter filter=new IntentFilter();
		filter.addAction("PRIVATELETTER");
		filter.addAction("PRIVATELETTERUPLOAD");
		registerReceiver(receiver, filter);
	}
	
	BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals("PRIVATELETTER")&&Integer.parseInt(intent.getExtras().getString("user_id"))==user_id) {
				//����Զ������
				loadData(false);
			}
			else if(intent.getAction().equals("PRIVATELETTERUPLOAD")) {
				if(intent.getExtras().getBoolean("success")) {
					MessageManager.getInstance(MessageActivity.this).uploadComp(user_id, intent.getExtras().getString("tag"));
					for(int i=0;i<models.size();i++) {
						String dbValue=models.get(i).getPrivate_letter_usercontent();
						JSONObject obj=new JSONObject();
						try {
							obj.put("image", intent.getExtras().getString("tag"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String getValue=obj.toString();
						if(dbValue.equals(getValue)) {
							models.get(i).setPrivate_letter_success(1);
							ProgressBar pb=(ProgressBar) actualListView.findViewWithTag(intent.getExtras().getString("tag"));
							if(pb!=null) {
								pb.setVisibility(View.GONE);
							}
							JSONObject obj_final=new JSONObject();
							try {
								obj_final.put("image", intent.getExtras().getString("imageUrl"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							MessageManager.getInstance(MessageActivity.this).uploadToServer(obj_final.toString(), user_id, CustomPushReceiver.IMAGE_MODEL);
						}
					}
				}
			}
		}
	};
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText(getIntent().getExtras().getString("username"));
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		message_resize=(ResizeLayout) findViewById(R.id.message_resize);
		message_resize.setOnResizeListener(new ResizeLayout.OnResizeListener() { 
            
            public void OnResize(int w, int h, int oldw, int oldh) { 
            	if(isFirstKeyBoard) {
            		isFirstKeyBoard=false;
            		return;
            	}
                int change = BIG; 
                if (h < oldh) { 
                    change = SMALL; 
                }     
                currentKeyBoardState=change;
                mHandler.sendEmptyMessage(0);
            } 
        }); 
		message_list=(PullToRefreshListView) findViewById(R.id.message_list);
		message_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
				//����ˢ�¼������ݿ��е�����
				loadData(true);
			}
		});
		//listview���ֱ仯����
		message_list.setOnResizeListener(new OnResizeListener() {
			
			@Override
			public void onResize(int w, int h, int oldw, int oldh) {
				// TODO Auto-generated method stub
				if(models!=null) {
					actualListView.setSelection(models.size());
				}
			}
		});
		actualListView=message_list.getRefreshableView();
		actualListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState==SCROLL_STATE_IDLE) {
					if(MessageActivity.this.firstVisibleItem+MessageActivity.this.visibleItemCount==MessageActivity.this.totalItemCount) {
						isLast=true;
					}
				}
				else {
					isLast=false;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				MessageActivity.this.firstVisibleItem=firstVisibleItem;
				MessageActivity.this.visibleItemCount=visibleItemCount;
				MessageActivity.this.totalItemCount=totalItemCount;
				
			}
		});
		actualListView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(message_tool_layout!=null&&message_content!=null) {
					if(message_tool_layout.getVisibility()==View.VISIBLE||currentKeyBoardState==SMALL) {
						message_tool_layout.setVisibility(View.GONE);
						InputMethodManager imm=(InputMethodManager)getSystemService(MessageActivity.this.INPUT_METHOD_SERVICE);  
						imm.hideSoftInputFromWindow(message_content.getWindowToken(), 0);
						actualListView.setSelection(models.size());
					}
				}
				return false;
			}
		});
		adapter=new MessageAdapter(MessageActivity.this, models, getIntent().getExtras().getString("avatar_large"));
		actualListView.setAdapter(adapter);
		message_content=(EmojiconEditText) findViewById(R.id.message_content);
		message_content.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(s.toString().length()>0) {
					message_type_change.setVisibility(View.INVISIBLE);
					message_type_change.setClickable(false);
					message_commit.setVisibility(View.VISIBLE);
					message_commit.setClickable(true);
				}
				else {
					message_type_change.setVisibility(View.VISIBLE);
					message_type_change.setClickable(true);
					message_commit.setVisibility(View.INVISIBLE);
					message_commit.setClickable(false);
				}
			}
		});
		message_content.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				message_tool_layout.setVisibility(View.GONE);
				return false;
			}
		});
		message_commit=(TextView) findViewById(R.id.message_commit);
		message_commit.setClickable(false);
		message_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!message_content.getText().toString().equals("")) {
					addLocalData(message_content.getText().toString());
					message_content.setText("");
				}
			}});
		message_type_change=(ImageView) findViewById(R.id.message_type_change);
		message_type_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isClickTypeChnage=true;
				if(message_tool_layout.getVisibility()==View.VISIBLE) {
					message_content.requestFocus();
					InputMethodManager imm=(InputMethodManager)getSystemService(MessageActivity.this.INPUT_METHOD_SERVICE);  
					imm.showSoftInput(message_content, InputMethodManager.SHOW_FORCED);
				}
				else {
					if(currentKeyBoardState==BIG) {
						message_tool_layout.setVisibility(View.VISIBLE);
					}
					InputMethodManager imm=(InputMethodManager)getSystemService(MessageActivity.this.INPUT_METHOD_SERVICE);  
					imm.hideSoftInputFromWindow(message_content.getWindowToken(), 0);
				}
			}});
		message_tool_layout=(LinearLayout) findViewById(R.id.message_tool_layout);
		message_tool_pic=(LinearLayout) findViewById(R.id.message_tool_pic);
		message_tool_pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, ParamsManager.ALBUM);
			}
		});
		message_tool_camera=(LinearLayout) findViewById(R.id.message_tool_camera);
		message_tool_camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MessageActivity.this, CameraBeforeActivity.class);
				startActivityForResult(intent, ParamsManager.CAMERA);
			}
		});
		message_tool_bussinesscard=(LinearLayout) findViewById(R.id.message_tool_bussinesscard);
		message_tool_bussinesscard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MessageActivity.this, MyBussinessCardActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("type", "choice");
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.MESSAGE_CHOICE_BUSSINESS);
			}
		});
		message_tool_location=(LinearLayout) findViewById(R.id.message_tool_location);
		message_tool_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MessageActivity.this, CurrentLocationActivity.class);
				startActivityForResult(intent, ParamsManager.PRIVATEMESSAGE_LOCATION);
			}
		});
		
		//��ʼ���ȵ��ñ������ݿ⣬��󼴵�������δ�ջص���Ϣȡ��
		loadData(true);
		loadData(false);
	}
	
	private synchronized void loadData(boolean isLocal) {
		if(isLocal) {
			loadLocalMessageData();
		}
		else {
			loadRemoteMessageData();
		}
	}
	
	/**
	 * ���ر������ݿ��е����ݣ�������һҳ���߳�ʼ������ʱʹ��
	 */
	private void loadLocalMessageData() {
		if(models.size()==0) {
			models.addAll(DB.getInstance(MessageActivity.this).getPrivateLetterModel(user_id, -1, MessageActivity.this));
			sectionControlManager(ROLL_TO_LASTPOSITION);
		}
		else {
			int startNum=models.size();
			models.addAll(0, DB.getInstance(MessageActivity.this).getPrivateLetterModel(user_id, models.get(0).getPrivate_letter_contenttime(), MessageActivity.this));
			sectionControlManager(ROLL_TO_CURRENTPOSITION, models.size()-startNum);
		}
	}
	
	/**
	 * �ֶ��ظ�������
	 */
	private void addLocalData(String message) {
		PrivateLetterModel model=new PrivateLetterModel();
		model.setPrivate_letter_contenttime(System.currentTimeMillis()+ParamsManager.extratime);
		model.setPrivate_letter_to(0);
		model.setPrivate_letter_usercontent(message);
		model.setPrivate_letter_userid(user_id);
		model.setPrivate_letter_type(1);
		model.setPrivate_letter_success(1);
		models.add(model);
		//���
		ArrayList<PrivateLetterModel> models_=new ArrayList<PrivateLetterModel>();
		models_.add(model);
		DB.getInstance(MessageActivity.this).insertPrivateLetter(models_, MessageActivity.this);
		sectionControlManager(ROLL_TO_LASTPOSITION);
		
		JSONObject obj=new JSONObject();
		try {
			obj.put("user_id", getIntent().getExtras().getInt("user_id"));
			obj.put("user_name", getIntent().getExtras().getString("username"));
			obj.put("avatar_large", getIntent().getExtras().getString("avatar_large"));
			obj.put("content", message);
			obj.put("type", 1);
			obj.put("time", ParamsManager.extratime+System.currentTimeMillis());
			MessageManager.getInstance(MessageActivity.this).operMessage(obj, false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			UserModel umodel=DB.getInstance(MessageActivity.this).getUserModel();
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "sendmessage", umodel.getToken(), MessageActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
			AsyncHttpClient client=new AsyncHttpClient();
			JSONObject obj_upload=new JSONObject();
			obj_upload.put("from_user_id", umodel.getUser_id());
			obj_upload.put("to_user_id", user_id);
			obj_upload.put("content", message);
			obj_upload.put("type", CustomPushReceiver.PRIVATELETTER_MODEL);
			ByteArrayEntity entity=new ByteArrayEntity(obj_upload.toString().getBytes("UTF-8"));
			client.post(MessageActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/push/sendmessage", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					
				}});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ���Ϳ���Զ����������
	 */
	private void loadRemoteMessageData() {
		UserModel model=DB.getInstance(MessageActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getmessage", model.getToken(), MessageActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("from_user_id", ""+user_id);
		params.add("timestamp", models.size()==0?"0":""+models.get(models.size()-1).getPrivate_letter_contenttime());
		client.get(MessageActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/push/getmessage", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				models.addAll(JsonParse.getPrivateLetterModel(new String(responseBody), user_id));
				DB.getInstance(MessageActivity.this).insertPrivateLetter(JsonParse.getPrivateLetterModel(new String(responseBody), user_id), MessageActivity.this);
				sectionControlManager(ROLL_TO_NONE);
				removeMessageData();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
			}});
	}
	
	/**
	 * ɾ��Զ����������
	 */
	private void removeMessageData() {
		UserModel model=DB.getInstance(MessageActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "deletemessage", model.getToken(), MessageActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("from_user_id", ""+user_id);
		params.add("timestamp", models.size()==0?"0":""+models.get(models.size()-1).getPrivate_letter_contenttime());
		client.get(MessageActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/push/deletemessage", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println("onSuccess:");
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				System.out.println("onFailure:");
				
			}});
	}
	
	/**
	 * ����listview�ļ���λ��
	 */
	private void sectionControlManager(final int sectionState, final int sectionNum) {
		adapter.notifyDataSetChanged();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				message_list.onRefreshComplete();
				if(sectionState==ROLL_TO_LASTPOSITION) {
					actualListView.setSelection(models.size());
					isLast=true;
				}
				else if(sectionState==ROLL_TO_CURRENTPOSITION) {
					actualListView.setSelection(sectionNum);
				}
				else if(sectionState==ROLL_TO_NONE) {
					if(isLast) {
						actualListView.setSelection(models.size());
					}
				}
			}
		}, 100);		
	}
	
	/**
	 * ����listview�ļ���λ��
	 * @param sectionState
	 */
	private void sectionControlManager(int sectionState) {
		sectionControlManager(sectionState, 0);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
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
		if(models.size()>0) {
			MessageManager.getInstance(MessageActivity.this).clearOneMessage(this, models.get(models.size()-1).getPrivate_letter_userid());
		}
		StatService.onPause(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==ParamsManager.CAMERA&&resultCode==RESULT_OK) {
			if(data==null) {
        		return;
        	}
			String path=CommonUtils.getScalePicturePathName(data.getExtras().getString("path"));
			JSONObject obj=new JSONObject();
			try {
				obj.put("image", path);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrivateLetterModel model=MessageManager.getInstance(MessageActivity.this).addModualData(obj.toString(), user_id, getIntent().getExtras().getString("username"), getIntent().getExtras().getString("avatar_large"), CustomPushReceiver.IMAGE_MODEL);
			PrivateMessageImageManager.getInstance(MessageActivity.this).start(path);
			models.add(model);
			sectionControlManager(ROLL_TO_LASTPOSITION);
		}
		else if(requestCode==ParamsManager.ALBUM&&resultCode==RESULT_OK) {
			if(data==null) {
        		return;
        	}
        	Uri uri=data.getData();
        	String filePath="";
			if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT) {
				filePath=CommonUtils.getPath(uri, MessageActivity.this);
			}
			else {
				String[] projection={MediaStore.Images.Media.DATA};
				Cursor cs=managedQuery(uri, projection, null, null, null);
				int columnNumber=cs.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cs.moveToFirst();
				filePath=cs.getString(columnNumber);
				filePath.replaceAll("file:///", "/");
			}
			String path=CommonUtils.getScalePicturePathName(filePath);
			JSONObject obj=new JSONObject();
			try {
				obj.put("image", path);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrivateLetterModel model=MessageManager.getInstance(MessageActivity.this).addModualData(obj.toString(), user_id, getIntent().getExtras().getString("username"), getIntent().getExtras().getString("avatar_large"), CustomPushReceiver.IMAGE_MODEL);
			PrivateMessageImageManager.getInstance(MessageActivity.this).start(path);
			models.add(model);
			sectionControlManager(ROLL_TO_LASTPOSITION);
		}
		else if(requestCode==ParamsManager.PRIVATEMESSAGE_LOCATION&&resultCode==RESULT_OK) {
			JSONObject obj=new JSONObject();
			try {
				obj.put("lat", data.getExtras().getString("latitude"));
				obj.put("lng", data.getExtras().getString("longitude"));
				obj.put("address", data.getExtras().getString("content"));
				obj.put("title", data.getExtras().getString("title"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			models.add(MessageManager.getInstance(MessageActivity.this).addModualData(obj.toString(), user_id, getIntent().getExtras().getString("username"), getIntent().getExtras().getString("avatar_large"), CustomPushReceiver.LOCATION_MODEL));
			sectionControlManager(ROLL_TO_LASTPOSITION);
		}
		else if(requestCode==ParamsManager.MESSAGE_CHOICE_BUSSINESS&&resultCode==RESULT_OK) {
			UserModel umodel=(UserModel) data.getSerializableExtra("choiceModel");
			JSONObject obj=new JSONObject();
			try {
				obj.put("user_id", umodel.getUser_id());
				obj.put("user_name", umodel.getUser_name());
				obj.put("avatar_large", umodel.getAvatar_large());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			models.add(MessageManager.getInstance(MessageActivity.this).addModualData(obj.toString(), user_id, getIntent().getExtras().getString("username"), getIntent().getExtras().getString("avatar_large"), CustomPushReceiver.BUSSINESS_MODEL));
			sectionControlManager(ROLL_TO_LASTPOSITION);
		}
	}
	
	InputHandler mHandler=new InputHandler(); 
	
	class InputHandler extends Handler { 
        @Override 
        public void handleMessage(Message msg) { 
        	if(isClickTypeChnage) {
    			isClickTypeChnage=false;
        		if (currentKeyBoardState==BIG) { 
        			message_tool_layout.setVisibility(View.VISIBLE);
                } 
                else { 
    				message_tool_layout.setVisibility(View.GONE);
                }
        	}
            super.handleMessage(msg); 
        } 
    } 
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(message_tool_layout.getVisibility()==View.VISIBLE) {
			message_tool_layout.setVisibility(View.GONE);
		}
		else {
			super.onBackPressed();
		}
	}
}
