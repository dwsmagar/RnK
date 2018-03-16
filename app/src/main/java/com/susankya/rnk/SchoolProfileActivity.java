package com.susankya.rnk;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class
SchoolProfileActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final int PICKIMAGE = 25;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    String filePath;

    private ViewPager mViewPager;
    private String[] tabs;

    public static ArrayList<AboutInfo> aboutShopList;
    public static ArrayList<ProductInfo> productsList=new ArrayList<>();
    String name,location;

    @BindView(R.id.locationtv)TextViewPlus locationtv;
    @BindView(R.id.changeCover)TextViewPlus changeCover;
    @BindView(R.id.coverpic)ImageView coverpic;
    @BindView(R.id.gpsbtn)ImageButton gpsbtn;
    @BindView(R.id.locationLL)LinearLayout locationLL;
    private String collegeName,cLocation,cFollowers,cBannerURL,SN;
    public static String dbName="Susankyatest";
    ImageLoader loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        ButterKnife.bind(this);
        loader=ImageLoader.getInstance();
        loader.init(ImageLoaderConfiguration.createDefault(this));
        tabs=new String[]{"About","Gallery"};
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle b=getIntent().getExtras();
        collegeName=b.getString("name");
        cLocation=b.getString("location");
        cBannerURL=b.getString("url");
        dbName=b.getString("dbName");
        SN=b.getString("SN");
        if(getActionBar()!=null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        locationtv.setText(cLocation);
        toolbar.setTitle(collegeName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fetchAbout();
        if(!UtilitiesAdi.isProfileOfAdmin(getApplicationContext(),SN))
        {
           changeCover.setVisibility(View.GONE);
        }else
            changeCover.setVisibility(View.VISIBLE);
        changeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                /*
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICKIMAGE);*/
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case REQUEST_CODE_ASK_PERMISSIONS:
                    showFileChooser();
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PICKIMAGE:
                if(resultCode !=RESULT_CANCELED){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Toast.makeText(getApplicationContext(),selectedImage.toString(),Toast.LENGTH_SHORT).show();
                    //Log.d("TAG", "onActivityResult:original "+selectedImage.toString());
                    filePath = getRealPathFromURI(selectedImage.toString());
                    CompressImage compressImage=new CompressImage(getApplicationContext(),selectedImage);
                    filePath=compressImage.getFinalFilename();
                    //Log.d("TAG1", "onActivityResult: "+compressImage.getFinalFilename());
                    //Log.d("TAG", "onActivityResult:edited"+filePath);
                    String url=FragmentCodes.MAIN_DATABASE+"update_cover_pic.php";
                    new NewFileUploader(filePath,"img",url,new String[]{"cmdxxx","dbName"},new String[]{FragmentCodes.CMDXXX,Utilities.getDatabaseName(getApplicationContext())},SchoolProfileActivity.this).setFileUploadedListener(new NewFileUploader.FileUploadedListener() {
                        @Override
                        public void OnFileUploaded(String result) {
                            //Log.d("Gallery", "OnFileUploaded: "+result);
                            if(result.equals("successfully uploaded")){
                                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                                fetchAbout();
                            }
                        }
                    });
                }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }


        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23){
            int hasReadPermission =checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasReadPermission!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            showFileChooser();
        } else{
            showFileChooser();
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position)
            {
                case 0:
                    return AboutSchoolFragment.newInstance(SN,"");

                case 1:
                    return GalleryFragment.newInstance(SN,"");

                default:
                    return new AboutSchoolFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];

        }
    }

    String[] fields=new String[]{"Name","Location","Date of establishment","Contact","Website","Email","Description"};
    String[] data;
    void fetchAbout()
    {
        aboutShopList=new ArrayList<>();
        String[] params=new String[]{FragmentCodes.CMDXXX,SN};
        String[] placeholders=new String[]{"cmdxxx","college_sn"};

        new PhpConnect(FragmentCodes.NEW_HOST+"Call Main Database/collegeProfile.php","Loading...",
                SchoolProfileActivity.this,1,false,params,placeholders)
                .setListener(
                        new PhpConnect.ConnectOnClickListener() {
                            @Override
                            public void onConnectListener(String res) {

                                try
                                {
                                    JSONObject jsonObject=new JSONObject(res);
                                    String location=jsonObject.getString("location");
                                    String URLboy=jsonObject.getString("cover_pic");
                                    String doe=jsonObject.getString("date_of_establishment");
                                    String desc=jsonObject.getString("description");
                                    String contact=jsonObject.getString("public_contact");
                                    String website=jsonObject.getString("website");
                                    String email=jsonObject.getString("email");
                                    cBannerURL=URLboy;

                                    data=new String[]{collegeName,location,doe,contact,website,email,desc};
                                    for (int i=0;i<data.length;i++)
                                    {
                                        AboutInfo a=new AboutInfo(fields[i],data[i]);
                                        aboutShopList.add(a);
                                    }
                                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                                    mViewPager = (ViewPager) findViewById(R.id.container);
                                    cLocation=location;
                                    locationtv.setText(cLocation);
                                    mViewPager.setAdapter(mSectionsPagerAdapter);
                                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                                    tabLayout.setupWithViewPager(mViewPager);
                                    //Log.d("cover11",URI.create(FragmentCodes.MAIN_DATABASE+URLboy).toString());
                                    loader.displayImage(URI.create(FragmentCodes.MAIN_DATABASE_DUMMY + URLboy).toString(), coverpic, new ImageLoadingListener() {
                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {

                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                            if(SN.equals(UtilitiesAdi.giveMeSN(getApplicationContext(),Utilities.getDatabaseName(getApplicationContext()))))
                                            {
Utilities.saveToInternalStorage(getApplicationContext(), loadedImage, Utilities.getUsername(getApplicationContext()) + "thumbnail.jpg");
Utilities.setProfilePicThumbnailLoaded(getApplicationContext(), true);
                                            }

                                        }

                                        @Override
                                        public void onLoadingCancelled(String imageUri, View view) {

                                        }
                                    });

                                }
                                catch (Exception e)
                                {
                                    //Log.d("galti vayo",e.toString());
                                }
                            }
                        }
                );
    }
    public String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        //Log.d("TAG", "getRealPathFromURI: "+contentURI.toString());
        if(!contentURI.toString().contains("file://")){
            String[] projection = { MediaStore.MediaColumns.DATA };
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(contentUri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else
                return null;}
        else return contentURI.toString().replace("file://","");
    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    PICKIMAGE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
