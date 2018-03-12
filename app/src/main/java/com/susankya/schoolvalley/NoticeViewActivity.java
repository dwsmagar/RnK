package com.susankya.schoolvalley;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NoticeViewActivity extends AppCompatActivity implements FragmentCodes {

    private static final String SAVING_STATE_SLIDER_ANIMATION = "SliderAnimationSavingState";
    private boolean isSliderAnimation = false;
    private static SQLiteDatabase db;
    private static final int ID_COPY = 1;
    @BindView(R.id.n_title)
    TextViewPlus titletv;
    @BindView(R.id.ph_tv)
    TextViewPlus phtv;
    @BindView(R.id.category_content)
    TextViewPlus categorytv;
    @BindView(R.id.date_content)
    TextViewPlus datetv;
    @BindView(R.id.attachment_ll)
    View attachmentLL;
    @BindView(R.id.attachment_tv)
    TextViewPlus attachmenttv;
    @BindView(R.id.sizetv)
    TextViewPlus sizetv;
    @BindView(R.id.n_desc)
    TextViewPlus desctv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.liketv)
    TextViewPlus likeTV;
    @BindView(R.id.liker)
    Button likeButton;
    @BindView(R.id.seentv)
    TextViewPlus seenTV;
    @BindView(R.id.seenRL)
    View seenRL;
    @BindView(R.id.image_layout)
    View imageLayout;
    notice n;
    int seen = 0, totalSeenCount = 0;
    GradientDrawable sd;
    boolean checkedOnce = false;
    int btnChecked;
    private String filename;
    SQLiteHelper sqLiteHelper;
    boolean seenChanged = false;
    boolean allGave = false;
    boolean isStudent = true;
    int l;
    public static final int STORAGE_PERMISSION = 2301;

    void perm() {
        String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        for (int i = 0; i < 1; i++) {
            if (ContextCompat.checkSelfPermission(NoticeViewActivity.this,
                    perms[i])
                    != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(NoticeViewActivity.this,
                        new String[]{perms[i]},
                        139
                );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    void putSeen() {
        if (Utilities.isConnectionAvailable(NoticeViewActivity.this) && seen == 0 && Utilities.isStudent(getApplicationContext())) {
            new PhpConnect(FragmentCodes.HOST + "Android%20Call/seen.php", "", getApplicationContext(), 0, false,
                    new String[]{FragmentCodes.CMDXXX, "update", n.getNotice_num() + "", Utilities.getUserNumber(getApplicationContext()) + ""}, new String[]{"cmdxxx", "category", "notice_number", "user_no"}).
                    setListener(
                            new PhpConnect.ConnectOnClickListener() {
                                @Override
                                public void onConnectListener(String res) {
                                    try {
                                        JSONObject j = new JSONObject(res);
                                        if (j.getString("message").toLowerCase().equals("success")) {
                                            totalSeenCount++;
                                            seenTV.setText(totalSeenCount + "");
                                            seenRL.setVisibility(View.VISIBLE);
                                            seenChanged = true;
                                        }

                                    } catch (Exception e) {

                                    }
                                }
                            }
                    );
        }
    }

    @BindView(R.id.img_view)
    ImageView img;
    @BindView(R.id.clickimg)
    TextViewPlus clickImg;
    Bitmap b;
    File mediaStorageDirN = new File(Environment.getExternalStorageDirectory(), downloadLocation + "/" + "Attachments");

    public void saverBitmap() {
        File f = new File(mediaStorageDirN, n.getFilename());
        ////Log.d("TAG", "onPreExecute: "+filePath);
        if (!mediaStorageDirN.exists()) {
            if (!mediaStorageDirN.mkdirs()) {
                //Log.d("App", "failed to create directory");
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addImageToGallery(f.getAbsolutePath(), getApplicationContext());
    }

    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality, String filename) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), src, filename, null);
        return Uri.parse(path);
    }

    void pictureShower() {
        File f = new File(mediaStorageDirN, n.getFilename());
        if (ContextCompat.
                checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (f.exists()) {
                String filePath = f.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                img.setImageBitmap(bitmap);
                b = bitmap;
                imageLayout.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                clickImg.setVisibility(View.VISIBLE);
            } else {
                ImageLoader i = ImageLoader.getInstance();
                i.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
                i.displayImage(n.getAttachmenturl(), img, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        img.setVisibility(View.VISIBLE);
                        b = loadedImage;
                        clickImg.setVisibility(View.VISIBLE);
                        saverBitmap();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        } else {
            perm();
            Toast.makeText(getApplicationContext(), "Please give Storage permission to display image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        n = ImmortalApplication.clickedNotice;

        setContentView(R.layout.activity_view_notice);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(n.getCategory());
                // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(UtilitiesAdi.catColor(n.getCategory(), this.getResources())));
            }

            if (n.getCategory() == null) {
                this.finish();
            }
        } catch (Exception e) {
            this.finish();
        }
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(UtilitiesAdi.catColor(n.getCategory(), this.getResources()));
        }

// clear FLAG_TRANSLUCENT_STATUS flag

        if (n.isHasImage()) {
            pictureShower();
        }

        img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri uri = getImageUri(b, Bitmap.CompressFormat.JPEG, 100, n.getFilename());
                        intent.setDataAndType(uri, "image/*");
                        startActivity(intent);
                    }
                }
        );

// finally change the color

        isStudent = Utilities.isStudent(getApplicationContext());
        titletv.setCustomFont(this, BOLD);
        titletv.setText(n.getTitle());
        sd = (GradientDrawable) phtv.getBackground().mutate();
        sd.setColor(UtilitiesAdi.catColor(n.getCategory(), this.getResources()));
        phtv.setBackground(sd);
        categorytv.setText(n.getCategory());
        phtv.setCustomFont(this, REGULAR);
        phtv.setText(n.getCategory().substring(0, 1).toUpperCase());
        datetv.setText(n.getDate());
        //desctv.setMovementMethod(LinkMovementMethod.getInstance());
        seen = n.seen;
        totalSeenCount = n.totalSeenCount;
        //Toast.makeText(getApplicationContext(),n.totalSeenCount+"",Toast.LENGTH_SHORT).show();
        if (totalSeenCount == 0)
            seenRL.setVisibility(View.GONE);
        else if (seen == 1 && Utilities.isStudent(getApplicationContext()))
            seenTV.setText(totalSeenCount + "");
        else if (Utilities.isAdmin(getApplicationContext()))
            seenTV.setText(totalSeenCount + "");

        desctv.setText(n.getDescription());
        Linkify.addLinks(desctv, Linkify.ALL);
        registerForContextMenu(desctv);

        if (!isStudent) {
            likeButton.setVisibility(View.GONE);
        } else if (n.liked)
            likeButton.setText("Unlike");
        likeTV.setText(String.valueOf(n.likeNum));
        likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String action = "";
                        if (n.liked)
                            action = "unlike";
                        else action = "like";
                        final Calendar c = Calendar.getInstance();
                        final SimpleDateFormat d1 = new SimpleDateFormat("yyyy MMM dd");
                        final SimpleDateFormat d2 = new SimpleDateFormat("hh:mm:ss a");
                        String date = d1.format(c.getTime());
                        String current_time = d2.format(c.getTime());
                        new PhpConnect(MAIN_DATABASE + "like_notice.php", "", getApplicationContext(), 0,
                                new String[]{CMDXXX, Utilities.getUserNumber(getApplicationContext()) + "", UtilitiesAdi.giveMeSN(getApplicationContext(), Utilities.getDatabaseName(getApplicationContext())),
                                        action, n.getNotice_num() + "", date, current_time},
                                new String[]{"cmdxxx", "user_no", "college_sn", "action", "notice_no", "date", "time"}).setListener(
                                new PhpConnect.ConnectOnClickListener() {
                                    @Override
                                    public void onConnectListener(String res) {
                                        try {
                                            JSONObjectAuto j = new JSONObjectAuto(new JSONObject(res));
                                            String msg = j.getString("message");
                                            if (msg.equals("1")) {
                                                if (n.liked) {
                                                    n.liked = false;
                                                    likeButton.setText("Like");
                                                    n.likeNum--;

                                                } else {
                                                    n.liked = true;
                                                    likeButton.setText("Unlike");
                                                    n.likeNum++;
                                                }
                                                likeTV.setText(n.likeNum + "");
                                            }

                                        } catch (Exception e) {
                                        }
                                    }
                                }
                        );
                    }
                }
        );
        if (!n.isHasAttachment())
            attachmentLL.setVisibility(View.GONE);
        else {
            attachmenttv.setText(n.getFilename());
            filename = n.getFilename();
            sizetv.setText(n.sizeMB);
            attachmentLL.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                            for (int i = 0; i < 1; i++) {
                                if (ContextCompat.checkSelfPermission(NoticeViewActivity.this,
                                        perms[i])
                                        != PackageManager.PERMISSION_GRANTED) {

                                    // Should we show an explanation?
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(NoticeViewActivity.this,
                                            perms[i])) {

                                        // Show an explanation to the user *asynchronously* -- don't block
                                        // this thread waiting for the user's response! After the user
                                        // sees the explanation, try again to request the permission.

                                    } else {

                                        // No explanation needed, we can request the permission.

                                        ActivityCompat.requestPermissions(NoticeViewActivity.this,
                                                new String[]{perms[i]},
                                                STORAGE_PERMISSION
                                        );

                                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                        // app-defined int constant. The callback method gets the
                                        // result of the request.
                                    }
                                } else attachmentDownloader();
                            }
                        }
                    }
            );
        }
        putSeen();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {

        menu.setHeaderTitle("Select Action");
        menu.add(0, ID_COPY, 0, "Copy");//groupId, itemId, order, title
        //menu.add(0, ID_DETAILS, 0, "Details");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ID_COPY:
                String string = desctv.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Notice", string);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Text copied to clipboard.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onDestroy() {

        Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
        if (seenChanged)
            setResult(Activity.RESULT_OK, resultIntent);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    attachmentDownloader();
                    //granted
                } else {
                    Toast.makeText(getApplicationContext(), "Please grant permission in order to download this attachment.", Toast.LENGTH_LONG).show();
                    //not granted
                }
                break;
            case 139: {
                pictureShower();
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public ProgressDialog progressDialog;

    public void attachmentDownloader() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMessage("Download in progress");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        new DownloadTask(NoticeViewActivity.this).execute(Utilities.encodeLinkSpace(n.getAttachmenturl()));
    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private String filePath;
        private PowerManager.WakeLock mWakeLock;

        private File file;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), downloadLocation + "/" + "Attachments");
            filePath = Environment.getExternalStorageDirectory().toString() + "/" + downloadLocation + "/" + "Attachments/" + filename;
            ////Log.d("TAG", "onPreExecute: "+filePath);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    //Log.d("App", "failed to create directory");

                }
            }
            // download the file

            file = new File(mediaStorageDir, n.getFilename());
            if (!file.exists()) {
                progressDialog.show();
                progressDialog.setMessage(n.getFilename());
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DownloadTask.this.cancel(true);
                                dialog.dismiss();
                            }
                        });
            } else {
                Toast.makeText(NoticeViewActivity.this, "This attachment exists.", Toast.LENGTH_LONG).show();
                DownloadTask.this.cancel(true);
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                //  //Log.d("checkingok",s);
                progressDialog.dismiss();
                Snackbar.make(NoticeViewActivity.this.findViewById(R.id.rootV), "Attachment download complete", Snackbar.LENGTH_SHORT).show();
                File file = new File(filePath);
                MimeTypeMap map = MimeTypeMap.getSingleton();
                String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                String type = map.getMimeTypeFromExtension(ext);

                if (type == null)
                    type = "*/*";
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.fromFile(file);

                    intent.setDataAndType(data, type);

                    startActivity(intent);
                } catch (Throwable t) {

                }
            }
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                //Log.d("okboy", sUrl[0]);
                final URL url = new URL(sUrl[0]);

                connection = (HttpURLConnection) url.openConnection();
                //connection.setRequestProperty("Accept-Encoding", "identity"); // <--- Add this line
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = Integer.parseInt(n.getFileSize());

                //Log.d("fileLen", fileLength + "");
                if (!file.exists()) {
                    input = connection.getInputStream();

                    output = new FileOutputStream(file);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
                        if (isCancelled()) {
                            input.close();
                            return null;
                        }
                        total += count;
                        // publishing the progress....
                        if (fileLength > 0) // only if total length is known
                            publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);


                    }
                    if (n.isHasImage())
                        addImageToGallery(file.getAbsolutePath(), NoticeViewActivity.this);
                } else {
                    NoticeViewActivity.this.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(NoticeViewActivity.this, "This attachment exists.", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                }

            } catch (Exception e) {
                //Log.d("dl error", e + "");
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                    //Log.d("dl error", ignored + "");
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public void onSaveInstanceState(Bundle outstate) {

        if (outstate != null) {
            outstate.putBoolean(SAVING_STATE_SLIDER_ANIMATION, isSliderAnimation);
        }
        super.onSaveInstanceState(outstate);
    }

    public void onRestoreInstanceState(Bundle inState) {
        if (inState != null) {
            isSliderAnimation = inState.getBoolean(SAVING_STATE_SLIDER_ANIMATION, false);
        }
        super.onRestoreInstanceState(inState);
    }
}
