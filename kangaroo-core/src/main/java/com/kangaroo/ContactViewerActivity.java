package com.kangaroo;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.kangaroo.rest.ContactSenderActivity;
import com.kangaroo.util.About;
import com.kangaroo.util.AppConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ContactViewerActivity extends ListActivity{
    String name;
    String number;

    Map<String, String> contacts=new HashMap<String,String>();
    ArrayList<HashMap<String, String>> contactsList=new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_viewer_layout);
        getContactsFromPhone();
    }

    private void getContactsFromPhone(){
        ContentResolver resolver=getContentResolver();
        Uri uri=ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor=resolver.query(uri, null, null, null, null);

        while(cursor.moveToNext())
        {
            String contactId=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            String hasPhone=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if(Integer.parseInt(hasPhone)>0)
            {
                Uri phones=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                Cursor c = getContentResolver().query(phones,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contactId, null,null);
                while(c.moveToNext())
                {   number=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    int type=c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    switch (type)
                    {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            contacts.put(name, number);
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            break;
                        default:
                            break;
                    }
                }
                c.close();
            }
        }
        cursor.close();
        renderListView(contacts,contactsList);
    }

    private void renderListView(Map<String, String> contacts,
                                ArrayList<HashMap<String, String>> contactsList) {
        Set<String> set=contacts.keySet();
        Iterator<String> it=set.iterator();
        while (it.hasNext()) {
            name=it.next();
            number=contacts.get(name);
            HashMap<String, String> map=new HashMap<String,String>();
            map.put(AppConstant.CONTACT_NAME,name);
            map.put(AppConstant.CONTACT_NUMBER, number);
            contactsList.add(map);
        }
        ListAdapter adapter = new SimpleAdapter(
                ContactViewerActivity.this,contactsList,
                R.layout.contact_viewer_list_layout, new String[] {AppConstant.CONTACT_NAME,
                AppConstant.CONTACT_NUMBER}, new int[] {
                R.id.contactName, R.id.contactNumber,});

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String obj1=contactsList.get(position).get(AppConstant.CONTACT_NAME);
        String obj2=contactsList.get(position).get(AppConstant.CONTACT_NUMBER);
        System.getProperties().put(AppConstant.CONTACT_NAME,obj1);
        System.getProperties().put(AppConstant.CONTACT_NUMBER, obj2);
        Intent sender = new Intent(this,ContactSenderActivity.class);
        startActivity(sender);
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