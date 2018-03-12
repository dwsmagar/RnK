package com.susankya.schoolvalley.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.schoolvalley.R;

import org.w3c.dom.Text;

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


