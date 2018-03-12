package com.susankya.schoolvalley;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AboutSchoolFragment extends Fragment {

    @BindView(R.id.shop_about_lv)RecyclerView rv;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private String SN;


    AboutAdapter adapter;



    public AboutSchoolFragment() {
        // Required empty public constructor
    }


    public static AboutSchoolFragment newInstance(String param1, String param2) {
        AboutSchoolFragment fragment = new AboutSchoolFragment();
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
SN=getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_about_shop, container, false);
        ButterKnife.bind(this,v);
        adapter=new AboutAdapter(getActivity().getApplicationContext(), SchoolProfileActivity.aboutShopList);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        return  v;

    }

    public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder>
    {

        ArrayList<AboutInfo> list;
        Context c;



        public AboutAdapter(Context c, ArrayList<AboutInfo> list)
        {
            super();
            this.c=c;
            this.list=list;

        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.fieldtv)TextViewPlus field;
            @BindView(R.id.datatv)TextViewPlus data;
            @BindView(R.id.edit)ImageView edit;
            public ViewHolder(View v)
            {
                super(v);
                ButterKnife.bind(this,v);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

         final    AboutInfo aI=list.get(position);
            holder.field.setText(aI.getField());

            if(aI.getData().isEmpty() || aI.getData().equals("null"))
            holder.data.setText("-");
            else
            holder.data.setText(aI.getData());
            holder.data.setCustomFont(getContext(),FragmentCodes.REGULAR);
           /* if(!UtilitiesAdi.isProfileOfAdmin(getActivity(),SN))
                holder.edit.setVisibility(View.GONE);
            else
                holder.edit.setVisibility(View.VISIBLE);*/
            if((aI.getField().equals("Location")||aI.getField().equals("Description")||aI.getField().equals("Date of establishment")||aI.getField().equals("Website")||aI.getField().equals("Contact"))&&UtilitiesAdi.isProfileOfAdmin(getActivity(),SN))
            {
                holder.edit.setVisibility(View.VISIBLE);
            }else
            holder.edit.setVisibility(View.GONE);



            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog=new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.edit_profile_info_dialog);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final TextViewPlus title=(TextViewPlus)dialog.findViewById(R.id.title);
                    final Button ok=(Button)dialog.findViewById(R.id.ok);
                    final Button cancel=(Button)dialog.findViewById(R.id.cancel);
                    final EditText edit_info=(EditText)dialog.findViewById(R.id.edit_info);
                    edit_info.setText(aI.getData());
                    title.setText(aI.getField());
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(edit_info.getText().toString().isEmpty()){
                                Toast.makeText(getActivity(),"Fill the field",Toast.LENGTH_SHORT).show();
                                return;
                            }else if(edit_info.getText().toString().trim().equals(aI.getData())){
                                Toast.makeText(getActivity(),"No changes to update",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String link=FragmentCodes.MAIN_DATABASE+"update_profile.php";
                            //Log.d("TAG", "onClick: "+aI.getField()+ " "+aI.getData());
new PhpConnect(link,"Updating...",getActivity(),1,new String[]{FragmentCodes.CMDXXX,edit_info.getText().toString().trim(),UtilitiesAdi.giveMeSN(getActivity(),
        Utilities.getDatabaseName(getActivity())),aI.getField().toLowerCase()},new String[]{"cmdxxx","value","college_sn","parameter"}).setListener(new PhpConnect.ConnectOnClickListener() {
    @Override
    public void onConnectListener(String res) {
        if(res.equals("1")){
            Toast.makeText(getActivity(),"Updated",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            aI.setData(edit_info.getText().toString().trim());
            list.set(position,aI);
            holder.data.setText(edit_info.getText().toString().trim());
        }
        //Log.d("update", "onConnectListener: "+res);
    }
});
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.about_info_item,null);
            return new ViewHolder(view);
        }
    }

}


