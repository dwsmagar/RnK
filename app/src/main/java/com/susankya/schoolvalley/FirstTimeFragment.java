package com.susankya.schoolvalley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class FirstTimeFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private int layout_id;
    public static  final  String HASSETROUTINE="routine set garyo ta?";
    public static  final  String SECTION="section kaun sa hai be tera?";
    public static FirstTimeFragment newInstance(int  param1) {
        FirstTimeFragment fragment = new FirstTimeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public FirstTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
          }
        switch(mParam1){
            case 1:
               // layout_id= R.layout.first_time_second_fragment;
                layout_id= R.layout.layout_help_2;
            break;


        }
    }




    public String[] giveMeStringArrayFromJSONObject(JSONObject object,String[] placeholder) throws JSONException
    {
        String [] values=new String[placeholder.length];
        for(int i=0;i<placeholder.length;i++)
        {
            values[i]=object.get(placeholder[i]).toString();
        }
        return values;
    }
    SQLiteHelper sqLiteHelper;
    Spinner collegeSelectionSpinner;
   CustomSpinnerAdapter collegeAdapter;
    EditText firstNameET,lastNameET,emailET;
    String[] collegeList;
    Button signUpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   final View v= inflater.inflate(layout_id, container, false);


        if(layout_id== R.layout.layout_help_2)
        {
            TextView tv=(TextView)v.findViewById(R.id.login_tv);
            tv.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            getFragmentManager().beginTransaction().replace(R.id.first_container,new FirstLoginFragment()).addToBackStack(null).commit();
                        }
                    }
            );
            Button next=(Button)v.findViewById(R.id.next_button);
            next.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle b=new Bundle();
                            b.putString("from", "StudentInActivity");

                            Fragment f=new FirstSignUpFragment();
                            f.setArguments(b);
                           getFragmentManager().beginTransaction().replace(R.id.first_container,f).addToBackStack(null).commit();

                        }
                    }
            );
        }
            if(layout_id== R.layout.first_time_second_fragment)
            {

                collegeSelectionSpinner=(Spinner)v.findViewById(R.id.college_spinner);
                firstNameET=(EditText)v.findViewById(R.id.first_name);
                lastNameET=(EditText)v.findViewById(R.id.first_name);
                emailET=(EditText)v.findViewById(R.id.first_name);
                collegeAdapter=new CustomSpinnerAdapter(getActivity(), R.id.college_spinner,
                        ((StudentInActivity)getActivity()).collegeNames);
                collegeSelectionSpinner.setAdapter(collegeAdapter);
                signUpButton=(Button)v.findViewById(R.id.signup_button);
                signUpButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                               String dbName= ((StudentInActivity)getActivity()).databaseNameArray.get(collegeSelectionSpinner.getSelectedItemPosition());
                                SharedPreferences sp=getActivity().getSharedPreferences("dbName",Context.MODE_PRIVATE);
                                sp.edit().putString("dbName", dbName).commit();
                                sp.edit().putBoolean("collegeSelected", true).commit();
                              //  Toast.makeText(getActivity(),getActivity().getSharedPreferences("dbName",Context.MODE_PRIVATE).getString("dbName","null"),Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), NavDrawerActivity.class));
                                getActivity().finish();
                            }
                        }
                );


            }

            return v;
    }



}
