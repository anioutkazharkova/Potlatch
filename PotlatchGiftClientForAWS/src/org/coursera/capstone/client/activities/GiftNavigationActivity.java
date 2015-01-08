package org.coursera.capstone.client.activities;

import org.coursera.capstone.client.R;
import org.coursera.capstone.client.fragments.GiftNewsFragment;
import org.coursera.capstone.client.fragments.InfoDialogFragment;
import org.coursera.capstone.client.fragments.LoginFragment;
import org.coursera.capstone.client.fragments.SettingsFragment;
import org.coursera.capstone.client.fragments.TopGiversFragment;
import org.coursera.capstone.client.fragments.UserGiftsFragment;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


public class GiftNavigationActivity extends BaseActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	@SuppressWarnings("deprecation")
	private ActionBarDrawerToggle mDrawerToggle;
	String[] mTitles;
	private LoginFragment loginFragment;
	private GiftNewsFragment mainFragment;
	private TopGiversFragment topGiversFragment;
	private UserGiftsFragment giftsFragment;
	private SettingsFragment settingsFragment;
	
	public int test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_layout);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mTitles= getResources().getStringArray(R.array.drawer_items_array);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        InitFragments();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
               // getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
               // getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
       
	}
	
	private void InitFragments()
	{
		mainFragment=new GiftNewsFragment();
		topGiversFragment=new TopGiversFragment();
		giftsFragment=new UserGiftsFragment();
		settingsFragment=new SettingsFragment();
		loginFragment=new LoginFragment();
		
	}
	

	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	         // The action bar home/up action should open or close the drawer.
	         // ActionBarDrawerToggle will take care of this.
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        // Handle action buttons
	        switch(item.getItemId()) {
	       
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

	    /* The click listner for ListView in the navigation drawer */
	    private class DrawerItemClickListener implements ListView.OnItemClickListener {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            selectItem(position);
	        }
	    }

	    public void changeFragment(int position)
	    {
	    	selectItem(position);
	    }
	    private void selectItem(int position) {
	      
	    	
	        FragmentManager fragmentManager = getFragmentManager();
	        switch(position)
	        {
	        case 0:
	        	mainFragment=new GiftNewsFragment();
	       fragmentManager.beginTransaction().replace(R.id.content_frame, mainFragment).commit();
	       break;
	        case 1:
	        	topGiversFragment=new TopGiversFragment();
	 	       fragmentManager.beginTransaction().replace(R.id.content_frame,topGiversFragment).commit();
	 	       break;
	        case 2:
	        	if (userManager.isAuthorized())
	        	{
	        	giftsFragment=new UserGiftsFragment();
	 	       fragmentManager.beginTransaction().replace(R.id.content_frame, giftsFragment).commit();
	        	}
	        	else
	        	{
	        		InfoDialogFragment dialog=new InfoDialogFragment("Log in to view your gifts", true);
					dialog.show(getFragmentManager(), "dialog");
	        	}
	 	       break;
	        case 3:
	        	settingsFragment=new SettingsFragment();
	 	       fragmentManager.beginTransaction().replace(R.id.content_frame,settingsFragment).commit();
	 	       break;
	        case 4:
	        	loginFragment=new LoginFragment();
	 	       fragmentManager.beginTransaction().replace(R.id.content_frame, loginFragment).commit();
	 	       break;
	        }
	        // update selected item and title, then close the drawer
	        mDrawerList.setItemChecked(position, true);
	       // setTitle(mPlanetTitles[position]);
	        mDrawerLayout.closeDrawer(mDrawerList);
	        setTitle(mTitles[position]);
	    }

	  

	    /**
	     * When using the ActionBarDrawerToggle, you must call it during
	     * onPostCreate() and onConfigurationChanged()...
	     */

	    @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }

	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        // Pass any configuration change to the drawer toggls
	       
	
	    }
	    @Override
	    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    	// TODO Auto-generated method stub
	    	return super.onMenuItemSelected(featureId, item);
	    }
}
