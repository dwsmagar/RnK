package com.susankya.rnk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Routine implements FragmentCodes {
   static SQLiteHelper sqLiteHelper;
    private static SQLiteDatabase db;
   public Routine(){

    }
    private int dayPosition;
    private String[] days=new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private String periods[];

    public int getDayPosition() {
        return dayPosition;
    }

    public void setDayPosition(int dayPosition) {
        this.dayPosition = dayPosition;
    }

    public String[] getPeriods() {
        return periods;
    }

    public void setPeriods(String[] periods) {
        this.periods = periods;
    }

    public String[] getDays() {
        return days;
    }

   public String getDay(int pos)
   {
       return days[pos];
   }

public String[] getPeriodsOf(int day,Context c){
    JSONObject jsonObject;
   String mJSON=loadRoutine(getDay(day),c);
    String[] subjectTopics=new String[8];
    try
    {
        jsonObject=new JSONObject(mJSON);
        for(int i=0;i<8;i++)
        {
            subjectTopics[i]=jsonObject.getString(Integer.toString(i));
                     }

    }

    catch(Exception e)
    {

    }
    return subjectTopics;
}
   public  String[] getRoutine(int day,Context con){
        sqLiteHelper = new SQLiteHelper(con);
        db = sqLiteHelper.getWritableDatabase();
        try{
            String first=null,second=null,third=null,fourth=null,fifth=null,sixth=null,seventh=null,eighth=null;
            String[] columns=new String []{C_FIRST,C_SECOND,C_THIRD,C_FOURTH,C_FIFTH,C_SIXTH,C_SEVENTH,C_EIGHTH,C_DAY,C_SECTION};//C_SECTION+"= ? && "+
            Cursor c=db.query(TABLENAME,columns,C_DAY+"= ?",new String[]{getDay(day)},null,null,null);
            //  Toast.makeText(getActivity(),"DONE",Toast.LENGTH_SHORT).show();
            while(c.moveToNext()) {
                 int i = c.getColumnIndex(C_FIRST);
                int j = c.getColumnIndex(C_SECOND);
                int k = c.getColumnIndex(C_THIRD);
                int l = c.getColumnIndex(C_FOURTH);
                int m = c.getColumnIndex(C_FIFTH);
                int n = c.getColumnIndex(C_SIXTH);
                int o = c.getColumnIndex(C_SEVENTH);
                int p = c.getColumnIndex(C_EIGHTH);
                int q = c.getColumnIndex(C_DAY);
                int rr = c.getColumnIndex(C_SECTION);
                first = c.getString(i);
                second = c.getString(j);
                third = c.getString(k);
                fourth = c.getString(l);
                fifth = c.getString(m);
                sixth = c.getString(n);
                seventh = c.getString(o);
                eighth = c.getString(p);
                            //  Toast.makeText(getActivity(),first+" "+second+" "+third+" "+fourth+" "+fifth+" "+sixth+" "+seventh,Toast.LENGTH_SHORT).show();
                        }
            String[]   days=new String[]{first,second,third,fourth,fifth,sixth,seventh,eighth};
            return days;
            // Toast.makeText(getActivity(), routines.size()+"", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            //Toast.makeText(con, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return new String []{"","",""};
    }
   public String loadRoutine(String day,Context context)
   {
       String line=null;

       BufferedReader reader = null;
       try {
           // Open and read the file into a StringBuilder
           InputStream in = context.getApplicationContext().openFileInput("routine"+day);
           reader = new BufferedReader(new InputStreamReader(in));
           StringBuilder jsonString = new StringBuilder();
           while ((line = reader.readLine()) != null) {
               jsonString.append(line);
           }
           line =jsonString.toString();


       }
       catch (Exception e) {

       } finally {
           if (reader != null)
               try {
                   reader.close();
               }
               catch (Exception e){

               }
       }
       return line;
   }



}
