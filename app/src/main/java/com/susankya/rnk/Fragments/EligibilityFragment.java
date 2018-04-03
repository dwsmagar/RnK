package com.susankya.rnk.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.susankya.rnk.ImmortalApplication;
import com.susankya.rnk.Interfaces.Nimainterface;
import com.susankya.rnk.R;
import com.susankya.rnk.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EligibilityFragment extends Fragment {
    @BindView(R.id.eg_scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.spQualification)
    Spinner spQualification;
    @BindView(R.id.spCountry)
    Spinner spCountry;
    @BindView(R.id.add_view)
    IconTextView add;
    @BindView(R.id.remove_view)
    IconTextView remove;
    @BindView(R.id.btnConfirm)
    Button confirm;
    @BindView(R.id.firstName)
    EditText firstName;
    @BindView(R.id.lastName)
    EditText lastName;
    @BindView(R.id.emailAddress)
    EditText email;
    @BindView(R.id.phoneNo)
    EditText telephone;
    @BindView(R.id.txtAddress)
    EditText txtAddress;
    @BindView(R.id.remarks)
    EditText remarks;

    @BindView(R.id.spTest)
    Spinner spTest;
    @BindView(R.id.spTest2)
    Spinner spTest2;
    @BindView(R.id.spTest3)
    Spinner spTest3;
    @BindView(R.id.spTest4)
    Spinner spTest4;
    @BindView(R.id.spTest5)
    Spinner spTest5;
    @BindView(R.id.spTest6)
    Spinner spTest6;
    @BindView(R.id.spTest7)
    Spinner spTest7;

    @BindView(R.id.test_score)
    EditText score;
    @BindView(R.id.test_score2)
    EditText score2;
    @BindView(R.id.test_score3)
    EditText score3;
    @BindView(R.id.test_score4)
    EditText score4;
    @BindView(R.id.test_score5)
    EditText score5;
    @BindView(R.id.test_score6)
    EditText score6;
    @BindView(R.id.test_score7)
    EditText score7;

    @BindView(R.id.leftLayout2)
    View view2;
    @BindView(R.id.leftLayout3)
    View view3;
    @BindView(R.id.leftLayout4)
    View view4;
    @BindView(R.id.leftLayout5)
    View view5;
    @BindView(R.id.leftLayout6)
    View view6;
    @BindView(R.id.leftLayout7)
    View view7;

    @BindView(R.id.firstNameLayout)
    TextInputLayout firstLayout;
    @BindView(R.id.lastNameLayout)
    TextInputLayout lastLayout;
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.phoneLayout)
    TextInputLayout phoneLayout;
    @BindView(R.id.addressLayout)
    TextInputLayout addressLayout;
    @BindView(R.id.scoreLayout)
    TextInputLayout scoreLayout;
    @BindView(R.id.scoreLayout2)
    TextInputLayout scoreLayout2;
    @BindView(R.id.scoreLayout3)
    TextInputLayout scoreLayout3;
    @BindView(R.id.scoreLayout4)
    TextInputLayout scoreLayout4;
    @BindView(R.id.scoreLayout7)
    TextInputLayout scoreLayout7;
    @BindView(R.id.scoreLayout5)
    TextInputLayout scoreLayout5;
    @BindView(R.id.scoreLayout6)
    TextInputLayout scoreLayout6;

    private int ADD_COUNT = 1;
    private int MAX_COUNT = 7;
    private boolean error = false;
    private Eligibility e;

    public EligibilityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eligibility, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Check Eligibility");
        initData();
        return view;
    }

    private void setSpQualification() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] qualification = new String[]{
                "Select", "SEE | SLC | O Level", "+2 or A - Level", "Bachelor Degree", "Master Degree"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, qualification);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spQualification.setAdapter(spinnerArrayAdapter);
    }

    private void setSpTest() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] test = new String[]{
                "Select", "IELTS", "TOEFL", "SAT", "GRE", "GMAT", "PTE"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, test);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spTest.setAdapter(spinnerArrayAdapter);
        spTest2.setAdapter(spinnerArrayAdapter);
        spTest3.setAdapter(spinnerArrayAdapter);
        spTest4.setAdapter(spinnerArrayAdapter);
        spTest5.setAdapter(spinnerArrayAdapter);
        spTest6.setAdapter(spinnerArrayAdapter);
        spTest7.setAdapter(spinnerArrayAdapter);
    }

    private void setSpCountry() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] country = new String[]{
                "Select", "Germany", "USA", "Spain", "UK", "Australia", "New-Zealand", "Canada", "India", "Denmark", "Cyprus", "Poland", "Hungary", "Malta", "Others"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, country);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spCountry.setAdapter(spinnerArrayAdapter);
    }

    private void initData() {
        setSpCountry();
        setSpQualification();
        setSpTest();
        e = new Eligibility();
        e.ielts = "ielts";
        e.toefl = "toefl";
        e.sat = "sat";
        e.gmat = "gmat";
        e.gre = "gre";
        e.pte = "pte";

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isConnectionAvailable(getActivity())) {
                onConfirm();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("No internet connection, please try again later.")
                            .setPositiveButton("Ok", null)
                            .show();
                }
            }
        });
        if (ADD_COUNT > 0 && ADD_COUNT <= MAX_COUNT) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addView(ADD_COUNT);
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeView(ADD_COUNT);
                }
            });
        }
    }

    private void onConfirm() {
        boolean fnErr, lnErr, emailErr, phErr, addErr, testErr, scoreErr, quaErr;
        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();
        String email_address = email.getText().toString().trim();
        String phone = telephone.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String qualification = spQualification.getSelectedItem().toString().trim();

        String test_score = score.getText().toString().trim(); // this is necessary
        String test_score2 = score2.getText().toString().trim();
        String test_score3 = score3.getText().toString().trim();
        String test_score4 = score4.getText().toString().trim();
        String test_score5 = score5.getText().toString().trim();
        String test_score6 = score6.getText().toString().trim();
        String test_score7 = score7.getText().toString().trim();

        String test = spTest.getSelectedItem().toString().trim(); // this is necessary
        String test2 = spTest2.getSelectedItem().toString().trim();
        String test3 = spTest3.getSelectedItem().toString().trim();
        String test4 = spTest4.getSelectedItem().toString().trim();
        String test5 = spTest5.getSelectedItem().toString().trim();
        String test6 = spTest6.getSelectedItem().toString().trim();
        String test7 = spTest7.getSelectedItem().toString().trim();

        String country = spCountry.getSelectedItem().toString().trim();
        String remarks_msg = remarks.getText().toString().trim();

        if (first_name.isEmpty()) {
            fnErr = true;
            firstLayout.setError("This field is required *");
        } else {
            fnErr = false;
            firstLayout.setError(null);
        }

        if (last_name.isEmpty()) {
            lnErr = true;
            lastLayout.setError("This field is required *");
        } else {
            lnErr = false;
            lastLayout.setError(null);
        }

        if (email_address.isEmpty()) {
            emailErr = true;
            emailLayout.setError("This field is required *");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            emailErr = true;
            emailLayout.setError("Invalid email address");
        } else {
            emailErr = false;
            emailLayout.setError(null);
        }

        if (spQualification.getSelectedItemPosition() == 0) {
            quaErr = true;
            showSpError(spQualification, "Please select an option");
        } else {
            quaErr = false;
        }

        if (spCountry.getSelectedItemPosition() == 0) {
            country = "Others";
        }

        if (spTest.getSelectedItemPosition() == 0) {
            testErr = true;
            showSpError(spTest, "Select");
        } else testErr = false;

        if (test_score.isEmpty()) {
            scoreErr = true;
            scoreLayout.setError("This field is required");
        } else if (!TextUtils.isDigitsOnly(test_score)) {
            scoreErr = true;
            scoreLayout.setError("Invalid number");
        } else {
            scoreErr = false;
            scoreLayout.setError(null);
            returnScore(test_score, test);
        }

        if (ADD_COUNT > 1) {
            if (spTest2.getSelectedItemPosition() == 0) {
                error = true;
                showSpError(spTest2, "Select");
            }
            if (test_score2.isEmpty()) {
                error = true;
                scoreLayout2.setError("This field is required");
            } else if (!TextUtils.isDigitsOnly(test_score2)) {
                error = true;
                scoreLayout2.setError("Invalid number");
            } else {
                error = false;
                scoreLayout2.setError(null);
                returnScore(test_score2, test2);
            }
        }

        if (ADD_COUNT > 2) {
            if (spTest3.getSelectedItemPosition() == 0) {
                error = true;
                showSpError(spTest3, "Select");
            } else {
                if (test_score3.isEmpty()) {
                    error = true;
                    scoreLayout3.setError("This field is required");
                } else if (!TextUtils.isDigitsOnly(test_score3)) {
                    error = true;
                    scoreLayout3.setError("Invalid number");
                } else {
                    error = false;
                    scoreLayout3.setError(null);
                    returnScore(test_score3, test3);
                }
            }
        }

        if (ADD_COUNT > 3) {
            if (spTest4.getSelectedItemPosition() == 0) {
                error = true;
                showSpError(spTest4, "Select");
            } else {
                if (test_score4.isEmpty()) {
                    error = true;
                    scoreLayout4.setError("This field is required");
                } else if (!TextUtils.isDigitsOnly(test_score4)) {
                    error = true;
                    scoreLayout4.setError("Invalid number");
                } else {
                    error = false;
                    scoreLayout4.setError(null);
                    returnScore(test_score4, test4);
                }
            }
        }

        if (ADD_COUNT > 4) {
            if (spTest5.getSelectedItemPosition() == 0) {
                error = true;
                showSpError(spTest5, "Select");
            } else {
                if (test_score5.isEmpty()) {
                    error = true;
                    scoreLayout5.setError("This field is required");
                } else if (!TextUtils.isDigitsOnly(test_score5)) {
                    error = true;
                    scoreLayout5.setError("Invalid number");
                } else {
                    error = false;
                    scoreLayout5.setError(null);
                    returnScore(test_score5, test5);
                }
            }
        }

        if (ADD_COUNT > 5) {
            if (spTest6.getSelectedItemPosition() == 0) {
                error = true;
                showSpError(spTest6, "Select");
            } else {
                if (test_score6.isEmpty()) {
                    error = true;
                    scoreLayout6.setError("This field is required");
                } else if (!TextUtils.isDigitsOnly(test_score6)) {
                    error = true;
                    scoreLayout6.setError("Invalid number");
                } else {
                    error = false;
                    scoreLayout6.setError(null);
                    returnScore(test_score6, test6);
                }
            }
        }

        //// -------------------------- //////
        if (phone.isEmpty()) {
            phone = "Not entered";
        } else if (phone.length() < 10)
            phoneLayout.setError("Enter the valid 10 number");
        else phoneLayout.setError(null);

        if (address.isEmpty()) {
            address = "Not entered";
        } else addressLayout.setError(null);

        if (remarks_msg.isEmpty()) {
            remarks_msg = "No messages";
        }

        if (!fnErr && !lnErr && !emailErr && !quaErr && !testErr && !scoreErr) {
            Eligibility data = new Eligibility();
            data.name = first_name + " " + last_name;
            data.email = email_address;
            data.address = address;
            data.phone = phone;
            data.qualification = qualification;
            data.ielts = e.ielts;
            data.toefl = e.toefl;
            data.gmat = e.gmat;
            data.sat = e.sat;
            data.gre = e.gre;
            data.pte = e.pte;
            data.priority_country = country;
            data.remarks = remarks_msg;
            postData(data);
        }
    }

    private void addView(int counter) {
        remove.setVisibility(View.VISIBLE);
        if (counter == 1) {
            view2.setVisibility(View.VISIBLE);
            ADD_COUNT++;
        }
        if (counter == 2) {
            view3.setVisibility(View.VISIBLE);
            ADD_COUNT++;
        }
        if (counter == 3) {
            view4.setVisibility(View.VISIBLE);
            ADD_COUNT++;
        }
        if (counter == 4) {
            view5.setVisibility(View.VISIBLE);
            ADD_COUNT++;
        }
        if (counter == 5) {
            view6.setVisibility(View.VISIBLE);
            ADD_COUNT++;
        }
    }

    private void showSpError(Spinner spinner, String errorMsg) {
        TextView errorText = (TextView) spinner.getSelectedView();
        errorText.setTextSize(12);
        errorText.setTextColor(Color.RED);//just to highlight that this is an error
        errorText.setText(errorMsg);//changes the selected item text to this
    }

    private void removeView(int counter) {
        remove.setVisibility(View.VISIBLE);
        if (counter == 2) {
            remove.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            ADD_COUNT--;
        }
        if (counter == 3) {
            view3.setVisibility(View.GONE);
            ADD_COUNT--;
        }
        if (counter == 4) {
            view4.setVisibility(View.GONE);
            ADD_COUNT--;
        }
        if (counter == 5) {
            view5.setVisibility(View.GONE);
            ADD_COUNT--;
        }
        if (counter == 6) {
            view6.setVisibility(View.GONE);
            ADD_COUNT--;
        }
    }

    private void postData(Eligibility eligibility) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Nimainterface nimainterface = ImmortalApplication.getRetrofit().create(Nimainterface.class);
        nimainterface.postEligibility(eligibility).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Form added successfully.", Toast.LENGTH_SHORT).show();
                    clearViews();
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Log.d("Throwable", "" + t);
            }
        });
    }

    private void returnScore(String value, String test) {
        if (test.equalsIgnoreCase(e.ielts))
            e.ielts = value;
        else if (test.equalsIgnoreCase(e.toefl))
            e.toefl = value;
        else if (test.equalsIgnoreCase(e.gmat))
            e.gmat = value;
        else if (test.equalsIgnoreCase(e.gre))
            e.gre = value;
        else if (test.equalsIgnoreCase(e.sat))
            e.sat = value;
        else if (test.equalsIgnoreCase(e.pte))
            e.pte = value;
    }

    private void clearViews() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        telephone.setText("");
        txtAddress.setText("");
        spQualification.setSelection(0);
        spCountry.setSelection(0);
        remarks.setText("");
        spTest.setSelection(0);
        score.setText("");
        telephone.setText("");
        scrollView.pageScroll(View.FOCUS_UP);
        if (ADD_COUNT > 1) {
            spTest2.setSelection(0);
            score2.setText("");
        }
        if (ADD_COUNT > 2) {
            spTest3.setSelection(0);
            score3.setText("");
        }
        if (ADD_COUNT > 3) {
            spTest4.setSelection(0);
            score4.setText("");
        }
        if (ADD_COUNT > 4) {
            spTest5.setSelection(0);
            score5.setText("");
        }
        if (ADD_COUNT > 5) {
            spTest6.setSelection(0);
            score6.setText("");
        }
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        view5.setVisibility(View.GONE);
        view6.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
        ADD_COUNT = 1;
    }
}
