package gomokuForAndroid.v_1;

//make import of classes to be used
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/*
 * this is the class the run every thing that happen on the login screen that is
 * input user data,link to registration page
 * 
 */
public class Gomoku_login extends Activity implements OnClickListener {

	// ---create instances---
	TextView registerScreen;
	static EditText loginEditText, passwordEditText;
	Button signInButton;
	CheckBox chckbx;
	public static String password, loginUser;
	String sentJSON;
	static ClientThread gomokuTcpClient;
	Handler handler;
	GetResponseType getResponseType = new GetResponseType();
	static Handler connect;
	
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
			.setTitle("QUIT")
			.setMessage("DO YOU WANT TO QUIT")
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
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
	
	// ---call the onCreate() and set its contentView to the gomoku_login xml
	// layout---
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ---setting default screen to login.xml---
		setContentView(R.layout.gomoku_login);

		// ---Initialises variables and views from the method---
		initalizeVariables();
		
		// ---create a handler to perform a task when an alert is called by sending
		// an empty message---
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.e("At Gomoku_login",
						"handler called: calling for response factory");
				
				// --- get message from bundler passed through the handler---
				Bundle bundle = msg.getData();
				String responseFromServer = bundle.getString("responseFromServer");
				
				// ---call for the response factory with the message from server---
				ResponseFactory responseFactory = new ResponseFactory(
						responseFromServer, Gomoku_login.this);
				responseFactory.doCalledResponse();
			}
		};

		connect = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				connectToServer();
			}
		};

	}

	// ---below method creates a connection to the server---
	public void connectToServer() {
		
		// ---initialise the ClientThread with the above handler and context of
		// this state---
		
		gomokuTcpClient = new ClientThread(handler, this);
		gomokuTcpClient.start();

	}

	// ---this is the method that initialises variables---
	private void initalizeVariables() {
		// TODO Auto-generated method stub
		registerScreen = (TextView) findViewById(R.id.link_to_register);
		loginEditText = (EditText) findViewById(R.id.loginNameGomokuET);
		passwordEditText = (EditText) findViewById(R.id.passwordGomokuET);
		signInButton = (Button) findViewById(R.id.signinGomokuBtn);
		chckbx = (CheckBox) findViewById(R.id.gomokucheckBox1);

		// ---make the below view listeners---
		registerScreen.setOnClickListener(this);
		signInButton.setOnClickListener(this);
		chckbx.setOnClickListener(this);

	}

	// ---create an onClick listener to listen to the item click on the screen---
	@Override
	public void onClick(View v) {

		// ---create a switch statement to handle multiple click---
		switch (v.getId()) {
		case R.id.gomokucheckBox1:

			// ---below sets the password visible to player with a toggle button---
			if (chckbx.isChecked()) {
				// ---this will set the password visible---
				passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			} else {
				// ---this will set the password to be hidden---
				passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;
		case R.id.link_to_register:
			// ---Switching to Register screen---
			Intent i = new Intent(Gomoku_login.this,
					Gomoku_register.class);
			startActivity(i);
			finish();
			break;
		case R.id.signinGomokuBtn:
			
			try{
				if(ClientThread.socket.isConnected()){
					// send sign in data to server
					getValues();

					// checks if input is correct
					checkFieldAndSendMsg();
				} else {
					// this method prompts for user to enter the ip and port numbers
					GomokuNetworkSettingsHandler controlHandler = new GomokuNetworkSettingsHandler(
							this);
					controlHandler.promptForSettingsInput();
				}
				} catch(NullPointerException e) {
					GomokuNetworkSettingsHandler controlHandler = new GomokuNetworkSettingsHandler(
							this);
					controlHandler.promptForSettingsInput();
					e.printStackTrace();
				}

		default:
			break;
		}
	}

	// ---this method will check if user has entered any data in the fields---
	private void checkFieldAndSendMsg() {

		// ---check if there is input---
		try {
			// ---resets error to null---
			loginEditText.setError(null);
			passwordEditText.setError(null);

			// ---checks for valid password.---
			if (TextUtils.isEmpty(loginUser)) {
				loginEditText
						.setError(getString(R.string.error_field_required));
			} else if (TextUtils.isEmpty(password)) {
				passwordEditText
						.setError(getString(R.string.error_field_required));
			} else {
				// ---if data has been entered now send the data to the server---
				gomokuTcpClient.sendMessage(turnObjectToJSON(loginUser,
						password));
			}

		} catch (Exception e) {
			Log.e("At Gomoku_login",
					" error while setting errors at loginEditText & paswordEditText to null");
		}

	}

	// ---this method will get data from the fieldv
	private void getValues() {
		// ---login name and password that are passed are assigned to strings---
		loginUser = loginEditText.getText().toString();
		password = passwordEditText.getText().toString();

	}

	// ---this method will turn the data into JSON in the required protocol---
	private String turnObjectToJSON(String loginUser2, String password2) {
		//
		Log.e("user", loginUser2);
		Log.e("pass", password2);
		sentJSON = String
				.format("{ \"type\":\"LOGIN\", \"username\":\"%s\", \"password\":\"%s\" }",
						loginUser2, password2);
		return sentJSON;

	}

}
