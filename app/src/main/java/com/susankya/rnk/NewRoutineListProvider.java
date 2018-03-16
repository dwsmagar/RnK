package com.susankya.rnk;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abhinav Dev on 2/26/2017.
 */

public class NewRoutineListProvider implements RemoteViewsService.RemoteViewsFactory,FragmentCodes {
    private ArrayList<RoutineInfo> listItemList = new ArrayList();
    private Context context = null;
    private int appWidgetId,day;

    public static SQLiteHelper sqLiteHelper;
    public static SQLiteDatabase db;
    public NewRoutineListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        day=intent.getIntExtra("dayBoy",5);
        //Log.d(TAG, "ListProvider: " +day);
        populateListItem();
    }

    private void populateListItem() {
        listItemList=insertIntoView(day+1,context);
    }
    public static ArrayList<RoutineInfo> insertIntoView(int day,Context c)
    {
        ArrayList<RoutineInfo> rArray=new ArrayList<>();
        JSONArray routineArray=getRoutine(day,c);

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
        //Log.d(TAG, "insertIntoView: "+routineArray.length());
        return rArray;
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    *
    */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.material_routine_item_widget);
        RoutineInfo r = (RoutineInfo) listItemList.get(position);
        remoteView.setTextViewText(R.id.start_time, r.getStart());
        remoteView.setTextViewText(R.id.end_time, r.getEnd());
        remoteView.setTextViewText(R.id.routine_subject, r.getSubject());
        remoteView.setTextViewText(R.id.teacher_name, r.getTeacher());
        return remoteView;
    }
    public static JSONArray getRoutine(int day, Context context){
        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
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
            //Log.d(TAG, "getRoutine: json"+jsonArray.toString());
            return jsonArray;
        }
        catch (Throwable e)
        {
            //Log.d("checker",e+"");
        }
        return null;
    }
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    private class list{
        String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}