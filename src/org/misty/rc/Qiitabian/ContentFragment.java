package org.misty.rc.Qiitabian;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public ContentFragment(Context context, int mode) {
        this.context = context;
        this._mode = mode;
    }

    private void query() {
        GsonRequest request = GsonRequest.GET(
                QiitaAPI.getItems(_mode, _token),
                Item[].class,
                itemListener,
                GsonRequest.errorListener
        );
        VolleyHolder.getRequestQueue(context).add(request);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this._url_name = getArguments().getString(Auth.URL_NAME);
        this._token = getArguments().getString(Auth.TOKEN);
        query();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_fragment, container, false);
        return root;
    }

    GsonRequest.Listener<Item[]> itemListener = new GsonRequest.Listener<Item[]>() {
        @Override
        public void onResponse(Item[] items, Map<String, String> header) {
            setListAdapter(new ItemAdapter(context, R.layout.content_list_row, items));
        }
    };

}
