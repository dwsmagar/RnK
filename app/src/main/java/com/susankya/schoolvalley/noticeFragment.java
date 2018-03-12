package com.susankya.schoolvalley;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class noticeFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private TextView mDate, mDescription;
    private TextView t1, t2, t3;
    private ArrayList<notice> mNotices = new ArrayList<notice>();

    public static noticeFragment newInstance(String title) {
        noticeFragment fragment = new noticeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, title);
        fragment.setArguments(args);
        return fragment;
    }

    public noticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notice, container, false);
        mNotices = NavDrawerActivity.getArrayList();
        mDate = (TextView) v.findViewById(R.id.btn_date);
        mDescription = (TextView) v.findViewById(R.id.description_content);
        mDescription.setMovementMethod(new ScrollingMovementMethod());
        t1 = (TextView) v.findViewById(R.id.category_text);
        t2 = (TextView) v.findViewById(R.id.Title_content);
        final noticeBoard NB = noticeBoard.get(getActivity(), getActivity());
        notice n = getNotice(mParam1);
        mDate.setText(n.getDate() + "  " + n.getTime());
        mDescription.setText(n.getDescription() + "\n");
        t1.setText(n.getCategory());
        t2.setText(n.getTitle());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
       /* if(NavDrawerActivity.getZugad()==0)
        ((NavDrawerActivity) getActivity()).onSectionAttached(1);*/
    }

    public notice getNotice(String pos) {
        for (notice c : mNotices) {
            if (c.getTitle().equals(pos))
                return c;
        }
        return null;
    }

}
