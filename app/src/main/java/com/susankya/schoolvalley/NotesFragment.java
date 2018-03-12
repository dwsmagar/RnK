package com.susankya.schoolvalley;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class NotesFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private ListView list;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;
    private FloatingActionButton add_notes;
    AdapterForNotes afn;
    private ArrayList<NotesInfo> notesInfos;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
getActivity().setTitle("Notes");
    }
    public NotesFragment() {
        // Required empty public constructor
    }

 /*     public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getActivity().getMenuInflater();
     //   inflater.inflate(R.menu.add_notes, menu);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                try {
                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new AddNotes()).addToBackStack(null).commit();
                }
                catch (Exception e){
                    //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        sqLiteHelper=new SQLiteHelper(getActivity());
        db=sqLiteHelper.getWritableDatabase();
        notesInfos=new ArrayList<>();
    }
    void AddNotes(){
        try {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new AddNotes()).addToBackStack(null).commit();
        }
        catch (Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
private void getList(){
    try{
        Cursor c=db.query(TABLE_NOTES,new String[]{C_TITLE,C_NOTE,C_DATE,C_TIME},null,null,null,null,null);
        notesInfos.clear();
        while(c.moveToNext()){
      //      Toast.makeText(getActivity(),"here",Toast.LENGTH_SHORT).show();
           NotesInfo ni=new NotesInfo();
            int i=c.getColumnIndex(C_NOTE);
            int j=c.getColumnIndex(C_TITLE);
            int k=c.getColumnIndex(C_DATE);
            int l=c.getColumnIndex(C_TIME);
            String title=c.getString(j);
            String note=c.getString(i);
            String date=c.getString(k);
            String time=c.getString(l);
            ni.setDate(date);
            ni.setNote(note);
            ni.setTime(time);
            ni.setTitle(title);
            notesInfos.add(ni);
        }
        Collections.reverse(notesInfos);
    }
    catch( Exception e){
     //    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
    }
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_notes, container, false);
        list=(ListView)v.findViewById(R.id.list);
        TextView tv=(TextView)v.findViewById(R.id.emptyView);
        list.setEmptyView(tv);
        add_notes=(FloatingActionButton)v.findViewById(R.id.floating_add_button);
        getList();
        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNotes();
            }
        });
        afn=new AdapterForNotes(notesInfos);
        list.setAdapter(afn);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotesInfo ni= (NotesInfo) parent.getAdapter().getItem(position);
                Cursor c=db.query(TABLE_NOTES,new String[]{C_SN},C_TITLE+"= ? and "+C_NOTE+"= ? ",new String[]{ni.getTitle(),ni.getNote()},null,null,null);
                while (c.moveToNext()){
                    int sn=c.getInt(c.getColumnIndex(C_SN));
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, AddNotes.newInstance(sn)).addToBackStack(null).commit();
                }
            }
        });
        return v;
    }
    private class  AdapterForNotes extends ArrayAdapter<NotesInfo>{

        public AdapterForNotes(ArrayList<NotesInfo> ni) {
            super(getActivity(), 0,ni);
        }

        @Override
        public View getView(final int pos,View v,ViewGroup vg){
            if(v==null){
                v=getActivity().getLayoutInflater().inflate(R.layout.notes_list_item,null);
            }
         final   NotesInfo ni=getItem(pos);
            ImageView delete=(ImageView)v.findViewById(R.id.deleteNote);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setTitle("Delete");
                    builder1.setMessage("Are you sure you want to delete this note?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        db.delete(TABLE_NOTES, C_TITLE + "= ? and " + C_NOTE + "= ? ", new String[]{ni.getTitle(), ni.getNote()});
                                    }
catch (Exception e){
   // Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
}
                                    getList();
                                        afn.notifyDataSetChanged();
                                }
                            });   builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    return;
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });
         TextView title=(TextView)v.findViewById(R.id.title);
         TextView note=(TextView)v.findViewById(R.id.note);
         TextView date=(TextView)v.findViewById(R.id.date);
         TextView time=(TextView)v.findViewById(R.id.time);
            title.setText(ni.getTitle());
            date.setText(ni.getDate());
            time.setText(ni.getTime());
            if(ni.getNote().length()<=25)
            note.setText(ni.getNote());
            else
            note.setText(ni.getNote().substring(0,25)+"...");
            return v;
        }

    }
    private class  NotesInfo{
        private String title,note,time,date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
 }
