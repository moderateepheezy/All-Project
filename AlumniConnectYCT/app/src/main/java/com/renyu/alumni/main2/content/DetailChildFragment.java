package com.renyu.alumni.main2.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.PublicIndexModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.news.NewsDetailActivity;
import com.renyu.alumni.postsystem.PostActivityDetailActivity;
import com.renyu.alumni.postsystem.PostCopDetailActivity;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class DetailChildFragment extends Fragment {
	
	View view=null;
	PullToRefreshListView detail_child_listview=null;
	ListView acturalListView=null;
	DetailChildFragmentAdapter adapter=null;
		
	ArrayList<PublicIndexModel> models=null;
	
	int page=1;
	
	boolean isLoading=false;
	
	public static DetailChildFragment newInstance(String value) {
		DetailChildFragment fragment=new DetailChildFragment();
		Bundle bundle=new Bundle();
		bundle.putString("type", value);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null) {
			models=new ArrayList<PublicIndexModel>();
			view=inflater.inflate(R.layout.fragment_detailchild, container, false);
			detail_child_listview=(PullToRefreshListView) view.findViewById(R.id.detail_child_listview);
			detail_child_listview.setMode(Mode.BOTH);
			detail_child_listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
					if(isLoading) {
						return;
					}
					page=1;
					pubchannel_index(getArguments().getString("type"));
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
					if(isLoading) {
						return;
					}
					pubchannel_index(getArguments().getString("type"));
				}
			});
			acturalListView=detail_child_listview.getRefreshableView();
			adapter=new DetailChildFragmentAdapter(getActivity(), models);
			acturalListView.setAdapter(adapter);
			acturalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					switch(models.get(position-1).getResource_type()) {
					case 1:
						Intent intent1=new Intent(getActivity(), NewsDetailActivity.class);
						Bundle bundle1=new Bundle();
						bundle1.putString("url", models.get(position-1).getResource_url());
						bundle1.putString("desc", models.get(position-1).getResource_title());
						bundle1.putString("name", "�ƴ��˷���");
						intent1.putExtras(bundle1);
						startActivity(intent1);
						break;
					case 2:
						Intent intent2=null;
						Bundle bundle2=new Bundle();
						if(!models.get(position-1).getResource_url().equals("")) {
							intent2=new Intent(getActivity(), NewsDetailActivity.class);
							bundle2.putString("url", models.get(position-1).getResource_url());
							bundle2.putString("desc", models.get(position-1).getResource_title());
							bundle2.putString("name", "�ƴ��˷���");
						}
						else {
							intent2=new Intent(getActivity(), PostActivityDetailActivity.class);
							bundle2.putInt("activity_id", Integer.parseInt(models.get(position-1).getResource_id()));
						}
						intent2.putExtras(bundle2);
						startActivity(intent2);
						break;
					default:
						Intent intent3=new Intent(getActivity(), PostCopDetailActivity.class);
						Bundle bundle3=new Bundle();
						bundle3.putInt("resource_id", Integer.parseInt(models.get(position-1).getResource_id()));
						intent3.putExtras(bundle3);
						startActivity(intent3);
					}
				}
			});
			pubchannel_index(getArguments().getString("type"));
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null) {
			parent.removeView(view);
		}

		if(!EventBus.getDefault().isRegistered(DetailChildFragment.this)) {
			EventBus.getDefault().register(DetailChildFragment.this);
		}
		
		return view;
	}

	private void pubchannel_index(String type) {
		isLoading=true;
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("page", ""+page);
		params.put("pagesize", "20");
		params.put("type", type);
		Security se=new Security();
		UserModel user=DB.getInstance(getActivity()).getUserModel();
		Header[] headers;
		if(user!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "index", user.getToken(), getActivity());
			Header[] headers_={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+user.getToken()), new BasicHeader("Validation", serToken)};			
			headers=headers_;
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "index", "null", getActivity());
			Header[] headers_={new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		client.get(getActivity(), ParamsManager.HttpUrl+"StudentsContacts/contactsapi/pubchannel/index", headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				if(getActivity()!=null&&getActivity().isFinishing()) {
					return;
				}
				ArrayList<PublicIndexModel> models_=JsonParse.getPubchannelIndex(new String(responseBody));
				if(page==1) {
					models.clear();
				}
				if(models_.size()>0) {
					models.addAll(models_);
					adapter.notifyDataSetChanged();
					page++;
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				isLoading=false;
				detail_child_listview.onRefreshComplete();
			}
		});
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
		EventBus.getDefault().unregister(DetailChildFragment.this);
	}
	
	public void onEventMainThread(String result) {
		if(result.equals("refreshContent")) {
			if(isLoading) {
				return;
			}
			page=1;
			pubchannel_index(getArguments().getString("type"));
		}
	}
}
