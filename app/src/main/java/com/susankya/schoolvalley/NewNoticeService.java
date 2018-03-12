package com.susankya.schoolvalley;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

public class NewNoticeService extends IntentService implements FragmentCodes {
private static String TAG="servic";
    public static final String NOTICE_FROM_SERVICE="notice from service";
    public static String NOTICESERVICE="notice is from here";
    public static final String NOTICE_NUMBER_OF_LAST_NOTIFICATION="notice ko last number";
    public static final String HAS_SENT_NOTIFICATION="has sent notification";
    public static String NOTICESERVICE1="notice is from here1";
private String institute_Name="Institute\'s ";
    private String singularNotice=" unread notice.";
    private String pluralNotice=" unread notices.";
    public static final String LAST_NOTICE_NO="last notice no";
private static final int INTERVAL=1000*15;
    int last_notice_no;
    public static String ACTION="com.timex.greenland.NewNoticeService.ACTION";
    int notices=0;
   // public static String NOTIFICATION_TAG="tag for our sarkari battery khane notification";
    public NewNoticeService() {
        super(TAG);
    }
//private static PendingIntent pi;
    private static Context mContext;
    private static boolean isAlarmOn;
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sp=getApplicationContext().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
       last_notice_no=sp.getInt(LAST_NOTICE_NO,0);
          ConnectivityManager cm = (ConnectivityManager)
          getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        boolean isNetworkAvailable = cm.getBackgroundDataSetting() &&
                cm.getActiveNetworkInfo() != null;
        institute_Name=Utilities.getInstitution(getApplicationContext())+"\'s Notice";
        if (!isNetworkAvailable)
            return;
String link=NEW_HOST+"GetNumberOfUnseenNotices.php";
        //Log.d(TAG, "onHandleIntent: 3"+Utilities.getDatabaseName(getApplicationContext()));
        new PhpConnect(link,"",getApplicationContext(),0,new String[]{CMDXXX,UtilitiesAdi.giveMeSN(getApplicationContext(),Utilities.getDatabaseName(getApplicationContext())),last_notice_no+"",Utilities.getUserNumber(getApplicationContext())+""},new String[]{"cmdxxx","college_sn","noticeNumber","user_no"})
                .setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                try {
                    String text=singularNotice;
                    //  Toast.makeText(getApplicationContext(),"from Notice  "+result.toString(),Toast.LENGTH_LONG).show();
                    JSONObject j=new JSONObject(res);
                    int count=j.getInt("count");
                    int blocked=j.getInt("blocked");

                    //Log.d(TAG, "onConnectListener: count"+count);
                    final SharedPreferences sp=getApplicationContext().getSharedPreferences(PUBLIC_SHAREDPREFERENCE,MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putInt("blocked",blocked);
                    //Log.d("blockedc",blocked+"");
                    editor.commit();
                    {
                        //Log.d(TAG, "last notice: "+last_notice_no+" "+sp.getInt(NOTICE_NUMBER_OF_LAST_NOTIFICATION,-1));
                        //  Toast.makeText(getApplicationContext(),""+sp.getInt(LAST_NOTICE_NO,0)+" "+sp.getInt(NOTICE_NUMBER_OF_LAST_NOTIFICATION,-1),Toast.LENGTH_SHORT).show();
                        //if(last_notice_no!=sp.getInt(NOTICE_NUMBER_OF_LAST_NOTIFICATION,-1))
                       if(count!=0&&!sp.getBoolean(HAS_SENT_NOTIFICATION,false))
                        {
                            //Log.d(TAG, "here");
                            editor.putInt(NOTICE_NUMBER_OF_LAST_NOTIFICATION,last_notice_no);
                            editor.commit();
                            Intent i = new Intent(getApplicationContext(),NavDrawerActivity.class);
                            sp.edit().putBoolean("abcd",true).commit();
                            if(count==1)
                            {
                                text=singularNotice;
                            }
                            else text=pluralNotice;
                            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
                            Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                    .setTicker("Unread Notice")
                                    .setSmallIcon(R.drawable.ic_action_notice_notification)
                                    .setContentTitle(institute_Name)
                                    .setContentText("You have " + count + text)
                                    .setContentIntent(pi)
                                    .setAutoCancel(true).build();
                            notification.sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            notificationManager.notify(NOTICE_FROM_SERVICE,11, notification);
                            editor.putBoolean(HAS_SENT_NOTIFICATION,true).commit();
                        }


                    }
                }catch (Exception e){
                    //Log.d(TAG, "onConnectListener: "+e.toString());
                    //  Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
          }

    public static void setServiceAlarm(Context context, boolean isOn) {
        mContext=context;
     //   Toast.makeText(context,isOn?"notice on":"Notice off",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, NewNoticeService.class);
   PendingIntent pi = PendingIntent.getService(
           context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC,
            System.currentTimeMillis(), INTERVAL, pi);
          //  Toast.makeText(context,"Alarm set",Toast.LENGTH_SHORT).show();
        }
        else
        {
            alarmManager.cancel(pi);
            pi.cancel();
       //    Toast.makeText(context,"Alarm unset",Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isServiceAlarmOn(Context context) {
        Intent i = new Intent(context, NewNoticeService.class);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    }




