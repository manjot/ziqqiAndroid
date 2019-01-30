package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.ziqqi.R;
import com.ziqqi.databinding.ActivityLoginBinding;
import com.ziqqi.model.loginResponse.LoginResponse;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.LoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    EditText etPassword;
    CallbackManager callbackManager;
    TextView btnFacebook, btnGmail;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;

    LoginViewModel loginViewModel;
    ActivityLoginBinding loginBinding;
    String deviceId;

    public static final int RC_SIGN_IN = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginBinding.executePendingBindings();
        loginBinding.setViewModel(loginViewModel);

        deviceId = UUID.randomUUID().toString();
        initViews();
        setUpFonts();

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));
                startFacebookLogin(callbackManager);
            }
        });

        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    private void setUpFonts() {
        Typeface regular = FontCache.get(getResources().getString(R.string.regular), this);
        Typeface medium = FontCache.get(getResources().getString(R.string.medium), this);
        Typeface light = FontCache.get(getResources().getString(R.string.light), this);
        loginBinding.etEmail.setTypeface(regular);
        loginBinding.etPassword.setTypeface(regular);
        loginBinding.tvForgetPassword.setTypeface(regular);
        loginBinding.btnSignIn.setTypeface(medium);
        loginBinding.tvOr.setTypeface(light);
        loginBinding.tvDontHaveAcc.setTypeface(light);
        loginBinding.tvSignIn.setTypeface(medium);

    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void gettingFacebookData(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            sendReqToServer(object.getString("first_name"), object.getString("last_name"), object.getString("email"), "f", accessToken.getUserId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,gender,last_name,id,email,name,link,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void startFacebookLogin(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                PreferenceManager.setStringValue(Constants.FACEBOOK_OR_GMAIL, "f");
                gettingFacebookData(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        PreferenceManager.setStringValue(Constants.FACEBOOK_OR_GMAIL, "g");
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            Log.e("GoogleSIgnIn", acct.getGivenName());
            String firstName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String lastName = acct.getFamilyName();
            String id = acct.getId();
            sendReqToServer(firstName, lastName, personEmail, "g", id);
        }
    }


    private void initViews() {
        etPassword = findViewById(R.id.et_password);
        btnFacebook = findViewById(R.id.btn_fb);
        btnGmail = findViewById(R.id.btn_gmail);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);


        callbackManager = CallbackManager.Factory.create();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void onClickForgetPassword(View view) {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }

    public void onClickSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void onClickSignIn(View view) {
        final HashMap<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("username", loginBinding.etEmail.getText().toString());
        loginRequest.put("password", loginBinding.etPassword.getText().toString());
        loginRequest.put("device_id", deviceId);
        loginRequest.put("device_type", 1);

        loginViewModel.init(loginRequest);
        loginViewModel.getLoginResponse().observe(LoginActivity.this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(@Nullable LoginResponse loginResponse) {
                if (loginResponse.getError()) {
                    Utils.ShowToast(LoginActivity.this, loginResponse.getMessage());
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    PreferenceManager.setStringValue(Constants.DEVICE_ID, deviceId);
                    PreferenceManager.setBoolValue(Constants.LOGGED_IN, true);
                    finishAffinity();
                }
            }
        });
    }

    public void sendReqToServer(String firstName, String lastName, String email, String type, String userId) {

        HashMap<String, Object> socialLoginRequest = new HashMap<>();
        socialLoginRequest.put("email", email);
        socialLoginRequest.put("fname", firstName);
        socialLoginRequest.put("lname", lastName);
        socialLoginRequest.put("social_id", userId);
        socialLoginRequest.put("social_login_type", type);
        socialLoginRequest.put("device_id", deviceId);
        socialLoginRequest.put("device_type", 1);

        loginViewModel.init(socialLoginRequest);
        loginViewModel.getLoginResponse().observe(LoginActivity.this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(@Nullable LoginResponse loginResponse) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                PreferenceManager.setStringValue(Constants.DEVICE_ID, deviceId);
                PreferenceManager.setBoolValue(Constants.LOGGED_IN, true);
                finishAffinity();
            }
        });
    }
}
