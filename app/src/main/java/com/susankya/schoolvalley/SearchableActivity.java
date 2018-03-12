package com.susankya.schoolvalley;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SearchableActivity extends AppCompatActivity {

    Socket socket;
    JSONObject jsonObject;
    ArrayList<SearchItem> searchItems=new ArrayList<>();
    ListView searchlv;
    EditText searchboy;
    SearchAdapter searchAdapter;
    View holderLL,searcherLL,noResultLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchlv=(ListView)findViewById(R.id.search_lv);
        searchboy=(EditText) findViewById(R.id.searchboy);
        holderLL=findViewById(R.id.search_holder_ll);
        searcherLL=findViewById(R.id.searching_process_ll);
        noResultLL=findViewById(R.id.no_search_results_ll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jsonObject=new JSONObject();
        searchAdapter=new SearchAdapter(getApplicationContext(),searchItems);
        searchlv.setAdapter(searchAdapter);
        displayHolder();
        searchboy.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if(s.toString().length()>1)
                        {
                            byeHolder();
                            try {
                                makeJSON(s.toString());
                            }
                            catch (Exception e)
                            {
                                //Log.d("checker",e.toString());
                            }

                        }
                        else if(s.toString().isEmpty())
                        {
                            displayHolder();
                            searchItems.clear();
                            searchAdapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );

        searchlv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i=new Intent(SearchableActivity.this,SchoolProfileActivity.class);
                        Bundle b=new Bundle();
                        b.putString("name",searchItems.get(position).name);
                        b.putString("location",searchItems.get(position).location);
                        b.putString("SN",searchItems.get(position).collegeSN+"");
                        b.putString("url",searchItems.get(position).URL);
                        b.putString("dbName",searchItems.get(position).dbname);
                        i.putExtras(b);
                        startActivity(i);
                    }
                }
        );

    }


    public void displayHolder()
    {
        holderLL.setVisibility(View.VISIBLE);
        searchlv.setVisibility(View.GONE);
        searcherLL.setVisibility(View.GONE);
        noResultLL.setVisibility(View.GONE);
    }

    public void byeHolder()
    {
        holderLL.setVisibility(View.GONE);
        searchItems.clear();
        searchAdapter.notifyDataSetChanged();
        searchlv.setVisibility(View.VISIBLE);
        searcherLL.setVisibility(View.VISIBLE);
        noResultLL.setVisibility(View.GONE);

    }

    public void populate()
    {
        SearchItem s=new SearchItem();
        s.name="Checking School Abc";
        s.location="Cloadasd";
        searchItems.add(s);
        searchItems.add(s);
    }

    public void loadIntoArray(String q)
    {

        try{
            JSONArray jsonArray=new JSONArray(q);
            //Log.d("qvalue",q);
            //Log.d("jso",jsonArray.length()+"");

            searchItems.clear();
            for (int i=0;i<jsonArray.length();i++)
            {
                SearchItem searchItem=new SearchItem();
                JSONObject j=jsonArray.getJSONObject(i);
                String sName=j.getString("college_name");
                String sLocation=j.getString("location");
                String sDBname=j.getString("database_name");

                int sn=j.getInt("sn");
                String sUsername=j.getString("username");

                searchItem.name=sName;
                searchItem.location=sLocation;
                searchItem.collegeSN=sn;
                searchItem.username=sUsername;
searchItem.dbname=sDBname;
                searchItems.add(searchItem);
            }
            //Log.d("searchItemsL",searchItems.size()+"");
            SearchableActivity.this.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                           searcherLL.setVisibility(View.GONE);
                           searchAdapter.notifyDataSetChanged();

                            if(searchItems.isEmpty())
                                noResultLL.setVisibility(View.VISIBLE);

                        }
                    }
            );
        }
        catch (Exception e)
        {
            //Log.d("errorawdaexa",e+"");
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            try
            {
                makeJSON(query);
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }

        }
    }

    void makeJSON(String query) throws Exception
    {
        jsonObject.put("keywords",query);
        call();
    }

    void call()
    {
        try
        {
            socket = IO.socket("http://susankya.com:70/");
            socket.emit("client_data",jsonObject);
            socket.on("message", new Emitter.Listener() {

                        @Override
                        public void call(final Object... args) {
                            try {
                                //Log.d("checker",args[0].toString());
                                loadIntoArray(args[0].toString());

                            } catch (Exception e) {
                               //Log.d("errorxa",e.toString());
                                // tv.setText(e.toString());
                            }

                        }

                    }

            ).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                                        @Override
                                        public void call(Object... args) {
                                        }

                                    }

                            );
            socket.connect();
        }
        catch (Exception e)
        {
        }

    }

    public class SearchAdapter extends BaseAdapter
    {

        ArrayList<SearchItem> myList = new ArrayList<> ();
        LayoutInflater inflater;
        Context context;


        public SearchAdapter(Context context, ArrayList<SearchItem> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return searchItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public Object getItem(int position) {
            return searchItems.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView=inflater.inflate(R.layout.search_item_view,null,false);
            TextViewPlus name=(TextViewPlus)convertView.findViewById(R.id.school_name);
            TextViewPlus    location=(TextViewPlus)convertView.findViewById(R.id.school_location);
            name.setCustomFont(getApplicationContext(),FragmentCodes.REGULAR);
            name.setText(searchItems.get(position).name);
            location.setText(searchItems.get(position).location);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }


}
