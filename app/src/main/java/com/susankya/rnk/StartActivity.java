package com.susankya.rnk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends AppCompatActivity {

    private ViewPager mPager;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_action_routine,
            R.drawable.ic_action_notice,
    };

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "Log In");
        Bundle b = new Bundle();
        b.putString("from", "StartActivity");

        Fragment f = new FirstSignUpFragment();
        f.setArguments(b);
        adapter.addFragment(f, "Sign Up");
        viewPager.setAdapter(adapter);
    }

    public String USERNAME, PASSWORD;
    Fragment fragment;

    public void changeTab(int pos, UserInfo userInfo) {

        USERNAME = userInfo.getUserName();
        PASSWORD = userInfo.getPassword();
        LoginFragment.mUsername.setText(USERNAME);
        LoginFragment.mPassword.setText(PASSWORD);
        mPager.setCurrentItem(pos);

    }

    private void setupTabIcons() {
        for (int i = 0; i < tabIcons.length; i++) {
            //tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }

    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("rememberlogin", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isloggedin", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (isLoggedIn()) {
            String userFullName = getSharedPreferences("userinfo", MODE_PRIVATE).getString("fullName", "0");
            Utilities.toaster("Welcome, " + userFullName, Toast.LENGTH_LONG, StartActivity.this);


            Intent i = new Intent(this, NavDrawerActivity.class);
            i.putExtra("usernumberho", getSharedPreferences("userinfo", MODE_PRIVATE).getInt("number", 0));
            startActivity(i);
            finish();
        }
        mPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
        setupTabIcons();
        // mPager.setAdapter(new SwipePagerAdapter(getSupportFragmentManager()));
        mPager.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i2) {

                    }

                    @Override
                    public void onPageSelected(int i) {
                        // actionBar.setSelectedNavigationItem(i);
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                }
        );

        try {
            // actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        // Create a tab listener that is called when the user changes tabs.
        android.support.v7.app.ActionBar.TabListener tabListener = new android.support.v7.app.ActionBar.TabListener() {

            public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                mPager.setCurrentItem(tab.getPosition());

            }

            public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {

                // hide the given tab
            }

            public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 2; i++) {
           /* actionBar.addTab(
                    actionBar.newTab()
                            .setText(FragmentCodes.TITLES[i])
                            .setTabListener(tabListener));*/
        }
    }
}

