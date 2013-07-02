package gomokuForAndroid.v_1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameOverResponse extends Activity {

	static final String TAG_TYPE = "GAMEOVER";
	static final String TAG_WINNER = "winner";
	static final String TAG_GAMEID = "gameID";
	String winner;
	String gameId, sentJSON;
	Handler handler;
	Context cntx;
	AlertDialog dialog;
	JSONObject json;
	TextView countDwn;
	int counter=2;

	public GameOverResponse(Context cntx) {
		this.cntx = cntx;
	}

	public void process(String msg) {
		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		json = jParser.getJSONFromUrl(msg);
		try {
			// Storing each json item in variable
			winner = json.getString("winner");
		} catch (JSONException e) {
			e.printStackTrace();

		}
		Log.e("AT GAMEOVER: ", "gameover is being called");
		gameIsOver();

	}

	public void gameIsOver() {

		TextView winPlyr = new TextView(cntx);
		countDwn = new TextView(cntx);
		LinearLayout linearLayout = new LinearLayout(cntx);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		
		winPlyr.setText(winner + " has won");
		winPlyr.setTypeface(null,Typeface.BOLD);
		winPlyr.setTextSize(25);
		winPlyr.setGravity(Gravity.CENTER_HORIZONTAL);
		winPlyr.setTextColor(Color.GRAY);
		
		countDwn.setText("5");
		countDwn.setTypeface(null,Typeface.BOLD);
		countDwn.setTextSize(60);
		countDwn.setGravity(Gravity.CENTER_HORIZONTAL);
		countDwn.setTextColor(Color.RED);
		
		linearLayout.addView(winPlyr);
		linearLayout.addView(countDwn);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				countDwn.setText(counter+"");
				
				if(counter==0){
					dialog.dismiss();
					GomokuAndroidUI.handler.sendEmptyMessage(0);
				}
			}
		};
		
		
		dialog = new AlertDialog.Builder(cntx)
				.setTitle("     GAMEOVER")
				.setIcon(R.drawable.trophy)
				.setView(linearLayout)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						GomokuAndroidUI.handler.sendEmptyMessage(0);

					}
				})
				.setNegativeButton("RETRY",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									Gomoku_login.gomokuTcpClient.sendMessage(String
											.format("{ \"type\":\"CHALLENGE\", \"challengerUsername\":\"%s\", \"challengeeUsername\":\"%s\", \"message\":\"MAKE\"}",
													Gomoku_login.loginUser,
													(ChallengeResponse.challengerUsername
															.equals(Gomoku_login.loginUser)) ? ChallengeResponse.challengeeUsername
															: ChallengeResponse.challengerUsername));
									GomokuAndroidUI.handler.sendEmptyMessage(0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).show();
		
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 5; i >=0; i--) {
					counter = i;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(0);
				}
				
			}
		});
		executorService.shutdown();
	}
}
