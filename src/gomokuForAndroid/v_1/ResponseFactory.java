package gomokuForAndroid.v_1;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class ResponseFactory extends Activity {
	GetResponseType getResponseType = new GetResponseType();
	String jsonRecieved;
	Context context;
	String state, msg;
	public static boolean wait = false;
	public static Handler handler;
	ChallengeResponse challengeResponse = new ChallengeResponse(
			CustomizedListView.context);

	public ResponseFactory(String rcvd, Context context) {
		this.jsonRecieved = rcvd;
		this.context = context;
	}

	public void doCalledResponse() {
		Log.e("At Gomoku_login: ", "calling the response");
		if (getResponseType.getResponseType(jsonRecieved).equals("SENDPLAYER")) {
			Log.e("SENDPLAYER:", jsonRecieved);

			try {
				if (CustomizedListView.context != null) {
					Bundle bundle = new Bundle();
					Log.e("AT Response handler",
							"getting" + jsonRecieved.toString());
					bundle.putString("players", jsonRecieved.toString());
					Message message = new Message();
					message.setData(bundle);
					CustomizedListView.handler.sendMessage(message);
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					try {
						Log.e("waiting::", "");

						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Bundle bundle = new Bundle();
						Log.e("AT Response handler",
								"getting" + jsonRecieved.toString());
						bundle.putString("players", jsonRecieved.toString());
						Message message = new Message();
						message.setData(bundle);
						CustomizedListView.handler.sendMessage(message);

					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
			};

		} else if (getResponseType.getResponseType(jsonRecieved).equals(
				"CHALLENGE")) {
			Log.e("CHALLENGE:", jsonRecieved);
			// if (GomokuAndroidUI.inGame != true) {

			challengeResponse.process(jsonRecieved);
			// }
		} else if (getResponseType.getResponseType(jsonRecieved)
				.equals("LOGIN")) {
			Log.e("LOGIN:", jsonRecieved);
			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(jsonRecieved);

			// Storing each json item in variable

			try {
				state = json.getString("state");
				Log.e("AT RESPONSEFACTORY: ", state);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!state.equals("OK")) {
				Toast.makeText(context, "error: check password and username",
						Toast.LENGTH_LONG).show();
				Gomoku_login.loginEditText.setText("");
				Gomoku_login.passwordEditText.setText("");
			} else {

				Log.e("At Responsefactorw", "calling for CustomizedListView");
				Intent list = new Intent(context, CustomizedListView.class);
				list.putExtra("json", jsonRecieved);
				context.startActivity(list);

			}

		} else if (getResponseType.getResponseType(jsonRecieved).equals(
				"MAKEMOVE")) {
			Log.e("MAKEMOVE:", jsonRecieved);

			UpdateUI updateUI = new UpdateUI();
			updateUI.update(jsonRecieved);

		} else if (getResponseType.getResponseType(jsonRecieved).equals(
				"GAMEOVER")) {
			Log.e("GAMEOVER:", jsonRecieved);

			GameOverResponse gameOverResponse = new GameOverResponse(
					GomokuAndroidUI.context);
			gameOverResponse.process(jsonRecieved);

		} else if (getResponseType.getResponseType(jsonRecieved).equals(
				"MAKEMOVE")) {
			Log.e("MAKEMOVE:", jsonRecieved);

			UpdateUI updateUI = new UpdateUI();
			updateUI.update(jsonRecieved);

		} else if (getResponseType.getResponseType(jsonRecieved).equals(
				"REGISTERUSER")) {
			Log.e("REGISTERUSER:", jsonRecieved);

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(jsonRecieved);

			// Storing each json item in variable

			try {
				state = json.getString("status");
				msg = json.getString("message");
				Log.e("AT RESPONSEFACTORY: ", state);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!state.equals("OK")) {
				Toast.makeText(context, msg + "", Toast.LENGTH_LONG).show();

			} else {

				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				Log.e("At Responsefactory", "sending back to login screen");
				Intent list = new Intent(context, Gomoku_login.class);
				context.startActivity(list);
				Gomoku_register.handler.sendEmptyMessage(0);
			}

		} else if (getResponseType.getResponseType(jsonRecieved).equals(
				"HEARTBEAT")) {
			Log.e("HEARTBEAT:", jsonRecieved);

			// Gomoku_login.gomokuTcpClient.sendMessage();

		} else {
			Log.e("UNKNOWN RESPONSE FROM SERVER",
					"UNKNOWN RESPONSE FROM SERVER");
		}
	}

}
