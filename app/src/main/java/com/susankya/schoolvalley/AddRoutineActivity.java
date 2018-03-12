package com.susankya.schoolvalley;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddRoutineActivity extends AppCompatActivity {
private String sectionName;
    TextView sectionNameView,sectionNameViewFooter;
    ViewPagerAdapter adapter;
    private int lastroutine=0;
    boolean isAdded=false;
public static boolean isEdit=true;
    public static final String PERIOD_NO="period_no",START="start",END="end",TEACHER_NAME="teacher_name",SUBJECT="subject";
    public static SparseArray<List<RoutineInfo>> listSparseArray;
    public static SparseArray<List<RoutineInfo>> getListSparseArray() {
        return listSparseArray;
    }
    public static void setListSparseArray(int index,List<RoutineInfo> listSparseArray) {
        AddRoutineActivity.listSparseArray.put(index,listSparseArray);
    }public static void setSparseArray(SparseArray listSparseArray) {
        AddRoutineActivity.listSparseArray=listSparseArray;
    }
private   ProgressDialog pd;
    public static Map<Integer,List<RoutineInfo>> mapOfRoutine;
  public static   List<String> listOfRoutine;
    private TabLayout tabLayout;

    public static Map<Integer, List<RoutineInfo>> getMapOfRoutine() {
        return mapOfRoutine;
    }

    public static void setMapOfRoutine(Map<Integer, List<RoutineInfo>> mapOfRoutine) {
        AddRoutineActivity.mapOfRoutine = mapOfRoutine;
    }

    ViewPager sectionPager;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_notice_menu,menu);
        menu.findItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    private String jsonArrayString(List<RoutineInfo> routineInfos){
        JSONArray ja=new JSONArray();
        for(int i=0;i<routineInfos.size();i++){
            RoutineInfo ri=routineInfos.get(i);
            if(ri.getSubject().equals("Subject")||ri.getTeacher().equals("NA"))
                continue;
            JSONObject job=new JSONObject();
            try {
                job.put(PERIOD_NO,ri.getPeriodNo());
                job.put(TEACHER_NAME,ri.getTeacher());
                job.put(START,ri.getStart());
                job.put(END,ri.getEnd());
                job.put(SUBJECT,ri.getSubject());
                ja.put(job);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ja.toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_pager_fragment);
        if(getIntent()!=null){
            sectionName=getIntent().getStringExtra(PresentRoutineListFragment.TAG_SECTION_NAME);
            isEdit=((getIntent().getStringExtra(PresentRoutineListFragment.TAG_EDIT_VIEW)).equals(PresentRoutineListFragment.TAG_EDIT))?true:false;
        }else
            sectionName="section name";
       ((ImageView)findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(listSparseArray==null){
            //Log.d("ISNULL", "isnull ");
       listSparseArray=new SparseArray<>();
        for (int i=0;i<7;i++){
            List<RoutineInfo> ri=new ArrayList<>();
            listSparseArray.put(i, ri);
        }}else {
            for(int i=0;i<7;i++){
            //Log.d("TAG", "onCreate:"+listSparseArray.get(i).size());
        }
        }
        final  FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floating_add_button);
        fab.setVisibility(View.VISIBLE);
        listOfRoutine=new ArrayList<>();
        sectionNameView=(TextView)findViewById(R.id.section);
        sectionNameViewFooter=(TextView)findViewById(R.id.sectionFooter);
        sectionNameViewFooter.setVisibility(View.GONE);
        sectionPager=(ViewPager)findViewById(R.id.pager);
        sectionNameView.setText(sectionName.toUpperCase());
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(sectionPager);
            }
        });
       // sectionPager.getAdapter().notifyDataSetChanged();
        setupViewPager(sectionPager);
        sectionPager.setCurrentItem(0);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_notice));
        if(isEdit){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<listSparseArray.size();i++){
                        if(!jsonArrayString(listSparseArray.get(i)).equals("[]"))
                        {
                         lastroutine=i;
                        }
                    }
                    addRoutine(1);
                }
            });
        }else
        {
            fab.setVisibility(View.GONE);
        }
          sectionPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {

                //NavDrawerActivity.currentRoutine=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });

    }
    private void addRoutine(final  int day){

        if(day==1){
        pd=new ProgressDialog(AddRoutineActivity.this);
        pd.setMessage("Adding Routine...");
        pd.show();
        }
        String link;
        if (PresentRoutineListFragment.isAdd)
        link=FragmentCodes.NEW_HOST+"AddRoutine.php";
        else
        link=FragmentCodes.NEW_HOST+"AddRoutine/add.php";
        //Log.d("day", "addRoutine: "+day);
        //Log.d("link", "addRoutine: "+link);
        if(!jsonArrayString(listSparseArray.get(day-1)).equals("[]"))
        {
            int isLast=0;
            if(lastroutine==day-1)
                isLast=1;
            else
            isLast=0;
            new PhpConnect(link,"",getApplicationContext(),0,new String[]{FragmentCodes.CMDXXX,UtilitiesAdi.giveMeSN(getApplicationContext(),Utilities.getDatabaseName(getApplicationContext())),day+""
                ,sectionName,jsonArrayString(listSparseArray.get(day-1)),isLast+""}
                ,new  String[]{"cmdxxx","college_sn","day","section_name","data","isLast"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                //Log.d("TAGGG", "onConnectListener: "+jsonArrayString(listSparseArray.get(day-1)));
if(day==7) {
    pd.dismiss();
return;
}     if(res.equals("successful")){
                    isAdded=true;
                    if(day<7)
                        addRoutine(day+1);
                    else{
                        pd.dismiss();
                    }} else{
                   addRoutine(day+1);
                   // Toast.makeText(getApplicationContext(),"Could not add routine",Toast.LENGTH_SHORT).show();

                }}
        });}
        else if(day==7){
                pd.dismiss();
                if(isAdded) {
                 String messageString=(PresentRoutineListFragment.isAdd)?"Routine added":"Routine editted";
                    Toast.makeText(getApplicationContext(),messageString, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(),"Could not add routine.",Toast.LENGTH_SHORT).show();

                return;
        }
        else if(day<7){
            addRoutine(day+1);
        }
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<AddRoutineFragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public AddRoutineFragment getItem(int position) {
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

        public void addFragment(AddRoutineFragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private  void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(AddRoutineFragment.newInstance(0), "Sunday");
        adapter.addFragment(AddRoutineFragment.newInstance(1), "Monday");
        adapter.addFragment(AddRoutineFragment.newInstance(2), "Tuesday");
        adapter.addFragment(AddRoutineFragment.newInstance(3), "Wednesday");
        adapter.addFragment(AddRoutineFragment.newInstance(4), "Thursday");
        adapter.addFragment(AddRoutineFragment.newInstance(5), "Friday");
        adapter.addFragment(AddRoutineFragment.newInstance(6), "Saturday");
        viewPager.setAdapter(adapter);
        viewPager.getAdapter().notifyDataSetChanged();
    }

}
