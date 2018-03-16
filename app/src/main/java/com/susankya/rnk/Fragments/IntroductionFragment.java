package com.susankya.rnk.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.rnk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroductionFragment extends Fragment {
    @BindView(R.id.introtitle)TextView title;
    @BindView(R.id.locationTitle)TextView title1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.introduction_frag_layout, container, false);
        getActivity().setTitle(R.string.app_name);
        ButterKnife.bind(this, view);
        String t = "<font color=\"#333333\">LOCATION &amp;</font> <font color=\"#1CD36D\">MORE INFO</font>";
        title1.setText(Html.fromHtml(t));
        return view;
    }
}


