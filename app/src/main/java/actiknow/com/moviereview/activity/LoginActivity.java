package actiknow.com.moviereview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import actiknow.com.moviereview.R;
import actiknow.com.moviereview.utils.AppConfigTags;
import actiknow.com.moviereview.utils.UserDetailPref;

public class LoginActivity extends AppCompatActivity {
    UserDetailPref userDetailPref;
    CallbackManager callbackManager;
    LoginButton loginButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        initView();
        initData();

    }

    private void initData() {
        userDetailPref = UserDetailPref.getInstance();

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
                Log.e("loginResult", ""+loginResult);
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("Cance", "Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("ErrorFacebook", ""+exception);
            }
        });
    }

    private void initView() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        Log.e("JsonObj", ""+json_object);
                        try {
                            userDetailPref.putStringPref(LoginActivity.this, UserDetailPref.USER_NAME, json_object.getString(AppConfigTags.NAME));
                            userDetailPref.putStringPref(LoginActivity.this, UserDetailPref.USER_EMAIL, json_object.getString(AppConfigTags.EMAIL));
                            Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                            intent.putExtra("userProfile", json_object.toString());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
