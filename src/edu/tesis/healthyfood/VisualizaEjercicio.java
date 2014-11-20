package edu.tesis.healthyfood;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VisualizaEjercicio extends YouTubeFailureRecoveryActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualiza_ejercicio);
		Button b = (Button)this.findViewById(R.id.button1);
		YouTubePlayerView yp = (YouTubePlayerView)this.findViewById(R.id.youtube_view);
		
		yp.initialize(DeveloperKey.DEVELOPER_KEY, this);
		
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				registrar();
			}
		});
		
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		if(!arg2){
			arg1.cueVideo("rRgVtYHvJAE");
		}
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		// TODO Auto-generated method stub
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}
	
	private void registrar(){
		
	}
}
