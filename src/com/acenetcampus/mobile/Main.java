package com.acenetcampus.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.acenetcampus.mobile.MyAdapter;
import com.acenetcampus.mobile.R;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	/** Called when the activity is first created. */
	int UPDATE_COUNT = 10;
	String webRoot = "http://10.0.2.2/";
	String sourceXML = "user.xml";
	String profilePicURL;
	ArrayList<Update> updateList;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get data for page
		// attrib: profilePic,,updates
		getPageData(webRoot+sourceXML);
		loadProfilePic(webRoot+profilePicURL);
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
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				postUpdate(et.getText().toString());
			}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch(itemId){
		case 1:
			Toast.makeText(this, "update selected", Toast.LENGTH_LONG).show();
			break;
		case 2:
			Toast.makeText(this, "classes selected", Toast.LENGTH_LONG).show();
			Intent classIntent = new Intent(getApplicationContext(), Class.class);
			startActivity(classIntent);
			break;
		
		}
		return true;
	}
	void getPageData(String url) {
		String test = "";
		Element pageRoot = ParseXML(url);// equal to user
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

	Bitmap getImageFromWeb(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Bitmap imgBmp = BitmapFactory.decodeStream(is);
			return imgBmp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	

	void loadProfilePic(String url) {
		ImageButton profilePic = (ImageButton) findViewById(R.id.imageButton1);
		Bitmap profilePicBmp = getImageFromWeb(url);

		profilePic.setImageBitmap(profilePicBmp);
		profilePic.setAdjustViewBounds(true);
		profilePic.setMaxHeight(100);
		profilePic.setMaxWidth(100);

	}

	void postUpdate(String update) {
		Toast.makeText(this, "You posted:\n" + update, Toast.LENGTH_LONG)
				.show();

	}

	public InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream is = null;
		int responseCode = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		if (!(conn instanceof HttpURLConnection)) {
			throw new IOException("Its not Http Connection");
		}
		try {
			HttpURLConnection httpCon = (HttpURLConnection) conn;
			httpCon.setAllowUserInteraction(false);
			httpCon.setInstanceFollowRedirects(true);
			httpCon.setRequestMethod("GET");
			httpCon.connect();
			responseCode = httpCon.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				is = httpCon.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting.");
		}
		return is;
	}

	public Element ParseXML(String URL) {
		try {
			InputStream is = OpenHttpConnection(URL);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				try {
					Document doc = db.parse(is);
					org.w3c.dom.Element root = doc.getDocumentElement();
					return root;
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private String DownloadText(String URL) {

		int BUFFER_SIZE = 2000;
		InputStream in = null;

		try {
			in = OpenHttpConnection(URL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
			return "";
		}

		InputStreamReader isr = new InputStreamReader(in);
		int charRead;
		String str = "";
		char[] inputBuffer = new char[BUFFER_SIZE];

		try {
			while ((charRead = isr.read(inputBuffer)) > 0) {
				String readString = String
						.copyValueOf(inputBuffer, 0, charRead);
				str += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
			return "";
		}
		return str;
	}

	public void Check(String str) {
		if(str!=null)
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	public void CreateMenu(Menu menu){
		MenuItem mUpdates = menu.add(0, 1, 1, "updates");
		mUpdates.setAlphabeticShortcut('u');
		mUpdates.setIcon(R.drawable.icon);
		
		MenuItem mClasses = menu.add(0, 2, 2, "clubs");
		mClasses.setAlphabeticShortcut('c');
		mClasses.setIcon(R.drawable.icon);
		
	}
}