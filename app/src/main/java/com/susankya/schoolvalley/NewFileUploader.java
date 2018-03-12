package com.susankya.schoolvalley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ajay on 9/6/2015.
 */
public class NewFileUploader {

    private Uri uri;
    private Activity activity;
    public final static int POST_IMAGE=0;
    public final static int COMMENT_IMAGE=1;
    long totalSize=0;
    private String filePath="";
    private String[] filePatharray=null;
    private FileUploadedListener mListener;
  private  String placeHolderforfile="file";
    private String postNumber,urlToCall;

    private String[] placeholder,values;
    private ProgressDialog dialog;
    File sourceFile;

    public NewFileUploader(String filePath,String holderforFile,String url,String[] placehlder,String[] values,Activity act)
    {
        activity=act;
        this.uri=uri;
        this.placeHolderforfile=holderforFile;
        this.placeholder=placehlder;
        this.values=values;
        this.urlToCall=url;
        this.filePath=filePath;
        sourceFile = new File(filePath);
    }
    public NewFileUploader(String[] filePatharray,String holderforFile,String url,String[] placehlder,String[] values,Activity act)
    {
        activity=act;
        this.uri=uri;
        this.placeholder=placehlder;
        this.values=values;
        this.urlToCall=url;
        this.placeHolderforfile=holderforFile;
        this.filePatharray=filePatharray;
        sourceFile = new File(filePath);
    }
    public interface FileUploadedListener
    {
        void OnFileUploaded(String result);
    }

    public void setFileUploadedListener(FileUploadedListener listener)
    {
        mListener=listener;
        if(Utilities.isConnectionAvailable(activity))
        {
            dialog = new ProgressDialog(activity);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            dialog.setCancelable(false);
            new UploadFile().execute();
        }

    }

    private class UploadFile extends AsyncTask<Void,Integer,String>
    {
        @Override
        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        protected void onPreExecute()
        {
            if((sourceFile.length()/1024)>500)
            {
                Toast.makeText(activity,"Can not upload a file of size more than 500 KB.",Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.show();

        }
        private String upload() {
            String responseString = "no";

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlToCall);


            try {
                CustomMultiPartEntity entity=new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
for(int i=0;i<placeholder.length;i++){
    entity.addPart(placeholder[i], new StringBody(values[i]));
}
                try {
                    if (filePatharray == null) {
                        File file=new File(filePath);

                        if((file.length()/1024)>500){
                            Toast.makeText(activity,"Can not upload a file of size more than 500 KB.",Toast.LENGTH_SHORT).show();
                            return 0+"";
                            //continue;
                        }
                        entity.addPart(placeHolderforfile, new FileBody(new File(filePath)));
                    } else {
                        for (int i = 0; i < filePatharray.length; i++) {
                            File file=new File(filePatharray[i]);

                            if((file.length()/1024)>500){
                                Toast.makeText(activity,"Can not upload a file of size more than 500 KB.",Toast.LENGTH_SHORT).show();
                                return 0+"";
                                //continue;
                            }
                            entity.addPart(placeHolderforfile + "[" + i + "]", new FileBody(new File(filePatharray[i])));
                        }
                    }
                }
            catch (Exception e){
                //Log.d("TAG", "upload: "+e.toString());
            }
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                responseString = EntityUtils.toString(r_entity);

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        protected String doInBackground(Void... params)
        {
 return upload();
        } // End else block

        protected void onPostExecute(String res)
        {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            mListener.OnFileUploaded(res);
        }

    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}

