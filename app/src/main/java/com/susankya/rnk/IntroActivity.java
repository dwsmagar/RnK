package com.susankya.rnk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class IntroActivity extends AppCompatActivity implements FragmentCodes {

    private static final String SAVING_STATE_SLIDER_ANIMATION = "SliderAnimationSavingState";
    private boolean isSliderAnimation = false;
    private static SQLiteDatabase db;
    Button next, skip;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_intro);
        sqLiteHelper = new SQLiteHelper(IntroActivity.this);
        db = sqLiteHelper.getWritableDatabase();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        if (getSharedPreferences(NewSettingsFragment.LOGGED_OUt, MODE_PRIVATE).getBoolean(NewSettingsFragment.HASLOGGED_OUt, false)) {
            startActivity(new Intent(IntroActivity.this, StudentInActivity.class));
            IntroActivity.this.finish();
        }
        skip = (Button) findViewById(R.id.skipPager);
        skip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(IntroActivity.this, StudentInActivity.class));
                        IntroActivity.this.finish();
                    }
                }
        );
        next = (Button) findViewById(R.id.nextPager);
        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
                            startActivity(new Intent(IntroActivity.this, StudentInActivity.class));
                            IntroActivity.this.finish();
                        }
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                }
        );
        viewPager.setAdapter(new ViewPagerAdapter(R.array.icons, R.array.titles, R.array.hints));
        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        viewPager.setPageTransformer(true, new CustomPageTransformer());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

                View landingBGView = findViewById(R.id.landing_backgrond);
                int colorBg[] = getResources().getIntArray(R.array.landing_bg);
                ColorShades shades = new ColorShades();
                shades.setFromColor(colorBg[position % colorBg.length])
                        .setToColor(colorBg[(position + 1) % colorBg.length])
                        .setShade(positionOffset);

                landingBGView.setBackgroundColor(shades.generate());
            }

            public void onPageSelected(int position) {

                if (position == viewPager.getAdapter().getCount() - 1)
                    next.setText("START");
                else
                    next.setText("➔");
            }

            public void onPageScrollStateChanged(int state) {
            }
        });

        try {
                /* String DAY="day";
    String PERIOD_NO="period_no";
    String SUBJECT="subject";
    String SECTION_NAME="section_name";
    String TEACHER_NAME="teacher_name";
    String START="start";
    String END="END";
    String COLLEGE_SN="college_sn";*/
            db.execSQL("create table if not exists " + TABLENAME + " (" + DAY + " varchar(50), " + PERIOD_NO + " varchar(50), " + SUBJECT + " varchar(50), " + SECTION_NAME + " varchar(50), " +
                    TEACHER_NAME + " varchar(50), " + START + " varchar(50), " + END + " varchar(50), " + COLLEGE_SN + " varchar(50)); ");
            SharedPreferences sp = this.getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
            sp.edit().putBoolean(IS_DATABASE_CREATED, true).commit();
        } catch (Exception e) {
            //Log.d("errorxa",e.toString());
            Toast.makeText(IntroActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public class ViewPagerAdapter extends PagerAdapter {

        private int iconResId, titleArrayResId, hintArrayResId;

        public ViewPagerAdapter(int iconResId, int titleArrayResId, int hintArrayResId) {

            this.iconResId = iconResId;
            this.titleArrayResId = titleArrayResId;
            this.hintArrayResId = hintArrayResId;
        }

        @Override
        public int getCount() {
            return getResources().getIntArray(iconResId).length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
            String title = getResources().getStringArray(titleArrayResId)[position];
            String hint = getResources().getStringArray(hintArrayResId)[position];
            //   int coloredIcon=getResources().getIntArray(R.array.icons)[position];


            View itemView = getLayoutInflater().inflate(R.layout.viewpager_item, container, false);
            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView titleView = (TextView) itemView.findViewById(R.id.landing_txt_title);
            TextView hintView = (TextView) itemView.findViewById(R.id.landing_txt_hint);

//            BitmapDrawable bd = Utilities.setIconColor(IntroActivity.this, icon, Color.parseColor("#ffffff"));
            if (position != 0)
                iconView.setImageDrawable(icon);
            else iconView.setImageDrawable(icon);
            titleView.setText(title);
            hintView.setText(hint);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }

    public class CustomPageTransformer implements ViewPager.PageTransformer {


        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            View imageView = view.findViewById(R.id.landing_img_slide);
            View contentView = view.findViewById(R.id.landing_txt_hint);
            View txt_title = view.findViewById(R.id.landing_txt_title);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left
            } else if (position <= 0) { // [-1,0]
                // This page is moving out to the left

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 + position);
                    setAlpha(txt_title, 1 + position);
                }

                if (imageView != null) {
                    // Fade the image in
                    setAlpha(imageView, 1 + position);
                }

            } else if (position <= 1) { // (0,1]
                // This page is moving in from the right

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 - position);
                    setAlpha(txt_title, 1 - position);

                }
                if (imageView != null) {
                    // Fade the image out
                    setAlpha(imageView, 1 - position);
                }

            }
        }
    }

    /**
     * Sets the alpha for the view. The alpha will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view  - view to which alpha to be applied.
     * @param alpha - alpha value.
     */
    private void setAlpha(View view, float alpha) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setAlpha(alpha);
        }
    }

    /**
     * Sets the translationX for the view. The translation value will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view         - view to which alpha to be applied.
     * @param translationX - translationX value.
     */
    private void setTranslationX(View view, float translationX) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setTranslationX(translationX);
        }
    }

    public void onSaveInstanceState(Bundle outstate) {

        if (outstate != null) {
            outstate.putBoolean(SAVING_STATE_SLIDER_ANIMATION, isSliderAnimation);
        }

        super.onSaveInstanceState(outstate);
    }

    public void onRestoreInstanceState(Bundle inState) {

        if (inState != null) {
            isSliderAnimation = inState.getBoolean(SAVING_STATE_SLIDER_ANIMATION, false);
        }
        super.onRestoreInstanceState(inState);

    }
}
