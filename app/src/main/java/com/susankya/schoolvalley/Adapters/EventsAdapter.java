package com.susankya.schoolvalley.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;
import com.susankya.schoolvalley.Activities.AddEventActivity;
import com.susankya.schoolvalley.Models.EventItem;
import com.susankya.schoolvalley.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {
    private List<EventItem> eventsList;
    private Context context;
    private final OnItemClickListener listener;
    private String[] splittedDate;

    public EventsAdapter(List<EventItem> eventsList, Context context, OnItemClickListener listener) {
        this.eventsList = eventsList;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(EventItem item);
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_item, null);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, final int position) {
        String unsplittedDate = formateDate(eventsList.get(position).getDate());
        try {
            splittedDate = unsplittedDate.split(" ");
        } catch (Exception e) {
        }
        holder.bind(eventsList.get(position), listener);
        holder.day.setText(splittedDate[2]);
        holder.month.setText(splittedDate[1]);
        holder.eventTitle.setText(eventsList.get(position).getName());
        holder.eventDay.setText(splittedDate[0]);
        try {
            holder.eventTime.setText(timeFormatter(eventsList.get(position).getTime()));
        } catch (Exception e) {
        }
        holder.eventLocation.setText(eventsList.get(position).getLocation());
        holder.organizer.setText(eventsList.get(position).getOrganizedBy());
        Picasso.with(context).load(eventsList.get(position).getPicture()).fit().error(R.drawable.warning).
                placeholder(R.drawable.newplaceholder)
                .into(holder.imageView);

        final View menu = holder.dot_menu;

        holder.dot_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, menu);
                popupMenu.getMenuInflater().inflate(R.menu.event_popmenu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit: {
                                Bundle detail = new Bundle();
                                detail.putString("name", eventsList.get(position).getName());
                                detail.putString("description", eventsList.get(position).getDescription());
                                detail.putString("price", String.valueOf(eventsList.get(position).getPrice()));
                                detail.putString("organized_by", eventsList.get(position).getOrganizedBy());
                                detail.putString("date", eventsList.get(position).getDate());
                                detail.putString("time", eventsList.get(position).getTime());
                                detail.putString("imageUrl", eventsList.get(position).getPicture());
                                detail.putString("location", eventsList.get(position).getLocation());
//                                detail.putString("created_date", item.getCreated_on());
                                Intent intent = new Intent(context, AddEventActivity.class);
                                intent.putExtras(detail);
                                context.startActivity(intent);
                            }
                            return true;
                            case R.id.delete: {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Delete this event.")
                                        .setMessage("Do you want to delete ?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                            return true;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.month)
        TextView month;
        @BindView(R.id.eventTitle)
        TextView eventTitle;
        @BindView(R.id.eventDay)
        TextView eventDay;
        @BindView(R.id.eventTime)
        TextView eventTime;
        @BindView(R.id.eventLocation)
        TextView eventLocation;
        @BindView(R.id.organizer)
        TextView organizer;
        @BindView(R.id.imgEvent)
        ImageView imageView;
        @BindView(R.id.dot_menu)
        IconTextView dot_menu;

        public EventsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final EventItem item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    private String formateDate(String dateStr) {
        Date MyDate = null;
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            MyDate = newDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newDateFormat.applyPattern("E MMM dd yyyy");
        String obtainedDate = null;
        try {
            obtainedDate = newDateFormat.format(MyDate);
        } catch (Exception e) {
        }
        return obtainedDate;
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

