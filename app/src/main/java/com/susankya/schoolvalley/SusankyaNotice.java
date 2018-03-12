package com.susankya.schoolvalley;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.UUID;


public class SusankyaNotice extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    public static final String LAST_NOTICE_NO="last notice no of sunsankya";
    private ArrayList<String> list=new ArrayList<String>();
    public static final String TITLE="title";
    public static final String POSITION="title";
    public static final String NOTICE_NUM="Notice_num";
    private ListView mNoticeList;
    TextView tv;
    private int noticeNum=0;
    LinearLayout ll;
    private View emptyView;
    private TextView emptyTextView;
    ProgressBar loadingPB;
    private ArrayList<notice> mNotices;
    Boolean isButtonPressed=false;
    private int firstTimeLoading=1;
    public static String mFilename="Notices_susankya";
    private int layout,itemClickedPos,itemAtPos;
    private adapt adapter;
    private View footer;
    private int sizeOfArray,prevSizeOfArray;
    ProgressBar pb;
    private int sourceCode=0;
    // private  GetNotice getNotice;
    private DeleteNotice deleteNotice;

    private int escape=0;

    private boolean firstLoad=true,reachedBottomOnce=true;

    public void showEmptyView(int val)
    {
        if(mNotices.isEmpty())
        {
            mNoticeList.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            if(val==NO_INTERNET)
                emptyTextView.setText("No internet connection");
            else if(val==LIST_EMPTY)
                emptyTextView.setText("Looks like there's no notice to load!");
loadingPB.setVisibility(View.GONE);
        }

    }
    public void removeEmptyView()
    {
        if(mNotices.isEmpty())
        {
            mNoticeList.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);

        }
    }
    @Override
    public void onCreate(Bundle SIS)
    {
        super.onCreate(SIS);
        //   getNotice=new GetNotice();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        try {
            sourceCode=((NavDrawerActivity)getActivity()).getZugad();
        }
        catch (Exception e){

        }
        deleteNotice=new DeleteNotice();
        //getNotice=new GetNotice();

    }
    public static SusankyaNotice newInstance(String param1) {
        SusankyaNotice fragment = new SusankyaNotice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SusankyaNotice(){
        mParam1="a";
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        prevSizeOfArray=0;
        sizeOfArray=0;
        if(NavDrawerActivity.getZugad()==0)
            ((NavDrawerActivity) getActivity()).onSectionAttached(2);
        getActivity().setTitle("Susankya Notice");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        notificationManager.cancel(NewNoticeService.NOTICE_FROM_SERVICE,11);
        View v= inflater.inflate(R.layout.nlfragment, container, false);
        footer=(View)getActivity().getLayoutInflater().inflate(R.layout.footerboy, null);
        tv=(TextView)v.findViewById(R.id.deleteNote);
        loadingPB=(ProgressBar)v.findViewById(R.id.progress);
        emptyTextView=(TextView)v.findViewById(R.id.emptyView);
        emptyView=inflater.inflate(R.layout.empty_list_view,container,false);
        mNotices=new ArrayList<notice>();
        adapter=new adapt(mNotices);
        try {
            mNoticeList = (ListView) v.findViewById(R.id.list);
            mNoticeList.addFooterView(footer);


            mNoticeList.setAdapter(adapter);
            mNoticeList.setOnScrollListener(
                    new EndlessScrollListener() {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount) {
                            if(noticeNum!=0 && mNotices.size()>=10)
                            {
                                footer.setVisibility(View.VISIBLE);
                                // getNotice=new GetNotice();
                                escape=mNotices.get(mNotices.size()-1).getNotice_num();

                                if(reachedBottomOnce)
                                {
                                    GetNotices();
                                    reachedBottomOnce=false;
                                }
                            }
                            else
                                footer.setVisibility(View.INVISIBLE);


                        }
                    }
            );
            //  if(((NavDrawerActivity)getActivity()).isLoggedIn())
            registerForContextMenu(mNoticeList);
        }
        catch(Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        //     getNotice=new GetNotice();
        if(firstLoad)
        {
            loadingPB.setVisibility(View.VISIBLE);
            GetNotices();
            firstLoad=false;
        }

        else
        {


            if(((NavDrawerActivity)getActivity()).getArrayList()!=null)
                mNotices=((NavDrawerActivity)getActivity()).getArrayList();
            else
                mNotices=((ActivityForFragment)getActivity()).getArrayList();
            adapter=new adapt(mNotices);
            mNoticeList.setAdapter(adapter);

        }

        ((NavDrawerActivity)getActivity()).setArrayList(mNotices);
        ActivityForFragment.setArrayList(mNotices);

        if(sourceCode==1){
            tv.setText("Long press list item to delete it");
            mNoticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    notice n = (notice) parent.getAdapter().getItem(position);

                /*   Bundle b=new Bundle();
                    b.putString("title",n.getTitle());
                    b.putString("description",n.getDescription());
                    b.putString("date",n.getDate());
                    FragmentManager fm = getFragmentManager();
                   Notice dialog=new Notice();
                    dialog.setArguments(b);
                    dialog.show(fm, "SHOWING");*/

                }
            });
        }
        else {
            tv.setVisibility(View.INVISIBLE);
            mNoticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    notice n = (notice) parent.getAdapter().getItem(position);
                    android.support.v4.app.Fragment fragment = null;
                    int num = n.getNotice_num();
                    itemClickedPos = num;
                    Bundle b=new Bundle();
                    b.putString("title",n.getTitle());
                    b.putString("description",n.getDescription());
                    b.putString("date",n.getDate());
                    FragmentManager fm = getFragmentManager();
                    Notice dialog=new Notice();
                    dialog.setArguments(b);
                    dialog.show(fm, "SHOWING");

                }
            });
        }



        return v;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final int position = info.position;
        final adapt adapter= (adapt) getListAdapter();
        notice n= (notice)adapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.menu_item_delete_notice:{
                try{
                    String link="http://46.101.81.232/App/New App/ChhalFaal/SusankyaNotice/deleteNotice.php";
                    int number=n.getNotice_num();
                    new PhpConnect(link,"Deleting..",getActivity(),1,new String[]{CMDXXX, Utilities.getDatabaseName(getActivity().getApplicationContext()), Integer.toString(number)},new String[]{"cmdxxx","dbName","num"}).setListener(new PhpConnect.ConnectOnClickListener() {
                        @Override
                        public void onConnectListener(String res) {
                            //   Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
                            if(res.trim().toString().equals("1"))
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "Notice deleted successfully.", Toast.LENGTH_SHORT).show();
                                adapter.remove(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
                            }
                            else
                                Toast.makeText(getActivity().getApplicationContext(), "Failed to delete notice", Toast.LENGTH_SHORT).show();
                            showEmptyView(LIST_EMPTY);
                        }
                    });
                 /*   deleteNotice=new DeleteNotice();
                    itemAtPos=position;
                    int number=n.getNotice_num();
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                           deleteNotice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Integer.toString(number));
                        else
                            deleteNotice.execute(Integer.toString(number));

                    } else {
                        Toast.makeText(getActivity().getApplicationContext().getApplicationContext(), "NO CONNECTION", Toast.LENGTH_SHORT).show();

                    }*/
                }catch (Exception e){
                    //  Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.notice_delete, menu);
    }

    public Object getListAdapter() {
        return adapter;
    }


    private class adapt extends ArrayAdapter<notice> {
        public adapt(ArrayList<notice> c){
            super(getActivity(),0,c);
        }
        @Override
        public View getView(int pos,View v,ViewGroup vg){
            if(v==null){
                v=getActivity().getLayoutInflater().inflate(R.layout.notice_list,null);
            }
            String Date="";
            notice c=getItem(pos);
          LinearLayout ll=(LinearLayout)v.findViewById(R.id.main);
            final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_top);
            TextView tv0=(TextView)v.findViewById(R.id.date_content);
            tv0.setText(c.getDate()+" "+c.getTime());
            TextView tv1=(TextView)v.findViewById(R.id.title_content);
            final View whiteLine=v.findViewById(R.id.white_line);
            tv1.setText(c.getTitle());
            final TextView tv2=(TextView)v.findViewById(R.id.description_content);
            if(c.getDescription().length()<30)
                tv2.setText(c.getDescription().toString());
            else
                tv2.setText(c.getDescription().toString().substring(0,30)+"....");
            final TextView value=(TextView) v.findViewById(R.id.notice);
            value.setVisibility(View.GONE);
            value.setText(c.getDescription().toString());
       ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!value.isShown()){
                        value.setVisibility(View.VISIBLE);
                        value.startAnimation(animation);
                        tv2.setVisibility(View.GONE);
                        whiteLine.setVisibility(View.VISIBLE);
                    }
                    else if(value.isShown()){
                        tv2.setVisibility(View.VISIBLE);
                        value.setVisibility(View.GONE);
                        whiteLine.setVisibility(View.GONE);

                    }
                }
            });
            return v;
        }

    }


    private class DeleteNotice extends AsyncTask<String,Integer,String>
    {
        private ProgressDialog dialog=new ProgressDialog(getActivity());

        @Override
        protected String doInBackground(String... arg)
        {
            String noticenum=arg[0];

            String link;
            try{
                link = NEW_HOST+"deleteNotice.php?num="+noticenum;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(link);
                HttpResponse response = client.execute(request);
                String s= EntityUtils.toString(response.getEntity());
                //Log.d("DDD", "SSS");
                return s;
            }

            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPreExecute() {
            dialog.setCancelable(false);
            dialog.setMessage("Deleting...");
            dialog.show();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            //Log.d("yy", result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(result.equals("1"))
            {
                Toast.makeText(getActivity().getApplicationContext(), "Notice deleted successfully.", Toast.LENGTH_SHORT).show();
                adapter.remove(adapter.getItem(itemAtPos));
                adapter.notifyDataSetChanged();

            }
            else
                Toast.makeText(getActivity().getApplicationContext(), "Failed to delete notice", Toast.LENGTH_SHORT).show();
            showEmptyView(LIST_EMPTY);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
    public void GetNotices(){
        String link = "http://46.101.81.232/App/New App/ChhalFaal/SusankyaNotice/RetrieveNotice.php";
        if(Utilities.isConnectionAvailable(getActivity())){
            new PhpConnect(link,"Loading...",getActivity(),0,new String[]{"2568wwexve", Utilities.getDatabaseName(getActivity()),escape+""},new String[]{"cmdxxx","dbName","escape"}).setListener(
                    new PhpConnect.ConnectOnClickListener() {
                        @Override
                        public void onConnectListener(String result) {
                            try {
                                String Date = "";
                                //Log.d("AA", result);
                                save(result);
                                JSONArray array = (JSONArray) new JSONTokener(result)
                                        .nextValue();
                                DateConverter dc;
                                dc = new DateConverter();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jO = array.getJSONObject(i);
                                    notice n = new notice();
                                    n.setDescription(jO.getString("notice"));
                                    //Log.d("AKK", jO.getString("notice"));
                                    n.setId(UUID.randomUUID());
                                    n.setNotice_num(jO.getInt("notice_no"));
                                    n.setCategory(jO.getString("category"));
                                    String dateJO = jO.getString("date");
                                    String time = jO.getString("time");
                                    n.setTime(time);
                                    try {
                                        String[] months = new String[]{"babaJiKaThullu", "Baishak", "Jestha", "Ashar", "shrawan", "Bharda", "Ashoj", "Kartik", "Mangsir", "Poush", "Magh", "Falgun", "Chaitra"};
                                        String[] date = dateJO.split("-");
                                        if ((Integer.parseInt(date[0]) >= 2072)) {
                                            dateJO = months[Integer.parseInt(date[1])] + " " + Integer.parseInt(date[2]) + ", " + date[0];
                                        } else {
                         /*  dc.convert(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
                         int month,day,year;
                           year=dc.getyear();
                           month=dc.getMonths();
                           day=dc.getDays();
                           dateJO = months[month] + " " + day + ", " + year;*/
                                        }
                                    } catch (Exception e) {
                                        //  Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                    n.setDate(dateJO);
                                    n.setTitle("#" + Integer.toString(jO.getInt("notice_no")));

                                    //Log.d("ssss", "11");
                                    mNotices.add(n);
                                    //adapter.add(n);
                                }
                                prevSizeOfArray = sizeOfArray;
                                sizeOfArray = mNotices.size();
                                SharedPreferences sp = getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor e = sp.edit();
                                notice n = mNotices.get(0);
                                noticeNum = n.getNotice_num();
                                e.putInt(LAST_NOTICE_NO, n.getNotice_num());
                                //Toast.makeText(getActivity(),""+n.getNotice_num(),Toast.LENGTH_SHORT).show();
                                e.commit();
                                loadingPB.setVisibility(View.GONE);
                                loadOlderNotices();
                            } catch (Exception e) {
                                //
                                //  Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                            }
                            // mNoticeList.addFooterView(footer);

                            showEmptyView(LIST_EMPTY);
                            reachedBottomOnce = true;

                        }
                    });

        }else{
            tv.setVisibility(View.VISIBLE);
            tv.setText("No Internet Connection");
            tv.setTextColor(getResources().getColor(R.color.red));
            loadingPB.setVisibility(View.GONE);
          //  emptyTextView.setVisibility(View.VISIBLE);
           // emptyTextView.setText("No Internet Connection");
            //Toast.makeText(getActivity().getApplicationContext(), "NO CONNECTION. Connect to Internet to view latest notices.", Toast.LENGTH_SHORT).show();
            reachedBottomOnce=true;
            setter(load());
        }
    }


    private void loadOlderNotices()
    {

        if(sizeOfArray==prevSizeOfArray)
        {
            Toast.makeText(getActivity().getApplicationContext(), "No older notices", Toast.LENGTH_SHORT).show();
            footer.setVisibility(View.INVISIBLE);
        }

        else
            adapter.notifyDataSetChanged();
        if(!mNotices.isEmpty())
            firstTimeLoading=0;
    }
    /*private boolean connectToPHP()
       {
          removeEmptyView();
           ConnectivityManager connMgr = (ConnectivityManager)
                   getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
           if (networkInfo != null && networkInfo.isConnected()) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                   getNotice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
               else
                 getNotice.execute();
               return true;
           } else {
               tv.setVisibility(View.GONE);
               Toast.makeText(getActivity().getApplicationContext(), "NO CONNECTION. Connect to Internet to view latest notices.", Toast.LENGTH_SHORT).show();

               reachedBottomOnce=true;
               setter(load());
               return false;
           }

       }*/
    void setter(String result){
        try{
            ArrayList<notice> mNoticesnew=new ArrayList<notice>();
            JSONArray array = (JSONArray) new JSONTokener(result)
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jO = array.getJSONObject(i);
                notice n = new notice();
                n.setDescription(jO.getString("notice"));
                //Log.d("setter", jO.getString("notice"));
                n.setId(UUID.randomUUID());
                n.setNotice_num(jO.getInt("notice_no"));
                n.setCategory(jO.getString("category"));
                String date = jO.getString("date");
                String time = jO.getString("time");
                n.setTime(time);
                n.setDate(date);
                n.setTitle("Notice #" + Integer.toString(jO.getInt("notice_no")));
                //Log.d("ssss", "11");
                mNoticesnew.add(n);
                //  Toast.makeText(getActivity(),mNotices.toString(),Toast.LENGTH_SHORT).show();
            }
            //Log.d("AA", result);
            prevSizeOfArray=sizeOfArray;
            sizeOfArray=mNoticesnew.size();
            //Log.d("fromParticular notice", mNoticesnew.get(0).getDescription());
            mNotices=mNoticesnew;
            adapter=new adapt(mNoticesnew);
            mNoticeList.setAdapter(adapter);
            if(mNotices.size()==0)
                tv.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            // loadOderNotices();
        }
        catch (Exception e){
            //      Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }
        showEmptyView(NO_INTERNET);
    }
    public void save(String array)
    // throws JSONException, IOException
    {

        Writer writer = null;
        try {
            OutputStream out = getActivity().getApplicationContext()
                    .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());


        }
        catch(Exception e ){

        }
        finally {
            if (writer != null)
                try {
                    writer.close();
                }
                catch (Exception e){

                }
        }
    }
    public String load()// throws IOException, JSONException
    {
        String line=null;
        ArrayList<String> crimes = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            InputStream in = getActivity().getApplicationContext().openFileInput(  mFilename);
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
    public static class Notice extends android.support.v4.app.DialogFragment {
        private String title,description,date;

        public Notice(){

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(getArguments()!=null){
                title=getArguments().getString("title",null);
                description=getArguments().getString("description",null);
                date=getArguments().getString("date",null);
            }
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
            View v = inflater.inflate(R.layout.notice_dialog, null, false);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


            getDialog().getWindow().setLayout((6 * width)/7, (4 * height)/5);
            //getDialog().getWindow().setLayout((6 * width)/7, (4 * height)/5);
            TextView titleTv=(TextView)v.findViewById(R.id.title);
            titleTv.setText(title);
            TextView tv=(TextView)v.findViewById(R.id.notice);



            try
            {
                tv.setTypeface(Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/segoepr.ttf"));
            }
            catch (Exception e)
            {
                //Log.d("ttf", e.toString());
            }

            tv.setText(description);
            TextView Date=(TextView)v.findViewById(R.id.date);
            Date.setText("Published on: "+date);
            return v;
        }
    }

}
