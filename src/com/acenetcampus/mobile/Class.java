package com.acenetcampus.mobile;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Class extends Activity {
	private Main mainObject = new Main();
	String webRoot = "http://10.0.2.2/";
	String sourceXML = "classes.xml";
	String profilePicURL;
	ArrayList<ClassUpdate> classesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.classes);

		getData(webRoot + sourceXML);
		ListView lv = (ListView) findViewById(android.R.id.list);
        
		lv.setTextFilterEnabled(false);
        lv.setAdapter(new ClassAdapter(this, R.layout.list_view, classesList));
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String classClicked = classesList.get(arg2).getName();
				String descriptionClicked = classesList.get(arg2).getDescription();
				String imageClicked = classesList.get(arg2).getImage();
				String followClicked = classesList.get(arg2).getFollow();
				
				Intent commentIntent = new Intent(getApplicationContext(), ClassPage.class);
				
				Bundle extra = new Bundle();
				extra.putString("class",classClicked);
				extra.putString("description",descriptionClicked);
				extra.putString("image",imageClicked);
				extra.putString("follow", followClicked);
				commentIntent.putExtras(extra);
				
				startActivity(commentIntent);
			}
		});

	}

	private void getData(String url) {
		String test = "";
		org.w3c.dom.Element docRoot = mainObject.ParseXML(url);
		classesList = new ArrayList<ClassUpdate>();
		
		NodeList classNodes = docRoot.getElementsByTagName("class");
		for(int i=0;i<classNodes.getLength();i++){
			Node classNode = classNodes.item(i);
			ClassUpdate classUpdate = new ClassUpdate();
			
			NodeList classAttribs = classNode.getChildNodes();
			for(int j=0;j<classAttribs.getLength();j++){
				Node classAttrib = classAttribs.item(j);
				if(classAttrib.getNodeName().equalsIgnoreCase("name")){
					classUpdate.setName(classAttrib.getFirstChild().getNodeValue());
				}
				if(classAttrib.getNodeName().equalsIgnoreCase("image")){
					classUpdate.setImage(classAttrib.getFirstChild().getNodeValue());
				}
				if(classAttrib.getNodeName().equalsIgnoreCase("description")){
					classUpdate.setDescription(classAttrib.getFirstChild().getNodeValue());
				}
				if(classAttrib.getNodeName().equalsIgnoreCase("follow")){
					classUpdate.setFollow																		(classAttrib.getFirstChild().getNodeValue());
				}
			}
			test += classUpdate.getName();
			classesList.add(classUpdate);
		}
		
	}

	public void Check(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}
}
