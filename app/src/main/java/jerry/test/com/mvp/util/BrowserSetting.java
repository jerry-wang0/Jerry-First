package jerry.test.com.mvp.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;

import static android.content.pm.PackageManager.GET_INTENT_FILTERS;

/**
 * Created by jerry on 2017/04/26.
 */

public class BrowserSetting {

//        <uses-permission
//    android:name="android.permission.SET_PREFERRED_APPLICATIONS"
//    tools:ignore="ProtectedPermissions" />

    public void setDefaultBrowser(Context context) {
        String str1 = "android.intent.category.DEFAULT";
        String str2 = "android.intent.category.BROWSABLE";
        String str3 = "android.intent.category.VIEW";

        PackageManager packageManager = context.getPackageManager();
        //intent filter
        IntentFilter filter = new IntentFilter(str3);
        filter.addCategory(str1);
        filter.addCategory(str2);
        filter.addDataScheme("http");
        filter.addDataScheme("https");
        //intent
        Intent intent = new Intent(str3);
        intent.addCategory(str1);
        intent.addCategory(str2);
        Uri uri1 = Uri.parse("http://");
        Uri uri2 = Uri.parse("https://");
        intent.setDataAndType(uri1, null);
        intent.setDataAndType(uri2, null);
        //set our browser to the default
        ComponentName component = new ComponentName("jerry.test.com.mvp", "jerry.test.com.mvp.MainActivity");
        //find all browsers
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        int size = resolveInfoList.size();
        Log.v("BrowserSetting","size:===="+size);
        ComponentName[] arrayOfCompnentName = new ComponentName[size];
        for (int i = 0; i < size; i++) {
            ActivityInfo activityInfo = resolveInfoList.get(i).activityInfo;
            String packageName = activityInfo.packageName;
            String className = activityInfo.name;
            //clear before default setting
            packageManager.clearPackagePreferredActivities(packageName);
            ComponentName componentName = new ComponentName(packageName, className);
            arrayOfCompnentName[i] = componentName;

        }
        packageManager.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_SCHEME, arrayOfCompnentName, component);
    }
}
