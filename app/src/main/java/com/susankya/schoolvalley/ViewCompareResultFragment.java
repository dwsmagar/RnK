package com.susankya.schoolvalley;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ViewCompareResultFragment extends android.support.v4.app.Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String yourRoll,friendsRoll;
    private String[] firstValues,secondValues;
    private TextView tv;
    private TextView[] yourTV,friendsTV;
    private int yourID[],friendsID[];



    public static ViewCompareResultFragment newInstance(String[] yourValues,String[] friendsValues,String roll1,String roll2) {
        ViewCompareResultFragment fragment = new ViewCompareResultFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, yourValues);
       args.putStringArray(ARG_PARAM2, friendsValues);
        args.putString("ROLL1",roll1);
        args.putString("ROLL2",roll2);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewCompareResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firstValues= getArguments().getStringArray(ARG_PARAM1);
             secondValues = getArguments().getStringArray(ARG_PARAM2);
            yourRoll=getArguments().getString("ROLL1");
            friendsRoll=getArguments().getString("ROLL2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_view_compare_result, container, false);
         yourTV=new TextView[11];
        friendsTV=new TextView[11];
        yourID=new int[]{R.id.roll1, R.id.phy1, R.id.che1, R.id.mat1, R.id.eng1, R.id.biocomp1, R.id.nep1, R.id.total1, R.id.avg1, R.id.result1};
        friendsID=new int[]{R.id.roll2, R.id.phy2, R.id.che2, R.id.mat2, R.id.eng2, R.id.biocomp2, R.id.nep2, R.id.total2, R.id.avg2, R.id.result2};
       String name1=firstValues[5];
        String name2=secondValues[5];
        firstValues[5]=firstValues[9];
        secondValues[5]=secondValues[9];

        String temp1=firstValues[6];
        firstValues[6]=firstValues[7];
        firstValues[7]=temp1;

         temp1=secondValues[6];
        secondValues[6]=secondValues[7];
        secondValues[7]=temp1;


        for(int i=0;i<yourID.length;i++)
        {
            yourTV[i]=(TextView)v.findViewById(yourID[i]);
            friendsTV[i]=(TextView)v.findViewById(friendsID[i]);
            if(i>0 && i<9)
            {
                yourTV[i].setText(firstValues[i-1]);
                friendsTV[i].setText(secondValues[i-1]);
            }
        }
        yourTV[0].setText("ROLL "+yourRoll);
        friendsTV[0].setText("ROLL "+friendsRoll);
        yourTV[9].setText(firstValues[8]);
        friendsTV[9].setText(secondValues[8]);
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((NavDrawerActivity) getActivity()).onSectionAttached(3);
    }




}
