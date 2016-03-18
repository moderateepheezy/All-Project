package org.simpumind.com.nigeriasenator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class ContentDisplay extends AppCompatActivity {

    private String title;
    private String content;
    TextView textviewTitle;
    TextView txtDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtDetails = (TextView) findViewById(R.id.txtDetails);
        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setIcon(R.drawable.ic_launcher);

        Intent intent = getIntent();
        if(getIntent().hasExtra("history") || getIntent().hasExtra("hisTitle")) {
            setVotedForValue();
        }else if(intent.hasExtra("voted") || intent.hasExtra("votedTitle")){
            setHistoryValue();
        }
        else if(intent.hasExtra("constitue") || intent.hasExtra("conTitle")){
            setElectingSenate();
        }else if(intent.hasExtra("formed") || intent.hasExtra("formTitle")){
            setSensateFormed();
        }else if(intent.hasExtra("earn") || intent.hasExtra("earnTitle")){
            setSenateEarn();
        }
        // do something with the data

    }

    public void setVotedForValue(){
        Intent intent = getIntent();
        String content = intent.getStringExtra("history");
        String title = intent.getStringExtra("hisTitle");
        txtDetails.setText(Html.fromHtml(content));
        txtDetails.setMovementMethod(new ScrollingMovementMethod());
        textviewTitle.setText(title);
    }

    public void setHistoryValue(){
        Intent intent = getIntent();
        String content = intent.getStringExtra("voted");
        String title = intent.getStringExtra("votedTitle");
        txtDetails.setText(Html.fromHtml(content));
        txtDetails.setMovementMethod(new ScrollingMovementMethod());
        textviewTitle.setText(title);
    }

    public void setElectingSenate(){
        Intent intent = getIntent();
        String content = intent.getStringExtra("constitue");
        String title = intent.getStringExtra("conTitle");
        txtDetails.setText(Html.fromHtml(content));
        txtDetails.setMovementMethod(new ScrollingMovementMethod());
        textviewTitle.setText(title);
    }

    public void setSensateFormed(){
        Intent intent = getIntent();
        String content = intent.getStringExtra("formed");
        String title = intent.getStringExtra("formTitle");
        txtDetails.setText(Html.fromHtml(content));
        txtDetails.setMovementMethod(new ScrollingMovementMethod());
        textviewTitle.setText(title);
    }

    public void setSenateEarn(){
        Intent intent = getIntent();
        String content = intent.getStringExtra("earn");
        String title = intent.getStringExtra("earnTitle");
        txtDetails.setText(Html.fromHtml(content));
        txtDetails.setMovementMethod(new ScrollingMovementMethod());
        textviewTitle.setText(title);
    }
}
