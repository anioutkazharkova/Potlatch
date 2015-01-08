package org.coursera.capstone.client.activities;

import java.io.IOException;

import org.coursera.capstone.client.R;
import org.coursera.capstone.client.adapters.AddPictureAdapter;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SelectImageDialogFragment extends DialogFragment {


	String[] selectOptions=new String[]{"Take a picture","Select from Gallery"};
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=getActivity().getLayoutInflater().inflate(R.layout.listview_add_picture,null);
		ListView listView=(ListView) view.findViewById(R.id.lvAddPicture);
		listView.setAdapter(new AddPictureAdapter(getActivity(),selectOptions));
		
		final Context mContext=getActivity();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {				
				if (position==0)
				{
					//Toast.makeText(getActivity(), "Take a pic", Toast.LENGTH_LONG).show();
					try {
						GiftCreateActivity.createTakePictureIntent(mContext);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					dismiss();
				}
				else
				{
					//Toast.makeText(getActivity(), "Select from gallery", Toast.LENGTH_LONG).show();
					GiftCreateActivity.createSelectIntent(mContext);
					dismiss();
				}
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Select a picture").setView(view);		
		return builder.create();
	}
	

}
