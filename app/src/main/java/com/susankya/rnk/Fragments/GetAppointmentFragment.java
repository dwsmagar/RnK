package com.susankya.rnk.Fragments;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.susankya.rnk.Activities.AppointmentDetailDialogFragment;
import com.susankya.rnk.Adapters.AppointmentAdapter;
import com.susankya.rnk.ImmortalApplication;
import com.susankya.rnk.Interfaces.Nimainterface;
import com.susankya.rnk.R;
import com.susankya.rnk.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAppointmentFragment extends Fragment {
    @BindView(R.id.list_recycler)
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
    SwipeRefreshLayout container;
    @BindView(R.id.empty_img)
    ImageView imageView;
    private DividerItemDecoration mDividerItemDecoration;
    private LinearLayoutManager layoutManager;
    private boolean first;

    public GetAppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_appointment, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Appointment Lists");
        initData();
        return view;
    }

    private void getAppointmentList() {
        if (first)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
        Nimainterface nimainterface = ImmortalApplication.getRetrofit().create(Nimainterface.class);
        nimainterface.getAppointments(65).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_reunion1));
//                        iconTextView.setText("{md-group}");
                        emptyText.setText("No appointments lists.");
                    } else {
                        emptyView.setVisibility(View.GONE);

                        AppointmentAdapter adapter = new AppointmentAdapter(getActivity(), response.body(), new AppointmentAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Appointment item) {
                                Bundle detailBundle = new Bundle();
                                detailBundle.putString("name", item.getFull_name());
                                detailBundle.putString("email", item.getEmail());
                                detailBundle.putString("address", item.getAddress());
                                detailBundle.putString("telephone", String.valueOf(item.getTelephone_no()));
                                detailBundle.putString("mobile", String.valueOf(item.getMobile_no()));
                                detailBundle.putString("appointment", item.getAppointment());
                                detailBundle.putString("purpose", item.getPurpose_of_visit());
                                detailBundle.putString("evidenceId", item.getEvidence_of_id());
                                detailBundle.putString("evidenceIdNo", item.getEvidence_of_id_number());
                                detailBundle.putString("date", item.getDate());
                                detailBundle.putString("time", item.getTime());
                                detailBundle.putString("createdDate", item.getCreated_date().split("T")[0]);
                                detailBundle.putString("message", item.getMessage());

                                FragmentManager f = (getActivity()).getSupportFragmentManager();
                                AppointmentDetailDialogFragment c = new AppointmentDetailDialogFragment();
                                c.setArguments(detailBundle);
                                c.show(f, "Dialog");
                            }
                        });
                        recyclerView.setAdapter(adapter);

                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to fetch the data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "Failure message: " + t);
            }
        });
    }

    private void initData() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        if (Utilities.isConnectionAvailable(getActivity())) {
            first = true;
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getAppointmentList();
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
//            iconTextView.setText("{mdi-wifi-off}");
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_plug));
            emptyText.setText("OOPS, out of connection");
        }

        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isConnectionAvailable(getActivity())) {
                    first = false;
                    getAppointmentList();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            emptyView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            container.setRefreshing(false);
                        }
                    }, 3000);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
//                    iconTextView.setText("{mdi-wifi-off}");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_plug));
                    emptyText.setText("OOPS, out of connection");
                    container.setRefreshing(false);
                }
            }
        });
    }
}
