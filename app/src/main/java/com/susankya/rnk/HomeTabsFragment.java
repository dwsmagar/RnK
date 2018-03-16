package com.susankya.rnk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.susankya.rnk.Fragments.EventsFragment;
import com.susankya.rnk.Fragments.IntroductionFragment;
import com.susankya.rnk.Fragments.NeemaProfileFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeTabsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //    private String tabTitles[] = new String[]{"Notice", "Routine", "Result", "Questions"};
    public String[] tabsName = new String[]{"Nimas Edu.", "Notices", "Events", "Neema's Profile"};
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int imageResId[] =
            new int[]
                    {
                            R.drawable.home_icon,
                            R.drawable.notice_icon,
                            R.drawable.event_icon,
                            R.drawable.profile_icon,
                    };

    private int bigImageResId[] = new int[]
            {
                    R.drawable.big_home_icon,
                    R.drawable.big_notice_icon,
                    R.drawable.big_event_icon,
                    R.drawable.big_profile_icon
            };

    private Drawable[] blackPngs, greyPngs;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager pager;
    Fragment fragmentAt1;

    public HomeTabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename and change types and number of parameters
    public static HomeTabsFragment newInstance(String param1, String param2) {
        HomeTabsFragment fragment = new HomeTabsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static SampleFragmentPagerAdapter s;
    public ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_tabs, container, false);
        ButterKnife.bind(this, v);
        fragments.add(new IntroductionFragment());
        fragments.add(new NoticeListFragment());
        fragments.add(new EventsFragment());
        fragments.add(new NeemaProfileFragment());
        s = new SampleFragmentPagerAdapter(getChildFragmentManager(), getActivity().getApplicationContext(), fragments);
        pager.setAdapter(s);
        pager.setCurrentItem(0, true);
        blackPngs = new Drawable[bigImageResId.length];
        greyPngs = new Drawable[imageResId.length];

        for (int i = 0; i < bigImageResId.length; i++) {
            int color = Color.parseColor("#000000");
            PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
            Drawable d = ContextCompat.getDrawable(getActivity(), bigImageResId[i]);
            d.setColorFilter(color, mMode);
            blackPngs[i] = d;
        }

        for (int i = 0; i < imageResId.length; i++) {
            int color = getActivity().getResources().getColor(R.color.grey_light);
            PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
            Drawable d = ContextCompat.getDrawable(getActivity(), imageResId[i]);
            d.setColorFilter(color, mMode);
            greyPngs[i] = d;
        }

        tabLayout.setupWithViewPager(pager);
        for (int i = 0; i < greyPngs.length; i++)
            tabLayout.getTabAt(i).setIcon(greyPngs[i]).setCustomView(R.layout.custom_tab);

        tabLayout.getTabAt(0).setIcon(blackPngs[0]);
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tab.setIcon(blackPngs[tab.getPosition()]);
                        getActivity().setTitle(tabsName[tab.getPosition()]);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        tab.setIcon(greyPngs[tab.getPosition()]);
//                        tab.getIcon().setColorFilter(getActivity().getResources().getColor(R.color.grey_light), PorterDuff.Mode.SRC_ATOP);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );
        return v;
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 4;
        public ArrayList<Fragment> fs;
        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> fragmentArrayList) {
            super(fm);
            this.context = context;
            fs = fragmentArrayList;
        }

        @Override
        public int getCount() {
            return tabsName.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fs.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
           /* Drawable image =blackPngs[position];
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            int color = Color.parseColor("#000000"); //The color u want
           // image.setColorFilter(color,null);
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;*/
            return "";
            // Generate title based on item position
        }
    }
}
