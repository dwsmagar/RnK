package com.susankya.rnk.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.susankya.rnk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EligibilityDetailDialogFragment extends DialogFragment {
    @BindView(R.id.name)
    TextView fullName;
    @BindView(R.id.email_add)
    TextView email;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.qualification)
    TextView qualification;
    @BindView(R.id.country)
    TextView country;
    @BindView(R.id.date)
    TextView created_date;
    @BindView(R.id.remarks)
    TextView message;
    @BindView(R.id.ielts)
    TextView ielts;
    @BindView(R.id.toefl)
    TextView toefl;
    @BindView(R.id.gmat)
    TextView gmat;
    @BindView(R.id.gre)
    TextView gre;
    @BindView(R.id.sat)
    TextView sat;
    @BindView(R.id.pte)
    TextView pte;
    @BindView(R.id.toefl_layout)
    View toeflView;
    @BindView(R.id.gmat_layout)
    View gmatView;
    @BindView(R.id.sat_layout)
    View satView;
    @BindView(R.id.gre_layout)
    View greView;
    @BindView(R.id.pte_layout)
    View pteView;
    @BindView(R.id.ielts_layout)
    View ieltsView;
    @BindView(R.id.dismiss)
    Button dismissBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eligibility_detail_dialog, container, false);
        ButterKnife.bind(this, view);
        populateData();
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.setTitle(R.string.eligibility_details);
        }
    }

    private void populateData() {
        if (getArguments() != null) {
            Bundle details = getArguments();
            fullName.setText(details.getString("name"));
            email.setText(details.getString("email"));
            address.setText(details.getString("address"));
            phone.setText(details.getString("mobile"));
            qualification.setText(details.getString("qualification"));
            country.setText(details.getString("country"));
            created_date.setText(details.getString("createdDate"));
            message.setText(details.getString("message"));

            String ielts_marks = details.getString("ielts");
            String toefl_marks = details.getString("toefl");
            String gmat_marks = details.getString("gmat");
            String gre_marks = details.getString("gre");
            String sat_marks = details.getString("sat");
            String pte_marks = details.getString("pte");

            if (ielts_marks == null || ielts_marks.isEmpty() || checkTest(ielts_marks)) ieltsView.setVisibility(View.GONE);
            else ielts.setText(ielts_marks);
            if (toefl_marks == null || toefl_marks.isEmpty() || checkTest(toefl_marks)) toeflView.setVisibility(View.GONE);
            else toefl.setText(toefl_marks);
            if (gmat_marks == null || gmat_marks.isEmpty()|| checkTest(gmat_marks)) gmatView.setVisibility(View.GONE);
            else gmat.setText(gmat_marks);
            if (gre_marks == null || gre_marks.isEmpty()|| checkTest(gre_marks)) greView.setVisibility(View.GONE);
            else gre.setText(gre_marks);
            if (sat_marks == null || sat_marks.isEmpty()|| checkTest(sat_marks)) satView.setVisibility(View.GONE);
            else sat.setText(sat_marks);
            if (pte_marks == null || pte_marks.isEmpty()|| checkTest(pte_marks)) pteView.setVisibility(View.GONE);
            else pte.setText(pte_marks);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    private boolean checkTest(String value) {
        if (value.equals("ielts"))
            return true;
        else if (value.equals("toefl"))
            return true;
        else if (value.equals("gmat"))
            return true;
        else if (value.equals("gre"))
            return true;
        else if (value.equals("sat"))
            return true;
        else if (value.equals("pte"))
            return true;
        else return false;
    }
}
