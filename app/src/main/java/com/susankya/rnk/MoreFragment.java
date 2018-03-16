package com.susankya.rnk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MoreFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPreferences sp;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView moreListView;
    MoreAdapter adapter;
    ArrayList<FeatureItem> mFeatures;


    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("More");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_more, container, false);
        moreListView=(ListView)v.findViewById(R.id.more_listview);
        mFeatures=new ArrayList<>();
        sp=getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String[] moretiles=getActivity().getResources().getStringArray(R.array.more_tiles);
    //   int[] drawables=new int[]{R.drawable.ic_action_adminlogin_2,R.drawable.ic_action_aboutus_1,R.drawable.ic_action_forum_2,R.drawable.ic_action_notice_1,R.drawable.ic_action_settings};
        int[] drawables=new int[]{R.drawable.ic_action_adminlogin_2,R.drawable.ic_action_aboutus_1,R.drawable.ic_action_notice_1,R.drawable.ic_action_settings};

        for(int i=0;i<moretiles.length;i++)
        {
            FeatureItem f=new FeatureItem();
            f.setImage(BitmapFactory.decodeResource(getActivity().getResources(),drawables[i]));
            f.setText(moretiles[i]);
            mFeatures.add(f);


        }

       if(UtilitiesAdi.getSP(getActivity(),"userinfo").getInt("verified",0)==3)
       {
           FeatureItem f=new FeatureItem();
           f.setImage(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.ic_action_post_ad));
           f.setText("Post Ad");
           mFeatures.add(f);
       }
        adapter=new MoreAdapter(mFeatures);
        moreListView.setAdapter(adapter);
        moreListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.Fragment fragment = null;
                        switch (position) {
                            case 0: {
                                if (isLoggedIn())
                                    fragment = new FragmentForAdminLogin();
                                else
                                    fragment = new AdminLoginFragment();
                                break;
                            }

                            case 1:
                            {
                                fragment=new AboutUsFragment();
                                //startActivity(new Intent(this,HelpActivity.class));
                                break;
                            }

                           /* case 2:
                            {
                                fragment=new ChhalfalOffline();
                                break;
                            }*/
                            case 2:
                            {
                                fragment= new SusankyaNotice();
                                break;
                            }
                            case 3:
                            {
                                fragment= new SettingsFragment();
                                break;
                            }


                        }

                        getFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                    }
                }
        );
        return  v;
    }

    public boolean isLoggedIn() {
        return sp.getBoolean("loggedin", false);
    }


    private class MoreAdapter extends BaseAdapter {

        int[] more_colors;

        ArrayList<FeatureItem> featureItems;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            }


            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.iconrow);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.text1);
            //TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

            //txtCount.setVisibility(View.GONE);
            imgIcon.setImageBitmap(featureItems.get(position).getImage());
            txtTitle.setText(featureItems.get(position).getText());
            txtTitle.setTextColor(more_colors[position]);
                      //convertView.setBackgroundColor(getActivity().getResources().getColor(R.color.divider));
            return  convertView;
        }

        MoreAdapter(ArrayList<FeatureItem> fs)
        {
            featureItems=fs;
            more_colors=getActivity().getResources().getIntArray(R.array.more_colors);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return featureItems.get(position);
        }

        @Override
        public int getCount() {
            return featureItems.size();
        }
    }
}
