package org.coursera.capstone.client.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.coursera.capstone.client.core.entities.GiftEntity;
import org.coursera.capstone.client.entities.Gift;

import retrofit.client.Response;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

public class UtilityMethods  {

	
public static BitmapFactory.Options getOptions()
{
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inSampleSize= 5;
	options.inPurgeable = true;
	options.inInputShareable = true;
	
	return options;
}

public static String getPathFromUri(Uri uri,Context context)
{
	String selectedImagePath="";
	Cursor cursor = context.getContentResolver().query(uri, null, null,null, null);       
	if (cursor == null) { 
        selectedImagePath = uri.getPath();
    } else {
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        selectedImagePath = cursor.getString(idx);
    }
	return selectedImagePath;
}

public static String getAudioInfo(Uri uri,Context context)
{
	String processedString="";
	
	 final String[] columns = { 
             MediaStore.Audio.Media.ARTIST, 
             MediaStore.Audio.Media.TITLE,              
             MediaStore.Audio.Media.DURATION };
     final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
     final Cursor cursor = context.getContentResolver().query(uri,
             columns, where, null, null);

     while (cursor.moveToNext()) {
         processedString+= cursor.getString(cursor
                 .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));      
        processedString +=" - "+ cursor.getString(cursor
                 .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));       
         
         }
	
	return processedString;
}

public static byte[] processImageResponse(Response result) throws IOException
{
	InputStream imageData = result.getBody().in();
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	int read=0;
	byte[] data = new byte[1024];

	while ((read = imageData.read(data)) != -1) {
	  buffer.write(data, 0, read);
	}
	return buffer.toByteArray();
}

public static String getIdFromUrl(String url)
{
	String[] array=url.split("/");
	String id="";
	if (array!=null)
	{
		id=(array[array.length-2]);
	}
	return id;
}

public static ArrayList<Gift> processGiftList(Collection<GiftEntity> list)  {
	// TODO Auto-generated method stub
	
ArrayList<Gift> giftList=new ArrayList<Gift>();
for(GiftEntity item:list)
{
	final Gift gift=new Gift();
	gift.setText(item.getText());
	gift.setTitle(item.getTitle());
	gift.setChainId(item.getChainId());
	gift.setParent(item.getIsParent()==0?false:true);
	gift.setId(item.getId());
	gift.setUserToken(item.getUserToken());
	gift.setDate(item.getDate());
	gift.setLatitude(item.getLat());
	gift.setLongitude(item.getLng());
	gift.setUserName(item.getUserName());
	gift.setAddress(item.getAddress());
	if (item.getImageUrl()!=null && item.getImageUrl()!="")
	{
	gift.setGiftImageUrl(item.getImageUrl());
	}
	

	gift.setTouchCount(item.getTouchCount());
	
	giftList.add(gift);
	}
return giftList;
}



public static byte[] getResizedBitmap(Context context,byte[] giftImage) {
	Bitmap bm=BitmapFactory.decodeByteArray(giftImage, 0, giftImage.length);	 
	int width = bm.getWidth();
	 
	int height = bm.getHeight();
	 
	if (bm.getWidth()>1024 ||bm.getHeight()>1024)
	{
		int newHeight=1024;
		int newWitdth=1024;
		float scalex=newWitdth/width;
		float scaley=newHeight/height;
		
		float scale=(scalex>scaley)?scaley:scalex;
	Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm,(int)(width*scale),(int)(height*scale) , true);
	bm=null;
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	byte[] byteArray = stream.toByteArray();
	resizedBitmap=null;
	return byteArray;
	}
	else
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
	
}



}
