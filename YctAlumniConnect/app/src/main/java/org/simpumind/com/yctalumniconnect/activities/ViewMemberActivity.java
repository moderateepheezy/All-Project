package org.simpumind.com.yctalumniconnect.activities;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftinc.kit.widget.AspectRatioImageView;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import org.simpumind.com.yctalumniconnect.R;
import org.simpumind.com.yctalumniconnect.RoundedImageView;
import org.simpumind.com.yctalumniconnect.model.AllMember;


import com.ftinc.kit.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewMemberActivity extends AppCompatActivity {

    public static final String EXTRA_OS = "extra_member";

    @Bind(R.id.cover)
    RoundedImageView mCover;

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.fname)
    TextView fname;

    @Bind(R.id.mname)
    TextView mname;

    @Bind(R.id.lname)
    TextView lname;

    @Bind(R.id.sex)
    TextView sex;

    @Bind(R.id.yod)
    TextView yod;

    @Bind(R.id.phoneNum)
    TextView phone;

    @Bind(R.id.dep)
    TextView dep;

    @Bind(R.id.emmail)
    TextView email;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private AllMember am;
    private SlidrConfig mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_member);
        ButterKnife.bind(this);

        // Get the status bar colors to interpolate between
        int primary = getResources().getColor(R.color.primaryDark);
        int secondary = getResources().getColor(R.color.red_500);

        // Build the slidr config
        int numPositions = SlidrPosition.values().length;
        SlidrPosition position = SlidrPosition.values()[Utils.getRandom().nextInt(numPositions)];
       // mTitle.setText(position.name());

        mConfig = new SlidrConfig.Builder()
                .primaryColor(primary)
                .secondaryColor(secondary)
                .position(position)
                .velocityThreshold(2400)
                .distanceThreshold(.25f)
                .edge(true)
                .touchSize(Utils.dpToPx(this, 32))
                .build();

        int color = getResources().getColor(R.color.primary);

        getWindow().setStatusBarColor(color);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        am = getIntent().getParcelableExtra(EXTRA_OS);
        if(savedInstanceState != null) am = savedInstanceState.getParcelable(EXTRA_OS);

        //Set layout content
        fname.setText(am.firstName);
        lname.setText(am.lastName);
        mname.setText(am.middleName);
        email.setText(am.email);
        dep.setText(am.course);
        phone.setText(am.phone);
        sex.setText(am.sex);
        yod.setText(am.yearOfDegree );
        mTitle.setText(am.getUserName());

        Glide.with(this)
                .load(R.drawable.contact_default)
                .crossFade()
                .into(mCover);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_OS, am);
    }

}
