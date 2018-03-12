package com.susankya.schoolvalley;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SingleResultFragment extends Fragment {

    @BindView(R.id.shop_about_lv)RecyclerView rv;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private String SN;
    public ArrayList<AboutInfo> resultMarks=new ArrayList<>();
    public String[] columns;
    public String[] marks;
    private String colName,roll,res;
    int size;

    AboutAdapter adapter;
    public SingleResultFragment() {
        // Required empty public constructor
    }


    public static SingleResultFragment newInstance(String param1, String param2,String param3) {
        SingleResultFragment fragment = new SingleResultFragment();
        fragment.colName=param1;
        fragment.roll=param2;
        fragment.res=param3;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void manageJSON()
    {

        try {

            JSONObject job= new JSONObject(res);
            size=job.getInt("no_of_columns");
            columns=new String[size];
            marks=new String[size];
            //Log.d("sizeboy",size+"");
            String nameOfColumn=job.getString("columns");
            //Log.d("sizeboy",size+"");
            String resultString=job.getString("datas");
            //Log.d("sizeboy",size+"");
            JSONArray cols=new JSONArray(nameOfColumn);
            //Log.d("sizeboy",size+"");
           JSONArray j=new JSONArray(resultString);

            JSONObject datas=new JSONArray(resultString).getJSONObject(0);

            for (int i=0;i<size;i++)
            {

                columns[i]=cols.getString(i);
                //Log.d("sizeboy11",size+"");
                //Log.d("column",columns[i]);
                marks[i]=datas.getString(columns[i]);
               // //Log.d("marks",marks[i]);
                AboutInfo a=new AboutInfo(columns[i],marks[i]);
                resultMarks.add(a);

            }
        }
        catch (Exception e)
        {
            //Log.d("mistakehere",e.toString());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_about_shop, container, false);
        ButterKnife.bind(this,v);
        manageJSON();
        adapter=new AboutAdapter(getActivity().getApplicationContext(), resultMarks);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        return  v;

    }

    public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder>
    {

        ArrayList<AboutInfo> list;
        Context c;



        public AboutAdapter(Context c, ArrayList<AboutInfo> list)
        {
            super();
            this.c=c;
            this.list=list;

        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.fieldtv)TextViewPlus field;
            @BindView(R.id.datatv)TextViewPlus data;

            public ViewHolder(View v)
            {
                super(v);
                ButterKnife.bind(this,v);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

         final    AboutInfo aI=list.get(position);
            holder.field.setText(capitalize(aI.getField().replace("_"," ").trim()));

            if(aI.getData().isEmpty() || aI.getData().equals("null"))
            holder.data.setText("-");
            else
            holder.data.setText(aI.getData());
            holder.data.setCustomFont(getContext(),FragmentCodes.REGULAR);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.result_row_item,null);
            return new ViewHolder(view);
        }
    }
    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}



