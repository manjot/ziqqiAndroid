package com.ziqqi.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ziqqi.R;
import com.ziqqi.fragments.DealsFragment;
import com.ziqqi.fragments.HomeFragment;
import com.ziqqi.fragments.SearchFragment;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.CustomTypefaceSpan;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    int permissionCode = 1234;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    Handler handler;
    ImageView ivProfilePic;
    TextView tvLogin;

    GoogleSignInAccount acct;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    AccessToken accessToken;

    String personName, personEmail;
    Uri personPhoto;

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    DealsFragment dealsFragment;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        callbackManager = CallbackManager.Factory.create();


        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.navigation);

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        handler = new Handler();
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        dealsFragment = new DealsFragment();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (!CheckingPermissionIsEnabledOrNot()) {
            RequestMultiplePermission();
        }


        ivProfilePic = navigationView.getHeaderView(0).findViewById(R.id.iv_profile_pic);
        tvLogin = navigationView.getHeaderView(0).findViewById(R.id.tv_login);
        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
            tvLogin.setText(R.string.log_out);
            tvLogin.setTypeface(FontCache.get(getResources().getString(R.string.regular), this));
        } else tvLogin.setText(R.string.login_signup);


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tvLogin.getText().toString().equals(getResources().getString(R.string.login_signup))) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    mDrawerLayout.closeDrawers();
                } else {
                    if (PreferenceManager.getStringValue(Constants.FACEBOOK_OR_GMAIL).equals("f")) {
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        mDrawerLayout.closeDrawers();
                        PreferenceManager.setBoolValue(Constants.LOGGED_IN, false);
                        finishAffinity();
                    } else if (PreferenceManager.getStringValue(Constants.FACEBOOK_OR_GMAIL).equals("g")) {
                        googleSignOut();
                        finishAffinity();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        mDrawerLayout.closeDrawers();
                        PreferenceManager.setBoolValue(Constants.LOGGED_IN, false);
                        finishAffinity();
                    }
                }
            }
        });


        if (savedInstanceState == null) {
            replaceFragment(homeFragment);
        }
        if (PreferenceManager.getStringValue(Constants.FACEBOOK_OR_GMAIL).equals("f")) {
            accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken == null || accessToken.isExpired()) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finishAffinity();
            } else {
                gettingFacebookData(accessToken);
            }
        } else {
            acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                personName = acct.getDisplayName();
                personEmail = acct.getEmail();
                personPhoto = acct.getPhotoUrl();

                if (personPhoto != null) {
                    Glide.with(this).load(personPhoto).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivProfilePic);
                } else {
                    Glide.with(this).load(R.drawable.ic_person).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivProfilePic);
                }

            }
        }

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
          /*  SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }*/

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int first = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int second = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int third = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int fourth = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return first == PackageManager.PERMISSION_GRANTED &&
                second == PackageManager.PERMISSION_GRANTED &&
                third == PackageManager.PERMISSION_GRANTED
                && fourth == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        CAMERA,
                        SEND_SMS
                        , READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                }, permissionCode);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == permissionCode) {
            if (grantResults.length > 0) {

                boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean SendSMSPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean readExternal = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean writeExtrenl = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                if (CameraPermission && SendSMSPermission && readExternal && writeExtrenl) {

                } else {
                    //  Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, 300);
        mDrawerLayout.closeDrawers();
        return false;
    }

    private void navigate(int id) {
        switch (id) {
            case R.id.bottom_nav_home:
                if (!homeFragment.isVisible())
                    replaceFragment(homeFragment);
                break;
            case R.id.bottom_nav_search:
                if (!searchFragment.isVisible())
                    replaceFragment(searchFragment);
                break;

            case R.id.bottom_nav_coupan:
                if (!dealsFragment.isVisible())
                    replaceFragment(dealsFragment);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 1) {
                finish();
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private void gettingFacebookData(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (object.getString("first_name") != null) {
                                PreferenceManager.setStringValue(Constants.FIRST_NAME, object.getString("first_name"));
                            }
                            if (object.getString("last_name") != null) {
                                PreferenceManager.setStringValue(Constants.LAST_NAME, object.getString("last_name"));
                            }

                            PreferenceManager.setStringValue(Constants.USER_ID, accessToken.getUserId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,gender,last_name,id,email,name,link,picture");
        request.setParameters(parameters);
        request.executeAsync();

        getFacebookProfilePicture();
    }

    private void getFacebookProfilePicture() {
        personPhoto = Uri.parse("https://graph.facebook.com/" + PreferenceManager.getStringValue(Constants.USER_ID) + "/picture?type=large");
        Log.e("ProfilePic", "https://graph.facebook.com/" + PreferenceManager.getStringValue(Constants.USER_ID) + "/picture?type=large");
        if (personPhoto != null) {
            Glide.with(this).load(personPhoto).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivProfilePic);
        } else {
            Glide.with(this).load(R.drawable.ic_person).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivProfilePic);
        }
    }

    private void googleSignOut() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        mDrawerLayout.closeDrawers();
                        PreferenceManager.setBoolValue(Constants.LOGGED_IN, false);
                    }
                });

    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
}
