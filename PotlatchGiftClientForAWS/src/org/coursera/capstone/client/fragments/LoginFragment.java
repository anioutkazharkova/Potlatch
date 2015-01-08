package org.coursera.capstone.client.fragments;


import java.util.concurrent.Callable;

import org.coursera.capstone.client.CallableTask;
import org.coursera.capstone.client.R;
import org.coursera.capstone.client.TaskCallback;
import org.coursera.capstone.client.UserManager;
import org.coursera.capstone.client.activities.GiftNavigationActivity;
import org.coursera.capstone.client.core.UserEntitySvcApi;
import org.coursera.capstone.client.core.UserSvc;
import org.coursera.capstone.client.core.entities.UserEntity;
import org.coursera.capstone.client.entities.User;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment implements OnClickListener{
	
	UserEntitySvcApi userService;
	
	EditText etLogin,etPass;
	Button btnLogin;
	boolean isLogined=false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.activity_login_screen, null);
		etLogin=(EditText)view.findViewById(R.id.etLogin);
		etPass=(EditText)view.findViewById(R.id.etPass);
		btnLogin=(Button)view.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		userService=((GiftNavigationActivity)getActivity()).userService;
		 UserManager manager=((GiftNavigationActivity)getActivity()).userManager;
		if (manager.isAuthorized())
		{
			etLogin.setText(manager.getAuthUser().getName());
			etPass.setText(manager.getAuthUser().getPassword());	
			etLogin.setEnabled(false);
		    etLogin.setTextColor(Color.BLACK);
		    etPass.setEnabled(false);
		    etPass.setTextColor(Color.BLACK);
			isLogined=true;
			btnLogin.setText("Logout");
		}
		else
		{	
			etLogin.setEnabled(true);
			etPass.setEnabled(true);
			btnLogin.setText("Login");
			isLogined=false;
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnLogin:
			login();
			break;
		}
	}
	private void GoToPosition(int item)
	{
		((GiftNavigationActivity)getActivity()).changeFragment(item);
	}

	private void initServices(User user)
	{
		((GiftNavigationActivity)getActivity()).initServices(user);
	}
	private void login() {
		final UserManager manager=((GiftNavigationActivity)getActivity()).userManager;
		if (!isLogined)
		{
		final String login=etLogin.getText().toString();
		final String pass=etPass.getText().toString();
		
		final UserEntitySvcApi tempService=UserSvc.init(new User(login,pass));
		
		// TODO Auto-generated method stub
		CallableTask.invoke(new Callable<UserEntity>() {

			@Override
			public UserEntity call() throws Exception {
				// TODO Auto-generated method stub
				return tempService.getAuthorities();
			}

			
			
		},new TaskCallback<UserEntity>() {

			@Override
			public void success(UserEntity result) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity().getApplicationContext(), result.getRole(),Toast.LENGTH_LONG).show();
				manager.Authorize(new User(login,pass));
				
				Log.d("login",manager.isAuthorized()+" "+manager.getAuthUser().getName());
				GoToPosition(0);
				initServices(manager.getAuthUser());
			}

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity().getApplicationContext(), "error",Toast.LENGTH_LONG).show();
				Log.d("login",manager.isAuthorized()+" "+manager.getAnonymousUser().getName());
			}
		});
		}
		else
		{
		manager.UnAuthorize();
		
		Log.d("logout",manager.isAuthorized()+" "+manager.getAnonymousUser().getName());
		GoToPosition(0);
		initServices(manager.getAnonymousUser());
		}
	}
}
