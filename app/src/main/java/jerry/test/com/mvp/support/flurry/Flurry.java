package jerry.test.com.mvp.support.flurry;

import android.content.Context;

import com.flurry.android.FlurryAgent;

import java.util.Map;

/**
 * Created by Liqun Wan on 2015/10/19.
 */
public final class Flurry {

    /**
     *
     */
	public static void init(Context context) {
//		new FlurryAgent.Builder().withLogEnabled(false).build(context, BuildConfig.FLURRY_API_KEY);
	}

    /**
     *
     */
    public static void onPageView() {
        FlurryAgent.onPageView();
    }

	public static void setUserId(String id) {
		FlurryAgent.setUserId(id);
	}

    /**
     *
     */
	public static void logEvent(String event) {
		FlurryAgent.logEvent(event);
	}

	public static void logEvent(String event, boolean timed) {
		FlurryAgent.logEvent(event, timed);
	}
	
	public static void logEvent(String event, Map<String, String> args) {
		FlurryAgent.logEvent(event, args);
	}

	public static void logEvent(String event, Map<String, String> args, boolean end) {
		if(end) FlurryAgent.endTimedEvent(event);
		else FlurryAgent.logEvent(event, args, true);
	}

	public static void logError(String event, String m, Throwable t){
		FlurryAgent.onError(event, m, t);
	}
}
