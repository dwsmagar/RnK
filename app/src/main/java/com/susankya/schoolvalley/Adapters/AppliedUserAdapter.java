package com.susankya.schoolvalley.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.schoolvalley.Models.AppliedUser;
import com.susankya.schoolvalley.R;
import com.susankya.schoolvalley.TextViewPlus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppliedUserAdapter extends RecyclerView.Adapter<AppliedUserAdapter.AppliedUserRecyclerViewholder> {
    Context context;
    List<AppliedUser> userList = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AppliedUser item);
    }

    public AppliedUserAdapter(Context context, List<AppliedUser> users, OnItemClickListener listener) {
        this.context = context;
        userList = users;
        this.listener = listener;
    }

    @Override
    public AppliedUserRecyclerViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.applied_user_item, parent, false);
        return new AppliedUserRecyclerViewholder(view);
    }

    @Override
    public void onBindViewHolder(AppliedUserRecyclerViewholder holder, int position) {
       // holder.textTitle.setText(userList.get(position).getFull_name().substring(0,1).toUpperCase());
        if (userList.get(position).getFirstName().equals("") ){
            holder.fullName.setText("User has not entered the name");
            holder.fullName.setTextColor(context.getResources().getColor(R.color.grey));
        }else holder.fullName.setText(userList.get(position).getFirstName()+" "+userList.get(position).getMiddleName()+" "+userList.get(position).getLastName());

       if (userList.get(position).getEmail().equals("")){
           holder.email.setText("User has not entered the email");
           holder.email.setTextColor(context.getResources().getColor(R.color.grey));
       }else holder.email.setText(userList.get(position).getEmail());
        holder.bind(userList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class AppliedUserRecyclerViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.full_name)
        TextView fullName;
        @BindView(R.id.email)
        TextViewPlus email;

        public AppliedUserRecyclerViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final AppliedUser item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
