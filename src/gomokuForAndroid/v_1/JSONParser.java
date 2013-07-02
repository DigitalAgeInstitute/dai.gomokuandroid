package gomokuForAndroid.v_1;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	static JSONObject jObj = null;

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String data) {

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(data);
			Log.e("JSON : got object", "");
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
}
