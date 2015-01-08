package org.coursera.capstone.client.fragments;


import org.coursera.capstone.client.R;
import org.coursera.capstone.client.UserManager;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
	CheckBoxPreference setInapp,setObscene;
	ListPreference setUpdateTime;
	boolean enableFilter=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.option_layout_fragment);
		setInapp=(CheckBoxPreference)findPreference("chbShowInappopriate");
		setObscene=(CheckBoxPreference)findPreference("chbShowObscene");
		setUpdateTime=(ListPreference)findPreference("listUpdateTime");
		
		enableFilter=UserManager.getInstance().isAuthorized();
		setInapp.setEnabled(enableFilter);
		setObscene.setEnabled(enableFilter);
	}

	
}
