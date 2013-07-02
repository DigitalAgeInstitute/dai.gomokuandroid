package gomokuForAndroid.v_1;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateUI {

	static final String TAG_TYPE = "MAKEMOVE";
	static final String TAG_STATUS = "status";
	static final String TAG_ROW = "row";
	static final String TAG_COL = "col";
	static final String TAG_GAMEID = "gameID";
	String row, col, status;
	String gameId, sentJSON;
	String user_name;
	Handler handler = new Handler();
	static boolean ifcanPlay = false;
	static String play = "O";

	// contacts JSONArray
	JSONArray jsonArr = new JSONArray();

	public void update(String msg) {
		updateUIOnThisSide(msg);
	}

	public ArrayList<TextView> updateUIOnThisSide(String msg) {

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(msg);
		try {

			// Storing each json item in variable
			row = json.getString(TAG_ROW);
			Log.e("row is : ", row);
			col = json.getString(TAG_COL);
			Log.e("row is : ", col);
			status = json.getString(TAG_STATUS);
			gameId = json.getString(TAG_GAMEID);
			user_name = json.getString("username");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		int rw, cl;
		rw = Integer.parseInt(row);
		cl = Integer.parseInt(col);

		if (Gomoku_login.loginUser.equals(user_name)) {
			play = "O";
			GomokuAndroidUI.me.setCompoundDrawablesWithIntrinsicBounds(R.drawable.player_white, 0, 0, 0);;
			GomokuAndroidUI.op.setCompoundDrawablesWithIntrinsicBounds(R.drawable.player_black, 0, 0, 0);;
		} else {
			play = "X";
			GomokuAndroidUI.me.setCompoundDrawablesWithIntrinsicBounds(R.drawable.player_black, 0, 0, 0);;
			GomokuAndroidUI.op.setCompoundDrawablesWithIntrinsicBounds(R.drawable.player_white, 0, 0, 0);
		}

		Log.e("AT UPDATEUI: ", "updating screen: row:" + rw + " col: " + cl);
		int sum = (rw * 15) + cl;
		Log.e("AT UPDATEUI: ", "id: " + sum);

		if (status.equals("FAIL") && user_name.equals(Gomoku_login.loginUser)) {
			Toast.makeText(GomokuAndroidUI.context, "NOT YOUR TURN",
					Toast.LENGTH_LONG).show();
		}else if (status.equals("FAIL")) {
			Log.e("AT UPDATEUI: ", "WRONG TURN");
		} else {
			GomokuAndroidUI.arrTextViews.get((rw * 15) + cl).setText(play);
			
		}

		return GomokuAndroidUI.arrTextViews;

	}
}
