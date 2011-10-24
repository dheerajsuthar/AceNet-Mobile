package com.acenetcampus.mobile;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ClassPage extends Activity {
	Bundle extras;
	String sourceXML = "class_updates.xml";
	String profilePicURL;
	ArrayList<Update> updateList;
	Main mainObject = new Main();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		extras = getIntent().getExtras();
		if (extras.getString("follow").equalsIgnoreCase("false")) {
			showFollowPage();
		} else {
			showClassUpdates();
		}
	}

	void showFollowPage() {
		setContentView(R.layout.classpagenotfound);
		TextView tvTitle, tvDesc;
		tvTitle = (TextView) findViewById(R.id.textView1);
		tvDesc = (TextView) findViewById(R.id.textView2);
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		Button b = (Button) findViewById(R.id.button1);

		tvTitle.setText(extras.getString("class"));
		tvDesc.setText(extras.getString("description"));

		Main imageGetter = new Main();
		Bitmap bmp = imageGetter.getImageFromWeb(imageGetter.webRoot
				+ extras.getString("image"));
		iv.setImageBitmap(bmp);
		iv.setAdjustViewBounds(true);
		iv.setMaxHeight(120);
		iv.setMaxWidth(120);

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Now following",
						Toast.LENGTH_LONG).show();

			}
		});

	}

	void showClassUpdates() {
		String webRoot = mainObject.webRoot;
		setContentView(R.layout.main);
		
		sourceXML =  extras.getString("class").toLowerCase() + "_" + sourceXML;
		Toast.makeText(this, sourceXML, Toast.LENGTH_LONG).show();
		getPageData(webRoot + sourceXML);
		loadProfilePic(webRoot + profilePicURL);
		Button b = (Button) findViewById(R.id.button1);
		final EditText et = (EditText) findViewById(R.id.editText1);

		ListView lv = (ListView) findViewById(android.R.id.list);

		lv.setAdapter(new MyAdapter(this, R.layout.list_view, updateList));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String userClicked = updateList.get(arg2).getUser();
				String updateClicked = updateList.get(arg2).getUpdate();
				String picClicked = updateList.get(arg2).getUserPic();
				
				Intent commentIntent = new Intent(getApplicationContext(), Comment.class);
				
				Bundle extra = new Bundle();
				extra.putString("user", userClicked);
				extra.putString("update", updateClicked);
				extra.putString("pic", picClicked);
				commentIntent.putExtras(extra);
				
				startActivity(commentIntent);
			}
		});
	}

	void getPageData(String url) {
		String test = "";
		Element pageRoot = mainObject.ParseXML(url);// equal to user
		NodeList nodes = pageRoot.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().equalsIgnoreCase("profilePic")) {
				profilePicURL = node.getFirstChild().getNodeValue();

			}

			if (node.getNodeName().equalsIgnoreCase("updates")) { // equal to
																	// updates
				updateList = new ArrayList<Update>();

				NodeList updateNodes = node.getChildNodes();
				for (int j = 0; j < updateNodes.getLength(); j++) {
					if (updateNodes.item(j).getNodeName()
							.equalsIgnoreCase("update")) // equals to update
					{
						Update update = new Update();
						String user = null, profilePic = null, message = null;

						Node updateNode = updateNodes.item(j);
						NodeList updateAttribs = updateNode.getChildNodes();

						for (int k = 0; k < updateAttribs.getLength(); k++) {
							Node updateAttrib = updateAttribs.item(k);
							if (updateAttrib.getNodeName().equalsIgnoreCase(
									"user")) {
								user = updateAttrib.getFirstChild()
										.getNodeValue();

							}
							if (updateAttrib.getNodeName().equalsIgnoreCase(
									"profilePic")) {
								profilePic = updateAttrib.getFirstChild()
										.getNodeValue();

							}
							if (updateAttrib.getNodeName().equalsIgnoreCase(
									"message")) {
								message = updateAttrib.getFirstChild()
										.getNodeValue();

							}

							// updateList.add(update);
						}

						update.setUser(user);
						update.setUserPic(profilePic);
						update.setUpdate(message);
						updateList.add(update);
					}
				}

			}
		}
	}

	void loadProfilePic(String url) {
		ImageButton profilePic = (ImageButton) findViewById(R.id.imageButton1);
		Bitmap profilePicBmp = mainObject.getImageFromWeb(url);

		profilePic.setImageBitmap(profilePicBmp);
		profilePic.setAdjustViewBounds(true);
		profilePic.setMaxHeight(100);
		profilePic.setMaxWidth(100);

	}
}
