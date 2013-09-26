package org.misty.rc.Qiitabian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.misty.rc.Qiitabian.models.Item;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/26
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    private LayoutInflater inflater;
    private int layout;
    private Item[] items;
    private ImageLoader imageLoader;

    public ItemAdapter(Context context, int textViewResourceId, Item[] objects) {
        super(context, textViewResourceId, objects);

        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = textViewResourceId;
        this.items = objects;

        this.imageLoader = new ImageLoader(VolleyHolder.getRequestQueue(context), new QiitaImageCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null) {
            view = this.inflater.inflate(this.layout, null);
        }

        //profile icon
        ((NetworkImageView)view.findViewById(R.id.item_icon))
                .setImageUrl(items[position].user.profile_image_url, imageLoader);

        //item state
        ((TextView)view.findViewById(R.id.account_state))
                .setText(items[position].created_at_in_words);

        //title
        ((TextView)view.findViewById(R.id.item_title))
                .setText(items[position].title);

        ((TextView)view.findViewById(R.id.item_tags))
                .setText(items[position].tags.toString());

        return view;
    }

    @Override
    public void add(Item object) {
        super.add(object);
    }
}
