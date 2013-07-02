package gomokuForAndroid.v_1;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

public class GetResponseType{
	/* private static String url = "http://api.androidhive.info/contacts/"; */

	// JSON Node names
	static String TAG_TYPE = "type";
	static String responseType;

	public String getResponseType(String msg) {
		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(msg);

		try {
			responseType = json.getString(TAG_TYPE).toString();
			Log.e("response type", json.getString(TAG_TYPE).toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseType;
	}
}
