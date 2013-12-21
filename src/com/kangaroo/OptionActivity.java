package com.kangaroo;

import com.kangaroo.event.EventHandler;
import com.kangaroo.utility.About;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class OptionActivity extends Activity {
	Button btn_import_contacts;
	Button btn_synchronize_contacts;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option_activity_layout);
		btn_import_contacts=(Button) findViewById(R.id.btn_import_from_phone);
		btn_synchronize_contacts=(Button) findViewById(R.id.btn_import_from_server);
		btn_import_contacts.setOnClickListener(new EventHandler(this));
		btn_synchronize_contacts.setOnClickListener(new EventHandler(this));		
		
	}

public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.app_option, menu);
		return true ;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return applyMenuChoices(item)|| super.onOptionsItemSelected(item);
	}
	private boolean applyMenuChoices(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			Intent aboutIntent=new Intent(this,About.class);
			startActivity(aboutIntent);
			break;
		case R.id.exit:
			Intent exitIntent=new Intent(Intent.ACTION_MAIN);
			exitIntent.addFlags(exitIntent.FLAG_ACTIVITY_CLEAR_TOP);
			exitIntent.addCategory(exitIntent.CATEGORY_HOME);
			startActivity(exitIntent);

		default:
			break;
		}
		return true;
	}

	
}
