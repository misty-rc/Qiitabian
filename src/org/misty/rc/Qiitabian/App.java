package org.misty.rc.Qiitabian;

import android.app.Application;
import com.deploygate.sdk.DeployGate;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/26
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DeployGate.install(this, null, true);
    }
}