package com.renyu.alumni.organization;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
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
import com.renyu.alumni.login.LoginActivity;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

public class OrganizationFragment extends Fragment {
	
	View view=null;

	TextView nav_title=null;
	ImageView nav_left_item=null;
	ImageView nav_right_item=null;
	PopupWindow pop=null;
	
	PullToRefreshListView organization_list=null;
	ListView actualbeginListView=null;
	TextView organization_login=null;
	RelativeLayout organization_login_layout=null;
	TextView organization_list_no=null;
	
	OrganizationAdapter adapter=null;
	
	ArrayList<Object> allOrganizationInfo=null;
	//�ж��Ƿ����ڼ���
	boolean isLoading=false;
	//�״μ����û�model�����Ա���ÿ�λص���fragmentʱ���ܹ����¼���Ƿ��û��Ѿ���¼
	UserModel modelFirst=null;
	//��ѯ����
	String queryType="all";
	
	MyLoadingDialog dialog=null;
	
	public static OrganizationFragment getInstance(String queryType) {
		OrganizationFragment fragment=new OrganizationFragment();
		Bundle bundle=new Bundle();
		bundle.putString("queryType", queryType);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(getArguments()!=null&&getArguments().getString("queryType")!=null) {
			queryType=getArguments().getString("queryType");
		}
		
		IntentFilter filter=new IntentFilter();
		filter.addAction("refresh");
		getActivity().registerReceiver(receiver, filter);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		getActivity().unregisterReceiver(receiver);
	}
	
	BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals("refresh")) {
				refreshOrganization();
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null) {
			modelFirst=DB.getInstance(getActivity()).getUserModel();
			allOrganizationInfo=new ArrayList<Object>();
			
			view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_organization, container, false);
			
			nav_title=(TextView) view.findViewById(R.id.nav_title);
			nav_title.setText("У����֯");
			nav_left_item=(ImageView) view.findViewById(R.id.nav_left_item);
			nav_left_item.setVisibility(View.VISIBLE);
			nav_left_item.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getActivity().finish();
				}
			});
			nav_right_item=(ImageView) view.findViewById(R.id.nav_right_item);
			nav_right_item.setVisibility(View.VISIBLE);
			nav_right_item.setImageResource(R.drawable.ic_organization_add);
			nav_right_item.setOnClickListener(new ImageView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UserModel model=DB.getInstance(getActivity()).getUserModel();
					if(model==null) {
						Intent intent=new Intent(getActivity(), LoginActivity.class);
						startActivity(intent);
					}
					else {
//						if(pop!=null&&pop.isShowing()) {
//							pop.dismiss();						
//						}
//						else {
//							showAddOrganizationLayout();
//						}
						
						String[] strArray=null;
						if(queryType.equals("all")) {
							String[] temp={"��Ӱ༶¼", "���У�ѻ�"};
							strArray=temp;
						}
						else if(queryType.equals("myclass")) {
							String[] temp={"��Ӱ༶¼"};
							strArray=temp;
						}
						else if(queryType.equals("myalumni")) {
							String[] temp={"���У�ѻ�"};
							strArray=temp;
						}
						CommonUtils.showCustomAlertDialog(getActivity(), strArray, null, new OnDialogItemClickListener() {

							@Override
							public void click(int pos) {
								// TODO Auto-generated method stub
								if(queryType.equals("all")) {
									if(pos==0) {
										Intent intent=new Intent(getActivity(), SearchClassActivity.class);
										getParentFragment().startActivityForResult(intent, ParamsManager.ORGANIZATION_REFRESH);
									}
									else if(pos==1) {
										Intent intent=new Intent(getActivity(), SearchAlumniActivity.class);
										startActivity(intent);
									}
								}
								else if(queryType.equals("myclass")) {
									Intent intent=new Intent(getActivity(), SearchClassActivity.class);
									startActivityForResult(intent, ParamsManager.ORGANIZATION_REFRESH);
								}
								else if(queryType.equals("myalumni")) {
									Intent intent=new Intent(getActivity(), SearchAlumniActivity.class);
									startActivity(intent);
								}
							}});
					}				
				}});
			
			organization_list=(PullToRefreshListView) view.findViewById(R.id.organization_list);
			actualbeginListView=organization_list.getRefreshableView();
			adapter=new OrganizationAdapter(getActivity(), allOrganizationInfo, OrganizationFragment.this);
			actualbeginListView.setAdapter(adapter);
			actualbeginListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
				}
			});
			organization_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
					// Update the LastUpdatedLabel
					//�˴���bug�����л�ҳ��ʱ����ɵײ�subview����Сʱ�����ڴ˲���ʾ
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
					// Do work to refresh the list here.
					if(!isLoading) {
						loadOrganizationData();
					}					
				}
			});
			
			organization_login=(TextView) view.findViewById(R.id.organization_login);
			organization_login.setOnClickListener(new TextView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
				}});
			organization_login_layout=(RelativeLayout) view.findViewById(R.id.organization_login_layout);
			UserModel model=DB.getInstance(getActivity()).getUserModel();
			if(model==null) {
				organization_login_layout.setVisibility(View.VISIBLE);
			}
			else {
				organization_login_layout.setVisibility(View.GONE);
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						refreshOrganization();
					}
				}, 300);
			}
			organization_list_no=(TextView) view.findViewById(R.id.organization_list_no);
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null) {
			parent.removeView(view);
		}
		return view;
	}
	
	public void refreshOrganization() {
		organization_list.setRefreshing(true);
	}
	
	/**
	 * ����У����֯�б�
	 */
	private void loadOrganizationData() {
		isLoading=true;
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(getActivity()).getUserModel();
		if(model==null) {
			organization_list.onRefreshComplete();
			isLoading=false;
			return;
		}
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "get", model.getToken(), getActivity());
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(getActivity(), ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/get", headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if(getActivity()!=null&&getActivity().isFinishing()) {
					return;
				}
				System.out.println(new String(arg2));
				allOrganizationInfo.clear();
				allOrganizationInfo.addAll(JsonParse.getAllOrganizationModel(new String(arg2), queryType));
				adapter.notifyDataSetChanged();
				organization_list_no.setVisibility(View.GONE);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				if(getActivity()!=null&&getActivity().isFinishing()) {
					return;
				}
				organization_list_no.setVisibility(View.VISIBLE);
				if(arg0==401) {
					JsonParse.showMessage(getActivity(), new String(arg2));
				}
				else {
					CommonUtils.showCustomToast(getActivity(), getResources().getString(R.string.network_error), false);
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				organization_list.onRefreshComplete();
				isLoading=false;
			}
		});
	}
	
	/**
	 * ���У����֯
	 */
	private void showAddOrganizationLayout() {
		LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.view_addorganization, null, false);
		pop=new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		TextView add_organization_class=(TextView) view.findViewById(R.id.add_organization_class);
		add_organization_class.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), SearchClassActivity.class);
				getParentFragment().startActivityForResult(intent, ParamsManager.ORGANIZATION_REFRESH);
				pop.dismiss();
			}});
		TextView add_organization_alumni=(TextView) view.findViewById(R.id.add_organization_alumni);
		add_organization_alumni.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), SearchAlumniActivity.class);
				startActivity(intent);
				pop.dismiss();
			}});
		pop.update();
		int[] location = new int[2];    
        organization_list.getLocationOnScreen(location);
        int width=view.getMeasuredWidth();  
        DisplayMetrics dm=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        pop.showAtLocation(organization_list, Gravity.NO_GRAVITY, dm.widthPixels-width, location[1]);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkLogin();
		StatService.onResume(this);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(pop!=null&&pop.isShowing()) {
			pop.dismiss();						
		}
		StatService.onPause(this);
	}
	
	/**
	 * ����û���¼״̬�Ա���ˢ��
	 */
	private void checkLogin() {
		if(modelFirst==null&&DB.getInstance(getActivity()).getUserModel()!=null) {
			modelFirst=DB.getInstance(getActivity()).getUserModel();
			organization_login_layout.setVisibility(View.GONE);
			refreshOrganization();
		}
		else if(modelFirst!=null&&DB.getInstance(getActivity()).getUserModel()==null) {
			modelFirst=null;
			organization_login_layout.setVisibility(View.VISIBLE);
			allOrganizationInfo.clear();
			adapter.notifyDataSetChanged();
		}
		else if(modelFirst!=null&&DB.getInstance(getActivity()).getUserModel()!=null) {
			if(modelFirst.getUser_id()!=DB.getInstance(getActivity()).getUserModel().getUser_id()) {
				modelFirst=DB.getInstance(getActivity()).getUserModel();
				refreshOrganization();
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//���У����֯��ص�
		if(requestCode==ParamsManager.ORGANIZATION_REFRESH&&resultCode==Activity.RESULT_OK) {
			refreshOrganization();
		}
	}
}
