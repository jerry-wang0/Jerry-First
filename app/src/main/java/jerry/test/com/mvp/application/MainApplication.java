package jerry.test.com.mvp.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangguanlin on 2017/04/17.
 */

public class MainApplication extends Application {

    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
