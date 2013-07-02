package gomokuForAndroid.v_1;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter implements Filterable {

	private Activity activity;
	// private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	private ArrayList<HashMap<String, String>> originalData;
	private ArrayList<HashMap<String, String>> filteredData;
	String plyrID, uname, fname;
	Context c;
	int p;

	public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		originalData = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		filteredData = d;

	}

	public int getCount() {
		return filteredData.size();
	}

	public Object getItem(int position) {
		return filteredData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		this.p = position;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row, null);

		final Button challengebtn = (Button) vi.findViewById(R.id.button1);
		challengebtn.setTag(position);
		challengebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				view.getTag();
				Integer value = (Integer) view.getTag();
				final String[] str = filteredData.get(value).toString()
						.split(",");

				Gomoku_login.gomokuTcpClient.sendMessage(String
						.format("{ \"type\":\"CHALLENGE\", \"challengerUsername\":\"%s\", \"challengeeUsername\":\"%s\", \"message\":\"MAKE\" }",
								Gomoku_login.loginUser,
								str[2].substring(10, str[2].length())));
				Log.e("Challenger ", Gomoku_login.loginUser);
				Log.e("challengee ", str[2].substring(10, str[2].length()));
			}
		});

		TextView playerUName = (TextView) vi.findViewById(R.id.title); // title
		TextView fullNm = (TextView) vi.findViewById(R.id.full_name); // artist
																		// name
		TextView plyrID = (TextView) vi.findViewById(R.id.playerID); // duration

		HashMap<String, String> player = new HashMap<String, String>();
		player = filteredData.get(position);

		// Setting all values in listview
		plyrID.setText("id: "
				+ player.get(CustomizedListView.TAG_PLAYER_ID));
		playerUName.setText(player.get(CustomizedListView.TAG_USER_NAME));
		fullNm.setText(player.get(CustomizedListView.TAG_FIRST_NAME)
				+ " " + player.get(CustomizedListView.TAG_LAST_NAME));
		return vi;
	}

	@Override
	public Filter getFilter() {

		return new Filter() {

			@Override
			protected void publishResults(CharSequence arg0,
					FilterResults filterResults) {
				filteredData = (ArrayList<HashMap<String, String>>) filterResults.values;
				notifyDataSetChanged();

			}

			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				FilterResults results = new FilterResults();

				// If there's nothing to filter on, return the original data for
				// your list
				if (charSequence == null || charSequence.length() == 0) {
					results.values = originalData;
					results.count = originalData.size();
				} else {
					ArrayList<HashMap<String, String>> filterResultsData = new ArrayList<HashMap<String, String>>();

					for (HashMap<String, String> data : originalData) {
						// In this loop, you'll filter through originalData and
						// compare each item to charSequence.
						// If you find a match, add it to your new ArrayList
						// I'm not sure how you're going to do comparison, so
						// you'll need to fill out this conditional
						String user_name_str = data.get("userName").toString()
								+ " " + data.get("firstName").toString() + " "
								+ data.get("lastName").toString();
						if (user_name_str.contains(charSequence)) {
							Log.e("At LazyAdapter",
									"searching for " + data.get("userName")
											+ " ...successful ");
							filterResultsData.add(data);
						}
					}

					results.values = filterResultsData;
					results.count = filterResultsData.size();
				}

				return results;
			}
		};
	}

}