package com.susankya.schoolvalley;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactRecyclerview extends RecyclerView.Adapter<ContactRecyclerview.ContactVH> {
    Context context;
    String[] contactList;

    public ContactRecyclerview(Context context, String[] contacts) {
        this.context = context;
        contactList = contacts;
    }

    public class ContactVH extends RecyclerView.ViewHolder {
        @BindView(R.id.contact_text)
        TextView contact;
        @BindView(R.id.contact_check)
        CheckBox contactCheck;

        public ContactVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    @Override
    public ContactVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ContactVH(view);
    }

    @Override
    public void onBindViewHolder(ContactVH holder, int position) {
        Toast.makeText(context, ""+contactList[position], Toast.LENGTH_SHORT).show();
        holder.contact.setText(contactList[position]);
    }

    @Override
    public int getItemCount() {
        return contactList.length;
    }
}
