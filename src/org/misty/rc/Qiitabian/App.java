package org.misty.rc.Qiitabian;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import com.deploygate.sdk.DeployGate;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/26
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class App extends Application {

    public static final String TAG_CONTENT = "tag_content";
    public static final String TAG_DETAIL = "tag_detail";
    public static final String API_URL = "api_url";
    public static final String UNKNOWN = "unknown";


    private String PREF_TOP_VIEW_KEY;
    private SharedPreferences preference;

    @Override
    public void onCreate() {
        super.onCreate();
        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        DeployGate.install(this, null, true);

        PREF_TOP_VIEW_KEY = getString(R.string.pref_top_view_key);
    }

    public String getTopViewDefaultKey() {
        return PREF_TOP_VIEW_KEY;
    }

    public int getTopViewDefault() {
        return Integer.parseInt(preference.getString(PREF_TOP_VIEW_KEY, "99"));
    }
}
