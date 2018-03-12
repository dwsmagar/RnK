package com.susankya.schoolvalley;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;


public class AboutUsFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View[] views = new View[5];
    private String feedback;
    private static ProgressDialog dialog;
    private int[] id = {R.id.v5, R.id.v6, R.id.v7, R.id.v8, R.id.v9};
    private UploadFeedback uploadFeedback = new UploadFeedback();
    private String mParam1;
    private TextView feedbackInfo;
    private RelativeLayout rl;
    LinearLayout adiLL, aviLL, sanjogLL, piyushLL;
    private EditText et, etNAME;
    private String mParam2, name;
    private SharedPreferences sp;
    private String[] url = new String[]{"http://www.facebook.com/abhi.nav.338658", "http://www.facebook.com/eminem4aditya", "https://www.facebook.com/piyush.jaiswal.562", "http://www.facebook.com/sanjog.dhakal"};

    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("About Us");
    }

    public AboutUsFragment() {
        // Required empty public constructor
    }

    /* @Override
     public void onResume() {
         super.onResume();
         if(sp.getBoolean(SettingsFragment.SETTINGS_PROFILE,true))
             ((NavDrawerActivity) getActivity()).onSectionAttached(7);
         else
             ((NavDrawerActivity) getActivity()).onSectionAttached(8);

     }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dialog = new ProgressDialog(getActivity());
        sp = getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
    }

    private void sendIntent(int index) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url[index]));
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getCircularImage(int drawable, int radius) {
        return RoundedImageView.getCroppedBitmap(BitmapFactory.decodeResource(getActivity().getResources(), drawable), radius);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_about_us, container, false);
        ImageView adiCall = (ImageView) v.findViewById(R.id.adicall);
        ImageView aviCall = (ImageView) v.findViewById(R.id.avicall);
        ImageView sandyCall = (ImageView) v.findViewById(R.id.sandycall);
        ImageView piyushCall = (ImageView) v.findViewById(R.id.piyushcall);
       /*textViews.setText("We are a team of 3, but carry aims and goals of many. We think for the development and start doing it. We started as a small IT team in Biratnagar, Nepal but we will continue to expand and hope to serve the whole world.");
        */
        piyushCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+9779852031049"));
                startActivity(intent);
            }
        });
        adiCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+9779862027999"));
                startActivity(intent);
            }
        });
        aviCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+9779862026110"));
                startActivity(intent);
            }
        });
        sandyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+9779842493334"));
                startActivity(intent);
            }
        });
        ImageView avi = (ImageView) v.findViewById(R.id.arr2);
        ImageView sanjog = (ImageView) v.findViewById(R.id.arr1);
        ImageView adi = (ImageView) v.findViewById(R.id.arr3);

        ImageView piyush = (ImageView) v.findViewById(R.id.piyushimage);
        ImageView aviFB = (ImageView) v.findViewById(R.id.aviFB);
        ImageView adiFB = (ImageView) v.findViewById(R.id.adiFB);
        ImageView sandyFB = (ImageView) v.findViewById(R.id.sandyFB);
        ImageView piyushFB = (ImageView) v.findViewById(R.id.piyushFB);
        piyushFB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendIntent(3);
                    }
                }
        );
        aviFB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendIntent(0);
                    }
                }
        );
        adiFB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendIntent(1);
                    }
                }
        );
        sandyFB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendIntent(2);
                    }
                }
        );
        Bitmap b = getCircularImage(R.drawable.adi, SIZE);
        adi.setImageBitmap(b);

        Bitmap sanjogIMG = getCircularImage(R.drawable.sanjog, SIZE);
        sanjog.setImageBitmap(sanjogIMG);
        Bitmap aviIMG = getCircularImage(R.drawable.avi, SIZE);
        avi.setImageBitmap(aviIMG);

        Bitmap piyushIMG = getCircularImage(R.drawable.piyush, SIZE);
        piyush.setImageBitmap(piyushIMG);


        sanjogLL = (LinearLayout) v.findViewById(R.id.sanjog_ko_boli_ll);
        aviLL = (LinearLayout) v.findViewById(R.id.avi_ko_boli_ll);

        piyushLL = (LinearLayout) v.findViewById(R.id.piyush_ko_boli_ll);
        adiLL = (LinearLayout) v.findViewById(R.id.adi_ko_boli_ll);
        feedbackInfo = (TextView) v.findViewById(R.id.v10);
        rl = (RelativeLayout) v.findViewById(R.id.feedback);
        feedbackInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl.isShown()) {
                    rl.setVisibility(View.GONE);
                    feedbackInfo.setVisibility(View.VISIBLE);
                } else {
                    rl.setVisibility(View.VISIBLE);
                    feedbackInfo.setVisibility(View.GONE);
                }

            }
        });
        final Button sendFeedback = (Button) v.findViewById(R.id.sendFeedback);
        et = (EditText) v.findViewById(R.id.feedbackEditText);
        etNAME = (EditText) v.findViewById(R.id.nameEditText);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadFeedback = new UploadFeedback();
                    feedback = et.getText().toString().trim();
                    name = etNAME.getText().toString().trim();

                    if (feedback.isEmpty() || name.isEmpty())
                        Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    else
                        send(URLEncoder.encode(feedback, "utf-8"));
                } catch (Exception e) {
                    // Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll);
//        Animation animation= ((NavDrawerActivity)getActivity()).getAnimation();
        //  ll.setAnimation(animation);

        RelativeLayout Aditya = (RelativeLayout) v.findViewById(R.id.v8);
        RelativeLayout Abhinav = (RelativeLayout) v.findViewById(R.id.v7);
        RelativeLayout Sanjog = (RelativeLayout) v.findViewById(R.id.v6);
        RelativeLayout Piyush = (RelativeLayout) v.findViewById(R.id.piyush_RL);
        Aditya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adiLL.getVisibility() == View.GONE)
                    adiLL.setVisibility(View.VISIBLE);
                else
                    adiLL.setVisibility(View.GONE);
            }
        });
        Abhinav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aviLL.getVisibility() == View.GONE)
                    aviLL.setVisibility(View.VISIBLE);
                else
                    aviLL.setVisibility(View.GONE);
            }
        });
        Sanjog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sanjogLL.getVisibility() == View.GONE)
                    sanjogLL.setVisibility(View.VISIBLE);
                else
                    sanjogLL.setVisibility(View.GONE);
            }
        });
        Piyush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (piyushLL.getVisibility() == View.GONE)
                    piyushLL.setVisibility(View.VISIBLE);
                else
                    piyushLL.setVisibility(View.GONE);
            }
        });
        for (int i = 0; i < views.length; i++) {
            views[i] = v.findViewById(id[i]);
        }
        for (int i = 0; i < views.length; i++) {
            final int time = i * 300;
            views[i].setAlpha(0f);
            views[i].setVisibility(View.VISIBLE);
            views[i].animate().alpha(1f).setDuration(time);

        }
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.open_up);
        views[0].setAnimation(animation1);

        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                views[0].animate()
                        .alpha(0f)
                        .setDuration(1000)//time
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                views[0].setVisibility(View.GONE);
                            }
                        });
            }
        });
        return v;
    }


    private void send(String feedback) throws Exception {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                uploadFeedback.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, feedback);
            } else {
                uploadFeedback.execute(feedback);
            }

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "NO CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

    private class UploadFeedback extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String encodedName = URLEncoder.encode(name, "utf-8");
                String link = HOST + "Feedback/InsertFeedback.php";
                //Log.d("LINK",link);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost(link);
                request.setEntity(
                        Utilities.getPostParameters(
                                new String[]{name, feedback},
                                new String[]{"email", "feedback"}
                        )
                );
                HttpResponse response = client.execute(request);
                String s = EntityUtils.toString(response.getEntity());

                return s;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        private String e(String s) throws Exception {
            return URLEncoder.encode(s, "utf-8");
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result.equals("1")) {
                Toast.makeText(getActivity(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                // Toast.makeText(getActivity(),"Try gar feri",Toast.LENGTH_SHORT).show();

            }
            et.setText("");
            etNAME.setText("");
            rl.setVisibility(View.GONE);
            feedbackInfo.setVisibility(View.VISIBLE);
        }
    }

}
