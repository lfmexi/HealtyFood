package edu.tesis.healthyfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class VisualizaEjercicio extends YouTubeFailureRecoveryActivity {

	String user;
	private String ejercicio;
	private String objetivo;
	private String idVideo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualiza_ejercicio);
		YouTubePlayerView yp = (YouTubePlayerView)this.findViewById(R.id.youtube_view);
		TextView tEjercicio = (TextView)this.findViewById(R.id.textNombreEjercicio);
		TextView tObjetivo = (TextView)this.findViewById(R.id.textObjetivo);
		
		Intent i = this.getIntent();
		user = i.getExtras().getString("infoUser");
		ejercicio = i.getExtras().getString("ejercicio");
		objetivo = i.getExtras().getString("objetivo");
		idVideo = i.getExtras().getString("idVideo");
		
		tEjercicio.setText(ejercicio);
		tObjetivo.setText(objetivo);
		
		yp.initialize(DeveloperKey.DEVELOPER_KEY, this);
			
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		if(!arg2){
			arg1.cueVideo(idVideo);
		}
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		// TODO Auto-generated method stub
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}
	
}
