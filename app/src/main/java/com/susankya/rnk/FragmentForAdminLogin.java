package com.susankya.rnk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


public class FragmentForAdminLogin extends android.support.v4.app.Fragment implements FragmentCodes {
    private FragmentTabHost tabHost;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String SER_EXTRA="EXtra";
    public static FragmentTabHost fragmentTabHost;
    private String mParam1;
    private String mParam2;
private SharedPreferences sp;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
    }
    public void logout()
    {
        new AlertDialog.Builder(getActivity()).
                setTitle("Log out")
                .setMessage("Do you really want to log out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                        "Log out",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove("username");
                                editor.remove("password");
                                editor.putBoolean("loggedin",false);
                                editor.commit();
                                ((NavDrawerActivity)getActivity()).setLoggedIn(false);
                                getFragmentManager().beginTransaction().replace(R.id.content_frame,new AdminLoginFragment()).addToBackStack(null).commit();
                            }
                        }
                ).setNegativeButton(
                "Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        ).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.menu_logout:
            {
             logout();
            }
            default:
                return false;
        }
    }

    public static FragmentForAdminLogin newInstance(String param1, String param2) {
        FragmentForAdminLogin fragment = new FragmentForAdminLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentForAdminLogin() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
      NavDrawerActivity.setZugad(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NavDrawerActivity.setZugad(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ((NavDrawerActivity)getActivity()).setZugad(1);
        sp=getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE,Context.MODE_PRIVATE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View indicator=null;
        try {

            //fragmentTabHost.setBackgroundColor(R.drawable.drawerlightblue);

            indicator= inflater.inflate(R.layout.fragment_fragment_for_home,fragmentTabHost, false);
            ((TextView)indicator.findViewById(R.id.name)).setText(sp.getString("collegeName",""));
            fragmentTabHost=(FragmentTabHost)indicator.findViewById(android.R.id.tabhost);
            fragmentTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabcontentS);
            fragmentTabHost.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.blue_grey_drawable));
            //fragmentTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selector);
            fragmentTabHost.addTab(setIndicator(getActivity(),fragmentTabHost.newTabSpec("Add Notice"), R.drawable.ic_action_notice_white,"Add Notice"),addNoticeFragment.class,null);
            fragmentTabHost.addTab(setIndicator(getActivity(),fragmentTabHost.newTabSpec("Delete Notice"), R.drawable.ic_action_deletenotice,"Delete Notice"),NoticeListFragment.class,null);
            fragmentTabHost.addTab(setIndicator(getActivity(),fragmentTabHost.newTabSpec("Ask Question Code"), R.drawable.ic_action_question_white,"Questions"),AddDeleteQuestionsFragment.class,null);
            fragmentTabHost.setCurrentTab(0);
            fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    //  Toast.makeText(getActivity(),tabId,Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }

        return indicator;
    }

    public TabHost.TabSpec setIndicator(Context c, TabHost.TabSpec spc,int id,String label){
        View v=getActivity().getLayoutInflater().from(c).inflate(R.layout.trial,null);
        v.setBackgroundResource(R.drawable.tab_selector);
        TextView tv=(TextView)v.findViewById(R.id.label);
        tv.setText(label);
        ImageView imv=(ImageView)v.findViewById(R.id.image);
        imv.setBackgroundResource(id);
        return spc.setIndicator(v);
    }
}
