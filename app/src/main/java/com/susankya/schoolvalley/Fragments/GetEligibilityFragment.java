package com.susankya.schoolvalley.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.susankya.schoolvalley.Activities.EligibilityDetailDialogFragment;
import com.susankya.schoolvalley.Adapters.EligibiltyAdapter;
import com.susankya.schoolvalley.ImmortalApplication;
import com.susankya.schoolvalley.R;
import com.susankya.schoolvalley.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetEligibilityFragment extends Fragment {
    @BindView(R.id.list_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.emtpyTextview)
    TextView emptyText;
    @BindView(R.id.emptyTextLayout)
    View emptyView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.icontText)
    IconTextView iconTextView;
    private boolean first;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_eligibility, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Eligibility Lists");
        initData();
        return view;
    }

    private void getElibillityList() {
        if (first)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
        Nimainterface nimainterface = ImmortalApplication.getRetrofit().create(Nimainterface.class);
        nimainterface.getEligibilityList().enqueue(new Callback<List<Eligibility>>() {
            @Override
            public void onResponse(Call<List<Eligibility>> call, Response<List<Eligibility>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        emptyText.setText("No eligibilty lists found.");
                    } else {
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setAdapter(new EligibiltyAdapter(getActivity(), response.body(), new EligibiltyAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Eligibility item) {
                                Bundle detailBundle = new Bundle();
                                detailBundle.putString("name", item.getName());
                                detailBundle.putString("email", item.getEmail());
                                detailBundle.putString("address", item.getAddress());
                                detailBundle.putString("mobile", String.valueOf(item.getPhone()));
                                detailBundle.putString("qualification", item.getQualification());
                                detailBundle.putString("ielts", item.getIelts());
                                detailBundle.putString("toefl", item.getToefl());
                                detailBundle.putString("gmat", item.getGmat());
                                detailBundle.putString("sat", item.getSat());
                                detailBundle.putString("gre", item.getGre());
                                detailBundle.putString("pte", item.getPte());
                                detailBundle.putString("country", item.getPriority_country());
                                detailBundle.putString("createdDate", item.getCreated_date().split("T")[0]);
                                detailBundle.putString("message", item.remarks);
                                FragmentManager f = (getActivity()).getSupportFragmentManager();
                                EligibilityDetailDialogFragment c = new EligibilityDetailDialogFragment();
                                c.setArguments(detailBundle);
                                c.show(f, "Dialog");
                            }
                        }));
                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to fetch the data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Eligibility>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "Failure message: " + t);
            }
        });
    }

    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        if (Utilities.isConnectionAvailable(getActivity())) {
            first = true;
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getElibillityList();
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
                    getElibillityList();
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
}
