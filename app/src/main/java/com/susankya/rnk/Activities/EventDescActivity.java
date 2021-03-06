package com.susankya.rnk.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.susankya.rnk.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDescActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.eventName)
    TextView event_name;
    @BindView(R.id.date)
    TextView event_date;
    @BindView(R.id.eventTime)
    TextView event_time;
    @BindView(R.id.eventLocation)
    TextView event_location;
    @BindView(R.id.eventDesc)
    TextView event_desc;
    @BindView(R.id.eventPrice)
    TextView event_price;
    @BindView(R.id.eventDay)
    TextView event_day;
    @BindView(R.id.eventMonth)
    TextView event_month;
    @BindView(R.id.organizer)
    TextView organizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_desc);
        ButterKnife.bind(this);
        toolbar.setTitle("Event Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(UtilitiesAdi.catColor(n.getCategory(), this.getResources()));
        }
        getDetail();
    }
    private void getDetail() {
        Bundle details = getIntent().getExtras();
        String[] splittedDate = null;
        String unsplittedDate = formatDate(details.getString("date"));
        try {
            splittedDate = unsplittedDate.split(" ");
        } catch (Exception e) {
        }
        organizer.setText(details.getString("organized_by"));
        try {
            event_day.setText(splittedDate[2]);
        } catch (Exception e) {
        }
        try {
            event_month.setText(splittedDate[1]);
        } catch (Exception e) {
        }
        event_name.setText(details.getString("name"));
        event_date.setText(details.getString("date"));
        event_time.setText(details.getString("time"));
        event_location.setText(details.getString("location"));
        event_desc.setText(details.getString("description"));
        if (Integer.parseInt(details.getString("price")) == 0)
            event_price.setText(details.getString("price") + " " + "Free");
        else
            event_price.setText(details.getString("price"));
        if (details.getString("imageUrl") == null) {
            Picasso.with(getApplicationContext()).load(R.drawable.ic_gallery).into(imageView);
        } else {
            Picasso.with(getApplicationContext()).load(details.getString("imageUrl")).error(R.drawable.ic_warning).fit().into(imageView);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private String formatDate(String dateStr) {
        //Toast.makeText(context, ""+dateStr, Toast.LENGTH_SHORT).show();
        Date MyDate = null;
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        try {
            if(dateStr.contains("-"))
                MyDate = newDateFormat.parse(dateStr);
            else
                MyDate = newDateFormat1.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newDateFormat.applyPattern("E MMM dd yyyy");
        String obtainedDate = null;
        try {
            obtainedDate = newDateFormat.format(MyDate);
        } catch (Exception e) {
        }
        return obtainedDate;
    }

    private String timeFormatter(String inputTime) {
        String outputTime = null;
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        try {
            Date _24HourDt = _24HourSDF.parse(inputTime);
            outputTime = _12HourSDF.format(_24HourDt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTime;
    }
}
