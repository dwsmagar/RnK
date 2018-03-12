package com.susankya.schoolvalley;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectInstitutionDialog extends DialogFragment implements FragmentCodes {

    @BindView(R.id.search_inst)EditText searchET;
    @BindView(R.id.inst_list)ListView list;
    ArrayList<SearchItem> searchItems;
   SearchAdapter searchAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_select_institution,container,false);
        ButterKnife.bind(this,v);

        searchItems=StudentInActivity.searchItemArrayList;
        searchAdapter=new SearchAdapter(getActivity(),searchItems);
        list.setAdapter(searchAdapter);

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FirstSignUpFragment.chosen=position;
                        FirstSignUpFragment.selectedInst.setText(searchItems.get(position).name);
                        getDialog().dismiss();
                    }
                }
        );

        return v;
    }
    public class SearchAdapter extends BaseAdapter
    {
        LayoutInflater inflater;
        Context context;


        public SearchAdapter(Context context, ArrayList<SearchItem> myList) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return searchItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public Object getItem(int position) {
            return searchItems.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.search_item_view,null,false);
                holder=new ViewHolder();
                holder.name=(TextViewPlus)convertView.findViewById(R.id.school_name);
                holder.location=(TextViewPlus)convertView.findViewById(R.id.school_location);
                convertView.setTag(holder);
            }

           else
            {
                holder=(ViewHolder)convertView.getTag();
            }

            SearchItem ss=searchItems.get(position);
            holder.name.setText(ss.name);
            holder.location.setText(ss.location);
            return convertView;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    static class ViewHolder {
        private TextViewPlus name,location;
    }
}
