/*
package org.simpumind.com.yctalumniconnect;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_All_News_List extends ArrayAdapter<ItemNewsList>{
	
	private Activity activity;
	private List<ItemNewsList> itemsnewslist;
	private ItemNewsList objnewslistBean;
	private int row;
	public ImageLoader imageLoader; 
	 
	 public Adapter_All_News_List(Activity act, int resource, List<ItemNewsList> arrayList) {
			super(act, resource, arrayList);
			this.activity = act;
			this.row = resource;
			this.itemsnewslist = arrayList;
			imageLoader=new ImageLoader(activity);
			 
		}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(row, null);

				holder = new ViewHolder();
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			if ((itemsnewslist == null) || ((position + 1) > itemsnewslist.size()))
				return view;

			objnewslistBean = itemsnewslist.get(position);

			holder.txt_newsheading=(TextView)view.findViewById(R.id.txt_newslistheading);
			holder.txt_newsdate=(TextView)view.findViewById(R.id.txt_newslistdate);

			holder.img_news=(ImageView)view.findViewById(R.id.img_newslist);

			holder.txt_newsheading.setText(objnewslistBean.getNewsHeading());
			holder.txt_newsdate.setText(objnewslistBean.getNewsDate());

		 	imageLoader.DisplayImage("http://www.shareesblog.com/wp-content/images/shareeavatar.png", holder.img_news);
			//imageLoader.DisplayImage(Constant.SERVER_IMAGE_NEWSLIST_THUMBS+objnewslistBean.getNewsImage().toString(), holder.img_news);
			
			return view;
			 
		}

		public class ViewHolder {

			public ImageView img_news;
			public  TextView txt_newsheading;
			public  TextView txt_newsdate;
			 
		}
}
*/
