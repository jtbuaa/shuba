package com.qiwenge.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * BaseFragment
 * 
 * Created by John on 2014-4-26
 */
public class BaseFragment extends Fragment {

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}

	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(getActivity(), cls);
		startActivity(intent);
	}

	public void startActivity(Class<?> cls, Bundle extra) {
		Intent intent = new Intent(getActivity(), cls);
		intent.putExtras(extra);
		startActivity(intent);
	}

}
