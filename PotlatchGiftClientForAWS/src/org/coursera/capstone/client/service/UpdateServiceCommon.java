package org.coursera.capstone.client.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class UpdateServiceCommon {
public static int UPDATE_CODE=789;

public static boolean checkServiceIsRunning(Context context,Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName())) {
            return true;
        }
    }
    return false;
}

public static  void startUpdateService(Context context)
{
	
    if (!checkServiceIsRunning(context,UpdateGiftsService.class))
    {
        context.startService(new Intent(context, UpdateGiftsService.class));
    }
   
}

public static void stopUpdateService(Context context)
{
	
    if (!checkServiceIsRunning(context,UpdateGiftsService.class))
   {
    	context.stopService(new Intent(context, UpdateGiftsService.class));
   }
}

public static void restartUpdateService(Context context)
{
	stopUpdateService(context);
	startUpdateService(context);
}
public static int getUpdateTime(Context context)
{
	SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
	String update=sp.getString("listUpdateTime", "60000");
	return Integer.parseInt(update);
}
}
