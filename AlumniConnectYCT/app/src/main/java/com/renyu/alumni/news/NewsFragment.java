package com.renyu.alumni.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.NewsGetModel;
import com.renyu.alumni.model.NewsModel;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class NewsFragment extends Fragment {
	
	TextView nav_title=null;
	
	View view=null;
	
	PullToRefreshListView news_list=null;
	ListView actralListView=null;
	NewsAdapter adapter=null;
	ArrayList<NewsModel> newsModels=null;
	
	View headView=null;
	AutoScrollViewPager news_view_pager=null;
	LinearLayout news_view_pager_point_layout=null;
	ArrayList<NewsModel> imageList;
	ArrayList<ImageView> points=null;
	ImagePagerAdapter imageAdapter=null;
	
	//��ǰҳ��
	int page=1;
	boolean isLoading=false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null) {
			imageList=new ArrayList<NewsModel>();
			newsModels=new ArrayList<NewsModel>();
			points=new ArrayList<ImageView>();
			
			view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news, container, false);
			nav_title=(TextView) view.findViewById(R.id.nav_title);
			nav_title.setText("����");
			
			news_list=(PullToRefreshListView) view.findViewById(R.id.news_list);
			news_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
					//�˴���bug�����л�ҳ��ʱ����ɵײ�subview����Сʱ�����ڴ˲���ʾ
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
					// Do work to refresh the list here.
					int scrollValue=news_list.getHeadScroll();
					//����0��ʱ��Ϊ����ˢ�£�С��0��ʱ��Ϊ����ˢ��
					if(scrollValue>0) {
						if(!isLoading) {
							loadNewsData();
						}						
					}
					else {
						if(!isLoading) {
							page=1;
							loadNewsData();
						}
					}
					
				}
			});
			news_list.setMode(Mode.BOTH);
			actralListView=news_list.getRefreshableView();
			actralListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), NewsDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("url", newsModels.get(position-2).getUrl());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			//addHeadView();
			//actralListView.setAdapter(adapter);
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					news_list.setRefreshing(true);
				}
			}, 300);
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null) {
			parent.removeView(view);
		}
		return view;
	}
	
	private void addHeadView() {
		headView=LayoutInflater.from(getActivity()).inflate(R.layout.view_newsheader, null);
		news_view_pager=(AutoScrollViewPager) headView.findViewById(R.id.news_view_pager);
		news_view_pager_point_layout=(LinearLayout) headView.findViewById(R.id.news_view_pager_point_layout);
		
		imageAdapter=new ImagePagerAdapter(getActivity(), imageList);
		imageAdapter.setInfiniteLoop(true);
        news_view_pager.setAdapter(imageAdapter);
        news_view_pager.setOnPageChangeListener(new MyOnPageChangeListener());

        news_view_pager.setInterval(4000);
        news_view_pager.startAutoScroll();
        news_view_pager.setCurrentItem(Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%getSize(imageList));
        
        points.clear();
        for(int i=0;i<imageList.size();i++) {
        	LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	params.leftMargin=5;
        	params.rightMargin=5;
        	params.bottomMargin=15;
        	ImageView imageview=new ImageView(getActivity());
        	if(i==0) {
        		imageview.setImageResource(R.drawable.point_green);
        	}
        	else {
        		imageview.setImageResource(R.drawable.point_white);
        	}
        	points.add(imageview);
        	news_view_pager_point_layout.addView(imageview, params);
        }
        actralListView.addHeaderView(headView);
	}
	
	private void loadNewsData() {
		isLoading=true;
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("university_id", ParamsManager.university_id);
		params.add("page", ""+page);
		params.add("page_size", "10");
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getnews", "null", getActivity());
		Header[] headers={new BasicHeader("Validation", serToken)};		
		client.get(getActivity(), "http://112.126.70.71:7050/getnews", headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				if(getActivity()!=null&&getActivity().isFinishing()) {
					return;
				}
				NewsGetModel model=JsonParse.getNewsGetModel(new String(responseBody));
				if(page==1&&adapter==null) {
					imageList.clear();
					ArrayList<NewsModel> models=model.getHead_news();
					for(int i=0;i<models.size();i++) {
						imageList.add(models.get(i));
					}
					if(models.size()!=0) {
						addHeadView();
					}
					adapter=new NewsAdapter(getActivity(), newsModels);
					actralListView.setAdapter(adapter);
				}
				if(page==1) {
					newsModels.clear();
				}
				ArrayList<NewsModel> models=model.getNews();
				newsModels.addAll(models);				
				adapter.notifyDataSetChanged();
				if(models.size()!=0) {
					page++;
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(getActivity()!=null&&getActivity().isFinishing()) {
					return;
				}
				if(statusCode==401) {
					JsonParse.showMessage(getActivity(), new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(getActivity(), getResources().getString(R.string.network_error), false);
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				news_list.onRefreshComplete();
				isLoading=false;
			}
		});
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            System.out.println(new StringBuilder().append((position) % getSize(imageList) + 1).append("/").append(getSize(imageList)));
            for(int i=0;i<getSize(points);i++) {
            	if(i==(position) % getSize(points)) {
            		points.get(i).setImageResource(R.drawable.point_green);
            	}
            	else {
            		points.get(i).setImageResource(R.drawable.point_white);
            	}
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        	
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        	
        }
    }
	
	@Override
	public void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        if(news_view_pager!=null) {
        	news_view_pager.stopAutoScroll();
        }
		StatService.onPause(this);
    }

    @Override
	public void onResume() {
        super.onResume();
        // start auto scroll when onResume
        if(news_view_pager!=null) {
        	news_view_pager.startAutoScroll();
        }
		StatService.onResume(this);
    }
    
    public static <V> int getSize(List<V> sourceList) {
        return sourceList==null?0:sourceList.size();
    }

}
