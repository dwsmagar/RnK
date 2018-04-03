package com.susankya.rnk.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
    @BindView(R.id.introtitle)
    TextView title;
    @BindView(R.id.locationTitle)
    TextView title1;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.introduction_frag_layout, container, false);
        getActivity().setTitle(R.string.app_name);
        ButterKnife.bind(this, view);
        String t = "<font color=\"#333333\">LOCATION &amp;</font> <font color=\"#F79F1F\">MORE INFO</font>";
        title1.setText(Html.fromHtml(t));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
                }
//                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?daddr=Kathmandu Infosys, Dilli Bazaar Rd 1259, Kathmandu 44600"));
            }
        });
        return view;
    }
}


