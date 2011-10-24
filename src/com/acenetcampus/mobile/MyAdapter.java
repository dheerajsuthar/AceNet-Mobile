package com.acenetcampus.mobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.app.Activity;

public class MyAdapter extends ArrayAdapter<Update> implements ListAdapter {
	Context myContext;
	ArrayList<Update> updates;

	public MyAdapter(Context context, int resource, ArrayList<Update> updateList) {

		super(context, resource, updateList);
		this.myContext = context;
		this.updates = updateList;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) myContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_view, null);
		}

		Update update = null;
		update = updates.get(position);
		if (update != null) {
			TextView tv = (TextView) row.findViewById(R.id.textView1);
			TextView tvUpdate = (TextView) row.findViewById(R.id.textView2);
			tv.setText(update.getUser()+":\n ");
			tvUpdate.setText(update.getUpdate());
			
			ImageView iv = (ImageView) row.findViewById(R.id.imageView1);
			Main imageGetter = new Main();
			Bitmap bmp = imageGetter.getImageFromWeb(imageGetter.webRoot+update.getUserPic());
			iv.setImageBitmap(bmp);
			iv.setAdjustViewBounds(true);
			iv.setMaxHeight(70);
			iv.setMaxWidth(70);

		}
		return row;
	}

	
}