package com.susankya.rnk;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.Calendar;


/**
 * Implementation of App Widget functionality.
 */

public class RoutineWidget extends AppWidgetProvider implements FragmentCodes {
    public static final String FORCE_UPDATE="com.timex.greenland.RoutineWidget.FORCE_UPDATE";
    private String days[]={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Routine routine = new Routine();
        final SharedPreferences sp=context.getSharedPreferences("Public", Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        //Log.d("rWidget",day+"");
                   final int N = appWidgetIds.length;
            for (int i = 0; i < N; i++) {
                RemoteViews remoteViews = updateWidgetListView(context,
                        appWidgetIds[i]);
                appWidgetManager.updateAppWidget(appWidgetIds[i],
                        remoteViews);
            }


    }
    @Override
    public void onReceive(Context context,Intent intent){
        //Log.d(TAG, "onReceive: "+"reached");
        super.onReceive(context,intent);
        ComponentName componentName=new ComponentName(context,RoutineWidget.class);
        if(FORCE_UPDATE.equals(intent.getAction())){
            try {
                AppWidgetManager awm = AppWidgetManager.getInstance(context);
                onUpdate(context,awm,awm.getAppWidgetIds(componentName));
            }
            catch (Exception e){
                //Log.d(TAG, "onReceive: "+e.toString());
                //Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }


    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),R.layout.routine_widget_layout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, RoutineListService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.putExtra("dayBoy",day);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                svcIntent);
        remoteViews.setTextViewText(R.id.day,days[day]);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }
  /*
   @Override
    public void onReceive(Context context,Intent intent){
        super.onReceive(context,intent);
        ComponentName componentName=new ComponentName(context,RoutineWidget.class);
        if(FORCE_UPDATE.equals(intent.getAction())){
            try {
                AppWidgetManager awm = AppWidgetManager.getInstance(context);
                onUpdate(context,awm,awm.getAppWidgetIds(componentName));
            }
            catch (Exception e){
                //Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Routine routine = new Routine();
        final SharedPreferences sp=context.getSharedPreferences("Public", Context.MODE_PRIVATE);
        boolean morning=sp.getBoolean(SettingsFragment.SETTINGS_SHIFT,true);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat d2= new SimpleDateFormat("HH");
        String current_hour=d2.format(calendar.getTime());
        int hour=Integer.parseInt(current_hour);
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
      /*  if(morning) {
            if (hour >= 12) {
                day++;
                if (day == 7)
                    day = 0;
            }
            }
        else {
            if(hour>=16){
                day++;
                if (day == 7)
                    day = 0;
            }
        }
        //Log.d("rWidget",day+"");
         if (day != 6) {
            final int N = appWidgetIds.length;
            for (int i = 0; i < N; i++) {
                RemoteViews remoteViews = updateWidgetListView(context,
                        appWidgetIds[i],day);
                appWidgetManager.updateAppWidget(appWidgetIds[i],
                        remoteViews);
            }
        }

    }

    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId,int day) {

        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),R.layout.routine_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, RoutineListService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.putExtra("dayBoy",day);

        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.widgetlv,
                svcIntent);
        return remoteViews;
    }
    @Override
    public void onEnabled(Context context) {
        ComponentName componentName=new ComponentName(context,RoutineWidget.class);
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        onUpdate(context,awm,awm.getAppWidgetIds(componentName));
         }
static void show(Context c,String s){
    //Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
}
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

*/

}