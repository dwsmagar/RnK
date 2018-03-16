package com.susankya.rnk.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.susankya.rnk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppliedUserDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.intake)
    TextView intake;
    @BindView(R.id.course)
    TextView course;
    @BindView(R.id.country)
    TextView country;
    @BindView(R.id.firstName)
    TextView first_name;
    @BindView(R.id.middleName)
    TextView middle_name;
    @BindView(R.id.middleLayout)
    View middleLayout;
    @BindView(R.id.last_name)
    TextView last_name;
    @BindView(R.id.dob)
    TextView dob;
    @BindView(R.id.pob)
    TextView pob;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.nationality)
    TextView nationality;
    @BindView(R.id.marital_stat)
    TextView marital_status;
    @BindView(R.id.citizenshipNo)
    TextView citizenship_no;
    @BindView(R.id.passport)
    TextView passport;
    @BindView(R.id.dateofissue)
    TextView pass_dateofissue;
    @BindView(R.id.dateofexpiry)
    TextView pass_dateofexpiry;
    @BindView(R.id.placeofissue)
    TextView pass_placeofissue;

    // mailing view
    @BindView(R.id.street_name)
    TextView street_name;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.stateorprovince)
    TextView state;
    @BindView(R.id.mail_country)
    TextView mailing_country;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.email)
    TextView email;

    // school education view
    @BindView(R.id.school_name)
    TextView schoolName;
    @BindView(R.id.school_city)
    TextView schoolCity;
    @BindView(R.id.school_state)
    TextView schoolState;
    @BindView(R.id.school_country)
    TextView schoolCountry;
    @BindView(R.id.school_qualification)
    TextView schoolQualification;
    @BindView(R.id.school_grade)
    TextView schoolGrade;
    @BindView(R.id.school_completed)
    TextView schoolDateofCompete;

    // high school view
    @BindView(R.id.highschool_name)
    TextView highSchoolName;
    @BindView(R.id.highschool_city)
    TextView highSchoolCity;
    @BindView(R.id.highschool_state)
    TextView highSchoolState;
    @BindView(R.id.highschool_country)
    TextView highSchoolCountry;
    @BindView(R.id.highschool_qualification)
    TextView highSchoolQualification;
    @BindView(R.id.highschool_grade)
    TextView highSchoolGrade;
    @BindView(R.id.highschool_completed)
    TextView highSchoolDateofCompete;

    // undergraduate view
    @BindView(R.id.university)
    TextView university;
    @BindView(R.id.undergrad_city)
    TextView undergraduateCity;
    @BindView(R.id.undergrad_state)
    TextView undergraduateState;
    @BindView(R.id.undergrad_country)
    TextView undergraduateCountry;
    @BindView(R.id.undergrad_qualification)
    TextView undergraduateQualification;
    @BindView(R.id.undergrad_grade)
    TextView undergraduateGrade;
    @BindView(R.id.undergrad_major)
    TextView undergraduateMajorSubject;
    @BindView(R.id.undergrad_completed)
    TextView undergraduateDateofCompete;

    //Graduate
    @BindView(R.id.graduate_university)
    TextView graduateUniversity;
    @BindView(R.id.graduate_city)
    TextView graduateCity;
    @BindView(R.id.graduate_state)
    TextView graduateState;
    @BindView(R.id.graduate_country)
    TextView graduateCountry;
    @BindView(R.id.graduate_qualification)
    TextView graduateQualification;
    @BindView(R.id.graduate_grade)
    TextView graduateGrade;
    @BindView(R.id.graduate_major)
    TextView graduateMajorSubject;
    @BindView(R.id.graduate_completed)
    TextView graduateDateofCompete;

    // test view
    @BindView(R.id.ielts_score)
    TextView ielts;
    @BindView(R.id.toefl_score)
    TextView toefl;
    @BindView(R.id.gmat_score)
    TextView gmat;
    @BindView(R.id.gre_score)
    TextView gre;
    @BindView(R.id.sat_score)
    TextView sat;
    @BindView(R.id.pte_score)
    TextView pte;
    @BindView(R.id.toefl_layout)
    View toeflLayout;
    @BindView(R.id.gmat_layout)
    View gmatLayout;
    @BindView(R.id.gre_layout)
    View greLayout;
    @BindView(R.id.sat_layout)
    View satLayout;
    @BindView(R.id.pte_layout)
    View pteLayout;
    @BindView(R.id.ielts_layout)
    View ieltsLayout;
    @BindView(R.id.testtxt)
    TextView text;

    // current employment view
    @BindView(R.id.current_employer)
    TextView currentEmp;
    @BindView(R.id.fieldofactivity)
    TextView fieldOfActivity;
    @BindView(R.id.position)
    TextView position;
    @BindView(R.id.current_startdate)
    TextView current_start_date;
    @BindView(R.id.current_department)
    TextView current_dept;
    @BindView(R.id.current_empType)
    TextView current_emp_type;

    // previous emp exp
    @BindView(R.id.pre_employer)
    TextView pre_emp;
    @BindView(R.id.pre_job_location)
    TextView pre_location;
    @BindView(R.id.pre_jobTitle)
    TextView job_title;
    @BindView(R.id.pre_startdate)
    TextView pre_start_date;
    @BindView(R.id.pre_endDate)
    TextView pre_end_date;
    @BindView(R.id.pre_empType)
    TextView pre_emp_type;

    // Attachments view
    @BindView(R.id.photo)
    TextView photo;
    @BindView(R.id.passport_copy)
    TextView passportCopy;
    @BindView(R.id.citizenship_coopy)
    TextView citizenshipCopy;
    @BindView(R.id.school_cert_copy)
    TextView schoolCertCopy;
    @BindView(R.id.high_school_cert_copy)
    TextView highSchoolCertCopy;
    @BindView(R.id.undergrad_cert_copy)
    TextView undergradCertCopy;
    @BindView(R.id.grad_cert_copy)
    TextView gradCertCopy;
    @BindView(R.id.resume)
    TextView resumeCopy;
    @BindView(R.id.test_copy)
    TextView testCopy;

    // signature
    @BindView(R.id.signature)
    TextView signature;
    @BindView(R.id.signature_date)
    TextView signature_date;

    String title;
    @BindView(R.id.dismiss)
    Button dismissBtn;


    private void populateData() {
        final Bundle details = getIntent().getExtras();
        if (details != null) {
            String yearValue = details.getString("year");
            check(yearValue, year);
            check(details.getString("intake"), intake);
            check(details.getString("course"), course);
            check(details.getString("pcountry"), country);
            check(details.getString("firstname"), first_name);
            title = details.getString("title");
            String middleName = details.getString("middlename");
            if (middleName.equals("")) middleLayout.setVisibility(View.GONE);
            else middle_name.setText(middleName);
            check(details.getString("lastname"), last_name);
            check(details.getString("dateofbirth"), dob);
            check(details.getString("placeofbirth"), pob);
            check(details.getString("sex"), sex);
            check(details.getString("nationality"), nationality);
            check(details.getString("maritalstatus"), marital_status);
            check(details.getString("citizenshipno"), citizenship_no);

            String passportno = details.getString("passportno");
            check(passportno, passport);
            String dateofissue = details.getString("passdateofissue");
            check(dateofissue, pass_dateofissue);
            String dateofexpiry = details.getString("passdateofexpiry");
            check(dateofexpiry, pass_dateofexpiry);
            String placeofissue = details.getString("placeofissue");
            check(placeofissue, pass_placeofissue);
            String streetname = details.getString("streetname");
            check(streetname, street_name);
            check(details.getString("city"), city);
            check(details.getString("stateorprovince"), state);
            check(details.getString("country"), mailing_country);
            check(details.getString("phone"), phone);
            check(details.getString("mobile"), mobile);
            check(details.getString("email"), email);
            check(details.getString("schoolname"), schoolName);
            check(details.getString("schoolcity"), schoolCity);
            check(details.getString("schoolstate"), schoolState);
            check(details.getString("schoolcountry"), schoolCountry);
            check(details.getString("schoolqualification"), schoolQualification);
            check(details.getString("schoolgrad"), schoolGrade);
            check(details.getString("schoolcompleted"), schoolDateofCompete);
            check(details.getString("highschoolname"), highSchoolName);
            check(details.getString("highschoolcity"), highSchoolCity);
            check(details.getString("highschoolstate"), highSchoolState);
            check(details.getString("highschoolcountry"), highSchoolCountry);
            check(details.getString("highschoolqualification"), highSchoolQualification);
            check(details.getString("highschoolgrade"), highSchoolGrade);
            check(details.getString("highschoolcompleted"), highSchoolDateofCompete);
            check(details.getString("undergraduniversity"), university);
            check(details.getString("undergradcity"), undergraduateCity);
            check(details.getString("undergradstate"), undergraduateState);
            check(details.getString("undergradcountry"), undergraduateCountry);
            check(details.getString("undergraddegree"), undergraduateQualification);
            check(details.getString("undergradmajorsub"), undergraduateMajorSubject);
            check(details.getString("undergradmarks"), undergraduateGrade);
            check(details.getString("undergradcompleted"), undergraduateDateofCompete);
            check(details.getString("graduniversity"), graduateUniversity);
            check(details.getString("gradcity"), graduateCity);
            check(details.getString("gradstate"), graduateState);
            check(details.getString("gradcountry"), graduateCountry);
            check(details.getString("graduatedegree"), graduateQualification);
            check(details.getString("graduatemajorsub"), graduateMajorSubject);
            check(details.getString("graduatemarks"), graduateGrade);
            check(details.getString("gradcompleted"), graduateDateofCompete);

            String ielts_score = details.getString("ielts");
            String toefl_score = details.getString("toefl");
            String gmat_score = details.getString("gmat");
            String gre_score = details.getString("gre");
            String pte_score = details.getString("pte");
            String sat_score = details.getString("sat");

            if ((ielts_score.isEmpty()|| returnScore(ielts_score)) && (toefl_score.isEmpty() || returnScore(toefl_score))&& (gmat_score.isEmpty()||returnScore(gmat_score)) && (gre_score.isEmpty() || returnScore(gre_score))&& (pte_score.isEmpty()||returnScore(pte_score)) && (sat_score.isEmpty()||returnScore(sat_score))) {
                text.setVisibility(View.VISIBLE);
                text.setTextColor(getResources().getColor(R.color.red));
                text.setText("No tests.");
                ieltsLayout.setVisibility(View.GONE);
                toeflLayout.setVisibility(View.GONE);
                gmatLayout.setVisibility(View.GONE);
                greLayout.setVisibility(View.GONE);
                pteLayout.setVisibility(View.GONE);
                satLayout.setVisibility(View.GONE);
            } else {
                if (ielts_score.isEmpty() || ielts_score.equals("") || returnScore(ielts_score))
                    ieltsLayout.setVisibility(View.GONE);
                else ielts.setText(ielts_score);
                if (toefl_score.isEmpty() || toefl_score.equals("") || returnScore(toefl_score))
                    toeflLayout.setVisibility(View.GONE);
                else toefl.setText(toefl_score);
                if (gmat_score.isEmpty() || gmat_score.equals("") || returnScore(gmat_score))
                    gmatLayout.setVisibility(View.GONE);
                else gmat.setText(gmat_score);
                if (gre_score.isEmpty() || gre_score.equals("") || returnScore(gre_score))
                    greLayout.setVisibility(View.GONE);
                else gre.setText(gre_score);
                if (pte_score.isEmpty() || pte_score.equals("") || returnScore(pte_score))
                    pteLayout.setVisibility(View.GONE);
                else pte.setText(pte_score);
                if (sat_score.isEmpty() || sat_score.equals("") || returnScore(sat_score))
                    satLayout.setVisibility(View.GONE);
                else sat.setText(sat_score);
            }

            check(details.getString("currentemployer"), currentEmp);
            check(details.getString("currentfieldofactivity"), fieldOfActivity);
            check(details.getString("position"), position);
            check(details.getString("currentstartdate"), current_start_date);
            check(details.getString("currentdept"), current_dept);
            check(details.getString("currentemptype"), current_emp_type);
            check(details.getString("preemployer"), pre_emp);
            check(details.getString("prejoblocation"), pre_location);
            check(details.getString("prejobtitle"), job_title);
            check(details.getString("prestartdate"), pre_start_date);
            check(details.getString("preenddate"), pre_end_date);
            check(details.getString("preemptype"), pre_emp_type);

            photo.setPaintFlags(photo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            resumeCopy.setPaintFlags(resumeCopy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            citizenshipCopy.setPaintFlags(citizenshipCopy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            schoolCertCopy.setPaintFlags(schoolCertCopy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            highSchoolCertCopy.setPaintFlags(highSchoolCertCopy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            undergradCertCopy.setPaintFlags(undergradCertCopy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            gradCertCopy.setPaintFlags(gradCertCopy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            testCopy.setPaintFlags(testCopy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            passportCopy.setPaintFlags(passportCopy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            final String photograph = details.getString("photograph");
            final String resume = details.getString("resume");
            final String citizencopy = details.getString("citizenshipcopy");
            final String schoolcopy = details.getString("schoolcertificate");
            final String higschoolcopy = details.getString("highschoolcertificate");
            final String undergradcopy = details.getString("undergradcertificate");
            final String gradcopy = details.getString("graduatecertificate");
            final String testcopy = details.getString("testcopy");
            final String passportcopy = details.getString("passportcopy");

            if (citizencopy == null || citizencopy.isEmpty())
                citizenshipCopy.setVisibility(View.GONE);
            if (schoolcopy == null || schoolcopy.isEmpty()) schoolCertCopy.setVisibility(View.GONE);
            if (higschoolcopy == null || higschoolcopy.isEmpty())
                highSchoolCertCopy.setVisibility(View.GONE);
            if (undergradcopy == null || undergradcopy.isEmpty())
                undergradCertCopy.setVisibility(View.GONE);
            if (gradcopy == null || gradcopy.isEmpty()) gradCertCopy.setVisibility(View.GONE);
            if (testcopy == null || testcopy.isEmpty()) testCopy.setVisibility(View.GONE);
            if (passportcopy == null || passportcopy.isEmpty())
                passportCopy.setVisibility(View.GONE);

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photograph != null) {
                        Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                        callBrowser(photograph);
                    } else
                        Toast.makeText(AppliedUserDetailActivity.this, "No attachment found", Toast.LENGTH_SHORT).show();
                }
            });

            resumeCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (resume != null) {
                        Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                        callBrowser(resume);
                    } else
                        Toast.makeText(AppliedUserDetailActivity.this, "No attachment found", Toast.LENGTH_SHORT).show();
                }
            });
            citizenshipCopy.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                    callBrowser(citizencopy);
                }
            });
            schoolCertCopy.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                    callBrowser(schoolcopy);
                }
            });
            highSchoolCertCopy.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                    callBrowser(higschoolcopy);
                }
            });
            undergradCertCopy.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                    callBrowser(undergradcopy);
                }
            });
            gradCertCopy.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                    callBrowser(gradcopy);
                }
            });
            testCopy.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                    callBrowser(testcopy);
                }
            });
            passportCopy.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppliedUserDetailActivity.this, "Opening on browser.", Toast.LENGTH_SHORT).show();
                    callBrowser(passportcopy);
                }
            });

            signature.setText(details.getString("signatuername"));
            signature_date.setText(details.getString("signdate"));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applieduser_detail);
        ButterKnife.bind(this);
        populateData();
        toolbar.setTitle("Applied User Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void callBrowser(String url) {
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(launchBrowser);
    }

    private void check(String input, TextView view) {
        if (input == null || input.equals("") || input.equals("null")) {
            view.setTextColor(getResources().getColor(R.color.red));
            view.setText("User has not entered this value");
            view.setTextSize(10f);
        } else view.setText(input);
    }

    private boolean returnScore(String value) {
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
