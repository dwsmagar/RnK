package com.susankya.rnk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class FragmentTest extends android.support.v4.app.Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private ArrayList<String> list;
    private ListView lv;
    private String mParam1;
    private String mParam2;

    public static FragmentTest newInstance(String param1, String param2) {
        FragmentTest fragment = new FragmentTest();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentTest() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        list=new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add("hello");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_test, container, false);
        lv = (ListView) v.findViewById(R.id.list_result);
adapt adapter=new adapt(list);
        lv.setAdapter(adapter);
        return v;
    }

    private class adapt extends ArrayAdapter<String> {
        private ArrayList<String> profile;

        public adapt(ArrayList<String> c) {
            super(getActivity(), 0, c);
            profile = c;
        }

        @Override
        public View getView(final int pos, View v, ViewGroup vg) {
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.result_row, null);
            }

            return v;
        }

    }
}

