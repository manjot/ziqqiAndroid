package com.ziqqi.activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ziqqi.R;
import com.ziqqi.databinding.ActivityProfileBinding;
import com.ziqqi.model.uploadphotomodel.UploadPhoto;
import com.ziqqi.retrofit.ApiClient;
import com.ziqqi.retrofit.ApiInterface;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.ProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    ProfileViewModel viewModel;
    String strSharingUrl;
    LoginDialog loginDialog;
    private static final int CAPTURE_IMAGE_TO_CAMERA_ACTIVITY_REQUEST_CODE = 1887;
    private static final int CAPTURE_IMAGE_TO_GALLERY_ACTIVITY_REQUEST_CODE = 1889;
    File compressedImageFile = null;
    File f = null;
    Bitmap bitmap = null;
    private String path;
    RequestBody authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        loginDialog = new LoginDialog();

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        strSharingUrl = "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getPackageName();

        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
            binding.tvName.setText(PreferenceManager.getStringValue(Constants.FIRST_NAME));
            binding.tvEmail.setText(PreferenceManager.getStringValue(Constants.EMAIL));
            Glide.with(this)
                    .load(PreferenceManager.getStringValue(Constants.USER_PHOTO)).apply(RequestOptions.placeholderOf(R.drawable.place_holder))
                    .into(binding.ivProfilePic);
        }



        binding.ivChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                    requestStoragePermission();
                }else {
                    loginDialog = new LoginDialog();
                    loginDialog.showDialog(ProfileActivity.this);
                }

            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.llMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MyOrdersActivity.class));
            }
        });
        binding.llMyAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MyAddressBookActivity.class));
            }
        });
        binding.llCountryLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SelectYourLanguageActivity.class));
            }
        });
        binding.llCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CommunicationPrefActivity.class));
            }
        });
        binding.llHelpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HelpCenterActivity.class));
            }
        });
        binding.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ziqqi");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, strSharingUrl);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }
        });
        binding.llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FeedbackActivity.class));
            }
        });
        binding.llRatePlaystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
    }

    private void addPhoto() {
        if (f != null) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
            MultipartBody.Part body = MultipartBody.Part.createFormData("filename", f.getName(), reqFile);
            authToken = createPartFromString(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<UploadPhoto> call = apiInterface.addPhoto(authToken, body);
            call.enqueue(new Callback<UploadPhoto>() {
                @Override
                public void onResponse(Call<UploadPhoto> call, Response<UploadPhoto> response) {
                    Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    PreferenceManager.setStringValue(Constants.USER_PHOTO, response.body().getPayload().getFileupload());
                }

                @Override
                public void onFailure(Call<UploadPhoto> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_TO_CAMERA_ACTIVITY_REQUEST_CODE) {
            if (Environment.getExternalStorageDirectory().toString() != null && !Environment.getExternalStorageDirectory().toString().isEmpty()) {
//                String enveronmentName = Environment.getExternalStorageDirectory().toString();
//                if (Environment.getExternalStorageDirectory().toString() != null && !Environment.getExternalStorageDirectory().toString().isEmpty()) {
//                    PrefKeep.getInstance().setUserPicString(Environment.getExternalStorageDirectory().toString());
//                }
//                if (enveronmentName == null && enveronmentName.equalsIgnoreCase("")) {
//                    enveronmentName = PrefKeep.getInstance().getUserPicString();
//                }
                f = new File(Environment.getExternalStorageDirectory().toString());
            }
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(this, "Error while capturing image", Toast.LENGTH_LONG).show();

                return;

            }

            try {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                binding.ivProfilePic.setImageBitmap(bitmap);
                addPhoto();
                // uploadImageAvtar();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (requestCode == CAPTURE_IMAGE_TO_GALLERY_ACTIVITY_REQUEST_CODE) {

            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");
                path = getRealPathFromURI(selectedImage);
                // PrefKeep.getInstance().setUserPicString(path);
//                if (path == null && path.equalsIgnoreCase("")) {
//                    path = PrefKeep.getInstance().getUserPicString();
//                }
                f = new File(path);
                binding.ivProfilePic.setImageBitmap(bitmap);
                addPhoto();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment
                                    .getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(f));
                            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                            startActivityForResult(intent, CAPTURE_IMAGE_TO_CAMERA_ACTIVITY_REQUEST_CODE);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, CAPTURE_IMAGE_TO_GALLERY_ACTIVITY_REQUEST_CODE);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(ProfileActivity.this, "Camera Permission error" ,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ProfileActivity.this, "Camera Permission error" ,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.RECORD_AUDIO
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        selectImage();
//                        if (report.areAllPermissionsGranted()) {
//                            selectImage();
//                            // do you work now
//                        }
//
//                        // check for permanent denial of any permission
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            showSettingsDialog();
//                            // permission is denied permenantly, navigate user to app settings
//                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

}
