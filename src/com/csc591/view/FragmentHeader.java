package com.csc591.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHeader extends Fragment {

	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_header,
				container,
				false);
		return view;
	}
	
}
