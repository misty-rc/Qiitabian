package org.misty.rc.Qiitabian;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.misty.rc.Qiitabian.models.*;

import java.util.Map;

public class MainActivity extends Activity implements ContentFragment.ContentChangeListener {
    private String _url_name;
    private String _token;

    private App app;
    private SharedPreferences preferences;
    private FragmentManager fragmentManager;
    private GestureDetector gestureDetector;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private TagAdapter tagAdapter;
    private LinearLayout drawer;
    private ActionBarDrawerToggle drawerToggle;

    private ImageLoader imageLoader;

    private Bundle _fragmentArgs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("qiita", "activity:onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //init application
        initialize();

        //for debug
        Map<String, ?> pref = preferences.getAll();
        Log.d("qiita", pref.toString());

        //test get userinfo
        if(savedInstanceState == null) {
            Log.d("qiita", "activity:state null");
            ContentFragment fragment = ContentFragment.newInstance();
            fragment.setArguments(_fragmentArgs);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "content").commit();
        } else {
            Log.d("qiita", "activity:state" + savedInstanceState.toString());

//            fragmentChanger(0);
        }
    }


    private void initialize() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _url_name = preferences.getString(Auth.URL_NAME, null);
        _token = preferences.getString(Auth.TOKEN, null);

        app = (App)this.getApplication();

        //set tokenset
        _fragmentArgs = new Bundle();
        _fragmentArgs.putString(Auth.URL_NAME, _url_name);
        _fragmentArgs.putString(Auth.TOKEN, _token);
        _fragmentArgs.putString(App.API_URL, QiitaAPI.getTopViewItems(app.getTopViewDefault(), _token));
//        _fragmentArgs.putString(app.getTopViewDefaultKey(), app.getTopViewDefault());

        gestureDetector = new GestureDetector(this, new FlingHandler());

        //imageLoader for NetworkImageView
        imageLoader = new ImageLoader(VolleyHolder.getRequestQueue(this), new QiitaImageCache());

        //FragmentManager
        fragmentManager = getFragmentManager();

        //main layout with NavigationDrawer
        drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //navigation drawer
        drawer = (LinearLayout) findViewById(R.id.left_drawer_layout);

        //list in navigation drawer
        drawerList = (ListView) findViewById(R.id.left_drawer_list);
        drawerList.setOnItemClickListener(new TagClickListener());
        tagAdapter = new TagAdapter(this, R.layout.tag_item);
        drawerList.setAdapter(tagAdapter);
        setTagList();

        //profile -> own post
        LinearLayout profile = (LinearLayout)findViewById(R.id.profile);
        profile.setOnClickListener(new ProfileClickListener());

        //profile -> stocks
        TextView stocks = (TextView)findViewById(R.id.profile_stocks);
        stocks.setOnClickListener(new StocksClickListener());

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setProfileIcon(String url) {
        NetworkImageView view = (NetworkImageView) findViewById(R.id.profile_icon);
        view.setImageUrl(url, imageLoader);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return gestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else
            switch (item.getItemId()) {
                case R.id.action_preference:
                    Intent intent = new Intent(this, QiitaPreferenceActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("qiita", "activity:onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("qiita", "activity:onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /*
    * request queue
    *
    * */

    private void setTagList() {
        GsonRequest request = GsonRequest.GET(
                QiitaAPI.getFollowingTags(_url_name, _token),
                Tag[].class,
                tagListener,
                GsonRequest.errorListener);
        VolleyHolder.getRequestQueue(this).add(request);
    }

    private void getUser() {
        GsonRequest request = GsonRequest.GET(
                QiitaAPI.getUser(_token),
                User.class,
                userListener,
                GsonRequest.errorListener);
        VolleyHolder.getRequestQueue(this).add(request);
    }


    /*
    * event listener
    *
    * */

    private class TagClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawer(drawer);
            ListView list = (ListView)parent;
            String item = (String)list.getItemAtPosition(position);
            _fragmentArgs.putString(App.API_URL, QiitaAPI.getTagItems(item, 1));
            //TODO: tag click -> list content with tag
        }
    }

    private class ProfileClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentTransaction ts = fragmentManager.beginTransaction();
            ts.commit();
        }
    }

    private class StocksClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ContentFragment fragment = (ContentFragment)fragmentManager.findFragmentByTag("content");
            if(fragment == null) {
                fragment = ContentFragment.newInstance();
            }
            _fragmentArgs.putString(App.API_URL, QiitaAPI.getTopViewItems(QiitaAPI.STOCKS, _token));
            fragment.setArguments(_fragmentArgs);

            FragmentTransaction ts = fragmentManager.beginTransaction();
            ts.replace(R.id.content_frame, fragment, "content").commit();
        }
    }


    @Override
    public void changeDetailFragment(String uuid) {
        DetailFragment fragment = (DetailFragment)fragmentManager.findFragmentByTag("detail");
        if(fragment == null) {
            fragment = DetailFragment.newInstance();
        }
        _fragmentArgs.putString("uuid", uuid);
        fragment.setArguments(_fragmentArgs);

        FragmentTransaction ts = fragmentManager.beginTransaction();
        ts.addToBackStack(null).replace(R.id.content_frame, fragment, "detail").commit();
    }

    /*
    * volley response listener
    *
    * */

    private GsonRequest.Listener<Tag[]> tagListener = new GsonRequest.Listener<Tag[]>() {
        @Override
        public void onResponse(Tag[] tags, Map<String, String> header) {
            tagAdapter.addAll(tags);
            drawerList.invalidateViews();
        }
    };

    private GsonRequest.Listener<User> userListener = new GsonRequest.Listener<User>() {
        @Override
        public void onResponse(User user, Map<String, String> header) {
            Gson gson = new GsonBuilder().create();
            setProfileIcon(user.profile_image_url);

            String val = gson.toJson(user, user.getClass());

            Bundle args = new Bundle();
            args.putString("debug", val);

            DebugFragment fragment = new DebugFragment();
            fragment.setArguments(args);

            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    };


    /*
    * inner fragment
    *
    * */
    public class DebugFragment extends Fragment {
        public DebugFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.debug_fragment, container, false);

            Bundle args = getArguments();
            String val = args.getString("debug");

            ((TextView) root.findViewById(R.id.debug_textview)).setText(val);

            return root;
        }
    }


    private class FlingHandler implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float dx = Math.abs(velocityX);
            float dy = Math.abs(velocityY);
            if (dx > dy && dx > 200) {
                if (e1.getX() - e2.getX() < 150) {
                    drawerLayout.openDrawer(drawer);
                    return true;
                }
            }
            return false;
        }
    }
}
