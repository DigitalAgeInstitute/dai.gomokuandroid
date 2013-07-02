package gomokuForAndroid.v_1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class CustomizedListView extends Activity {

	// JSON Node names
	static final String TAG_PLAYER = "players";
	static final String TAG_PLAYER_ID = "playerID";
	static final String TAG_USER_NAME = "userName";
	static final String TAG_FIRST_NAME = "firstName";
	static final String TAG_LAST_NAME = "lastName";

	ListView list;
	LazyAdapter adapter;
	// contacts JSONArray
	JSONArray player = new JSONArray();
	String msg;
	static Context context;
	int current_page = 1;;
	ProgressDialog pDialog;
	static Handler handler;
	TextView search;
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// this listens to the menu clicked on the action bar

		switch (item.getItemId()) {
		case R.id.gomoku_settings:

			// this method prompts for user to enter the ip and port numbers
			GomokuNetworkSettingsHandler controlHandler = new GomokuNetworkSettingsHandler(
					this);
			controlHandler.promptForSettingsInput();
			break;
		case R.id.exit:
			
			
			new AlertDialog.Builder(this)
			.setTitle("LOGOUT")
			.setMessage("DO YOU WANT TO LOGOUT")
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							
							try {
								ClientThread.socket.close();
							} catch (IOException e) {
								e.printStackTrace();
							} catch(NullPointerException e){
								e.printStackTrace();
							}
							Gomoku_login.connect.sendEmptyMessage(0);
							Gomoku_login.loginEditText.setText("");
							Gomoku_login.passwordEditText.setText("");
							finish();
						}
					})
			.setNegativeButton("CANCEL",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					}).show();
			
			
			break;

		default:
			break;
		}

		return true;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		this.context = CustomizedListView.this;
		ResponseFactory.handler.sendEmptyMessage(0);
		//set the context in a class that can be accessed by any other
		//PlayerData.context = Gomoku_PlayerList_Screen.this;
		
		handler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				String players = bundle.getString("players");
				addPlayers(players);
			};
		};
		
		
		
		search = (TextView) findViewById(R.id.inputSearch);
		search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					CustomizedListView.this.adapter.getFilter().filter(arg0);

			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	public void addPlayers(String msg) {
		// increment current page
		current_page += 1;

		// Hashmap for ListView
		ArrayList<HashMap<String, String>> playerList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();
		Log.e("customlist view:", msg);

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(msg);

		try {

			// Getting Array of Contacts
			player = json.getJSONArray(TAG_PLAYER);

			// looping through All Contacts
			for (int i = 0; i < player.length(); i++) {
				JSONObject c = player.getJSONObject(i);

				// Storing each json item in variable
				String id = c.getString(TAG_PLAYER_ID);
				String user_name = c.getString(TAG_USER_NAME);
				String first_name = c.getString(TAG_FIRST_NAME);
				String last_name = c.getString(TAG_LAST_NAME);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				if (!(user_name.equals(Gomoku_login.loginUser))) {
					map.put(TAG_PLAYER_ID, id);
					map.put(TAG_USER_NAME, user_name);
					map.put(TAG_FIRST_NAME, first_name);
					map.put(TAG_LAST_NAME, last_name);

					// adding HashList to ArrayList
					playerList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		list = (ListView) findViewById(R.id.list);

		// Getting adapter by passing xml data ArrayList
		adapter = new LazyAdapter(CustomizedListView.this, playerList);
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
	}

}