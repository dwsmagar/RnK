package com.susankya.rnk;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.UUID;


public class noticePagerFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2="asdasd";
    private ViewPager pager;
    private ArrayList<notice> mNotices;
    private UUID result_title;
    private int pos;
    private String mParam1;
    private int layout_id;
    private notice n=null;
    private Button edit,delete;
    public static noticePagerFragment newInstance(String param1) {
        noticePagerFragment fragment = new noticePagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    public noticePagerFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
                layout_id= R.layout.activity_pager;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(layout_id, container, false);
        mNotices=new ArrayList<notice>();
        mNotices= NavDrawerActivity.getArrayList();
      /*  for(int i=0;i<10;i++){
            notice n=new notice();
            mNotices.add(n);
        }*/
        pager=(ViewPager)v.findViewById(R.id.pager);
        pager.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        pos=position;

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
       /* result_title=(String) getIntent().getSerializableExtra(Main.TITLE);
        pos=(int)getIntent().getSerializableExtra(Main.POSITION);*/
        FragmentManager fm=getFragmentManager();
        pager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                 n=mNotices.get(position);
                return noticeFragment.newInstance(n.getTitle());
            }

            @Override
            public int getCount() {
                return mNotices.size();// mCrime.size();
            }
        });
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
getActivity().setTitle(mNotices.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       /* result_title=(UUID) getIntent().getSerializableExtra(NoticeListFragment.NOTICE_NUM);
              */
        result_title=UUID.fromString(mParam1);
        for (int i = 0; i < mNotices.size(); i++) {
            if (mNotices.get(i).getId().toString().equals(result_title.toString()) ){
                {
                    pos=i;
                    pager.setCurrentItem(i);
                   notice n=mNotices.get(i);
                   getActivity().setTitle(n.getTitle());
                }

                break;
            }
        }

        return  v;
    }





}
