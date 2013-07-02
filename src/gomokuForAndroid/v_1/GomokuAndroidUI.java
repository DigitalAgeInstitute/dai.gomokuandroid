package gomokuForAndroid.v_1;

import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class GomokuAndroidUI extends Activity {
	static ArrayList<TextView> arrTextViews;
	int[][] coordinates = new int[15][15];
	boolean player1 = true;
	String sentJSON;
	static Handler handler;
	static LinearLayout layout;
	static boolean inGame = false;
	static Context context;
	public static boolean turn = false;
	static TextView me,op;
	TextView vs;

	
	
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
			.setTitle("QUIT GAME")
			.setMessage("ARE SURE YOU WANT \nTO QIUT THIS GAME")
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
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//
		me = new TextView(this);
		op = new TextView(this);
		me.setGravity(Gravity.LEFT);
		op.setGravity(Gravity.RIGHT);
		me.setCompoundDrawablesWithIntrinsicBounds(R.drawable.player_white, 0, 0, 0);
		
		op.setCompoundDrawablesWithIntrinsicBounds(R.drawable.player_white, 0, 0, 0);
		vs = new TextView(this);
		me.setText("me");
		op.setText((ChallengeResponse.challengerUsername
				.equals(Gomoku_login.loginUser)) ? ChallengeResponse.challengeeUsername
				: ChallengeResponse.challengerUsername);
		
		me.setTextSize(15);
		me.setPadding(5, 3, 3, 3);
		vs.setPadding(20, 3, 20, 3);
		op.setTextSize(15);
		op.setPadding(3, 3, 5, 3);
		
		// below a layout is created and view added to it
		LinearLayout currentGame = new LinearLayout(this);
		currentGame.setOrientation(LinearLayout.HORIZONTAL);
		currentGame.setBackgroundColor(Color.WHITE);
		currentGame.addView(me);
		currentGame.setPadding(2, 2, 2, 2);
		currentGame.addView(vs);
		currentGame.addView(op);
		currentGame.setBackgroundColor(Color.LTGRAY);
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				finish();
			}
		};
		
		this.context = GomokuAndroidUI.this;
		layout = new LinearLayout(this);

		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(mainParams);
		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
		LinearLayout.LayoutParams colParams = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
		
		layout.addView(currentGame);
		int length = 225;
		int index = 0;
		int no_of_column = 15;
		int num_of_row = length / no_of_column;

		arrTextViews = new ArrayList<TextView>(length);

		for (int i = 0; i < length; i++) {
			final TextView textView = new TextView(this);
			textView.setLayoutParams(colParams);
			textView.setId(i);
			textView.setText("");
			textView.setGravity(Gravity.CENTER);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

			if ((i % 2) == 0) {
				textView.setBackgroundColor(Color.rgb(166, 172, 169));
			} else {
				textView.setBackgroundColor(Color.rgb(227, 233, 227));
			}

			textView.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					final TextView tView = (TextView) v;
					if (tView.getText().equals("")) {
						
							Gomoku_login.gomokuTcpClient.sendMessage(String
									.format("{ \"type\":\"MAKEMOVE\", \"username\":\"%s\", \"gameID\":\"%s\", \"row\":%d, \"col\":%d }",
											Gomoku_login.loginUser,
											ChallengeResponse.gameId,
											(tView.getId() / 15),
											(tView.getId() % 15)));
							
						
					} else {
						Toast.makeText(
								getApplication(),
								((tView.getText()) == "O" ? "player O cant play here"
										: "player X cant play here"),
								Toast.LENGTH_SHORT).show();
					}

					
					v = tView;
				}

			});
			arrTextViews.add(textView);

		}

		for (int i = 0; i < num_of_row; i++) {
			LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(rowParams);
			row.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < no_of_column; j++) {
				row.addView(arrTextViews.get(index));
				index++;
			}
			layout.addView(row);
		}
		
		setContentView(layout);
		
		if(ChallengeResponse.challengerUsername.equals(Gomoku_login.loginUser)){
			GomokuAndroidUI.me.setCompoundDrawablesWithIntrinsicBounds(R.drawable.player_black, 0, 0, 0);;
		}else {
			GomokuAndroidUI.op.setCompoundDrawablesWithIntrinsicBounds(R.drawable.player_white, 0, 0, 0);
		}
	}

}
