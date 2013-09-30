package org.misty.rc.Qiitabian;

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
    private int _mode;

    private String _url_name;
    private String _token;
    private View _footer;
    private LayoutInflater _infrater;

    private ItemAdapter _adapter;

    private static final int READING = 0;
    private static final int DONE = 1;

    private View getFooter() {
        if (_footer == null) {
            _footer = _infrater.inflate(R.layout.list_footer, null);
        }
        return _footer;
    }

    public ContentFragment(Context context, int mode) {
        this.context = context;
        this._mode = mode;
        this._infrater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this._adapter = new ItemAdapter(this.context, R.layout.content_list_row);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this._url_name = getArguments().getString(Auth.URL_NAME);
        this._token = getArguments().getString(Auth.TOKEN);

        query();
    }

    private boolean isReading = false;

    private void query() {
        isReading = true;
        changeFooterState(READING);
        GsonRequest request = GsonRequest.GET(
                QiitaAPI.getItems(_mode, _token),
                Item[].class,
                itemListener,
                GsonRequest.errorListener
        );
        VolleyHolder.getRequestQueue(context).add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_fragment, container, false);
        ((ListView)root.findViewById(android.R.id.list)).addFooterView(getFooter()); //なんか違う気がする
        ((ListView)root.findViewById(android.R.id.list)).setOnScrollListener(new scrollEnd());
        setListAdapter(_adapter);

        return root;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = (Item) l.getItemAtPosition(position);
        Log.d("qiita", "position: " + position + " ,title: " + item.title);

        DetailFragment fragment = new DetailFragment(context, item);
        FragmentTransaction ts = getFragmentManager().beginTransaction();
        ts.replace(R.id.content_frame, fragment, App.TAG_DETAIL);
        ts.addToBackStack(null);
        ts.commit();
    }

    private void changeFooterState(int mode) {
        switch (mode) {
            case READING:
                _footer.findViewById(R.id.footer_read_progress).setVisibility(View.VISIBLE);
                _footer.findViewById(R.id.footer_read_next).setVisibility(View.INVISIBLE);
                break;
            case DONE:
                _footer.findViewById(R.id.footer_read_progress).setVisibility(View.INVISIBLE);
                _footer.findViewById(R.id.footer_read_next).setVisibility(View.VISIBLE);
        }
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

            if(totalItemCount == firstVisibleItem + visibleItemCount) {
                query();
            }
        }
    }

    GsonRequest.Listener<Item[]> itemListener = new GsonRequest.Listener<Item[]>() {
        @Override
        public void onResponse(Item[] items, Map<String, String> header) {
            _adapter.addAll(items);
            changeFooterState(DONE);
            getListView().invalidateViews();
            isReading = false;
            changeFooterState(DONE);
        }
    };
}
