package com.example.bookclub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnSignIn;
    private Button mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();
        btnLogin = findViewById(R.id.btnLogin);
        mEtUsername = findViewById(R.id.etUsername);
        mEtPassword = findViewById(R.id.etPassword);
        mBtnSignIn = findViewById(R.id.btnSignIn);
        mBtnSignUp = findViewById(R.id.btnSignUp);

        if(ParseUser.getCurrentUser() != null){
            HomeActivity();
        }


        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBtnSignIn.getText().toString().isEmpty() && !mEtPassword.getText().toString().isEmpty()) {
                    ParseUser.logInInBackground(mEtUsername.getText().toString(), mEtPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_SHORT).show();
                                displayAlert("Welcome Back!", "");
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEtUsername.getText().toString().isEmpty() && !mEtPassword.getText().toString().isEmpty()) {
                    ParseUser user = new ParseUser();
                    user.setUsername(mEtUsername.getText().toString());
                    user.setPassword(mEtPassword.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "Sign Up Successfully!", Toast.LENGTH_SHORT).show();
                                displayAlert("Welcome to Book Club!", "");
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collection<String> permission = Arrays.asList("public_profile", "email");
                ParseFacebookUtils.logInWithReadPermissionsInBackground(MainActivity.this, permission, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null) {
                            ParseUser.logOut();
                            Log.e("error", "Error", e);

                        }
                        if (user == null) {
                            ParseUser.logOut();
                            Toast.makeText(MainActivity.this, "Canceled Facebook login", Toast.LENGTH_SHORT).show();
                            return;

                        }
                        if (user.isNew()) {
                            Toast.makeText(MainActivity.this, "User signed up through Facebook", Toast.LENGTH_SHORT).show();
                            getUserDetailsFromFB();
                            return;

                        }
                        Toast.makeText(MainActivity.this, "User logged in through Facebook", Toast.LENGTH_SHORT).show();
                        getUserDetailsFromParse();

                    }
                });
            }
        });
    }

    private void HomeActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getUserDetailsFromParse() {
        ParseUser user = ParseUser.getCurrentUser();
        String title = "Welcome Back";
        String message = "User: " + user.getUsername() + "\n" + "Login Email: " + user.getEmail();
        displayAlert(title, message);
    }

    private void displayAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void getUserDetailsFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                ParseUser user = ParseUser.getCurrentUser();
                try {
                    user.setUsername(jsonObject.getString("name"));
                    user.setEmail(jsonObject.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        displayAlert("New here", "Welcome");
                    }
                });
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

}