package org.coursera.capstone.client.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.common.UtilityMethods;
import org.coursera.capstone.client.core.GiftEntitySvcApi;
import org.coursera.capstone.client.core.ImageEntitySvcApi;
import org.coursera.capstone.client.core.entities.GiftEntity;
import org.coursera.capstone.client.database.DatabaseHelper;
import org.coursera.capstone.client.entities.Gift;

import retrofit.client.Response;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public abstract class LoadImageGiftFragment extends Fragment {

	public ArrayList<Gift> gifts;
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	public	abstract void getGiftImage(int index);
	public	abstract void setListViewData();
	public abstract void operateGiftList(Collection<GiftEntity> result);
	public abstract void getGiftsList(final GiftEntitySvcApi giftService);


	public void downloadGiftImage(final int ind,final ImageEntitySvcApi imageService){
		final String id=UtilityMethods.getIdFromUrl(gifts.get(ind).getGiftImageUrl());
		
		
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
						databaseHelper=new DatabaseHelper(getActivity());
						db=databaseHelper.getWritableDatabase();
						ContentValues cv=new ContentValues();
						cv.put("url", gifts.get(ind).getGiftImageUrl());
						cv.put("image_bytes", gifts.get(ind).getGiftImage());
						try{
						db.insert("image_entity", null, cv);
						}
						catch(Exception e){}
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
