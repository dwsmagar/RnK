package com.susankya.rnk;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class PublishedResultList extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private AskRoll dialog;
    private String filterString;
private ListView result_list;
    private TextView emptyView;

    ProgressBar loadingPB;
    private adapt result_adapter;
    private ArrayList<ResultDetail> resultDetails;
    private ArrayList<ResultDetail> tempresultDetails;
     public static PublishedResultList newInstance(String param1, String param2) {
        PublishedResultList fragment = new PublishedResultList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PublishedResultList() {

    }
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onResume() {
        super.onResume();
        //getActivity().setTitle("Result");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search,menu);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_published_result_list, container, false);
        result_list=(ListView)v.findViewById(R.id.list_result);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);
                        fetchResult();
                    }
                }
        );
        NavDrawerActivity.getSearchEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isVisible()){
                filterString=s.toString();
                tempresultDetails=new ArrayList<ResultDetail>();
                for(ResultDetail temp:resultDetails){
                    if(temp.getResult_title().toLowerCase().contains(filterString.toLowerCase()))
                        tempresultDetails.add(temp);
                }

                result_adapter=new adapt(tempresultDetails);
                result_list.setAdapter(result_adapter);}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setHasOptionsMenu(true);
        loadingPB=(ProgressBar)v.findViewById(R.id.progress);
        emptyView=(TextView)v.findViewById(R.id.emptyView);
        resultDetails = new ArrayList<>();
        result_adapter=new adapt(resultDetails);
        fetchResult();
        return  v;
    }


    void fetchResult()
    {
        String link=MAIN_DATABASE+"resultListing.php";
        link= Utilities.makeUrl(link);

        if(Utilities.isConnectionAvailable(getActivity())){
            if(Utilities.getDatabaseName(getActivity()).equals("none"))
            {
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("No institute selected");
                loadingPB.setVisibility(View.GONE);
                InstituteNotSelected instituteNotchosenSialod=new InstituteNotSelected();
                instituteNotchosenSialod.show(getFragmentManager(),"showing");
            }else{
                if(!swipeRefreshLayout.isRefreshing())
                loadingPB.setVisibility(View.VISIBLE);
                new PhpConnect(link, "Loading...", getActivity(),0, new String[]{CMDXXX, UtilitiesAdi.giveMeSN(getContext(),Utilities.getDatabaseName(getContext()))}, new String[]{"cmdxxx","college_sn"}).setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        JSONArray array = null;
                        try {


                            resultDetails.clear();
                            array = (JSONArray) new JSONTokener(res)
                                    .nextValue();
                            for (int i = 0; i < array.length(); i++) {
                                ResultDetail RD=new ResultDetail();
                                JSONObject job=array.getJSONObject(i);
                                RD.setSn(job.getString("sn"));
                                RD.setResult_title(job.getString("result_title"));
                                RD.setSearch_column(job.getString("search_column"));
                                RD.setTable_name(job.getString("table_name"));
                                RD.setDate(job.getString("date"));
                                try
                                {
                                    RD.section=job.getString("section");
                                }
                                catch (Exception e)
                                {

                                }

                                resultDetails.add(RD);
                            }


                        } catch (Exception e) {
                        }
                        loadingPB.setVisibility(View.GONE);
                        result_adapter=new adapt(resultDetails);
                        swipeRefreshLayout.setRefreshing(false);
                        result_list.setEmptyView(emptyView);
                        result_list.setAdapter(result_adapter);
                        result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final ResultDetail rd= (ResultDetail) parent.getAdapter().getItem(position);
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                dialog= new AskRoll().newInstance(rd.getTable_name(), rd.getSearch_column());
                                dialog.show(fragmentManager, "SHOWING");
                            }
                        });
                    }
                });}}

        else {
            loadingPB.setVisibility(View.GONE);
            emptyView.setText("No Internet Connection.");
            result_list.setEmptyView(emptyView);
            //result_list.setAdapter(result_adapter);
             }
    }
      private class adapt extends ArrayAdapter<ResultDetail> {
        private ArrayList<ResultDetail> profile;
        public adapt(ArrayList<ResultDetail> c) {
            super(getActivity(), 0, c);
            profile = c;
        }

        @Override
        public View getView(final int pos, View v, ViewGroup vg) {
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.published_result_list_item, null);
            }
            TextViewPlus title,section,date,phtv;
            GradientDrawable sd;
               title=(TextViewPlus) v.findViewById(R.id.title_content);
            section=(TextViewPlus)v.findViewById(R.id.section_content);
            date=(TextViewPlus)v.findViewById(R.id.date_content);
            phtv=(TextViewPlus)v.findViewById(R.id.ph_tv);

            sd=(GradientDrawable) phtv.getBackground().mutate();
            sd.setColor(getActivity().getResources().getColor(R.color.result_color));
           phtv.setBackground(sd);
            ResultDetail Rd=getItem(pos);
            String detail="";
            phtv.setText(Rd.getResult_title().substring(0,1));

            title.setText(Rd.getResult_title());
            date.setText(Rd.getDate());
            section.setText(Rd.getSection());
            date.setText(Rd.getDate());

            return v;
        }

    }
    public static class AskRoll extends android.support.v4.app.DialogFragment {
        public  AskRoll(){
        }
        private String colName,tableName;
        private Button okButton,cancelButton;
        private EditText roll;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        }
        public AskRoll newInstance(String tablename,String colName){
            AskRoll dialog=new AskRoll();
            Bundle b=new Bundle();
            b.putString("tableName",tablename);
            b.putString("colName",colName);
            dialog.setArguments(b);
            return dialog;
        }
         public void onResume()
        {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            //getDialog().getWindow().setLayout((14 * width)/15, (3 * height)/10);

            super.onResume();
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            final View v = inflater.inflate(R.layout.ask_roll, null, false);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            roll=(EditText)v.findViewById(R.id.roll);
            okButton=(Button)v.findViewById(R.id.ok);
            cancelButton=(Button)v.findViewById(R.id.cancel);
            colName=getArguments().getString("colName").trim();
            tableName=getArguments().getString("tableName").trim();
            ((TextView)v.findViewById(R.id.header)).setText("Please enter your "+colName.replace("_"," ")+" below:");
           // Toast.makeText(getActivity(),colName +" "+tableName, Toast.LENGTH_SHORT).show();
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String value=roll.getText().toString().trim();
                    String link=MAIN_DATABASE+"FetchResult.php";
                    link= Utilities.makeUrl(link);
                    new PhpConnect(link,"Loading...",getActivity(),1,new String[]{tableName,CMDXXX,colName,String.valueOf(value)},new String[]{"tableName","cmdxxx","colName","value"}).setListener(new PhpConnect.ConnectOnClickListener() {
                        @Override
                        public void onConnectListener(String res) {
                       if(!res.equals("0")){
                            Intent i=new Intent(getActivity(),ResultActivity.class);
                            i.putExtra(ResultActivity.RESULT,res);
                            i.putExtra(ResultActivity.NAME,tableName);
                            i.putExtra(ResultActivity.ROLL,colName);
                            i.putExtra(ResultActivity.ROLLVALUE,value);
                            startActivity(i);
                           getDialog().dismiss();
                           }
                            else {
                               Toast.makeText(getActivity(),"Could not find your result. Make sure you typed the correct "+colName,Toast.LENGTH_LONG).show();
                            getDialog().dismiss();
                            }
                        }
                    });
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
            return v;
        }
       }

    private class ResultDetail{
     private String sn;
        private String code;
        private String _class;

        /*[
{
"sn": "5",
"college_sn": "24",
"table_name": "24_1488182859",
"result_title": "ABCDEF",
"search_column": "roll",
"section": "xyz",
"date": "2017 02 27",
"time": "13:52:41 pm"
},*/

        private String table_name;

        public String getSearch_column() {
            return search_column;
        }

        public void setSearch_column(String search_column) {
            this.search_column = search_column;
        }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public String getResult_title() {
            return result_title;
        }

        public void setResult_title(String result_title) {
            this.result_title = result_title;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        private String search_column;
        private String result_title;
        private String faculty;
private String date;
        private String section;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String get_class() {
            return _class;
        }

        public void set_class(String _class) {
            this._class = _class;
        }

        public String getFaculty() {
            return faculty;
        }

        public void setFaculty(String faculty) {
            this.faculty = faculty;
        }

        public String getExam_type() {
            return exam_type;
        }

        public void setExam_type(String exam_type) {
            this.exam_type = exam_type;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getRoll() {
            return roll;
        }

        public void setRoll(String roll) {
            this.roll = roll;
        }

        private String level;
        private String exam_type;
        private String term;
        private String roll;
    }
}
