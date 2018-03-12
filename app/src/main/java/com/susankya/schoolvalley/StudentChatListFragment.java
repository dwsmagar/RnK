package com.susankya.schoolvalley;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.VolleyLog.TAG;


public class StudentChatListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final int OPEN_CONVERSATION = 1, DELETE_CONVERSATION = 2;
    private static final String mFilenameOfChatList = "chatList";
    public static final String PREF_RECEIVER_UTUCHAT_LIST = "userToUserChatList_receiver";
    private String current_user;
    private BroadcastReceiver onNotice;

    private ArrayList<ChatListItemDetail> mChatListDetail;
    @BindView(R.id.recyclarView)
    RecyclerView recyclarView;
    @BindView(R.id.emtpyTextview)
    TextView emptyText;
    @BindView(R.id.emptyTextLayout)
    View emptyView;
    @BindView(R.id.icontText)
    IconTextView iconTextView;
    @BindView(R.id.floating_more_button)
    FloatingActionButton fab_moreButton;
    private ChatListAdapter ChatListAdapter;
    private LinearLayoutManager lm;
    public boolean loading = false;
    //  Bundle bundle;
    private String msg_text;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String msg = "msg", fromname = "fromname", fromu = "fromi", mobilenum = "helloNumber";

    public static StudentChatListFragment newInstance(String param1, String param2, String param3, String param4) {
        StudentChatListFragment fragment = new StudentChatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromname = getArguments().getString(ARG_PARAM1);
            fromu = getArguments().getString(ARG_PARAM2);
            mobilenum = getArguments().getString(ARG_PARAM3);
            msg = getArguments().getString(ARG_PARAM4);
        }
        current_user = Utilities.getUserNumber(getActivity()) + "";
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(onNotice);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_chat_list, container, false);
        ButterKnife.bind(this, v);
        getActivity().setTitle("Chat List");
        fab_moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GroupMateListFragment()).addToBackStack(null).commit();
            }
        });
        lm = new LinearLayoutManager(getActivity());
        mChatListDetail = new ArrayList<>();
        recyclarView.setLayoutManager(lm);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclarView.getContext(),
                lm.getOrientation());
        recyclarView.addItemDecoration(mDividerItemDecoration);
        ChatListAdapter = new ChatListAdapter(getActivity(), mChatListDetail);
        recyclarView.setAdapter(ChatListAdapter);
        if (Utilities.isConnectionAvailable(getActivity())) {
            //Log.d(TAG, "onCreateView: reached1");
            getList();
        } else {
            String path = getActivity().getFilesDir().getAbsolutePath();
            File file = new File(path + "/" + mFilenameOfChatList);
            if (file.exists()) {
                String savedResult = Utilities.load(getActivity(), mFilenameOfChatList);
                operateResultString(savedResult);
            } else {
                //Log.d(TAG, "onCreateView: reached");
                emptyView.setVisibility(View.VISIBLE);
                iconTextView.setText("{mdi-wifi-off}");
                emptyText.setText("OOPs, Out of connection");
            }
        }
      /*  if(mChatListDetail.size()!=0)
            recyclarView.scrollToPosition(mChatListDetail.size()-1);
            //recyclarView.smoothScrollToPosition(mChatListDetail.size()-1);*/

        //registerForContextMenu(recyclarView);//backend work not done
        onNotice = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: " + "BroadCAst Received in list");
                for (int i = 0; i < mChatListDetail.size(); i++) {
                    Log.d(TAG, "onReceive: " + mChatListDetail.get(i).getUser_no() + " " + intent.getStringExtra("sent_by"));

                    if (mChatListDetail.get(i).getUser_no().equals(intent.getStringExtra("sent_by"))) {
                        mChatListDetail.get(i).setText(intent.getStringExtra("msg"));
                        mChatListDetail.get(i).setHasNew(true);
                        ChatListItemDetail cDetail = mChatListDetail.get(i);
                        mChatListDetail.remove(i);
                        mChatListDetail.add(0, cDetail);
                    }
                    recyclarView.getAdapter().notifyDataSetChanged();
                }
            }
        };
        getActivity().registerReceiver(onNotice, new IntentFilter(MyFirebaseMessagingService.BROADCAST_ACTION_UTUCHAT_LIST));///user_no is added for the registration of a particular user for the particular notification to be sent to.
        getActivity().getSharedPreferences(PREF_RECEIVER_UTUCHAT_LIST, Context.MODE_PRIVATE).edit().putBoolean(PREF_RECEIVER_UTUCHAT_LIST, true).commit();// same as above, if the fragment for the user whose message's notification is received by the device.
        return v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = -1;
        try {
            pos = ((ChatListAdapter) recyclarView.getAdapter()).getPosition();
        } catch (Exception e) {
            //Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        final int position = pos;
        switch (item.getItemId()) {
            case OPEN_CONVERSATION:
                Fragment fragment = UserToUserChatFragment.newInstance("" + mChatListDetail.get(position).getUser_no(), mChatListDetail.get(position).getFirstname() + " " + mChatListDetail.get(position).getLastname());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                break;
            case DELETE_CONVERSATION:
                //Stuff to delete
                String link = FragmentCodes.MAIN_DATABASE + "Firebase/deleteChat.php";
                new PhpConnect(link, "Deleting...", getActivity(), 1,
                        new String[]{UtilitiesAdi.giveMeSN(getActivity(), Utilities.getDatabaseName(getActivity()))
                                , mChatListDetail.get(position).getChat_no() + "", "deleteAll", mChatListDetail.get(position).getUser_no()}
                        , new String[]{"college_sn", "chat_no", "action"
                        , "user_no"}).setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        if (res.contains("1")) {
                            mChatListDetail.remove(position);
                            ChatListAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause() {
        try {
            getActivity().unregisterReceiver(onNotice);
        } catch (Exception e) {
            //Log.d(TAG, "onPause: "+e.toString());
        }
        super.onPause();
    }

    private void operateResultString(String res) {
        try {
            JSONArray ja = new JSONArray(res);
            if (ja.length() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                emptyText.setText("No Chat History");
                iconTextView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                //Utilities.save(getActivity(),res,mFilenameOfChatList);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject job = (JSONObject) ja.get(i);
                    ChatListItemDetail detail = new ChatListItemDetail();
                    detail.setText(job.getString("text"));
                    detail.setUser_no(job.getString("user"));
                    detail.setChat_no(job.getString("chat_no"));
                    detail.setFirstname(job.getString("first_name"));
                    detail.setLastname(job.getString("last_name"));
                    mChatListDetail.add(detail);
                    Log.d(TAG, "operateResultString: " + ja.length());
                }
            }
            ChatListAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Could not load data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void getList() {
        String link = FragmentCodes.MAIN_DATABASE + "Firebase/users_chat/users_chat_listing.php";
        new PhpConnect(link, "getting list..", getActivity(), 1, new String[]{FragmentCodes.CMDXXX, current_user}, new String[]{"cmdxxx", "user_sn"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                Log.d("TAG", "onConnectListener: " + res+current_user);

                res = res.replace("ï»¿", "").trim();
                operateResultString(res);
            }
        });
    }

    public class ChatListAdapter extends RecyclerView.Adapter<ChatHolder> {

        private Context context;
        private int viewType;

        public void setLoading(boolean loading) {
            isLoading = loading;
        }

        private boolean isLoading;
        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public ChatListAdapter(Context context, ArrayList<ChatListItemDetail> chatlistdetail) {
            // 1. Initialize our adapter
            this.context = context;
        }

        // 2. Override the onCreateViewHolder method
        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 3. Inflate the view and return the new ViewHolder
            this.viewType = viewType;
            View view;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_list_item, parent, false);
            return new ChatHolder(this.context, view);
        }


        @Override
        public void onBindViewHolder(final ChatHolder holder, final int position) {
            holder.name.setText(mChatListDetail.get(position).getFirstname() + " " + mChatListDetail.get(position).getLastname());
            holder.text.setText(mChatListDetail.get(position).getText());
            holder.image.setText(mChatListDetail.get(position).getFirstname().substring(0, 1));
            if (mChatListDetail.get(position).hasNew) {
                holder.name.setTypeface(holder.name.getTypeface(), Typeface.BOLD);
                holder.text.setTypeface(holder.text.getTypeface(), Typeface.BOLD);
            }
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mChatListDetail.get(position).setHasNew(false);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, UserToUserChatFragment.newInstance("" + mChatListDetail.get(position).getUser_no(), mChatListDetail.get(position).getFirstname() + " " + mChatListDetail.get(position).getLastname())).addToBackStack(null).commit();
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setPosition(holder.getPosition());
                    return false;
                }
            });
        }

        @Override
        public void onViewRecycled(ChatHolder holder) {
            holder.itemView.setOnLongClickListener(null);
            super.onViewRecycled(holder);
        }
        // 4. Override the onBindViewHolder method


        @Override
        public int getItemCount() {
            return mChatListDetail.size();
        }
    }

    private class ChatHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        Context context;
        TextViewPlus name, text, image;
        private View view;

        public ChatHolder(Context c, View v) {
            super(v);
            this.view = v;
            this.name = (TextViewPlus) v.findViewById(R.id.name);
            this.text = (TextViewPlus) v.findViewById(R.id.chatMsg);
            this.image = (TextViewPlus) v.findViewById(R.id.textForFont);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {

            menu.setHeaderTitle("Select Action");
            menu.add(0, OPEN_CONVERSATION, 0, "Open conversation");//groupId, itemId, order, title
            // menu.add(0, DELETE_CONVERSATION, 0, "Delete conversation");
        }
    }
}



