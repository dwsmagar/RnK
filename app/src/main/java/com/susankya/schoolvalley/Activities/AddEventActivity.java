package com.susankya.schoolvalley.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.susankya.schoolvalley.Fragments.EventsFragment;
import com.susankya.schoolvalley.Fragments.Nimainterface;
import com.susankya.schoolvalley.ImmortalApplication;
import com.susankya.schoolvalley.R;
import com.susankya.schoolvalley.Utilities;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.susankya.schoolvalley.NewAboutUsFragment.REQUEST_CODE_CHOOSE;

public class AddEventActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.editBanner)
    TextView addBanner;
    @BindView(R.id.event_title)
    EditText eventTitle;
    @BindView(R.id.event_location)
    EditText eventLocation;
    @BindView(R.id.event_price)
    EditText eventPrice;
    @BindView(R.id.event_desc)
    EditText eventDescription;
    @BindView(R.id.event_organized_by)
    EditText eventOrganizedBy;
    @BindView(R.id.event_date)
    TextView eventDate;
    @BindView(R.id.event_time)
    TextView eventTime;
    @BindView(R.id.eventBanner)
    ImageView eventBanner;
    @BindView(R.id.event_publish)
    Button btnPublish;
    private String dateValue = "";
    private String timeValue = "";
    private int changed = 0;
    private List<Uri> mSelected = new ArrayList<>();
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            toolbar.setTitle("Edit Event");
            btnPublish.setText("Update");
            populateData(getIntent().getExtras());
        } else
            toolbar.setTitle("Add Event");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
        addBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addBanner.setText("Edit Banner Image");
                getBanner();
            }
        });

        eventBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changed++;
                getBanner();

            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isConnectionAvailable(AddEventActivity.this)) {
                    onSubmit();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                    builder.setMessage("No internet connection, please try again later.")
                            .setPositiveButton("Ok", null)
                            .show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void onSubmit() {
        boolean error = false, titleErr = false, desErr = false, locationErr = false, orgErr = false, priceErr = false, dateErr = false, timeErr = false;
        String title = eventTitle.getText().toString().trim();
        String description = eventDescription.getText().toString().trim();
        String location = eventLocation.getText().toString().trim();
        String organized_by = eventOrganizedBy.getText().toString().trim();
        String eventFee = eventPrice.getText().toString().trim();

        if (mSelected.isEmpty()) {
            error = true;
            addBanner.setTextColor(getResources().getColor(R.color.red));
            addBanner.setText("Choose the image. *");
        }

        if (title.isEmpty()) {
            titleErr = true;
            eventTitle.setError("This field is required *");
        } else {
            eventTitle.setError(null);
        }

        if (description.isEmpty()) {
            desErr = true;
            eventDescription.setError("This field is required *");
        } else {
            eventDescription.setError(null);
        }

        if (location.isEmpty()) {
            locationErr = true;
            eventLocation.setError("This field is required *");
        } else {
            eventLocation.setError(null);
        }

        if (organized_by.isEmpty()) {
            orgErr = true;
            eventOrganizedBy.setError("This field is required *");
        } else {
            eventOrganizedBy.setError(null);
        }

        if (eventFee.isEmpty()) {
            priceErr = true;
            eventPrice.setError("This field is required *");
        } else if (!TextUtils.isDigitsOnly(eventFee)) {
            priceErr = true;
            eventPrice.setError("Enter the valid number");
        } else {
            eventPrice.setError(null);
        }

        if (dateValue.trim().isEmpty()) {
            dateErr = true;
            eventDate.setTextColor(getResources().getColor(R.color.red));
            eventDate.setText("Date is empty*");
        } else eventDate.setTextColor(getResources().getColor(R.color.secondary_text));

        if (timeValue.trim().isEmpty()) {
            timeErr = true;
            eventTime.setTextColor(getResources().getColor(R.color.red));
            eventTime.setText("Time is empty*");
        } else eventTime.setTextColor(getResources().getColor(R.color.secondary_text));


        if (!error && !dateErr && !timeErr && !titleErr && !priceErr && !desErr && !orgErr && !locationErr) {
            if (Utilities.isConnectionAvailable(AddEventActivity.this)) {
                addEvent(title, eventFee, description, organized_by, dateValue, timeValue, location, mSelected.get(0));
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_ASK_PERMISSIONS)
    private void getBanner() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Matisse.from(AddEventActivity.this)
                    .choose(MimeType.allOf())
                    .theme(R.style.Matisse_Dracula)
                    .countable(false)
                    .maxSelectable(1)
                    .gridExpectedSize(this.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new PicassoEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        } else {
            EasyPermissions.requestPermissions(this, "Before you can select photos, please grant Permissions. Click OK to proceed.",
                    REQUEST_CODE_ASK_PERMISSIONS, perms);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            if (changed > 1)
                mSelected.set(0, Matisse.obtainResult(data).get(0));
            else
                mSelected = Matisse.obtainResult(data);

            Picasso.with(AddEventActivity.this).load(mSelected.get(0)).fit().into(eventBanner);
            addBanner.setTextColor(getResources().getColor(R.color.white));
            addBanner.setText(R.string.change_banner);
        }
    }

    private void initData() {
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }
        });
    }

    private void chooseDate() {
        Calendar now = Calendar.getInstance();
        new android.app.DatePickerDialog(
                this,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String yearStr = String.valueOf(year);
                        String monthStr = String.valueOf(month + 1);
                        String dayStr = String.valueOf(dayOfMonth);
                        String dateStr = yearStr + "-" + monthStr + "-" + dayStr;
                        eventDate.setText(dateStr);
                        dateValue = dateStr;
                        eventDate.setTextColor(getResources().getColor(R.color.secondary_text));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void chooseTime() {
        Calendar now = Calendar.getInstance();
        new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourStr = String.valueOf(hourOfDay);
                        String minStr = String.valueOf(minute);
                        String timeStr = hourStr + ":" + minStr;
                        eventTime.setText(timeFormatter(timeStr));
                        timeValue = timeStr;
                        eventTime.setTextColor(getResources().getColor(R.color.secondary_text));
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        ).show();
    }

    private String timeFormatter(String inputTime) {
        String outputTime = null;
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        try {
            Date _24HourDt = _24HourSDF.parse(inputTime);
            outputTime = _12HourSDF.format(_24HourDt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTime;
    }

    private void addEvent(String title, String priceValue, String des, String organizeValue, String date_value, String time_value, String location_value, Uri image) {
        final ProgressDialog dialog = new ProgressDialog(AddEventActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        File file = new File(getRealPathFromUri(mSelected.get(0), getApplicationContext()));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), priceValue);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), des);
        RequestBody organized = RequestBody.create(MediaType.parse("text/plain"), organizeValue);
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), date_value);
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), "03-03");
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), location_value);
        Nimainterface nimainterface = ImmortalApplication.getRetrofit().create(Nimainterface.class);
        nimainterface.postEvent(name, description, price, organized, date, time, body, location).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    EventsFragment fragment = new EventsFragment();
                    fragment.add = true;
                    finish();
                } else
                    Toast.makeText(AddEventActivity.this, "Something is missing", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("fatal", "failure" + t);
            }
        });
    }

    private void populateData(Bundle bundle) {
        Picasso.with(AddEventActivity.this).load(bundle.getString("imageUrl")).into(eventBanner);
        eventTitle.setText(bundle.getString("name"));
        eventDate.setText(bundle.getString("date"));
        eventTime.setText(bundle.getString("time"));
        eventLocation.setText(bundle.getString("location"));
        eventPrice.setText(bundle.getString("price"));
        eventOrganizedBy.setText(bundle.getString("organized_by"));
        eventDescription.setText(bundle.getString("description"));
    }


    public static String getRealPathFromUri(final Uri uri, Context context) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static MultipartBody.Part getPart(File file, String partName) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

