package com.susankya.rnk;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.susankya.rnk.Fragments.Nimainterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsFragment extends Fragment {
    @BindView(R.id.sendbtn)
    IconTextView send;
    @BindView(R.id.sms_editTxt)
    EditText sms;
    @BindView(R.id.contact_list)
    RecyclerView contactRV;
    private String fromPhone;
    private String toPhone;
    String[] contacts = new String[]{"Diwas", "Ichchha", "Aditya", "Abhinav", "Sanjog", "Aarati"};
    private String token = "UuDqRQDLkAHXNRrC0S6v";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sms_layout, null);
        ButterKnife.bind(this, view);
        initData();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        contactRV.setLayoutManager(manager);
        contactRV.setAdapter(new ContactRecyclerview(getActivity(), contacts));
        return view;
    }

    private void initData() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsTxt = sms.getText().toString().trim();
                if (smsTxt.isEmpty()) {
                    Toast.makeText(getActivity(), "Field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    postSms(token, "InfoSms", "", smsTxt);
                }
            }
        });
    }

    private void postSms(String token, String fromPhone, String tophone, String text) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Nimainterface nimainterface = ImmortalApplication.smsRetrofit().create(Nimainterface.class);
        nimainterface.sendSms(token, fromPhone, tophone, text).enqueue(new Callback<Sms>() {
            @Override
            public void onResponse(Call<Sms> call, Response<Sms> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
                    Log.d("fatal", response.raw().toString());
                } else {
                    Toast.makeText(getActivity(), "Failed to send sms.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "" + response.body().getResponse_code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Sms> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("fatal", "failure" + t);
            }
        });
    }
}
