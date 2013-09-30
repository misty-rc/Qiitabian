package org.misty.rc.Qiitabian;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.misty.rc.Qiitabian.models.Item;

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

    public DetailFragment(Context context, Item item) {
        this.context = context;
        this.item = item;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.detail_fragment, container, false);

        ((TextView)root.findViewById(R.id.detail_content)).setText(item.body);
        return root;
    }
}
