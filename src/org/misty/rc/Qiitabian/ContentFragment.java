package org.misty.rc.Qiitabian;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import org.misty.rc.Qiitabian.models.Auth;
import org.misty.rc.Qiitabian.models.Item;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/27
 * Time: 9:32
 * To change this template use File | Settings | File Templates.
 */

public class ContentFragment extends ListFragment {

    private Context context;
//    private int _mode;

    private String _url_name;
    private String _token;
    private String _url;
    private View _footer;
    private LayoutInflater _infrater;
    private App app;
    private ItemAdapter _adapter;

    private static final int READING = 0;
    private static final int DONE = 1;

    private boolean isReading = false;
    private boolean isCreatedView = false;

    private StateHolder stateHolder;
    private class StateHolder {
        public String url;
        public int page;
        public boolean deadend = false;
    }

    public interface ContentChangeListener {
        public void changeDetailFragment(String uuid);
    }

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (ContentChangeListener)activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString()
                    + "must implement ContentChangeListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("qiita", "fragment:onCreate");
        super.onCreate(savedInstanceState);

        //retain => true
        setRetainInstance(true);
        app = (App)getActivity().getApplication();

        //adapter init
        this._adapter = new ItemAdapter(getActivity(), R.layout.content_list_row);
        setListAdapter(_adapter);

        this._url_name = getArguments().getString(Auth.URL_NAME);
        this._token = getArguments().getString(Auth.TOKEN);
        this._url = getArguments().getString(App.API_URL);
        if(stateHolder == null) {
            Log.d("qiita", "fragment:stateholder init");
            stateHolder = new StateHolder();
            stateHolder.page = 1;
            stateHolder.url = _url;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("qiita", "fragment:onCreateView");
        View root = inflater.inflate(R.layout.content_fragment, container, false);
        ListView list = (ListView)root.findViewById(android.R.id.list);
        list.addFooterView(getFooter(inflater)); //なんか違う気がする
        list.setOnScrollListener(new scrollEnd());
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("qiita", "fragment:onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    private ContentChangeListener callback;


    @Override
    public void onPause() {
        Log.d("qiita", "fragment:onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("qiita", "fragment:onResume");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("qiita", "fragment:onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = (Item) l.getItemAtPosition(position);
        Log.d("qiita", "position: " + position + " ,title: " + item.title);

        callback.changeDetailFragment(item.uuid);
    }


    private class scrollEnd implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d("qiita", "onScrollStateChanged" + ", state: " + scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.d("qiita", "onScroll" + ", first: " + firstVisibleItem + ", visible count: " + visibleItemCount + ", total: " + totalItemCount);

            if(isReading) {
                return;
            }

            if(totalItemCount == firstVisibleItem + visibleItemCount && totalItemCount > 0) {
                if(!stateHolder.deadend) {
                    query();
                }
            }
        }
    }

    private View getFooter(LayoutInflater inflater) {
        if (_footer == null) {
            _footer = inflater.inflate(R.layout.list_footer, null);
        }
        return _footer;
    }

    private void query() {
        Log.d("qiita", "fragment:query");
        isReading = true;
        GsonRequest request = GsonRequest.GET(
                QiitaAPI.queryBuild(stateHolder.url, stateHolder.page),
                Item[].class,
                itemListener,
                GsonRequest.errorListener
        );
        VolleyHolder.getRequestQueue(context).add(request);
    }

    GsonRequest.Listener<Item[]> itemListener = new GsonRequest.Listener<Item[]>() {
        @Override
        public void onResponse(Item[] items, Map<String, String> header) {
            Log.d("qiita", "GsonRequest => onResponse");
            _adapter.addAll(items);
            getListView().invalidateViews();

            if(items.length == 0 || items.length < QiitaAPI.PER_PAGE) {
                stateHolder.deadend = true;
                stateHolder.page = 0;
            } else {
                stateHolder.page += 1;
            }
            isReading = false;
        }
    };
}
