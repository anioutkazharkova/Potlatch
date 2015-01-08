package org.coursera.capstone.client.activities;


import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.R;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.adapters.GiftAdapter;
import org.coursera.capstone.client.common.UtilityMethods;
import org.coursera.capstone.client.core.entities.GiftEntity;
import org.coursera.capstone.client.database.DatabaseHelper;
import org.coursera.capstone.client.entities.Gift;
import org.coursera.capstone.client.fragments.IActionable;
import org.coursera.capstone.client.fragments.InfoDialogFragment;
import org.coursera.capstone.client.service.IServiceUpdatable;
import org.coursera.capstone.client.service.UpdateServiceCommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GiftChainActivity extends LoadImageGiftActivity implements OnClickListener,IActionable,IServiceUpdatable {

	private String id;
	private ListView listView;	
	private LinearLayout btnCreate;	
	private GiftAdapter mAdapter;
	private int Total;
	private int Page=0;
	private int currentVisibleItem=0;
	private int lastVisibleItem=0;
	private int lastReceived=0;
private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			updateContent();
		}
	};

private DatabaseHelper databaseHelper;
private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_chain_layout);
		listView=(ListView) findViewById(R.id.lvGifts);
		id=getIntent().getStringExtra("id");
		setTitle(getResources().getString(R.string.chain_activity));
		
		//btnCreate=(ImageButton)findViewById(R.id.btnAddChain);
		//btnCreate.setOnClickListener(this);
		listView.setAdapter(new GiftAdapter(getApplicationContext()));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent giftIntent=new Intent(getApplicationContext(),GiftViewActivity.class);
				giftIntent.putExtra(Gift.class.getCanonicalName(),gifts.get(position));
				startActivity(giftIntent);
			}
		});
listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				setCurrentVisibleItem(firstVisibleItem);
				if (currentVisibleItem>lastVisibleItem)
				{
					
						if (currentVisibleItem==gifts.size()-2)
						{
							
							if (lastReceived==10)
							{
						Page=Page+1;
						getGiftList();
						
							}
						}
					
					
				}
				setLastVisibleItem(currentVisibleItem);
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Page=0;
		currentVisibleItem=0;
		lastVisibleItem=0;
		getGiftList();
		registerReceiver(broadcastReceiver, new IntentFilter("update_content"));
		startUpdateService(this);
	}
	private void getGiftList()
	{
		CallableTask.invoke(new Callable<Collection<GiftEntity>>(){

			@Override
			public Collection<GiftEntity> call() throws Exception {
				// TODO Auto-generated method stub
				if (id!="")
				return giftService.findByChainIdPaged(id,Page);
				else 
					return null;
			}
			
		}, new TaskCallback<Collection<GiftEntity>>() {

			@Override
			public void success(final Collection<GiftEntity> result) {
				
				if (result!=null)
				operateGifts(result);
			}

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	@Override
	public void getGiftImage(int index)
	{
		databaseHelper=new DatabaseHelper(this);
		db=databaseHelper.getReadableDatabase();
		final int ind=index;
		if (gifts.get(index).getGiftImageUrl()!="" && gifts.get(index).getGiftImageUrl()!=null)
		{
			Cursor cursor=db.rawQuery("SELECT * FROM image_entity WHERE url = ?", new String[]{gifts.get(index).getGiftImageUrl()});	
			if (cursor==null || cursor.getCount()==0)
			{
				db.close();
				databaseHelper.close();
			downloadGiftImage(ind,imageService);
			}
			else
			{
				cursor.moveToFirst();
				gifts.get(index).setGiftImage(cursor.getBlob(2));
				cursor.close();
				db.close();
				databaseHelper.close();
				if (ind<gifts.size()-1)
				{
					
					getGiftImage(ind+1);
				}
				else
				{								
					setListViewData();
				}
			}
		}
		else
			if (index<gifts.size()-1)
			{
				index+=1;
				getGiftImage(index);
			}
			
		
	}

	@Override
	public void setListViewData()
	{
		mAdapter=new GiftAdapter(this,getGifts(),true,false);
		listView.setAdapter(mAdapter);		
		listView.invalidateViews();
	}
	
	
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	getMenuInflater().inflate(R.menu.header_chain_menu, menu);
	MenuItem item=menu.findItem(R.id.action_create);
	View menuView=item.getActionView();
	btnCreate=(LinearLayout)menuView.findViewById(R.id.btnAddChain);
	TextView tvLabel=(TextView)menuView.findViewById(R.id.tvMenuLabel);
	tvLabel.setText("Add");
	btnCreate.setOnClickListener(this);
	return super.onCreateOptionsMenu(menu);
}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnAddChain:
			if (userManager.isAuthorized())
			{
			Intent giftCreateIntent=new Intent(getApplicationContext(),GiftCreateActivity.class);
			giftCreateIntent.putExtra("create_new_chain", false);
			giftCreateIntent.putExtra("id", id);
			startActivityForResult(giftCreateIntent, 121);
			}
			else
			{
				InfoDialogFragment dialog=new InfoDialogFragment("Log in to create a gift", true);
				dialog.show(getFragmentManager(), "dialog");
				}
			break;
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
		}
		return super.onMenuItemSelected(featureId, item);
	}



	@Override
	public void operateGifts(Collection<GiftEntity> result) {
		// TODO Auto-generated method stub
		setGifts(UtilityMethods.processGiftList(result));
		listView.setAdapter(new GiftAdapter(getApplicationContext(),getGifts(),true,false));
		if (gifts!=null)
		{
			getGiftImage(0);
		}
	}
	public ArrayList<Gift> getGifts() {
		return gifts;
	}


	public void setGifts(ArrayList<Gift> gifts) {
		this.gifts = gifts;
	}
	public int getTotal() {
		return Total;
	}

	public void setTotal(int total) {
		Total = total;
	}

	public int getPage() {
		return Page;
	}

	public void setPage(int page) {
		Page = page;
	}

	public int getCurrentVisibleItem() {
		return currentVisibleItem;
	}

	public void setCurrentVisibleItem(int currentVisibleItem) {
		this.currentVisibleItem = currentVisibleItem;
	}

	public int getLastVisibleItem() {
		return lastVisibleItem;
	}

	public void setLastVisibleItem(int lastVisibleItem) {
		this.lastVisibleItem = lastVisibleItem;
	}

	@Override
	public void performAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub
		Page=0;
		currentVisibleItem=0;
		lastVisibleItem=0;
		getGiftList();
	}

	@Override
	public void startUpdateService(Context context) {
		// TODO Auto-generated method stub
		UpdateServiceCommon.startUpdateService(this);
	}

	@Override
	public void stopUpdateService(Context context) {
		// TODO Auto-generated method stub
		UpdateServiceCommon.stopUpdateService(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (broadcastReceiver!=null)
		unregisterReceiver(broadcastReceiver);
		stopUpdateService(this);
		super.onPause();
	}
}
