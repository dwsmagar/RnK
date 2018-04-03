package com.susankya.rnk.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.susankya.rnk.Activities.AppliedUserDetailActivity;
import com.susankya.rnk.Adapters.AppliedUserAdapter;
import com.susankya.rnk.Adapters.VerticalSpaceItemDecoration;
import com.susankya.rnk.ImmortalApplication;
import com.susankya.rnk.Interfaces.Nimainterface;
import com.susankya.rnk.Models.AppliedUser;
import com.susankya.rnk.R;
import com.susankya.rnk.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAppliedUsersFragment extends Fragment {
    @BindView(R.id.applied_users_rv)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.emtpyTextview)
    TextView emptyText;
    @BindView(R.id.emptyTextLayout)
    View emptyView;
    @BindView(R.id.icon)
    IconTextView iconTextView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private AppliedUserAdapter adapter;
    private DividerItemDecoration mDividerItemDecoration;
    private VerticalSpaceItemDecoration itemDecoration;
    private LinearLayoutManager layoutManager;
    private boolean first;

    public GetAppliedUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_applied_users, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Applied User Lists");
        init();
        return view;
    }

    private void init() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        if (Utilities.isConnectionAvailable(getActivity())) {
            first = true;
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getUsers();
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            iconTextView.setText("{mdi-wifi-off}");
            emptyText.setText("OOPS, out of connection");
        }

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isConnectionAvailable(getActivity())) {
                    first = false;
                    getUsers();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            emptyView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            swipeContainer.setRefreshing(false);
                        }
                    }, 3000);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    iconTextView.setText("{mdi-wifi-off}");
                    emptyText.setText("OOPS, out of connection");
                    swipeContainer.setRefreshing(false);
                }
            }
        });

    }

    private Bundle saveUsersDetail(AppliedUser user) {
        Bundle bundle = new Bundle();
        bundle.putString("year", user.getYear());
        bundle.putString("intake", user.getIntake());
        bundle.putString("course", user.getCourse());
        bundle.putString("pcountry", user.getPermanentCountry());
        bundle.putString("title", user.getTitle());
        bundle.putString("firstname", user.getFirstName());
        bundle.putString("middlename", user.getMiddleName());
        bundle.putString("lastname", user.getLastName());
        bundle.putString("dateofbirth", user.getDateOfBirth());
        bundle.putString("placeofbirth", user.getPlaceOfBirth());
        bundle.putString("sex", user.getSex());
        bundle.putString("nationality", user.getNationality());
        bundle.putString("maritalstatus", user.getMaritalStatus());
        bundle.putString("citizenshipno", user.getCitizenshipNumber());
        bundle.putString("passportno", user.getPassportNumber());
        bundle.putString("passdateofissue", user.dateOfIssue);
        bundle.putString("passdateofexpiry", user.getDateOfExpiry());
        bundle.putString("placeofissue", user.getPlaceOfIssue());
        bundle.putString("streetname", user.getStreetNameNumber());
        bundle.putString("city", user.getCity());
        bundle.putString("stateorprovince", user.getState());
        bundle.putString("country", user.getCountry());
        bundle.putString("phone", user.getPhone());
        bundle.putString("mobile", user.getMobile());
        bundle.putString("email", user.getEmail());
        bundle.putString("schoolname", user.getSchoolName());
        bundle.putString("schoolcity", user.getSchoolCity());
        bundle.putString("schoolstate", user.getSchoolState());
        bundle.putString("schoolcountry", user.getSchoolCountry());
        bundle.putString("schoolqualification", user.getSchoolQualificationObtained());
        bundle.putString("schoolgrad", user.getSchoolMarksObtained());
        bundle.putString("schoolcompleted", user.getSchoolDateOfCompletion());
        bundle.putString("highschoolname", user.getHighSchoolName());
        bundle.putString("highschoolcity", user.getHighSchoolCity());
        bundle.putString("highschoolstate", user.getHighSchoolState());
        bundle.putString("highschoolcountry", user.getHighSchoolCountry());
        bundle.putString("highschoolqualification", user.getHighSchoolQualificationObtained());
        bundle.putString("highschoolgrade", user.getHighSchoolMarksObtained());
        bundle.putString("highschoolcompleted", user.getHighSchoolDateOfCompletion());
        bundle.putString("undergraduniversity", user.getUndergradUniversity());
        bundle.putString("undergradcity", user.getUndergradCity());
        bundle.putString("undergradstate", user.getUndergradCity());
        bundle.putString("undergradcountry", user.getUndergradCountry());
        bundle.putString("undergraddegree", user.getUndergradDegreeObtained());
        bundle.putString("undergradmajorsub", user.getUndergradMajorSubject());
        bundle.putString("undergradmarks", user.getUndergradMarksObtained());
        bundle.putString("undergradcompleted", user.getUndergradDateOfCompletion());
        bundle.putString("graduniversity", user.graduateUniversity);
        bundle.putString("gradcity", user.graduateCity);
        bundle.putString("gradstate", user.graduateState);
        bundle.putString("gradcountry", user.graduateCountry);
        bundle.putString("graduatedegree", user.getGraduateDegreeObtained());
        bundle.putString("graduatemajorsub", user.getGraduateMajorSubject());
        bundle.putString("graduatemarks", user.getGraduateMarksObtained());
        bundle.putString("gradcompleted", user.getGraduateDateOfCompletion());
        bundle.putString("ielts", user.getIelts());
        bundle.putString("toefl", user.getToefl());
        bundle.putString("gmat", user.getGmat());
        bundle.putString("gre", user.getGre());
        bundle.putString("pte", user.getPte());
        bundle.putString("sat", user.getSat());
        bundle.putString("currentemployer", user.getEmploymentCurrentEmployer());
        bundle.putString("currentfieldofactivity", user.getFieldOfActivity());
        bundle.putString("position", user.getPosition());
        bundle.putString("currentstartdate", user.getStartDate());
        bundle.putString("currentdept", user.getDepartment());
        bundle.putString("currentemptype", user.getEmploymentType());
        bundle.putString("preemployer", user.getPreviousEmployer());
        bundle.putString("prejoblocation", user.getPreviousLocation());
        bundle.putString("prejobtitle", user.getPreviousJobTitle());
        bundle.putString("prestartdate", user.getPreviousStartDate());
        bundle.putString("preenddate", user.getPreviousEndDate());
        bundle.putString("preemptype", user.getPreviousEmploymentType());
        bundle.putString("photograph", user.getPhotograph());
        bundle.putString("resume", user.getResume());
        bundle.putString("passportcopy", user.getPassportCopy());
        bundle.putString("citizenshipcopy", user.getCitizenshipCopy());
        bundle.putString("schoolcertificate", user.getSchoolEducationCertificate());
        bundle.putString("highschoolcertificate",user.getHighSchoolCertificate());
        bundle.putString("undergradcertificate", user.getUndergradCertificate());
        bundle.putString("graduatecertificate", user.getGraduateCertificate());
        bundle.putString("testcopy", user.getTest());
        bundle.putString("signatuername", user.getSignatureName());
        bundle.putString("signdate", user.getDateOfSignature());
        return bundle;
    }

    private void getUsers() {
        if (first)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
        Nimainterface nimainterface = ImmortalApplication.getRetrofit().create(Nimainterface.class);
        nimainterface.getAppliedUsers().enqueue(new Callback<List<AppliedUser>>() {
            @Override
            public void onResponse(Call<List<AppliedUser>> call, Response<List<AppliedUser>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    adapter = new AppliedUserAdapter(getActivity(), response.body(), new AppliedUserAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(AppliedUser item) {
                            Intent intent = new Intent(getActivity(), AppliedUserDetailActivity.class);
                            intent.putExtras(saveUsersDetail(item));
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "Failed to load the data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AppliedUser>> call, Throwable t) {
                Log.d("failure", "failure" + t.toString());
            }
        });
    }
}
