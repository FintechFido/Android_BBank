package com.example.fintech2020_bbanck.function;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

public class Call_HIDO extends Activity {

    public Call_HIDO(){ }

    public boolean exist_check(Context context) {
        boolean isExist = false;
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = packageManager.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if (mApps.get(i).activityInfo.packageName.startsWith("com.example.fintech_hido")) {
                    isExist = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }
}
