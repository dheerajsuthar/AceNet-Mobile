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

public class ClassAdapter extends ArrayAdapter<ClassUpdate> implements ListAdapter {
	Context myContext;
	ArrayList<ClassUpdate> classes;

	public ClassAdapter(Context context, int resource, ArrayList<ClassUpdate> classList) {

		super(context, resource, classList);
		this.myContext = context;
		this.classes = classList;
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

		ClassUpdate classUpdate = null;
		classUpdate = classes.get(position);
		if (classUpdate != null) {
			TextView tv = (TextView) row.findViewById(R.id.textView1);
			TextView tvDesc = (TextView) row.findViewById(R.id.textView2);
			tv.setText(classUpdate.getName()+":\n ");
			tvDesc.setText(classUpdate.getDescription());
			
			ImageView iv = (ImageView) row.findViewById(R.id.imageView1);
			Main imageGetter = new Main();
			Bitmap bmp = imageGetter.getImageFromWeb(imageGetter.webRoot+classUpdate.getImage());
			iv.setImageBitmap(bmp);
			iv.setAdjustViewBounds(true);
			iv.setMaxHeight(60);
			iv.setMaxWidth(60);

		}
		return row;
	}

	
}