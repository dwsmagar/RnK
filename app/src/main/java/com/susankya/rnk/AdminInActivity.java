package com.susankya.rnk;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class AdminInActivity extends AppCompatActivity implements FragmentCodes {

    ViewPager viewPager;
    private Button next, pre;
    private SharedPreferences sp;
    public static AdminInActivity thisAct;
    public static boolean collegeLoaded = false;

    private void manageButton() {
        if (viewPager.getCurrentItem() == 0) {
            pre.setClickable(false);
            pre.setText("");
        } else {
            pre.setClickable(true);
            pre.setText("Previous");
        }
        if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
            next.setClickable(false);
        } else
            next.setClickable(true);
    }

    public static void killThis() {
        thisAct.finish();
    }

    public String[] collegeNames, databaseNames;
    ArrayList<String> collegeNameArray = new ArrayList<>(), databaseNameArray = new ArrayList<>();
    String link = "";

    public void getCollegeList() {
        new PhpConnect(link, "Please wait...", this, 0, new String[]{"2568wwexve"}, new String[]{"cmdxxx"}).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        // Toast.makeText(StudentInActivity.this,res,Toast.LENGTH_LONG).show();
                        //Log.d("cmdxxx",res);
                        //[{"college_name":"test","database_name":"Susankyatest"},{"college_name":"susankya","database_name":"Susankyaaditya"}]
                        try {
                            JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                            collegeNames = new String[jsonArray.length()];
                            databaseNames = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String collegeName = jsonObject.getString("college_name");
                                String databaseName = jsonObject.getString("database_name");
                                collegeNames[i] = collegeName;
                                databaseNames[i] = databaseName;
                                collegeNameArray.add(collegeName);
                                databaseNameArray.add(databaseName);

                            }
                            collegeLoaded = true;

                        } catch (Exception e) {

                        }
                    }
                }
        );
    }

    public void loadCollegeList() {
        String res = UtilitiesAdi.loadJSON(getApplicationContext(), "collegelist");
        try {
            JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
            collegeNames = new String[jsonArray.length()];
            databaseNames = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String collegeName = jsonObject.getString("college_name");
                String databaseName = jsonObject.getString("database_name");
                collegeNames[i] = collegeName;
                databaseNames[i] = databaseName;
                collegeNameArray.add(collegeName);
                databaseNameArray.add(databaseName);

            }
            collegeLoaded = true;
        } catch (Exception e) {

        }

    }

    AdminInActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisAct = AdminInActivity.this;
        if (getSharedPreferences("visitedOnce", MODE_PRIVATE).getBoolean("visited", false)
                && !getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).getBoolean("isloggedin", false)

                && false) {
            startActivity(new Intent(AdminInActivity.this, StartActivity.class));
            this.finish();
        } else if (getSharedPreferences("visitedOnce", MODE_PRIVATE).getBoolean("visited", false) && getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).getBoolean("isloggedin", false)) {
            startActivity(new Intent(AdminInActivity.this, NavDrawerActivity.class));
            this.finish();
        } else {
            setContentView(R.layout.activity_first_time);
            Fragment f = new AdminFirstLoginFragment();
            Bundle b = new Bundle();
            b.putString("from", "AdminInActivity");
            f.setArguments(b);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.first_container, f).commit();
        }
    }
}
