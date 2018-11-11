package com.gloiot.hygoSupply.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;

import java.util.List;
import java.util.Map;

public class SharePopWindAdapter extends BaseAdapter {

	private List<Map<String, Object>> mList = null ;
    Context mContext;
    Map<String, Object> map =null;
    public SharePopWindAdapter(Context context, List<Map<String, Object>> list) {
    	mContext = context;
    	mList = list;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	Holder holder = null;
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.share_popwind_gridview, null);
			holder = new Holder();
			holder.share_popwind_Img = (ImageView) view.findViewById(R.id.share_popwind_Img);
			holder.share_popwind_txt = (TextView) view.findViewById(R.id.share_popwind_txt);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		map = mList.get(position);
		holder.share_popwind_Img.setImageResource((Integer) map.get("img"));
		holder.share_popwind_txt.setText(String.valueOf(map.get("name")));
		return view;
	}
	
	class Holder {
		ImageView share_popwind_Img;
		TextView share_popwind_txt;
	}

}
