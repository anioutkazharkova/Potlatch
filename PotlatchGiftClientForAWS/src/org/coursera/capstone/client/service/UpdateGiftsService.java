package org.coursera.capstone.client.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class UpdateGiftsService extends Service {

	
	PendingIntent pendIntent;
	AlarmManager updateScheduler=null;
	
	
	
	
	@Override
	public int onStartCommand(Intent intent,int flags, int startId) {
		// TODO Auto-generated method stub
		
		startService();
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	private void startService()
	{
		Log.d("service", "start");
		  Intent intent = new Intent(this, UpdateScheduler.class);
          pendIntent = PendingIntent.getBroadcast(getApplicationContext(), UpdateServiceCommon.UPDATE_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         updateScheduler = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
         updateScheduler.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), UpdateServiceCommon.getUpdateTime(getApplicationContext()), pendIntent);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (updateScheduler!=null)
		{
			updateScheduler.cancel(pendIntent);
			updateScheduler=null;
		}
		super.onDestroy();
	}
	

}
