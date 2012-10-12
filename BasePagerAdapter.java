package com.tophyr.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BasePagerAdapter extends PagerAdapter {

	private BaseAdapter m_Adapter;
	private ArrayList<Queue<View>> m_Ready;
	private HashMap<Object, View> m_Associations;
	
	public BasePagerAdapter(BaseAdapter adapter) {
		m_Adapter = adapter;
		
		final int viewTypeCount = adapter.getViewTypeCount();
		m_Ready = new ArrayList<Queue<View>>(viewTypeCount);
		for (int i = 0; i < viewTypeCount; i++)
			m_Ready.add(new LinkedList<View>());
		m_Associations = new HashMap<Object, View>();
	}
	
	@Override
	public int getCount() {
		return m_Adapter.getCount();
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public boolean isViewFromObject(View view, Object ob) {
		return view.equals(m_Associations.get(ob));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Object ob = m_Adapter.getItem(position);
		
		int viewType = m_Adapter.getItemViewType(position);
		View convertView = m_Ready.get(viewType).poll();
		View newView = m_Adapter.getView(position, convertView, container);
		
		if (convertView != newView && convertView != null)
			m_Ready.get(viewType).add(convertView); // if we didn't use the convertView, add it back to the ready queue
		m_Associations.put(ob, newView);
		
		container.addView(newView);
		
		return ob;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View v = m_Associations.remove(object);
		if (v != null) {
			m_Ready.get(m_Adapter.getItemViewType(position)).add(v);
			container.removeView(v);
		}
	}
}