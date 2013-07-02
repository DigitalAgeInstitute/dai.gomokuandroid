package gomokuForAndroid.v_1;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ChallengeResponse extends Activity {

	static final String TAG_CHALLENGERUSERNAME = "challengerUsername";
	static final String TAG_CHALLENGEEUSERNAME = "challengeeUsername";
	static final String TAG_TYPE = "CHALLENGE";
	static final String TAG_MESSAGE = "message";
	static final String TAG_GAMEID = "gameID";
	static String challengeeUsername;
	static String challengerUsername;
	String message;
	static String gameId;
	String sentJSON;
	Handler handler = new Handler();
	Context context;

	public ChallengeResponse(Context cntx) {
		this.context = cntx;
	}

	JSONObject json;

	public void process(String msg) {
		message = msg;

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		json = jParser.getJSONFromUrl(msg);
		try {

			// Getting Array of Contacts

			// Storing each json item in variable
			challengeeUsername = json.getString("challengeeUsername");
			challengerUsername = json.getString("challengerUsername");
			message = json.getString("message");
			gameId = json.getString("gameID");
			Log.e("getting gameID", "gameID==>>" + gameId);
			Log.e("message", message);

		} catch (JSONException e) {
			e.printStackTrace();

		}
		if (message.equalsIgnoreCase("MAKE")) {
			Log.e("At ChallengeResponse ", msg);
			promptForChallenge();
			GomokuAndroidUI.inGame = !GomokuAndroidUI.inGame;

		} else if (message.equalsIgnoreCase("ACCEPT")) {
			Log.e("At ChallengeResponse ", msg);
			if (challengerUsername.equals(Gomoku_login.loginUser)) {
				Toast.makeText(
						context,
						challengeeUsername
										+ " accepted challenge",
						Toast.LENGTH_LONG).show();
				
				
				
			}
			Intent UI = new Intent(context, GomokuAndroidUI.class);
			context.startActivity(UI);

		} else if (message.equalsIgnoreCase("REJECT")) {
			Log.e("At ChallengeResponse ", msg);
			new AlertDialog.Builder(context)
					.setTitle("MATCH REQUEST")
					.setMessage(challengeeUsername.toUpperCase() + " CURRENTLY IS BUSY")
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							}).show();
		}
	}

	public void promptForChallenge() {
		Log.e("At ChallengeResponse ", message);
		Log.e("promtForChallenge is called", "prompt for challenge");

		new AlertDialog.Builder(context)
				.setTitle("MATCH REQUEST")
				.setIcon(R.drawable.challenge)
				.setMessage(challengerUsername + " requests for a match")
				.setPositiveButton("PLAY",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Gomoku_login.gomokuTcpClient.sendMessage(String
										.format("{ \"type\":\"CHALLENGE\", \"challengerUsername\":\"%s\", \"challengeeUsername\":\"%s\", \"message\":\"ACCEPT\" }",
												challengerUsername,
												Gomoku_login.loginUser));

							}
						})
				.setNegativeButton("CANCEL",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Gomoku_login.gomokuTcpClient.sendMessage(String
										.format("{ \"type\":\"CHALLENGE\", \"challengerUsername\":\"%s\", \"challengeeUsername\":\"%s\", \"message\":\"REJECT\" }",
												challengerUsername,
												Gomoku_login.loginUser));
							}
						}).show();
	}
}
