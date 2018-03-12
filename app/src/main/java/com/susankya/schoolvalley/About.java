package com.susankya.schoolvalley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class About extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View[] views = new View[12];
    private SharedPreferences sp;

    private String mParam1;
    private String mParam2;

    //private int[] id={R.id.b1,R.id.b2,R.id.b3,R.id.b4,R.id.b5,R.id.b6,R.id.b7,R.id.b8,R.id.b9,R.id.b10,R.id.b11,R.id.b12};
    public static About newInstance(String param1, String param2) {
        About fragment = new About();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public About() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sp.getBoolean(SettingsFragment.SETTINGS_PROFILE, true))
            ((NavDrawerActivity) getActivity()).onSectionAttached(5);
        else
            ((NavDrawerActivity) getActivity()).onSectionAttached(6);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sp = getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int t = 0;
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll);
        Animation animation = ((NavDrawerActivity) getActivity()).getAnimation();
        ll.setAnimation(animation);
        ImageView image = (ImageView) v.findViewById(R.id.image);
        Animation jump = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_top);
        image.setAnimation(jump);
        return v;
        //aditya jasto baklol kai xaina

    }

    private class AddNotice extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected String doInBackground(String... arg) {

            try {
                String link = HOST + "AddRoutine/AddRoutine.php";
                //Log.d("link",link);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost(link);
                String values[] = new String[]{"Sunday", "Pascal", "1", "2", "3", "4", "5", "6", "7", "8"};
                String placeholder[] = new String[]{"day", "section", "first", "second", "third", "forth", "fifth", "sixth", "seventh", "eighth"};

                request.setEntity(Utilities.getPostParameters(values, placeholder));
                HttpResponse response = client.execute(request);
                String s = EntityUtils.toString(response.getEntity());
                //Log.d("DDD","SSS");
                return s;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Adding...");
            dialog.show();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Log.d("yy",result);
            if (dialog.isShowing()) {
                Toast.makeText(getActivity(), "Re baklol", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

            if (result.equals("1"))
                Toast.makeText(getActivity().getApplicationContext(), "Notice added successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity().getApplicationContext(), "Failed to add notice", Toast.LENGTH_SHORT).show();
        }
    }
}
