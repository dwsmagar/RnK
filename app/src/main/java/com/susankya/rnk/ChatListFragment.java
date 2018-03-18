package com.susankya.rnk;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final int OPEN_CONVERSATION = 1, DELETE_CONVERSATION = 2;
    private static final String mFilenameOfChatList = "chatList";
    private ArrayList<ChatListItemDetail> mChatListDetail;
    @BindView(R.id.recyclarView)
    RecyclerView recyclarView;
    @BindView(R.id.emtpyTextview)
    TextView emptyText;
    @BindView(R.id.empty_img)
    ImageView imageView;
    @BindView(R.id.emptyTextLayout)
    View emptyView;
    private ChatListAdapter ChatListAdapter;
    private LinearLayoutManager lm;
    private DividerItemDecoration mDividerItemDecoration;
    public boolean loading = false;
    //  Bundle bundle;
    private String msg_text;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String msg = "msg", fromname = "fromname", fromu = "fromi", mobilenum = "helloNumber";

    public static ChatListFragment newInstance(String param1, String param2, String param3, String param4) {
        ChatListFragment fragment = new ChatListFragment();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_list, container, false);
        ButterKnife.bind(this, v);
        getActivity().setTitle("Inquiry Lists");
        lm = new LinearLayoutManager(getActivity());
        mChatListDetail = new ArrayList<>();
        // populateList();
        /*lm.setReverseLayout(true);
        lm.getStackFromEnd();*/
        recyclarView.setLayoutManager(lm);
        ChatListAdapter = new ChatListAdapter(getActivity(), mChatListDetail);
        recyclarView.setAdapter(ChatListAdapter);
        mDividerItemDecoration = new DividerItemDecoration(recyclarView.getContext(),
                lm.getOrientation());
        recyclarView.addItemDecoration(mDividerItemDecoration);
        if (Utilities.isConnectionAvailable(getActivity())) {
            //Log.d(TAG, "onCreateView: reached1");
            getChatList();
        } else {
            String path = getActivity().getFilesDir().getAbsolutePath();
            File file = new File(path + "/" + mFilenameOfChatList);
            if (file.exists()) {
                String savedResult = Utilities.load(getActivity(), mFilenameOfChatList);
                operateResultString(savedResult);
            } else {
                //Log.d(TAG, "onCreateView: reached");
                emptyView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_plug));
                emptyText.setText("OOPs, out of Connection");
            }
        }
      /*  if(mChatListDetail.size()!=0)
            recyclarView.scrollToPosition(mChatListDetail.size()-1);
            //recyclarView.smoothScrollToPosition(mChatListDetail.size()-1);*/
        registerForContextMenu(recyclarView);
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
                Fragment fragment = ChatFragment.newInstance(mChatListDetail.get(position).getUser_no(), UtilitiesAdi.giveMeSN(getActivity(), Utilities.getDatabaseName(getActivity())),
                        mChatListDetail.get(position).getFirstname() + " " + mChatListDetail.get(position).getLastname());
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
                        //Log.d(TAG, "onConnectListener: "+res);
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
        super.onPause();
    }

    private void getChatList() {
        if (mChatListDetail == null) {
            mChatListDetail = new ArrayList<>();
        }
        String link = FragmentCodes.MAIN_DATABASE + "Firebase/chat_listing.php";
        new PhpConnect(link, "Loading...", getActivity(), 1,
                new String[]{FragmentCodes.CMDXXX, "65", "0"},
                new String[]{"cmdxxx", "college_sn", "escape"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                res = res.replace("ï»¿", "").trim();
                Log.d("TAG","error"+res);
//                Log.d("", "onConnectListener: "+res);
                // Utilities.save(getActivity(),res,mFilenameOfChatList);
                operateResultString(res);
            }
        });
    }

    private void operateResultString(String res) {
        try {
            JSONArray ja = new JSONArray(res);
            if (ja.length() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_communication));
                emptyText.setText("No Inquiry List Found");
            } else {
                emptyView.setVisibility(View.GONE);
                Utilities.save(getActivity(), res, mFilenameOfChatList);
                for (int i = ja.length() - 1; i >= 0; i--) {
                    ChatListItemDetail cd = getChatListItemDetailFromJson((JSONObject) ja.get(i));
                    mChatListDetail.add(cd);
                }
            }
            ChatListAdapter.notifyDataSetChanged();
            //  recyclarView.scrollToPosition(mChatListDetail.size()-1);

        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Could not load data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private ChatListItemDetail getChatListItemDetailFromJson(JSONObject job) {
        ChatListItemDetail cd = new ChatListItemDetail();
        try {
            cd.setChat_no(job.getInt("chat_no") + "");
            cd.setFirstname(job.getString("firstName"));
            cd.setLastname(job.getString("lastName"));
            cd.setText(job.getString("text"));
            cd.setUser_no(job.getString("user_no"));
            cd.setUserid(job.getString("userid"));
        } catch (Exception e) {
            //Log.d(TAG, "getChatListItemDetailFromJson: "+e.toString());
        }
        return cd;
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
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = ChatFragment.newInstance(mChatListDetail.get(position).getUser_no(), "62",
                            mChatListDetail.get(position).getFirstname() + " " + mChatListDetail.get(position).getLastname());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
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
            menu.add(0, DELETE_CONVERSATION, 0, "Delete conversation");
        }
    }

}

