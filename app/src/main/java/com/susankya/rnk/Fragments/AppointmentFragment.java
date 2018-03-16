package com.susankya.rnk.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.susankya.rnk.ImmortalApplication;
import com.susankya.rnk.R;
import com.susankya.rnk.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentFragment extends android.support.v4.app.Fragment {
    @BindView(R.id.datePicker)
    IconTextView datePicker;
    @BindView(R.id.timePicker)
    IconTextView timePicker;
    @BindView(R.id.spPurposeOfVisit)
    Spinner spPurposeOfVisit;
    @BindView(R.id.spAppointment)
    Spinner spAppointment;
    @BindView(R.id.spEvidence)
    Spinner spEvidence;
    @BindView(R.id.firstName)
    EditText firstName;
    @BindView(R.id.lastName)
    EditText lastName;
    @BindView(R.id.telephoneNo)
    EditText telephoneNo;
    @BindView(R.id.mobileNo)
    EditText mobileNo;
    @BindView(R.id.txtAddress)
    EditText addressEdit;
    @BindView(R.id.emailAddress)
    EditText emailEdit;
    @BindView(R.id.evidence_of_id_no)
    EditText evendenceNo;
    @BindView(R.id.message)
    EditText messageEdit;
    @BindView(R.id.btnConfirm)
    Button confirm;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.firstNameLayout)
    TextInputLayout firstNameLayout;
    @BindView(R.id.lastNameLayout)
    TextInputLayout lastNameLayout;
    @BindView(R.id.telephoneNoLayout)
    TextInputLayout telephoneNoLayout;
    @BindView(R.id.addressLayout)
    TextInputLayout addressLayout;
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.mobileNoLayout)
    TextInputLayout mobileNoLayout;
    @BindView(R.id.evidenceIdLaoyut)
    TextInputLayout evidenceIdLaoyut;
    @BindView(R.id.ap_scrollView)
    ScrollView scrollView;
    private String dateValue = "";
    private String timeValue = "";
    private int LENGTH = 7;
    private int MAX_LENGTH = 10;
    ArrayAdapter<String> spinnerArrayAdapter;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Appointment");
        initData();
        return view;
    }

    private void getPurposeOfVisitSpinner() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] purpose_of_visit = new String[]{
                "Select", "Business Partner", "Student", "Guardian", "Marketing", "Others"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, purpose_of_visit);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spPurposeOfVisit.setAdapter(spinnerArrayAdapter);
    }

    private void getEvidenceSpinner() {
        String[] evidence = new String[]{
                "Select", "Passport", "Citizenship", "Driving License", "Voting Card", "PAN Card", "Others"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, evidence);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spEvidence.setAdapter(spinnerArrayAdapter);
    }

    private void getAppointmentSpinner() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] appointment = new String[]{
                "Select", "Counselling", "Accounting", "Marketing", "Parents|Guardian", "Students", "Others"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, appointment);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spAppointment.setAdapter(spinnerArrayAdapter);
    }

    private void initData() {
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });
        getPurposeOfVisitSpinner();
        getEvidenceSpinner();
        getAppointmentSpinner();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isConnectionAvailable(getActivity())) {
                    onSubmit();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("No internet connection, please try again later.")
                            .setPositiveButton("Ok", null)
                            .show();
                }
            }
        });
    }

    private void chooseDate() {
        Calendar now = Calendar.getInstance();
        new android.app.DatePickerDialog(
                getActivity(),
                new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String yearStr = String.valueOf(year);
                        String monthStr = String.valueOf(month + 1);
                        String dayStr = String.valueOf(dayOfMonth);
                        String dateStr = yearStr + "-" + monthStr + "-" + dayStr;
                        date.setText(dateStr);
                        dateValue = dateStr;
                        date.setTextColor(getResources().getColor(R.color.secondary_text));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void chooseTime() {
        Calendar now = Calendar.getInstance();
        new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourStr = String.valueOf(hourOfDay);
                        String minStr = String.valueOf(minute);
                        String timeStr = hourStr + ":" + minStr;
                        timeValue = timeFormatter(timeStr);
                        time.setText(timeValue);
                        time.setTextColor(getResources().getColor(R.color.secondary_text));
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        ).show();
    }

    private void onSubmit() {
        boolean error = false;
        String fullname = firstName.getText().toString().trim() + " " + lastName.getText().toString().trim();
        String telephone = telephoneNo.getText().toString().trim();
        String mobile = mobileNo.getText().toString().trim();
        String address = addressEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String evidenceOfId = evendenceNo.getText().toString().trim();
        String message = messageEdit.getText().toString().trim();
        String purposeVisit = spPurposeOfVisit.getSelectedItem().toString();
        String evidenceId = spEvidence.getSelectedItem().toString();
        String appointmentStr = spAppointment.getSelectedItem().toString();

        if (firstName.getText().toString().trim().isEmpty()) {
            firstNameLayout.setError("This field is required *");
            error = true;
        } else {
            firstNameLayout.setError(null);
        }
        if (lastName.getText().toString().trim().isEmpty()) {
            error = true;
            lastNameLayout.setError("This field is required *");
        } else {
            lastNameLayout.setError(null);
        }

        // telephone validation
        if (telephone.isEmpty()) {
            error = true;
            telephoneNoLayout.setError("This field is required *");
        } else if (telephone.length() < LENGTH || telephone.length() > MAX_LENGTH) {
            error = true;
            telephoneNoLayout.setError("Number should not be less than " + LENGTH + " and greater than " + MAX_LENGTH + ".");
        } else {
            telephoneNoLayout.setError(null);
        }

        // mobile validation
        if (mobile.isEmpty()) {
            error = true;
            mobileNoLayout.setError("This field is required *");
        } else if (mobile.length() < MAX_LENGTH) {
            error = true;
            mobileNoLayout.setError("Number should not be less than " + MAX_LENGTH + ".");
        } else {
            mobileNoLayout.setError(null);
        }

        if (address.isEmpty()) {
            error = true;
            addressLayout.setError("This field is required *");
        } else {
            addressLayout.setError(null);
        }

        // email validation
        if (email.isEmpty()) {
            error = true;
            emailLayout.setError("This field is required *");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = true;
            emailLayout.setError("Invalid email address.");
        } else {
            emailLayout.setError(null);
        }

        if (evidenceOfId.isEmpty()) {
            error = true;
            evidenceIdLaoyut.setError("This field is required *");
        } else {
            evidenceIdLaoyut.setError(null);
        }
        if (spEvidence.getSelectedItemPosition() == 0) {
            error = true;
            showSpError(spEvidence);
        }

        if (spAppointment.getSelectedItemPosition() == 0) appointmentStr = "Others";
        if (spPurposeOfVisit.getSelectedItemPosition() == 0) purposeVisit = "Others";
        if (message.isEmpty()) {
            message = "No message.";
        }

        if (dateValue.trim().isEmpty()) {
            error = true;
            date.setTextColor(getResources().getColor(R.color.red));
            date.setText("Date is empty*");
        } else date.setTextColor(getResources().getColor(R.color.secondary_text));

        if (timeValue.trim().isEmpty()) {
            error = true;
            time.setTextColor(getResources().getColor(R.color.red));
            time.setText("Time is empty*");
        } else time.setTextColor(getResources().getColor(R.color.secondary_text));

        if (!error) {
            Appointment appointmentData = new Appointment();
            appointmentData.full_name = fullname;
            appointmentData.telephone_no = telephone;
            appointmentData.mobile_no = mobile;
            appointmentData.address = address;
            appointmentData.email = email;
            appointmentData.evidence_of_id_number = evidenceOfId;
            appointmentData.message = message;
            appointmentData.purpose_of_visit = purposeVisit;
            appointmentData.evidence_of_id = evidenceId;
            appointmentData.appointment = appointmentStr;
            appointmentData.date = dateValue;
            appointmentData.time = timeValue;
            sendData(appointmentData);
        }
    }

    private void showSpError(Spinner spinner) {
        TextView errorText = (TextView) spinner.getSelectedView();
        errorText.setTextSize(12);
        errorText.setTextColor(Color.RED);//just to highlight that this is an error
        errorText.setText("Please select an item");//changes the selected item text to this
    }

    private void sendData(final Appointment data) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Nimainterface nimainterface = ImmortalApplication.getRetrofit().create(Nimainterface.class);
        nimainterface.postAppointment(data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Appointment made successfully.", Toast.LENGTH_SHORT).show();
                    clearViews();
                } else {
                    Toast.makeText(getActivity(), "Unable to submit.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("fatal", "" + t);
            }
        });
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

    private void clearViews() {
        firstName.setText("");
        firstName.setFocusable(true);
        lastName.setText("");
        emailEdit.setText("");
        telephoneNo.setText("");
        mobileNo.setText("");
        addressEdit.setText("");
        spPurposeOfVisit.setSelection(0);
        spEvidence.setSelection(0);
        evendenceNo.setText("");
        date.setText("Date*");
        time.setText("Time*");
        messageEdit.setText("");
        spAppointment.setSelection(0);
        scrollView.pageScroll(View.FOCUS_UP);
    }
}