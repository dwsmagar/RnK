package com.susankya.schoolvalley;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.UUID;


public class NoticeListFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    private RecyclerView mNoticeList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private TextViewPlus emptyTextView;
    private IconTextView emptyIcon;
    private View emptyView;
    private ProgressBar loadingPB;
    private LinearLayout ll;
    private EditText searchEt;
    private FloatingActionButton fab;
    private String filterString;

    private int noticeNum = 0;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private final String LAST_NOTICE_NO = "last notice no";
    private ArrayList<notice> mNotices, tempmNotices;
    public int totalItemCount, lastVisibleItem;
    public boolean loading = false;
    public static String mFilename = "Notices";
    private View footer;
    private int sizeOfArray, prevSizeOfArray;
    private int sourceCode = 0;
    public static final int NORMAL_VIEW = 1;
    public int visibleThreshold = 2;
    public static final int HEADER_VIEW = 0;
    public static final int FOOTER_VIEW = 2;
    private int escape = 0;

    public static notice openedNotice = new notice();
    private boolean firstLoad = true, reachedBottomOnce = true;

    public void showEmptyView(int val) {
        if (mNotices.isEmpty()) {
            mNoticeList.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            if (val == NO_INTERNET)
                emptyTextView.setText("No internet connection");
            else if (val == LIST_EMPTY)
                emptyTextView.setText("Looks like there's no notice to load!");
            loadingPB.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle SIS) {
        super.onCreate(SIS);
        //   getNotice=new GetNotice();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        try {
            sourceCode = NavDrawerActivity.getZugad();
        } catch (Exception e) {
        }

        setHasOptionsMenu(true);
    }

    public static NoticeListFragment newInstance(String param1) {
        NoticeListFragment fragment = new NoticeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public NoticeListFragment() {
        mParam1 = "a";
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        prevSizeOfArray = 0;
        sizeOfArray = 0;
        // getActivity().setTitle("Notice");
    }

    private NoticeAdapter noticeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Notices");
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        notificationManager.cancel(NewNoticeService.NOTICE_FROM_SERVICE, 11);
        View v = inflater.inflate(R.layout.nlfragment, container, false);
        footer = getActivity().getLayoutInflater().inflate(R.layout.footerboy, null);

        fab = v.findViewById(R.id.floating_add_button);
        loadingPB = v.findViewById(R.id.progress);
        emptyIcon = v.findViewById(R.id.emptyIcon);
        emptyTextView = v.findViewById(R.id.emptyTxt);
        emptyView = v.findViewById(R.id.empty_layout);
        swipeRefreshLayout = v.findViewById(R.id.swiperefreshlayout);
        mNoticeList = v.findViewById(R.id.list);

        mNotices = new ArrayList<>();
        mNoticeList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mNoticeList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mNoticeList.getContext(),
                DividerItemDecoration.VERTICAL);
        mNoticeList.addItemDecoration(dividerItemDecoration);

        if (Utilities.isAdmin(getActivity())) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), AddNoticeActivity.class);
                    i.putExtra(AddNoticeActivity.TITLE, "");
                    i.putExtra(AddNoticeActivity.DESCRIPTION, "");
                    i.putExtra(AddNoticeActivity.NOTICE_NUM, "0");
                    i.putExtra(AddNoticeActivity.ISEDIT, false);
                    startActivity(i);
                }
            });
        } else
            fab.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        escape = 0;
                        prevSizeOfArray = 0;
                        sizeOfArray = 0;
                        reachedBottomOnce = false;
                        allNoticeLoaded = false;
                        mNotices.clear();
                        if (Utilities.isConnectionAvailable(getActivity())) {
                            emptyView.setVisibility(View.VISIBLE);
                            mNoticeList.setVisibility(View.VISIBLE);
                            GetNotices();
                        } else {
                            mNoticeList.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            emptyIcon.setVisibility(View.VISIBLE);
                            loadingPB.setVisibility(View.GONE);
                            emptyIcon.setText("{mdi-wifi-off}");
                            emptyTextView.setVisibility(View.VISIBLE);
                            emptyTextView.setText("OOPs out of Connection");
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );

        try {
            noticeAdapter = new NoticeAdapter(getActivity(), R.layout.notice_list, mNotices);
            mNoticeList.setAdapter(noticeAdapter);
            mNoticeList.addOnScrollListener(
                    new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            totalItemCount = layoutManager.getItemCount();
                            lastVisibleItem = layoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                //End of the items
                                if (noticeNum != 0 && mNotices.size() >= 10 && !allNoticeLoaded) {
                                    escape = mNotices.get(mNotices.size() - 1).getNotice_num();
                                    // Toast.makeText(getActivity(),escape+"",Toast.LENGTH_SHORT).show();
                                    mNotices.add(null);
                                    noticeAdapter.notifyItemInserted(mNotices.size() - 1);
                                    if (reachedBottomOnce) {
                                        GetNotices();
                                        reachedBottomOnce = false;
                                    }
                                }
                                loading = true;
                            }
                        }
                    }
            );

        } catch (Exception e) {
            // Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }

        if (firstLoad) {
            if (Utilities.isConnectionAvailable(getActivity())) {
                emptyView.setVisibility(View.VISIBLE);
                loadingPB.setVisibility(View.VISIBLE);
                GetNotices();
            } else {
                emptyView.setVisibility(View.VISIBLE);
                emptyIcon.setVisibility(View.VISIBLE);
                emptyIcon.setText("{mdi-wifi-off}");
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText("OOPs, out of Connection");
                loadingPB.setVisibility(View.GONE);
                reachedBottomOnce = true;
//                setter(load());
            }
            firstLoad = false;
        } else {
            mNoticeList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            loadingPB.setVisibility(View.GONE);
            if (((NavDrawerActivity) getActivity()).getArrayList() != null)
                mNotices = ((NavDrawerActivity) getActivity()).getArrayList();
            noticeAdapter = new NoticeAdapter(getActivity(), R.layout.notice_list, mNotices);
            mNoticeList.setAdapter(noticeAdapter);
            manageEmptyiew(mNotices);
        }
        registerForContextMenu(mNoticeList);

        if (sourceCode == 1) {
//            tv.setText("Click on red cross button to delete");
//            tv.setTextColor(getActivity().getResources().getColor(R.color.red));
        }
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.notice_delete, menu);
    }

    public class NoticeAdapter extends RecyclerView.Adapter<NoticeHolder> {

        private final ArrayList<notice> notices;
        private Context context;
        private int itemResource;

        public void setLoading(boolean loading) {
            isLoading = loading;
        }

        private boolean isLoading;

        public NoticeAdapter(Context context, int itemResource, ArrayList<notice> notices) {

            // 1. Initialize our adapter
            this.notices = notices;
            this.context = context;
            this.itemResource = itemResource;
        }

        // 2. Override the onCreateViewHolder method
        @Override
        public NoticeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 3. Inflate the view and return the new ViewHolder
            View view;
            switch (viewType) {
                case NORMAL_VIEW:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(this.itemResource, parent, false);
                    return new NoticeHolder(this.context, view);
                case FOOTER_VIEW:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.footerboy, parent, false);
                    return new NoticeHolder(this.context, view);

                default:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(this.itemResource, parent, false);
                    return new NoticeHolder(this.context, view);
            }
        }

        // 4. Override the onBindViewHolder method
        @Override
        public void onBindViewHolder(NoticeHolder holder, int position) {
            // 5. Use position to access the correct Bakery object
            notice nt = mNotices.get(position);
            holder.bindNotice(nt, position);
        }

        @Override
        public int getItemCount() {
            return mNotices.size();
        }

        @Override
        public int getItemViewType(int position) {

            if (mNotices.get(position) != null)
                return NORMAL_VIEW;
            else
                return FOOTER_VIEW;
        }
    }

    private class NoticeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextViewPlus noticeTitle, noticeDesc, noticeDate, placeholder;
        TextView category;
        ImageView attachment, share;
        notice notice;
        IconTextView dot_menu;
        LinearLayout toggle_menu;
        GradientDrawable sd;

        public NoticeHolder(Context c, View v) {
            super(v);
            this.noticeTitle = v.findViewById(R.id.title_content);
            this.noticeDate = v.findViewById(R.id.date_content);
            this.category = v.findViewById(R.id.category_content);
            this.noticeDesc = v.findViewById(R.id.description_content);
            this.attachment = v.findViewById(R.id.attachment_icon);
            this.placeholder = v.findViewById(R.id.ph_tv);
            this.dot_menu = v.findViewById(R.id.dot_menu);
            this.toggle_menu = v.findViewById(R.id.toggle_menu);
            v.setOnClickListener(this);
        }

        public void bindNotice(final notice notice, final int position) {

            if (getItemViewType() == FOOTER_VIEW) {
                if (Utilities.isConnectionAvailable(getActivity()))
                    this.itemView.setVisibility(View.GONE);
                else
                    this.itemView.setVisibility(View.VISIBLE);
            } else {
                if (Utilities.isAdmin(getActivity())) {
                    this.dot_menu.setVisibility(View.VISIBLE);
                    final View menuButton = this.dot_menu;
                    this.toggle_menu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(getActivity(), menuButton);
                            popupMenu.getMenuInflater().inflate(R.menu.menu_edit_delete_routine, popupMenu.getMenu());
                            // popupMenu.getMenu().getItem(0).setVisible(false);
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_delete_routine: {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setTitle("Delete Notice")
                                                    .setMessage("Do you want to delete?")
                                                    .setNegativeButton("Cancel", null)
                                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String link = FragmentCodes.NEW_HOST + "deleteNotice.php";
                                                            new PhpConnect(link, "Deleting...", getActivity(), 1, new String[]{FragmentCodes.CMDXXX, UtilitiesAdi.giveMeSN(getActivity(), Utilities.getDatabaseName(getActivity())), notice.getNotice_num() + ""}, new String[]{"cmdxxx", "college_sn", "num"}).setListener(new PhpConnect.ConnectOnClickListener() {
                                                                @Override
                                                                public void onConnectListener(String res) {
                                                                    if (res.trim().equals("1")) {
                                                                        Toast.makeText(getActivity(), "Notice deleted.", Toast.LENGTH_SHORT).show();
                                                                        mNotices.remove(position);
                                                                        noticeAdapter.notifyDataSetChanged();
                                                                        manageEmptyiew(mNotices);
                                                                    } else
                                                                        Toast.makeText(getActivity(), "Could not delete notice", Toast.LENGTH_SHORT).show();
                                                                    //Log.d("TAG", "onConnectListener: "+res);
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .show();
                                            return true;
                                        }
                                        case R.id.menu_view_routine: {

                                            if (Utilities.getBlocked(getActivity().getApplicationContext()) == 0) {
                                                Intent intent = new Intent(getActivity(), NoticeViewActivity.class);
                                                ((ImmortalApplication) getActivity().getApplication()).clickedNotice = mNotices.get(position);
                                                startActivityForResult(intent, 99);
                                            } else
                                                Snackbar.make(dot_menu, "You have been blocked by the admin.", Snackbar.LENGTH_LONG).show();
                                            break;
                                        }
                                        //  deleteNotice(notice.getNotice_num());

                                        case R.id.menu_edit_routine: {
                                            Intent i = new Intent(getActivity(), AddNoticeActivity.class);
                                            i.putExtra(AddNoticeActivity.TITLE, mNotices.get(position).getTitle());
                                            i.putExtra(AddNoticeActivity.DESCRIPTION, mNotices.get(position).getDescription());
                                            i.putExtra(AddNoticeActivity.NOTICE_NUM, mNotices.get(position).getNotice_num() + "");
                                            i.putExtra(AddNoticeActivity.ISEDIT, true);

                                            startActivity(i);
                                            break;
                                        }
                                    }
                                    return false;
                                }
                            });
                            popupMenu.show();
                        }
                    });
                } else {
                    this.dot_menu.setVisibility(View.GONE);
                }
                this.notice = notice;
                this.noticeTitle.setCustomFont(getActivity(), REGULAR);
                this.noticeTitle.setText(notice.getTitle());
                this.noticeDesc.setText(notice.getDescription());
                this.noticeDate.setText(notice.getMonthDay());
                this.placeholder.setText(notice.getCategory().substring(0, 1).toUpperCase());
                this.placeholder.setCustomFont(getActivity(), LIGHT);
                if (this.notice.isHasAttachment())
                    this.attachment.setVisibility(View.VISIBLE);
                else this.attachment.setVisibility(View.GONE);
                this.category.setText(notice.getCategory());
            }
        }

        @Override
        public void onClick(View v) {
            if (Utilities.getBlocked(getActivity().getApplicationContext()) == 0) {
                Intent intent = new Intent(getActivity(), NoticeViewActivity.class);
                ((ImmortalApplication) getActivity().getApplication()).clickedNotice = this.notice;
                startActivityForResult(intent, 99);
            } else
                Snackbar.make(v, "You have been blocked by the admin.", Snackbar.LENGTH_LONG).show();
        }
    }

    void manageEmptyiew(ArrayList JA) {
//        Log.d("TAG", "manageEmptyiew: " + JA.size());
//        String str = "fssfs";
        if (JA.size() == 0) {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyIcon.setVisibility(View.GONE);
            if (Utilities.isAdmin(getActivity())) {
                emptyTextView.setText("You have not uploaded any notice. Click on the add button below to add your first notice");
            } else {
                emptyTextView.setText("Your institution has not added any notice yet.");
            }
            loadingPB.setVisibility(View.GONE);
        } else
            emptyTextView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {
                firstLoad = true;
                mNotices.clear();
                GetNotices();
            }
        }
    }

    public void GetNotices() {
        String link = NEW_HOST + "RetrieveNotice.php";
        new PhpConnect(link, "Loading...", getActivity(), 0, new String[]{FragmentCodes.CMDXXX, "64", escape + "", Utilities.getUserNumber(getActivity()) + ""}, new String[]{"cmdxxx", "college_sn", "escape", "user_no"}).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String result) {
                        try {
//                            save(result);
                            JSONArray array = (JSONArray) new JSONTokener(result)
                                    .nextValue();
                            DateConverter dc;
                            dc = new DateConverter();

                            if (mNotices.size() > 10) {
                                mNotices.remove(mNotices.size() - 1);
                                noticeAdapter.notifyItemRemoved(mNotices.size());
                            }

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jO = array.getJSONObject(i);
                                notice n = new notice();
                                n.setDescription(jO.getString("notice"));
                                n.seen = jO.getInt("is_seen");
                                n.totalSeenCount = jO.getInt("seen_by");
                                n.setId(UUID.randomUUID());
                                n.setNotice_num(jO.getInt("notice_no"));
                                String attachmentUrl = jO.getString("file_location");
                                if (!attachmentUrl.isEmpty()) {
                                    n.setHasAttachment(true);
                                    n.setAttachmenturl(attachmentUrl);

                                    if (UtilitiesAdi.isImage(attachmentUrl))
                                        n.setHasImage(true);
                                    String fileName = jO.getString("file_name");
                                    String fileSize = jO.getString("file_size");
                                    n.setFilename(fileName);
                                    Double size = (Double.parseDouble(fileSize) / (1024 * 1024));
                                    Double sizeTruncated = BigDecimal.valueOf(size)
                                            .setScale(2, RoundingMode.HALF_UP)
                                            .doubleValue();
                                    n.setFileSize(fileSize);
                                    n.sizeMB = " (" + sizeTruncated + "MB)";
                                }

                                if (n.getDescription().length() >= 500 || n.isHasAttachment()) {
                                    n.setOpenDialog(true);
                                }
                                n.setCategory(jO.getString("category"));

                                String dateJO = jO.getString("date");
                                String dates[] = dateJO.split(" ");
                                int liked = jO.getInt("liked");
                                if (liked == 0) n.liked = false;
                                else n.liked = true;
                                n.setMonthDay(dates[1] + " " + dates[2]);
                                n.setTitle(jO.getString("title"));
                                n.likeNum = jO.getInt("likes");
                                String time = jO.getString("time");
                                n.setTime(time);
                                try {
                                    String[] months = new String[]{"babaJiKaThullu", "Baishak", "Jestha", "Ashar", "shrawan", "Bharda", "Ashoj", "Kartik", "Mangsir", "Poush", "Magh", "Falgun", "Chaitra"};
                                    String[] date = dateJO.split("-");
                                    if ((Integer.parseInt(date[0]) >= 2072)) {
                                        dateJO = months[Integer.parseInt(date[1])] + " " + Integer.parseInt(date[2]) + ", " + date[0];
                                    } else {

                                    }
                                } catch (Exception e) {
                                    // Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                                }
                                n.setDate(dateJO);
                                mNotices.add(n);
                                //adapter.add(n);
                            }

//                            Log.d("fatal", "" + mNotices.size());
                            manageEmptyiew(mNotices);
                            noticeAdapter.notifyDataSetChanged();

                            prevSizeOfArray = sizeOfArray;
                            loading = false;

                            sizeOfArray = mNotices.size();
                            SharedPreferences sp = getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor e = sp.edit();
                            notice n = mNotices.get(0);
                            ((NavDrawerActivity) getActivity()).setArrayList(mNotices);
                            if (Utilities.isAdmin(getContext()))
                                ActivityForFragment.setArrayList(mNotices);
                            noticeNum = n.getNotice_num();
                            e.putInt(LAST_NOTICE_NO, n.getNotice_num());
                            e.commit();
                            loadingPB.setVisibility(View.GONE);
                            loadOlderNotices();
                        } catch (Exception e) {
                            //Log.d("jklm",e.toString());
                            mNotices.remove(null);
                            noticeAdapter.notifyDataSetChanged();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        // showEmptyView(LIST_EMPTY);
                        reachedBottomOnce = true;
                    }
                });

    }

    public boolean allNoticeLoaded = false;

    private void loadOlderNotices() {
        if (sizeOfArray == prevSizeOfArray) {
            Toast.makeText(getActivity().getApplicationContext(), "No older notices", Toast.LENGTH_SHORT).show();
            allNoticeLoaded = true;
            footer.setVisibility(View.GONE);
        } else
            noticeAdapter.notifyDataSetChanged();
    }

    void setter(String result) {
        try {
            ArrayList<notice> mNoticesnew = new ArrayList<notice>();
            JSONArray array = (JSONArray) new JSONTokener(result)
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jO = array.getJSONObject(i);
                notice n = new notice();
                n.setDescription(jO.getString("notice"));
                //Log.d("setter", jO.getString("notice"));
                n.setId(UUID.randomUUID());
                n.setNotice_num(jO.getInt("notice_no"));
                n.setCategory(jO.getString("category"));
                String date = jO.getString("date");
                String time = jO.getString("time");
                n.setTime(time);
                n.setDate(date);
                n.setTitle(jO.getString("title"));
                //Log.d("ssss", "11");
                mNoticesnew.add(n);
                //  Toast.makeText(getActivity(),mNotices.toString(),Toast.LENGTH_SHORT).show();
            }
            //Log.d("AA", result);
            prevSizeOfArray = sizeOfArray;
            sizeOfArray = mNoticesnew.size();
            //Log.d("fromParticular notice", mNoticesnew.get(0).getDescription());
            mNotices = mNoticesnew;
            noticeAdapter = new NoticeAdapter(getActivity(), R.layout.notice_list, mNotices);
            mNoticeList.setAdapter(noticeAdapter);
            if (mNotices.size() == 0)
                emptyTextView.setVisibility(View.GONE);
            noticeAdapter.notifyDataSetChanged();
            // loadOderNotices();
        } catch (Exception e) {
            //      Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }
        showEmptyView(NO_INTERNET);
    }

    public void save(String array)
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

    public String load()// throws IOException, JSONException
    {
        String line = null;
        ArrayList<String> crimes = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            InputStream in = getActivity().getApplicationContext().openFileInput(NoticeListFragment.mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            line = jsonString.toString();

        } catch (Exception e) {

        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e) {

                }
        }
        return line;
    }

    public static class Notice extends android.support.v4.app.DialogFragment {
        private String title, description, date;

        public Notice() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                title = getArguments().getString("title", null);
                description = getArguments().getString("description", null);
                date = getArguments().getString("date", null);
            }
        }

        public void onResume() {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            getDialog().getWindow().setLayout((14 * width) / 15, (3 * height) / 5);
            super.onResume();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
            View v = inflater.inflate(R.layout.notice_dialog, null, false);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
            //getDialog().getWindow().setLayout((6 * width)/7, (4 * height)/5);
            TextView titleTv = (TextView) v.findViewById(R.id.title);
            titleTv.setText(title);
            TextView tv = (TextView) v.findViewById(R.id.notice);
            try {
                tv.setTypeface(Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/segoepr.ttf"));
            } catch (Exception e) {
                //Log.d("ttf", e.toString());
            }

            tv.setText(description);
            TextView Date = (TextView) v.findViewById(R.id.date);
            Date.setText("Published on: " + date);
            return v;
        }
    }
}
