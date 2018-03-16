package com.susankya.rnk;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class testFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private String jsonString;
public static String mFilename="Temp.txt";
    private String mParam1;
    private String mParam2;
private TextView[] t=new TextView[3];
    private ViewPager pager;
    public static testFragment newInstance(String param1, String param2) {
        testFragment fragment = new testFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public testFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private String s(double val)
    {
        return Double.toString(val);
    }
    private String s(int val)
    {
        return Integer.toString(val);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        JSONArray array=new JSONArray();
        View v= inflater.inflate(R.layout.fragment_test, container, false);
        AutoCompleteTextView tv=(AutoCompleteTextView)v.findViewById(R.id.test);
       /* pager=(ViewPager)v.findViewById(R.id.pager);
        final ActionBar actionBar = getActivity().getActionBar();
        try
        {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }*/
        return v;
    }
    public String load(String mFilename)// throws IOException, JSONException
    {
        String line=null;
        ArrayList<String> crimes = new ArrayList<String>();
        String[] str=new String[4];
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = getActivity().getApplicationContext().openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
              while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            line =jsonString.toString();
            // Parse the JSON using JSONTokener
       //     JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

        }
        catch (Exception e) {
            //Toast.makeText(getActivity(), "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                }
                catch (Exception e){
                    //Toast.makeText(getActivity(), "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return line;
    }
   /*  t[0]=(TextView)v.findViewById(R.id.text);
        t[1]=(TextView)v.findViewById(R.id.result);
        t[2]=(TextView)v.findViewById(R.id.t3);
        if((jsonString=load(ViewResultFragment.FILENAME))!=null){
            t[0].setText(load(ViewResultFragment.FILENAME));
            try{
                array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            }catch (Exception e){
                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        }for(int i=0;i<array.length();i++)
        {
Toast.makeText(getActivity(),"no of results "+Integer.toString(array.length()),Toast.LENGTH_SHORT).show();
        try {
            JSONObject job=array.getJSONObject(i);
  mFilename=job.getString(ViewResultFragment.NAME)+job.getString(ViewResultFragment.TERMINAL)+job.getString(ViewResultFragment.FACULTY)+job.getString(ViewResultFragment.CLASS)+Integer.toString(job.getInt(ViewResultFragment.ROLL));
            JSONObject resultSet = (JSONObject) new JSONTokener(load(mFilename)).nextValue();
            Toast.makeText(getActivity(),"Successful",Toast.LENGTH_SHORT).show();
           //Log.d("here", "reached class 11 sc");
            int PHYSICS=resultSet.getInt("Physics");
            int CHEMISTRY=resultSet.getInt("Chemistry");
            int MATHS=resultSet.getInt("Math");
            int ENGLISH=resultSet.getInt("English");
            int BIOCOMP=resultSet.getInt("Bio_Comp");
            int NEPALI=0;
            String NAME=resultSet.getString("Name");
            double AVERAGE=resultSet.getDouble("Percentage");
            //Log.d("name",NAME);
            int TOTAL=resultSet.getInt("Total");
            String POSITION=resultSet.getString("Position");
              t[i+1].setText(s(PHYSICS)+"  "+s(CHEMISTRY)+"  "+s(MATHS)+"  "+s(ENGLISH)+"  "+s(BIOCOMP)+"  "+s(NEPALI)+"  "+NAME+"  "+s(AVERAGE)+"  "+s(TOTAL)+"  ");
        }
        catch(Exception e){
            Toast.makeText(getActivity(),"Error parsing to JSON",Toast.LENGTH_SHORT).show();
        }}*/
}
