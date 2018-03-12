package com.susankya.schoolvalley;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SettingsFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static AskRoutine dialog;
    public static final String HASSETROUTINE = "routine set garyo ta?";
    public static final String SECTION = "section kaun sa hai be tera?";
    public static final String SETTINGS_NOTICE = "com.timex.greenland.settings_notification";
    public static final String SETTINGS_SHIFT = "Bihana_awne_hos_ta_ki_din_ma?";
    public static final String SETTINGS_PROFILE = "Profile_for_teacher_and_student";
    public static final String SETTINGS_NOTIFICATION = "are_bhai_asti_varkhar_forum_banako_haina?";
    public static final String SETTINGS_IMAGE = "re vae image on rakhxas ki nae? data dherae khanxa off gar";
    SQLiteHelper sqLiteHelper;
    private static SQLiteDatabase db;
    private static ToggleButton tb_shift, tb_profile;
    private String mParam1;
    private String mParam2;
    private static TextView profile_title, notification_title, notification_description;
    private Switch loadImages;
    private static SharedPreferences sp = null;
    private static PhpConnect getRoutine;
    private static Switch aSwitchForNotice, aSwitchForNotification;

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Settings");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("Settings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        try {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView description = (TextView) v.findViewById(R.id.description);
            title.setText("Notice Alert");
            loadImages = (Switch) v.findViewById(R.id.image_switch);
            tb_profile = (ToggleButton) v.findViewById(R.id.toggleButton_profile);
            description.setText("Receive push notifications for new notices.");
            aSwitchForNotice = (Switch) v.findViewById(R.id.switch1);
            final TextView title_shift = (TextView) v.findViewById(R.id.title_shift);
            title_shift.setText("Shift");
            TextView description_shift = (TextView) v.findViewById(R.id.description_shift);
            description_shift.setText("Update routine widget smartly according to your shift.");
            tb_shift = (ToggleButton) v.findViewById(R.id.toggleButton_shift);
            tb_shift.setTextOff("Day");
            tb_shift.setTextOn("Morning");
            sp = getActivity().getSharedPreferences("Public", Context.MODE_PRIVATE);
            tb_shift.setChecked(sp.getBoolean(SETTINGS_SHIFT, true));
            aSwitchForNotice.setChecked(sp.getBoolean(SETTINGS_NOTICE, true));
            loadImages.setChecked(getActivity().getSharedPreferences("imageOn", Context.MODE_PRIVATE)
                    .getBoolean(SETTINGS_IMAGE, true));
            //Toast.makeText(getActivity(),String.valueOf(sharedPref.getBoolean("com.timex.greenland.settings_notification",true)),Toast.LENGTH_SHORT).show();
            loadImages.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            SharedPreferences sp = getActivity().getSharedPreferences("imageOn", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            if (isChecked) {

                                editor.putBoolean(SETTINGS_IMAGE, true);

                            } else {
                                editor.putBoolean(SETTINGS_IMAGE, false);

                            }
                            editor.commit();
                        }
                    }
            );
            aSwitchForNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!NewNoticeService.isServiceAlarmOn(getActivity()))
                            NewNoticeService.setServiceAlarm(getActivity(), true);
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean(SETTINGS_NOTICE, true).apply();
                    } else {
                        if (NewNoticeService.isServiceAlarmOn(getActivity()))
                            NewNoticeService.setServiceAlarm(getActivity(), false);
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean(SETTINGS_NOTICE, false).apply();
                    }
                }
            });
            tb_shift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateRoutineWidget(getActivity());
                    if (isChecked) {
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean(SETTINGS_SHIFT, true).apply();

                    } else {
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean(SETTINGS_SHIFT, false).apply();
                    }
                }
            });

            profile_title = (TextView) v.findViewById(R.id.title_profile);
            profile_title.setText("Switch profile");
            final TextView profile_description = (TextView) v.findViewById(R.id.description_profile);
            profile_description.setText("Turn on Admin Login feature (for teachers)");
            tb_profile.setTextOn("Student");
            tb_profile.setTextOff("Teacher");
            tb_profile.setChecked(sp.getBoolean(SETTINGS_PROFILE, true));
            tb_profile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean(SETTINGS_PROFILE, true).apply();
                        tb_profile.setChecked(true);
                    } else {

                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean(SETTINGS_PROFILE, false).apply();
                        tb_profile.setChecked(false);
                    }
                }
            });

            notification_title = (TextView) v.findViewById(R.id.title_notification);
            notification_description = (TextView) v.findViewById(R.id.description_notification);
            notification_title.setText("Notification Alert");
            notification_description.setText("Receive push notifications for Chhalfal.");
            aSwitchForNotification = (Switch) v.findViewById(R.id.switch1_notification);
            aSwitchForNotification.setChecked(sp.getBoolean(SETTINGS_NOTIFICATION, true));

        } catch (Exception e) {
            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        RelativeLayout get_routine = (RelativeLayout) v.findViewById(R.id.routineRL);
        get_routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHelper = new SQLiteHelper(getActivity());
                db = sqLiteHelper.getWritableDatabase();
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                dialog = new AskRoutine();
                dialog.show(fragmentManager, "SHOWING");
            }
        });

        return v;
    }

    private void updateRoutineWidget(Context context) {
        try {
            AppWidgetManager.getInstance(context);
            Intent intent = new Intent(RoutineWidget.FORCE_UPDATE);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static class askPin extends android.support.v4.app.DialogFragment {
        public static final String UNIT = "unit";
        private EditText pin;
        private Button ok, cancel;
        private static boolean shouldChange = true;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
            View v = inflater.inflate(R.layout.ask_teacher_pin, null, false);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            //getDialog().getWindow().setTitle("Add Question:");
            // tb_profile.setChecked(true);
            pin = (EditText) v.findViewById(R.id.pin);
            ok = (Button) v.findViewById(R.id.ok_button);
            cancel = (Button) v.findViewById(R.id.cancel_button);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shouldChange = false;
                    tb_profile.setChecked(true);
                    dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shouldChange = false;
                    if (pin.getText().toString().equals("545")) {
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean(SETTINGS_PROFILE, false).apply();
                        tb_profile.setChecked(false);
                        dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Wrong pin!", Toast.LENGTH_SHORT).show();
                        tb_profile.setChecked(true);
                        dismiss();
                    }
                }
            });

            return v;
        }

        @Override
        public void onDestroyView() {
            try {
                if (shouldChange)
                    tb_profile.setChecked(true);
            } catch (Exception e) {
//Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            super.onDestroyView();
        }

    }

    public static class AskRoutine extends android.support.v4.app.DialogFragment {
        public AskRoutine() {

        }

        public AskRoutine(EditText[] periods_et, TextView[] periods, int mParam) {
            this.mParam1 = mParam;
            this.periods_edit = periods_et;
            this.periods = periods;
        }

        private EditText[] periods_edit;
        private TextView[] periods;
        private String str;
        private String[] class11_sections, class12_sections, classes = new String[]{"11", "12"}, daysInWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        ;
        private Button okButton;
        private int mParam1;
        private Spinner classSpinner, sectionSpinner;

        private String[] sections = new String[]{"Pascal", "Einstein", "Galileo", "Faraday", "Euclid", "Maxwell", "Curie"};

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        public String[] giveMeStringArrayFromJSONObject(JSONObject object, String[] placeholder) throws JSONException {
            String[] values = new String[placeholder.length];
            for (int i = 0; i < placeholder.length; i++) {
                values[i] = object.get(placeholder[i]).toString();
            }
            return values;
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
            final View v = inflater.inflate(R.layout.load_routine, null, false);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
            sectionSpinner = (Spinner) v.findViewById(R.id.section_spinner);
            String link = NEW_HOST + "SectionName.php";
            new PhpConnect(link, "", getActivity(), 0, new String[]{CMDXXX, Utilities.getDatabaseName(getActivity())}, new String[]{"cmdxxx", "dbName"}).setListener(new PhpConnect.ConnectOnClickListener() {
                @Override
                public void onConnectListener(String res) {
                    try {
                        JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                        sections = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject job = jsonArray.getJSONObject(i);
                            sections[i] = job.getString("sectionn");

                        }
                        ((LinearLayout) v.findViewById(R.id.loadingSection)).setVisibility(View.INVISIBLE);
                        List<String> _class = new ArrayList<>(Arrays.asList(sections));
                        Collections.sort(_class, String.CASE_INSENSITIVE_ORDER);
                        sections = _class.toArray(new String[sections.length]);
                        sectionSpinner.setAdapter(
                                new CustomSpinnerAdapter(
                                        getActivity(), R.id.section_spinner, sections
                                )
                        );
                    } catch (Exception e) {
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            okButton = (Button) v.findViewById(R.id.load_routine_button);
            final SharedPreferences sp = getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
            okButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            str = sectionSpinner.getSelectedItem().toString();
                            String link = NEW_HOST + "GetRoutine.php";
                            new PhpConnect(link, "loading", getActivity(), 1, new String[]{CMDXXX, databaseName, str}, new String[]{"cmdxxx", "dbName", "section"}).setListener(new PhpConnect.ConnectOnClickListener() {
                                @Override
                                public void onConnectListener(String res) {

                                    try {
                                        JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jO = jsonArray.getJSONObject(i);
                                            try {
                                                saveRoutine(giveMeStringArrayFromJSONObject(
                                                        jO,
                                                        new String[]{"1st_period",
                                                                "2nd_period",
                                                                "3rd_period",
                                                                "4th_period",
                                                                "5th_period",
                                                                "6th_period",
                                                                "7th_period",
                                                                "8th_period",
                                                        }
                                                ), jO.get("days").toString(), getActivity());
                                                //     getFragmentManager().popBackStack();
                                                //   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new FragmentForHome()).addToBackStack(null).commit();
                                            } catch (Exception w) {
                                                //Log.d(" ViewRoutineFragment", w.toString());
                                                // Toast.makeText(getActivity(), w.toString(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
//setRoutine();
                                    } catch (Exception e) {
                                        //Log.d(" ViewRoutineFragment", e.toString());
                                        // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                                    }

                                    sp.edit().putBoolean(HASSETROUTINE, true).apply();
                                    sp.edit().putString(SECTION, str).apply();
                                }
                            });
                        }
                    });
            return v;
        }


        private void saveRoutine(String[] periods, String day, Context x) {
            try {
                db.delete(TABLENAME, C_DAY + "=?", new String[]{day});
                ContentValues cv = new ContentValues();
                cv.put(C_FIRST, periods[0]);
                cv.put(C_SECOND, periods[1]);
                cv.put(C_THIRD, periods[2]);
                cv.put(C_FOURTH, periods[3]);
                cv.put(C_FIFTH, periods[4]);
                cv.put(C_SIXTH, periods[5]);
                cv.put(C_SEVENTH, periods[6]);
                cv.put(C_EIGHTH, periods[7]);
                cv.put(C_DAY, day);
                cv.put(C_SECTION, str);
                db.insert(TABLENAME, "null", cv);
                //Toast.makeText(x, day, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }

        private class adapt extends ArrayAdapter<String> {
            public adapt(ArrayList<String> c) {
                super(getActivity(), 0, c);
            }

            @Override
            public View getView(int pos, View v, ViewGroup vg) {
                if (v == null) {
                    v = getActivity().getLayoutInflater().inflate(R.layout.text_view_for_section_list, null);
                }
                try {
                    String section = getItem(pos);
                    TextView tv = (TextView) v.findViewById(R.id.section);
                    tv.setText(section);
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                return v;
            }

        }
    }

}
