package org.coursera.capstone.client.fragments;



import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.R;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.UserManager;
import org.coursera.capstone.client.activities.GiftNavigationActivity;
import org.coursera.capstone.client.adapters.UserAdapter;
import org.coursera.capstone.client.core.Settings;
import org.coursera.capstone.client.core.UserEntitySvcApi;
import org.coursera.capstone.client.core.entities.UserEntity;
import org.coursera.capstone.client.entities.User;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TopGiversFragment extends Fragment {
	
	private ArrayList<User> userList;
	private ListView listView;
	private UserAdapter mAdapter;
	private boolean isAutorized;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.top_users_layout, null);
		listView=(ListView) view.findViewById(R.id.lvUsers);
		mAdapter=new UserAdapter(getActivity().getApplicationContext());
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
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		final UserEntitySvcApi userService=((GiftNavigationActivity)getActivity()).userService;
		
		CallableTask.invoke(new Callable<Collection<UserEntity>>(){

			@Override
			public Collection<UserEntity> call() throws Exception {
				// TODO Auto-generated method stub
				return userService.getUsersTopList();
			}
			
		}, new TaskCallback<Collection<UserEntity>>() {

			@Override
			public void success(Collection<UserEntity> result) {
				// TODO Auto-generated method stub
				setUserList(processUsersList(result));
				mAdapter=new UserAdapter(getActivity().getApplicationContext(),userList);
				listView.setAdapter(mAdapter);
			}

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private ArrayList<User> processUsersList(Collection<UserEntity> list)
	{
		ArrayList<User> users=new ArrayList<User>();
		for(UserEntity item:list)
		{
			User user=new User();
			user.setName(item.getName());
			user.setUserToken(item.getUuid());
			user.setGiftCounts(item.getGiftCounts());
			
			if (!item.getRole().equals(Settings.anonymous))
			users.add(user);
		}
		
		return users;
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}
}
