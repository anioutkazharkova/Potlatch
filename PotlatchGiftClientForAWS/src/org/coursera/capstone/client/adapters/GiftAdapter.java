package org.coursera.capstone.client.adapters;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.R;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.UserManager;
import org.coursera.capstone.client.activities.GiftChainActivity;
import org.coursera.capstone.client.core.GiftEntitySvcApi;
import org.coursera.capstone.client.core.GiftSvc;
import org.coursera.capstone.client.core.entities.CommentEntity;
import org.coursera.capstone.client.core.entities.GiftEntity;
import org.coursera.capstone.client.entities.Gift;
import org.coursera.capstone.client.fragments.InfoDialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GiftAdapter extends BaseAdapter implements OnClickListener {

	ArrayList<Gift> list=new ArrayList<Gift>();
	Context mContext;
	LayoutInflater inflater;
	boolean showDeleteMarkers=false;
	boolean showChainButton=true;
	
	private GiftEntitySvcApi giftService;
	
	 public GiftAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext=context;
		inflater=LayoutInflater.from(mContext);
	}
	 public GiftAdapter(Context context,ArrayList<Gift> listGift) {
			// TODO Auto-generated constructor stub
			this(context);
			list=new ArrayList<Gift>();
			list=listGift;
		}
	 
	 public GiftAdapter(Context context,ArrayList<Gift> listGift,boolean showDeleteMarkers) {
			// TODO Auto-generated constructor stub
			this(context,listGift);
			this.showDeleteMarkers=showDeleteMarkers;
		}
	 public GiftAdapter(Context context,ArrayList<Gift> listGift,boolean showDeleteMarkers,boolean showChainButton){
	 this(context,listGift,showDeleteMarkers);
	 this.showChainButton=showChainButton;
	 }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		final ViewHolder holder;
		final Gift currentGift=list.get(position);
		if (convertView==null)
		{
			convertView=inflater.inflate(R.layout.gift_item_layout, null);
			holder=new ViewHolder();
			
			holder.tvText=(TextView) convertView.findViewById(R.id.tvGiftText);
			holder.tvTitle=(TextView)convertView.findViewById(R.id.tvGiftTitle);
			holder.image=(ImageView)convertView.findViewById(R.id.imGiftImage);
			holder.imRemove=(ImageButton)convertView.findViewById(R.id.imbRemove);
			holder.imChain=(ImageButton)convertView.findViewById(R.id.imbChain);
			holder.imTouch=(ImageButton)convertView.findViewById(R.id.imbTouch);
			holder.tvTouch=(TextView)convertView.findViewById(R.id.tvTouchCount);
			holder.tvDate=(TextView)convertView.findViewById(R.id.tvGiftDate);
			holder.tvAuthor=(TextView)convertView.findViewById(R.id.tvAuthor);
			holder.layoutGeo=(LinearLayout)convertView.findViewById(R.id.layoutLocation);
			holder.tvAddress=(TextView)convertView.findViewById(R.id.tvAddress);
			convertView.setTag(holder);
			
		}
		else
		{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.tvDate.setFocusable(false);
		holder.tvText.setFocusable(false);
		holder.tvTitle.setFocusable(false);
		holder.image.setFocusable(false);
		holder.imRemove.setFocusable(false);
		holder.imTouch.setFocusable(false);
		holder.tvTouch.setFocusable(false);
		if (!showDeleteMarkers)
		{
			holder.imRemove.setVisibility(View.INVISIBLE);
		}
		else
		{
			UserManager um=UserManager.getInstance();
			if (um.isAuthorized())
			{
				if (um.getAuthUser().getName().equals(currentGift.getUserName()))
				{
					holder.imRemove.setVisibility(View.VISIBLE);
				}
				else
				{
					holder.imRemove.setVisibility(View.INVISIBLE);
				}
			}
			else
				holder.imRemove.setVisibility(View.INVISIBLE);
		}
		if (!showChainButton)
		{
			holder.imChain.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.imChain.setVisibility(View.VISIBLE);
		}
		holder.imTouch.setTag(position);
		holder.imChain.setTag(position);
		holder.imRemove.setTag(position);
		holder.tvTouch.setTag(position);
		
		if (currentGift.getAddress()!=""&& currentGift.getAddress()!=null)
		{
			holder.layoutGeo.setVisibility(View.VISIBLE);
			holder.tvAddress.setText(currentGift.getAddress());
		}
		else
		{
			holder.layoutGeo.setVisibility(View.GONE);
		}
			
		holder.tvAuthor.setText(currentGift.getUserName());
		holder.tvText.setText(currentGift.getText());
		holder.tvTitle.setText(currentGift.getTitle());
		if(currentGift.getDate()!="")
		{
			holder.tvDate.setText(currentGift.getDate());
		}
		if (currentGift.getTouchCount()==0)
		{
			holder.tvTouch.setText("");
		}
		else
		{
			holder.tvTouch.setText(currentGift.getTouchCount()+"");
		}
		//holder.image.setTag(currentGift.getGiftImageUrl());
		holder.image.setImageBitmap(null);
		if (currentGift.getGiftImageUrl()!=null && currentGift.getGiftImage()!=null)
		{
			try{
			Bitmap bitmap=BitmapFactory.decodeByteArray(currentGift.getGiftImage(), 0, currentGift.getGiftImage().length);
			holder.image.setImageBitmap(bitmap);
			bitmap=null;
			}
			catch(Exception e){
			
			}
			//notifyDataSetChanged();
		}
		
			
		
		final String id=currentGift.getId();		
		holder.imTouch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
					UserManager userManager=UserManager.getInstance();
					
					if (userManager.isAuthorized())
					{
						final GiftEntitySvcApi giftService=GiftSvc.init(userManager.getAuthUser());	
						CallableTask.invoke(new Callable<GiftEntity>(){

							@Override
							public GiftEntity call() throws Exception {
								// TODO Auto-generated method stub
								CommentEntity comment=new CommentEntity();
								comment.setIsTouched(1);
								return giftService.addComment(comment, id);
							}
							
						}, new TaskCallback<GiftEntity>() {

							@Override
							public void success(GiftEntity result) {
								// TODO Auto-generated method stub
								currentGift.setTouchCount(result.getTouchCount());
								holder.tvTouch.setText(result.getTouchCount()+"");
							}

							@Override
							public void error(Exception e) {
								// TODO Auto-generated method stub
								
							}
						});
						
					}else
					{
						InfoDialogFragment dialog=new InfoDialogFragment("Log in to touch a gift", true);
						dialog.show(((Activity) mContext).getFragmentManager(), "dialog");
						
					}
				
			}});
		holder.imRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				UserManager userManager=UserManager.getInstance();
				
				if (userManager.isAuthorized())
				{
					final GiftEntitySvcApi giftService=GiftSvc.init(userManager.getAuthUser());
				CallableTask.invoke(new Callable<GiftEntity>(){

					@Override
					public GiftEntity call() throws Exception {
						// TODO Auto-generated method stub
						
						return giftService.removeGift(id);
					}
					
				}, new TaskCallback<GiftEntity>() {

					@Override
					public void success(GiftEntity result) {
						// TODO Auto-generated method stub
						//Remove					
						if (currentGift.isParent())
						{
							list.removeAll(list);
							((Activity)mContext).finish();
						}
						else
						list.remove(id);
						notifyDataSetChanged();
					}

					@Override
					public void error(Exception e) {
						// TODO Auto-generated method stub
						
					}
				});
				}
			}
		});
		final String chainId=currentGift.getChainId();
		holder.imChain.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//Открыть окно цепочки
				Intent chainIntent=new Intent(mContext,GiftChainActivity.class);
				chainIntent.putExtra("id",chainId);
				mContext.startActivity(chainIntent);
				
			}
		});
		return convertView;
	}
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	class ViewHolder
	{
		public TextView tvTitle,tvText,tvTouch,tvDate,tvAuthor;
		public ImageView image;
		public ImageButton imRemove,imTouch,imChain;
		public LinearLayout layoutGeo;
		public TextView tvAddress;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	public GiftEntitySvcApi getGiftService() {
		return giftService;
	}
	public void setGiftService(GiftEntitySvcApi giftService) {
		this.giftService = giftService;
	}
	
	

}
