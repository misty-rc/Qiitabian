package org.misty.rc.Qiitabian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.misty.rc.Qiitabian.models.Auth;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/24
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //debug
        initialize();

        if(TextUtils.isEmpty(preferences.getString(Auth.TOKEN, null))) {
            setContentView(R.layout.login);
            getActionBar().hide();

            Button button = (Button)findViewById(R.id.login_button);
            button.setOnClickListener(loginClick);

        } else {
            //still login
            changeActivity();
        }
    }

    private void initialize() {
        if(preferences.getBoolean(getString(R.string.pref_delete_pref_key), false)) {
            //delete setting
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
//            editor.remove(Auth.URL_NAME);
//            editor.remove(Auth.TOKEN);
//            editor.putBoolean(getString(R.string.pref_delete_pref_key), false);
            editor.commit();
        }

    }

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            String url_name = ((EditText)findViewById(R.id.url_name)).getText().toString();
            String password = ((EditText)findViewById(R.id.password)).getText().toString();

            GsonRequest request = GsonRequest.POST(
                    QiitaAPI.getToken(),
                    Auth.class,
                    QiitaAPI.getTokenParams(url_name, password),
                    exAuthListener,
                    errorListener
            );

            VolleyHolder.getRequestQueue(getApplication()).add(request);
        }
    };

    private GsonRequest.Listener<Auth> exAuthListener = new GsonRequest.Listener<Auth>() {
        @Override
        public void onResponse(Auth auth, Map<String, String> header) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Auth.TOKEN, auth.token);
            editor.putString(Auth.URL_NAME, auth.url_name);
            editor.commit();

            changeActivity();
        }
    };

    private Response.Listener<Auth> authListener = new Response.Listener<Auth>() {
        @Override
        public void onResponse(Auth auth) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Auth.TOKEN, auth.token);
            editor.putString(Auth.URL_NAME, auth.url_name);
            editor.commit();

            changeActivity();
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(getApplication(), "認証失敗", Toast.LENGTH_LONG).show();
        }
    };

    private void changeActivity() {
        Intent i = new Intent(getApplication(), MainActivity.class);
        startActivity(i);

        LoginActivity.this.finish();
    }

}
