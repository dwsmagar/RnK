package com.susankya.rnk.Fragments;

import com.susankya.rnk.Models.AppliedUser;
import com.susankya.rnk.Models.EventItem;
import com.susankya.rnk.Sms;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface Nimainterface {

    @FormUrlEncoded
    @POST("sms/")
    Call<Sms> sendSms(@Field("token") String token, @Field("from") String from, @Field("to") String to, @Field("text") String text);

    @GET("credit/")
    Call<Sms> creditLeft(@Query("token") String token);

    @GET("events/")
    Call<List<EventItem>> getEvents();

    @Multipart
    @POST("events/")
    Call<ResponseBody> postEvent(@Part("name") RequestBody name, @Part("description") RequestBody description,
                                 @Part("price") RequestBody price, @Part("organized_by") RequestBody organized_by,
                                 @Part("date") RequestBody date, @Part("time") RequestBody time, @Part MultipartBody.Part picture,
                                 @Part("location") RequestBody location);

    @Headers("Content-Type: application/json")
    @POST("appointments/")
    Call<ResponseBody> postAppointment(@Body Appointment appointment);

    @GET("appointments/")
    Call<List<Appointment>> getAppointments();

    @Headers("Content-Type: application/json")
    @POST("eligibility/")
    Call<ResponseBody> postEligibility(@Body Eligibility eligibility);

    @GET("eligibility/")
    Call<List<Eligibility>> getEligibilityList();

    @Multipart
    @POST("appliedusers/")
    Call<ResponseBody> postAppliedUsers(@PartMap Map<String, RequestBody> map,
                                        @Part MultipartBody.Part photograph,
                                        @Part MultipartBody.Part resume,
                                        @Part MultipartBody.Part passport_copy,
                                        @Part MultipartBody.Part citizenship_copy,
                                        @Part MultipartBody.Part school_education_certificate,
                                        @Part MultipartBody.Part high_school_education_certificate,
                                        @Part MultipartBody.Part undergrad_certificate,
                                        @Part MultipartBody.Part graduate_certificate,
                                        @Part MultipartBody.Part test
    );

    @GET("appliedusers/")
    Call<List<AppliedUser>> getAppliedUsers();
}
