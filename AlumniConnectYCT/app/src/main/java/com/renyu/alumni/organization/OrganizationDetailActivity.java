package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.AluassociationinfoModel;
import com.renyu.alumni.model.ClassInfoModel;
import com.renyu.alumni.model.ClassUserModel;
import com.renyu.alumni.model.CreateclassinfoModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;
import com.renyu.alumni.ucenter.UserInfoActivity;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.SwipeBackLayout.OnFinishListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OrganizationDetailActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	TextView nav_title=null;
	ImageView nav_left_item=null;
	TextView nav_right_item_text=null;
	
	TextView organization_detail_list_title=null;
	PullToRefreshListView organization_detail_list=null;
	ListView actualListView=null;
	OrganizationDetailAdapter adapter=null;
	RelativeLayout organization_detail_new_adder=null;
	ImageView organization_detail_new_adder_image=null;
	TextView organization_detail_new_adder_num=null;
	TextView organization_detail_new_adder_title=null;
	TextView organization_detail_new_adder_otherinfo=null;
	RelativeLayout organization_detail_result=null;
	ImageView organization_detail_result_image=null;
	TextView organization_detail_resultback=null;
	TextView organization_detail_result_title=null;
	TextView organization_detail_result_otherinfo=null;
	TextView organization_detail_introduction=null;
	TextView organization_detail_introduction_manager=null;
	LinearLayout organization_detail_result_layout=null;
	TextView organization_detail_result_text=null;
	TextView organization_detail_result_text_more=null;
	TextView organization_detail_reapply=null;
	TextView organization_detail_giveup=null;
	TextView organization_detail_nolist=null;
	LinearLayout organization_detail_join_layout=null;
	TextView organization_detail_join_text=null;
	TextView organization_detail_join_apply=null;
	
	//��ǰ����������
	Object data_obj=null;
	//��ǰ�û�����Ա״̬
	int isAdmin=0;
	ArrayList<ClassUserModel> models=null;
	//�ж��Ƿ����ڼ���
	boolean isLoading=false;
	//��ǰҳ���Ƿ������Ķ�
	boolean isNeedLoad=false;
	//֮ǰУ�ѻ������������ݶ���
	int beforeJoinId=-1;
	String beforeJoinValidation="";

	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organization_detail);
		
		models=new ArrayList<ClassUserModel>();
		
		init();
	}
	
	private void init() {

		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setOnFinishListener(new OnFinishListener() {
			
			@Override
			public void finish() {
				// TODO Auto-generated method stub
				if(isNeedLoad) {
					setResult(RESULT_OK, getIntent());
				}
			}
		});
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("����");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isNeedLoad) {
					setResult(RESULT_OK, getIntent());
				}
				finish();
			}});
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setText("�ٻ�");
		
		organization_detail_list_title=(TextView) findViewById(R.id.organization_detail_list_title);
		organization_detail_list=(PullToRefreshListView) findViewById(R.id.organization_detail_list);
		organization_detail_list.setMode(Mode.PULL_FROM_START);
		organization_detail_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				//�˴���bug�����л�ҳ��ʱ����ɵײ�subview����Сʱ�����ڴ˲���ʾ
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
				if(!isLoading) {
					if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
						loadOrganizationDetail((ClassInfoModel) data_obj);
					}
					else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
						loadOrganizationDetail((AluassociationinfoModel) data_obj);
					}
				}
			}
		});
		actualListView=organization_detail_list.getRefreshableView();
		adapter=new OrganizationDetailAdapter(OrganizationDetailActivity.this, models, OrganizationDetailActivity.this, getIntent().getExtras().getString("type"));
		actualListView.setAdapter(adapter);
		organization_detail_nolist=(TextView) findViewById(R.id.organization_detail_nolist);
		organization_detail_new_adder=(RelativeLayout) findViewById(R.id.organization_detail_new_adder);
		organization_detail_new_adder_image=(ImageView) findViewById(R.id.organization_detail_new_adder_image); 
		organization_detail_new_adder_num=(TextView) findViewById(R.id.organization_detail_new_adder_num);
		organization_detail_new_adder_title=(TextView) findViewById(R.id.organization_detail_new_adder_title);
		organization_detail_new_adder_otherinfo=(TextView) findViewById(R.id.organization_detail_new_adder_otherinfo);
		organization_detail_result=(RelativeLayout) findViewById(R.id.organization_detail_result);
		organization_detail_result_image=(ImageView) findViewById(R.id.organization_detail_result_image); 
		organization_detail_resultback=(TextView) findViewById(R.id.organization_detail_resultback);
		organization_detail_result_title=(TextView) findViewById(R.id.organization_detail_result_title);
		organization_detail_result_otherinfo=(TextView) findViewById(R.id.organization_detail_result_otherinfo);
		organization_detail_introduction=(TextView) findViewById(R.id.organization_detail_introduction);
		organization_detail_introduction_manager=(TextView) findViewById(R.id.organization_detail_introduction_manager);
		organization_detail_result_layout=(LinearLayout) findViewById(R.id.organization_detail_result_layout);
		organization_detail_result_text=(TextView) findViewById(R.id.organization_detail_result_text);
		organization_detail_result_text_more=(TextView) findViewById(R.id.organization_detail_result_text_more);
		organization_detail_reapply=(TextView) findViewById(R.id.organization_detail_reapply);
		organization_detail_giveup=(TextView) findViewById(R.id.organization_detail_giveup);
		organization_detail_join_layout=(LinearLayout) findViewById(R.id.organization_detail_join_layout);
		organization_detail_join_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(OrganizationDetailActivity.this, OrganizationJoinActivity.class);
				Bundle bundle=new Bundle();
				if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
					bundle.putString("type", "ClassInfoModel");
					ClassInfoModel model=(ClassInfoModel) data_obj;
					bundle.putInt("id", getIntent().getExtras().getInt("id")==0?model.getClass_id():getIntent().getExtras().getInt("id"));
				}
				else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
					bundle.putString("type", "AluassociationinfoModel");
					AluassociationinfoModel model=(AluassociationinfoModel) data_obj;
					bundle.putInt("id", getIntent().getExtras().getInt("id")==0?model.getAluassociation_id():getIntent().getExtras().getInt("id"));
				}
				bundle.putBoolean("refreshCurrent", getIntent().getExtras().getBoolean("refreshCurrent"));
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		organization_detail_join_text=(TextView) findViewById(R.id.organization_detail_join_text);
		organization_detail_join_apply=(TextView) findViewById(R.id.organization_detail_join_apply);
		//����3������£��������¼���
		if(getIntent().getExtras().getBoolean("isNew")) {
			organization_detail_resultback.setVisibility(View.INVISIBLE);
			organization_detail_join_layout.setVisibility(View.VISIBLE);
			actualListView.setVisibility(View.GONE);
			if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
				organization_detail_join_text.setText("����δ���뱾�༶\n�ݲ��ܿ����༶��Ա");
			}
			else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
				organization_detail_join_text.setText("����δ���뱾У�ѻ�\n�ݲ��ܿ���У�ѻ��Ա");
			}
		}
		else {
			organization_detail_resultback.setVisibility(View.VISIBLE);
			organization_detail_join_layout.setVisibility(View.GONE);
			actualListView.setVisibility(View.VISIBLE);
		}
		
		if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
			organization_detail_list_title.setText("�༶��Ա�б�");
			data_obj=getIntent().getExtras().getSerializable("ClassInfoModel");
			final ClassInfoModel model=(ClassInfoModel) data_obj;
			nav_right_item_text.setVisibility(View.VISIBLE);
			nav_right_item_text.setOnClickListener(new TextView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ArrayList<String> strArrayList=new ArrayList<String>();
					ArrayList<Integer> imageArrayList=new ArrayList<Integer>();
					
					if(CommonUtils.isAppInstalled(OrganizationDetailActivity.this, "com.tencent.mobileqq")) {
						imageArrayList.add(R.drawable.qq_logo);
						strArrayList.add("QQ����");
						imageArrayList.add(R.drawable.qzone_logo);
						strArrayList.add("QQ�ռ�");
					}
					if(CommonUtils.isAppInstalled(OrganizationDetailActivity.this, "com.tencent.mm")) {
						imageArrayList.add(R.drawable.weixin_logo);
						strArrayList.add("΢�ź���");
						imageArrayList.add(R.drawable.weixinpy_logo);
						strArrayList.add("΢������Ȧ");
					}
					if(CommonUtils.isAppInstalled(OrganizationDetailActivity.this, "com.sina.weibo")) {
						imageArrayList.add(R.drawable.weibo_logo);
						strArrayList.add("����΢��");
					}
					Object[] strArrayTemp=(Object[]) strArrayList.toArray();
					final String[] strArray=new String[strArrayTemp.length];
					for(int i=0;i<strArrayTemp.length;i++) {
						strArray[i]=String.valueOf(strArrayTemp[i]);
					}
					Object[] imageArrayTemp=(Object[]) (imageArrayList.toArray());
					final int[] imageArray=new int[imageArrayTemp.length];
					for(int i=0;i<imageArrayTemp.length;i++) {
						imageArray[i]=Integer.valueOf(String.valueOf(imageArrayTemp[i]));
					}
					CommonUtils.showCustomAlertDialog(OrganizationDetailActivity.this, strArray, imageArray, new OnDialogItemClickListener() {

						@Override
						public void click(int pos) {
							// TODO Auto-generated method stub
							if(strArray[pos].equals("QQ����")) {
								CommonUtils.sendComp(OrganizationDetailActivity.this, 4, model.getClass_id());
								CommonUtils.shareQQ(OrganizationDetailActivity.this, getResources().getString(R.string.share_member), "http://morningtel.qiniudn.com/ic_launcher.png", "�ƴ��˺�����", "http://aga.ustc.edu.cn/ustcapp/index.html");
							}
							else if(strArray[pos].equals("QQ�ռ�")) {
								CommonUtils.sendComp(OrganizationDetailActivity.this, 4, model.getClass_id());
								CommonUtils.shareQQKJ(OrganizationDetailActivity.this, getResources().getString(R.string.share_member), "http://morningtel.qiniudn.com/ic_launcher.png", "�ƴ��˺�����", "http://aga.ustc.edu.cn/ustcapp/index.html");
							}
							else if(strArray[pos].equals("΢�ź���")) {
								CommonUtils.sendComp(OrganizationDetailActivity.this, 2, model.getClass_id());
								CommonUtils.shareWeixin(OrganizationDetailActivity.this, getResources().getString(R.string.share_member), "�ƴ��˺�����", "", "http://aga.ustc.edu.cn/ustcapp/index.html");
							}
							else if(strArray[pos].equals("΢������Ȧ")) {
								CommonUtils.sendComp(OrganizationDetailActivity.this, 2, model.getClass_id());
								CommonUtils.shareWeixinPy(OrganizationDetailActivity.this, getResources().getString(R.string.share_member), "�ƴ��˺�����", "", "http://aga.ustc.edu.cn/ustcapp/index.html");
							}
							else if(strArray[pos].equals("����΢��")) {
								CommonUtils.sendComp(OrganizationDetailActivity.this, 2, model.getClass_id());
								CommonUtils.shareWeibo(OrganizationDetailActivity.this, getResources().getString(R.string.share_member), "http://morningtel.qiniudn.com/ic_launcher.png", "�ƴ��˺�����", "http://aga.ustc.edu.cn/ustcapp/index.html");
							}
						}});
				}});
			loadClassData(model);
			loadOrganizationDetail(model);
		}
		else if(getIntent().getExtras().getString("type").equals("CreateclassinfoModel")) {
			organization_detail_list_title.setText("�༶��Ա�б�");
			data_obj=getIntent().getExtras().getSerializable("CreateclassinfoModel");	
			final CreateclassinfoModel model=(CreateclassinfoModel) data_obj;
			isAdmin=model.getSuper_admin();
			adapter.setAdmin(isAdmin);
			organization_detail_new_adder.setVisibility(View.GONE);
			organization_detail_result.setVisibility(View.VISIBLE);
			organization_detail_result_title.setText(model.getClass_name());
			organization_detail_result_otherinfo.setText("�����ˣ�"+model.getCreate_username());
			if(model.getClass_state()==0) {
				organization_detail_resultback.setText("����");
				organization_detail_nolist.setText("����δͨ����ˣ���Ա�����ݲ�����");
				organization_detail_nolist.setVisibility(View.VISIBLE);
			}
			else if(model.getClass_state()==2) {
				organization_detail_resultback.setText("�ܾ�");
				organization_detail_result_layout.setVisibility(View.VISIBLE);
				String str="";
				for(int i=0;i<model.getRefuseStr().split("&").length;i++) {
					str+=(i+1)+"."+model.getRefuseStr().split("&")[i];
					if(i!=model.getRefuseStr().split("&").length-1) {
						str+="\n";
					}
				}
				organization_detail_result_text.setText("���������ѱ�����Ա�ܾ�");
				organization_detail_result_text_more.setText(str);
				organization_detail_result_text_more.setVisibility(View.VISIBLE);
				organization_detail_list.setVisibility(View.GONE);
				organization_detail_reapply.setText("��֪����");
				organization_detail_reapply.setOnClickListener(new TextView.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						deleteApply(model.getClass_id());
					}});
				organization_detail_giveup.setVisibility(View.GONE);
				organization_detail_nolist.setVisibility(View.GONE);
			}
		}
		else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
			data_obj=getIntent().getExtras().getSerializable("AluassociationinfoModel");	
			final AluassociationinfoModel model=(AluassociationinfoModel) data_obj;
			organization_detail_list_title.setText("У�ѻ��Ա�б�");
			organization_detail_introduction.setVisibility(View.VISIBLE);
			organization_detail_introduction.setText("���޼��");
			organization_detail_introduction_manager.setOnClickListener(new TextView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(OrganizationDetailActivity.this, IntroductionManagerActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("aluassociation_id", ""+model.getAluassociation_id());
					bundle.putString("title", model.getAluassociation_name());
					bundle.putString("content", organization_detail_introduction.getText().toString());
					intent.putExtras(bundle);
					startActivityForResult(intent, 107);
				}});
			loadAlumni(model);
			loadOrganizationDetail(model);
		}
	}
	
	private void loadClassData(final ClassInfoModel model) {
		isAdmin=model.getClass_admin();
		adapter.setAdmin(isAdmin);
		if(model.getClass_admin()==1) {
			organization_detail_new_adder.setVisibility(View.VISIBLE);
			organization_detail_result.setVisibility(View.GONE);
			if(model.getStudent_apply()>0) {
				organization_detail_new_adder_num.setVisibility(View.VISIBLE);
				organization_detail_new_adder_num.setText(""+model.getStudent_apply());
			}
			else {
				organization_detail_new_adder_num.setVisibility(View.GONE);
			}
			organization_detail_new_adder_title.setText(model.getClass_name());
			organization_detail_new_adder_otherinfo.setText("�����ˣ�"+model.getAdmin_name()+"  �Ѽ��룺"+model.getStudent_num()+"��");
		}
		else {
			if(model.getClass_role_state()==1) {
				organization_detail_new_adder.setVisibility(View.VISIBLE);
				organization_detail_result.setVisibility(View.GONE);
				organization_detail_new_adder_num.setVisibility(View.GONE);
				organization_detail_new_adder_title.setText(model.getClass_name());
				organization_detail_new_adder_otherinfo.setText("�����ˣ�"+model.getAdmin_name()+"  �Ѽ��룺"+model.getStudent_num()+"��");
			}
			else {
				organization_detail_new_adder.setVisibility(View.GONE);
				organization_detail_result.setVisibility(View.VISIBLE);
				organization_detail_result_title.setText(model.getClass_name());
				organization_detail_result_otherinfo.setText("�����ˣ�"+model.getAdmin_name()+"  �Ѽ��룺"+model.getStudent_num()+"��");
				if(model.getClass_role_state()==0) {
					organization_detail_resultback.setText("����");
				}
				else if(model.getClass_role_state()==2) {
					organization_detail_resultback.setText("�ܾ�");
					organization_detail_result_layout.setVisibility(View.VISIBLE);
					String str="";
					organization_detail_result_text.setText("������Ȩ�޲鿴�ð༶");
					for(int i=0;i<model.getRefuseStr().split("&").length;i++) {
						str+=(i+1)+"."+model.getRefuseStr().split("&")[i];
						if(i!=model.getRefuseStr().split("&").length-1) {
							str+="\n";
						}
					}
					organization_detail_result_text_more.setVisibility(View.VISIBLE);
					organization_detail_result_text_more.setText(str);
					organization_detail_list.setVisibility(View.GONE);
					organization_detail_reapply.setOnClickListener(new TextView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(OrganizationDetailActivity.this, OrganizationJoinActivity.class);
							Bundle bundle=new Bundle();
							bundle.putString("type", "ClassInfoModel");
							bundle.putInt("id", getIntent().getExtras().getInt("id")==0?model.getClass_id():getIntent().getExtras().getInt("id"));
							bundle.putBoolean("refreshCurrent", getIntent().getExtras().getBoolean("refreshCurrent"));
							intent.putExtras(bundle);
							startActivity(intent);
						}});
					organization_detail_giveup.setOnClickListener(new TextView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							giveupclass(model.getClass_id());
						}});
				}
				else if(model.getClass_role_state()==3) {
					organization_detail_result_layout.setVisibility(View.GONE);
				}
			}
		}
	}
	
	private void loadAlumni(final AluassociationinfoModel model) {
		isAdmin=model.getAluassociation_admin();
		adapter.setAdmin(isAdmin);
		organization_detail_introduction.setText(model.getAluassociation_desc().equals("")?"���޼��":model.getAluassociation_desc());
		if(model.getAluassociation_admin()==1) {
			organization_detail_introduction_manager.setVisibility(View.VISIBLE);
			organization_detail_new_adder.setVisibility(View.VISIBLE);
			organization_detail_result.setVisibility(View.GONE);
			if(model.getAluassociation_apply()>0) {
				organization_detail_new_adder_num.setVisibility(View.VISIBLE);
				organization_detail_new_adder_num.setText(""+model.getAluassociation_apply());
			}
			else {
				organization_detail_new_adder_num.setVisibility(View.GONE);
			}
			organization_detail_new_adder_title.setText(model.getAluassociation_name());
			organization_detail_new_adder_otherinfo.setText("����Ա��"+model.getAdmin_name()+"  �Ѽ��룺"+model.getAluassociation_num()+"��");
		}
		else {
			if(model.getAluassociation_role_state()==1) {
				organization_detail_new_adder.setVisibility(View.VISIBLE);
				organization_detail_result.setVisibility(View.GONE);
				organization_detail_new_adder_num.setVisibility(View.GONE);
				organization_detail_new_adder_title.setText(model.getAluassociation_name());
				organization_detail_new_adder_otherinfo.setText("����Ա��"+model.getAdmin_name()+"  �Ѽ��룺"+model.getAluassociation_num()+"��");
			}
			else {
				organization_detail_new_adder.setVisibility(View.GONE);
				organization_detail_result.setVisibility(View.VISIBLE);
				organization_detail_result_title.setText(model.getAluassociation_name());
				organization_detail_result_otherinfo.setText("����Ա��"+model.getAdmin_name()+"  �Ѽ��룺"+model.getAluassociation_num()+"��");
				if(model.getAluassociation_role_state()==0) {
					organization_detail_resultback.setText("����");
				}
				else if(model.getAluassociation_role_state()==2) {
					organization_detail_resultback.setText("�ܾ�");
					organization_detail_result_layout.setVisibility(View.VISIBLE);
					String str="";
					//����Ƭ�н���
					if(getIntent().getExtras().getBoolean("isBussinessCard")) {
						organization_detail_result_text.setText("������Ȩ�޲鿴��У�ѻ�:\n���������ѱ�����Ա�ܾ�");
					}
					else {
						organization_detail_result_text.setText("���������ѱ�����Ա�ܾ�");
					}
					for(int i=0;i<model.getRefuseStr().split("&").length;i++) {
						str+=(i+1)+"."+model.getRefuseStr().split("&")[i];
						if(i!=model.getRefuseStr().split("&").length-1) {
							str+="\n";
						}
					}
					organization_detail_result_text_more.setVisibility(View.VISIBLE);
					organization_detail_result_text_more.setText(str);
					organization_detail_list.setVisibility(View.GONE);
					organization_detail_reapply.setOnClickListener(new TextView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(OrganizationDetailActivity.this, OrganizationJoinActivity.class);
							Bundle bundle=new Bundle();
							bundle.putString("type", "AluassociationinfoModel");
							bundle.putInt("id", getIntent().getExtras().getInt("id")==0?model.getAluassociation_id():getIntent().getExtras().getInt("id"));
							bundle.putBoolean("refreshCurrent", getIntent().getExtras().getBoolean("refreshCurrent"));
							intent.putExtras(bundle);
							startActivity(intent);
						}});
					organization_detail_giveup.setOnClickListener(new TextView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							giveupalumni(model.getAluassociation_id());
						}});
				}
				else if(model.getAluassociation_role_state()==3) {
					organization_detail_result_layout.setVisibility(View.GONE);
				}
			}
		}
	}
	
	/**
	 * ���س�Ա�б�
	 * @param omodel
	 */
	private void loadOrganizationDetail(Object omodel) {
		isLoading=true;
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
		if(omodel instanceof ClassInfoModel) {
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "classinfo", model.getToken(), OrganizationDetailActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			final ClassInfoModel cmodel=(ClassInfoModel) omodel;
			client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/classinfo/"+cmodel.getClass_id(), headers, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					System.out.println(new String(arg2));
					models.clear();
					models.addAll(JsonParse.getAllClassUserModel(new String(arg2), cmodel));
					loadClassData(cmodel);
					adapter.notifyDataSetChanged();
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					if(arg0==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(arg2));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					organization_detail_list.onRefreshComplete();
					isLoading=false;
					
					if(models.size()!=0) {
						return;
					}
					organization_detail_list.setVisibility(View.GONE);
					organization_detail_nolist.setVisibility(View.VISIBLE);
					if(cmodel.getClass_admin()==1) {
						organization_detail_nolist.setText("�༶�ﻹû��С���Ŷ���������Ҽ���ɡ�");
					}
					else {
						if(cmodel.getClass_role_state()==1) {
							organization_detail_nolist.setText("�༶�ﻹû��С���Ŷ���������Ҽ���ɡ�");
						}
						else if(cmodel.getClass_role_state()==0) {
							organization_detail_nolist.setText("����δͨ����ˣ���Ա�����ݲ�����");
						}
					}
				}
			});
		}
		else if(omodel instanceof AluassociationinfoModel) {
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "aluassociationinfo", model.getToken(), OrganizationDetailActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			final AluassociationinfoModel amodel=(AluassociationinfoModel) omodel;
			client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/aluassociationinfo/"+amodel.getAluassociation_id(), headers, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					System.out.println(new String(arg2));
					models.clear();
					models.addAll(JsonParse.getAllAlumniUserModel(new String(arg2), amodel));
					loadAlumni(amodel);
					adapter.notifyDataSetChanged();
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					if(arg0==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(arg2));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					organization_detail_list.onRefreshComplete();
					isLoading=false;
					
					if(models.size()!=0) {
						return;
					}
					organization_detail_list.setVisibility(View.GONE);
					organization_detail_nolist.setVisibility(View.VISIBLE);
					if(amodel.getAluassociation_admin()==1) {
						organization_detail_nolist.setText("У�ѻ��ﻹû��С���Ŷ���������Ҽ���ɡ�");
					}
					else {
						if(amodel.getAluassociation_role_state()==1) {
							organization_detail_nolist.setText("У�ѻ��ﻹû��С���Ŷ���������Ҽ���ɡ�");
						}
						else if(amodel.getAluassociation_role_state()==0) {
							organization_detail_nolist.setText("����δͨ����ˣ���Ա�����ݲ�����");
						}
					}
				}
			});
		}
	}
	
	/**
	 * ��׼���߾ܾ��³�Ա
	 * @param cmodel
	 * @param isApply
	 */
	public void operateNewUser(ClassUserModel cmodel, final boolean isApply, final boolean refuse_types, final String refusereason) {
		dialog=CommonUtils.showCustomAlertProgressDialog(OrganizationDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		JSONObject obj=new JSONObject();
		try {
			obj.put("type", isApply?"1":"0");
			obj.put("common_user_id", ""+cmodel.getUser_id());
			if(!isApply) {
				if(refuse_types) {
					JSONArray array=new JSONArray();
					array.put(1);
					obj.put("refuse_types", array);
				}
				else {
					obj.put("refuse_types", new JSONArray());
				}
				obj.put("refusereason", refusereason);
			}
			if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
				obj.put("class_id", ((ClassInfoModel) data_obj).getClass_id());
			}
			else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
				obj.put("aluassociation_id", ((AluassociationinfoModel) data_obj).getAluassociation_id());
			}
			System.out.println(obj.toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));

			UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
			Security se=new Security();
			String serToken="";
			String url="";
			if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
				serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "applyclassinfo", model.getToken(), OrganizationDetailActivity.this);
				url=ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/applyclassinfo";
			}
			else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
				serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "applyaluassociation", model.getToken(), OrganizationDetailActivity.this);
				url=ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/applyaluassociation";
			}
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			client.post(OrganizationDetailActivity.this, url, headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
						loadOrganizationDetail((ClassInfoModel) data_obj);
					}
					else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
						loadOrganizationDetail((AluassociationinfoModel) data_obj);
					}
					isNeedLoad=true;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	
	/**
	 * ������˽���
	 * @param tipMessage
	 */
	public void startApply(ClassUserModel model, String tipMessage) {
		Intent intent=new Intent(OrganizationDetailActivity.this, OrganizationDetailApplyActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("desp", tipMessage);
		bundle.putSerializable("model", model);
		if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
			bundle.putString("type", "ClassInfoModel");
			bundle.putSerializable("ClassInfoModel", (ClassInfoModel) data_obj);
		}
		else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
			bundle.putString("type", "AluassociationinfoModel");
			bundle.putSerializable("AluassociationinfoModel", (AluassociationinfoModel) data_obj);
		}
		intent.putExtras(bundle);
		startActivityForResult(intent, ParamsManager.ORGANIZATION_OPERATION);
	}
	
	public void setAdminOperationChoice(final int user_id) {
		String[] strArray={"��Ϊ����Ա", "ɾ���ó�Ա"};
		int[] imageArray={R.drawable.ic_edit, R.drawable.ic_delete_activity};
		CommonUtils.showCustomAlertDialog(OrganizationDetailActivity.this, strArray, imageArray, new OnDialogItemClickListener() {

			@Override
			public void click(int pos) {
				// TODO Auto-generated method stub
				if(pos==0) {
					setMemberAdmin(user_id);
				}
				else if(pos==1) {
					deleteMember(user_id);
				}
			}});
	}
	
	/**
	 * ���ð༶����Ա
	 */
	public void setMemberAdmin(int user_id) {
		dialog=CommonUtils.showCustomAlertProgressDialog(OrganizationDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
		Security se=new Security();
		if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "rolesetclass", model.getToken(), OrganizationDetailActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			ClassInfoModel model_=(ClassInfoModel) data_obj;
			params.put("class_id", model_.getClass_id());
			params.put("user_id", user_id);
			params.put("type", 1);
			client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/rolesetclass", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
						loadOrganizationDetail((ClassInfoModel) data_obj);
					}
					else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
						loadOrganizationDetail((AluassociationinfoModel) data_obj);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
				}
			});
		}
		else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "rolesetaluassociation", model.getToken(), OrganizationDetailActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			AluassociationinfoModel model_=((AluassociationinfoModel) data_obj);
			params.put("aluassociation_id", model_.getAluassociation_id());
			params.put("user_id", user_id);
			params.put("type", 1);
			client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/rolesetaluassociation", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
						loadOrganizationDetail((ClassInfoModel) data_obj);
					}
					else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
						loadOrganizationDetail((AluassociationinfoModel) data_obj);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
				}
			});
		}
	}
	
	private void deleteApply(int class_id) {
		dialog=CommonUtils.showCustomAlertProgressDialog(OrganizationDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "deleteclass", model.getToken(), OrganizationDetailActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"/StudentsContacts/contactsapi/aluassociation/deleteclass/"+class_id, headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
				Intent intent=new Intent();
				intent.setAction("refresh");
				sendBroadcast(intent);
				finish();
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(statusCode==401) {
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
		});
	}
	
	/**
	 * ȥ���༶��Ա
	 */
	private void deleteMember(int user_id) {
		dialog=CommonUtils.showCustomAlertProgressDialog(OrganizationDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
		
		if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "rolesetclass", model.getToken(), OrganizationDetailActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			ClassInfoModel model_=(ClassInfoModel) data_obj;
			params.put("class_id", model_.getClass_id());
			params.put("user_id", user_id);
			params.put("type", 0);
			client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/rolesetclass", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
						loadOrganizationDetail((ClassInfoModel) data_obj);
					}
					else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
						loadOrganizationDetail((AluassociationinfoModel) data_obj);
					}
					isNeedLoad=true;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
				}
			});
		}
		else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "rolesetaluassociation", model.getToken(), OrganizationDetailActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			AluassociationinfoModel model_=((AluassociationinfoModel) data_obj);
			params.put("aluassociation_id", model_.getAluassociation_id());
			params.put("user_id", user_id);
			params.put("type", 0);
			client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/rolesetaluassociation", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
						loadOrganizationDetail((ClassInfoModel) data_obj);
					}
					else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
						loadOrganizationDetail((AluassociationinfoModel) data_obj);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
				}
			});
		}
	}
	
	/**
	 * �û������༶����
	 * @param class_id
	 */
	private void giveupclass(int class_id) {
		dialog=CommonUtils.showCustomAlertProgressDialog(OrganizationDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "giveupclass", model.getToken(), OrganizationDetailActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/giveupclass/"+class_id, headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
				Intent intent=new Intent();
				intent.setAction("refresh");
				sendBroadcast(intent);
				finish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
		
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		});
	}
	
	/**
	 * �û�����У�ѻ�����
	 * @param aluassociation_id
	 */
	private void giveupalumni(int aluassociation_id) {
		dialog=CommonUtils.showCustomAlertProgressDialog(OrganizationDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "giveupalu", model.getToken(), OrganizationDetailActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/giveupalu/"+aluassociation_id, headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
				Intent intent=new Intent();
				intent.setAction("refresh");
				sendBroadcast(intent);
				finish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
		
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		});
	}
	
	/**
	 * ����У�ѻ�����
	 * @param aluassociation_id
	 * @param apply_validation
	 */
	public void joinAlumni(int aluassociation_id, String apply_validation) {
		dialog=CommonUtils.showCustomAlertProgressDialog(OrganizationDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "joinaluassociation", model.getToken(), OrganizationDetailActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		JSONObject obj=new JSONObject();
		try {
			obj.put("aluassociation_id", aluassociation_id);
			obj.put("apply_validation", apply_validation);
			System.out.println(obj.toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/joinaluassociation", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					//��������Ϣ����ȫ��ʱ�����û�ȥ����֮�����¼���
					if(JsonParse.getLoginResult(OrganizationDetailActivity.this, new String(responseBody))==2) {
						//�����ֻ���Bug
						ParamsManager.isOpenCount=0;
						Intent intent=new Intent(OrganizationDetailActivity.this, UserInfoActivity.class);
						Bundle bundle=new Bundle();
						bundle.putBoolean("isNeedBack", true);
						intent.putExtras(bundle);
						startActivityForResult(intent, ParamsManager.ORGANIZATION_ALUMNIREJOIN);
					}
					else {
						Intent intent=new Intent();
						intent.setAction("refresh");
						sendBroadcast(intent);
						finish();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					System.out.println(new String(responseBody));
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
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
	
	/**
	 * ����༶����
	 * @param class_id
	 * @param apply_validation
	 */
	public void joinClassMember(int class_id, String apply_validation) {
		dialog=CommonUtils.showCustomAlertProgressDialog(OrganizationDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(OrganizationDetailActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "joinclass", model.getToken(), OrganizationDetailActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		JSONObject obj=new JSONObject();
		try {
			obj.put("class_id", class_id);
			obj.put("apply_validation", apply_validation);
			System.out.println(obj.toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(OrganizationDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/joinclass", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					Intent intent=new Intent();
					intent.setAction("refresh");
					sendBroadcast(intent);
					finish();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					System.out.println(new String(responseBody));
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			if(isNeedLoad) {
				setResult(RESULT_OK, getIntent());
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==107&&resultCode==RESULT_OK) {
			String intro=data.getExtras().getString("intro");
			organization_detail_introduction.setText(intro);
		}
		else if(requestCode==ParamsManager.ORGANIZATION_OPERATION&&resultCode==RESULT_OK) {
			boolean isApply=data.getExtras().getBoolean("oper");
			ClassUserModel model=(ClassUserModel) data.getExtras().getSerializable("model");
			if(isApply) {
				operateNewUser(model, isApply, false, "");
			}
			else {
				operateNewUser(model, isApply, data.getExtras().getBoolean("checked"), data.getExtras().getString("refusereason"));
			}
		}
		else if(resultCode==RESULT_OK&&ParamsManager.ORGANIZATION_ALUMNIREJOIN==requestCode) {
			joinAlumni(beforeJoinId, beforeJoinValidation);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
			joinClassMember(intent.getExtras().getInt("id"), intent.getExtras().getString("apply_validation"));
		}
		else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
			joinAlumni(intent.getExtras().getInt("id"), intent.getExtras().getString("apply_validation"));
			
			beforeJoinId=intent.getExtras().getInt("id");
			beforeJoinValidation=intent.getExtras().getString("apply_validation");
		}
		
	}
}
