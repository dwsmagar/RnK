package com.susankya.rnk;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static final String BROADCAST_ACTION = "com.susankya.schoolvalley.broadcastReceiver";
    public static final String BROADCAST_ACTION_UTUCHAT = "com.susankya.schoolvalley.broadcastReceiver.usertouserchat";
    public static final String BROADCAST_ACTION_UTUCHAT_LIST = "com.susankya.schoolvalley.broadcastReceiver.usertouserchat";
    public static final String BROADCAST_ACTION_activity = "com.susankya.schoolvalley.broadcastReceiver.activity";
    public static final String ACTION="action_of_intent";
    public static final String ACTION_CHAT="action_chat";
    public static final String ACTION_CHAT_UTU="action_chat_UTU";
    private static String prevSection,section;

    private static SQLiteDatabase db;
    private SQLiteHelper sqLiteHelper;
    final int UserToUser=1,UserTOCollege=2;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
if(!getSharedPreferences("rememberlogin", Context.MODE_PRIVATE).getBoolean("isloggedin",false))
    return;
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d("PVL", "MESSAGE RECEIVED!!");
        //sendNotification("messageReceived");
       // Toast.makeText(getApplicationContext(),"Recieved", Toast.LENGTH_SHORT).show();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String,String> data=remoteMessage.getData();

            //Log.d(TAG, "Message data payload: " + data.get("message")+" "+data.get("data"));
            //Log.d(TAG, "Message data payload: " +data.get("data"));
            try{
            JSONObject job=new JSONObject(data.get("data").toString());
                Log.d(TAG, "onMessageReceived: utuchat"+data.get("data").toString());
            if(job.getString("title").equals("Message")) {

                JSONObject obj=new JSONObject(data.get("data"));
                JSONObject messageObject=new JSONObject(obj.get("message").toString());
                //Log.d(TAG, "Message data payload: " +messageObject);
                String messageText="",sender="";
                if(messageObject.has("message"))
                    messageText=messageObject.getString("message");
                if(messageObject.has("sent_by"))
                    sender=messageObject.getString("sent_by");
                Intent intent1=new Intent(BROADCAST_ACTION_activity);
                sendBroadcast(intent1);
                Intent intent=new Intent(BROADCAST_ACTION);
                intent.putExtra("sent_by",sender);
                intent.putExtra("msg",messageText);
                sendBroadcast(intent);
                boolean isReceiverRegistered=getSharedPreferences(ChatFragment.PREF_RECEIVER,MODE_PRIVATE).getBoolean(ChatFragment.PREF_RECEIVER,false);
                //Log.d(TAG, "onMessageReceived: receiver"+isReceiverRegistered);
                if(!isReceiverRegistered)
                sendNotification("You have a new message",sender,messageText);
            }


                else if(job.getString("title").equals("Routine")){
                final SharedPreferences sp=getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);

                sqLiteHelper = new SQLiteHelper(getApplicationContext());

                db = sqLiteHelper.getWritableDatabase();
                section=sp.getString(ViewRoutineFragment.SECTION, "Select Your Section");

                if(!section.equals("Select Your Section"))
                    prevSection=section;
                JSONObject obj=new JSONObject(data.get("data"));
                JSONObject messageObject=new JSONObject(obj.get("message").toString());
                JSONObject job1=new JSONObject(messageObject.get("message").toString());
               final String section=messageObject.get("section_name").toString();
                //Log.d(TAG, "onMessageReceived: "+section);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        routineFetcher(section);
                    }
                }, 500);

            } else if(job.getString("title").equals("UserToUserChat")){

                JSONObject obj=new JSONObject(data.get("data"));
                JSONObject messageObject=new JSONObject(obj.get("message").toString());
                //Log.d(TAG, "Message data payload: " +messageObject);
                String messageText="",sender="",sender_user_no="",firstnameofSender="",lastnameofSender="",user_id_of_sender="";
                if(messageObject.has("message"))
                    messageText=messageObject.getString("message");
                if(messageObject.has("sent_by"))
                    sender=messageObject.getString("sent_by");//semt by and user number are same thing
                if(messageObject.has("user_no"))
                    sender_user_no=messageObject.getString("user_no");
                if(messageObject.has("firstName"))
                    firstnameofSender=messageObject.getString("firstName");
                if(messageObject.has("lastName"))
                    lastnameofSender=messageObject.getString("lastName");
                if(messageObject.has("userid"))
                    user_id_of_sender=messageObject.getString("userid");
                Intent intent1=new Intent(BROADCAST_ACTION_activity);
                sendBroadcast(intent1);
                Intent intent=new Intent(BROADCAST_ACTION_UTUCHAT+sender);
                intent.putExtra("sent_by",sender);
                intent.putExtra("msg",messageText);
                intent.putExtra("senderName",firstnameofSender+" "+lastnameofSender);
                intent.putExtra("senderUsername",user_id_of_sender);
                sendBroadcast(intent);

                Intent intentForChatList=new Intent(BROADCAST_ACTION_UTUCHAT_LIST);
                intentForChatList.putExtra("sent_by",sender);
                intentForChatList.putExtra("msg",messageText);
                intentForChatList.putExtra("senderName",firstnameofSender+" "+lastnameofSender);
                intentForChatList.putExtra("senderUsername",user_id_of_sender);
                sendBroadcast(intentForChatList);

                boolean isReceiverRegistered=getSharedPreferences(UserToUserChatFragment.PREF_RECEIVER_UTUCHAT,MODE_PRIVATE).getBoolean(UserToUserChatFragment.PREF_RECEIVER_UTUCHAT+sender,false);
                if(!isReceiverRegistered)
                    sendNotification(firstnameofSender+" "+lastnameofSender+" sent you a message",sender,messageText,firstnameofSender+" "+lastnameofSender);

            }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }}


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification( remoteMessage.getNotification().getBody());
            Intent intent=new Intent(BROADCAST_ACTION);
            intent.putExtra("fromu",remoteMessage.getFrom());
            intent.putExtra("msg",remoteMessage.getNotification().getBody());
            intent.putExtra("fromname","Abhinav");
            //  MSGReceiver.completeWakefulIntent(intent);
            sendBroadcast(intent);
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String messageBody,String sender,String messageText) {
            Intent intent = new Intent(this, NavDrawerActivity.class);
            intent.putExtra(ACTION, ACTION_CHAT);
            intent.putExtra("sent_by", sender);
            intent.putExtra("msg", messageText);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_chat)
                    .setContentTitle("Message")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }    private void sendNotification(String messageBody,String sender,String messageText,String sender_name) {
            Intent intent = new Intent(this, NavDrawerActivity.class);
            intent.putExtra(ACTION, ACTION_CHAT_UTU);
            intent.putExtra("sent_by", sender);
            intent.putExtra("msg", messageText);
            intent.putExtra("senderName", sender_name);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_chat)
                    .setContentTitle(sender_name)
                    .setContentText(messageText)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(Integer.parseInt(sender) /* ID of notification */, notificationBuilder.build());

    }
    void routineFetcher(final String section)
    {

        final SharedPreferences sp=getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String link = FragmentCodes.NEW_HOST + "FetchRoutine.php";

        new PhpConnect(link,"Loading...",getApplicationContext(),0,new String[]{FragmentCodes.CMDXXX
                ,Utilities.getDatabaseName(getApplicationContext()),section,Utilities.getSN(getApplicationContext())}
                ,new String[]{"cmdxxx","dbName","section_name","user_no"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                //Log.d(TAG, "onConnectListener: routine"+res);
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
            /*    sp.edit().putBoolean(FragmentCodes.HASSETROUTINE, true).apply();
                sp.edit().putString(FragmentCodes.SECTION, section).apply();
                ViewRoutinePagerFragment.changeName(section);
                ViewRoutinePagerFragment.SectionViewFooter.setText("Change section");
                ViewRoutinePagerFragment.updateAdapter();*/
                updateRoutineWidget(getApplicationContext());

            }
        });
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

    private void savePeriodRow(JSONObject jsonObject)
    {
        try
        {
            ContentValues cv = new ContentValues();
            cv.put(FragmentCodes.DAY, jsonObject.getString("day"));
            cv.put(FragmentCodes.PERIOD_NO, jsonObject.getString("period_no"));
            cv.put(FragmentCodes.SUBJECT, jsonObject.getString("subject"));
            cv.put(FragmentCodes.SECTION_NAME, jsonObject.getString("section_name"));
            cv.put(FragmentCodes.TEACHER_NAME, jsonObject.getString("teacher_name"));
            cv.put(FragmentCodes.START, jsonObject.getString("start"));
            cv.put(FragmentCodes.END, jsonObject.getString("end"));
            cv.put(FragmentCodes.COLLEGE_SN, jsonObject.getString("college_sn"));
            db.insert(FragmentCodes.TABLENAME, "null", cv);
        }
        catch (Exception e)
        {
            //Log.d("savePeriodRow",e+"");
        }

    }
    private static boolean routineExists(String section){

        try
        {
            String[] columns=new String[]{FragmentCodes.DAY,FragmentCodes.PERIOD_NO,FragmentCodes.SUBJECT,
                    FragmentCodes.SECTION_NAME,FragmentCodes.TEACHER_NAME,FragmentCodes.START,
                    FragmentCodes.END,FragmentCodes.COLLEGE_SN};//C_SECTION+"= ? && "+
            Cursor c=db.query(FragmentCodes.TABLENAME,columns,FragmentCodes.SECTION_NAME+"= ?",new String[]{section},null,null,null);
            //Log.d("yetaxa",c.getCount()+"");
            return !(c.getCount()==0);
        }
        catch (Exception e)
        {

            return false;
        }

    }
    private static boolean deleteRoutine(String section){
        int c=db.delete(FragmentCodes.TABLENAME,FragmentCodes.SECTION_NAME+"= ?",new String[]{section});
        return (c>0);

    }


}
        //MSGReceiver.completeWakefulIntent(intent);

//dVXqolPDWV0:APA91bHVP_TaYgJzARmaFTH8ZP9yqTLfzdPlQFXUtS4p56-sgxPx8J4pS5-disrCPbjTQqYbdx7lOQ3lSFEcradQw-JyHHxvmlMKAc5oIhAAx3eoa59iwQREERQfXKy0xAJWrLI149Uv