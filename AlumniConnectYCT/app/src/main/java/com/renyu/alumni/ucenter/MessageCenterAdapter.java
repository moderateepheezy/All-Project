package com.renyu.alumni.ucenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.ReceiverPrivateLetterModel;
import com.rockerhieu.emojicon.EmojiconTextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageCenterAdapter extends BaseAdapter {
	
	ArrayList<ReceiverPrivateLetterModel> messageMap=null;
	Context context=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	public MessageCenterAdapter(Context context, ArrayList<ReceiverPrivateLetterModel> messageMap) {
		this.context=context;
		this.messageMap=messageMap;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_user_press));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_user_press));	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageMap.size();
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
		MessageCenterHolder holder=null;
		if(convertView==null) {
			holder=new MessageCenterHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_messagecenter, null);
			holder.adapter_messagecenter_content=(EmojiconTextView) convertView.findViewById(R.id.adapter_messagecenter_content);
			holder.adapter_messagecenter_time=(TextView) convertView.findViewById(R.id.adapter_messagecenter_time);
			holder.adapter_messagecenter_title=(TextView) convertView.findViewById(R.id.adapter_messagecenter_title);
			holder.adapter_messagecenter_image=(ImageView) convertView.findViewById(R.id.adapter_messagecenter_image);
			holder.adapter_messagecenter_icon=(TextView) convertView.findViewById(R.id.adapter_messagecenter_icon);
			convertView.setTag(holder);
		}
		else {
			holder=(MessageCenterHolder) convertView.getTag();
		}
		holder.adapter_messagecenter_title.setText(messageMap.get(position).getUsername());
		bitmapUtils.display(holder.adapter_messagecenter_image, ParamsManager.bucketName+messageMap.get(position).getAvatar_large(), config);
		holder.adapter_messagecenter_time.setText(CommonUtils.getPrivateMessageCenterTimeFormat(messageMap.get(position).getTime()));
		holder.adapter_messagecenter_content.setText(replaceUBB(messageMap.get(position).getContent()));
		if(messageMap.get(position).getNoReadCount()>0) {
			holder.adapter_messagecenter_icon.setVisibility(View.VISIBLE);
			holder.adapter_messagecenter_icon.setText(""+messageMap.get(position).getNoReadCount());
		}
		else {
			holder.adapter_messagecenter_icon.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	public static String replaceUBB(String text) {
		text = CommonUtils.convertNull(text);
		Pattern pattern = Pattern.compile("\\[emoji\\][0-9a-zA-Z]+,?[0-9a-zA-Z]*\\[/emoji\\]");
		Matcher matcher = pattern.matcher(text);
		String subStr = "";
		String encodeStr = "";
		String newStr = "";
		String[] subArr = new String[0];
		while(matcher.find()) {
			subStr = matcher.group();
			encodeStr = subStr.substring(7, subStr.lastIndexOf("[/emoji]"));
			subArr = encodeStr.split(",");
			if(subArr.length==1)
				newStr = newString(Integer.parseInt(subArr[0], 16));
			else
				newStr = newString(Integer.parseInt(subArr[0], 16))+newString(Integer.parseInt(subArr[1], 16));
			
			text = text.replace(matcher.group(), newStr);
		}
		return text;
	}
	
	public static final String newString(int codePoint) {
		if (Character.charCount(codePoint) == 1)
		{
			return String.valueOf((char)codePoint);
		}
		else
		{
			return new String(Character.toChars(codePoint));
		}
	}

}

class MessageCenterHolder {
	ImageView adapter_messagecenter_image=null;
	TextView adapter_messagecenter_title=null;
	TextView adapter_messagecenter_time=null;
	EmojiconTextView adapter_messagecenter_content=null;
	TextView adapter_messagecenter_icon=null;
}
