package com.susankya.rnk;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class ObjectiveResult extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private ListView lv;
    private ArrayList<Result> results;
    JSONArray array;
    private String mParam1=null;
    private ProgressBar pb;
    private int num;
    private String mParam2=null;
    private TextView rollno,name,marks;
    public static ObjectiveResult newInstance(String param1) {
        ObjectiveResult fragment = new ObjectiveResult();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
            fragment.setArguments(args);
        return fragment;
    }

    public ObjectiveResult() {
        mParam1=null;
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1, null);
          }
        results=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.objective_result, container, false);
        lv=(ListView)v.findViewById(R.id.list_result);
        rollno=(TextView)v.findViewById(R.id.rollno);
        name=(TextView)v.findViewById(R.id.name);
        marks=(TextView)v.findViewById(R.id.marks);
        if(mParam1!=null)
    publishResult(mParam1);
    //    connectToPHP(mParam1);
        return v;
    }
private void publishResult(String result){
    try
    {
        array = (JSONArray) new JSONTokener(result)
                .nextValue();
        for (int i = 0; i < array.length(); i++){
            Result result1=new Result();
            JSONObject job=  array.getJSONObject(i);
            result1.setName(job.getString("Name"));
            result1.setPos(i);
            result1.setMarks(job.getInt("Marks"));
            result1.setRoll(job.getInt("Roll"));
            results.add(result1);
        }
        adapt adapter=new adapt(results);
        //Toast.makeText(getActivity(),results.get(1).getName(),Toast.LENGTH_SHORT).show();
        lv.setAdapter(adapter);
        ResultActivity.setListView(lv,results);

    }
    catch(Exception e)
    {
        //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
    }
}
    private boolean connectToPHP(int Class )
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new GetAllResult().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Integer.toString(Class));
            else
                new GetAllResult().execute(Integer.toString(Class));
            return true;
        }
        else
            return false;
    }

    private class GetAllResult extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected String doInBackground(String... arg0) {


            try {
                String Class=arg0[0];
                String link = HOST+"ExtractTheResult/ExtractTheResultOBJ.php?class="+Class;
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
            ////    pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            //       pb.setVisibility(View.INVISIBLE);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            //Log.d("OBJ",result);
            //Toast.makeText(getActivity(),result.toString().trim(),Toast.LENGTH_LONG).show();
            try
            {
                array = (JSONArray) new JSONTokener(result)
                        .nextValue();
                for (int i = 0; i < array.length(); i++){
                    Result result1=new Result();
                    JSONObject job=  array.getJSONObject(i);
                    result1.setName(job.getString("Name"));
                   result1.setMarks(job.getInt("Marks"));
                    result1.setPos(i);
                   result1.setRoll(job.getInt("Roll"));
                    results.add(result1);
                }
                adapt adapter=new adapt(results);
                ResultActivity.setListView(lv,results);
               // Toast.makeText(getActivity(),results.get(1).getName(),Toast.LENGTH_SHORT).show();
                lv.setAdapter(adapter);

            }
            catch(Exception e)
            {
                //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ///         pb.setMax(100);
            //     pb.setProgress(values[0]);
        }
    }
    private class adapt extends ArrayAdapter<Result> {
        public adapt(ArrayList<Result> c) {
            super(getActivity(), 0, c);
        }
private TextView rollno,marks,name;
        @Override
        public View getView(final int pos, View v, ViewGroup vg) {
            try {
                if (v == null) {
                    v = getActivity().getLayoutInflater().inflate(R.layout.objective_result_row, null);
                }
                Result result = getItem(pos);
                rollno = (TextView) v.findViewById(R.id.rollno);
                name = (TextView) v.findViewById(R.id.name);
                marks = (TextView) v.findViewById(R.id.marks);
                name.setText(result.getName());
                rollno.setText(""+result.getRoll());
                marks.setText(""+result.getMarks());
            }
            catch (Exception e){
                //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
            return v;
        }

    }


}

