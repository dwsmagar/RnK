package com.susankya.schoolvalley;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by ajay on 11/28/2015.
 */
public class ImageZoomerDialog extends DialogFragment {
    public static String WHICH_ACTIVITY="KUN ACTIVITY HO RE VAE?";
    int activity;
   final public static int INSIDE_ACTIVITY=1;
    final public static int FRAGMENT_NEWS_ACTIVITY=2;
    final public static int NOTES=3;
    TouchImageView touchImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        activity=getArguments().getInt(WHICH_ACTIVITY);
        }



    }

    public static ImageZoomerDialog newInstance(int param1) {
        ImageZoomerDialog fragment = new ImageZoomerDialog();
        Bundle args = new Bundle();
        args.putInt(WHICH_ACTIVITY, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((int) (width * 1), (int) (height * 0.9));
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {

        View v=inflater.inflate(R.layout.image_zoomer_dialog_layout,null);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((int)(width * 1), (int)(height * 0.9));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

       touchImageView=(TouchImageView)v.findViewById(R.id.zoom);
        boolean isBitmap=true;
        Bitmap b=null;
        BitmapDrawable bd=null;
        switch (activity)
        {

            case NOTES:
            isBitmap=false;
                bd=AddNotes.getBd();
            default:
                break;
        }
        if(isBitmap)
        touchImageView.setImageBitmap(b);
       else
        touchImageView.setImageDrawable(bd);
        return v;
    }
}
