package org.simpumind.com.yctalumniconnect.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpumind.com.yctalumniconnect.ItemNewsList;
import org.simpumind.com.yctalumniconnect.R;
import org.simpumind.com.yctalumniconnect.RoundedImageView;
import org.simpumind.com.yctalumniconnect.app.AppConst;
import org.simpumind.com.yctalumniconnect.app.AppController;
import org.simpumind.com.yctalumniconnect.groupchat.ForumActivity;
import org.simpumind.com.yctalumniconnect.groupchat.helper.Constants;
import org.simpumind.com.yctalumniconnect.helper.MyAdapter;
import org.simpumind.com.yctalumniconnect.model.News;
import org.simpumind.com.yctalumniconnect.picasa.model.Category;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

private SliderLayout mDemoSlider;
    private List<ParseObject> ob;

    Intent i;

    AlertDialog actions;
/*

    Bitmap imageBitmap;

    RoundedImageView rImage;
*/

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 100;

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
            TAG_GPHOTO_ID = "gphoto$id", TAG_T = "$t",
            TAG_ALBUM_TITLE = "title";

     ArrayList<ItemNewsList> news;



    private ParseUser currentUser;

    private static String ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }
            getAllUser();
        getAllNews();

        if (currentUser != null) {
            //get current user username and turn it to string
            ct = currentUser.getUsername(); //this line was outside earlier
            //rest of code
        }

        loadAlbum();
        joinChat();

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_home, null);
        if(ct != null) {
            ((TextView) nav_header.findViewById(R.id.user)).setText(ct);
        }
        else{
            loadLoginView();
        }
        /*rImage = (RoundedImageView) nav_header.findViewById(R.id.userIcon);
        rImage.setImageResource(R.drawable.black);
        rImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserImageFromParse();
            }
        });*/
        navigationView.addHeaderView(nav_header);


        navigationView.setNavigationItemSelectedListener(this);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.slide1);
        file_maps.put("Big Bang Theory", R.drawable.slide2);
        file_maps.put("House of Cards", R.drawable.slide3);
        file_maps.put("House of Cards", R.drawable.slide4);
        file_maps.put("House of Cards", R.drawable.slide5);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i){
                    case 0:
                        intent = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.main, Fragment.instantiate(HomeActivity.this, "org.simpumind.com.yctalumniconnect.ReminisceFragment"));
                        tx.addToBackStack(null);
                        tx.commit();
                        break;
                    case 2:
                         intent = new Intent(HomeActivity.this, ForumActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(HomeActivity.this, AlbumActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
//                        Toast.makeText(HomeActivity.this, "Testing" + AppConst.allNews.get(0).getNews(), Toast.LENGTH_SHORT).show();
                        intent = new Intent(HomeActivity.this, NewsActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        showAbout();
                        break;
                    case 6:
                        intent = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(HomeActivity.this, AllMembersActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
                return false;
            } else {
                getSupportFragmentManager().popBackStack();
                removeCurrentFragment();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void removeCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment currentFrag = getSupportFragmentManager().findFragmentByTag("org.simpumind.com.yctalumniconnect.ReminisceFragment");
    }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_logout){
           /* ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });*/
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home){
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_reminisce) {
            Toast.makeText(this, "Username: " + ct, Toast.LENGTH_LONG).show();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(HomeActivity.this, "org.simpumind.com.yctalumniconnect.ReminisceFragment"));
            tx.commit();

        } else if (id == R.id.nav_album) {
            i = new Intent(HomeActivity.this, AlbumActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_forum) {
            i = new Intent(HomeActivity.this, AllMembersActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_about) {
            showAbout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    public void loadAlbum(){

        String url = AppConst.URL_PICASA_ALBUMS
                .replace("_PICASA_USER_", AppController.getInstance()
                        .getPrefManger().getGoogleUserName());

        Log.d(TAG, "Albums request url: " + url);

        // Preparing volley's json object request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Albums Response: " + response.toString());
                List<Category> albums = new ArrayList<Category>();
                try {
                    // Parsing the json response
                    JSONArray entry = response.getJSONObject(TAG_FEED)
                            .getJSONArray(TAG_ENTRY);

                    // loop through albums nodes and add them to album
                    // list
                    for (int i = 0; i < entry.length(); i++) {
                        JSONObject albumObj = (JSONObject) entry.get(i);
                        // album id
                        String albumId = albumObj.getJSONObject(
                                TAG_GPHOTO_ID).getString(TAG_T);

                        // album title
                        String albumTitle = albumObj.getJSONObject(
                                TAG_ALBUM_TITLE).getString(TAG_T);

                        Category album = new Category();
                        album.setId(albumId);
                        album.setTitle(albumTitle);

                        // add album to list
                        albums.add(album);

                        Log.d(TAG, "Album Id: " + albumId
                                + ", Album Title: " + albumTitle);
                    }

                    // Store albums in shared pref
                    AppController.getInstance().getPrefManger()
                            .storeCategories(albums);


                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.msg_unknown_error),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error: " + error.getMessage());

                // show error toast
                Toast.makeText(getApplicationContext(),
                        getString(R.string.splash_error),
                        Toast.LENGTH_LONG).show();

                // Unable to fetch albums
                // check for existing Albums data in Shared Preferences
                if (AppController.getInstance().getPrefManger()
                        .getCategories() != null && AppController.getInstance().getPrefManger()
                        .getCategories().size() > 0) {
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG).show();
                    // Albums data not present in the shared preferences
                    // Launch settings activity, so that user can modify
                    // the settings

                    Intent i = new Intent(HomeActivity.this,
                            SettingsActivity.class);
                    // clear all the activities
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

            }
        });

        // disable the cache for this request, so that it always fetches updated
        // json
        jsonObjReq.setShouldCache(false);

        // Making the request
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getAllUser(){
        AppConst.people = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
       // query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (ParseUser mixs : objects) {
                        org.simpumind.com.yctalumniconnect.model.AllMember dr = new org.simpumind.com.yctalumniconnect.model.AllMember();
                        dr.setMiddleName((String) mixs.get("otherName"));
                        dr.setFirstName((String) mixs.get("firstName"));
                        dr.setLastName((String) mixs.get("lastName"));
                        dr.setCourse((String) mixs.get("course"));
                        dr.setEmail((String) mixs.get("email"));
                        dr.setSex((String) mixs.get("sex"));
                        dr.setUserName(mixs.getUsername());
                        dr.setYearOfDegree((String) mixs.get("yearOfDegree"));
                        dr.setPhone((String) mixs.get("phone"));

                        AppConst.people.add(dr);
                    }
                }
            }
        });
    }

    private void getAllNews(){
        try {
            AppConst.allNews = new ArrayList<>();
            ParseQuery<ParseObject> query = new ParseQuery<>("News");
           // query.fromLocalDatastore();
            query.addAscendingOrder("createdAt");
            ob = query.find();
            for(ParseObject nt: ob){
                News n = new News();
                n.setHeading((String)nt.get("heading"));
                n.setNews((String) nt.get("news"));
                n.setTime(nt.getCreatedAt().toString());

                AppConst.allNews.add(n);
            }
        }catch (ParseException e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    public void joinChat(){


        SharedPreferences sp = getSharedPreferences(Constants.CHAT_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constants.CHAT_USERNAME, ct);
        edit.apply();
    }

    protected void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.back_image);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    public void getUserImageFromParse() {

        CharSequence colors[] = new CharSequence[] {"Take Picture", "Get Image From Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch (which) {
                    case 0: // Delete
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        break;
                    case 1: // Copy
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //from the gallery
            case SELECT_PHOTO:
                if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                 rImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
            //from the camera
            case CAMERA_REQUEST:
                if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    rImage.setImageBitmap(photo);
                }
                break;
        }
    }*/

}
