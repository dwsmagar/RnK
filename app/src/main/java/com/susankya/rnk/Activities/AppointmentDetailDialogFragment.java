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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentDetailDialogFragment extends DialogFragment {
@BindView(R.id.name) TextView fullName;
    @BindView(R.id.email_add) TextView email;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.telephone) TextView telephone_num;
    @BindView(R.id.mobile) TextView mobile_num;
    @BindView(R.id.appointment) TextView appointment;
    @BindView(R.id.purpose) TextView visit_purpose;
    @BindView(R.id.evidenceId) TextView evidence_id;
    @BindView(R.id.evidenceIdNo) TextView evidence_num;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.time) TextView time;
    @BindView(R.id.created_date) TextView created_date;
    @BindView(R.id.message) TextView message;
    @BindView(R.id.dismiss)Button dismissBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_detail_dialog, container, false);
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
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.setTitle(R.string.appointment_details);
        }
    }

    private void populateData() {
        if (getArguments() != null) {
            Bundle details = getArguments();
            fullName.setText(details.getString("name"));
            email.setText(details.getString("email"));
            address.setText(details.getString("address"));
            telephone_num.setText(details.getString("telephone"));
            mobile_num.setText(details.getString("mobile"));
            appointment.setText(details.getString("appointment"));
            visit_purpose.setText(details.getString("purpose"));
            evidence_id.setText(details.getString("evidenceId"));
            evidence_num.setText(details.getString("evidenceIdNo"));
            date.setText(details.getString("date"));
            time.setText(timeFormatter(details.getString("time")));
            created_date.setText(details.getString("createdDate"));
            message.setText(details.getString("message"));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    private String timeFormatter(String inputTime) {
        String outputTime = null;
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        try {
            Date _24HourDt = _24HourSDF.parse(inputTime);
            outputTime = _12HourSDF.format(_24HourDt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTime;
    }
}
