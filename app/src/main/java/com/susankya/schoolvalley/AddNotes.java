package com.susankya.schoolvalley;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddNotes extends android.support.v4.app.Fragment implements FragmentCodes {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int mParam1;
    private int hasImage=0;

    public static BitmapDrawable getBd() {
        return bd;
    }

    public static void setBd(BitmapDrawable bd) {
        AddNotes.bd = bd;
    }

    private static BitmapDrawable bd;


    private ImageView image;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;
private String Title="",Note="",date="",time="";
    private Uri selectedImage;
    String selectedImageLocation="";
private boolean hasGoneToSelectImage;
    public static AddNotes newInstance(int param1) {
        AddNotes fragment = new AddNotes();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AddNotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasImage=0;
        selectedImageLocation="hell";
        sqLiteHelper=new SQLiteHelper(getActivity());
        db=sqLiteHelper.getWritableDatabase();
        if (getArguments() != null) {
            try {
                mParam1 = getArguments().getInt(ARG_PARAM1);
                Cursor c = db.query(TABLE_NOTES, new String[]{C_SN,C_TITLE, C_NOTE, C_TIME, C_DATE, C_POST_PIC_LOCATION, C_HASIMAGE}, C_SN + "= ? ", new String[]{mParam1 + ""}, null, null, null);
                while (c.moveToNext()) {
                    Title = c.getString(c.getColumnIndex(C_TITLE));
                    Note = c.getString(c.getColumnIndex(C_NOTE));
                    date = c.getString(c.getColumnIndex(C_DATE));
                    time = c.getString(c.getColumnIndex(C_TIME));
                    selectedImageLocation = c.getString(c.getColumnIndex(C_POST_PIC_LOCATION));
                    hasImage = c.getInt(c.getColumnIndex(C_HASIMAGE));
                }
                db.delete(TABLE_NOTES,C_SN + "= ? ", new String[]{mParam1 + ""});
            }catch (Exception  e){
        //    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        hasGoneToSelectImage=false;
         getActivity().setTitle("Add Note");
        try{
            db.execSQL("create table "+TABLE_NOTES+" ("+C_SN+" integer primary key autoincrement, "+C_TITLE+" varchar(50), "+C_NOTE+" varchar, "+
                    C_TIME+" varchar(50), "+C_DATE+" varchar(50), "+C_HASIMAGE+" integer, "+C_POST_PIC_LOCATION+" varchar, "+C_PIC_LOCATION+" varchar(100));");
        }catch (Exception e){
       // Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
hasGoneToSelectImage=false;
        switch(requestCode) {
            case FragmentCodes.SELECT_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    hasImage=1;
                    selectedImage = imageReturnedIntent.getData();
                    File f=new File(getPath(selectedImage));
                    selectedImageLocation=selectedImage.toString();
                   // Toast.makeText(getActivity(),selectedImageLocation,Toast.LENGTH_SHORT).show();
                    double sizeInKB=f.length()/1024;
                    BitmapDrawable b= PictureUtils.getScaledDrawable(getActivity(),getPath(selectedImage));
                 image.setVisibility(View.VISIBLE);
                    image.setImageDrawable(b);
                        }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_add_notes, container, false);
        final TextView title=(EditText)v.findViewById(R.id.title);
        TextView note=(EditText)v.findViewById(R.id.note);
        image=(ImageView)v.findViewById(R.id.showImage);
        title.setText(Title);
           note.setText(Note);
        if (hasImage==1){
        image.setVisibility(View.VISIBLE);
        BitmapDrawable c = PictureUtils.getScaledDrawable(getActivity(),getPath(Uri.parse(selectedImageLocation)));
        image.setVisibility(View.VISIBLE);
        image.setImageDrawable(c);
        }
image.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        setBd(PictureUtils.getScaledDrawable(getActivity(),getPath(Uri.parse(selectedImageLocation))));
        ImageZoomerDialog imageZoomerDialog= ImageZoomerDialog.newInstance(ImageZoomerDialog.NOTES);
        imageZoomerDialog.show(getFragmentManager(),"ABCD");
    }
});
        ImageView addImage=(ImageView)v.findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasGoneToSelectImage=true;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, FragmentCodes.SELECT_PHOTO);
            }
        });
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            Title=s.toString();
                if(Title.length()>=20){
               title.setText(s.toString().substring(0,20));
            Toast.makeText(getActivity(),"Title can not be more than 20 characters",Toast.LENGTH_SHORT).show();
                }}

            @Override
            public void afterTextChanged(Editable s) {
Title=s.toString();
            }
        });
        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Note=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Note=s.toString();
            }
        });
    return  v;
    }
    @Override
    public void onPause() {
if(!hasGoneToSelectImage){
  //  Toast.makeText(getActivity(),"reached",Toast.LENGTH_SHORT).show();
if(!(Title.equals("")&&Note.equals(""))){
        try {
            Calendar c=Calendar.getInstance();
            final SimpleDateFormat d1 = new SimpleDateFormat("hh:mm a");
            final SimpleDateFormat d2 = new SimpleDateFormat("MMM dd");
            String time=d1.format(c.getTime());
            String date=d2.format(c.getTime());
            ContentValues cv = new ContentValues();
            cv.put(C_TITLE, Title.equals("")?"Untitled":Title);
            cv.put(C_NOTE, Note);
            cv.put(C_TIME,time);
            cv.put(C_DATE, date);
          cv.put(C_HASIMAGE,hasImage);
          cv.put(C_POST_PIC_LOCATION,selectedImageLocation);
          db.insert(TABLE_NOTES, null, cv);
           // Toast.makeText(getActivity(),"done"+db.insert(TABLE_NOTES, null, cv),Toast.LENGTH_SHORT).show();
        }catch (Exception e)  {
        //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
}
}
        super.onPause();
    }
}
