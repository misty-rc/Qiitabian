package org.misty.rc.Qiitabian;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import org.markdown4j.Markdown4jProcessor;
import org.misty.rc.Qiitabian.models.Auth;
import org.misty.rc.Qiitabian.models.Item;
import org.misty.rc.Qiitabian.models.ItemDetail;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/30
 * Time: 9:17
 * To change this template use File | Settings | File Templates.
 */
public class DetailFragment extends Fragment {

    private Context context;
    private Item item;
    private LayoutInflater inflater;

    private String _url_name;
    private String _token;
    private String _uuid;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        this._url_name = getArguments().getString(Auth.URL_NAME);
        this._token = getArguments().getString(Auth.TOKEN);
        this._uuid = getArguments().getString(Item.UUID);
        this.context = getActivity().getApplicationContext();
        if(savedInstanceState == null) {

        }
    }

    //    public DetailFragment(Context context, Item item) {
//        this.context = context;
//        this.item = item;
//        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }

    private View _root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = _root = inflater.inflate(R.layout.detail_fragment, container, false);

        ((TextView)root.findViewById(R.id.detail_content)).setText("hogehoge");

        query();
        return root;
    }

    private void query() {
        GsonRequest request = GsonRequest.GET(
                QiitaAPI.getDetail(_uuid),
                ItemDetail.class,
                detailListener,
                GsonRequest.errorListener
        );
        VolleyHolder.getRequestQueue(context).add(request);
    }

    GsonRequest.Listener<ItemDetail> detailListener = new GsonRequest.Listener<ItemDetail>() {
        @Override
        public void onResponse(ItemDetail response, Map<String, String> header) {
            String html = null;
            try {
                html = new Markdown4jProcessor().process(response.raw_body);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Log.d("qiita", html);
            ((WebView)_root.findViewById(R.id.detail_view)).loadData(response.body, "text/html; charset=utf-8", "utf-8");

//            ((WebView)_root.findViewById(R.id.detail_view)).loadData(html, "text/html; charset=utf-8", "utf-8");
//            ((WebView)_root.findViewById(R.id.detail_view)).loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
            ((TextView)_root.findViewById(R.id.detail_content)).setText(response.title);

        }
    };
}
