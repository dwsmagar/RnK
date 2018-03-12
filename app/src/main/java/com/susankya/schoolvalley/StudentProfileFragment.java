package com.susankya.schoolvalley;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StudentProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_NAME = "name";
    public static final String TAG_FOR_USER_DETAIL_JSON = "tagForuserJson";
    @BindView(R.id.student_detail_rv)
    RecyclerView recyclerView;
    @BindView(R.id.name)
    TextViewPlus name;
    @BindView(R.id.textForFont)
    TextViewPlus textForFont;
    private String jsonData;
    private String[] titles = new String[]{"Section", "Class", "Roll no", "Gender", "Age"};
    private String[] values = new String[]{"Section", "Class", "Roll no", "Gender", "Age"};
    private String nameOfStudent = "Name";
    private ArrayList<AboutInfo> arraylist;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    public static StudentProfileFragment newInstance(String name, String[] param1, String[] param2) {
        StudentProfileFragment fragment = new StudentProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putStringArray(ARG_PARAM1, param1);
        args.putStringArray(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("User Profile");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titles = getArguments().getStringArray(ARG_PARAM1);
            values = getArguments().getStringArray(ARG_PARAM2);
            nameOfStudent = getArguments().getString(ARG_NAME);
        }
        getActivity().setTitle("User Profile");
        jsonData = getActivity().getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE).getString(TAG_FOR_USER_DETAIL_JSON, null);
    }

    void hideView() {
        recyclerView.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
    }

    void ShowView() {
        recyclerView.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_profile, container, false);
        ButterKnife.bind(this, v);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        name.setText(nameOfStudent);
        arraylist = new ArrayList<>();
        if (jsonData != null)
            populateListitem();
        AboutAdapter aboutAdapter = new AboutAdapter(getActivity(), arraylist);
        recyclerView.setAdapter(aboutAdapter);
        return v;
    }

    void populateListitem() {
        String placeholdersAll[] = new String[]{"user_no", "firstName", "lastName", "userid", "verified", "level", "dbName", "gender", "profile_picture_location", "phone_number", "location", "interest", "hobby", "thumbmailLocation", "SeePostsFrom", "college_sn", "section", "semester", "faculty", "class", "roll", "blocked", "college_name", "college_location", "cover_pic"};
        String placeholdersFiltered[] = new String[]{"username", "location", "phone_number"};
        String placeholterNameToDisplay[] = new String[]{"Email Address:", "Location:", "Phone Number:"};
        try {
            String nameText = UtilitiesAdi.getString(getActivity(), "firstName") + " " + UtilitiesAdi.getString(getActivity(), "lastName");
            name.setText(nameText);
            textForFont.setText(nameText.substring(0, 1).toUpperCase());
            for (int i = 0; i < placeholdersFiltered.length; i++) {
                String value = UtilitiesAdi.getString(getActivity(), placeholdersFiltered[i]);
                AboutInfo ai = new AboutInfo(placeholterNameToDisplay[i], value);
                ai.setColumn_name(placeholdersFiltered[i]);
                arraylist.add(ai);
            }
        } catch (Exception e) {
            //Log.d("TAG", "populateListitem: "+e.toString());
        }
    }

    public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder> {
        ArrayList<AboutInfo> list;
        Context c;

        public AboutAdapter(Context c, ArrayList<AboutInfo> list) {
            super();
            this.c = c;
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.fieldtv)
            TextView field;
            @BindView(R.id.datatv)
            TextView data;
            @BindView(R.id.editBtn)
            TextView edit;

            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final AboutInfo aI = list.get(position);
            holder.field.setText(aI.getField());

            if (aI.getData().isEmpty() || aI.getData().equals("null"))
                holder.data.setText("-");
            else
                holder.data.setText(aI.getData());
           /* if(!UtilitiesAdi.isProfileOfAdmin(getActivity(),SN))
                holder.edit.setVisibility(View.GONE);
            else
                holder.edit.setVisibility(View.VISIBLE);*/
            if ((aI.getField().equals("Phone Number:") || aI.getField().equals("Location:") || aI.getField().equals("Semester")
                    || aI.getField().equals("Faculty") || aI.getField().equals("Class") || aI.getField().equals("Roll no.")) && !Utilities.isAdmin(getActivity())) {
                holder.edit.setVisibility(View.VISIBLE);
            } else
                holder.edit.setVisibility(View.GONE);

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile_info_dialog, null);
                    final EditText edit_info = (EditText) view.findViewById(R.id.edit_info);
                    edit_info.setText(aI.getData());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(view)
                    .setTitle(aI.getField())
                    .setPositiveButton("Update", null)
                    .setNegativeButton("Cancel", null);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (edit_info.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), "Fill the field", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edit_info.getText().toString().trim().equals(aI.getData())) {
                                Toast.makeText(getActivity(), "No changes to update", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String link = FragmentCodes.MAIN_DATABASE + "users/editUser.php";
                            //Log.d("TAG", "onClick: "+aI.getField()+ " "+aI.getData());
                            new PhpConnect(link, "Updating...", getActivity(), 1, new String[]{FragmentCodes.CMDXXX, edit_info.getText().toString().trim(), Utilities.getSN(getActivity())
                                    , aI.getColumn_name()}, new String[]{"cmdxxx", "value", "user_no", "column_name"}).setListener(new PhpConnect.ConnectOnClickListener() {
                                @Override
                                public void onConnectListener(String res) {
                                    Log.d("response","response"+res);
                                    if (res.equals("successful")) {
                                        UtilitiesAdi.setString(getActivity(), aI.getColumn_name(), edit_info.getText().toString());
                                        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        aI.setData(edit_info.getText().toString().trim());
                                        list.set(position, aI);
                                        holder.data.setText(edit_info.getText().toString().trim());
                                    }
                                    Log.d("update", "onConnectListener: "+res);
                                }
                            });
                        }
                    });
                }
            });
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.about_info_item, null);
            return new AboutAdapter.ViewHolder(view);
        }
    }
}
