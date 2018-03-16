package com.susankya.rnk;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ViewRoutineFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

 public static  final String HASSETROUTINE="routine set garyo ta?";
    public static  final String SECTION="section kaun sa hai be tera?";

    private  int mParam1;
    boolean isEditing=false;

    String[] valueharu=new String[]{"","","","","","","",""};
    private static String section,prevSection;
    public ArrayList<RoutineInfo> routineInfoArrayList;

    private TextView editRoutineTV;
    private ListView routineListView;

    public adapt RoutineAdapter;

    private static String[] daysInWeek;

    SQLiteHelper sqLiteHelper;
    private static SQLiteDatabase db;
    public static ViewRoutineFragment newInstance(int param1) {
        ViewRoutineFragment fragment = new ViewRoutineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewRoutineFragment() {
        // Required empty public constructor
    }


    public class adapt extends ArrayAdapter<RoutineInfo> {

        TextViewPlus subject,teacher_name,startTime,endTime,timeSeparator;
        EditText subjectEdit;
        String val=null;
        RoutineInfo ro;
        public adapt(ArrayList<RoutineInfo> c){
            super(getActivity(),0,c);
        }
        @Override
        public View getView(final int pos,View v,ViewGroup vg){

                v=getActivity().getLayoutInflater().inflate(R.layout.material_routine_layout,null);
                RoutineInfo r=getItem(pos);
            ro=r;

            RelativeLayout uneditmode=(RelativeLayout)v.findViewById(R.id.unedit_mode);
            RelativeLayout editMode=(RelativeLayout)v.findViewById(R.id.edit_mode);
                subject=(TextViewPlus) uneditmode.findViewById(R.id.routine_subject);
                startTime=(TextViewPlus) uneditmode.findViewById(R.id.start_time);
                timeSeparator=(TextViewPlus)uneditmode.findViewById(R.id.timeseparator);
            teacher_name=(TextViewPlus) uneditmode.findViewById(R.id.teacher_name);

                endTime=(TextViewPlus)uneditmode.findViewById(R.id.end_time);

                timeSeparator.setCustomFont(getContext(),VLIGHT);
                startTime.setCustomFont(getContext(),VLIGHT);
                endTime.setCustomFont(getContext(),VLIGHT);
                endTime.setText(r.getEnd().toUpperCase());
            teacher_name.setText(r.getTeacher());

            startTime.setText(r.getStart().toUpperCase());
            subject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.abc_fade_in);
                      subject.setAnimation(animation);
                }
            });
            subjectEdit=(EditText)editMode.findViewById(R.id.routine_subject_edittext);
                  subject.setText(r.getSubject());
            if(isEditing)
            {
                subjectEdit.addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                val=s.toString();
                                valueharu[pos]=val;

                            }
                        }
                );
                uneditmode.setVisibility(View.GONE);
                editMode.setVisibility(View.VISIBLE);
                if(pos==0)
                    subjectEdit.requestFocus();
                subjectEdit.setSelectAllOnFocus(true);
                subjectEdit.setText(valueharu[pos]);
            }
            else
            {
                editMode.setVisibility(View.GONE);
                uneditmode.setVisibility(View.VISIBLE);

            }
                return v;
        }

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);

        }
        sqLiteHelper = new SQLiteHelper(getActivity());
        db = sqLiteHelper.getWritableDatabase();


        daysInWeek=new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    }

    View emptyView,listRL;
    ImageView emptylistImg;
    TextViewPlus emptyTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.view_routine_material, container, false);
        emptyTV=(TextViewPlus)v.findViewById(R.id.emptytv);
        listRL=v.findViewById(R.id.listRL);
        emptyView=v.findViewById(R.id.empty_view);

        editRoutineTV=(TextView)v.findViewById(R.id.edit_routine_tv);
        emptylistImg=(ImageView)v.findViewById(R.id.emptylistimg);
        Drawable d=getActivity().getResources().getDrawable(R.drawable.empty_list);
        d.setColorFilter(getActivity().getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        emptylistImg.setBackground(d);
        editRoutineTV.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRoutineWidget(getActivity());
                        String values[]=new String[8];

                        if(isEditing)
                        {
                            for(int i=0;i<8;i++)
                            {
                                //View rowView=routineListView.getChildAt(i);
                                View rowView=routineListView.getAdapter().getView(i,null,routineListView);
                                RelativeLayout RL=(RelativeLayout)rowView.findViewById(R.id.edit_mode);
                                EditText et=(EditText)RL.findViewById(R.id.routine_subject_edittext);
                                if(valueharu[i].isEmpty())
                                    valueharu[i]="--";
                                else
                                    values[i]=et.getText().toString().trim();

                            }
                            saveRoutine(valueharu,daysInWeek[mParam1],getActivity());



                            routineInfoArrayList.clear();

                            RoutineAdapter.notifyDataSetChanged();
                            isEditing=false;
                            editRoutineTV.setText("Edit");
                        }
                        else
                        {
                            isEditing=true;
                            editRoutineTV.setText("Save");
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            RoutineAdapter.notifyDataSetChanged();
                        }

                    }
                }
        );
        routineInfoArrayList=new ArrayList<>();

        routineListView=(ListView)v.findViewById(R.id.routineListView);
        final SharedPreferences sp=getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
      section=sp.getString(SECTION, "Select Your Section");

        if(!section.equals("Select Your Section"))
            prevSection=section;


       // NavDrawerActivity.currentRoutine=mParam1+1;
        routineInfoArrayList=insertIntoView(mParam1+1);
        if (section.equals("Select Your Section"))
        {
            emptyTV.setText("Select your section");
            emptylistImg.setVisibility(View.GONE);
        }
        else emptyTV.setText("No routine for this day");
        routineListView.setEmptyView(emptyView);
        RoutineAdapter=new adapt(routineInfoArrayList);
        routineListView.setAdapter(RoutineAdapter);

        if (routineInfoArrayList.isEmpty())
        {

        }
        return v;
    }

    void showEmptyView()
    {
        listRL.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
    void hideEmptyView()
    {
        listRL.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
    private void updateRoutineWidget(Context context){
        try {
            AppWidgetManager.getInstance(context);
            Intent intent = new Intent(RoutineWidget.FORCE_UPDATE);
            context.sendBroadcast(intent);
        }
        catch (Exception e){
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
             }
    }

   public static ArrayList<RoutineInfo> insertIntoView(int day)
    {
        ArrayList<RoutineInfo> rArray=new ArrayList<>();
        JSONArray routineArray=getRoutine(day);

        try {
            //Log.d("mainplace",routineArray.getJSONObject(0).getString(TEACHER_NAME));
        }
        catch (Exception e)
        {
            //Log.d("error2016",e+"");
        }


        for (int i=0;i<routineArray.length();i++)
        {
            try {
                RoutineInfo routineInfo=new RoutineInfo();
                JSONObject jsonObject=routineArray.getJSONObject(i);
                routineInfo.day=UtilitiesAdi.returnDayNum(jsonObject.getString("day"));

                routineInfo.periodNo= jsonObject.getString("period_no");
                routineInfo.subject= jsonObject.getString("subject");
                routineInfo.section= jsonObject.getString("section_name");
                routineInfo.teacher= jsonObject.getString("teacher_name");
                routineInfo.start=jsonObject.getString("start");
                routineInfo.end= jsonObject.getString("END");
                routineInfo.collegeSN=jsonObject.getString("college_sn");
                rArray.add(routineInfo);

            }
            catch (Exception e)
            {
                //Log.d("k vo vaae",e.toString());
            }


        }
        return rArray;
    }

    @SuppressLint("ValidFragment")
    public static class DownloadRoutine extends android.support.v4.app.DialogFragment {
       public DownloadRoutine(){
        }
        public String sectionName;
        private String[] sections;
        private Button okButton;
     //   private int mParam1;
      private Spinner sectionSpinner;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        public String[] giveMeStringArrayFromJSONObject(JSONObject object,String[] placeholder) throws JSONException
        {
            String[] values=new String[placeholder.length];
            for(int i=0;i<placeholder.length;i++)
            {
                values[i]=object.get(placeholder[i]).toString();
            }
            return values;
        }

        public void onResume()
        {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            getDialog().getWindow().setLayout((14 * width)/15, (3 * height)/5);
            super.onResume();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
          final View v = inflater.inflate(R.layout.load_routine, null, false);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            final  TextView failureText=(TextView)v.findViewById(R.id.failuretext);
            final ProgressBar PB=(ProgressBar)v.findViewById(R.id.progress);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
            sectionSpinner=(Spinner)v.findViewById(R.id.section_spinner);
            okButton=(Button)v.findViewById(R.id.load_routine_button);
            okButton.setVisibility(View.GONE);
            String link=NEW_HOST+"SectionName.php";
            new PhpConnect(link,"",getActivity(),0,new String[]{CMDXXX, UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity()))},new String[]{"cmdxxx","college_sn"}).setListener(new PhpConnect.ConnectOnClickListener() {
                @Override
                public void onConnectListener(String res) {
                    try {
                        //Log.d("TAGabcd", "onConnectListener:"+res);
                        JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                        sections = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject job = jsonArray.getJSONObject(i);
                            sections[i] = job.getString("section_name");

                        }
                        if(jsonArray.length()>0)
                        {
                            ((LinearLayout)v.findViewById(R.id.loadingSection)).setVisibility(View.INVISIBLE);
                            List<String> _class=new ArrayList<>(Arrays.asList(sections));
                            Collections.sort(_class, String.CASE_INSENSITIVE_ORDER);
                            sections=_class.toArray(new String[sections.length]);
                            sectionSpinner.setAdapter(
                                    new CustomSpinnerAdapter(
                                            getActivity(), R.id.section_spinner, sections
                                    )
                            );
                            okButton.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            failureText.setVisibility(View.VISIBLE);
                            failureText.setText("Looks like your institute has not uploaded any routine yet.");
                            PB.setVisibility(View.GONE);
                            okButton.setVisibility(View.GONE);
                        }
                        //updateRoutineWidget(getActivity());

                    } catch (Exception e) {
                        //Log.d("TAGabcd", "onConnectListener: "+e.toString());
                        failureText.setVisibility(View.VISIBLE);
                        failureText.setText("No routine is available. You can set it manually.");
                        PB.setVisibility(View.GONE);
                        okButton.setVisibility(View.GONE);
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });



            okButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         //   dialog.dismiss();

                            sectionName=sectionSpinner.getSelectedItem().toString();

                            routineFetcher(sectionName);
                 }
            });
            return v;
        }

        void routineFetcher(final String section)
        {
            final SharedPreferences sp=getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
            String link = NEW_HOST + "FetchRoutine.php";

            new PhpConnect(link,"Loading...",getActivity(),1,new String[]{CMDXXX,Utilities.getDatabaseName(getActivity()),section,Utilities.getSN(getActivity())},new String[]{"cmdxxx","dbName","section_name","user_no"}).setListener(new PhpConnect.ConnectOnClickListener() {
                @Override
                public void onConnectListener(String res) {

                    int day=0;
                    try {
                        if(routineExists(prevSection))
                        {
                            //Log.d("abcxyz",prevSection);
                            deleteRoutine(prevSection);
                        }

                        JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jO = jsonArray.getJSONObject(i);
                            try {
                                day=Integer.parseInt(jO.getString("day"));
                                savePeriodRow(jO);

                            } catch (Exception w) {
                                //Log.d(" ViewRoutineFragment", w.toString());
                                // Toast.makeText(getActivity(), w.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } catch (Exception e) {
                        //Log.d(" ViewRoutineFragment", e.toString());
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                    }
                    sp.edit().putBoolean(HASSETROUTINE, true).apply();
                    sp.edit().putString(SECTION, sectionName).apply();
                    ViewRoutinePagerFragment.changeName(sectionName);
                    ViewRoutinePagerFragment.SectionViewFooter.setText("Change section");
                    ViewRoutinePagerFragment.updateAdapter();
                    updateRoutineWidget(getActivity().getApplicationContext());


                }
            });
            ViewRoutinePagerFragment.dialog.dismiss();

        }
        private void updateRoutineWidget(Context context){
            //Log.d(TAG, "updateRoutineWidget: reached");
            try {
                AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
                ComponentName widgetComponent = new ComponentName(context, RoutineWidget.class);
                int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);
                Intent update = new Intent(context,RoutineWidget.class);
                update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
                update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                context.sendBroadcast(update);
               /* //AppWidgetManager.getInstance(context);
                Intent intent = new Intent(RoutineWidget.FORCE_UPDATE);
               //intent.setAction(RoutineWidget.FORCE_UPDATE);
                context.sendBroadcast(intent);*/
            }
            catch (Exception e){
                //Log.d(TAG, "updateRoutineWidget: "+e.toString());
                //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }


        private void savePeriodRow(JSONObject jsonObject)
        {
            try
            {
                ContentValues cv = new ContentValues();
                cv.put(DAY, jsonObject.getString("day"));
                cv.put(PERIOD_NO, jsonObject.getString("period_no"));
                cv.put(SUBJECT, jsonObject.getString("subject"));
                cv.put(SECTION_NAME, jsonObject.getString("section_name"));
                cv.put(TEACHER_NAME, jsonObject.getString("teacher_name"));
                cv.put(START, jsonObject.getString("start"));
                cv.put(END, jsonObject.getString("end"));
                cv.put(COLLEGE_SN, jsonObject.getString("college_sn"));
                db.insert(TABLENAME, "null", cv);
            }
            catch (Exception e)
            {
                //Log.d("savePeriodRow",e+"");
            }

        }

    }

    private void saveRoutine(String[] periods,String day,Context x){
        try{
        db.delete(TABLENAME,C_DAY+"=?",new String[]{day});
        ContentValues cv=new ContentValues();
        cv.put(C_FIRST,periods[0]);
        cv.put(C_SECOND,periods[1]);
        cv.put(C_THIRD,periods[2]);
        cv.put(C_FOURTH,periods[3]);
        cv.put(C_FIFTH,periods[4]);
        cv.put(C_SIXTH,periods[5]);
        cv.put(C_SEVENTH,periods[6]);
        cv.put(C_EIGHTH,periods[7]);
        cv.put(C_DAY,day);
        cv.put(C_SECTION,section);
        db.insert(TABLENAME, "null", cv);
        }
    catch (Exception e){
        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
    }
    }



    private static JSONArray getRoutine(int day){
        try{

            String[] columns=new String[]{DAY,PERIOD_NO,SUBJECT,SECTION_NAME,TEACHER_NAME,START,END,COLLEGE_SN};//C_SECTION+"= ? && "+
            //Log.d("abcdef",day+"");
      Cursor c=db.query(TABLENAME,columns,DAY+"= ?",new String[]{day+""},null,null,null);
            //Log.d("abcdef","ok");

            JSONArray jsonArray=new JSONArray();

            while(c.moveToNext())
            {

                //Log.d("abcdef","ok1.5");
                int dayN=c.getColumnIndex(DAY);
                //Log.d("abcdef","ok2");
                int periodNo=c.getColumnIndex(PERIOD_NO);
                //Log.d("abcdef","ok3");
                int subjectN=c.getColumnIndex(SUBJECT);
                int sectionNameN=c.getColumnIndex(SECTION_NAME);
                int teacherN=c.getColumnIndex(TEACHER_NAME);
                int startN=c.getColumnIndex(START);
                int endN=c.getColumnIndex(END);
                int collegeSN=c.getColumnIndex(COLLEGE_SN);
                JSONObject row=new JSONObject();
                row.put(DAY,c.getString(dayN));
                //Log.d("problemhere",row.getString(DAY));
                row.put(PERIOD_NO,c.getString(periodNo));
                //Log.d("problemhere",row.getString(PERIOD_NO));
                row.put(SUBJECT,c.getString(subjectN));
                //Log.d("problemhere",row.getString(SUBJECT));
                row.put(SECTION_NAME,c.getString(sectionNameN));
                //Log.d("problemhere",row.getString(SECTION_NAME));
                row.put(TEACHER_NAME,c.getString(teacherN));
                //Log.d("problemhere",row.getString(TEACHER_NAME));
                row.put(START,c.getString(startN));
                row.put(END,c.getString(endN));
                row.put(COLLEGE_SN,c.getString(collegeSN));
                jsonArray.put(row);
           }
            c.close();
            return jsonArray;
        }
        catch (Throwable e)
          {
            //Log.d("checker",e+"");
          }
return null;
    }

    private static boolean routineExists(String section){

        try
        {
            String[] columns=new String[]{DAY,PERIOD_NO,SUBJECT,SECTION_NAME,TEACHER_NAME,START,END,COLLEGE_SN};//C_SECTION+"= ? && "+
            Cursor c=db.query(TABLENAME,columns,SECTION_NAME+"= ?",new String[]{section},null,null,null);
            //Log.d("yetaxa",c.getCount()+"");
            return !(c.getCount()==0);
        }
        catch (Exception e)
        {

            return false;
        }

    }
    private static boolean deleteRoutine(String section){
        int c=db.delete(TABLENAME,SECTION_NAME+"= ?",new String[]{section});
        return (c>0);

    }


}
