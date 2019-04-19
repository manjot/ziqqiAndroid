package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.LoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import static com.ziqqi.utils.Utils.setWindowFlag;

public class LoginActivity extends AppCompatActivity {
    EditText etPassword;
    CallbackManager callbackManager;
    TextView btnFacebook, btnGmail;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;

    LoginViewModel loginViewModel;
    ActivityLoginBinding loginBinding;
    String deviceId;

    Animation popEnter, popExit;
    boolean hasEmailAnimation = false;
    boolean hasPasswordAnimation = false;

    AlertDialog.Builder builder;

    public static final int RC_SIGN_IN = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ziqqi",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        loginBinding.executePendingBindings();
        loginBinding.setViewModel(loginViewModel);

        deviceId = UUID.randomUUID().toString();
        initViews();
        setUpFonts();

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityHelper.isConnectedToNetwork(LoginActivity.this)) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                    startFacebookLogin(callbackManager);
                } else {
                    //showAlert("f");
                    Utils.ShowToast(LoginActivity.this, "No Internet Connection");
                }
            }
        });

        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityHelper.isConnectedToNetwork(LoginActivity.this)) {
                    signInWithGoogle();
                } else {
                    // showAlert("g");
                    Utils.ShowToast(LoginActivity.this, "No Internet Connection");
                }
            }
        });

        loginBinding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Utils.isValidEmail(loginBinding.etEmail.getText().toString())) {
                    if (!hasEmailAnimation) {
                        loginBinding.ivEmail.startAnimation(popEnter);
                        loginBinding.ivEmail.setVisibility(View.VISIBLE);
                        hasEmailAnimation = true;
                    }
                } else {
                    if (loginBinding.ivEmail.getVisibility() == View.VISIBLE) {
                        loginBinding.ivEmail.startAnimation(popExit);
                        loginBinding.ivEmail.setVisibility(View.GONE);
                        hasEmailAnimation = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loginBinding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() >= 6) {
                    if (!hasPasswordAnimation) {
                        loginBinding.ivPassword.startAnimation(popEnter);
                        loginBinding.ivPassword.setVisibility(View.VISIBLE);
                        hasPasswordAnimation = true;
                    }
                } else {
                    if (loginBinding.ivPassword.getVisibility() == View.VISIBLE) {
                        loginBinding.ivPassword.startAnimation(popExit);
                        loginBinding.ivPassword.setVisibility(View.GONE);
                        hasPasswordAnimation = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showAlert(final String type) {
        builder.setTitle("")
                .setMessage("No Internet Connection")
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (type.equals("g")) {
                            signInWithGoogle();
                        } else if (type.equals("f")) {
                            startFacebookLogin(callbackManager);
                        } else {
                            signIn();
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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
        loginBinding.progressBar.setVisibility(View.VISIBLE);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void gettingFacebookData(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (object.has("email")) {
                                sendReqToServer(object.getString("first_name"), object.getString("last_name"), object.getString("email"), "f", accessToken.getUserId());
                            } else {
                                sendReqToServer(object.getString("first_name"), object.getString("last_name"), "", "f", accessToken.getUserId());

                            }
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
                error.printStackTrace();
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
        loginBinding.progressBar.setVisibility(View.GONE);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            // Log.e("GoogleSIgnIn", acct.getGivenName());
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        popEnter = AnimationUtils.loadAnimation(this, R.anim.pop_enter_animation);
        popExit = AnimationUtils.loadAnimation(this, R.anim.pop_exit_animation);

    }

    public void onClickForgetPassword(View view) {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }

    public void onClickSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void onClickSignIn(View view) {
        if (ConnectivityHelper.isConnectedToNetwork(this))
            signIn();
        else
            //showAlert("");
            Utils.ShowToast(LoginActivity.this, "No Internet Connection");
    }

    private void signIn() {
        final HashMap<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("username", loginBinding.etEmail.getText().toString());
        loginRequest.put("password", loginBinding.etPassword.getText().toString());
        loginRequest.put("device_id", deviceId);
        loginRequest.put("device_type", 1);

        loginViewModel.init(loginRequest);
        loginBinding.progressBar.setVisibility(View.VISIBLE);
        loginViewModel.getLoginResponse().observe(LoginActivity.this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(@Nullable LoginResponse loginResponse) {
                loginBinding.progressBar.setVisibility(View.GONE);
                if (loginResponse.getError()) {
                    if (loginResponse.getCode() == 204){
                        Utils.ShowToast(LoginActivity.this, loginResponse.getMessage());
                    }else if (loginResponse.getCode() == 203){
                        startActivity(new Intent(LoginActivity.this, OtpVerifyActivity.class).putExtra("cId", loginResponse.getPayload().getCustomer_id()));
                        Utils.ShowToast(LoginActivity.this, loginResponse.getMessage());
                        finish();
                    }
                } else {
                    loginBinding.progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    PreferenceManager.setStringValue(Constants.DEVICE_ID, deviceId);
                    PreferenceManager.setBoolValue(Constants.LOGGED_IN, true);
                    PreferenceManager.setStringValue(Constants.FIRST_NAME, loginResponse.getPayload().getFirstName());
                    PreferenceManager.setStringValue(Constants.EMAIL, loginResponse.getPayload().getEmail());
                    PreferenceManager.setStringValue(Constants.AUTH_TOKEN, loginResponse.getPayload().getAuth_token());
                    finishAffinity();
                }
            }
        });
    }

    public void sendReqToServer(String firstName, String lastName, String email, String type, String userId) {
        loginBinding.progressBar.setVisibility(View.VISIBLE);
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
                loginBinding.progressBar.setVisibility(View.GONE);
                if (!loginResponse.getError()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    PreferenceManager.setStringValue(Constants.DEVICE_ID, deviceId);
                    PreferenceManager.setBoolValue(Constants.LOGGED_IN, true);
                    PreferenceManager.setStringValue(Constants.FIRST_NAME, loginResponse.getPayload().getFirstName());
                    PreferenceManager.setStringValue(Constants.EMAIL, loginResponse.getPayload().getEmail());
                    PreferenceManager.setStringValue(Constants.AUTH_TOKEN, loginResponse.getPayload().getAuth_token());
                    finishAffinity();
                }
            }
        });
    }
}
