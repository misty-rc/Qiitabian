package org.misty.rc.Qiitabian;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.misty.rc.Qiitabian.models.Auth;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/10/01
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public class ContentListFragment extends ListFragment {

    public static ContentListFragment newInstance() {
        ContentListFragment instance = new ContentListFragment();
        return instance;
    }

    private ItemAdapter _adapter;
    private String _token;
    private String _url_name;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _adapter = new ItemAdapter(getActivity(), R.layout.content_list_row);
        _token = getArguments().getString(Auth.TOKEN);
        _url_name = getArguments().getString(Auth.URL_NAME);

        setListAdapter(_adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.id.content_frame, container, false);

        return root;
    }

}
