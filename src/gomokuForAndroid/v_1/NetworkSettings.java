package gomokuForAndroid.v_1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NetworkSettings {
	
	// this are some of the variables used in the dialogs created below
		public static EditText ipEditText;
		public static EditText portEditText;
		TextView ipTextView;
		TextView portTextView;
	
	// below dialog is for the setting button
		public Dialog networkGameSettingsDialog(final Context context) {

			// below input fields and text fields are initialised
			ipEditText = new EditText(context);
			portEditText = new EditText(context);
			ipTextView = new TextView(context);
			portTextView = new TextView(context);

			// below set the text fields with a message
			ipTextView.setText("ip address");
			portTextView.setText("port number");

			// below a layout is created and view added to it
			LinearLayout networkSettingsLayout = new LinearLayout(context);
			networkSettingsLayout.setOrientation(LinearLayout.VERTICAL);
			networkSettingsLayout.addView(ipTextView);
			networkSettingsLayout.addView(ipEditText);
			ipEditText.setText(ClientThread.SERVER_IP.toString());
			networkSettingsLayout.addView(portTextView);
			networkSettingsLayout.addView(portEditText);
			portEditText.setText(ClientThread.SERVER_PORT + "");
			
			// create an instance of a dialog box
			final AlertDialog dialog;
			dialog = new AlertDialog.Builder(context)
					.setTitle("Network Settings")
					.setIcon(R.drawable.cloud)
					.setView(networkSettingsLayout)
					.setPositiveButton("CONNECT",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

								}
							})
					.setNegativeButton("CANCEL",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

								}
							}).create();
			dialog.show();

			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (TextUtils.isEmpty(ipEditText.getText().toString())) {
								ipEditText.setError("empty field");
								Log.e("ControlHander", "empty ip address");

							} else if (TextUtils.isEmpty(portEditText.getText()
									.toString())) {
								portEditText.setError("empty field");

								Log.e("ControlHander", "empty port number");

							} else if (GomokuNetworkSettingsHandler
									.isValidInetAddress(ipEditText.getText()
											.toString()) == false) {
								ipEditText.setError("invalid ip address");
								Log.e("ControlHander", "invalid ip address");

							} else {

								// get the values passed in the EditText and
								// assign the to the variables ip and port
								dialog.dismiss();
								String ip = ipEditText.getText().toString();
								int port = Integer.parseInt(portEditText.getText()
										.toString());

								GomokuNetworkSettingsHandler gomokuNetworkSettingsHandler = new GomokuNetworkSettingsHandler(
										context);
								gomokuNetworkSettingsHandler.setIp_Port(ip, port);
								Log.e("ControlHander", "valid ip address");
								Log.e("At GomokuDialogs", "ip_address = " + ip);
								Log.e("At GomokuDialogs", "port = " + port);

								//create or start a connection to the server
								Gomoku_login.connect.sendEmptyMessage(0);
							}
						}
					});

			return dialog;
		}
	
}
