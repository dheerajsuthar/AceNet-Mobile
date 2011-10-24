package com.acenetcampus.mobile;

import android.app.Activity;

import com.acenetcampus.mobile.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Comment extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
       
        final EditText edt = (EditText) findViewById(R.id.editText1);
        TextView t = (TextView) findViewById(R.id.textView1);
        Button b = (Button) findViewById(R.id.button1);
        Bundle extra = getIntent().getExtras();
     
        t.setText((extra.getString("user") + ":\n" + extra.getString("update")));
        ImageView iv = (ImageView) findViewById(R.id.imageView1);
		final Main imageGetter = new Main();
		Bitmap bmp = imageGetter.getImageFromWeb(imageGetter.webRoot+extra.getString("pic"));
		iv.setImageBitmap(bmp);
		iv.setAdjustViewBounds(true);
		iv.setMaxHeight(120);
		iv.setMaxWidth(120);
        
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), edt.getText(), Toast.LENGTH_LONG).show();
				
			}
		});
    }
}
