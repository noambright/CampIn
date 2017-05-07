package com.example.noam.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

   /* private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ImageButton profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.main_activity);

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                String email = "";
                                String birthday = "";
                                String userId = "";
                                // Application code
                                try {
                                    email = object.getString("email");
                                    birthday = object.getString("birthday");
                                    userId = object.getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Bitmap bitmap = getFacebookProfilePicture(userId);
                                profilePicture = (ImageButton)findViewById(R.id.imageButton);
                                profilePicture.setImageBitmap(bitmap);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   try {

                       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                               .setAction("Action", null).show();
                   }
                   catch (Exception e)
                   {
                       e.printStackTrace();
                   }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap getFacebookProfilePicture(String userID){
        Bitmap bitmap = null;

        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
           // URL imageURL = new URL("data:image/jpeg;base64,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");

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

           // bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }*/


}
