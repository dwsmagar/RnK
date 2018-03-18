package com.susankya.rnk;

import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.android.volley.VolleyLog.TAG;


public class UserToUserChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int OWN_VIEW = 1, OTHER_VIEW = 2;
    private ArrayList<UserToUserChatDetail> mUserToUserChatDetails;
    @BindView(R.id.sendbtn)
    IconTextView sendButton;
    @BindView(R.id.recyclarView)
    RecyclerView recyclarView;
    @BindView(R.id.sms_editTxt)
    EditText chat_msg;
    @BindView(R.id.emptyTextLayout)
    View emptyView;
    @BindView(R.id.empty_img)
    ImageView empty;
    @BindView(R.id.emtpyTextview)
    TextView emptyText;
    private ChatAdapter chatAdapter;
    private BroadcastReceiver onNotice;
    private LinearLayoutManager lm;
    public boolean loading = false;
    private static final int ID_COPY = 1, ID_DELETE = 2, ID_FORWARD = 3, ID_DETAILS = 4;
    private int noticeNum = 0;
    public static final int FOOTER_VIEW = 3;
    public static final String PREF_RECEIVER_UTUCHAT = "preference_for_UTU_receiver";
    private int escape = 0;
    private String msg_text;
    private String user_no = "user_no", display_name = "Name";
    private int current_user_no = 0;

    public static UserToUserChatFragment newInstance(String user_no, String displayName) {
        UserToUserChatFragment fragment = new UserToUserChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user_no);
        args.putString(ARG_PARAM2, displayName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_no = getArguments().getString(ARG_PARAM1);
            display_name = getArguments().getString(ARG_PARAM2);
        }
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(user_no));
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
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, v);
        current_user_no = Utilities.getUserNumber(getActivity());
//        Log.d(TAG, "newInstance: " + user_no + " " + display_name + " " + current_user_no);
        getActivity().setTitle(display_name);
        lm = new LinearLayoutManager(getActivity());
        mUserToUserChatDetails = new ArrayList<>();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utilities.isConnectionAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "No Internet Connection, please try again later.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    msg_text = chat_msg.getText().toString().trim();
                    if (!msg_text.trim().isEmpty()) {
                        UserToUserChatDetail cd = new UserToUserChatDetail();
                        cd.text = msg_text;
                        cd.user1 = user_no;
                        cd.user2 = current_user_no + "";
                        cd.sentBy = current_user_no + "";
                        cd.status = "sending";
                        mUserToUserChatDetails.add(cd);
                        chatAdapter.notifyDataSetChanged();
                        recyclarView.scrollToPosition(mUserToUserChatDetails.size() - 1);
                        sendMessage(mUserToUserChatDetails.size() - 1);
                        chat_msg.setText("");
                        emptyView.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "Write some message.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // populateList();
        /*lm.setReverseLayout(true);
        lm.getStackFromEnd();*/
        recyclarView.setLayoutManager(lm);
        chatAdapter = new ChatAdapter(getActivity(), mUserToUserChatDetails);
        recyclarView.setAdapter(chatAdapter);

        if (mUserToUserChatDetails.size() != 0)
            recyclarView.scrollToPosition(mUserToUserChatDetails.size() - 1);

        if (Utilities.isConnectionAvailable(getActivity())) {
            getMessages();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection, you may not be able to send messages.", Toast.LENGTH_SHORT).show();
            String path = getActivity().getFilesDir().getAbsolutePath();
            File file = new File(path + "/" + user_no + "Chat");
            if (file.exists()) {
                String result = Utilities.load(getActivity(), user_no + "Chat");
                operateResultString(result);
                //Log.d(TAG, "onCreateView: "+result);

            } else
                Toast.makeText(getActivity(), "No Chat history found.", Toast.LENGTH_SHORT).show();
        }

        onNotice = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: " + "BroadCAst REceived a");
                UserToUserChatDetail chatDetail = new UserToUserChatDetail();
                String str = intent.getStringExtra("msg");
                String sentBy = intent.getStringExtra("sent_by");
                String senderUsername = intent.getStringExtra("senderUsername");

                if (sentBy.equals(user_no)) {
                    chatDetail.text = str;
                    chatDetail.sentBy = sentBy;
                    chatDetail.user2 = Utilities.getUserNumber(getActivity()) + "";
                    chatDetail.user1 = sentBy;

                    mUserToUserChatDetails.add(chatDetail);
                    chatAdapter.notifyDataSetChanged();
                    recyclarView.scrollToPosition(mUserToUserChatDetails.size() - 1);
                    Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert);
                    try {
                        RingtoneManager.getRingtone(getActivity(), sound).play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        };
        getActivity().registerReceiver(onNotice, new IntentFilter(MyFirebaseMessagingService.BROADCAST_ACTION_UTUCHAT + user_no));///user_no is added for the registration of a particular user for the particular notification to be sent to.
        getActivity().getSharedPreferences(PREF_RECEIVER_UTUCHAT, Context.MODE_PRIVATE).edit().putBoolean(PREF_RECEIVER_UTUCHAT + user_no, true).commit();// same as above, if the fragment for the user whose message's notification is received by the device.
        registerForContextMenu(recyclarView);

        return v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = -1;
        try {
            pos = ((UserToUserChatFragment.ChatAdapter) recyclarView.getAdapter()).getPosition();
        } catch (Exception e) {
            //Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        final int position = pos;
        switch (item.getItemId()) {
            case ID_COPY:
                String string = mUserToUserChatDetails.get(position).text;
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Chat", string);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Text copied to clipboard.", Toast.LENGTH_SHORT).show();
                break;
            case ID_DELETE:
                String link = FragmentCodes.MAIN_DATABASE + "Firebase/users_chat/deleteChat.php";
                new PhpConnect(link, "Deleting...", getActivity(), 1,
                        new String[]{mUserToUserChatDetails.get(position).chat_no + "", "delete"}
                        , new String[]{"chat_no", "action"
                }).setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        //Log.d(TAG, "onConnectListener: "+res);
                        if (res.contains("1")) {
                            mUserToUserChatDetails.remove(position);
                            chatAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Toast.makeText(getActivity(),"Stuff to delete here",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause() {
        try {
            getActivity().unregisterReceiver(onNotice);
            getActivity().getSharedPreferences(PREF_RECEIVER_UTUCHAT, Context.MODE_PRIVATE).edit().putBoolean(PREF_RECEIVER_UTUCHAT + user_no, false).commit();
        } catch (Exception e) {
            //Log.d(TAG, "onPause: "+e.toString());
        }
        super.onPause();
    }

    private void operateResultString(String res) {

        try {
            JSONArray ja = new JSONArray(res);
            if (!(ja.length() == 0)) {
                emptyView.setVisibility(View.GONE);
                save(res, user_no + "Chat");
                for (int i = ja.length() - 1; i >= 0; i--) {
                    UserToUserChatDetail cd = getChatDetailFromJson((JSONObject) ja.get(i));
                    mUserToUserChatDetails.add(cd);
                }
            } else {
                emptyView.setVisibility(View.VISIBLE);
                empty.setImageDrawable(getResources().getDrawable(R.drawable.ic_conversation));
                emptyText.setText("No chat history found. Start with a new conversation");
            }
            chatAdapter.notifyDataSetChanged();
            recyclarView.scrollToPosition(mUserToUserChatDetails.size() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMessages() {
        if (mUserToUserChatDetails == null) {
            mUserToUserChatDetails = new ArrayList<>();
        }
        String link = FragmentCodes.MAIN_DATABASE + "Firebase/users_chat/users_chat.php";

        new PhpConnect(link, "loading", getActivity(), 1, new String[]{FragmentCodes.CMDXXX, user_no
                , Utilities.getUserNumber(getActivity()) + "", escape + "", "view_chat"},
                new String[]{"cmdxxx", "user_one", "user_two", "last_chat_no", "action"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                res = res.replace("ï»¿", "").trim();
                Log.d(TAG, "frompost onConnectListener: " + res);
                operateResultString(res);
            }
        });
    }

    private UserToUserChatDetail getChatDetailFromJson(JSONObject job) {
        UserToUserChatDetail cd = new UserToUserChatDetail();
        try {
            cd.text = job.getString("text");
            cd.user1 = job.getString("user1");
            cd.user2 = job.getString("user2");
            cd.sentBy = job.getString("sent_by");
            cd.chat_no = job.getString("chat_no");
            cd.status = "";
        } catch (Exception e) {
            //Log.d(TAG, "getChatDetailFromJson: "+e.toString());
        }
        return cd;
    }

    public void save(String array, String mFilename)
    // throws JSONException, IOException
    {

        Writer writer = null;
        try {
            OutputStream out = getActivity().getApplicationContext()
                    .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } catch (Exception e) {

        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception e) {

                }
        }
    }

    private void sendMessage(final int indexOfArray) {
        String link = FragmentCodes.MAIN_DATABASE + "Firebase/users_chat/users_go.php";
        new PhpConnect(link, "", getActivity(), 0, new String[]{FragmentCodes.CMDXXX, current_user_no + "", user_no, current_user_no + "", msg_text, "chat"},
                new String[]{"cmdxxx", "user_one", "user_two", "sent_by", "text", "action"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                Log.d(TAG, "onConnectListener: utuchat" + res);
                res = res.replace("ï»¿", "").trim();
                Uri sound = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.send_message);
                try {
                    RingtoneManager.getRingtone(getActivity(), sound).play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                builder.setSound(sound);
                NotificationManager notificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1234, builder.build());
                notificationManager.cancel(Integer.parseInt(user_no));

                mUserToUserChatDetails.get(indexOfArray).status = "sent";
                chatAdapter.notifyDataSetChanged();
                try {
                    JSONObject job = new JSONObject(res);
                    //Log.d(TAG, "onConnectListener: result"+job);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (res.trim().equals("1")) {

                }
            }
        });
    }

    private void populateList() {
        mUserToUserChatDetails = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserToUserChatDetail cd = new UserToUserChatDetail();
            mUserToUserChatDetails.add(cd);
        }
    }

    public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

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

        public ChatAdapter(Context context, ArrayList<UserToUserChatDetail> notices) {
            // 1. Initialize our adapter
            this.context = context;
        }

        // 2. Override the onCreateViewHolder method
        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 3. Inflate the view and return the new ViewHolder
            this.viewType = viewType;
            View view;
            switch (viewType) {
                case OWN_VIEW:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_item_own, parent, false);
                    //Log.d(TAG, "onCreateViewHolder: OWN");
                    return new ChatHolder(this.context, view);
                case OTHER_VIEW:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_item_other, parent, false);
                    //Log.d(TAG, "onCreateViewHolder: others");
                    return new ChatHolder(this.context, view);
                case FOOTER_VIEW:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.footerboy, parent, false);
                    return new ChatHolder(this.context, view);
                default:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_item_own, parent, false);
                    return new ChatHolder(this.context, view);
            }
        }

        @Override
        public void onBindViewHolder(final ChatHolder holder, int position) {
            if (viewType == FOOTER_VIEW) {

            } else {
                holder.text.setText(mUserToUserChatDetails.get(position).text);
                Log.d(TAG, "onBindViewHolder: " + Utilities.getUserNumber(getActivity()) + " " + mUserToUserChatDetails.get(position).sentBy + "");
                if ((Utilities.getUserNumber(getActivity()) + "").equals(mUserToUserChatDetails.get(position).sentBy + "")) {
                    if (mUserToUserChatDetails.get(position).status.equals("sending")) {
                        holder.pb.setVisibility(View.VISIBLE);
                        holder.sentImage.setVisibility(View.GONE);
                    } else if (mUserToUserChatDetails.get(position).status.equals("sent")) {
                        Toast.makeText(context, "Sent", Toast.LENGTH_SHORT).show();
                        holder.sentImage.setVisibility(View.VISIBLE);
                        holder.pb.setVisibility(View.GONE);
                    } else {
                        holder.sentImage.setVisibility(View.GONE);
                        holder.pb.setVisibility(View.GONE);
                    }
                }
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        setPosition(holder.getPosition());
                        return false;
                    }
                });

            }
        }

        // 4. Override the onBindViewHolder method
        @Override
        public void onViewRecycled(ChatHolder holder) {
            holder.itemView.setOnLongClickListener(null);
            super.onViewRecycled(holder);
        }

        @Override
        public int getItemViewType(int position) {
            if (mUserToUserChatDetails.get(position) != null) {
                Log.d(TAG, "frompost getItemViewType: " + mUserToUserChatDetails.get(position).sentBy + " " + Utilities.getUserNumber(getActivity()));
                if (mUserToUserChatDetails.get(position).sentBy.trim().equals(Utilities.getUserNumber(getActivity()) + "")) {
                    return OWN_VIEW;
                } else
                    return OTHER_VIEW;
            } else
                return FOOTER_VIEW;
        }

        @Override
        public int getItemCount() {
            return mUserToUserChatDetails.size();
        }
    }

    private class ChatHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        Context context;
        TextViewPlus name, text;
        ProgressBar pb;
        ImageView sentImage;


        public ChatHolder(Context c, View v) {
            super(v);
            this.name = (TextViewPlus) v.findViewById(R.id.name);
            this.text = (TextViewPlus) v.findViewById(R.id.text);
            this.pb = (ProgressBar) v.findViewById(R.id.progress);
            this.sentImage = (ImageView) v.findViewById(R.id.done);
            v.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {

            menu.setHeaderTitle("Select Action");
            menu.add(0, ID_COPY, 0, "Copy");//groupId, itemId, order, title
//            menu.add(0, ID_DELETE, 0, "Delete");//groupId, itemId, order, title
            //menu.add(0, ID_DETAILS, 0, "Details");

        }
    }
}

