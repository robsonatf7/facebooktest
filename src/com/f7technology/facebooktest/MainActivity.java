package com.f7technology.facebooktest;

import java.util.Arrays;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	private String TAG = "MainActivity";
	private TextView lblEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lblEmail = (TextView) findViewById(R.id.lblEmail);
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public void onError(FacebookException error) {
				Log.i(TAG, "Error " + error.getMessage());
			}
			
		});

		authButton.setReadPermissions(Arrays.asList("public_profile", "email"));
		authButton.setSessionStatusCallback (new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				
				if (session.isOpened()) {
					Log.i(TAG, "Access Token"+session.getAccessToken());
					Request.newMeRequest(session, new Request.GraphUserCallback() {
						
						@Override
						public void onCompleted(GraphUser user, Response response) {
							
							if (user != null) {
								Log.i(TAG, "User ID "+ user.getId());
								Log.i(TAG, "Email "+user.asMap().get("email"));
								lblEmail.setText(user.asMap().get("email").toString());;
							}
							
						}
					});
				}
			}
		});
	}

	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
