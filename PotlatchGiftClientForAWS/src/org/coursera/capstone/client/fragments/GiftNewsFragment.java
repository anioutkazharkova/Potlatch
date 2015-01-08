package org.coursera.capstone.client.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.R;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.UserManager;
import org.coursera.capstone.client.activities.GiftCreateActivity;
import org.coursera.capstone.client.activities.GiftNavigationActivity;
import org.coursera.capstone.client.activities.GiftViewActivity;
import org.coursera.capstone.client.adapters.GiftAdapter;
import org.coursera.capstone.client.common.UtilityMethods;
import org.coursera.capstone.client.core.GiftEntitySvcApi;
import org.coursera.capstone.client.core.ImageEntitySvcApi;
import org.coursera.capstone.client.core.entities.GiftEntity;
import org.coursera.capstone.client.database.DatabaseHelper;
import org.coursera.capstone.client.entities.Gift;
import org.coursera.capstone.client.service.IServiceUpdatable;
import org.coursera.capstone.client.service.UpdateServiceCommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class GiftNewsFragment extends LoadImageGiftFragment implements OnClickListener,IServiceUpdatable {
	
	ListView listView;
	SearchView searchView;	
	LinearLayout btnCreate;
	boolean isAutorized=false;	
	GiftAdapter mAdapter;
	private int Total;
	private int Page=0;
	private int currentVisibleItem=0;
	private int lastVisibleItem=0;
	private int lastReceived=0;
	private boolean showFiltered=false;
	private DatabaseHelper databaseHelper;
	SQLiteDatabase db;
	private BroadcastReceiver broadcastReceiver=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.giftnews_layout, null);
		
		
		listView=(ListView) view.findViewById(R.id.lvGifts);
		listView.setClickable(true);
		setHasOptionsMenu(true);
		//searchView=(SearchView)view.findViewById(R.id.etSearchGift);
		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getActivity());
		if (sp.getBoolean("chbShowInappopriate", false)==true||sp.getBoolean("chbShowObscene", false)==true){
			showFiltered=true;
		}
		else
			showFiltered=false;
		UserManager manager=((GiftNavigationActivity)getActivity()).userManager;
		isAutorized=manager.isAuthorized();
		if (isAutorized)
		{
			((GiftNavigationActivity)getActivity()).initServices(manager.getAuthUser());
		}
		else
		{
			((GiftNavigationActivity)getActivity()).initServices(manager.getAnonymousUser());
		}		
				
		listView.setAdapter(new GiftAdapter(getActivity()));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent giftIntent=new Intent(getActivity(),GiftViewActivity.class);
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
						final GiftEntitySvcApi giftService=((GiftNavigationActivity)getActivity()).giftService;
						getGiftsList(giftService);
						
							}
						}
					
					
				}
				setLastVisibleItem(currentVisibleItem);
			}
		});
		
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		final GiftEntitySvcApi giftService=((GiftNavigationActivity)getActivity()).giftService;
		Page=0;
		currentVisibleItem=0;
		lastVisibleItem=0;
		getGiftsList(giftService);
		restart();
	}
	
	private void restart()
	{
		broadcastReceiver=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				updateContent();
			}
		};
		getActivity().registerReceiver(broadcastReceiver, new IntentFilter("update_content"));
		startUpdateService(getActivity());
	}
	
	@Override
	public void getGiftsList(final GiftEntitySvcApi giftService)
	{
		
		CallableTask.invoke(new Callable<Collection<GiftEntity>>(){

			@Override
			public Collection<GiftEntity> call() throws Exception {
				// TODO Auto-generated method stub
				if (!isAutorized|| !showFiltered)
				{
				return giftService.getGiftsPaged(Page);
				
				}
				else
					return giftService.getGiftsFilteredPaged(Page);
			}}, new TaskCallback<Collection<GiftEntity>>(){

				@Override
				public void success(Collection<GiftEntity> result) {
					// TODO Auto-generated method stub
					operateGiftList(result);					
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
		databaseHelper=new DatabaseHelper(getActivity());
		db=databaseHelper.getReadableDatabase();
		final int ind=index;
		if (gifts.get(index).getGiftImageUrl()!="" && gifts.get(index).getGiftImageUrl()!=null)
		{
			final ImageEntitySvcApi	imageService=((GiftNavigationActivity)getActivity()).imageService;
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

	private void getGiftsByTitle(final String searchTitle,final GiftEntitySvcApi giftService)
	{
		CallableTask.invoke(new Callable<Collection<GiftEntity>>(){

			@Override
			public Collection<GiftEntity> call() throws Exception {
				// TODO Auto-generated method stub
				return giftService.findByTitle(searchTitle);
			}}, new TaskCallback<Collection<GiftEntity>>(){

				@Override
				public void success(Collection<GiftEntity> result) {
					// TODO Auto-generated method stub
					if (result!=null)
					{
						if (result.size()==0)
						{
							InfoDialogFragment dialog=new InfoDialogFragment("Nothing was found", true);
							dialog.show(getFragmentManager(), "dialog");
						}
							
					}
					operateGiftList(result);					
				}

				@Override
				public void error(Exception e) {
					// TODO Auto-generated method stub
					
				}
				
			});	
	}
	
	@Override
	public void setListViewData()
	{
		mAdapter=new GiftAdapter(getActivity(),getGifts());
		listView.setAdapter(mAdapter);		
		listView.invalidateViews();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnAddChain:
			if (isAutorized)
			{
			Intent giftCreateIntent=new Intent(getActivity(),GiftCreateActivity.class);
			giftCreateIntent.putExtra("create_new_chain", true);
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


	public ArrayList<Gift> getGifts() {
		return gifts;
	}


	public void setGifts(ArrayList<Gift> gifts) {
		this.gifts = gifts;
	}

	@Override
	public void operateGiftList(Collection<GiftEntity> result) {
		// TODO Auto-generated method stub
		if (result!=null && result.size()>0)
		{
		setGifts(UtilityMethods.processGiftList(result));
		lastReceived=gifts.size();
		mAdapter=new GiftAdapter(getActivity(),getGifts());
		listView.setAdapter(mAdapter);
		listView.invalidateViews();
		if (gifts!=null)
		{
			getGiftImage(0);
		}
		}
		else
		{	mAdapter=new GiftAdapter(getActivity());
			listView.setAdapter(mAdapter);
		}
	}

@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// TODO Auto-generated method stub
	inflater.inflate(R.menu.header_menu, menu);
	MenuItem item=menu.findItem(R.id.action_search);
	View menuView=item.getActionView();
	searchView=(SearchView) menuView.findViewById(R.id.svSearchGift);
	searchView.clearFocus();
	final GiftEntitySvcApi giftService=((GiftNavigationActivity)getActivity()).giftService;
	searchView.setOnQueryTextListener(new OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String query) {
			// TODO Auto-generated method stub
			if (query!="")
			{
				pause();
			getGiftsByTitle(query, giftService);
			}
			else 
				{
				restart();
				getGiftsList(giftService);
				}
			searchView.clearFocus();
			return true;
			
		}
		
		@Override
		public boolean onQueryTextChange(String newText) {
			// TODO Auto-generated method stub
			
				
			return true;
		}
	});	
	
	item=menu.findItem(R.id.action_create);
	menuView=item.getActionView();
	btnCreate=(LinearLayout)menuView.findViewById(R.id.btnAddChain);
	TextView tvLabel=(TextView)menuView.findViewById(R.id.tvMenuLabel);
	tvLabel.setText("Add");
	btnCreate.setOnClickListener(this);
	super.onCreateOptionsMenu(menu, inflater);
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

public int getLastReceived() {
	return lastReceived;
}

public void setLastReceived(int lastReceived) {
	this.lastReceived = lastReceived;
}

@Override
public void updateContent() {
	// TODO Auto-generated method stub
	final GiftEntitySvcApi giftService=((GiftNavigationActivity)getActivity()).giftService;
	Page=0;
	currentVisibleItem=0;
	lastVisibleItem=0;
	getGiftsList(giftService);
}

@Override
public void startUpdateService(Context context) {
	// TODO Auto-generated method stub
	UpdateServiceCommon.startUpdateService(getActivity());
}

@Override
public void stopUpdateService(Context context) {
	// TODO Auto-generated method stub
	UpdateServiceCommon.stopUpdateService(getActivity());
}
@Override
public void onPause() {
	// TODO Auto-generated method stub
	pause();
	super.onPause();
}
private void pause()
{
	if (broadcastReceiver!=null)
	{
		getActivity().unregisterReceiver(broadcastReceiver);
		broadcastReceiver=null;
		
	}
		stopUpdateService(getActivity());
}
}
