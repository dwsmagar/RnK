package com.susankya.schoolvalley;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;


public class PresentRoutineListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String  TAG_SECTION_NAME="sectionname";
    public static final String  TAG_EDIT_VIEW="editview";
    public  static boolean isAdd=true;
    public static final String  TAG_EDIT="edit";
    public static final String  TAG_VIEW="view";
    private EditText searchEditText;
    private SwipeRefreshLayout swipeRefreshLayout;
private ListView listOfRoutine;
    private String filterString;
    private adapt adapter;
    Menu menu;
    ArrayList<String> arrayList;
    ArrayList<String> temparrayList;
    TextView header,emptyView;
    View line;
    FloatingActionButton fab;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PresentRoutineListFragment() {
        // Required empty public constructor
    }

    public static PresentRoutineListFragment newInstance(String param1, String param2) {
        PresentRoutineListFragment fragment = new PresentRoutineListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
private void manageEmpty(ArrayList list){
    if(list.size()==0){
    emptyView.setVisibility(View.VISIBLE);
    header.setVisibility(View.INVISIBLE);
    }else {

        emptyView.setVisibility(View.GONE);
        header.setVisibility(View.VISIBLE);
    }
}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        loadSectionList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                if(NavDrawerActivity.isShowingSearchEditText()) {
                    NavDrawerActivity.hideSearch();
                }
                else{
                    ((NavDrawerActivity)getActivity()).showSearch();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search,menu);
        this.menu=menu;
       }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_add_routine, container, false);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        swipeRefreshLayout.setRefreshing(true);
                        arrayList.clear();
                        loadSectionList();
                    }
                }
        );
        listOfRoutine=(ListView)v.findViewById(R.id.routine_set_list);
        header=(TextView)v.findViewById(R.id.header);
        emptyView=(TextView)v.findViewById(R.id.emptyView);
        line=(View)v.findViewById(R.id.divider);
        searchEditText=NavDrawerActivity.getSearchEditText();
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isVisible()){
            filterString=charSequence.toString();
                temparrayList=new ArrayList<String>();
                for(String temp:arrayList){
                    if(temp.toLowerCase().contains(filterString.toLowerCase()))
                    temparrayList.add(temp);
                }
                adapter=new adapt(temparrayList);
                listOfRoutine.setAdapter(adapter);}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fab=(FloatingActionButton)v.findViewById(R.id.floating_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.ask_name_of_section_dialog);
                final EditText et=(EditText)dialog.findViewById(R.id.sectionName);

                ((Button)dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ((Button)dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!et.getText().toString().isEmpty()){
                            isAdd=true;
                            Intent intent=new Intent(getActivity(),AddRoutineActivity.class);
                            intent.putExtra(TAG_SECTION_NAME,et.getText().toString().trim().toUpperCase());
                            AddRoutineActivity.setSparseArray(null);
                            intent.putExtra(TAG_EDIT_VIEW,TAG_EDIT);

                            startActivity(intent);

                            dialog.dismiss();
                        }else
                        {
                            Toast.makeText(getActivity(),"Enter section's name",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.show();
            }
        });
       loadSectionList();
    return  v;
    }
    private void loadSectionList(){
        String link=FragmentCodes.NEW_HOST+"SectionName.php";
        new PhpConnect(link,"",getActivity(),0,new String[]{FragmentCodes.CMDXXX, UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity()))},new String[]{"cmdxxx","college_sn"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                try {
                    //   Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
                    //Log.d("TAGabcd", "onConnectListener:"+res);
                    JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                    arrayList=new ArrayList<String>();
                    String[]   sections = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject job = jsonArray.getJSONObject(i);
                        sections[i] = job.getString("section_name");
                        arrayList.add(job.getString("section_name"));
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    adapter=new adapt(arrayList);
                    listOfRoutine.setAdapter(adapter);
                  manageEmpty(arrayList);
                     } catch (Exception e) {
                    //Log.d("TAG", "onConnectListener: "+e.toString());
                }
            }
        });

    }
    private class adapt extends ArrayAdapter<String> {
        private ArrayList<String> sectionList;
        String[] routine;

        public adapt(ArrayList<String> c) {
            super(getActivity(), 0, c);
            //profile = c;
             sectionList= c;
        }

        @Override
        public View getView(final int pos, View v, ViewGroup vg) {
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.question_set_item, null);
            }
            final String sectionName = sectionList.get(pos);

            final ImageView holder = (ImageView) v.findViewById(R.id.download_questions_set);
            holder.setVisibility(View.GONE);
            final TextViewPlus tvForFont=(TextViewPlus) v.findViewById(R.id.textForFont);
            tvForFont.setVisibility(View.VISIBLE);
            tvForFont.setText(sectionName.substring(0,1).toUpperCase());
            GradientDrawable sd=(GradientDrawable) tvForFont.getBackground().mutate();
            sd.setColor(getResources().getColor(R.color.colorPrimary));
            tvForFont.setBackground(sd);
            final ImageView Popup = (ImageView) v.findViewById(R.id.popup);
            final View.OnClickListener onClickListener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(getActivity(), Popup);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_edit_delete_routine, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_edit_routine:
                                    isAdd=false;
                                    editRoutine(sectionName,TAG_EDIT);
                                    return true;
                                case R.id.menu_delete_routine:
                                    //deleteRoutine(sectionName);
                                    String link=FragmentCodes.NEW_HOST+"AddRoutine.php";
                                    new PhpConnect(link,"Deleting...",getActivity(),1,new String[]{FragmentCodes.CMDXXX,UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity())),sectionName,"delete"},new String[]{"cmdxxx","college_sn","section_name","action"}).setListener(new PhpConnect.ConnectOnClickListener() {
                                        @Override
                                        public void onConnectListener(String res) {
                                            //Log.d("TAG", "onConnectListener: "+res);
                                            if(res.equals("1")){
                                                sectionList.remove(pos);
                                                notifyDataSetChanged();
                                                manageEmpty(sectionList);
                                                Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    return true;
                                case R.id.menu_view_routine:
                                    editRoutine(sectionName,TAG_VIEW);
                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                    popupMenu.show();
                }
            };
            Popup.setOnClickListener(onClickListener);
            v.setOnClickListener(onClickListener);
            TextView tv = (TextView) v.findViewById(R.id.code);
            tv.setText(sectionName);
            return v;

        }
        private void deleteRoutine(final String section){

            String link=FragmentCodes.NEW_HOST+"AddRoutine.php";

            new PhpConnect(link,"Deleting...",getActivity(),1,new String[]{FragmentCodes.CMDXXX,UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity())),section,"delete"},new String[]{"cmdxxx","college_sn","section_name","action"}).setListener(new PhpConnect.ConnectOnClickListener() {
                @Override
                public void onConnectListener(String res) {
                    //Log.d("TAG", "onConnectListener: "+res);
if(res.equals("1")){
    Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
    adapter.remove(section);
  notifyDataSetChanged();
    loadSectionList();
}
                }
            });

        }
        private void editRoutine(final String section,final String edit_view)
        {
            final SharedPreferences sp=getActivity().getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
            String link = FragmentCodes.NEW_HOST + "FetchRoutine.php";

            new PhpConnect(link,"Loading...",getActivity(),1,new String[]{FragmentCodes.CMDXXX,Utilities.getDatabaseName(getActivity()),section},new String[]{"cmdxxx","dbName","section_name"}).setListener(new PhpConnect.ConnectOnClickListener() {
                @Override
                public void onConnectListener(String res) {

                    int day=0;
                    try {
                        /*if(routineExists(section))
                            deleteRoutine(section);*/
                        SparseArray<List<RoutineInfo>> listSparseArray;
                        listSparseArray=new SparseArray<List<RoutineInfo>>();
                        for(int i=0;i<7;i++){
                            listSparseArray.put(i,new ArrayList<RoutineInfo>());
                        }
                        JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jO = jsonArray.getJSONObject(i);
                            try {
listSparseArray.get(Integer.parseInt(jO.getString("day"))-1).add(getRoutineinfo(jO));
                            // day=Integer.parseInt(jO.getString("day"));
                            } catch (Exception w) {
                                //Log.d("ViewRoutineFragment", w.toString());
                                // Toast.makeText(getActivity(), w.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        AddRoutineActivity.setSparseArray(listSparseArray);
Intent i=new Intent(getActivity(),AddRoutineActivity.class);
                        i.putExtra(TAG_SECTION_NAME,section);
                        i.putExtra(TAG_EDIT_VIEW,edit_view);
                        startActivity(i);
//setRoutine();
                    } catch (Exception e) {
                        //Log.d(" ViewRoutineFragment", e.toString());
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                    }
                   }
            });

        }
private RoutineInfo getRoutineinfo(JSONObject jo){
    RoutineInfo ri=new RoutineInfo();

    try {
        ri.setPeriodNo(jo.getString("period_no"));
        ri.setEnd(jo.getString("end"));
        ri.setStart(jo.getString("start"));
        ri.setSubject(jo.getString("subject"));
        ri.setTeacher(jo.getString("teacher_name"));
        ri.setSection(jo.getString("section_name"));
        ri.setDay(jo.getString("day"));
        ri.setCollegeSN(jo.getString("college_sn"));
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return ri;
}

    }
}

