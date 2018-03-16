package com.susankya.rnk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;


public class NewSettingsFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> list;
    private ListView lv;
    private String mParam1;
    private String mParam2;
    public static final String LOGGED_OUt = "loggedout";
    public static final String HASLOGGED_OUt = "hasloggedout";

    private RelativeLayout logoutRL;
    public static String mFilename = "Notices";

    public static NewSettingsFragment newInstance(String param1, String param2) {
        NewSettingsFragment fragment = new NewSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Settings");
    }

    public NewSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("hello");
        }
    }
    View changePWRL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.settingsfragment, container, false);
        logoutRL = (RelativeLayout) v.findViewById(R.id.logoutRL);
        changePWRL = v.findViewById(R.id.changepwRL);
        changePWRL.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager f = getChildFragmentManager();
                        ChangePasswordDialog c = new ChangePasswordDialog();
                        c.show(f, "changepw");
                    }
                }
        );
        logoutRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Log out");
                builder1.setMessage("Are you sure?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                clearData();
                                startActivity(new Intent(getActivity(), Splash.class));
                                getActivity().finish();
                                NewNoticeService.setServiceAlarm(getActivity(), false);
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
        return v;
    }

    void clearData() {
        getActivity().getSharedPreferences(EnterCodeFragment.SCHOOL_CODE_PREF, Context.MODE_PRIVATE).edit().
                putString(EnterCodeFragment.SCHOOL_CODE, "").commit();
        File file = getActivity().getFilesDir();
        File[] files = file.listFiles();
        for (File fileTodelete : files) {
            //Log.d("TAG", "clearData: "+fileTodelete.delete()+" "+fileTodelete.getAbsolutePath());
        }
        getActivity().getSharedPreferences(LOGGED_OUt, Context.MODE_PRIVATE).edit().putBoolean(HASLOGGED_OUt, true).commit();
        getActivity().getSharedPreferences("visitedOnce", Context.MODE_PRIVATE).edit().putBoolean("visited", false).commit();
        getActivity().getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).edit().putBoolean("isloggedin", false).commit();
        Utilities.saveUserInfo("", "", 0, "", "", "", getActivity());
        SharedPreferences sharedPreferences1 = getActivity().getApplicationContext().getSharedPreferences("rememberlogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putBoolean("isloggedin", false);
        editor1.commit();
        getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE).edit().clear().commit();
        //Toast.makeText(getActivity(),"Welcome, "+userInfo.getFullName(),Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getActivity(), NavDrawerActivity.class);

        SharedPreferences loginTypeSP = getActivity().getApplicationContext().getSharedPreferences("type", Context.MODE_PRIVATE);
        SharedPreferences.Editor loginTypeET = loginTypeSP.edit();
        loginTypeET.clear();
        loginTypeET.commit();
        //  i.putExtra("usernumberho",userNum);

        SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("dbName", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.clear();
        e.commit();
        SharedPreferences sps = getActivity().getApplicationContext().getSharedPreferences("visitedOnce", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor11 = sps.edit();
        editor11.putBoolean("visited", false);
        editor11.putBoolean("loggedIn", false);
        editor11.commit();
        SharedPreferences routineSP = getActivity().getApplicationContext().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        routineSP.edit().clear().commit();
        Utilities.setProfilePicThumbnailLoaded(getContext(), false);
        UtilitiesAdi.setProfileLoaded(getActivity(), false);
        getContext().deleteDatabase(FragmentCodes.DATABASE);
    }

    private class adapt extends ArrayAdapter<String> {
        private ArrayList<String> profile;

        public adapt(ArrayList<String> c) {
            super(getActivity(), 0, c);
            profile = c;
        }

        @Override
        public View getView(final int pos, View v, ViewGroup vg) {
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.result_row, null);
            }

            return v;
        }

    }
    /*        if (getSharedPreferences("visitedOnce",MODE_PRIVATE).getBoolean("visited",false)&&
        getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).getBoolean("isloggedin",false))*/
}

