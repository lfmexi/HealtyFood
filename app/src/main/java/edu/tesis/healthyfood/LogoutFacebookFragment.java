package edu.tesis.healthyfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import edu.tesis.healthyfood.genericTasks.GenericCheckSession;

/**
 * Created by luis on 2/04/15.
 */
public class LogoutFacebookFragment extends DialogFragment {

    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    AccessTokenTracker accessTokenTracker;

    public static LogoutFacebookFragment newInstance(){
        LogoutFacebookFragment fragment=new LogoutFacebookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                changeProfile(currentProfile);
            }
        };
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Profile.fetchProfileForCurrentAccessToken();
            }
        };

        Profile.fetchProfileForCurrentAccessToken();
        changeProfile(Profile.getCurrentProfile());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstance){
        View v = inflater.inflate(R.layout.fragment_social_login,container,false);
        getDialog().setTitle("Inicio con redes sociales");
        LoginButton loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile","email");

        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                onSuccesLogin(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
                onCancelFb();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                onCancelFb();
                Log.i("Exception:", exception.toString());
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onCancelFb(){
        Toast.makeText(this.getActivity(), "Fallo", Toast.LENGTH_LONG);
        Log.i("error","error de login de facebook");
    }
    private void onSuccesLogin(LoginResult loginResult){
        Log.i("UserID",loginResult.getAccessToken().getUserId());
    }

    private void changeProfile(Profile profile){
        if(profile==null){
            if(this.getDialog()!=null){
                profileTracker.stopTracking();
                this.dismiss();
                GenericCheckSession.Logout(this.getActivity());
            }
        }
    }

}
