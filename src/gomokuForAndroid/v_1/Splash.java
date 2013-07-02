package gomokuForAndroid.v_1;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity{
	MediaPlayer oursong = new MediaPlayer();

	@Override
	protected void onCreate(Bundle splashing) {
		// TODO Auto-generated method stub
		super.onCreate(splashing);
		setContentView(R.layout.splash);
		
		oursong = MediaPlayer.create(Splash.this, R.raw.pirate);
		oursong.setLooping(true);
		oursong.start();
		
		Thread thread1 = new Thread(){
		public void run(){
			try{
				sleep(3300);
				
			}catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				Intent openStartingPoint = new Intent(Splash.this,Gomoku_login.class);
				startActivity(openStartingPoint);
			}
		}
		};
		thread1.start();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
		oursong.stop();
	}
	
}