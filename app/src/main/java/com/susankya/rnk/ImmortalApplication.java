package com.susankya.rnk;

import android.support.multidex.MultiDexApplication;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImmortalApplication extends MultiDexApplication {
    private static ImmortalApplication abc;
    public static String getTheSN() {
        return theSN;
    }
    public static void setTheSN(String theSN) {
        ImmortalApplication.theSN = theSN;
    }
    public static String theSN;
    public static String beforeLoginSN, bLSName, bLdbName;
    public static notice clickedNotice;
    public static String category;
    public ArrayList<GalleryItem> galleryItemArrayList;
    private static String smsUrl = "http://api.sparrowsms.com/v2/";
    private static String nimaUrl = "https://nimas.susankya.com/api/";

    public ImmortalApplication getInstance() {
        return abc;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        abc = this;

        // initializing iconify
        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new TypiconsModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule())
                .with(new MeteoconsModule())
                .with(new WeathericonsModule())
                .with(new SimpleLineIconsModule())
                .with(new IoniconsModule());
    }

    public void set(String SN, String sName, String dbName, String category) {
        beforeLoginSN = SN;
        bLSName = sName;
        bLdbName = dbName;
        ImmortalApplication.category = category;
    }

    public static Retrofit smsRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(smsUrl)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(nimaUrl)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }
}

