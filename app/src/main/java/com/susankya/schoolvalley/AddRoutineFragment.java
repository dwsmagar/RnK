package com.susankya.schoolvalley;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


public class AddRoutineFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private int lastDay=0;
    public static  final String HASSETROUTINE="routine set garyo ta?";
    public static  final String SECTION="section kaun sa hai be tera?";

    private  int mParam1;
    boolean isEditing=false;
    private boolean isEditingMode=true;
    public static final String PERIOD_NO="period_no",START="start",END="end",TEACHER_NAME="teacher_name",SUBJECT="subject";
List<RoutineInfo> routineInfos;
    String[] valueharu=new String[]{"","","","","","","",""};
    private static String section;
    public ArrayList<RoutineInfo> routineInfoArrayList;

    private TextView day,editRoutineTV;
    private ListView routineListView;

    public adapt RoutineAdapter;

    private static String[] daysInWeek;

    SQLiteHelper sqLiteHelper;
    private static SQLiteDatabase db;
    public static AddRoutineFragment newInstance(int param1) {
        AddRoutineFragment fragment = new AddRoutineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AddRoutineFragment() {
        // Required empty public constructor
    }




private  void setArrayList(){
    AddRoutineActivity.setListSparseArray(mParam1,routineInfos);

    //Log.d("TAG", "setArrayList:"+routineInfos.size());
 //   AddRoutineActivity.listOfRoutine.add(mParam1,jsonArrayString(routineInfos));
   // //Log.d("TAG", "setArrayList:"+AddRoutineActivity.listOfRoutine.get(mParam1).toString());
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
        isEditingMode=AddRoutineActivity.isEdit;
        sqLiteHelper = new SQLiteHelper(getActivity());
        db = sqLiteHelper.getWritableDatabase();
routineInfos=AddRoutineActivity.getListSparseArray().get(mParam1);
        //Log.d("RoutineInfo", "onCreate: "+mParam1+" "+routineInfos.size());
        if(isEditingMode&&routineInfos.size()==0){
        RoutineInfo ri=new RoutineInfo();
        ri.setCollegeSN( UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity())));
        ri.setPeriodNo("1");
        ri.setStart("10:15 AM");
        ri.setEnd("11:00 AM");
        ri.setTeacher("Teacher Name");
        ri.setSubject("Subject");
        routineInfos.add(ri);
        }else if(isEditingMode&&!(routineInfos.get(routineInfos.size()-1).getTeacher().equals("Teacher Name")||
                routineInfos.get(routineInfos.size()-1).getSubject().equals("Subject"))){
            RoutineInfo ri=new RoutineInfo();
            ri.setCollegeSN( UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity())));
            ri.setPeriodNo("1");
            ri.setStart("10:15 AM");
            ri.setEnd("11:00 AM");
            ri.setTeacher("Teacher Name");
            ri.setSubject("Subject");
            routineInfos.add(ri);
        }
        daysInWeek=new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.view_routine_material, container, false);
        day=(TextView)v.findViewById(R.id.day);
        editRoutineTV=(TextView)v.findViewById(R.id.edit_routine_tv);
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
//        day.setText(daysInWeek[mParam1] + " (" + section + ")")

        RoutineAdapter=new adapt(routineInfos);
        routineListView.setAdapter(RoutineAdapter);
        return v;
    }
    private void updateRoutineWidget(Context context){
        try {
            AppWidgetManager.getInstance(context);
            Intent intent = new Intent(getActivity(),RoutineWidget.class);
            intent.setAction(RoutineWidget.FORCE_UPDATE);
            context.sendBroadcast(intent);
        }
        catch (Exception e){
            //Log.d(TAG, "updateRoutineWidget: "+e.toString());
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
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
    public class adapt extends ArrayAdapter<RoutineInfo> {

        TextViewPlus subject,teacher_name,startTime,endTime,timeSeparator;
        EditText subjectEdit;
        int startHour,startmin,endHour,endMin;
        String nextStart="10:00 AM",nextEnd="11:00 AM";
        ImageView imageViewDelete;
        String val=null;
        RoutineInfo ro;
        public adapt(List<RoutineInfo> c){
            super(getActivity(),0,c);
        }
        @Override
        public View getView(final int pos,View v,ViewGroup vg){

            v=getActivity().getLayoutInflater().inflate(R.layout.material_routine_layout_add,null);
            final   RoutineInfo r=getItem(pos);

            RelativeLayout uneditmode=(RelativeLayout)v.findViewById(R.id.unedit_mode);
            RelativeLayout editMode=(RelativeLayout)v.findViewById(R.id.edit_mode);
            subject=(TextViewPlus) uneditmode.findViewById(R.id.routine_subject);
            teacher_name=(TextViewPlus) uneditmode.findViewById(R.id.teacher_name);
            imageViewDelete =(ImageView)uneditmode.findViewById(R.id.edit_button);
            startTime=(TextViewPlus) uneditmode.findViewById(R.id.start_time);
            timeSeparator=(TextViewPlus)uneditmode.findViewById(R.id.timeseparator);
            endTime=(TextViewPlus)uneditmode.findViewById(R.id.end_time);
            //timeSeparator.setCustomFont(getContext(),VLIGHT);
            //startTime.setCustomFont(getContext(),VLIGHT);
            //endTime.setCustomFont(getContext(),VLIGHT);
            endTime.setText(r.getEnd().toUpperCase());
            startTime.setText(r.getStart().toUpperCase());
            teacher_name.setText(r.getTeacher());

           /* if(pos==0)
                imageViewDelete.setVisibility(View.GONE);*/
            View.OnClickListener showDialog=new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog=new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.add_routine_detail_dialog);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final EditText etTeacher=(EditText)dialog.findViewById(R.id.teacher);
                    final EditText etSubject=(EditText)dialog.findViewById(R.id.subject);
                    final ImageView startTimeImage=(ImageView)dialog.findViewById(R.id.startEdit);
                    final ImageView endTimeImage=(ImageView)dialog.findViewById(R.id.endEdit);
                    final TextView starttv=(TextView)dialog.findViewById(R.id.startTimetv);
                    final TextView endtv=(TextView)dialog.findViewById(R.id.endTimetv);
                    final RelativeLayout RLForStartTime=(RelativeLayout)dialog.findViewById(R.id.rlForStart);
                    final RelativeLayout RLForEndTime=(RelativeLayout)dialog.findViewById(R.id.rlForEnd);

                    starttv.setText(r.getStart());
                    endtv.setText(r.getEnd());
                    if(!r.getTeacher().equals("Teacher Name"))etTeacher.setText(r.getTeacher());
                    if(!r.getSubject().equals("Subject"))etSubject.setText(r.getSubject());
                    String[] startTiming=r.getStart().replaceAll(" ","").replaceAll("AM","").replaceAll("PM","").split(":");
                    startHour=Integer.parseInt(startTiming[0]);
                    startmin=Integer.parseInt(startTiming[1]);
                    String[] endTiming=r.getEnd().replaceAll(" ","").replaceAll("AM","").replaceAll("PM","").split(":");
                    endHour=Integer.parseInt(endTiming[0]);
                    endMin=Integer.parseInt(endTiming[1]);
                    ((Button)dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //Log.d("TAG", "onDismiss");
                        }
                    });
                    final View.OnClickListener startTimeSelector=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TimePickerDialog dialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String AM_PM ;
                                    if(hourOfDay < 12) {
                                        AM_PM = "AM";
                                        if(hourOfDay==0)
                                            hourOfDay=12;
                                    } else {
                                        AM_PM = "PM";
                                        if(hourOfDay>12)
                                        hourOfDay=hourOfDay-12;
                                    }
                                    //Log.d("TAG", "onTimeSet: "+((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":")+(((minute<10)?"0"+minute:minute) + " " + AM_PM ));
                                    starttv.setText(((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":")+(((minute<10)?"0"+minute:minute) + " " + AM_PM ));
                                    r.setStart(((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":")+(((minute<10)?"0"+minute:minute) + " " + AM_PM ));
                                }
                            },startHour,startmin,false);dialog.show();
                        }
                    };
                    View.OnClickListener endTimeSelector= new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TimePickerDialog dialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String AM_PM ;
                                    if(hourOfDay < 12) {
                                        AM_PM = "AM";
                                        if(hourOfDay==0)
                                            hourOfDay=12;
                                    } else {
                                        AM_PM = "PM";
                                        if(hourOfDay!=12)
                                            hourOfDay=hourOfDay-12;
                                    }
                                    //Log.d("TAG", "onTimeSet: "+((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":")+(((minute<10)?"0"+minute:minute) + " " + AM_PM ));

                                    nextStart=(((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":")+(((minute<10)?"0"+minute:minute) + " " + AM_PM ));
                                    if(minute<15)
                                        nextEnd=(((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":")+(((minute+45<10)?"0"+minute:minute+45) + " " + AM_PM ));//hourOfDay + ":" + (minute+45) + " " + AM_PM;
                                    else
                                        nextEnd=(((hourOfDay+1<10?"0"+(hourOfDay+1):hourOfDay+1) + ":")+(((minute-15<10)?"0"+(minute-15):minute-15) + " " + AM_PM ));//(hourOfDay+1) + ":" + (minute-15) + " " + AM_PM;
                                    endtv.setText(((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":")+(((minute<10)?"0"+minute:minute) + " " + AM_PM ));
                                    r.setEnd(((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":")+(((minute<10)?"0"+minute:minute) + " " + AM_PM ));
                                }
                            },endHour,endMin,false);dialog.show();
                        }
                    };
                    startTimeImage.setOnClickListener(startTimeSelector);
                    endTimeImage.setOnClickListener(endTimeSelector);
                    RLForStartTime.setOnClickListener(startTimeSelector);
                    RLForEndTime.setOnClickListener(endTimeSelector);
                    ((Button)dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!etTeacher.getText().toString().isEmpty()||!etSubject.getText().toString().isEmpty()){
                                RoutineInfo ri=new RoutineInfo();
                                ri.setSubject(etSubject.getText().toString());
                                ri.setTeacher(etTeacher.getText().toString());
                                ri.setPeriodNo((pos+1)+"");
                                ri.setStart(r.getStart());
                                ri.setEnd(r.getEnd());
                                dialog.dismiss();
                                routineInfos.remove(pos);
                                routineInfos.add(pos,ri);
                                setArrayList();
                                RoutineInfo rui=new RoutineInfo();
                                rui.setStart(nextStart);
                                rui.setEnd(nextEnd);
                                routineInfos.add(rui);
                                notifyDataSetChanged();
                            }else
                            {
                                Toast.makeText(getActivity(),"Fill the fields",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    dialog.show();
                }
            };
            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(getItem(pos));
                    notifyDataSetChanged();
                    // routineInfos.remove(pos);
                    if(pos==0){
                        routineInfos.add(pos,new RoutineInfo());
                    }

                }
            });
            if(AddRoutineActivity.isEdit){
                v.setOnClickListener(showDialog);

            }else {
                imageViewDelete.setVisibility(View.GONE);
            }
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

 }
