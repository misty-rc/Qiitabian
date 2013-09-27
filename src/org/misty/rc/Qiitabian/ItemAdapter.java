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
    private ImageLoader imageLoader;

    public ItemAdapter(Context context, int textViewResourceId, Item[] objects) {
        super(context, textViewResourceId, objects);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = textViewResourceId;
        this.imageLoader = new ImageLoader(VolleyHolder.getRequestQueue(context), new QiitaImageCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Item item = getItem(position);

        if(convertView == null) {
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.itemIcon = (NetworkImageView)convertView.findViewById(R.id.item_icon);
            holder.accountState = (TextView)convertView.findViewById(R.id.account_state);
            holder.itemTitle = (TextView)convertView.findViewById(R.id.item_title);
            holder.itemTags = (TextView)convertView.findViewById(R.id.item_tags);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.itemIcon.setImageUrl(item.user.profile_image_url, imageLoader);
        holder.accountState.setText(item.created_at_in_words);
        holder.itemTitle.setText(item.title);
        holder.itemTags.setText(item.tags.toString());

        return convertView;
    }

    @Override
    public void add(Item object) {
        super.add(object);
    }

    private class ViewHolder {
        NetworkImageView itemIcon;
        TextView accountState;
        TextView itemTitle;
        TextView itemTags;
    }
}
