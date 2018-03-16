package com.susankya.rnk;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewSectionsListFragment extends Fragment implements FragmentCodes{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ViewSectionsListFragment() {
        // Required empty public constructor
    }

   @BindView(R.id.sections_list)ListView sectionsLV;
    public ArrayList<String> sections=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    SectionListAdapter sectionListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_view_sections_list, container, false);
        ButterKnife.bind(this,v);
        sectionListAdapter=new SectionListAdapter();
        sectionsLV.setAdapter(sectionListAdapter);
        sectionsLV.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                }
        );
        loadSections();
        return v;
    }

    void loadSections()
    {
        String link=NEW_HOST+"SectionName.php";
        new PhpConnect(link,"",getActivity(),0,new String[]{CMDXXX, UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity()))},new String[]{"cmdxxx","college_sn"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                try {
                    //Log.d("TAGabcd", "onConnectListener:"+res);
                    JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject job = jsonArray.getJSONObject(i);
                        sections.add(job.getString("section_name"));

                    }

                    sectionListAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    //Log.d("TAGabcd", "onConnectListener: "+e.toString());
                }
            }
        });

    }

    public class SectionListAdapter extends BaseAdapter
    {
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public Object getItem(int position) {
            return sections.get(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getCount() {
            return sections.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextViewPlus section;
            if (convertView==null)
            {
                convertView=getActivity().getLayoutInflater().inflate(R.layout.section_item_view,parent,false);

            }
            section=(TextViewPlus)convertView.findViewById(R.id.sectiontv);
            section.setText(sections.get(position));
            return  convertView;

        }
    }
}
