package com.susankya.schoolvalley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Splash extends AppCompatActivity implements FragmentCodes {
    private final int TIME=1500;
    String link;
    public String[] collegeNames,databaseNames;
    ArrayList<String> collegeNameArray=new ArrayList<>(),databaseNameArray=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        if (getSharedPreferences("visitedOnce",MODE_PRIVATE).getBoolean("visited",false)&&
        getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).getBoolean("isloggedin",false))
        {
             Intent i = new Intent(Splash.this, NavDrawerActivity.class);
            startActivity(i);
            finish();

        }
        else if(getSharedPreferences("visitedOnce",MODE_PRIVATE).getBoolean("visited",false)
                && !getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).getBoolean("isloggedin",false))
        {
            Intent i = new Intent(Splash.this, IntroActivity.class);
            startActivity(i);
            finish();

        }

        else
        {
            link="http://46.101.81.232/App/New%20App/Android%20Call/Call%20Main%20Database/institutionNames.php";

            final SharedPreferences sp=getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
            ImageView image=(ImageView)findViewById(R.id.image);
            //getCollegeList();
            Intent i = new Intent(Splash.this, IntroActivity.class);
            startActivity(i);
            finish();
            Animation animation= AnimationUtils.loadAnimation(this, R.anim.splash);
            image.setAnimation(animation);
        }


    }

    Context getActivity()
    {
        return  this;
    }
    public void getLevelList()
    {
        String link="http://46.101.81.232/App/New App/ChhalFaal/Profile/level.php";
        link= Utilities.makeUrl(link);
        new PhpConnect(link,"",getActivity(),0,new String[]{CMDXXX},new String[]{"cmdxxx"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
               // Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray JA = (JSONArray) new JSONTokener(res).nextValue();
                   // Toast.makeText(getActivity(),JA.length()+"",Toast.LENGTH_SHORT).show();
                    String[] strings = new String[JA.length()];
                    for (int i = 0; i < JA.length(); i++) {
                        strings[i] = JA.getString(i);
                    }
                    Set set = new HashSet(Arrays.asList(strings));
                    getActivity().getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE).edit().putStringSet("levels", set).apply();
                    getActivity().getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE).edit().putBoolean("hasGotLevel", true).apply();

                    Intent i = new Intent(Splash.this, IntroActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    //Log.d("asd",e+"");
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getCollegeList()
    {
        String link="http://46.101.81.232/App/New%20App/Android%20Call/Call%20Main%20Database/institutionNames.php";
        new PhpConnect(link,"Please wait...",this,0,new String[]{"2568wwexve"},new String[]{"cmdxxx"}).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {

                        UtilitiesAdi.saveJSON(getApplicationContext(), res, "collegelist");
                        UtilitiesAdi.setBoolean(getApplicationContext(), FragmentCodes.COLLEGE_LIST_LOADED, true);
                        // Toast.makeText(StudentInActivity.this,res,Toast.LENGTH_LONG).show();
                        //Log.d("cmdxxx", res);
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


                            //getLevelList();


                        } catch (Exception e) {

                            Toast.makeText(Splash.this, e+"", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }



}
