package gomokuForAndroid.v_1;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Gomoku_register extends Activity implements OnClickListener {

	TextView loginScreen;
	EditText reg_password1ET, reg_firstnameET, reg_emailET, reg_mobileET,
			reg_password2ET, reg_usernameET, reg_lastnameET;
	Button reg_button;
	static Handler handler;
	String reg_loginUser, reg_psswrd1, reg_psswrd2, reg_eml, reg_fllnm,
			sentJSON, reg_mbl, reg_fname, reg_lname;

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

			finish();

			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set View to register.xml
		setContentView(R.layout.gomoku_registration);

		// initalize variables and views
		initalizeVariables();
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				finish();
			}
		};

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gomoku_btnRegister:
			Log.e("picked", "");
			
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
			break;
		case R.id.link_to_login:
			// send you back to the log in screen
			Intent i = new Intent(getApplicationContext(), Gomoku_login.class);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}

	private void initalizeVariables() {

		// initialize variable
		loginScreen = (TextView) findViewById(R.id.link_to_login);
		reg_password1ET = (EditText) findViewById(R.id.gomoku_reg_password1);
		reg_firstnameET = (EditText) findViewById(R.id.gomoku_reg_fisrtname);
		reg_lastnameET = (EditText) findViewById(R.id.gomoku_reg_lastname);
		reg_emailET = (EditText) findViewById(R.id.gomoku_reg_email);
		reg_mobileET = (EditText) findViewById(R.id.gomoku_reg_mobile);
		reg_password2ET = (EditText) findViewById(R.id.gomoku_reg_password2);
		reg_usernameET = (EditText) findViewById(R.id.gomoku_reg_username);
		reg_button = (Button) findViewById(R.id.gomoku_btnRegister);

		// set listeners
		reg_button.setOnClickListener(this);
		loginScreen.setOnClickListener(this);

	}

	private void checkFieldAndSendMsg() {
		// check if there is input
		try {

			// reset errors
			loginScreen.setError(null);
			reg_password1ET.setError(null);
			reg_firstnameET.setError(null);
			reg_lastnameET.setError(null);
			reg_emailET.setError(null);
			reg_mobileET.setError(null);
			reg_password2ET.setError(null);
			reg_usernameET.setError(null);

			if (TextUtils.isEmpty(reg_fname)) {
				reg_firstnameET
						.setError(getString(R.string.error_field_required));
			} else if (TextUtils.isEmpty(reg_lname)) {
				reg_lastnameET
						.setError(getString(R.string.error_field_required));
			} else if (TextUtils.isEmpty(reg_eml)) {
				reg_emailET.setError(getString(R.string.error_field_required));
			} else if (TextUtils.isEmpty(reg_loginUser)) {
				reg_usernameET
						.setError(getString(R.string.error_field_required));
			} else if (TextUtils.isEmpty(reg_eml)) {
				reg_emailET.setError(getString(R.string.error_field_required));
			} else if (TextUtils.isEmpty(reg_psswrd1)) {
				reg_password1ET
						.setError(getString(R.string.error_field_required));
			} else if (TextUtils.isEmpty(reg_psswrd2)) {
				reg_password2ET
						.setError(getString(R.string.error_field_required));
			} else if (!reg_psswrd1.equals(reg_psswrd2)) {
				reg_password1ET
						.setError(getString(R.string.error_incorrect_password));
			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						Gomoku_login.gomokuTcpClient
								.sendMessage(turnObjectToJSON(reg_loginUser,
										reg_psswrd1, reg_eml, reg_mbl,
										reg_fname, reg_lname));
					}
				});
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private String turnObjectToJSON(String loginUser2, String password1,
			String email, String mobile, String fname, String lname) {
		//
		Log.e("user", loginUser2);
		Log.e("pass", password1);
		Log.e("mobile", mobile);
		Log.e("firstname", fname);
		Log.e("lastname", lname);
		Log.e("email", email);
		sentJSON = String
				.format("{ \"type\":\"REGISTERUSER\", \"firstName\":\"%s\", \"lastName\":\"%s\", \"username\":\"%s\", \"email\":\"%s\", \"contacts\":\"%s\", \"password\":\"%s\"}",
						fname, lname, loginUser2, email, mobile, password1);
		return sentJSON;

	}

	private void getValues() {
		// login name and password that are passed are assigned to strings
		reg_loginUser = reg_usernameET.getText().toString();
		reg_psswrd1 = reg_password1ET.getText().toString();
		reg_psswrd2 = reg_password2ET.getText().toString();
		reg_eml = reg_emailET.getText().toString();
		reg_fname = reg_firstnameET.getText().toString();
		reg_lname = reg_lastnameET.getText().toString();
		reg_mbl = reg_mobileET.getText().toString();

	}

}
