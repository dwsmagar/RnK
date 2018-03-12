package com.susankya.schoolvalley;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
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


public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final int OWN_VIEW = 1, OTHER_VIEW = 2;
    private ArrayList<ChatDetail> mChatDetails;
    SharedPreferences prefs;
    @BindView(R.id.sendbtn)
    IconTextView sendButton;
    @BindView(R.id.recyclarView)
    RecyclerView recyclarView;
    @BindView(R.id.sms_editTxt)
    EditText chat_msg;
    private ChatAdapter chatAdapter;
    private BroadcastReceiver onNotice;
    private LinearLayoutManager lm;
    public boolean loading = false;
    public int visibleThreshold = 2;
    private static final int ID_COPY = 1, ID_DELETE = 2, ID_FORWARD = 3, ID_DETAILS = 4;
    private int noticeNum = 0;
    public static final int FOOTER_VIEW = 3;
    public static final String PREF_RECEIVER = "preference_for+receiver";
    private int escape = 0;
    private String msg_text;
    TableLayout tab;
    // TODO: Rename and change types of parameter
    private String user_no = "user_no", college_sn = "fromi", display_name = "Name";

    public static ChatFragment newInstance(String user_no, String college_sn, String displayName) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user_no);
        args.putString(ARG_PARAM2, college_sn);
        args.putString(ARG_PARAM3, displayName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_no = getArguments().getString(ARG_PARAM1);
            college_sn = getArguments().getString(ARG_PARAM2);
            display_name = getArguments().getString(ARG_PARAM3);
        }
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
        getActivity().setTitle("Send Message");
        lm = new LinearLayoutManager(getActivity());
        mChatDetails = new ArrayList<>();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utilities.isConnectionAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                msg_text = chat_msg.getText().toString().trim();
                if (!msg_text.trim().isEmpty()) {
                    ChatDetail cd = new ChatDetail();
                    cd.setName("You");
                    cd.setText(msg_text);
                    cd.setUserSn(Utilities.getSN(getActivity()));
                    cd.setCollegeSn("64");
                    cd.setSentBy((Utilities.isAdmin(getActivity())) ? "c" : "u");
                    cd.setStatus("sending");
                    cd.setUserSn(UtilitiesAdi.giveMeSN(getActivity(), Utilities.getDatabaseName(getActivity())));
                    mChatDetails.add(cd);
                    chatAdapter.notifyDataSetChanged();
                    recyclarView.scrollToPosition(mChatDetails.size() - 1);
                    sendMessage(mChatDetails.size() - 1);
                    chat_msg.setText("");
                }else {
                    Toast.makeText(getActivity(), "Write some message.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // populateList();
        /*lm.setReverseLayout(true);
        lm.getStackFromEnd();*/
        recyclarView.setLayoutManager(lm);
        chatAdapter = new ChatAdapter(getActivity(), mChatDetails);
        recyclarView.setAdapter(chatAdapter);
        if (mChatDetails.size() != 0)
            recyclarView.scrollToPosition(mChatDetails.size() - 1);
        if (Utilities.isConnectionAvailable(getActivity())) {
            getMessages();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                //Log.d(TAG, "onReceive: "+"BroadCAst REceived a");
                ChatDetail chatDetail = new ChatDetail();
                String str = intent.getStringExtra("msg");
                String str1 = intent.getStringExtra("sent_by");
                chatDetail.setName(str1);
                chatDetail.setSentBy(str1);
                chatDetail.setText(str);
                mChatDetails.add(chatDetail);
                chatAdapter.notifyDataSetChanged();
                recyclarView.scrollToPosition(mChatDetails.size() - 1);
                Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert);
                try {
                    RingtoneManager.getRingtone(getActivity(), sound).play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        getActivity().registerReceiver(onNotice, new IntentFilter(MyFirebaseMessagingService.BROADCAST_ACTION));
        getActivity().getSharedPreferences(PREF_RECEIVER, Context.MODE_PRIVATE).edit().putBoolean(PREF_RECEIVER, true).commit();
        registerForContextMenu(recyclarView);
        return v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = -1;
        try {
            pos = ((ChatFragment.ChatAdapter) recyclarView.getAdapter()).getPosition();
        } catch (Exception e) {
            //Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        final int position = pos;
        switch (item.getItemId()) {
            case ID_COPY:
                String string = mChatDetails.get(position).getText();
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Chat", string);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Text copied to clipboard.", Toast.LENGTH_SHORT).show();
                break;
            case ID_DELETE:
                String link = FragmentCodes.MAIN_DATABASE + "Firebase/deleteChat.php";
                new PhpConnect(link, "Deleting...", getActivity(), 1,
                        new String[]{UtilitiesAdi.giveMeSN(getActivity(), Utilities.getDatabaseName(getActivity()))
                                , mChatDetails.get(position).getChat_no() + "", "delete", user_no}
                        , new String[]{"college_sn", "chat_no", "action"
                        , "user_no"}).setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        //Log.d(TAG, "onConnectListener: "+res);
                        if (res.contains("1")) {
                            mChatDetails.remove(position);
                            chatAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Toast.makeText(getActivity(),"Stuff to delete here",Toast.LENGTH_SHORT).show();
                break;
            case ID_DETAILS:
                //Stuff to delete
                Toast.makeText(getActivity(), "Stuff to delete here", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause() {
        try {
            getActivity().unregisterReceiver(onNotice);
            getActivity().getSharedPreferences(PREF_RECEIVER, Context.MODE_PRIVATE).edit().putBoolean(PREF_RECEIVER, false).commit();
        } catch (Exception e) {
            //Log.d(TAG, "onPause: "+e.toString());
        }
        super.onPause();
    }

    private void operateResultString(String res) {

        try {
            JSONArray ja = new JSONArray(res);
            if (!(ja.length() == 0)) {
                save(res, user_no + "Chat");
                for (int i = ja.length() - 1; i >= 0; i--) {
                    ChatDetail cd = getChatDetailFromJson((JSONObject) ja.get(i));
                    mChatDetails.add(cd);
                }
            }
            chatAdapter.notifyDataSetChanged();
            recyclarView.scrollToPosition(mChatDetails.size() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMessages() {
        if (mChatDetails == null) {
            mChatDetails = new ArrayList<>();
        }
        String link = FragmentCodes.MAIN_DATABASE + "Firebase/";

        new PhpConnect(link, "", getActivity(), 0, new String[]{FragmentCodes.CMDXXX, user_no
                , "64", escape + "", "view_chat"},
                new String[]{"cmdxxx", "user_no", "college_sn", "last_chat_no", "action"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                res = res.replace("ï»¿", "").trim();
                //Log.d(TAG, "onConnectListener: "+res);
                operateResultString(res);
            }
        });
    }

    private ChatDetail getChatDetailFromJson(JSONObject job) {
        ChatDetail cd = new ChatDetail();
        try {
            cd.setChat_no(job.getInt("chat_no"));
            cd.setSentBy(job.getString("sent_by"));
            cd.setUserSn(job.getString("user_no"));
            cd.setStatus("");
            cd.setCollegeSn(job.getString("college_sn"));
            cd.setText(job.getString("text"));
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
        String link = FragmentCodes.MAIN_DATABASE + "Firebase/";
        String sentBy = (Utilities.isAdmin(getActivity())) ? "c" : "u";
        new PhpConnect(link, "", getActivity(), 0, new String[]{FragmentCodes.CMDXXX, "64"
                , user_no, sentBy, msg_text, "chat"},
                new String[]{"cmdxxx", "college_sn", "user_no", "sent_by", "text", "action"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                //Log.d(TAG, "onConnectListener: "+res);
                res = res.replace("ï»¿", "").trim();
                Uri sound = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.send_message);
                try {
                    RingtoneManager.getRingtone(getActivity(), sound).play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                builder.setSound(sound);
                NotificationManager notificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1234, builder.build());*/
                mChatDetails.get(indexOfArray).setStatus("sent");
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
        mChatDetails = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ChatDetail cd = new ChatDetail();
            mChatDetails.add(cd);
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

        public ChatAdapter(Context context, ArrayList<ChatDetail> notices) {
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
                    return new ChatHolder(this.context, view);
                case OTHER_VIEW:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_item_other, parent, false);
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
                holder.name.setText(mChatDetails.get(position).getName());
                holder.text.setText(mChatDetails.get(position).getText());
                
                if ("64".equals(mChatDetails.get(position).getUserSn() + "")) {
                    if (mChatDetails.get(position).getStatus().equals("sending")) {
                        Toast.makeText(context, "Sending", Toast.LENGTH_SHORT).show();
                        holder.pb.setVisibility(View.VISIBLE);
                        holder.sentImage.setVisibility(View.GONE);
                    } else if (mChatDetails.get(position).getStatus().equals("sent")) {
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
            if (mChatDetails.get(position) != null) {
                if (mChatDetails.get(position).getSentBy().equals("c") && Utilities.isAdmin(getActivity())) {
                    return OWN_VIEW;
                } else if (mChatDetails.get(position).getSentBy().equals("c") && !Utilities.isAdmin(getActivity()))
                    return OTHER_VIEW;
                else if (mChatDetails.get(position).getSentBy().equals("u") && Utilities.isAdmin(getActivity()))
                    return OTHER_VIEW;
                else if (mChatDetails.get(position).getSentBy().equals("u") && !Utilities.isAdmin(getActivity()))
                    return OWN_VIEW;
                else
                    return OWN_VIEW;
            } else
                return FOOTER_VIEW;
        }

        @Override
        public int getItemCount() {
            return mChatDetails.size();
        }
    }

    private class ChatHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        Context context;
        TextViewPlus name, text;
        ProgressBar pb;
        boolean isOwnMessage;
        ImageView sentImage;

        public ChatHolder(Context c, View v) {
            super(v);
            this.isOwnMessage = true;
            this.name = v.findViewById(R.id.name);
            this.text = v.findViewById(R.id.text);
            this.pb = v.findViewById(R.id.progress);
            this.sentImage = v.findViewById(R.id.done);
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

