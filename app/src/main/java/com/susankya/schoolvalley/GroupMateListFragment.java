package com.susankya.schoolvalley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.susankya.schoolvalley.Adapters.VerticalSpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GroupMateListFragment extends android.support.v4.app.Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText searchEditText;
    private String filterString;
    private UserAdapter useradapter;
    private ArrayList<UserInfo> userINFO;
    private ArrayList<UserInfo> tempuserINFO;
    List<UserInfo> userInfos;
    @BindView(R.id.emtpyTextview)
    TextView emptyText;
    @BindView(R.id.icontText)
    IconTextView iconTextView;
    @BindView(R.id.user_list)
    RecyclerView recyclerView;
    @BindView(R.id.top_layout)
    View topLayout;
    @BindView(R.id.bottom)
    View bottomView;
    @BindView(R.id.progressBar)
    ProgressBar progress;
    @BindView(R.id.emptyTextLayout)
    View emptyView;

    public static GroupMateListFragment newInstance(String param1, String param2) {
        GroupMateListFragment fragment = new GroupMateListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GroupMateListFragment() {
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (NavDrawerActivity.isShowingSearchEditText()) {
                    NavDrawerActivity.hideSearch();
                } else {
                    ((NavDrawerActivity) getActivity()).showSearch();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Users");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("Users");
        userInfos = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.subscribed_user_list_recyclar_view_fragment, container, false);
        ButterKnife.bind(this, v);
        bottomView.setVisibility(View.GONE);
        topLayout.setVisibility(View.GONE);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        if (isConnectionAvailable()) {
            emptyView.setVisibility(View.GONE);
            userInfos = getUser(0);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            iconTextView.setText("{mdi-wifi-off}");
            emptyText.setText("OOps, Out of connection");
        }
        searchEditText = NavDrawerActivity.getSearchEditText();
        searchEditText.setHint("Search users");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterString = charSequence.toString();
                tempuserINFO = new ArrayList<UserInfo>();
                for (UserInfo ui : userINFO) {
                    if (ui.getFullName().toLowerCase().contains(filterString.toLowerCase()))
                        tempuserINFO.add(ui);
                }
                useradapter = new UserAdapter(getActivity(), R.layout.user_list_item_view, tempuserINFO);
                DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        linearLayoutManager.getOrientation());
                VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(8);
                recyclerView.addItemDecoration(verticalSpaceItemDecoration);
                //recyclerView.addItemDecoration(mDividerItemDecoration);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return v;
    }

    private List getUser(int escape) {
        userINFO = new ArrayList<>();
        String link = FragmentCodes.NEW_HOST + "Edit/blockUser.php";
        //Log.d("TAG", "onCreateView: "+link);
        new PhpConnect(link, "Loading user list...", getActivity(), 1, new String[]{FragmentCodes.CMDXXX,
                "view_users", "64", escape + ""}
                , new String[]{"cmdxxx", "action", "college_sn", "escape"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                //Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
                //Log.d("TAG", "onConnectListener: "+res);
                try {
                    JSONArray ja = new JSONArray(res);
                    //Log.d(TAG, "onConnectListener: "+ja.length());
                    if (ja.length() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyText.setVisibility(View.VISIBLE);
                        iconTextView.setVisibility(View.GONE);
                        emptyText.setText("No users have registered yet.");
                    } else {
                        getActivity().setTitle("Users(" + ja.length() + ")");
                        emptyView.setVisibility(View.GONE);
                        for (int i = 0; i < ja.length(); i++) {
                            userINFO.add(getuserInfo((JSONObject) ja.get(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d("TAG","ERROR "+e.toString());
                }
                useradapter = new UserAdapter(getActivity(), R.layout.applied_user_item, userINFO);
                recyclerView.setAdapter(useradapter);
            }
        });
        return userINFO;
    }/*
    [
  {
    "user_no": "87",
    "firstName": "Aditya",
    "lastName": "Shah",
    "userid": "developersanjog@gmail.com",
    "verified": "1",
    "institution": "Mero School\r\n",
    "level": "+2 level",
    "dbName": "Susankyatest",
    "gender": "m",
    "profile_picture_location": "IMGuploads/ProfilePics/adityaPrOpIc.jpg",
    "phone_number": "9819012345",
    "location": "Biratnagar, Nepal",
    "interest": "C++, Java, Android programming \nAnd \nAnything that excites the curious part of the brain.",
    "hobby": "programmeraditya@gmail.com",
    "thumbmailLocation": "IMGuploads/ThumbMail/adityaPrOpIc.jpg",
    "SeePostsFrom": "All",
    "college_sn": "0",
    "section": "",
    "roll": "0",
    "blocked": "0"
  },*/

    private UserInfo getuserInfo(JSONObject jo) {
        UserInfo ui = new UserInfo();
        try {
            ui.setUserNumber(jo.getInt("user_no"));
            ui.setFirstName(jo.getString("firstName"));
            ui.setLastName(jo.getString("lastName"));
            ui.setUserName(jo.getString("userid"));
            ui.setFullName(ui.getFirstName() + " " + ui.getLastName());

            ui.setVerified(jo.getInt("verified"));
            // ui.setInstitution(jo.getString("institution"));
            ui.setLevel(jo.getString("level"));
            ui.setRoll(jo.getString("roll"));
            ui.setDbName(jo.getString("dbName"));
            ui.setGender(jo.getString("gender"));
            ui.setPhoneNumber(jo.getString("phone_number"));
            ui.setLocation(jo.getString("location"));
            ui.setProfilepiclocation(jo.getString("thumbmailLocation"));
            ui.setInterest(jo.getString("interest"));
            ui.setHobby(jo.getString("hobby"));
            ui.setBlocked(jo.getInt("blocked"));
            ui.setCollegeSN(jo.getString("college_sn"));
            ui.setSection(jo.getString("section"));
        } catch (Exception e) {
            //Log.d("TAG", "getuserInfo: "+e.toString());
        }
        return ui;
    }

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        boolean isBlocked;
        String action;
        TextView name, status, userid;
        View view;
        IconTextView dotmenu;
        SearchItem searchItem;
        CheckBox checkBox;
        @BindView(R.id.ph_tv)
        TextViewPlus circleView;
        @BindView(R.id.email)
        TextViewPlus email;
        @BindView(R.id.full_name)
        TextView fullName;

        public UserHolder(Context c, View v) {
            super(v);
            ButterKnife.bind(this,v);
//            this.name = v.findViewById(R.id.user_name);
//            this.status =  v.findViewById(R.id.status);
//            this.userid =  v.findViewById(R.id.username);
//            this.dotmenu = v.findViewById(R.id.dot_menu);
//            this.checkBox = v.findViewById(R.id.checkbox);
            view = v;
            v.setOnClickListener(this);
        }

        public void bindNotice(SearchItem searchItem) {
            this.searchItem = searchItem;
            //this.name.setCustomFont(getActivity(), FragmentCodes.REGULAR);
            this.name.setText(searchItem.name);
            this.status.setText(searchItem.location);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
        private Context context;
        private int itemResource;
        private List<UserInfo> userInfos;


        public UserAdapter(Context context, int itemResource, List userinfos) {
            this.context = context;
            this.itemResource = itemResource;
            this.userInfos = userinfos;
        }

        // 2. Override the onCreateViewHolder method
        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 3. Inflate the view and return the new ViewHolder
            View view;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new UserHolder(this.context, view);

        }

        // 4. Override the onBindViewHolder method
        @Override
        public void onBindViewHolder(final UserHolder holder, final int position) {
            final UserInfo ui = userInfos.get(position);
           // holder.name.setText(ui.getFullName());
            //holder.userid.setText(ui.getUserName());
            holder.fullName.setText(ui.getFullName());
            //holder.userid.setVisibility(View.GONE);
//            holder.status.setVisibility(View.GONE);
//            holder.dotmenu.setVisibility(View.GONE);
//            holder.checkBox.setVisibility(View.GONE);
            holder.circleView.setVisibility(View.GONE);
            holder.email.setVisibility(View.GONE);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, UserToUserChatFragment.newInstance("" + ui.getUserNumber(), ui.getFirstName() + " " + ui.getLastName())).addToBackStack(null).commit();

                }
            });
        }

        @Override
        public int getItemCount() {
            return userInfos.size();
        }
    }

    private boolean isConnectionAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        else return false;

    }
}
