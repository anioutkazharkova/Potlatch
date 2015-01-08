package org.coursera.capstone.client.activities;


import java.util.concurrent.Callable;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.R;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.core.entities.CommentEntity;
import org.coursera.capstone.client.core.entities.GiftEntity;
import org.coursera.capstone.client.entities.Gift;
import org.coursera.capstone.client.fragments.IActionable;
import org.coursera.capstone.client.fragments.InfoDialogFragment;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GiftViewActivity extends BaseFragmentActivity implements OnClickListener,IActionable{
public enum Action{delete,comment};
Action performedAction=Action.delete;
	TextView tvDate,tvText,tvTitle,tvTouchCount,tvAddress;
	EditText etText;
	ImageView image;
	Gift currentGift;
	ImageButton btnTouch,btnRemoveGift,btnChain,btnShowLocation;
	LinearLayout layoutSound,layoutMap,layoutLocation;	
	private boolean isInappropriate;
	private boolean isObscene;
	private GoogleMap mMap;
	private Location mCurrentLocation;
	private boolean isVisible=false;
	private boolean isTouched=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_view_layout);
		
		
		tvText=(TextView)findViewById(R.id.tvGiftText);
		tvTitle=(TextView)findViewById(R.id.tvGiftTitle);
		tvDate=(TextView)findViewById(R.id.tvGiftDate);
		tvTouchCount=(TextView)findViewById(R.id.tvTouchCount);
		image=(ImageView)findViewById(R.id.imGiftImage);
		btnTouch=(ImageButton)findViewById(R.id.imbTouch);
		btnRemoveGift=(ImageButton)findViewById(R.id.imbRemove);
		btnChain=(ImageButton)findViewById(R.id.imbChain);
		layoutSound=(LinearLayout)findViewById(R.id.layoutSound);
		layoutSound.setVisibility(View.INVISIBLE);
		layoutMap=(LinearLayout)findViewById(R.id.layoutMap);
		btnTouch.setOnClickListener(this);
		btnRemoveGift.setOnClickListener(this);
		btnChain.setOnClickListener(this);
		btnRemoveGift.setVisibility(View.INVISIBLE);
		btnShowLocation=(ImageButton)findViewById(R.id.imbLocation);
		layoutLocation=(LinearLayout)findViewById(R.id.layoutLocation);
		tvAddress=(TextView)findViewById(R.id.tvAddress);
		
		currentGift=(Gift)getIntent().getParcelableExtra(Gift.class.getCanonicalName());
		setTitle(currentGift.getTitle());
		if (currentGift!=null)
		{
			tvText.setText(currentGift.getText());
			tvTitle.setText(currentGift.getTitle());
			tvDate.setText(currentGift.getDate());
			tvTouchCount.setText(currentGift.getTouchCount()>0?currentGift.getTouchCount()+"":"");
			if (currentGift.getLatitude()!=1000&&currentGift.getLongitude()!=1000)
			{				
				tvAddress.setText(currentGift.getAddress());
				setUpMapIfNeeded();
				layoutMap.setVisibility(View.GONE);
			btnShowLocation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!isVisible)
					{
						layoutMap.setVisibility(View.VISIBLE);
					}
					else
					{
						layoutMap.setVisibility(View.GONE);
					}
						isVisible=!isVisible;
					
				}
			});
			}
			else
			{
				layoutLocation.setVisibility(View.GONE);
				layoutMap.setVisibility(View.GONE);
			}
			try{
			image.setImageBitmap(BitmapFactory.decodeByteArray(currentGift.getGiftImage(), 0,currentGift.getGiftImage().length));	
			}
			catch(OutOfMemoryError e)
			{
				e.printStackTrace();
			}
			
			if (userManager.isAuthorized())
			{
				if (currentGift.getUserName().equals(userManager.getAuthUser().getName()))
				{
					btnRemoveGift.setVisibility(View.VISIBLE);
				}
				else
				{
					btnRemoveGift.setVisibility(View.INVISIBLE);
				}
			}
		}
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private void setUpMapIfNeeded() {
        if (mMap == null) {
           SupportMapFragment fragm =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
           if (fragm!=null)
        	   mMap=fragm.getMap();
          
            if (mMap != null) {
            	
                setUpMap();
            }
        }
	}
        private void setUpMap() {	        
 	       
    		if (currentGift.getLatitude()!=1000&&currentGift.getLongitude()!=1000)
    		{
            mCurrentLocation=new Location(LocationManager.NETWORK_PROVIDER);
            mCurrentLocation.setLatitude(currentGift.getLatitude());
            mCurrentLocation.setLongitude(currentGift.getLongitude());
    		}
            if (mCurrentLocation!=null)
            {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 15));
            mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))); 
       // new GetAddressTask(this,tvAddress).execute(mCurrentLocation);	      
            }
            }
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		case R.id.setInappropriate:
		{
			if (userManager.isAuthorized())	{
			if (this.isInappropriate)
			{
				this.setInappropriate(false);
				performedAction=Action.comment;
				InfoDialogFragment dialog=new InfoDialogFragment(getResources().getString(R.string.gift_was_unmarked),true);
				dialog.show(getFragmentManager(), "inappropriate");
				setComment(isInappropriate, isObscene);
			}
			else
				{this.setInappropriate(true);
				performedAction=Action.comment;
				InfoDialogFragment dialog=new InfoDialogFragment(getResources().getString(R.string.do_you_want_inapp));
				dialog.show(getFragmentManager(), "inappropriate");
				}
			
		}
		}
		break;
		case R.id.setObscene:
		{
			if (userManager.isAuthorized())	{
			if (this.isObscene)
			{
				this.setObscene(false);
				performedAction=Action.comment;
				InfoDialogFragment dialog=new InfoDialogFragment(getResources().getString(R.string.gift_was_unmarked),true);
				dialog.show(getFragmentManager(), "inappropriate");
				setComment(isInappropriate, isObscene);
			}
			else
				{this.setObscene(true);
				performedAction=Action.comment;
				InfoDialogFragment dialog=new InfoDialogFragment(getResources().getString(R.string.do_you_want_obscene));
				dialog.show(getFragmentManager(), "inappropriate");
				
				}
			
			
		}
		}
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.gift_view_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.imbChain:
			goToChain();
			break;
		case R.id.imbRemove:
		{
			performedAction=Action.delete;
			InfoDialogFragment dialog=new InfoDialogFragment(getResources().getString(R.string.do_you_want_delete));
			dialog.show(getFragmentManager(), "dialog");
		}
			//removeGift();
			break;
		case R.id.imbTouch:
			touchGift();
			break;
		}
	}
	
	

	private void removeGift() {
		// TODO Auto-generated method stub
		if (userManager.isAuthorized())
		{
			if (currentGift.getUserName().equals(userManager.getAuthUser().getName()))
			{
				CallableTask.invoke(new Callable<GiftEntity>(){

					@Override
					public GiftEntity call() throws Exception {
						// TODO Auto-generated method stub
						
						return giftService.removeGift(currentGift.getId());
					}
					
				}, new TaskCallback<GiftEntity>() {

					@Override
					public void success(GiftEntity result) {
						// TODO Auto-generated method stub
						//Remove					
						if (currentGift.isParent())
						{
							finish();
						}
						
					}

					@Override
					public void error(Exception e) {
						// TODO Auto-generated method stub
						
					}
				});
				}
		}
	}

	private void touchGift() {
	
		if (userManager.isAuthorized())		{
			
			if (!isTouched)
			{
				isTouched=true;
		setComment(isInappropriate, isObscene);
			}
		}
		else
		{
			InfoDialogFragment dialog=new InfoDialogFragment(getResources().getString(R.string.log_to_touch), true);
			dialog.show(getFragmentManager(), "dialog");
		}
	}
	
	private void setComment(final boolean isInappropriate,final boolean isObscene)
	{
		CallableTask.invoke(new Callable<GiftEntity>(){

			@Override
			public GiftEntity call() throws Exception {
				// TODO Auto-generated method stub
				CommentEntity comment=new CommentEntity();
				comment.setIsObscene(isObscene?1:0);
				comment.setIsInappropriate(isInappropriate?1:0);
				if (isTouched)
				{
			
				comment.setIsTouched(1);
				}
				
				return giftService.addComment(comment, currentGift.getId());
			}
			
		}, new TaskCallback<GiftEntity>() {			

			@Override
			public void success(GiftEntity result) {
				// TODO Auto-generated method stub
				currentGift.setTouchCount(result.getTouchCount());
				if (result.getTouchCount()>0)
				tvTouchCount.setText(result.getTouchCount()+"");
			}

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void goToChain() {
		// TODO Auto-generated method stub
		Intent chainIntent=new Intent(this,GiftChainActivity.class);		
		chainIntent.putExtra("id",currentGift.getChainId());
		startActivity(chainIntent);
	}


	public boolean isInappropriate() {
		return isInappropriate;
	}


	public void setInappropriate(boolean isInappropriate) {
		this.isInappropriate = isInappropriate;
	}


	public boolean isObscene() {
		return isObscene;
	}


	public void setObscene(boolean isObscene) {
		this.isObscene = isObscene;
	}

	@Override
	public void performAction() {
		// TODO Auto-generated method stub
		if(performedAction==Action.delete)
		{
		removeGift();
		}
		else if (performedAction==Action.comment)
		{
		setComment(isInappropriate, isObscene);
		}
	}
}
