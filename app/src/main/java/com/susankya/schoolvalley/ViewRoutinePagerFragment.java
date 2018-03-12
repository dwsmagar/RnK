package com.susankya.schoolvalley;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ViewRoutinePagerFragment extends android.support.v4.app.Fragment implements FragmentCodes {


    private int numberOfPages = 6;
    public static ViewPager viewPager;
    int day;
    private TabLayout tabLayout;
    private Toolbar mToolbar;
    private ImageView backImageView;
    static TextView SectionView,SectionViewFooter;
   public static ViewPagerAdapter adapter;
    private static SharedPreferences sp;
    SQLiteHelper sqLiteHelper;
 private static String section;
   static Button pre,next;
    public static int currentPage;
    public static ViewRoutineFragment.DownloadRoutine dialog;
    static String[] days=new String[]{"","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","",""};
    private static SQLiteDatabase db;
    public static ViewRoutinePagerFragment newInstance(int pos)
    {
        ViewRoutinePagerFragment fragment=new ViewRoutinePagerFragment();
        Bundle args=new Bundle();
        args.putInt("pos",pos);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onResume() {
        super.onResume();
        //updateAdapter();
        try {
            if (isVisible() && NavDrawerActivity.shouldresume)
                setupViewPager(viewPager);
            NavDrawerActivity.shouldresume = false;
        }
    catch (Exception e){
     //   Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
    }
    }

    public String fromListSection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar = Calendar.getInstance();

   day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        sqLiteHelper=new SQLiteHelper(getActivity());
        db=sqLiteHelper.getWritableDatabase();
sp=getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        try{
            if(!sp.getBoolean(IS_DATABASE_CREATED,false)){
                db.execSQL("create table if not exists "+TABLENAME+" ("+DAY+" varchar(50), "+PERIOD_NO+" varchar(50), "+SUBJECT+" varchar(50), "+SECTION_NAME+" varchar(50), "+
                        TEACHER_NAME+" varchar(50), "+START+" varchar(50), "+END+" varchar(50), "+COLLEGE_SN+" varchar(50)); ");
        sp.edit().putBoolean(IS_DATABASE_CREATED,true).apply();
            }}catch (Exception e){
         //   Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
         }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.routine_menu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
          chooseSection();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.routine_pager_fragment, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        SectionView =(TextView)v.findViewById(R.id.section);
        ((ImageView)v.findViewById(R.id.backButton)).setVisibility(View.GONE);
        SectionViewFooter =(TextView)v.findViewById(R.id.sectionFooter);
        final SharedPreferences sp=getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        section=sp.getString(ViewRoutineFragment.SECTION, "No section selected");
        if (!section.equals("No section selected"))
            SectionViewFooter.setText("Change section");
        SectionView.setText(section.toUpperCase());
        setupViewPager(viewPager);
        tabLayout = (TabLayout)v.findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        viewPager.setCurrentItem(day);
        viewPager.getAdapter().notifyDataSetChanged();
           viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
               @Override
               public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

               }
               @Override
               public void onPageSelected(int position) {
                   currentPage=position;
                   NavDrawerActivity.currentRoutine=position;
               }

               @Override
               public void onPageScrollStateChanged(int state) {

               }
           });
        SectionView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utilities.getBlocked(getActivity().getApplicationContext())==0)
                            chooseSection();
                        else
                            Snackbar.make(v,"You have been blocked by the admin.",Snackbar.LENGTH_LONG).show();
                    }
                }
        );
        SectionViewFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utilities.getBlocked(getActivity().getApplicationContext())==0)
               chooseSection();
                else
                    Snackbar.make(v,"You have been blocked by the admin.",Snackbar.LENGTH_LONG).show();


            }
        });

         return v;

    }
    public  static void changeName(String str){
        SectionView.setText(str);
    }

    public  static void dishmis(){
        ArrayList<RoutineInfo> r= adapter.getItem(currentPage).insertIntoView(currentPage+1);
        ((ViewRoutineFragment)adapter.getItem(currentPage)).routineInfoArrayList=r;
        ((ViewRoutineFragment)adapter.getItem(currentPage)).RoutineAdapter.notifyDataSetChanged();
    }

    public static void updateAdapter()
    {
        viewPager.getAdapter().notifyDataSetChanged();
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<ViewRoutineFragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public ViewRoutineFragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(ViewRoutineFragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private  void setupViewPager(ViewPager viewPager) {
         adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ViewRoutineFragment.newInstance(0), "Sunday");
        adapter.addFragment(ViewRoutineFragment.newInstance(1), "Monday");
        adapter.addFragment(ViewRoutineFragment.newInstance(2), "Tuesday");
        adapter.addFragment(ViewRoutineFragment.newInstance(3), "Wednesday");
        adapter.addFragment(ViewRoutineFragment.newInstance(4), "Thursday");
        adapter.addFragment(ViewRoutineFragment.newInstance(5), "Friday");
        adapter.addFragment(ViewRoutineFragment.newInstance(6), "Saturday");
        viewPager.setAdapter(adapter);
        viewPager.getAdapter().notifyDataSetChanged();
    }


    void chooseSection()
    {
        if (Utilities.isConnectionAvailable(getActivity())) {
            android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
            dialog = new
                    ViewRoutineFragment.DownloadRoutine();
            dialog.show(fragmentManager, "SHOWING");

        } else {
            Toast.makeText(getActivity(), "No Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
