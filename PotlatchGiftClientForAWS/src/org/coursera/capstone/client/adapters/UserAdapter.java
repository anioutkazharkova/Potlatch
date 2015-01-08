package org.coursera.capstone.client.adapters;

import java.util.ArrayList;

import org.coursera.capstone.client.R;
import org.coursera.capstone.client.entities.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<User> users;
	 public UserAdapter(Context context) {
		// TODO Auto-generated constructor stub
		 mContext=context;
	}
	  public UserAdapter(Context context,ArrayList<User> list) {
		// TODO Auto-generated constructor stub
		  this(context);
		  users=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=convertView;
		User currentUser=users.get(position);
		if (view==null)
		{
			view=LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, null);
		}
		TextView tvUser=(TextView) view.findViewById(R.id.tvUserName);
		TextView tvGiftCount=(TextView)view.findViewById(R.id.tvUserGiftCount);
		ImageView imageCup=(ImageView)view.findViewById(R.id.imCup);
		tvUser.setText(currentUser.getName());
		tvGiftCount.setText(currentUser.getGiftCounts()+"");
		if (position==0)
			imageCup.setImageResource(R.drawable.gold);
		else 
			if (position==1)
			{
				imageCup.setImageResource(R.drawable.silver);
			}
			else
				if (position==2)
				{
					imageCup.setImageResource(R.drawable.bronze);
				}
		return view;
	}

}
