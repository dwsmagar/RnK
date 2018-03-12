package com.susankya.schoolvalley.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.schoolvalley.Fragments.Appointment;
import com.susankya.schoolvalley.Fragments.Eligibility;
import com.susankya.schoolvalley.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EligibiltyAdapter extends RecyclerView.Adapter<EligibiltyAdapter.AppointmentRecyclerViewholer> {
    Context context;
    List<Eligibility> eligibilityList= new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Eligibility item);
    }

    public EligibiltyAdapter(Context context, List<Eligibility> eligibilities, OnItemClickListener listener) {
        this.context = context;
        eligibilityList = eligibilities;
        this.listener = listener;
    }

    @Override
    public AppointmentRecyclerViewholer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.eligibility_items, parent, false);
        return new AppointmentRecyclerViewholer(view);
    }

    @Override
    public void onBindViewHolder(AppointmentRecyclerViewholer holder, int position) {
        holder.fullName.setText(eligibilityList.get(position).getName());
        holder.email.setText(eligibilityList.get(position).getEmail());
        holder.createdDate.setText(eligibilityList.get(position).getCreated_date().split("T")[0]);
        holder.address.setText(eligibilityList.get(position).getAddress());
        holder.bind(eligibilityList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return eligibilityList.size();
    }

    public class AppointmentRecyclerViewholer extends RecyclerView.ViewHolder {
        @BindView(R.id.full_name)
        TextView fullName;
        @BindView(R.id.email)
        TextView email;
        @BindView(R.id.created_date)
        TextView createdDate;
        @BindView(R.id.address)
        TextView address;

        public AppointmentRecyclerViewholer(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Eligibility item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
