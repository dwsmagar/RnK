package com.susankya.schoolvalley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 8/4/2015.
 */
public class noticeBoard implements FragmentCodes {
    private Context mAppContext;
    private static noticeBoard sNoticeBoard;
   private static ArrayList<notice> mNotices;
    private int objectEscapedNotice;
    private String link;
    private Activity activity;

    public noticeBoard(Context context, int escapeNotice,Activity acti){
        mAppContext=context;
        objectEscapedNotice=escapeNotice;
        activity=acti;


    }
    public static  ArrayList<notice> getNotices(){
        return mNotices;
    }
    void addNotice(notice n){
        try {
            mNotices.add(n);
        }


    catch(Exception e){
        //Toast.makeText(mAppContext,"problem adding object",Toast.LENGTH_SHORT).show();
    }
    }
    void setEscapeNotice(int e)
    {
        objectEscapedNotice=e;
    }
    public static noticeBoard get(Context c,Activity act) {

        if ( sNoticeBoard== null) {

            sNoticeBoard = new noticeBoard(c.getApplicationContext(),0,act);
        }
        return sNoticeBoard;
    }
    public notice getNotice(String pos) {
        for (notice c : mNotices) {
            if (c.getTitle().equals(pos))
                return c;
        }
        return null;
    }

    private class GetNotice extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(activity);
        @Override
        protected String doInBackground(String... arg0) {


            try {


                link = HOST+"SendNotice.php?escape=0";
               // link = "http://binarycalc.host22.com/findresult.php?roll=1&class=11&faculty=Science";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(link);
                HttpResponse response = client.execute(request);
                String s = EntityUtils.toString(response.getEntity());
                return s;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            try
            {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                //Log.d("AA",result);
                JSONArray array = (JSONArray) new JSONTokener(result)
                        .nextValue();
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jO = array.getJSONObject(i);
                    notice n = new notice();
                    n.setDescription(jO.getString("notice"));
                    //Log.d("AKK", jO.getString("notice"));
                    n.setId(UUID.randomUUID());
                    n.setNotice_num(jO.getInt("notice_no"));
                    n.setCategory("Normal");
                    // n.setTitle(Integer.toString(jO.getInt("notice_no")));
                    n.setTitle("OK #" + i);
                    //Log.d("ssss", "11");

                    mNotices.add(n);
                }


            }
            catch(Exception e)
            {

            }

        }
    }


    private class Dummy extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {

           return "done";
        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {


        }
    }
}
