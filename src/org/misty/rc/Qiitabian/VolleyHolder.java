package org.misty.rc.Qiitabian;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/24
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public final class VolleyHolder {

    private static RequestQueue _requestQueue;

    private VolleyHolder() {}

    public static RequestQueue getRequestQueue(final Context context) {
        if(_requestQueue == null) {
            _requestQueue = Volley.newRequestQueue(context);
        }

        return _requestQueue;
    }
}
