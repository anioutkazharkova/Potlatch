package org.coursera.capstone.client.activities;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.GetAddressTask;
import org.coursera.capstone.client.R;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.common.UtilityMethods;
import org.coursera.capstone.client.core.entities.GiftEntity;
import org.coursera.capstone.client.core.entities.ImageEntity;
import org.coursera.capstone.client.core.entities.ImageStatus;
import org.coursera.capstone.client.fragments.InfoDialogFragment;

import retrofit.mime.TypedFile;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GiftCreateActivity extends BaseActivity implements OnClickListener {

	ImageButton btnMarkGeo, btnCamera, btnAttachSound,btnSettings,imRemoveSound;
	EditText etTitle;
	ImageView image;
	EditText etGiftText;
	TextView tvSoundName,tvAddress,tvAddressLabel;
	LinearLayout layoutSound;
	Button btnSave;
	
	private static String mCurrentPhotoPath="";	
	private String mSelectedImagePath="";
	
	public static Uri ImageUri=null;
	public static final int SELECT_PICTURE = 12;
	public static final int IMAGE_CAPTURE = 13;
	public static final int SELECT_SOUND= 14;
	public static final int GET_LOCATION=15;
	
	private boolean createNewChain=true;
	private String chainId;
	private LinearLayout btnSaveMenu;
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	
	private double Latitude=1000;
	private double Longitude=1000;
	private String address;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_create_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		setTitle(getResources().getString(R.string.create_activity));
		createNewChain=getIntent().getBooleanExtra("create_new_chain",true);
		if (!createNewChain)
		{
			chainId=getIntent().getStringExtra("id");
		}
		
		tvAddress=(TextView)findViewById(R.id.tvAddress);
		tvAddressLabel=(TextView)findViewById(R.id.tvGeoLabel);
		tvAddressLabel.setVisibility(View.INVISIBLE);
		etGiftText=(EditText)findViewById(R.id.tvGiftText);
		//btnSave=(Button)findViewById(R.id.btnSaveGift);
		etTitle=(EditText)findViewById(R.id.etGiftTitle);
		image=(ImageView)findViewById(R.id.imGiftImage);
		image.setOnClickListener(this);
		etTitle.clearFocus();
		etGiftText.clearFocus();
	
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	    Log.d("create", userManager.isAuthorized()+"");
	   
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		
		case R.id.imGiftImage:
		{
			SelectImageDialogFragment dialog=new SelectImageDialogFragment();
			dialog.show(getFragmentManager(), "select_pic");
		}
		break;
		case R.id.btnSaveGift:
		{
			if (!mSelectedImagePath.isEmpty()&&  !etTitle.getText().toString().isEmpty())
			{
			Log.d("save","saving");
			if (!createNewChain)
			{
			saveGift();
			}else
			{
				saveGiftNewChain();
			}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Can't save empty gift", Toast.LENGTH_LONG).show();
			}
		}
		break;
		
		}
	}
	
	private GiftEntity createGift()
	{
		GiftEntity gift=new org.coursera.capstone.client.core.entities.GiftEntity();
		gift.setText(etGiftText.getText().toString());
		gift.setTitle(etTitle.getText().toString());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");		
		gift.setDate(dateFormat.format(new Date()));
		gift.setLat(Latitude);
		gift.setLng(Longitude);
		gift.setAddress(address);
		
		return gift;
	}
	private void saveGiftNewChain() {
		// TODO Auto-generated method stub
	
		CallableTask.invoke(new Callable<GiftEntity>() {

			@Override
			public GiftEntity call() throws Exception {
				// TODO Auto-generated method stub
				
				return giftService.addGift(createGift());
			}
	
		},new TaskCallback<GiftEntity>() {

			@Override
			public void success(GiftEntity result) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity().getApplicationContext(), result.getRole(),Toast.LENGTH_LONG).show();
				
				addImageToGift(result);						
				Log.d("login",result.getText());
				
			}

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "error",Toast.LENGTH_LONG).show();				
			}
		});
		
	}
	private void loadImageData(ImageEntity result)
	{
		final ImageEntity res=result;
		CallableTask.invoke(new Callable<ImageStatus>(){

			@Override
			public ImageStatus call() throws Exception {
				// TODO Auto-generated method stub										
				File file=new File(mSelectedImagePath);
				return imageService.setImageData(res.getId(), new TypedFile(res.getContentType(), file));
			
			}
		}, new TaskCallback<ImageStatus>() {

			@Override
			public void success(ImageStatus result) {
				// TODO Auto-generated method stub
				Log.d("save", "success");
				InfoDialogFragment dialog=new InfoDialogFragment("Gift has been successfully created", true);
				dialog.show(getFragmentManager(), "dialog");
				finish();
			}

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void addImageToGift(GiftEntity result)
	{
		final Bitmap giftBitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
		final String id= result.getId();
		if (giftBitmap!=null)
		{
			CallableTask.invoke(new Callable<ImageEntity>(){

				@Override
				public ImageEntity call() throws Exception {
					// TODO Auto-generated method stub
					ImageEntity image=new ImageEntity();
					image.setName(mSelectedImagePath);
					image.setContentType("image/jpeg");
					image.setGiftId(id);
					return imageService.addImage(image);
				}}, new TaskCallback<ImageEntity>() {

					@Override
					public void success(ImageEntity result) {
						loadImageData(result);
														}
					@Override
					public void error(Exception e) {
						// TODO Auto-generated method stub
						
					}
				});}

	}
	
	private void saveGift() {
		// TODO Auto-generated method stub
	
		CallableTask.invoke(new Callable<GiftEntity>() {

			@Override
			public GiftEntity call() throws Exception {
				// TODO Auto-generated method stub
				
				return giftService.addGiftToChain(createGift(), chainId);
			}

			
			
		},new TaskCallback<GiftEntity>() {

			@Override
			public void success(GiftEntity result) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity().getApplicationContext(), result.getRole(),Toast.LENGTH_LONG).show();
				
				addImageToGift(result);		
					
				
				Log.d("login",result.getText());
				
			}

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "error",Toast.LENGTH_LONG).show();
				
			}
		});
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.create_gift_menu, menu);
		MenuItem item=menu.findItem(R.id.btnSave);
		View menuView=item.getActionView();
		btnSaveMenu=(LinearLayout) menuView.findViewById(R.id.btnSaveGift);
		btnSaveMenu.setOnClickListener(this);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		case R.id.btnAddGeo:
			
			startActivityForResult(new Intent(this,SelectLocationActivity.class),GET_LOCATION);
			break;
		
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode==SELECT_PICTURE)
		{
			if (resultCode==RESULT_OK)
			{
				Uri selectedImageUri = data.getData();
				mSelectedImagePath = UtilityMethods.getPathFromUri(selectedImageUri,this);
				if (mSelectedImagePath!=null)
				{
                image.setImageURI(selectedImageUri);
                image.setVisibility(View.VISIBLE);
				}
			}
		}
		if (requestCode==IMAGE_CAPTURE)
		{
			if (resultCode==RESULT_OK)
			{
				if (mCurrentPhotoPath!=null)
				{
					mSelectedImagePath=mCurrentPhotoPath;
					setPicture();
				}		
               
			}
		}
		if (requestCode==SELECT_SOUND)
		{
			if (resultCode==RESULT_OK){
				Uri selectedSoundUri=data.getData();
				tvSoundName.setText(UtilityMethods.getAudioInfo(selectedSoundUri, this));
				layoutSound.setVisibility(View.VISIBLE);
			}
		}
		if (requestCode==GET_LOCATION)
		{
			if (resultCode==RESULT_OK)
			{
				Latitude=data.getDoubleExtra(SelectLocationActivity.LATITUDE,1000);
				Longitude=data.getDoubleExtra(SelectLocationActivity.LONGITUDE,1000);
				processLocation();
				
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void processLocation()
	{
		if (Latitude!=1000 && Longitude!=1000)
		{
			Location mLocation=new Location(LocationManager.NETWORK_PROVIDER);
			mLocation.setLatitude(Latitude);
			mLocation.setLongitude(Longitude);
			try {
				address= new GetAddressTask(this,tvAddress).execute(mLocation).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tvAddressLabel.setVisibility(View.VISIBLE);
		}
	}
	
	private void setPicture()
	{		
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,UtilityMethods.getOptions());
		image.setImageBitmap(bitmap);
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setVisibility(View.VISIBLE);
		bitmap=null;
		
	}
	
	
	
	
	public static void createSelectIntent(Context context)
	{
		 Intent intent = new Intent();
         intent.setType("image/*");
         intent.setAction(Intent.ACTION_GET_CONTENT);         
         ((Activity) context).startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);

	}
	

	public static void createTakePictureIntent(Context context) throws IOException 
	{
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) 
    {
    	File f=null;
    	f = setUpPhotoFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));    
        ((Activity) context).startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
    }
	}

	private static File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());			
		File storageDir = new File(Environment.getExternalStorageDirectory(),"Potlatch");
		
		if (!storageDir.exists()) {
			if (!storageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}		
		File imageF=new File(storageDir.getAbsolutePath()+File.separator+JPEG_FILE_PREFIX+timeStamp+"_"+JPEG_FILE_SUFFIX);
		return imageF;
	}

	private static File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();		
		return f;
	}
	
	
	
	
	

	
}
