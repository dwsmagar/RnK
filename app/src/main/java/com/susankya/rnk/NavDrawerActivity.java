package com.susankya.rnk;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.susankya.rnk.Fragments.AppointmentFragment;
import com.susankya.rnk.Fragments.EventsFragment;
import com.susankya.rnk.Fragments.GetAppointmentFragment;
import com.susankya.rnk.Fragments.IntroductionFragment;
import com.susankya.rnk.Fragments.Nimainterface;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.susankya.rnk.NewSettingsFragment.HASLOGGED_OUt;
import static com.susankya.rnk.NewSettingsFragment.LOGGED_OUt;

public class NavDrawerActivity extends AppCompatActivity implements FragmentCodes {
    Toolbar toolbar;
    View header;
    //BroadcastReceiver onNotice;
    private ListView navList;
    private NavigationView navView;
    private RelativeLayout navlayout;
    private TextView home;
    private DrawerLayout mDrawerLayout;
    private TextView sign_out;
    private TextView change_pd;
    private TextView circle_tv;
    private TextView fullname;
    private TextView username;
    private ImageView imageView;
    //private Menu menu;

    private static ArrayList<notice> sharedNotices;
    private String[] mNavTiles;
    AnimationDrawable drawable;
    private static int lastFragmentIndex = 0;
    public static int currentRoutine;
    private int type;
    public static NavDrawerActivity app;
    public int index = 0, previousIndex;
    public boolean loggedIn;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private TypedArray navMenuIcons;
    public static boolean shouldresume;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    public static EditText searchEditText;
    private static boolean isShowingSearchEditText;
    private static boolean isFromDialog = true;
    private boolean doubleBackToExitPressedOnce = false;
    private static boolean isFromHome;
    private static LinearLayout motherView;
    private SharedPreferences sp;
    private CharSequence mTitle;
    private static ArrayList<View> views = new ArrayList();
    private boolean isNoticeNotificationOn;
    private static ArrayList<question> sharedQuestions;
    private static int zugad = 0;

    public static boolean isShowingSearchEditText() {
        return isShowingSearchEditText;
    }

    public static void setIsShowingSearchEditText(boolean isShowingSearchEditText) {
        NavDrawerActivity.isShowingSearchEditText = isShowingSearchEditText;
    }

    public static EditText getSearchEditText() {
        return searchEditText;
    }

    public static void setSearchEditText(EditText searchEditText) {
        NavDrawerActivity.searchEditText = searchEditText;
    }


    public static boolean isIsFromHome() {
        return isFromHome;
    }

    public void logout() {
        new AlertDialog.Builder(this).
                setTitle("Log out")
                .setMessage("Do you really want to log out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                        "Log out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences1 = NavDrawerActivity.this.getApplicationContext().getSharedPreferences("rememberlogin", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                editor1.putBoolean("isloggedin", false);
                                editor1.commit();
                                SharedPreferences sharedPreferences = NavDrawerActivity.this.getApplicationContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("name");
                                editor.remove("number");
                                editor.remove("username");
                                editor.remove("password");
                                editor.clear();
                                editor.commit();
                                SharedPreferences shared = NavDrawerActivity.this.getApplicationContext().getSharedPreferences("dbName", Context.MODE_PRIVATE);
                                SharedPreferences.Editor se = shared.edit();
                                se.clear();
                                se.commit();
                                Utilities.setProfilePicThumbnailLoaded(NavDrawerActivity.this.getApplicationContext(), false);
                                startActivity(new Intent(NavDrawerActivity.this, StartActivity.class));

                                dialog.cancel();
                                // startActivity(new Intent(NavDrawerActivity.this,StartActivity.class));
                                NavDrawerActivity.this.finish();
                            }
                        }
                ).setNegativeButton(
                "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        ).show();

    }

    public static void setIsFromHome(boolean isFromHome) {
        NavDrawerActivity.isFromHome = isFromHome;
    }


    public static void setIsFromDialog(boolean isFromDialog) {
        NavDrawerActivity.isFromDialog = isFromDialog;
    }

    public static boolean isIsFromDialog() {
        return isFromDialog;
    }


    public int getPreviousIndex() {
        return previousIndex;
    }

    public static int getZugad() {
        return zugad;
    }

    public static void setZugad(int zugada) {
        zugad = zugada;
    }

    public static ArrayList<question> getSharedQuestions() {
        return sharedQuestions;
    }

    public static void setSharedQuestions(ArrayList<question> sharedQuestionsa) {
        sharedQuestions = sharedQuestionsa;
    }

    public static void setViewListValue(int index, View v) {
        views.add(index, v);
    }

    public void showSearch() {
        searchEditText.setVisibility(View.VISIBLE);
        isShowingSearchEditText = true;
        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSearch() {
        searchEditText.setVisibility(View.GONE);
        isShowingSearchEditText = false;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void selectItem(int position) {
        shouldresume = true;
        position--;
        Fragment fragment = null;
        previousIndex = index;
        index = position;
        if (type == STUDENT)
            fragment = studentCase(position);
        else if (type == ADMIN)
            fragment = AdminCase(position);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (!(lastFragmentIndex == index)) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, Integer.toString(position)).addToBackStack(null).commit();
                //Log.d("TAG", "selectItem: "+fragmentManager.getBackStackEntryCount());
            } else {
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, Integer.toString(position)).addToBackStack(null).commit();
                //Log.d("TAG", "selectItem: "+fragmentManager.getBackStackEntryCount());
            }
        }
        lastFragmentIndex = index;
    }

    public Fragment studentCase(int position) {
        hideSearch();
        switch (position) {
            case 0:
                return new IntroductionFragment();
            case 1:
                NavDrawerActivity.getSearchEditText().setHint("Search");
                return new StudentProfileFragment();
            case 2:
                return new NoticeListFragment();
            case 3:
                return new EventsFragment();
            case 4: {
                return new AppointmentFragment();
            }
            case 5:
                //   Toast.makeText(getApplicationContext(),Utilities.getSN(getApplicationContext())+ UtilitiesAdi.giveMeSN(getApplicationContext(), Utilities.getDatabaseName(getApplicationContext())),Toast.LENGTH_SHORT).show();
                //  return ChatFragment.newInstance(Utilities.getSN(getApplicationContext()), UtilitiesAdi.giveMeSN(getApplicationContext(), Utilities.getDatabaseName(getApplicationContext())),Utilities.getCollegeName(getApplicationContext()));
                if (!(Utilities.getBlocked(getApplicationContext()) == 0)) {
                    Snackbar.make(motherView, "You have been blocked by the admin.", Snackbar.LENGTH_LONG).show();
                    return null;
                }
               /* return ChatFragment.newInstance(Utilities.getSN(getApplicationContext()),
                        UtilitiesAdi.giveMeSN(getApplicationContext(), Utilities.getDatabaseName(getApplicationContext())),
                        Utilities.getInstitution(getApplicationContext()));*/
                return new StudentChatListFragment();
            case 6: {
                if (!(Utilities.getBlocked(getApplicationContext()) == 0)) {
                    Snackbar.make(motherView, "You have been blocked by the admin.", Snackbar.LENGTH_LONG).show();
                    return null;
                }
                return ChatFragment.newInstance(Utilities.getSN(getApplicationContext()),
                        UtilitiesAdi.giveMeSN(getApplicationContext(), Utilities.getDatabaseName(getApplicationContext())),
                        Utilities.getInstitution(getApplicationContext()));
            }
            default:
                return null;
        }
    }

    public Fragment AdminCase(int position) {
        hideSearch();
        switch (position) {
            case 0:
                return new IntroductionFragment();
            case 1:
                return new NoticeListFragment();
            case 2:
                return new EventsFragment();
            case 3:
                return new GetAppointmentFragment();
            case 4:
                return new ChatListFragment();
            default:
                return null;
        }
    }

    public Bitmap getCircularImage(Bitmap b, int radius) {
        return RoundedImageView.getCroppedBitmap(b, radius);
    }

    private void checkType(String account, ImageView imageView) {
        switch (account) {
            case "reception":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                break;
            case "super-admin":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_manager));
                break;
            case "sub-admin":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_man));
                break;
            default:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_student));
        }
    }

    public void updateHeader() {
        String account_type = getSharedPreferences("account", Context.MODE_PRIVATE).getString("type", "");
        imageView = header.findViewById(R.id.admin);
        fullname = header.findViewById(R.id.name);
        username = header.findViewById(R.id.username);
        circle_tv = header.findViewById(R.id.circle_head_tv);
        circle_tv.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        checkType(account_type, imageView);
        Toast.makeText(app, "" + account_type, Toast.LENGTH_SHORT).show();

        if (!Utilities.getUsername(this).isEmpty()) {
            if (type == STUDENT) {
                circle_tv.setText(Utilities.getFullname(this).substring(0, 1));
                username.setText(Utilities.getUsername(this));
                fullname.setText(Utilities.getFullname(this));
            } else {
                fullname.setText(account_type.toUpperCase());
                username.setText("R & K Associates Edu. Consultancy");
            }

            if (Utilities.getProfilePicThumbnailLoaded(getApplicationContext())) {
                Bitmap b = Utilities.loadImageFromStorage(getApplicationContext(), Utilities.getUsername(getApplicationContext()) + "thumbnail.jpg");
                Bitmap blurred = b;// fastblur(b,1,20);
//                header.setBackground(new BitmapDrawable(blurred));
                adapter.notifyDataSetChanged();
            } else {
                ImageRequest imgRequest = new ImageRequest(Utilities.encodeLinkSpace(Utilities.getCoverPicURL(getApplicationContext())),
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap loadedImage) {
                                //  Bitmap b=getCircularImage(loadedImage, SIZE);
                                Bitmap blurred = loadedImage;//fastblur(loadedImage,1,20);
                                BitmapDrawable bd = new BitmapDrawable(blurred);
//                                header.setBackground(bd);
                                adapter.notifyDataSetChanged();
                                Utilities.saveToInternalStorage(getApplicationContext(), loadedImage, Utilities.getUsername(getApplicationContext()) + "thumbnail.jpg");
                                Utilities.setProfilePicThumbnailLoaded(getApplicationContext(), true);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                Volley.newRequestQueue(getApplicationContext()).add(imgRequest);
            }
//            instName.setText(Utilities.getInstitution(this));
        } else {
            fullname.setText("Sign in for full access");
//            userNameUser.setText("");
            fullname.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(NavDrawerActivity.this, StudentInActivity.class));
                        }
                    }
            );
//            userNameUser.setClickable(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHeader();
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        // fcmToken.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        //Log.d("TAG", "onCreate: "+token);
        app = NavDrawerActivity.this;
        drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.animation);
        setContentView(R.layout.activity_tab);
        motherView = findViewById(R.id.motherLayout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav);
        searchEditText = toolbar.findViewById(R.id.search_et);
        setSupportActionBar(toolbar);
        SharedPreferences loginTypeSP = this.getApplicationContext().getSharedPreferences("type", Context.MODE_PRIVATE);
        type = loginTypeSP.getInt("type", 2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mTitle = mDrawerTitle = getTitle();
        navList = findViewById(R.id.nav_listView);
        header = getLayoutInflater().inflate(R.layout.material_nav_header, null);
        navlayout = findViewById(R.id.left_drawer);
        sign_out = findViewById(R.id.logout);
        change_pd = findViewById(R.id.change_pd);

        navDrawerItems = new ArrayList<>();
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);

        final String code = getSharedPreferences(EnterCodeFragment.SCHOOL_CODE_PREF, MODE_PRIVATE).getString(EnterCodeFragment.SCHOOL_CODE, "");
//        if (!code.equals(""))
//            tvpOFSchoolCode.setText("Access Code: " + code);
//        else
//            tvpOFSchoolCode.setVisibility(View.GONE);
//        logout.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        logout();
//                    }
//                }
//        );

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(app);
                builder1.setTitle("Log out");
                builder1.setMessage("Are you sure?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                signout();
                                startActivity(new Intent(NavDrawerActivity.this, Splash.class));
                                finish();
                                NewNoticeService.setServiceAlarm(NavDrawerActivity.this, false);
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        change_pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                FragmentManager f = getSupportFragmentManager();
                ChangePasswordDialog c = new ChangePasswordDialog();
                c.show(f, "changepw");
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,//nav menu toggle icon
                R.string.drawer_open, R.string.drawer_close// nav drawer open - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        navList.addHeaderView(header);
        navList.setOnItemClickListener(new SlideMenuClickListener());
        navList.setAdapter(adapter);
        sp = getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(NewNoticeService.HAS_SENT_NOTIFICATION, false).commit();
        sp.edit().putInt(NewNoticeService.NOTICE_NUMBER_OF_LAST_NOTIFICATION, 0);
        isNoticeNotificationOn = sp.getBoolean(SettingsFragment.SETTINGS_NOTICE, true);
        if (!NewNoticeService.isServiceAlarmOn(getApplicationContext()) && isNoticeNotificationOn) {
            NewNoticeService.setServiceAlarm(getApplicationContext(), true);
        }
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //  navManagerAdmin();

        if (type == ADMIN) {
            navManagerAdmin();
        } else if (type == STUDENT) {
            navManagerStudent();
        }

        // else this.finish();
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            if (type == ADMIN) {
                setTitle(R.string.app_name);
                IntroductionFragment fragment = new IntroductionFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            } else if (type == STUDENT) {
                setTitle(R.string.app_name);
                IntroductionFragment fragment = new IntroductionFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        }
        // navMenuIcons.recycle();
        if (getIntent() != null) {
            try {
                if (getIntent().getStringExtra(MyFirebaseMessagingService.ACTION).equals(MyFirebaseMessagingService.ACTION_CHAT)) {
                    //Log.d("TAG", "onCreate: got");
                    Fragment fragment;
                    if (type == STUDENT)
                        fragment = ChatFragment.newInstance(Utilities.getSN(getApplicationContext()), UtilitiesAdi.giveMeSN(getApplicationContext(), Utilities.getDatabaseName(getApplicationContext())), Utilities.getInstitution(getApplicationContext()));
                    else if (type == ADMIN)
                        fragment = new ChatListFragment();
                    else
                        fragment = new BlankFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                } else if (getIntent().getStringExtra(MyFirebaseMessagingService.ACTION).equals(MyFirebaseMessagingService.ACTION_CHAT_UTU)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, UserToUserChatFragment.newInstance(getIntent().getStringExtra("sent_by"), getIntent().getStringExtra("senderName"))).addToBackStack(null).commit();

                }
            } catch (Exception e) {
                //Log.d(TAG, "onCreate: "+e.toString());
            }
//            boolean c = sp.getBoolean("abcd", false);
//            if (c) {
//                sp.edit().putBoolean("abcd", false).commit();
//                selectItem(1);
//            }
//            boolean d = sp.getBoolean("susankya", false);
//            if (d) {
//                sp.edit().putBoolean("susankya", false).commit();
//                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SusankyaNotice()).addToBackStack(null).commit();
//            }
        }

     /*   onNotice = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
          menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_chat_alert));
            }
        };
       registerReceiver(onNotice, new IntentFilter(MyFirebaseMessagingService.BROADCAST_ACTION_activity));*/
    }

    void navManagerStudent() {
        mNavTiles = getResources().getStringArray(R.array.nav_tiles_student);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons_student);

        for (int i = 0; i < mNavTiles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(mNavTiles[i], navMenuIcons.getResourceId(i, -1)));
        }
        updateHeader();
    }

    void navManagerAdmin() {


        mNavTiles = getResources().getStringArray(R.array.nav_tiles_admin);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons_admin);

        for (int i = 0; i < mNavTiles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(mNavTiles[i], navMenuIcons.getResourceId(i, -1)));
        }
        updateHeader();
    }

    private void changeFontColor(int index) {
        navList.getAdapter().getItem(index);
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position,
                                long id) {

            if (!(position < 0)) {
                selectItem(position);
            }
            mDrawerLayout.closeDrawer(navView);
        }
    }

    public boolean isLoggedIn() {
        return sp.getBoolean("loggedin", false);
    }

    public void setArrayList(ArrayList<notice> notices) {
        sharedNotices = notices;
    }

    public static ArrayList<notice> getArrayList() {
        return sharedNotices;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;// OPEN DRAWER
            }
            case R.id.action_chat: {
                //menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_chat));
                try {
                    //unregisterReceiver(onNotice);
                } catch (Exception e) {
                    //Log.d(TAG, "onOptionsItemSelected: "+e.toString());
                }
                if (!(Utilities.getBlocked(getApplicationContext()) == 0)) {
                    Snackbar.make(motherView, "You have been blocked by the admin.", Snackbar.LENGTH_LONG).show();

                    return false;
                }
                Fragment fragment;
                if (type == STUDENT)
                    fragment = ChatFragment.newInstance(Utilities.getSN(getApplicationContext()), UtilitiesAdi.giveMeSN(getApplicationContext(), Utilities.getDatabaseName(getApplicationContext())), Utilities.getInstitution(getApplicationContext()));
                else if (type == ADMIN)
                    fragment = new ChatListFragment();
                else
                    fragment = new BlankFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                return true;
            }

            case R.id.about:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new AboutUsFragment()).addToBackStack(null).commit();
                return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.nima_menu, menu);
        return true;
    }


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//        menu.findItem(R.id.profile).setIcon(
//                new IconDrawable(this, IoniconsIcons.ion_ios_contact_outline)
//                        .colorRes(R.color.white)
//                        .actionBarSize());
        // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    public Animation getAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
        return animation;
    }

    /**
     * Swaps fragments in the main content view
     */
    public void onSectionAttached(int number) {
        mTitle = mNavTiles[number];
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
        if (isShowingSearchEditText()) {
            searchEditText.setVisibility(View.GONE);
            searchEditText.setText("");
            isShowingSearchEditText = false;
            return;
        } else {
        }
//            searchEditText.setText("");
        //Toast.makeText(getApplicationContext(),"Pressed",Toast.LENGTH_SHORT).show();
        FragmentManager fm = getSupportFragmentManager();
        if (!getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).getBoolean("isloggedin", false))
            this.finish();
        if (fm.getBackStackEntryCount() == 1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                this.finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else
            super.onBackPressed();
    }

    private void checkSms() {
        final ProgressDialog progressDialog = new ProgressDialog(NavDrawerActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        Nimainterface nimainterface = ImmortalApplication.smsRetrofit().create(Nimainterface.class);
        nimainterface.creditLeft("UuDqRQDLkAHXNRrC0S6v").enqueue(new Callback<Sms>() {
            @Override
            public void onResponse(Call<Sms> call, retrofit2.Response<Sms> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawerActivity.this);
                    builder.setMessage("Total credits consumed: " + response.body().getCredits_consumed() + "\nTotal credits remained: " + response.body().getCredits_available())
                            .setTitle("Your SMS Credit")
                            .setNegativeButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<Sms> call, Throwable t) {

            }
        });
    }

    private void signout() {
        getSharedPreferences("account", Context.MODE_PRIVATE).edit().clear().commit();
        getSharedPreferences(EnterCodeFragment.SCHOOL_CODE_PREF, Context.MODE_PRIVATE).edit().
                putString(EnterCodeFragment.SCHOOL_CODE, "").commit();
        File file = app.getFilesDir();
        File[] files = file.listFiles();
        for (File fileTodelete : files) {
            //Log.d("TAG", "clearData: "+fileTodelete.delete()+" "+fileTodelete.getAbsolutePath());
        }
        getSharedPreferences(LOGGED_OUt, Context.MODE_PRIVATE).edit().putBoolean(HASLOGGED_OUt, true).commit();
        getSharedPreferences("visitedOnce", Context.MODE_PRIVATE).edit().putBoolean("visited", false).commit();
        getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).edit().putBoolean("isloggedin", false).commit();
        Utilities.saveUserInfo("", "", 0, "", "", "", app);
        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("rememberlogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putBoolean("isloggedin", false);
        editor1.commit();
        getSharedPreferences("userinfo", Context.MODE_PRIVATE).edit().clear().commit();
        //Toast.makeText(getActivity(),"Welcome, "+userInfo.getFullName(),Toast.LENGTH_SHORT).show();
        Intent i = new Intent(app, NavDrawerActivity.class);
        SharedPreferences loginTypeSP = getApplicationContext().getSharedPreferences("type", Context.MODE_PRIVATE);
        SharedPreferences.Editor loginTypeET = loginTypeSP.edit();
        loginTypeET.clear();
        loginTypeET.commit();
        //  i.putExtra("usernumberho",userNum);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("dbName", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.clear();
        e.commit();
        SharedPreferences sps = getApplicationContext().getSharedPreferences("visitedOnce", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor11 = sps.edit();
        editor11.putBoolean("visited", false);
        editor11.putBoolean("loggedIn", false);
        editor11.commit();
        SharedPreferences routineSP = getApplicationContext().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        routineSP.edit().clear().commit();
        Utilities.setProfilePicThumbnailLoaded(app, false);
        UtilitiesAdi.setProfileLoaded(app, false);
        app.deleteDatabase(FragmentCodes.DATABASE);
    }
}
