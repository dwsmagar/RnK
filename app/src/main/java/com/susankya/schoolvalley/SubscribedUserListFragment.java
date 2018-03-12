package com.susankya.schoolvalley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.susankya.schoolvalley.Fragments.Nimainterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribedUserListFragment extends android.support.v4.app.Fragment {
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
    @BindView(R.id.icontText)
    IconTextView icon;
    @BindView(R.id.emptyTextLayout)
    View emptyView;
    @BindView(R.id.emtpyTextview)
    TextView noConnection;
    @BindView(R.id.user_list)
    RecyclerView recyclerView;
    @BindView(R.id.sendbtn)
    IconTextView send;
    @BindView(R.id.sms_editTxt)
    EditText sms;
    @BindView(R.id.select_all)
    Button selectAll;
    @BindView(R.id.bottom)
    View bottomView;
    @BindView(R.id.user_count)
    TextView count;
    @BindView(R.id.top_layout)
    View topLayout;
    @BindView(R.id.progressBar)
    ProgressBar progress;
    private String token = "UuDqRQDLkAHXNRrC0S6v";
    List<String> numberList = new ArrayList();
    private DividerItemDecoration mDividerItemDecoration;
    private int checked = 0;

    public static SubscribedUserListFragment newInstance(String param1, String param2) {
        SubscribedUserListFragment fragment = new SubscribedUserListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SubscribedUserListFragment() {
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
        getActivity().setTitle("Send SMS");
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userInfos = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.subscribed_user_list_recyclar_view_fragment, container, false);
        ButterKnife.bind(this, v);
        getActivity().setTitle("Send SMS");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        if (isConnectionAvailable()) {
            bottomView.setVisibility(View.VISIBLE);
            selectAll.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            userInfos = getUser(0);
        } else {
            progress.setVisibility(View.GONE);
            topLayout.setVisibility(View.GONE);
            bottomView.setVisibility(View.GONE);
            selectAll.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            icon.setText("{mdi-wifi-off}");
            noConnection.setText("OOPs, Out of Connection");
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
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isConnectionAvailable(getActivity())){
                final String smsStr = sms.getText().toString().trim();
                if (smsStr.isEmpty() || smsStr.length() == 0) {
                    Toast.makeText(getActivity(), "Type the message", Toast.LENGTH_SHORT).show();
                } else {
                    if (numberList.size() > 0) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Confirm your action")
                                .setMessage("Message will only be send to the users having phone numbers. \n" + numberList.size() +
                                        " Credits will be used from your account. \nClick OK to send.")
                                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setMessage("Sending");
                                        progressDialog.setCancelable(false);
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();
                                        for (int i = 0; i < numberList.size(); i++) {
                                            postSms(token, "InfoSMS", numberList.get(i), smsStr);
                                        }
                                        progressDialog.dismiss();
                                        sms.setText("");
                                        useradapter = new UserAdapter(getActivity(), R.layout.user_list_item_view, saveInfo(false));
                                        recyclerView.setAdapter(useradapter);
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    } else {
                        Toast.makeText(getActivity(), "Select atleast one user to send the SMS. Message will only be send to the users having phone numbers.", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("No internet connection, please try again later.")
                        .setPositiveButton("OK", null)
                        .show();

                }
            }
        });
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked == 0) {
                    checked = 1;
                    selectAll.setText("Deselect All");
                    for (int i = 0; i < userINFO.size(); i++) {
                        if (!userINFO.get(i).getPhoneNumber().equals("0"))
                            numberList.add(userINFO.get(i).getPhoneNumber());
                    }
                    useradapter = new UserAdapter(getActivity(), R.layout.user_list_item_view, saveInfo(true));
                } else {
                    checked = 0;
                    selectAll.setText("Select All");
                    numberList.clear();
                    useradapter = new UserAdapter(getActivity(), R.layout.user_list_item_view, saveInfo(false));
                }
                recyclerView.setAdapter(useradapter);
            }
        });
        return v;
    }

    private List getUser(int escape) {
        userINFO = new ArrayList<>();
        String link = FragmentCodes.NEW_HOST + "Edit/blockUser.php";
        new PhpConnect(link, "Loading user list...", getActivity(), 1, new String[]{FragmentCodes.CMDXXX,
                "view_users", "64", escape + ""}
                , new String[]{"cmdxxx", "action", "college_sn", "escape"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                Log.d("TAG", "onConnectListener: " + res);
                try {
                    JSONArray ja = new JSONArray(res);
                    if (ja.length() == 0) {
                        noConnection.setVisibility(View.VISIBLE);
                        noConnection.setText("No users have registered yet.");
                    } else {
                        noConnection.setVisibility(View.GONE);
                        count.setText("Users / " + ja.length());
                        for (int i = 0; i < ja.length(); i++) {
                            userINFO.add(getuserInfo((JSONObject) ja.get(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d("TAG","ERROR "+e.toString());
                }

                useradapter = new UserAdapter(getActivity(), R.layout.user_list_item_view, saveInfo(false));
                recyclerView.setAdapter(useradapter);
            }
        });
        return userINFO;
    }

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

    private ArrayList<UserInfo> saveInfo(boolean isSelected) {
        ArrayList<UserInfo> list = new ArrayList<>();
        UserInfo info;
        for (int i = 0; i < userINFO.size(); i++) {
            info = new UserInfo();
            info.setSelected(isSelected);
            info.setUserNumber(userINFO.get(i).getUserNumber());
            info.setFirstName(userINFO.get(i).getFirstName());
            info.setLastName(userINFO.get(i).getLastName());
            info.setUserName(userINFO.get(i).getUserName());
            info.setVerified(userINFO.get(i).getVerified());
            info.setLevel(userINFO.get(i).getLevel());
            info.setRoll(userINFO.get(i).getRoll());
            info.setDbName(userINFO.get(i).getDbName());
            info.setGender(userINFO.get(i).getGender());
            info.setPhoneNumber(userINFO.get(i).getPhoneNumber());
            info.setLocation(userINFO.get(i).getLocation());
            info.setProfilepiclocation(userINFO.get(i).getProfilepiclocation());
            info.setInterest(userINFO.get(i).getInterest());
            info.setHobby(userINFO.get(i).getHobby());
            info.setBlocked(userINFO.get(i).getBlocked());
            info.setCollegeSN(userINFO.get(i).getCollegeSN());
            info.setSection(userINFO.get(i).getSection());
            list.add(info);
        }
        return list;
    }

    private class UserHolder extends RecyclerView.ViewHolder {
        Context context;
        boolean isBlocked;
        String action;
        TextView name, status, userid;
        View view;
        IconTextView dotmenu;
        SearchItem searchItem;
        CheckBox checkBox;

        public UserHolder(final Context c, View v) {
            super(v);
            this.name = v.findViewById(R.id.user_name);
            this.status = v.findViewById(R.id.status);
            this.userid = v.findViewById(R.id.username);
            this.dotmenu = v.findViewById(R.id.dot_menu);
            checkBox = v.findViewById(R.id.checkbox);
            view = v;
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
        private Context context;
        private int itemResource;
        private List<UserInfo> userInfos;

        public UserAdapter(Context context, int itemResource, List<UserInfo> userinfos) {
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
            holder.name.setText(ui.getFirstName() + " " + ui.getLastName());
            holder.userid.setText(ui.getUserName());
            holder.isBlocked = (userInfos.get(position).getBlocked() == 0) ? false : true;
            holder.checkBox.setChecked(ui.isSelected());
            holder.checkBox.setTag(R.string.num, ui.getPhoneNumber());
            holder.checkBox.setTag(R.string.pos, position);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = (String) holder.checkBox.getTag(R.string.num);
                    Integer pos = (Integer) holder.checkBox.getTag(R.string.pos);

                    if (holder.checkBox.isChecked()) {
                        if (!number.equals("0"))
                            numberList.add(number);
                    } else
                        numberList.remove(number);

                    if (userInfos.get(pos).isSelected()) {
                        userInfos.get(pos).setSelected(false);
                    } else {
                        userInfos.get(pos).setSelected(true);
                    }
                }
            });

            if (holder.isBlocked) {
                holder.status.setText("Blocked");
            } else
                holder.status.setText("Subscribed");

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), v.findViewById(R.id.dot_menu));
                    popupMenu.getMenuInflater().inflate(R.menu.block_unblock_user_menu, popupMenu.getMenu());
                    if (holder.isBlocked) {
                        popupMenu.getMenu().getItem(1).setTitle("Unblock");
                        holder.action = "unblock";
                    } else {
                        popupMenu.getMenu().getItem(1).setTitle("Block");
                        holder.action = "block";
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.block:
                                    blockUnblock(position, holder);
                                    break;
                                /* case R.id.message:
                                    Fragment fragment = ChatFragment.newInstance(userInfos.get(position).getUserNumber() + "", UtilitiesAdi.giveMeSN(getActivity(),
                                            Utilities.getDatabaseName(getActivity())), userInfos.get(position).getFirstName() + " " + userInfos.get(position).getLastName());

                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                                    break; */
                                case R.id.profile:
                                    UserInfo u = userInfos.get(position);
                                    UserProfileDialog userProfileDialog = UserProfileDialog.newInstance(
                                            u.getFullName(),
                                            u.getRoll(),
                                            u.getSection(),
                                            u.getUserName(),
                                            u.getPhoneNumber()
                                    );
                                    userProfileDialog.show(getFragmentManager(), "profile");
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        private void blockUnblock(int position, final UserHolder holder) {
            String link = FragmentCodes.NEW_HOST + "Edit/blockUser.php";

            new PhpConnect(link, holder.action + "ing...", getActivity(), 1, new String[]{FragmentCodes.CMDXXX, userInfos.get(position).getUserName(), holder.action}, new String[]{"cmdxxx", "user_id", "action"}).setListener(new PhpConnect.ConnectOnClickListener() {
                @Override
                public void onConnectListener(String res) {
                    //Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
                    if (res.trim().toLowerCase().equals("1")) {
                        if (holder.isBlocked) {
                            holder.status.setText("Subscribed");
                            holder.isBlocked = false;
                            Toast.makeText(getActivity(), "Unblocked", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.status.setText("Blocked");
                            holder.isBlocked = true;
                            Toast.makeText(getActivity(), "Blocked", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(getActivity(), "Could not " + holder.action, Toast.LENGTH_SHORT).show();
                    //Log.d("TAG", "onConnectListener: "+res);
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

    private void postSms(String token, String fromPhone, final String tophone, String text) {
        Nimainterface nimainterface = ImmortalApplication.smsRetrofit().create(Nimainterface.class);
        nimainterface.sendSms(token, fromPhone, tophone, text).enqueue(new Callback<Sms>() {
            @Override
            public void onResponse(Call<Sms> call, Response<Sms> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successfully send to " + tophone, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to send sms.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Sms> call, Throwable t) {
                Log.d("fatal", "failure" + t);
            }
        });
    }
}
