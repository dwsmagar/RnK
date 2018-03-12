package com.susankya.schoolvalley.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.susankya.schoolvalley.Activities.AddEventActivity;
import com.susankya.schoolvalley.Activities.EventDescActivity;
import com.susankya.schoolvalley.Adapters.EventsAdapter;
import com.susankya.schoolvalley.Adapters.VerticalSpaceItemDecoration;
import com.susankya.schoolvalley.ImmortalApplication;
import com.susankya.schoolvalley.Models.EventItem;
import com.susankya.schoolvalley.R;
import com.susankya.schoolvalley.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {
    private LinearLayoutManager layoutManager;
    private EventsAdapter adapter;
    @BindView(R.id.eventsRV)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipetoRefresh;
    @BindView(R.id.floating_add_button)
    FloatingActionButton addBtn;
    @BindView(R.id.emtpyTextview)
    TextView emptyText;
    @BindView(R.id.emptyTextLayout)
    View emptyView;
    @BindView(R.id.iconTextView)
    IconTextView iconTextView;
    boolean first;
    public boolean add = false;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Events");
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void getEventsList(boolean first) {
        if (first)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
        Nimainterface nimainterface = ImmortalApplication.getRetrofit().create(Nimainterface.class);
        nimainterface.getEvents().enqueue(new Callback<List<EventItem>>() {
            @Override
            public void onResponse(Call<List<EventItem>> call, Response<List<EventItem>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        emptyText.setText("No events. Click plus button to add events.");
                    } else {
                        emptyView.setVisibility(View.GONE);
                        adapter = new EventsAdapter(response.body(), getActivity(), new EventsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(EventItem item) {
                                Bundle detail = new Bundle();
                                detail.putString("name", item.getName());
                                detail.putString("description", item.getDescription());
                                detail.putString("price", item.getPrice().toString());
                                detail.putString("organized_by", item.getOrganizedBy());
                                detail.putString("date", item.getDate());
                                detail.putString("time", item.getTime());
                                detail.putString("imageUrl", item.getPicture());
                                detail.putString("location", item.getLocation());
                                detail.putString("created_date", item.getCreatedOn());
                                // calling intent
                                Intent intent = new Intent(getActivity(), EventDescActivity.class);
                                intent.putExtras(detail);
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getActivity(), "Couldn't load the data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EventItem>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "Failure message " + t);
            }
        });
    }

    private void initData() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(12));
        recyclerView.setHasFixedSize(true);

        if (Utilities.isConnectionAvailable(getActivity())) {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getEventsList(true);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            iconTextView.setText("{mdi-wifi-off}");
            emptyText.setText("OOPS, out of connection");
        }

        swipetoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isConnectionAvailable(getActivity())) {
                    first = false;
                    getEventsList(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            emptyView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            swipetoRefresh.setRefreshing(false);
                        }
                    }, 3000);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    iconTextView.setText("{mdi-wifi-off}");
                    emptyText.setText("OOPS, out of connection");
                    swipetoRefresh.setRefreshing(false);
                }
            }
        });

        if (Utilities.isAdmin(getActivity())) {
            addBtn.setVisibility(View.VISIBLE);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddEventActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        if (add)
//            progressBar.setVisibility(View.GONE);
            getEventsList(false);
        super.onResume();
    }
}
