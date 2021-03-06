package org.misty.rc.Qiitabian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.misty.rc.Qiitabian.models.Tag;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/25
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class TagAdapter extends ArrayAdapter<Tag> {
    private LayoutInflater inflater;
    private int layout;

    public TagAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = textViewResourceId;
    }

//    public TagAdapter(Context context, int textViewResourceId, Tag[] objects) {
//        super(context, textViewResourceId, objects);
//        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.layout = textViewResourceId;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Tag tag = getItem(position);

        if(convertView == null) {
            view = this.inflater.inflate(this.layout, null);
        }
        ((TextView)view.findViewById(R.id.tagname)).setText(tag.name);
        return view;
    }

    @Override
    public void add(Tag object) {
        super.add(object);
    }
}
