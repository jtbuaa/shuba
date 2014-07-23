package com.dev1024.ui;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * MyBaseAdapter
 * 
 * Created by John
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	public Context context;

	public List<T> data;

	@Override
	public int getCount() {
		return (data == null || data.isEmpty()) ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return (data == null || data.isEmpty()) ? null : data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
