package org.coursera.capstone.client.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.adapters.GiftAdapter;
import org.coursera.capstone.client.common.UtilityMethods;
import org.coursera.capstone.client.core.GiftEntitySvcApi;
import org.coursera.capstone.client.core.ImageEntitySvcApi;
import org.coursera.capstone.client.core.entities.GiftEntity;
import org.coursera.capstone.client.database.DatabaseHelper;
import org.coursera.capstone.client.entities.Gift;

import retrofit.client.Response;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

public abstract class LoadImageGiftActivity extends BaseActivity {

public ArrayList<Gift> gifts;
private DatabaseHelper databaseHelper;
private SQLiteDatabase db;
public	abstract void getGiftImage(int index);
public	abstract void setListViewData();
public abstract void operateGifts(Collection<GiftEntity> result);



public void downloadGiftImage(final int ind,final ImageEntitySvcApi imageService){
	final String id=UtilityMethods.getIdFromUrl(gifts.get(ind).getGiftImageUrl());
	databaseHelper=new DatabaseHelper(this);
	db=databaseHelper.getWritableDatabase();
	CallableTask.invoke(new Callable<Response>(){

		@Override
		public Response call() throws Exception {
			// TODO Auto-generated method stub
			return imageService.getData(id);
		}
		
	}, new TaskCallback<Response>(){

		@Override
		public void success(Response result) {
			// TODO Auto-generated method stub
			if (result.getStatus()==200)
			{
				try {
					
					if (gifts.get(ind).getGiftImageUrl()!="")
					{
					gifts.get(ind).setGiftImage(UtilityMethods.processImageResponse(result));
					ContentValues cv=new ContentValues();
					cv.put("url", gifts.get(ind).getGiftImageUrl());
					cv.put("image_bytes", gifts.get(ind).getGiftImage());
					db.insert("image_entity", null, cv);
					db.close();
					databaseHelper.close();
					}
					
					if (ind<gifts.size()-1)
					{
						
						getGiftImage(ind+1);
					}
					else
					{								
						setListViewData();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void error(Exception e) {
			// TODO Auto-generated method stub				
		}
		
	});
}



}
