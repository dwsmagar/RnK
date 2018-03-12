package com.susankya.schoolvalley.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.susankya.schoolvalley.CompressImage;
import com.susankya.schoolvalley.ImmortalApplication;
import com.susankya.schoolvalley.Models.AppliedUser;
import com.susankya.schoolvalley.R;
import com.susankya.schoolvalley.Utilities;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ApplyNowFragment extends Fragment {
    @BindView(R.id.fnLayout)
    TextInputLayout fnLayout;
    @BindView(R.id.lnLayout)
    TextInputLayout lnLayout;
    @BindView(R.id.pobLayout)
    TextInputLayout pobLayout;
    @BindView(R.id.nationalityLayout)
    TextInputLayout nationalityLayout;
    @BindView(R.id.cnLayout)
    TextInputLayout cnLayout;
    @BindView(R.id.mobileLayout)
    TextInputLayout mblLayout;
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.courseLayout)
    TextInputLayout courseLayout;
    @BindView(R.id.spTitle)
    Spinner spTitle;
    @BindView(R.id.spSex)
    Spinner spSex;
    @BindView(R.id.spYear)
    Spinner spYear;
    @BindView(R.id.spIntake)
    Spinner spIntake;
    @BindView(R.id.spCountry)
    Spinner spCountry;
    @BindView(R.id.spPermanentAddress)
    Spinner spPermanentAddress;
    @BindView(R.id.ceEmploymentType)
    Spinner ceEmploymentType;
    @BindView(R.id.spMaritalStatus)
    Spinner spMaritalStatus;
    @BindView(R.id.firstName)
    EditText firstName;
    @BindView(R.id.lastName)
    EditText lastName;
    @BindView(R.id.middleName)
    EditText middleName;
    @BindView(R.id.dateOfBirth)
    TextView dateOfBirth;
    @BindView(R.id.placeOfBirth)
    EditText placeOfBirth;
    @BindView(R.id.nationality)
    EditText nationality;
    @BindView(R.id.citizenshipNo)
    EditText citizenshipNo;
    @BindView(R.id.course)
    EditText desiredCourse;
    @BindView(R.id.passportNo)
    EditText passportNo;
    @BindView(R.id.dateOfIssue)
    TextView dateOfIssue;
    @BindView(R.id.placeOfIssue)
    EditText placeOfIssue;
    @BindView(R.id.dateOfExpiry)
    TextView dateOfExpiry;
    @BindView(R.id.streetNameNo)
    EditText streetNameNo;
    @BindView(R.id.maCity)
    EditText maCity;
    @BindView(R.id.maState)
    EditText maState;
    @BindView(R.id.maCountry)
    EditText maCountry;
    @BindView(R.id.maPhone)
    EditText maPhone;
    @BindView(R.id.maMobile)
    EditText maMobile;
    @BindView(R.id.maEmail)
    EditText maEmail;
    @BindView(R.id.is_permanentAddress)
    CheckBox ch_permanentAddress;
    @BindView(R.id.schoolName)
    EditText schoolName;
    @BindView(R.id.schoolCity)
    EditText schoolCity;
    @BindView(R.id.schoolState)
    EditText schoolState;
    @BindView(R.id.schoolCountry)
    EditText schoolCountry;
    @BindView(R.id.schoolQualification)
    EditText schoolQualification;
    @BindView(R.id.schoolGpa)
    EditText schoolGpa;
    @BindView(R.id.schoolDateOfCompletion)
    TextView schoolDateOfCompletion;
    @BindView(R.id.hsName)
    EditText highSchoolName;
    @BindView(R.id.hsCity)
    EditText highSchoolCity;
    @BindView(R.id.hsState)
    EditText highSchoolState;
    @BindView(R.id.hsCountry)
    EditText highSchoolCountry;
    @BindView(R.id.hsQualification)
    EditText hsQualification;
    @BindView(R.id.hsGpa)
    EditText hsGpa;
    @BindView(R.id.hsDateOfCompletion)
    TextView hsDateOfCompletion;
    @BindView(R.id.ugUniversity)
    EditText ugUniversityName;
    @BindView(R.id.ugCity)
    EditText ugCity;
    @BindView(R.id.ugState)
    EditText ugState;
    @BindView(R.id.ugCountry)
    EditText ugCountry;
    @BindView(R.id.ugDegreeObtained)
    EditText ugDegreeObtained;
    @BindView(R.id.ugMajorSubject)
    EditText ugMajorSubject;
    @BindView(R.id.ugGpa)
    EditText ugGpa;
    @BindView(R.id.ugDateOfCompletion)
    TextView ugDateOfCompletion;
    @BindView(R.id.gradUniversityName)
    EditText gradUniversityName;
    @BindView(R.id.gradCity)
    EditText gradCity;
    @BindView(R.id.gradState)
    EditText gradState;
    @BindView(R.id.gradCountry)
    EditText gradCountry;
    @BindView(R.id.gradDegreeObtained)
    EditText gradDegreeObtained;
    @BindView(R.id.gradMajorSubject)
    EditText gradMajorSubject;
    @BindView(R.id.gradGpa)
    EditText gradGpa;
    @BindView(R.id.gradDateOfCompletion)
    TextView gradDateOfCompletion;
    @BindView(R.id.spTests)
    Spinner spTests;
    @BindView(R.id.testScore)
    EditText testScore;
    @BindView(R.id.spTests2)
    Spinner spTests2;
    @BindView(R.id.testScore2)
    EditText testScore2;
    @BindView(R.id.add_test)
    IconTextView add_test;
    @BindView(R.id.remove_test)
    IconTextView remove_test;
    @BindView(R.id.currentEmployer)
    EditText currentEmployer;
    @BindView(R.id.ceFieldOfActivity)
    EditText ceFieldOfActivity;
    @BindView(R.id.cePosition)
    EditText cePosition;
    @BindView(R.id.ceStartDate)
    TextView ceStartDate;
    @BindView(R.id.ceDepartment)
    EditText ceDepartment;
    @BindView(R.id.ceResponsibilities)
    EditText ceResponsibilities;
    @BindView(R.id.prev_employer)
    EditText prevEmployer;
    @BindView(R.id.prev_job_location)
    EditText prevJobLocation;
    @BindView(R.id.prev_jobTitle)
    EditText prevJobTitle;
    @BindView(R.id.prev_job_start_date)
    TextView prevJobStartDate;
    @BindView(R.id.prev_job_end_date)
    TextView prevJobEndDate;
    @BindView(R.id.prevEmploymentType)
    Spinner prevEmploymentType;
    @BindView(R.id.add_prev_job)
    IconTextView add_prev_job_experience;
    @BindView(R.id.remove_prev_job)
    IconTextView remove_prev_job_experience;
    @BindView(R.id.qsOne)
    EditText ansQ1;
    @BindView(R.id.qsTwo)
    EditText ansQ2;
    @BindView(R.id.qsThree)
    EditText ansQ3;
    @BindView(R.id.qsFour)
    EditText ansQ4;
    @BindView(R.id.qsFive)
    EditText ansQ5;
    @BindView(R.id.qsSix)
    EditText ansQ6;
    @BindView(R.id.qsSeven)
    EditText ansQ7;
    @BindView(R.id.qsEight)
    EditText ansQ8;
    @BindView(R.id.statement_of_purpose)
    EditText statementOfPurpose;
    @BindView(R.id.txtPhotoChoose)
    TextView choosePhoto;
    @BindView(R.id.photo)
    TextView ppsizePhoto;
    @BindView(R.id.chooseResume)
    TextView chooseResume;
    @BindView(R.id.resume)
    TextView cv;
    @BindView(R.id.choosePassport)
    TextView choosePassport;
    @BindView(R.id.passport)
    TextView passport;
    @BindView(R.id.chooseCitizenship)
    TextView chooseCitizenship;
    @BindView(R.id.citizenship)
    TextView citizenship;
    @BindView(R.id.chooseSchoolCertificate)
    TextView chooseSchoolCertificate;
    @BindView(R.id.schoolCertificate)
    TextView schoolCertificate;
    @BindView(R.id.chooseHighSchoolCertificate)
    TextView chooseHighSchoolCertificate;
    @BindView(R.id.highSchoolCertificate)
    TextView highSchoolCertiificate;
    @BindView(R.id.chooseUnderGradCertificate)
    TextView chooseUnderGradCertificate;
    @BindView(R.id.underGradCertificate)
    TextView underGradCertificate;
    @BindView(R.id.chooseGradCertificate)
    TextView choosegradCertificate;
    @BindView(R.id.gradCertificate)
    TextView gradCertificate;
    @BindView(R.id.chooseTest)
    TextView chooseTest;
    @BindView(R.id.test)
    TextView test;
    @BindView(R.id.chooseMoreFile)
    TextView chooseMoreFile;
    @BindView(R.id.moreFile)
    TextView moreFile;
    @BindView(R.id.add_attachment)
    IconTextView add_attachment;
    @BindView(R.id.remove_attachment)
    IconTextView remove_attachment;
    @BindView(R.id.ch_agreement)
    CheckBox ch_agreement;
    @BindView(R.id.sign)
    EditText sign;

    @BindView(R.id.date_of_sign)
    TextView date_of_sign;
    @BindView(R.id.test2Layout)
    View test2View;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    private String dateValue = "";
    private String timeValue = "";
    private int changed = 0;
    private Boolean is_permanent_address;
    private List<Uri> mSelected = new ArrayList<>();
    private List<String> mPath;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 129;
    private static final int CHOOSE_PHOTO = 25;
    private static final int CHOOSE_RESUME = 26;
    private static final int CHOOSE_PASSPORT_COPY = 27;
    private static final int CHOOSE_CITIZENSHIP_COPY = 28;
    private static final int CHOOSE_SCHOOL_CERTIFICATE = 29;
    private static final int CHOOSE_HIGH_SCHOOL_CERTIFICATE = 30;
    private static final int CHOOSE_UnderGrad_CERTIFICATE = 31;
    private static final int CHOOSE_Grad_CERTIFICATE = 32;
    private static final int CHOOSE_TEST = 33;
    private static final int CHOOSE_MORE_FILE = 34;
    private String photoPath = "", resumePath = "", passportPath = "", citizenshipPath = "", schoolPath = "", highSPath = "", undergradPath = "", graduatePath = "", testPath = "";
    private int COUNT = 1;
    private String ielts, toefl, gmat, sat, gre, pte;

    public ApplyNowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_now, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Apply Online");
        initData();
        return view;
    }

    private void initData() {
        ielts = "ielts";
        toefl = "toefl";
        gmat = "toefl";
        sat = "toefl";
        gre = "toefl";
        pte = "toefl";
        getSpTitle();
        getSpSex();
        getSpYear();
        getSpIntake();
        getSpCountry();
        getPAddress();
        getSpEmployment();
        getMaritalStatus();
        getTestsTaken();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isConnectionAvailable(getActivity()))
                onSubmit();
                else Toast.makeText(getActivity(), "No internet connection, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_PHOTO);
            }
        });
        chooseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        choosePassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_PASSPORT_COPY);
            }
        });
        chooseCitizenship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_CITIZENSHIP_COPY);
            }
        });
        chooseSchoolCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_SCHOOL_CERTIFICATE);
            }
        });
        chooseHighSchoolCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_HIGH_SCHOOL_CERTIFICATE);
            }
        });
        chooseUnderGradCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_UnderGrad_CERTIFICATE);
            }
        });
        choosegradCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_Grad_CERTIFICATE);
            }
        });
        chooseTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_TEST);
            }
        });
        chooseMoreFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto(CHOOSE_MORE_FILE);
            }
        });
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(dateOfBirth);
            }
        });
        schoolDateOfCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(schoolDateOfCompletion);
            }
        });
        hsDateOfCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(hsDateOfCompletion);
            }
        });
        gradDateOfCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(gradDateOfCompletion);
            }
        });
        ugDateOfCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(ugDateOfCompletion);
            }
        });
        dateOfIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(dateOfIssue);
            }
        });
        dateOfExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(dateOfExpiry);
            }
        });
        date_of_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(date_of_sign);
            }
        });
        ceStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(ceStartDate);
            }
        });
        prevJobStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(prevJobStartDate);
            }
        });
        prevJobEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(prevJobEndDate);
            }
        });
        add_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT == 1) {
                    COUNT = 2;
                    test2View.setVisibility(View.VISIBLE);
                    remove_test.setVisibility(View.VISIBLE);
                } else if (COUNT > 1) {
                    Toast.makeText(getActivity(), "Cannot add more than two tests", Toast.LENGTH_SHORT).show();
                }
            }
        });

        remove_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                COUNT--;
                test2View.setVisibility(View.GONE);
                remove_test.setVisibility(View.GONE);
            }
        });
    }

    @AfterPermissionGranted(REQUEST_CODE_ASK_PERMISSIONS)
    private void getPhoto(int requestCode) {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            Matisse.from(this)
                    .choose(MimeType.allOf())
                    .theme(R.style.Matisse_Dracula)
                    .countable(false)
                    .maxSelectable(1)
                    .gridExpectedSize(this.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new PicassoEngine())
                    .forResult(requestCode);
        } else {
            EasyPermissions.requestPermissions(this, "Before you can select photos, please grant Permissions. Click OK to proceed.",
                    REQUEST_CODE_ASK_PERMISSIONS, perms);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK) {
            photoPath = showFileName(data, choosePhoto, ppsizePhoto);
            ppsizePhoto.setTextColor(getResources().getColor(R.color.primary_text));
        }
        if (requestCode == CHOOSE_RESUME && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            String path = CompressImage.getPathPlus(getContext(), data.getData());
            String[] filename = path.split("/");
            String finalPath = filename[filename.length - 1];
            cv.setText(finalPath);
            chooseResume.setText(R.string.change_file);
            resumePath = path;
            cv.setTextColor(getResources().getColor(R.color.primary_text));
        }
        if (requestCode == CHOOSE_PASSPORT_COPY && resultCode == RESULT_OK) {
            passportPath = showFileName(data, choosePassport, passport);
        }
        if (requestCode == CHOOSE_CITIZENSHIP_COPY && resultCode == RESULT_OK) {
            citizenshipPath = showFileName(data, chooseCitizenship, citizenship);
        }
        if (requestCode == CHOOSE_SCHOOL_CERTIFICATE && resultCode == RESULT_OK) {
            schoolPath = showFileName(data, chooseSchoolCertificate, schoolCertificate);
        }
        if (requestCode == CHOOSE_HIGH_SCHOOL_CERTIFICATE && resultCode == RESULT_OK) {
            highSPath = showFileName(data, chooseHighSchoolCertificate, highSchoolCertiificate);
        }
        if (requestCode == CHOOSE_UnderGrad_CERTIFICATE && resultCode == RESULT_OK) {
            undergradPath = showFileName(data, chooseUnderGradCertificate, underGradCertificate);
        }
        if (requestCode == CHOOSE_Grad_CERTIFICATE && resultCode == RESULT_OK) {
            graduatePath = showFileName(data, choosegradCertificate, gradCertificate);
        }
        if (requestCode == CHOOSE_TEST && resultCode == RESULT_OK) {
            testPath = showFileName(data, chooseTest, test);
        }

        // /////////
        if (requestCode == CHOOSE_MORE_FILE && resultCode == RESULT_OK) {
            showFileName(data, chooseMoreFile, moreFile);
        }
        /////////
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra("CONTENT_TYPE", "*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, CHOOSE_RESUME);
    }

    private String showFileName(Intent data, TextView tv1, TextView fileName) {
        if (changed > 1)
            mSelected.set(0, Matisse.obtainResult(data).get(0));
        else
            mSelected = Matisse.obtainResult(data);
        String path = getRealPathFromURI(getContext(), mSelected.get(0));
        String[] filename = path.split("/");
        String finalPath = filename[filename.length - 1];
        fileName.setText(finalPath);
        tv1.setText(R.string.change_file);
        return path;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void chooseDate(final TextView tv) {
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
                        dateValue = dateStr;
                        tv.setText(dateStr);
                        tv.setTextColor(getResources().getColor(R.color.secondary_text));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void onSubmit() {

        MultipartBody.Part photo = null, resume = null, citizen = null, passport = null, school = null, high_school = null, undergraduate = null, graduate = null, testBody = null;
        boolean titleErr, fnameErr, lnameErr, dateErr, birthErr, citizenErr, courseErr, intakeErr, countryErr, genderErr, statusErr, nationalityErr, mobileErr, emailErr, statementErr, signErr, signdateErr, photoErr, resumeErr;
        // personal information
        String goingToStudyYear = spYear.getSelectedItem().toString().trim();
        String intake = spIntake.getSelectedItem().toString().trim();
        String desired_course = desiredCourse.getText().toString().trim();
        String permanentCountry = spCountry.getSelectedItem().toString().trim();
        String title = spTitle.getSelectedItem().toString().trim();
        String last_name = lastName.getText().toString().trim();
        String middle_name = middleName.getText().toString().trim();
        String first_name = firstName.getText().toString().trim();
        String date_of_birth = dateOfBirth.getText().toString().trim();
        String place_of_birth = placeOfBirth.getText().toString().trim();
        String gender = spSex.getSelectedItem().toString().trim();
        String txt_nationality = nationality.getText().toString().trim();
        String maritalStatus = spMaritalStatus.getSelectedItem().toString().trim();
        String citizenship_no = citizenshipNo.getText().toString().trim();
        String passport_no = passportNo.getText().toString().trim();
        String pp_date_of_issue = dateOfIssue.getText().toString().trim();
        String pp_date_of_expiry = dateOfExpiry.getText().toString().trim();
        String pp_place_of_issue = placeOfIssue.getText().toString().trim();

        // mailing address
        String street_name_no = streetNameNo.getText().toString().trim();
        String city = maCity.getText().toString().trim();
        String state = maState.getText().toString().trim();
        String country = maCountry.getText().toString().trim();
        String phone = maPhone.getText().toString().trim();
        String mobile = maMobile.getText().toString().trim();
        String email = maEmail.getText().toString().trim();
        if (ch_permanentAddress.isChecked()) {
            is_permanent_address = true;
        } else {
            is_permanent_address = false;
        }
        Boolean isPermanentAddress = is_permanent_address;

        // school education
        String school_name = schoolName.getText().toString().trim();
        String school_city = schoolCity.getText().toString().trim();
        String school_state = schoolState.getText().toString().trim();
        String school_country = schoolCountry.getText().toString().trim();
        String school_qualification = schoolQualification.getText().toString().trim();
        String school_marksObtained = schoolGpa.getText().toString().trim();
        String school_date_of_completion = schoolDateOfCompletion.getText().toString().trim();

        // high school education
        String high_school_name = highSchoolName.getText().toString().trim();
        String high_school_city = highSchoolCity.getText().toString().trim();
        String high_school_state = highSchoolState.getText().toString().trim();
        String high_school_country = highSchoolCountry.getText().toString().trim();
        String high_school_qualification = hsQualification.getText().toString().trim();
        String high_school_gpa = hsGpa.getText().toString().trim();
        String high_school_date_of_completion = hsDateOfCompletion.getText().toString().trim();

        // undergraduate
        String undergradUniversity = ugUniversityName.getText().toString().trim();
        String undergradCity = ugCity.getText().toString().trim();
        String undergradState = ugState.getText().toString().trim();
        String undergradCountry = ugCountry.getText().toString().trim();
        String undergradDegreeObtained = ugDegreeObtained.getText().toString().trim();
        String undergradMajorSubject = ugMajorSubject.getText().toString().trim();
        String underGradGPA = ugGpa.getText().toString().trim();
        String underGradDateOfCompletion = ugDateOfCompletion.getText().toString().trim();

        // graduate
        String grad_university = gradUniversityName.getText().toString().trim();
        String grad_city = gradCity.getText().toString().trim();
        String grad_state = gradState.getText().toString().trim();
        String grad_country = gradCountry.getText().toString().trim();
        String grad_degree_obtained = gradDegreeObtained.getText().toString().trim();
        String grad_major_subject = gradMajorSubject.getText().toString().trim();
        String grad_gpa = gradGpa.getText().toString().trim();
        String grad_date_of_completion = gradDateOfCompletion.getText().toString().trim();

        // test
        String test = spTests.getSelectedItem().toString();
        String test_score = testScore.getText().toString();
        String test2 = spTests2.getSelectedItem().toString();
        String test_score2 = testScore2.getText().toString();

        // current employement
        String current_employer = currentEmployer.getText().toString().trim();
        String current_field_of_activity = ceFieldOfActivity.getText().toString().trim();
        String current_work_position = cePosition.getText().toString().trim();
        String ce_start_date = ceStartDate.getText().toString().trim();
        String curent_department = ceDepartment.getText().toString().trim();
        String current_responsibilities = ceResponsibilities.getText().toString().trim();
        String current_employ_type;
        if (ceEmploymentType.getSelectedItemPosition() == 0)
            current_employ_type = "null";
        else current_employ_type = ceEmploymentType.getSelectedItem().toString();

        // previous job exp
        String prev_employer = prevEmployer.getText().toString().trim();
        String prev_job_location = prevJobLocation.getText().toString().trim();
        String prev_job_title = prevJobTitle.getText().toString().trim();
        String prev_job_start_date = prevJobStartDate.getText().toString().trim();
        String prev_job_end_date = prevJobEndDate.getText().toString().trim();
        String prev_employ_type;
        if (prevEmploymentType.getSelectedItemPosition() == 0)
            prev_employ_type = "null";
        else prev_employ_type = prevEmploymentType.getSelectedItem().toString();

        // questions
        String ansOne = ansQ1.getText().toString().trim();
        String ansTwo = ansQ2.getText().toString().trim();
        String ansThree = ansQ3.getText().toString().trim();
        String ansFour = ansQ4.getText().toString().trim();
        String ansFive = ansQ5.getText().toString().trim();
        String ansSix = ansQ6.getText().toString().trim();
        String ansSeven = ansQ7.getText().toString().trim();
        String ansEight = ansQ8.getText().toString().trim();

        // statement of purpose
        String statement_of_purpose = statementOfPurpose.getText().toString().trim();

        // certification
        boolean certified;
        if (ch_agreement.isChecked()) certified = true;
        else certified = false;

        // signature
        String signature = sign.getText().toString().trim();
        String dateOfSign = date_of_sign.getText().toString().trim();

        returnScore(test_score, test);
        returnScore(test_score2, test2);

        fnameErr = checkEmpty2(first_name, "First name is required", fnLayout);
        lnameErr = checkEmpty2(last_name, "Last name is required", lnLayout);
        birthErr = checkEmpty2(place_of_birth, "This field is required", pobLayout);
        nationalityErr = checkEmpty2(txt_nationality, "This field is required", nationalityLayout);
        citizenErr = checkEmpty2(citizenship_no, "This field is required", cnLayout);
        courseErr = checkEmpty2(desired_course, "This field is required", courseLayout);

        if (checkEmpty2(mobile, "This field is required", mblLayout) == true)
            mobileErr = true;
        else if (!TextUtils.isDigitsOnly(mobile)) {
            mblLayout.setError("Enter the valid phone number");
            mobileErr = true;
        } else if (mobile.length() < 10) {
            mblLayout.setError("Phone number should not be less than 10");
            mobileErr = true;
        } else {
            mblLayout.setError(null);
            mobileErr = false;
        }

        if (email.isEmpty() || email.equals("")) {
            emailLayout.setError("This field is required");
            emailErr = true;
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(null);
            emailErr = false;
        } else {
            emailLayout.setError("Invalid email address");
            emailErr = true;
        }

        statementErr = checkEmpty3(statement_of_purpose, statementOfPurpose);
        dateErr = checkEmpty(dateValue, "Date is empty*", dateOfBirth);
        titleErr = showSpError(spTitle, "Select Title*");
        genderErr = showSpError(spSex, "Select Gender*");
        statusErr = showSpError(spMaritalStatus, "Select Status*");
        intakeErr = showSpError(spIntake, "Select Session*");
        countryErr = showSpError(spCountry, "Select Country*");
        signErr = checkEmpty3(signature, sign);
        signdateErr = checkEmpty(dateValue, "Date is empty*", date_of_sign);

        if (!photoPath.isEmpty()) {
            photo = getMultipartData(photoPath, "photograph");
            photoErr = false;
        } else {
            ppsizePhoto.setText("This field is required*");
            ppsizePhoto.setTextColor(getResources().getColor(R.color.red));
            photoErr = true;
        }

        if (!resumePath.isEmpty()) {
            resume = getMultipartData(resumePath, "resume");
            resumeErr = false;
        } else {
            cv.setText("This field is required*");
            cv.setTextColor(getResources().getColor(R.color.red));
            resumeErr = true;
        }

        if (!citizenshipPath.isEmpty())
            citizen = getMultipartData(citizenshipPath, "citizenship_copy");
        if (!passportPath.isEmpty())
            passport = getMultipartData(passportPath, "passport_copy");
        if (!schoolPath.isEmpty())
            school = getMultipartData(schoolPath, "school_education_certificate");
        if (!highSPath.isEmpty())
            high_school = getMultipartData(highSPath, "high_school_certificate");
        if (!undergradPath.isEmpty())
            undergraduate = getMultipartData(undergradPath, "undergrad_certificate");
        if (!graduatePath.isEmpty())
            graduate = getMultipartData(graduatePath, "graduate_certificate");
        if (!testPath.isEmpty())
            testBody = getMultipartData(testPath, "test");

        if (undergradMajorSubject.equals(""))
            undergradMajorSubject = "null";
        if (grad_major_subject.equals(""))
            grad_major_subject = "null";

        if (!fnameErr && !lnameErr && !birthErr && !nationalityErr && !citizenErr && !courseErr && !mobileErr && !emailErr && !statementErr &&
                !dateErr && !titleErr && !genderErr && !statusErr && !intakeErr && !countryErr && !signErr && !signdateErr && !photoErr && !resumeErr) {
            AppliedUser appliedUser = new AppliedUser();
            appliedUser.year = goingToStudyYear;
            appliedUser.intake = intake;
            appliedUser.course = desired_course;
            appliedUser.permanentCountry = permanentCountry;
            appliedUser.title = title;
            appliedUser.lastName = last_name;
            appliedUser.middleName = middle_name;
            appliedUser.firstName = first_name;
            appliedUser.dateOfBirth = date_of_birth;
            appliedUser.placeOfBirth = place_of_birth;
            appliedUser.sex = gender;
            appliedUser.nationality = txt_nationality;
            appliedUser.maritalStatus = maritalStatus;
            appliedUser.citizenshipNumber = citizenship_no;
            appliedUser.passportNumber = passport_no;
            appliedUser.dateOfIssue = pp_date_of_issue;
            appliedUser.dateOfExpiry = pp_date_of_expiry;
            appliedUser.placeOfIssue = pp_place_of_issue;

            //mailing
            appliedUser.streetNameNumber = street_name_no;
            appliedUser.city = city;
            appliedUser.state = state;
            appliedUser.country = country;
            appliedUser.phone = phone;
            appliedUser.mobile = mobile;
            appliedUser.email = email;
            appliedUser.isPermanent = isPermanentAddress;

            // school education
            appliedUser.schoolName = school_name;
            appliedUser.schoolCity = school_city;
            appliedUser.schoolState = school_state;
            appliedUser.schoolCountry = school_country;
            appliedUser.schoolQualificationObtained = school_qualification;
            appliedUser.schoolMarksObtained = school_marksObtained;
            appliedUser.schoolDateOfCompletion = school_date_of_completion;

            // high school
            appliedUser.highSchoolName = high_school_name;
            appliedUser.highSchoolCity = high_school_city;
            appliedUser.highSchoolState = high_school_state;
            appliedUser.highSchoolCountry = high_school_country;
            appliedUser.highSchoolQualificationObtained = high_school_qualification;
            appliedUser.highSchoolMarksObtained = high_school_gpa;
            appliedUser.highSchoolDateOfCompletion = high_school_date_of_completion;

            // undergraduate
            appliedUser.undergradUniversity = undergradUniversity;
            appliedUser.undergradCity = undergradCity;
            appliedUser.undergradState = undergradState;
            appliedUser.undergradCountry = undergradCountry;
            appliedUser.undergradDegreeObtained = undergradDegreeObtained;
            appliedUser.undergradMajorSubject = undergradMajorSubject;
            appliedUser.undergradMarksObtained = underGradGPA;
            appliedUser.undergradDateOfCompletion = underGradDateOfCompletion;

            // graduate
            appliedUser.graduateUniversity = grad_university;
            appliedUser.graduateCity = grad_city;
            appliedUser.graduateState = grad_state;
            appliedUser.graduateCountry = grad_country;
            appliedUser.graduateDegreeObtained = grad_degree_obtained;
            appliedUser.graduateMajorSubject = grad_major_subject;
            appliedUser.graduateMarksObtained = grad_gpa;
            appliedUser.graduateDateOfCompletion = grad_date_of_completion;

            //tests
            appliedUser.ielts = ielts;
            appliedUser.toefl = toefl;
            appliedUser.sat = sat;
            appliedUser.gre = gre;
            appliedUser.gmat = gmat;
            appliedUser.pte = pte;

            // current emp
            appliedUser.employmentCurrentEmployer = current_employer;
            appliedUser.fieldOfActivity = current_field_of_activity;
            appliedUser.position = current_work_position;
            appliedUser.startDate = ce_start_date;
            appliedUser.department = curent_department;
            appliedUser.employmentType = current_employ_type;
            appliedUser.responsibilites = current_responsibilities;

            // previous job exp
            appliedUser.previousEmployer = prev_employer;
            appliedUser.previousLocation = prev_job_location;
            appliedUser.previousJobTitle = prev_job_title;
            appliedUser.previousStartDate = prev_job_start_date;
            appliedUser.previousEndDate = prev_job_end_date;
            appliedUser.previousEmploymentType = prev_employ_type;

            // questions
            appliedUser.question1 = ansOne;
            appliedUser.question2 = ansTwo;
            appliedUser.question3 = ansThree;
            appliedUser.question4 = ansFour;
            appliedUser.question5 = ansFive;
            appliedUser.question6 = ansSix;
            appliedUser.question7 = ansSeven;
            appliedUser.question8 = ansEight;

            // statement of purpose
            appliedUser.statementOfPurpose = statement_of_purpose;

            // attachments
            appliedUser.photograph = photoPath;
            appliedUser.resume = resumePath;
            appliedUser.passportCopy = passportPath;
            appliedUser.citizenshipCopy = citizenshipPath;
            appliedUser.schoolEducationCertificate = schoolPath;
            appliedUser.highSchoolCertificate = highSPath;
            appliedUser.undergradCertificate = undergradPath;
            appliedUser.graduateCertificate = graduatePath;
            appliedUser.test = testPath;

            // certification
            appliedUser.certification = certified;

            // signature
            appliedUser.signatureName = signature;
            appliedUser.dateOfSignature = dateOfSign;

            sendData(prepareData(appliedUser), photo, resume, citizen, passport, school, high_school, undergraduate, graduate, testBody);
        } else
            Toast.makeText(getActivity(), "Please fill all the * fields.", Toast.LENGTH_SHORT).show();
    }

    private boolean showSpError(Spinner spinner, String errorMsg) {
        if (spinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setTextSize(12);
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(errorMsg);//changes the selected item text to this
            return true;
        } else return false;
    }

    private void getSpTitle() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] title = new String[]{
                "Select Title*", "Mr.", "Mrs.", "Miss."};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item2, title);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spTitle.setAdapter(spinnerArrayAdapter);
    }

    private void getSpYear() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] year = new String[]{
                "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, year);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spYear.setAdapter(spinnerArrayAdapter);
    }

    private void getSpSex() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] sex = new String[]{
                "Select Gender", "Male", "Female", "Others"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, sex);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spSex.setAdapter(spinnerArrayAdapter);
    }

    private void getSpIntake() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] intake = new String[]{
                "Select Session", "Spring(Jan - Feb)", "Summer1(Mar - Aprl)", "Summer2(June - July)", "Fall(Aug - Oct"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, intake);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spIntake.setAdapter(spinnerArrayAdapter);
    }

    private void getSpCountry() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] country = new String[]{
                "Select Country", "Germany", "USA", "Spain", "UK", "Australia", "New Zealand", "Canada", "India", "Denmark", "Cyprus", "Poland", "France", "Hungary", "Malta", "Finland", "Austria", "Japan", "Bangladesh", "China", "Philippines", "Greece", "Ireland", "Italy", "Switzerland", "Sweden", "Netherlands", "Lithuania", "Latvia"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, country);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spCountry.setAdapter(spinnerArrayAdapter);
    }

    private void getPAddress() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] pAddress = new String[]{
                "Select", "Yes", "No"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, pAddress);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spPermanentAddress.setAdapter(spinnerArrayAdapter);
    }

    private void getSpEmployment() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] employment = new String[]{
               "Select Emp. Type", "Full-time", "Part-time", "contract"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, employment);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        ceEmploymentType.setAdapter(spinnerArrayAdapter);
        prevEmploymentType.setAdapter(spinnerArrayAdapter);
    }

    private void getMaritalStatus() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] marital_status = new String[]{
                "Select your marital status", "Single", "Married", "Separated", "Divorced", "Others"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, marital_status);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spMaritalStatus.setAdapter(spinnerArrayAdapter);
    }

    private void getTestsTaken() {
        final ArrayAdapter<String> spinnerArrayAdapter;
        String[] tests = new String[]{
                "IELTS", "TOEFL", "SAT", "GRE", "GMAT", "PTE"};
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item2, tests);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spTests.setAdapter(spinnerArrayAdapter);
        spTests2.setAdapter(spinnerArrayAdapter);
    }

    private void sendData(HashMap<String, RequestBody> map, MultipartBody.Part photo, MultipartBody.Part resume, MultipartBody.Part citizen, MultipartBody.Part passport, MultipartBody.Part school, MultipartBody.Part highschool, MultipartBody.Part undergraduate, MultipartBody.Part graduate, MultipartBody.Part test) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Nimainterface nimainterface = ImmortalApplication.getRetrofit().create(Nimainterface.class);
        nimainterface.postAppliedUsers(map, photo, resume, passport, citizen, school, highschool, undergraduate, graduate, test).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("fatal", "res" + response.raw().toString());
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successfully submitted.", Toast.LENGTH_SHORT).show();
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

    // do not try to touch this
    private HashMap prepareData(AppliedUser user) {

        // personal info
        RequestBody year = RequestBody.create(MediaType.parse("text/plain"), user.getYear());
        RequestBody intake = RequestBody.create(MediaType.parse("text/plain"), user.getIntake());
        RequestBody course = RequestBody.create(MediaType.parse("text/plain"), user.getCourse());
        RequestBody permanent_country = RequestBody.create(MediaType.parse("text/plain"), user.getPermanentCountry());
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), user.getTitle());
        RequestBody last_name = RequestBody.create(MediaType.parse("text/plain"), user.getLastName());
        RequestBody middle_name = RequestBody.create(MediaType.parse("text/plain"), user.getMiddleName());
        RequestBody first_name = RequestBody.create(MediaType.parse("text/plain"), user.getFirstName());
        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), user.getDateOfBirth());
        RequestBody pob = RequestBody.create(MediaType.parse("text/plain"), user.getPlaceOfBirth());
        RequestBody sex = RequestBody.create(MediaType.parse("text/plain"), user.getSex());
        RequestBody nationality = RequestBody.create(MediaType.parse("text/plain"), user.getNationality());
        RequestBody marital_st = RequestBody.create(MediaType.parse("text/plain"), user.getMaritalStatus());
        RequestBody citizen_num = RequestBody.create(MediaType.parse("text/plain"), user.getCitizenshipNumber());
        RequestBody pass_num = RequestBody.create(MediaType.parse("text/plain"), user.getPassportNumber());
        RequestBody dateofissue = RequestBody.create(MediaType.parse("text/plain"), user.getDateOfIssue());
        RequestBody dateofexpiry = RequestBody.create(MediaType.parse("text/plain"), user.getDateOfExpiry());
        RequestBody placeofissue = RequestBody.create(MediaType.parse("text/plain"), user.getPlaceOfIssue());
        RequestBody streetnum = RequestBody.create(MediaType.parse("text/plain"), user.getStreetNameNumber());
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), user.getCity());
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), user.getState());
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), user.getCountry());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), user.getPhone());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), user.getMobile());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), user.getEmail());
        RequestBody is_permanent = RequestBody.create(MediaType.parse("text/plain"), user.getIsPermanent().toString());
        RequestBody school_name = RequestBody.create(MediaType.parse("text/plain"), user.getSchoolName());
        RequestBody school_city = RequestBody.create(MediaType.parse("text/plain"), user.getSchoolCity());
        RequestBody school_state = RequestBody.create(MediaType.parse("text/plain"), user.getSchoolState());
        RequestBody school_country = RequestBody.create(MediaType.parse("text/plain"), user.getSchoolCountry());
        RequestBody school_qua_obt = RequestBody.create(MediaType.parse("text/plain"), user.getSchoolQualificationObtained());
        RequestBody school_marks_obt = RequestBody.create(MediaType.parse("text/plain"), user.getSchoolMarksObtained());
        RequestBody school_date_of_comp = RequestBody.create(MediaType.parse("text/plain"), user.getSchoolDateOfCompletion());
        RequestBody high_school_name = RequestBody.create(MediaType.parse("text/plain"), user.getHighSchoolName());
        RequestBody high_school_city = RequestBody.create(MediaType.parse("text/plain"), user.getHighSchoolCity());
        RequestBody high_school_state = RequestBody.create(MediaType.parse("text/plain"), user.getHighSchoolState());
        RequestBody highschool_country_ = RequestBody.create(MediaType.parse("text/plain"), user.getHighSchoolCountry());
        RequestBody high_school_qua_obt = RequestBody.create(MediaType.parse("text/plain"), user.getHighSchoolQualificationObtained());
        RequestBody high_school_marks_obt = RequestBody.create(MediaType.parse("text/plain"), user.getHighSchoolMarksObtained());
        RequestBody high_school_date_of_comp = RequestBody.create(MediaType.parse("text/plain"), user.getHighSchoolDateOfCompletion());
        RequestBody undergrad_uni = RequestBody.create(MediaType.parse("text/plain"), user.getUndergradUniversity());
        RequestBody undergrad_city = RequestBody.create(MediaType.parse("text/plain"), user.getUndergradCity());
        RequestBody undergrad_state = RequestBody.create(MediaType.parse("text/plain"), user.getUndergradState());
        RequestBody undergrad_country = RequestBody.create(MediaType.parse("text/plain"), user.getUndergradCountry());
        RequestBody undergrad_degree_obt = RequestBody.create(MediaType.parse("text/plain"), user.getUndergradDegreeObtained());
        RequestBody undergrad_major_sub = RequestBody.create(MediaType.parse("text/plain"), user.getUndergradMajorSubject());
        RequestBody undergrad_marks_obt = RequestBody.create(MediaType.parse("text/plain"), user.getUndergradMarksObtained());
        RequestBody undergrad_dateofcomp = RequestBody.create(MediaType.parse("text/plain"), user.getUndergradDateOfCompletion());
        RequestBody graduate_uni = RequestBody.create(MediaType.parse("text/plain"), user.getGraduateUniversity());
        RequestBody graduate_city = RequestBody.create(MediaType.parse("text/plain"), user.getGraduateCity());
        RequestBody graduate_state = RequestBody.create(MediaType.parse("text/plain"), user.getGraduateState());
        RequestBody graduate_country = RequestBody.create(MediaType.parse("text/plain"), user.getGraduateCountry());
        RequestBody graduate_degree_obt = RequestBody.create(MediaType.parse("text/plain"), user.getGraduateDegreeObtained());
        RequestBody graduate_major_sub = RequestBody.create(MediaType.parse("text/plain"), user.getGraduateMajorSubject());
        RequestBody graduate_marks_obt = RequestBody.create(MediaType.parse("text/plain"), user.getGraduateMarksObtained());
        RequestBody graduate_dateofcomp = RequestBody.create(MediaType.parse("text/plain"), user.getGraduateDateOfCompletion());
        RequestBody ielts = RequestBody.create(MediaType.parse("text/plain"), user.getIelts());
        RequestBody toefl = RequestBody.create(MediaType.parse("text/plain"), user.getToefl());
        RequestBody sat = RequestBody.create(MediaType.parse("text/plain"), user.getSat());
        RequestBody gre = RequestBody.create(MediaType.parse("text/plain"), user.getGre());
        RequestBody gmat = RequestBody.create(MediaType.parse("text/plain"), user.getGmat());
        RequestBody pte = RequestBody.create(MediaType.parse("text/plain"), user.getPte());
        RequestBody curr_emp = RequestBody.create(MediaType.parse("text/plain"), user.getEmploymentCurrentEmployer());
        RequestBody curr_fielofacti = RequestBody.create(MediaType.parse("text/plain"), user.getFieldOfActivity());
        RequestBody curr_position = RequestBody.create(MediaType.parse("text/plain"), user.getPosition());
        RequestBody curr_start_date = RequestBody.create(MediaType.parse("text/plain"), user.getStartDate());
        RequestBody curr_department = RequestBody.create(MediaType.parse("text/plain"), user.getDepartment());
        RequestBody curr_emp_type = RequestBody.create(MediaType.parse("text/plain"), user.getEmploymentType());
        RequestBody curr_resp = RequestBody.create(MediaType.parse("text/plain"), user.getResponsibilites());
        RequestBody pre_emp = RequestBody.create(MediaType.parse("text/plain"), user.getPreviousEmployer());
        RequestBody pre_location = RequestBody.create(MediaType.parse("text/plain"), user.getPreviousLocation());
        RequestBody pre_job_title = RequestBody.create(MediaType.parse("text/plain"), user.getPreviousJobTitle());
        RequestBody pre_start_date = RequestBody.create(MediaType.parse("text/plain"), user.getPreviousStartDate());
        RequestBody pre_end_date = RequestBody.create(MediaType.parse("text/plain"), user.getPreviousEndDate());
        RequestBody pre_emp_type = RequestBody.create(MediaType.parse("text/plain"), user.getPreviousEmploymentType());
        RequestBody ques1 = RequestBody.create(MediaType.parse("text/plain"), user.getQuestion1());
        RequestBody ques2 = RequestBody.create(MediaType.parse("text/plain"), user.getQuestion2());
        RequestBody ques3 = RequestBody.create(MediaType.parse("text/plain"), user.getQuestion3());
        RequestBody ques4 = RequestBody.create(MediaType.parse("text/plain"), user.getQuestion4());
        RequestBody ques5 = RequestBody.create(MediaType.parse("text/plain"), user.getQuestion5());
        RequestBody ques6 = RequestBody.create(MediaType.parse("text/plain"), user.getQuestion6());
        RequestBody ques7 = RequestBody.create(MediaType.parse("text/plain"), user.getQuestion7());
        RequestBody ques8 = RequestBody.create(MediaType.parse("text/plain"), user.getQuestion8());
        RequestBody statement_of_pur = RequestBody.create(MediaType.parse("text/plain"), user.getStatementOfPurpose());
        RequestBody sign_name = RequestBody.create(MediaType.parse("text/plain"), user.getSignatureName());
        RequestBody sign_date = RequestBody.create(MediaType.parse("text/plain"), user.getDateOfSignature());
        RequestBody certification = RequestBody.create(MediaType.parse("text/plain"), user.getCertification().toString());

        HashMap<String, RequestBody> mapFile = new HashMap<>();

        mapFile.put("year", year);
        mapFile.put("intake", intake);
        mapFile.put("course", course);
        mapFile.put("permanent_country", permanent_country);
        mapFile.put("title", title);
        mapFile.put("last_name", last_name);
        mapFile.put("middle_name", middle_name);
        mapFile.put("first_name", first_name);
        mapFile.put("date_of_birth", dob);
        mapFile.put("place_of_birth", pob);
        mapFile.put("sex", sex);
        mapFile.put("nationality", nationality);
        mapFile.put("marital_status", marital_st);
        mapFile.put("citizenship_number", citizen_num);
        mapFile.put("passport_number", pass_num);
        mapFile.put("date_of_issue", dateofissue);
        mapFile.put("date_of_expiry", dateofexpiry);
        mapFile.put("place_of_issue", placeofissue);
        mapFile.put("street_name_number", streetnum);
        mapFile.put("city", city);
        mapFile.put("state", state);
        mapFile.put("country", country);
        mapFile.put("phone", phone);
        mapFile.put("mobile", mobile);
        mapFile.put("email", email);
        mapFile.put("is_permanent", is_permanent);
        mapFile.put("school_name", school_name);
        mapFile.put("school_city", school_city);
        mapFile.put("school_state", school_state);
        mapFile.put("school_country", school_country);
        mapFile.put("school_qualification_obtained", school_qua_obt);
        mapFile.put("school_marks_obtained", school_marks_obt);
        mapFile.put("school_date_of_completion", school_date_of_comp);
        mapFile.put("high_school_name", high_school_name);
        mapFile.put("high_school_city", high_school_city);
        mapFile.put("high_school_state", high_school_state);
        mapFile.put("high_school_country", highschool_country_);
        mapFile.put("high_school_qualification_obtained", high_school_qua_obt);
        mapFile.put("high_school_marks_obtained", high_school_marks_obt);
        mapFile.put("high_school_date_of_completion", high_school_date_of_comp);
        mapFile.put("undergrad_university", undergrad_uni);
        mapFile.put("undergrad_city", undergrad_city);
        mapFile.put("undergrad_state", undergrad_state);
        mapFile.put("undergrad_country", undergrad_country);
        mapFile.put("undergrad_degree_obtained", undergrad_degree_obt);
        mapFile.put("undergrad_major_subject", undergrad_major_sub);
        mapFile.put("undergrad_marks_obtained", undergrad_marks_obt);
        mapFile.put("undergrad_date_of_completion", undergrad_dateofcomp);
        mapFile.put("graduate_university", graduate_uni);
        mapFile.put("graduate_city", graduate_city);
        mapFile.put("graduate_state", graduate_state);
        mapFile.put("graduate_country", graduate_country);
        mapFile.put("graduate_degree_obtained", graduate_degree_obt);
        mapFile.put("graduate_major_subject", graduate_major_sub);
        mapFile.put("graduate_marks_obtained", graduate_marks_obt);
        mapFile.put("graduate_date_of_completion", graduate_dateofcomp);
        mapFile.put("ielts", ielts);
        mapFile.put("toefl", toefl);
        mapFile.put("sat", sat);
        mapFile.put("gmat", gmat);
        mapFile.put("gre", gre);
        mapFile.put("pte", pte);
        mapFile.put("employment_current_employer", curr_emp);
        mapFile.put("field_of_activity", curr_fielofacti);
        mapFile.put("position", curr_position);
        mapFile.put("start_date", curr_start_date);
        mapFile.put("department", curr_department);
        mapFile.put("employment_type", curr_emp_type);
        mapFile.put("responsibilites", curr_resp);
        mapFile.put("previous_employer", pre_emp);
        mapFile.put("previous_location", pre_location);
        mapFile.put("previous_job_title", pre_job_title);
        mapFile.put("previous_start_date", pre_start_date);
        mapFile.put("previous_end_date", pre_end_date);
        mapFile.put("previous_employment_type", pre_emp_type);
        mapFile.put("question1", ques1);
        mapFile.put("question2", ques2);
        mapFile.put("question3", ques3);
        mapFile.put("question4", ques4);
        mapFile.put("question5", ques5);
        mapFile.put("question6", ques6);
        mapFile.put("question7", ques7);
        mapFile.put("question8", ques8);
        mapFile.put("statement_of_purpose", statement_of_pur);
        mapFile.put("certification", certification);
        mapFile.put("signature_name", sign_name);
        mapFile.put("date_of_signature", sign_date);
        return mapFile;
    }

    private MultipartBody.Part getMultipartData(String path, String attrib) {
        MultipartBody.Part body = null;
        try {
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData(attrib, file.getName(), requestFile);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
        return body;
    }

    private boolean checkEmpty(String input, String msg, TextView textView) {
        if (input.isEmpty() || input.equals("")) {
            textView.setText(msg);
            textView.setTextColor(getResources().getColor(R.color.red));
            return true;
        } else {
            textView.setTextColor(getResources().getColor(R.color.secondary_text));
            return false;
        }
    }

    private boolean checkEmpty2(String input, String msg, TextInputLayout layout) {
        if (input.isEmpty() || input.equals("")) {
            layout.setError(msg);
            return true;
        } else {
            layout.setError(null);
            return false;
        }
    }

    private boolean checkEmpty3(String input, EditText editText) {
        if (input.isEmpty() || input.equals("")) {
            editText.setHint("This field is required");
            editText.setHintTextColor(getResources().getColor(R.color.red));
            return true;
        } else return false;
    }

    private void returnScore(String value, String test) {
        if (ielts.equalsIgnoreCase(test))
            ielts = value;
        else if (toefl.equalsIgnoreCase(test))
            toefl = value;
        else if (gmat.equalsIgnoreCase(test))
            gmat = value;
        else if (gre.equalsIgnoreCase(test))
            gre = value;
        else if (sat.equalsIgnoreCase(test))
            sat = value;
        else if (pte.equalsIgnoreCase(test))
            pte = value;
    }
}
