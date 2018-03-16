package com.susankya.rnk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


public class FragmentForHome extends android.support.v4.app.Fragment implements FragmentCodes {
    private FragmentTabHost tabHost;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private FragmentTabHost fragmentTabHost;
   int   mParam1;
    private String mParam2;


    public static FragmentForHome newInstance(int param1, String param2) {
        FragmentForHome fragment = new FragmentForHome();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentForHome() {
        mParam1=0;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NavDrawerActivity) getActivity()).onSectionAttached(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1,0);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View indicator=null;
        try {

            //fragmentTabHost.setBackgroundColor(R.drawable.drawerlightblue);

            indicator = inflater.inflate(R.layout.fragment_fragment_for_home, fragmentTabHost, false);
            fragmentTabHost = (FragmentTabHost) indicator.findViewById(android.R.id.tabhost);
            fragmentTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabcontentS);
            // if(Build.VERSION.SDK_INT>=17)
            try
            {
                fragmentTabHost.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.blue_grey_drawable));
            }

            catch(Exception e)
            {

            }
           // NavDrawerActivity.setIsFromHome(true);
         //fragmentTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selector);
            fragmentTabHost.addTab(setIndicator(getActivity(),fragmentTabHost.newTabSpec("Routine"), R.drawable.ic_action_routine,"Routine"),ViewRoutinePagerFragment.class,null);
            fragmentTabHost.addTab(setIndicator(getActivity(),fragmentTabHost.newTabSpec("View Result"), R.drawable.ic_action_result,"Saved Result"),IS_PAID_VERSION?showResultListOffline.class:UnpaidFragment.class,null);
             fragmentTabHost.addTab(setIndicator(getActivity(),fragmentTabHost.newTabSpec("View Question"), R.drawable.ic_action_question,"Saved Ques."),IS_PAID_VERSION?questionsListOffline.class:UnpaidFragment.class,null);
            fragmentTabHost.addTab(setIndicator(getActivity(),fragmentTabHost.newTabSpec("More Stuff"), R.drawable.ic_action_call,"Contacts"),MakeCalls.class,null);
            fragmentTabHost.setCurrentTab(mParam1);
      fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {

                }
            });

        }
        catch (Exception e){

        }

        return indicator;
    }
    public TabHost.TabSpec setIndicator(Context c, TabHost.TabSpec spc,int id,String label){
        View v=getActivity().getLayoutInflater().from(c).inflate(R.layout.trial,null);
   v.setBackgroundResource(R.drawable.tab_selector);
       ImageView imv=(ImageView)v.findViewById(R.id.image);
        TextView tv=(TextView)v.findViewById(R.id.label);
        tv.setText(label);
        imv.setBackgroundResource(id);
       return spc.setIndicator(v);
    }
}
