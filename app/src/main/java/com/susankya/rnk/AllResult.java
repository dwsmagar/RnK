package com.susankya.rnk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class AllResult extends android.support.v4.app.Fragment implements FragmentCodes{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> list;
    private ListView lv;
    private ArrayList<Result> results;
    JSONArray array;
    private int mParam1=0;
    private ProgressBar pb;
    private FloatingActionButton search;
    private int num;
    private String mParam2=null;
private TextView sub1,sub2,sub3,sub4,sub5,sub6,sub7;
    public static AllResult newInstance(int  param1, String param2) {
        AllResult fragment = new AllResult();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AllResult() {
     // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1,11);
            mParam2 = getArguments().getString(ARG_PARAM2,"ada");
        }
        list=new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add("hello");
        }
        results=new ArrayList<>();
    }

    private String s(int i)
    {
        return Integer.toString(i);
    }
    private String s(double i)
    {
        return Double.toString(i);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_result, container, false);
        search=(FloatingActionButton)v.findViewById(R.id.floating_add_button);
        lv = (ListView)v.findViewById(R.id.list_result);
search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                        //    getActivity().finish();
                            Result r = (Result) parent.getAdapter().getItem(position);

                            String[] vals = new String[]
                                        {
                                                s(r.getPhy()),
                                                s(r.getChe()),
                                                s(r.getMaths()),
                                                s(r.getEng()),
                                                s(r.getBio_comp()),
                                                s(r.getNep()),
                                                s(r.getTotal()),
                                                r.getPercentage(),
                                                r.getPosition()

                                        };


                            Intent i=new Intent();
                           i.putExtra("result",array.getJSONObject(position).toString());
                          //  Toast.makeText(getActivity(),array.getJSONObject(position).toString(),Toast.LENGTH_SHORT).show();
                            i.putExtra("name", r.getName());
                           i.putExtra("roll", r.getRoll());
                            i.putExtra("class", mParam1);
                            i.putExtra("exam", "first");
                          i.putExtra("values", vals);
                          getActivity().setResult(Activity.RESULT_OK,i);
                            getActivity().finish();

                        }catch (Exception e){
                            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
        sub1=(TextView)v.findViewById(R.id.sub1);
        sub2=(TextView)v.findViewById(R.id.sub2);
        sub3=(TextView)v.findViewById(R.id.sub3);
        sub4=(TextView)v.findViewById(R.id.sub4);
        sub5=(TextView)v.findViewById(R.id.sub5);
        switch (mParam1){
            case (11):{
                sub1.setText("Eng");
                sub2.setText("Phy");
                sub3.setText("Bio/Comp");
                sub4.setText("Che");
                sub5.setText("Math");
                    break;
            }
            case (12):{
                sub1.setText("Eng");
                sub2.setText("Phy");
                sub3.setText("Bio/Math");
                sub4.setText("Che");
                sub5.setText("Nep");
                break;
            }
        }
        publishResult(mParam2);
              return v;
    }

    private void publishResult(String result){
        try
        {
            array = (JSONArray) new JSONTokener(result)
                    .nextValue();
            for (int i = 0; i < array.length(); i++){
                Result result1=new Result();
                JSONObject job= (JSONObject) array.getJSONObject(i);
                result1.setChe(job.getDouble("Chemistry"));
                result1.setPhy(job.getDouble("Physics"));
                result1.setMaths(job.getDouble("Math"));
                result1.setBio_comp(job.getDouble("Bio_Comp"));
                result1.setEng(job.getDouble("English"));
                result1.setName(job.getString("Name"));
                result1.setPosition(job.getString("position"));
                if(mParam1==12)
                result1.setNep(job.getDouble("Nepali"));
                result1.setResult(job.getString("RESULT"));
                result1.setPercentage(String.valueOf(job.getDouble("Percentage")));
                result1.setTotal(job.getDouble("Total"));
                result1.setRoll(job.getInt("Roll"));
                result1.setPos(i);
                results.add(result1);
            }
            adapt adapter=new adapt(results);
            lv.setAdapter(adapter);
            ResultActivity.setListView(lv,results);
       }
        catch(Exception e)
        {
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    private class adapt extends ArrayAdapter<Result> {
             public adapt(ArrayList<Result> c) {
            super(getActivity(), 0, c);
                 }

        @Override
        public View getView(final int pos, View v, ViewGroup vg) {
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.result_row, null);
            }
            Result result=getItem(pos);
            TextView sn=(TextView)v.findViewById(R.id.sn);
            TextView roll=(TextView)v.findViewById(R.id.rollno);
            TextView name=(TextView)v.findViewById(R.id.name);
            TextView sub1=(TextView)v.findViewById(R.id.sub1);
            TextView sub2 =(TextView)v.findViewById(R.id.sub2);
            TextView sub3=(TextView)v.findViewById(R.id.sub3);
            TextView sub4=(TextView)v.findViewById(R.id.sub4);
            TextView sub5=(TextView)v.findViewById(R.id.sub5);
            TextView total=(TextView)v.findViewById(R.id.total);
            TextView percentage=(TextView)v.findViewById(R.id.percentage);
            TextView result_pass_fail=(TextView)v.findViewById(R.id.result);
            TextView position =(TextView)v.findViewById(R.id.position);
            sn.setText(""+(pos+1));
            name.setText(result.getName());
            sub1.setText(""+result.getEng());
            sub2.setText(""+result.getPhy());

            if(mParam1==12)
            {
                if(result.getBio_comp()==0 && result.getMaths()!=0)
                    sub3.setText(""+result.getMaths());
                else if(result.getMaths()==0 && result.getBio_comp()!=0)
                    sub3.setText(""+result.getBio_comp());
                else
                    sub3.setText("0");
                sub5.setText(""+result.getNep());
            }
            else
            {
                sub3.setText(""+result.getBio_comp());
                sub5.setText(""+result.getMaths());
            }



            sub4.setText(""+result.getChe());

            total.setText(""+result.getTotal());
          percentage.setText(""+result.getPercentage());
            result_pass_fail.setText(""+result.getResult());
            position.setText(""+result.getPosition());
            roll.setText(""+result.getRoll());
             return v;
        }

    }

}

