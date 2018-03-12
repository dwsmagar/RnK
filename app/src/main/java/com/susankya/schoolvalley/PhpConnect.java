package com.susankya.schoolvalley;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class PhpConnect {
    ConnectOnClickListener mListener;
    private String dialogText, link;
    private Context activity;
    private ProgressDialog dialog;
    private boolean isCancellable;
    private int showDialog;
    private String getOrPost = "GET";
    private String[] postParameters, postPlaceholders;
    private RequestQueue requestQueue;

    public PhpConnect(String link, String getDialogText, Context act, int showDialog) {
        this.link = link;
        dialogText = getDialogText;
        activity = act;
        isCancellable = false;
        this.showDialog = showDialog;
        dialog = new ProgressDialog(activity);
        dialog.setCancelable(false);
    }

    public PhpConnect(String link, String getDialogText, Context act, int showDialog, String[] parameters, String[] placeholders) {
        this.link = Utilities.encodeLinkSpace(link);
        dialogText = getDialogText;
        activity = act;
        this.showDialog = showDialog;
        dialog = new ProgressDialog(activity);
        dialog.setCancelable(false);
        getOrPost = "POST";
        isCancellable = false;
        postParameters = parameters;
        postPlaceholders = placeholders;
    }

    public PhpConnect(String link, String getDialogText, Context act, int showDialog, boolean cancellable, String[] parameters, String[] placeholders) {
        this.link = Utilities.encodeLinkSpace(link);
        dialogText = getDialogText;
        activity = act;
        this.showDialog = showDialog;
        dialog = new ProgressDialog(activity);
        dialog.setCancelable(false);
        getOrPost = "POST";
        isCancellable = cancellable;
        postParameters = parameters;
        postPlaceholders = placeholders;
    }

    interface ConnectOnClickListener {
        void onConnectListener(String res);
    }

    public void setListener(ConnectOnClickListener listener) {
        try {
            mListener = listener;
            if (isConnectionAvailable()) // execute();
            {
//                RequestQueue rq = Volley.newRequestQueue(activity, new HurlStack(null, getSocketFactory()));
                StringRequest postRequest = new StringRequest(Request.Method.POST, link,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    response = response.trim().replaceAll("ï»¿", "");
                                    Log.d("fromPost", response);

                                    if (showDialog == 1)
                                        dialog.dismiss();
                                    if (activity != null) {
                                        mListener.onConnectListener(response);
                                    }

                                } catch (Throwable t) {
                                    Log.d("fromPost", "throwable"+t.toString());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (showDialog == 1)
                                    dialog.dismiss();
                                try {
                                    if (activity != null)
                                        mListener.onConnectListener(error.toString());
                                } catch (Throwable t) {

                                }
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        // the POST parameters:
                        for (int i = 0; i < postPlaceholders.length; i++) {
                            params.put(postPlaceholders[i], postParameters[i]);
                            Log.d("frompost","Map"+ postPlaceholders[i] + " " + postParameters[i]);
                        }
                        return params;
                    }
                };
//               rq.add(postRequest);
                Volley.newRequestQueue(activity).add(postRequest);
                dialog = new ProgressDialog(activity);
                dialog.setMessage(dialogText);
                dialog.setCancelable(isCancellable);
                if (showDialog == 1)
                    dialog.show();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setMessage("No Internet connection");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            // new AlertDialogBuilder("No Internet connection","Please enable Mobile Data/Wi-Fi in order to log in.","OK","",activity);
        } catch (Throwable t) {
            Log.d("phpconnect", t.toString());
        }
    }

    private boolean isConnectionAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        else return false;
    }

//    private SSLSocketFactory getSocketFactory() {
//
//        CertificateFactory cf = null;
//        try {
//            cf = CertificateFactory.getInstance("X.509");
////            InputStream caInput = activity.getResources().openRawResource(R.raw.server);
//            Certificate ca;
//            try {
//                ca = cf.generateCertificate(caInput);
//                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
//            } finally {
//                caInput.close();
//            }
//
//
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//
//            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//
//                    Log.e("CipherUsed", session.getCipherSuite());
//                    return hostname.compareTo("192.168.1.10")==0; //The Hostname of your server
//
//                }
//            };
//
//
//            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//            SSLContext context = null;
//            context = SSLContext.getInstance("TLS");
//
//            context.init(null, tmf.getTrustManagers(), null);
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//
//            SSLSocketFactory sf = context.getSocketFactory();
//
//
//            return sf;
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//
//        return  null;
//    }
}
