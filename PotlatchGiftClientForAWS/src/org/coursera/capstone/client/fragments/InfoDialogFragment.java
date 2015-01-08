package org.coursera.capstone.client.fragments;

import org.coursera.capstone.client.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InfoDialogFragment extends DialogFragment {

	private String message;
	private boolean isOneButton=false;
	public InfoDialogFragment(){
		
	}
	public InfoDialogFragment(String message)
	{
		this.message=message;
	}
	public InfoDialogFragment(String message,boolean isOneButton)
	{
		this(message);
		this.isOneButton=isOneButton;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

View bodyTitle=getActivity().getLayoutInflater().inflate(R.layout.info_dialog_layout,null);
TextView textView=(TextView)bodyTitle.findViewById(R.id.tvDialogText);

textView.setText(message);

builder.setView(bodyTitle);
builder.setPositiveButton(R.string.ok,new OnClickListener() {
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		if (!isOneButton)
		{
		((IActionable)getActivity()).performAction();
		}
		dismiss();
	}

	
});
if (!isOneButton)
{
builder.setNegativeButton(R.string.cancel, new OnClickListener() {
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		dismiss();
	}
});
}

            return builder.create();
	}
}
