package com.susankya.rnk.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.rnk.Fragments.Appointment;
import com.susankya.rnk.R;
import com.susankya.rnk.TextViewPlus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentRecyclerViewholer> {
    Context context;
    List<Appointment> appointmentList = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Appointment item);
    }

    public AppointmentAdapter(Context context, List<Appointment> appointments, OnItemClickListener listener) {
        this.context = context;
        appointmentList = appointments;
        this.listener = listener;
    }

    @Override
    public AppointmentRecyclerViewholer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_items, parent, false);
        return new AppointmentRecyclerViewholer(view);
    }

    @Override
    public void onBindViewHolder(AppointmentRecyclerViewholer holder, int position) {
        holder.textTitle.setText(appointmentList.get(position).getFull_name().substring(0,1).toUpperCase());
        holder.fullName.setText(appointmentList.get(position).getFull_name());
        holder.email.setText(appointmentList.get(position).getEmail());
        holder.createdDate.setText(appointmentList.get(position).getCreated_date().split("T")[0]);
        holder.date.setText(appointmentList.get(position).getDate());
        holder.time.setText(timeFormatter(appointmentList.get(position).getTime()));
        holder.bind(appointmentList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class AppointmentRecyclerViewholer extends RecyclerView.ViewHolder {
        @BindView(R.id.full_name)
        TextView fullName;
        @BindView(R.id.email)
        TextView email;
        @BindView(R.id.created_date)
        TextView createdDate;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.ph_tv)
        TextViewPlus textTitle;

        public AppointmentRecyclerViewholer(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Appointment item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
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
}
