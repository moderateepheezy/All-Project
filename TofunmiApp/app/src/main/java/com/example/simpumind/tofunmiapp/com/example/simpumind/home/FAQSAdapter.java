package com.example.simpumind.tofunmiapp.com.example.simpumind.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.simpumind.tofunmiapp.R;

/**
 * Created by simpumind on 4/8/15.
 */
public class FAQSAdapter extends BaseAdapter{

    private LayoutInflater   mLayoutInflater;

    public FAQSAdapter(Context context){
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return QUESTIONS.length;
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
            convertView=mLayoutInflater.inflate(R.layout.custom_faqs, parent, false);
            mVHolder=new ViewHolder();
            mVHolder.mQuestions = (TextView)convertView.findViewById(R.id.question);
            mVHolder.mAnswers=(TextView)convertView.findViewById(R.id.answer);
            convertView.setTag(mVHolder);
        }else{
            mVHolder=(ViewHolder)convertView.getTag();
        }
        mVHolder.mAnswers.setText(ANSWERS[position]);
        mVHolder.mQuestions.setText(QUESTIONS[position]);

        return convertView;
    }
    static class ViewHolder{
        TextView mQuestions;
        TextView mAnswers;
    }


    static final String[] QUESTIONS = new String[] {
            "How can i subscribe to the channel",
            "I am having trouble logging out",
            "How can i end a session",
            "The timeline changes, Why?"
    };

    static final String[] ANSWERS = new String[]{
            "Email with attachment", "JavaMail, easy use", "Do not get frustrated", "Validating feedback in Android"
    };

}
