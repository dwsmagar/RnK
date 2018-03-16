package com.susankya.rnk;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class HelpFragment extends android.support.v4.app.Fragment implements FragmentCodes{

    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private int layout_id;
    public static  final  String HASSETROUTINE="routine set garyo ta?";
    public static  final  String SECTION="section kaun sa hai be tera?";
    public static HelpFragment newInstance(int  param1) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
          }
        switch(mParam1){
            case 1:
                layout_id= R.layout.layout_help_1;
            break;
            case 2:
                layout_id= R.layout.layout_help_2;
            break;
            case 3:
                layout_id= R.layout.layout_help_3;
        break;
            case 4:
                layout_id=R.layout.layout_help_4;
                break;
        }
    }

    private Spinner classSpinner,sectionSpinner;
    private static SQLiteDatabase db;
    private String sectionSelected;
    PhpConnect routinePhpConnect;

    private String[] class11_sections,class12_sections,classes=new String[]{"11","12"};
    private void saveRoutine(String[] periods,String day,Context x){
        try {

            ContentValues cv = new ContentValues();
            cv.put(C_FIRST, periods[0]);
            cv.put(C_SECOND, periods[1]);
            cv.put(C_THIRD, periods[2]);
            cv.put(C_FOURTH, periods[3]);
            cv.put(C_FIFTH, periods[4]);
            cv.put(C_SIXTH, periods[5]);
            cv.put(C_SEVENTH, periods[6]);
            cv.put(C_EIGHTH, periods[7]);
            cv.put(C_DAY, day);
            cv.put(C_SECTION, sectionSelected);
            db.insert(TABLENAME, "null", cv);
            //Toast.makeText(getActivity(), "DONE", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){

            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private int getNumber(){
        int a=0;
        try{
            String first=null,second=null,third=null,fourth=null,fifth=null,sixth=null,seventh=null,eighth=null;
            String[] columns=new String []{C_FIRST,C_SECOND,C_THIRD,C_FOURTH,C_FIFTH,C_SIXTH,C_SEVENTH,C_EIGHTH,C_DAY,C_SECTION};//C_SECTION+"= ? && "+
            Cursor c=db.query(TABLENAME,columns,C_DAY+"= ?",new String[]{"Friday"},null,null,null);
            //  Toast.makeText(getActivity(),"DONE",Toast.LENGTH_SHORT).show();

            while(c.moveToNext()){
                a++;
            }

            // Toast.makeText(getActivity(), routines.size()+"", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            //     Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
       return a;
    }
    public String[] giveMeStringArrayFromJSONObject(JSONObject object,String[] placeholder) throws JSONException
    {
        String [] values=new String[placeholder.length];
        for(int i=0;i<placeholder.length;i++)
        {
            values[i]=object.get(placeholder[i]).toString();
        }
        return values;
    }
    SQLiteHelper sqLiteHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   final View v= inflater.inflate(layout_id, container, false);
        if(layout_id==R.layout.layout_help_4){
            final RadioGroup profile=(RadioGroup)v.findViewById(R.id.profile);
            RadioButton student=(RadioButton)v.findViewById(R.id.student);
            sqLiteHelper = new SQLiteHelper(getActivity());
            db = sqLiteHelper.getWritableDatabase();

            RadioButton teacher=(RadioButton)v.findViewById(R.id.teacher);
            classSpinner=(Spinner)v.findViewById(R.id.class_spinner);
            sectionSpinner=(Spinner)v.findViewById(R.id.section_spinner);
            class11_sections=getActivity().getResources().getStringArray(R.array.class_eleven_section);
            class12_sections=getActivity().getResources().getStringArray(R.array.class_twelve_section);
            List<String> class11=new ArrayList<>(Arrays.asList(class11_sections));
            List<String> class12=new ArrayList<>(Arrays.asList(class12_sections));
            Collections.sort(class11, String.CASE_INSENSITIVE_ORDER);
            Collections.sort(class12,String.CASE_INSENSITIVE_ORDER);

            class11_sections=class11.toArray(new String[class11_sections.length]);
            class12_sections=class12.toArray(new String[class12_sections.length]);
            classSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedClass = classes[position];
                            if (selectedClass.equals("11"))
                                sectionSpinner.setAdapter(
                                        new CustomSpinnerAdapter(
                                                getActivity(), R.id.section_spinner, class11_sections
                                        )
                                );
                            else
                                sectionSpinner.setAdapter(
                                        new CustomSpinnerAdapter(
                                                getActivity(), R.id.section_spinner, class12_sections
                                        ));

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    }
            );
            classSpinner.setAdapter(new CustomSpinnerAdapter(getActivity(), R.id.class_spinner, classes));
            student.setChecked(true);
            final SharedPreferences sp = getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
            try
            {
                /* String DAY="day";
    String PERIOD_NO="period_no";
    String SUBJECT="subject";
    String SECTION_NAME="section_name";
    String TEACHER_NAME="teacher_name";
    String START="start";
    String END="END";
    String COLLEGE_SN="college_sn";*/
                db.execSQL("create table "+TABLENAME+" ("+PERIOD_NO+" varchar(50), "+SUBJECT+" varchar(50), "+SECTION_NAME+" varchar(50), "+
                        TEACHER_NAME+" varchar(50), "+START+" varchar(50), "+END+" varchar(50), "+COLLEGE_SN+" varchar(50));");
            }
            catch (Exception e)
            {
                //Log.d("errorxa",e.toString());
                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
            }

            sp.edit().putBoolean(IS_DATABASE_CREATED,true).commit();
            Button start =(Button)v.findViewById(R.id.start);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sectionSelected = sectionSpinner.getSelectedItem().toString();
                    if(Utilities.isShiftMorning(sectionSelected,getActivity()))
                        sp.edit().putBoolean(SettingsFragment.SETTINGS_SHIFT,true).commit();
                    else
                        sp.edit().putBoolean(SettingsFragment.SETTINGS_SHIFT,false).commit();
                    if (Utilities.isConnectionAvailable(getActivity())) {



                        String link = FragmentCodes.HOST + "AddRoutine/GetRoutine.php?section=" +sectionSelected;
                        routinePhpConnect = new PhpConnect(link, "Downloading your section's routine...", getActivity(), 1);
                        routinePhpConnect.setListener(new PhpConnect.ConnectOnClickListener() {
                                                          @Override
                                                          public void onConnectListener(String res) {

                                                              try {
                                                                  JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                                                                  for (int i = 0; i < jsonArray.length(); i++) {
                                                                      JSONObject jO = jsonArray.getJSONObject(i);
                                                                      try {
                                                                          saveRoutine(giveMeStringArrayFromJSONObject(
                                                                                  jO,
                                                                                  new String[]{"1st_period",
                                                                                          "2nd_period",
                                                                                          "3rd_period",
                                                                                          "4th_period",
                                                                                          "5th_period",
                                                                                          "6th_period",
                                                                                          "7th_period",
                                                                                          "8th_period",
                                                                                  }
                                                                          ), jO.get("days").toString(), getActivity());
                                                                          //     getFragmentManager().popBackStack();
                                                                          //   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new FragmentForHome()).addToBackStack(null).commit();

                                                                      } catch (Exception w) {
                                                                          //Log.d(" ViewRoutineFragment", w.toString());
                                                                          //Toast.makeText(getActivity(), w.toString(), Toast.LENGTH_SHORT).show();

                                                                      }
                                                                  }
                                                                  sp.edit().putBoolean(HASSETROUTINE, true).apply();
                                                                  sp.edit().putString(SECTION, sectionSelected).apply();
                                                                 sp.edit().putBoolean(HAS_VISITED, true).commit();
//setRoutine();
                                                              } catch (Exception e) {
                                                                  //Log.d(" ViewRoutineFragment", e.toString());
                                                                  //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                                                              }


                                                              //Toast.makeText(getActivity(),getNumber()+"",Toast.LENGTH_LONG).show();
                                                              startActivity(new Intent(getActivity(), NavDrawerActivity.class));
                                                              getActivity().finish();

                                                          }
                                                      }
                        );
                    }
                    else
                    {
                        sp.edit().putBoolean(HAS_VISITED,true).commit();
                        startActivity(new Intent(getActivity(), NavDrawerActivity.class));
                        getActivity().finish();

                    }

                }
            });


                profile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()

                                                   {
                                                       @Override
                                                       public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                           RadioButton r = (RadioButton) v.findViewById(checkedId);
                                                           int checkedIndex = profile.indexOfChild(r);
                                                           switch (checkedIndex) {
                                                               case 0:
                                                                   sp.edit().putBoolean(SettingsFragment.SETTINGS_PROFILE, true).apply();
                                                                   break;
                                                               case 1:
                                                                   sp.edit().putBoolean(SettingsFragment.SETTINGS_PROFILE, false).apply();
                                                                   break;
                                                           }
                                                       }
                                                   }

                );
            }


            return v;
    }



}
