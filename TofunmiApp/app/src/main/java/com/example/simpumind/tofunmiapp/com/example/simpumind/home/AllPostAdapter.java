package com.example.simpumind.tofunmiapp.com.example.simpumind.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simpumind.tofunmiapp.R;

/**
 * Created by simpumind on 4/8/15.
 */
public class AllPostAdapter extends BaseAdapter{

    private LayoutInflater   mLayoutInflater;

    public AllPostAdapter(Context context){
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return POSTS.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mVHolder;
        if(convertView == null){
            convertView=mLayoutInflater.inflate(R.layout.custom_all, parent, false);
            mVHolder=new ViewHolder();
            mVHolder.mPost=(TextView)convertView.findViewById(R.id.post);
            mVHolder.mLastSeen = (TextView)convertView.findViewById(R.id.lastseen);
            mVHolder.mUsername=(TextView)convertView.findViewById(R.id.username);
            convertView.setTag(mVHolder);
        }else{
            mVHolder=(ViewHolder)convertView.getTag();
        }
        mVHolder.mUsername.setText(NAMES[position]);
        mVHolder.mPost.setText(POSTS[position]);
        mVHolder.mLastSeen.setText(TIME[position]);

        return convertView;
    }
    static class ViewHolder{
        TextView mPost;
        TextView mLastSeen;
        TextView mUsername;
    }


    static final String[] POSTS = new String[] {"by adding an ability which allows the user" +
            " picking up a file to be sent along with the e-mail message as an attachment. The " +
            "email form will have an additional field with which the user can select a file " +
            "on his computer, like in the following screenshot",
            "Workflow is similar to the sample application described in the tutorial Sending e-mail with JSP, Servlet and " +
                    "JavaMail, plus with some changes for handling file upload and attaching the file to the e-mail message, as follows:",
            "Frustration arise the moment you realise that you " +
                    "cannot use a custom font by setting TextView's " +
                    "and EditText's android:typeface XML-attribute. " +
                    "AnyTextView is here to relieve your pain",
            "Form validation and feedback library for Android." +
                    " Provides .setText for more than just TextView" +
                    " and EditText widgets. Provides easy means to validate" +
                    " with dependencies."
    };

    static final String[] NAMES = new String[]{
            "Email with attachment", "JavaMail, easy use", "Do not get frustrated", "Validating feedback in Android"
    };

    static final String[] TIME = new String[]{
      "11:00pm", "12:45am", "02:43pm", "03:45pm"
    };
}
