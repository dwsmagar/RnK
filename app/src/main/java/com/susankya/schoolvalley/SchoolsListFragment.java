package com.susankya.schoolvalley;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SchoolsListFragment extends Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    SchoolAdapter schoolAdapter;
    public ArrayList<SearchItem> schools=new ArrayList<>();
    @BindView(R.id.school_list_rv)RecyclerView recyclerView;

    public SchoolsListFragment() {
        // Required empty public constructor
    }


    public static SchoolsListFragment newInstance(String param1, String param2) {
        SchoolsListFragment fragment = new SchoolsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_schools_list, container, false);
        ButterKnife.bind(this,v);
        schoolAdapter=new SchoolAdapter(getContext(),R.layout.search_item_view);
        recyclerView.setAdapter(schoolAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        populateList();
        return v;
    }

    void populateList()
    {
        for (int i=0;i<20;i++)
        {
            SearchItem s=new SearchItem();
            s.name="Testing boy School";
            s.location="Imadole, Lalitpur";
            schools.add(s);
        }
        schoolAdapter.notifyDataSetChanged();
    }
    private class SchoolHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextViewPlus name,location;
        SearchItem searchItem;

        public SchoolHolder(Context c,View v){
            super(v);
            this.name=(TextViewPlus)v.findViewById(R.id.school_name);
            this.location=(TextViewPlus)v.findViewById(R.id.school_location);

            v.setOnClickListener(this);


        }

        public void bindNotice(SearchItem searchItem)
        {
                this.searchItem=searchItem;
                this.name.setCustomFont(getActivity(),REGULAR);
                this.name.setText(searchItem.name);
                this.location.setText(searchItem.location);

        }

        @Override
        public void onClick(View v) {
            Intent i=new Intent(getActivity(),SchoolProfileActivity.class);
            Bundle b=new Bundle();
            b.putString("name",schools.get(getAdapterPosition()).name);
            b.putString("location",schools.get(getAdapterPosition()).location);
            b.putString("SN",schools.get(getAdapterPosition()).collegeSN+"");
            b.putString("url",schools.get(getAdapterPosition()).URL);
            i.putExtras(b);
            startActivity(i);

        }
    }

    public class SchoolAdapter extends RecyclerView.Adapter<SchoolHolder> {
        private Context context;
        private int itemResource;



        public SchoolAdapter(Context context, int itemResource) {
            this.context = context;
            this.itemResource = itemResource;
        }

        // 2. Override the onCreateViewHolder method
        @Override
        public SchoolHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 3. Inflate the view and return the new ViewHolder
            View view;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new SchoolHolder(this.context,view);

        }

        // 4. Override the onBindViewHolder method
        @Override
        public void onBindViewHolder(SchoolHolder holder, int position) {


            SearchItem s=schools.get(position);
            holder.bindNotice(s);
        }

        @Override
        public int getItemCount() {

            return schools.size();
        }


    }

}
