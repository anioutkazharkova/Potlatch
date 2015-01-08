package org.coursera.capstone.client.adapters;



import org.coursera.capstone.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddPictureAdapter extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;
	String[] items;
	
	public AddPictureAdapter(Context context)
	{
		mContext=context;
		inflater=(LayoutInflater)LayoutInflater.from(mContext);
	}
	
	public AddPictureAdapter(Context context,String[] items)
	{
		this(context);
		this.items=items;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (items!=null)
		return items.length;
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items[position];
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
		if (view==null)
		{
		view=inflater.inflate(R.layout.picture_add_dialog_item, null);	
		}
		TextView textView=(TextView)view.findViewById(R.id.tvDialogAddPicture);
		ImageView image=(ImageView)view.findViewById(R.id.imDialogAddPicture);
		String item=items[position];
		textView.setText(item);
		if (position==0)
		{
			image.setImageResource(R.drawable.ic_action_camera);
		}
		else
			image.setImageResource(R.drawable.ic_action_picture);
		return view;
	}

}
