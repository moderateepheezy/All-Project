package com.renyu.alumni.ucenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.CurrentLocationDetailActivity;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.image.ImageShowActivity;
import com.renyu.alumni.model.PrivateLetterModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.CircleImageView;
import com.renyu.alumni.news.NewsDetailActivity;
import com.renyu.alumni.organization.BussinessCardActivity;
import com.renyu.alumni.postsystem.PostCopDetailActivity;
import com.renyu.alumni.receiver.CustomPushReceiver;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
	
	Context context=null;
	ArrayList<PrivateLetterModel> models=null;
	String sendUserAvatar="";
	
	BitmapUtils bitmapUtils=null;
	BitmapDisplayConfig config=null;
	BitmapDisplayConfig config_inner=null;
	
	public MessageAdapter(Context context, ArrayList<PrivateLetterModel> models, String sendUserAvatar) {
		this.context=context;
		this.models=models;
		this.sendUserAvatar=sendUserAvatar;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_main_people));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_main_people));
		config_inner=new BitmapDisplayConfig();
		config_inner.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
		config_inner.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return models.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MessageHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_message, null);
			holder=new MessageHolder();
			holder.message_item_time=(TextView) convertView.findViewById(R.id.message_item_time);
			
			holder.adapter_message_left_image=(CircleImageView) convertView.findViewById(R.id.adapter_message_left_image);
			holder.adapter_message_left_text=(EmojiconTextView) convertView.findViewById(R.id.adapter_message_left_text);	
			holder.adapter_message_left_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_left_layout);
			holder.adapter_message_left_textimage_layout=(RelativeLayout) convertView.findViewById(R.id.adapter_message_left_textimage_layout);
			holder.adapter_message_left_textimage=(ImageView) convertView.findViewById(R.id.adapter_message_left_textimage);
			holder.adapter_message_left_module_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_left_module_layout);
			holder.adapter_message_left_module_title=(TextView) convertView.findViewById(R.id.adapter_message_left_module_title);
			holder.adapter_message_left_module_content=(TextView) convertView.findViewById(R.id.adapter_message_left_module_content);
			holder.adapter_message_left_location_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_left_location_layout);
			holder.adapter_message_left_currentlocation_title=(TextView) convertView.findViewById(R.id.adapter_message_left_currentlocation_title);
			holder.adapter_message_left_currentlocation_content=(TextView) convertView.findViewById(R.id.adapter_message_left_currentlocation_content);
			holder.adapter_message_left_bussinesscard_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_left_bussinesscard_layout);
			holder.adapter_message_left_bussinesscard_name=(TextView) convertView.findViewById(R.id.adapter_message_left_bussinesscard_name);
			
			holder.adapter_message_right_image=(CircleImageView) convertView.findViewById(R.id.adapter_message_right_image);
			holder.adapter_message_right_text=(EmojiconTextView) convertView.findViewById(R.id.adapter_message_right_text);	
			holder.adapter_message_right_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_right_layout);
			holder.adapter_message_right_textimage_layout=(RelativeLayout) convertView.findViewById(R.id.adapter_message_right_textimage_layout);
			holder.adapter_message_right_textimage=(ImageView) convertView.findViewById(R.id.adapter_message_right_textimage);
			holder.adapter_message_right_progress=(ProgressBar) convertView.findViewById(R.id.adapter_message_right_progress);
			holder.adapter_message_right_module_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_right_module_layout);
			holder.adapter_message_right_module_title=(TextView) convertView.findViewById(R.id.adapter_message_right_module_title);
			holder.adapter_message_right_module_content=(TextView) convertView.findViewById(R.id.adapter_message_right_module_content);
			holder.adapter_message_right_location_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_right_location_layout);
			holder.adapter_message_right_currentlocation_title=(TextView) convertView.findViewById(R.id.adapter_message_right_currentlocation_title);
			holder.adapter_message_right_currentlocation_content=(TextView) convertView.findViewById(R.id.adapter_message_right_currentlocation_content);
			holder.adapter_message_right_bussinesscard_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_right_bussinesscard_layout);
			holder.adapter_message_right_bussinesscard_name=(TextView) convertView.findViewById(R.id.adapter_message_right_bussinesscard_name);
			
			holder.adapter_message_subscribe_layout=(LinearLayout) convertView.findViewById(R.id.adapter_message_subscribe_layout);
			holder.adapter_message_subscribe_type=(TextView) convertView.findViewById(R.id.adapter_message_subscribe_type);
			holder.adapter_message_subscribe_title=(TextView) convertView.findViewById(R.id.adapter_message_subscribe_title);
			holder.adapter_message_subscribe_image=(ImageView) convertView.findViewById(R.id.adapter_message_subscribe_image);
			holder.adapter_message_subscribe_content=(TextView) convertView.findViewById(R.id.adapter_message_subscribe_content);
			holder.adapter_message_subscribe_openurl=(RelativeLayout) convertView.findViewById(R.id.adapter_message_subscribe_openurl);;
			
			convertView.setTag(holder);
		}
		else {
			holder=(MessageHolder) convertView.getTag();
		}
		final int pos_=position;
		holder.message_item_time.setText(CommonUtils.getPrivateMessageTimeFormat(models.get(position).getPrivate_letter_contenttime()));
		if(position==0) {
			holder.message_item_time.setVisibility(View.VISIBLE);
		}
		else {
			if(models.get(position).getPrivate_letter_contenttime()-models.get(position-1).getPrivate_letter_contenttime()>60000) {
				holder.message_item_time.setVisibility(View.VISIBLE);
			}
			else {
				holder.message_item_time.setVisibility(View.GONE);
			}
		}
		if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.Subscribe_MODEL) {
			holder.adapter_message_left_layout.setVisibility(View.GONE);
			holder.adapter_message_right_layout.setVisibility(View.GONE);
			holder.adapter_message_subscribe_layout.setVisibility(View.VISIBLE);
			
			try {
				JSONObject obj=new JSONObject(models.get(pos_).getPrivate_letter_usercontent());						
				bitmapUtils.display(holder.adapter_message_subscribe_image, ParamsManager.bucketName+obj.getString("picurl"), config_inner);
				holder.adapter_message_subscribe_type.setText(obj.getString("tag"));
				holder.adapter_message_subscribe_title.setText(obj.getString("title"));
				holder.adapter_message_subscribe_content.setText(obj.getString("description"));
				final String url_=obj.getString("url");
				final String desc_=obj.getString("description");
				final String title_=obj.getString("title");
				holder.adapter_message_subscribe_layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(context, NewsDetailActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("url", url_);
						bundle.putString("desc", desc_);
						bundle.putString("name", title_);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			holder.adapter_message_subscribe_layout.setVisibility(View.GONE);
			if(models.get(position).getPrivate_letter_to()==1) {
				holder.adapter_message_left_layout.setVisibility(View.VISIBLE);
				holder.adapter_message_right_layout.setVisibility(View.GONE);
				holder.adapter_message_right_progress.setTag("");
				if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.IMAGE_MODEL) {
					holder.adapter_message_left_textimage_layout.setVisibility(View.VISIBLE);
					holder.adapter_message_left_textimage_layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj=new JSONObject(models.get(pos_).getPrivate_letter_usercontent());
								ArrayList<String> imageUrls=new ArrayList<String>();
								imageUrls.add(ParamsManager.bucketName+obj.getString("image"));
								ArrayList<String> contents=new ArrayList<String>();
								contents.add("");
								Intent intent=new Intent(context, ImageShowActivity.class);
								Bundle bundle=new Bundle();
								bundle.putStringArrayList("imageUrl", imageUrls);
								bundle.putStringArrayList("content", contents);
								bundle.putInt("currentPage", 0);
								intent.putExtras(bundle);
								context.startActivity(intent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
					holder.adapter_message_left_module_layout.setVisibility(View.GONE);
					holder.adapter_message_left_location_layout.setVisibility(View.GONE);
					holder.adapter_message_left_text.setVisibility(View.GONE);
					holder.adapter_message_left_bussinesscard_layout.setVisibility(View.GONE);
					try {
						JSONObject obj=new JSONObject(models.get(position).getPrivate_letter_usercontent());
						bitmapUtils.display(holder.adapter_message_left_textimage, ParamsManager.bucketName+obj.getString("image"), config_inner);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.PRIVATELETTER_MODEL) {
					holder.adapter_message_left_textimage_layout.setVisibility(View.GONE);
					holder.adapter_message_left_module_layout.setVisibility(View.GONE);
					holder.adapter_message_left_location_layout.setVisibility(View.GONE);
					holder.adapter_message_left_text.setVisibility(View.VISIBLE);
					holder.adapter_message_left_bussinesscard_layout.setVisibility(View.GONE);
					holder.adapter_message_left_text.setText(models.get(position).getPrivate_letter_usercontent());
				}
				else if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.MODUAL_MODEL) {
					holder.adapter_message_left_textimage_layout.setVisibility(View.GONE);
					holder.adapter_message_left_module_layout.setVisibility(View.VISIBLE);
					holder.adapter_message_left_location_layout.setVisibility(View.GONE);
					holder.adapter_message_left_text.setVisibility(View.GONE);
					holder.adapter_message_left_bussinesscard_layout.setVisibility(View.GONE);
					holder.adapter_message_left_module_layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj=new JSONObject(models.get(pos_).getPrivate_letter_usercontent());
								Intent intent3=new Intent(context, PostCopDetailActivity.class);
								Bundle bundle3=new Bundle();
								bundle3.putInt("resource_id", Integer.parseInt(obj.getString("resource_id")));
								intent3.putExtras(bundle3);
								context.startActivity(intent3);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					try {
						JSONObject obj=new JSONObject(models.get(position).getPrivate_letter_usercontent());
						holder.adapter_message_left_module_title.setText(obj.getString("resource_title"));
						holder.adapter_message_left_module_content.setText(obj.getString("resource_content"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.LOCATION_MODEL) {
					holder.adapter_message_left_textimage_layout.setVisibility(View.GONE);
					holder.adapter_message_left_module_layout.setVisibility(View.GONE);
					holder.adapter_message_left_location_layout.setVisibility(View.VISIBLE);
					holder.adapter_message_left_location_layout.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj=new JSONObject(models.get(pos_).getPrivate_letter_usercontent());
								Intent intent=new Intent(context, CurrentLocationDetailActivity.class);
								Bundle bundle=new Bundle();
								bundle.putString("address", obj.getString("address"));
								bundle.putDouble("lat", obj.getDouble("lat"));
								bundle.putDouble("lng", obj.getDouble("lng"));
								intent.putExtras(bundle);
								context.startActivity(intent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}});
					holder.adapter_message_left_bussinesscard_layout.setVisibility(View.GONE);
					holder.adapter_message_left_text.setVisibility(View.GONE);
					try {
						JSONObject obj=new JSONObject(models.get(position).getPrivate_letter_usercontent());
						holder.adapter_message_left_currentlocation_title.setText(obj.getString("title").equals("��ǰλ��")?"λ�÷���":obj.getString("title"));				
						holder.adapter_message_left_currentlocation_content.setText(obj.getString("address"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.BUSSINESS_MODEL) {
					holder.adapter_message_left_textimage_layout.setVisibility(View.GONE);
					holder.adapter_message_left_module_layout.setVisibility(View.GONE);
					holder.adapter_message_left_location_layout.setVisibility(View.GONE);
					holder.adapter_message_left_text.setVisibility(View.GONE);
					holder.adapter_message_left_bussinesscard_layout.setVisibility(View.VISIBLE);
					holder.adapter_message_left_bussinesscard_layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj=new JSONObject(models.get(pos_).getPrivate_letter_usercontent());
								Intent intent=new Intent(context, BussinessCardActivity.class);
								Bundle bundle=new Bundle();
								bundle.putInt("user_id", obj.getInt("user_id"));
								intent.putExtras(bundle);
								context.startActivity(intent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					try {
						JSONObject obj=new JSONObject(models.get(position).getPrivate_letter_usercontent());
						holder.adapter_message_left_bussinesscard_name.setText(obj.getString("user_name")+"����Ƭ");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				bitmapUtils.display(holder.adapter_message_left_image, ParamsManager.bucketName+sendUserAvatar, config);
			}
			else {
				holder.adapter_message_left_layout.setVisibility(View.GONE);
				holder.adapter_message_right_layout.setVisibility(View.VISIBLE);
				if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.IMAGE_MODEL) {
					holder.adapter_message_right_textimage_layout.setVisibility(View.VISIBLE);
					holder.adapter_message_right_textimage_layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj=new JSONObject(models.get(pos_).getPrivate_letter_usercontent());
								ArrayList<String> imageUrls=new ArrayList<String>();
								imageUrls.add(obj.getString("image"));
								ArrayList<String> contents=new ArrayList<String>();
								contents.add("");
								Intent intent=new Intent(context, ImageShowActivity.class);
								Bundle bundle=new Bundle();
								bundle.putStringArrayList("imageUrl", imageUrls);
								bundle.putStringArrayList("content", contents);
								bundle.putInt("currentPage", 0);
								intent.putExtras(bundle);
								context.startActivity(intent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
					holder.adapter_message_right_text.setVisibility(View.GONE);
					holder.adapter_message_right_location_layout.setVisibility(View.GONE);
					holder.adapter_message_right_module_layout.setVisibility(View.GONE);
					holder.adapter_message_right_bussinesscard_layout.setVisibility(View.GONE);
					try {
						JSONObject obj=new JSONObject(models.get(position).getPrivate_letter_usercontent());
						bitmapUtils.display(holder.adapter_message_right_textimage, obj.getString("image"), config_inner);
						holder.adapter_message_right_progress.setTag(obj.getString("image"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(models.get(position).getPrivate_letter_success()==1) {
						holder.adapter_message_right_progress.setVisibility(View.GONE);
					}
					else {
						holder.adapter_message_right_progress.setVisibility(View.VISIBLE);
					}
				}
				else if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.PRIVATELETTER_MODEL) {
					holder.adapter_message_right_progress.setTag("");
					holder.adapter_message_right_textimage_layout.setVisibility(View.GONE);
					holder.adapter_message_right_text.setVisibility(View.VISIBLE);
					holder.adapter_message_right_location_layout.setVisibility(View.GONE);
					holder.adapter_message_right_module_layout.setVisibility(View.GONE);
					holder.adapter_message_right_bussinesscard_layout.setVisibility(View.GONE);
					holder.adapter_message_right_text.setText(models.get(position).getPrivate_letter_usercontent());
				}
				else if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.MODUAL_MODEL) {
					holder.adapter_message_right_progress.setTag("");
					holder.adapter_message_right_textimage_layout.setVisibility(View.GONE);
					holder.adapter_message_right_text.setVisibility(View.GONE);
					holder.adapter_message_right_location_layout.setVisibility(View.GONE);
					holder.adapter_message_right_bussinesscard_layout.setVisibility(View.GONE);
					holder.adapter_message_right_module_layout.setVisibility(View.VISIBLE);
					try {
						JSONObject obj=new JSONObject(models.get(position).getPrivate_letter_usercontent());
						holder.adapter_message_right_module_title.setText(obj.getString("resource_title"));
						holder.adapter_message_right_module_content.setText(obj.getString("resource_content"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.LOCATION_MODEL) {
					holder.adapter_message_right_progress.setTag("");
					holder.adapter_message_right_textimage_layout.setVisibility(View.GONE);
					holder.adapter_message_right_module_layout.setVisibility(View.GONE);
					holder.adapter_message_right_location_layout.setVisibility(View.VISIBLE);
					holder.adapter_message_right_location_layout.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj=new JSONObject(models.get(pos_).getPrivate_letter_usercontent());
								Intent intent=new Intent(context, CurrentLocationDetailActivity.class);
								Bundle bundle=new Bundle();
								bundle.putString("address", obj.getString("address"));
								bundle.putDouble("lat", obj.getDouble("lat"));
								bundle.putDouble("lng", obj.getDouble("lng"));
								intent.putExtras(bundle);
								context.startActivity(intent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}});
					holder.adapter_message_right_bussinesscard_layout.setVisibility(View.GONE);
					holder.adapter_message_right_text.setVisibility(View.GONE);
					try {
						JSONObject obj=new JSONObject(models.get(position).getPrivate_letter_usercontent());
						holder.adapter_message_right_currentlocation_title.setText(obj.getString("title").equals("��ǰλ��")?"λ�÷���":obj.getString("title"));				
						holder.adapter_message_right_currentlocation_content.setText(obj.getString("address"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(models.get(position).getPrivate_letter_type()==CustomPushReceiver.BUSSINESS_MODEL) {
					holder.adapter_message_right_progress.setTag("");
					holder.adapter_message_right_textimage_layout.setVisibility(View.GONE);
					holder.adapter_message_right_text.setVisibility(View.GONE);
					holder.adapter_message_right_location_layout.setVisibility(View.GONE);
					holder.adapter_message_right_bussinesscard_layout.setVisibility(View.VISIBLE);
					holder.adapter_message_right_bussinesscard_layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj=new JSONObject(models.get(pos_).getPrivate_letter_usercontent());
								Intent intent=new Intent(context, BussinessCardActivity.class);
								Bundle bundle=new Bundle();
								bundle.putInt("user_id", obj.getInt("user_id"));
								intent.putExtras(bundle);
								context.startActivity(intent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					holder.adapter_message_right_module_layout.setVisibility(View.GONE);
					try {
						JSONObject obj=new JSONObject(models.get(position).getPrivate_letter_usercontent());
						holder.adapter_message_right_bussinesscard_name.setText(obj.getString("user_name")+"����Ƭ");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				UserModel umodel=DB.getInstance(context).getUserModel();
				bitmapUtils.display(holder.adapter_message_right_image, ParamsManager.bucketName+umodel.getAvatar_large(), config);
			}
		}
		return convertView;
	}
}

class MessageHolder {
	TextView message_item_time=null;
	
	CircleImageView adapter_message_left_image=null;
	EmojiconTextView adapter_message_left_text=null;
	LinearLayout adapter_message_left_layout=null;
	RelativeLayout adapter_message_left_textimage_layout=null;
	ImageView adapter_message_left_textimage=null;
	LinearLayout adapter_message_left_module_layout=null;
	TextView adapter_message_left_module_title=null;
	TextView adapter_message_left_module_content=null;
	LinearLayout adapter_message_left_location_layout=null;
	TextView adapter_message_left_currentlocation_title=null;
	TextView adapter_message_left_currentlocation_content=null;
	LinearLayout adapter_message_left_bussinesscard_layout=null;
	TextView adapter_message_left_bussinesscard_name=null;
	
	EmojiconTextView adapter_message_right_text=null;
	CircleImageView adapter_message_right_image=null;
	LinearLayout adapter_message_right_layout=null;
	RelativeLayout adapter_message_right_textimage_layout=null;
	ImageView adapter_message_right_textimage=null;
	ProgressBar adapter_message_right_progress=null;
	LinearLayout adapter_message_right_module_layout=null;
	TextView adapter_message_right_module_title=null;
	TextView adapter_message_right_module_content=null;
	LinearLayout adapter_message_right_location_layout=null;
	TextView adapter_message_right_currentlocation_title=null;
	TextView adapter_message_right_currentlocation_content=null;
	LinearLayout adapter_message_right_bussinesscard_layout=null;
	TextView adapter_message_right_bussinesscard_name=null;
	
	LinearLayout adapter_message_subscribe_layout=null;
	TextView adapter_message_subscribe_type=null;
	TextView adapter_message_subscribe_title=null;
	ImageView adapter_message_subscribe_image=null;
	TextView adapter_message_subscribe_content=null;
	RelativeLayout adapter_message_subscribe_openurl=null;
}
