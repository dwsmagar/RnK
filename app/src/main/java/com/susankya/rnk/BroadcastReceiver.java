package com.susankya.rnk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class BroadcastReceiver extends android.content.BroadcastReceiver {
    private boolean isNoticeNotificationOn,isNotificationOn;
    @Override
    public void onReceive(Context context, Intent intent) {

        final SharedPreferences sp=context.getSharedPreferences("Public", Context.MODE_PRIVATE);
      //  sp.edit().putBoolean(NewNoticeService.NOTIFICATION_TAG,true).commit();
        isNoticeNotificationOn =sp.getBoolean(SettingsFragment.SETTINGS_NOTICE,true);
        isNotificationOn=sp.getBoolean(SettingsFragment.SETTINGS_NOTIFICATION,true);
           if(isOnline(context)){
               if(!NewNoticeService.isServiceAlarmOn(context)&& isNoticeNotificationOn)
                {
                    NewNoticeService.setServiceAlarm(context, true);
                 }

               }
        else {
                if(!NewNoticeService.isServiceAlarmOn(context)&& isNoticeNotificationOn)
                {
                  NewNoticeService.setServiceAlarm(context, true);
                }

                if(!isNoticeNotificationOn && NewNoticeService.isServiceAlarmOn(context)) {
                    NewNoticeService.setServiceAlarm(context, false);
                //    Toast.makeText(context, "service off", Toast.LENGTH_SHORT).show();
                }
            }

    }
    private boolean isOnline(Context context){
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        return (netInfo!=null&& netInfo.isConnected());
    }
}
