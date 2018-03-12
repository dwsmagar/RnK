package com.susankya.schoolvalley;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
public class Utilities {
    private static String USERINFO = "userinfo";

    public static String getPassword(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "null");
    }

    public static void save(Context context, String array, String mFilename)
    // throws JSONException, IOException
    {
        Writer writer = null;
        try {
            OutputStream out = context.getApplicationContext()
                    .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } catch (Exception e) {

        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception e) {

                }
        }
    }

    public static String load(Context context, String mFilename)// throws IOException, JSONException
    {
        String line = null;
        ArrayList<String> crimes = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            InputStream in = context.getApplicationContext().openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            line = jsonString.toString();

        } catch (Exception e) {

        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e) {

                }
        }
        return line;
    }


    public static String getDatabaseName(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        return sharedPreferences.getString("dbName", "null");
    }

    public static String getLocation(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        return sharedPreferences.getString("location", "null");
    }

    public static String getSN(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        return sharedPreferences.getString("sn", "null");
    }

    public static String getCoverPicURL(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        return (FragmentCodes.MAIN_DATABASE + sharedPreferences.getString("cover_pic", "null"));
    }

    public static String getRollNum(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        return (FragmentCodes.MAIN_DATABASE + sharedPreferences.getString("roll", "null"));
    }

    public static String getSection(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        return (FragmentCodes.MAIN_DATABASE + sharedPreferences.getString("section", "null"));
    }

    public static String getCollegeName(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        return (FragmentCodes.MAIN_DATABASE + sharedPreferences.getString("institution", "null"));
    }

    public static void setIsAdmin(Context context, boolean bool) {
        context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE).edit().putBoolean(FragmentCodes.IS_ADMIN, bool).commit();
    }

    public boolean iSadmin(Context context) {

        return context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE).getBoolean(FragmentCodes.IS_ADMIN, false);
    }

    public static void setDatabaseName(Context context, String db) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dbName", db);
        editor.commit();
        SharedPreferences sp = context.getSharedPreferences("dbName", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("dbName", db);

        e.commit();
    }

    public static BitmapDrawable setIconColor(Context c, Drawable d, int color) {
        if (color == 0) {
            color = 0xffffffff;
        }

        final Resources res = c.getResources();
        Drawable maskDrawable = d;
        if (!(maskDrawable instanceof BitmapDrawable)) {
            return null;
        }

        Bitmap maskBitmap = ((BitmapDrawable) maskDrawable).getBitmap();
        final int width = maskBitmap.getWidth();
        final int height = maskBitmap.getHeight();

        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawBitmap(maskBitmap, 0, 0, null);

        Paint maskedPaint = new Paint();
        maskedPaint.setColor(color);
        maskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        canvas.drawRect(0, 0, width, height, maskedPaint);

        //return outBitmap;
        BitmapDrawable outDrawable = new BitmapDrawable(res, outBitmap);
        return outDrawable;
    }

    public static void setProfilePicThumbnailLoaded(Context c, boolean val) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("pptloaded", val);
        editor.commit();
    }

    public static boolean getProfilePicThumbnailLoaded(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("pptloaded", false);
    }

    public static boolean isStudent(Context c) {
        SharedPreferences loginTypeSP = c.getApplicationContext().getSharedPreferences("type", Context.MODE_PRIVATE);
        if (loginTypeSP.getInt("type", 2) == FragmentCodes.STUDENT)
            return true;
        else return false;
    }

    public static boolean isAdmin(Context c) {
        SharedPreferences loginTypeSP = c.getApplicationContext().getSharedPreferences("type", Context.MODE_PRIVATE);
        if (loginTypeSP.getInt("type", 2) == FragmentCodes.ADMIN)
            return true;
        else return false;
    }

    public static void saveImage(String filename, Bitmap bmp) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String giveMePath(Context c) {
        ContextWrapper cw = new ContextWrapper(c.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        return directory.getAbsolutePath();
    }

    public static void saveToInternalStorage(Context c, Bitmap bitmapImage, String filename) {
        ContextWrapper cw = new ContextWrapper(c.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {

            }
        }
    }

    public static Bitmap loadImageFromStorage(Context c, String file) {

        Bitmap b = null;
        try {
            File f = new File(giveMePath(c), file);
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;

    }

    public static UrlEncodedFormEntity getPostParameters(String parameters[], String placeholders[]) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            nameValuePairs.add(new BasicNameValuePair(placeholders[i], parameters[i]));
        }
        return new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
    }

    public static boolean isValidString(String string) {
        return ((string != null) && !string.isEmpty());
    }

    public static void noInternetDialog(Activity activity) {
        new AlertDialogBuilder("No Internet connection", "Please enable Mobile Data/Wi-Fi in order to log in.", "OK", "", activity);
    }

    public static TimexTimer parseTimer(int millis) {
        int mins, secs;
        int hours = millis / (60 * 60 * 1000);
        int remainingmillis = millis - (hours * 60 * 60 * 1000);
        mins = remainingmillis / (60 * 1000);
        int remain2 = remainingmillis - (mins * 60 * 1000);
        secs = remain2 / 1000;
        //Log.d("hour",hours+"");
        //Log.d("mins",mins+"");
        //Log.d("secs",secs+"");
        return new TimexTimer(hours, mins, secs);

    }

    public static boolean isShiftMorning(String section, Activity activity) {
        String[] dayShift = activity.getResources().getStringArray(R.array.day);
        for (String s : dayShift) {
            if (s.equals(section))
                return false;
        }
        return true;
    }

    public static void toaster(String toastMessage, int length, Activity act) {
        //Toast.makeText(act,toastMessage,length).show();
    }

    public static boolean isConnectionAvailable(Activity activity) {
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        else return false;
    }

    public static int getBlocked(Context c) {
        final SharedPreferences sp = c.getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        return sp.getInt("blocked", 0);
    }

    public static void likeUnlike(Button like, Posts n, Activity activity) {

        //Toast.makeText(activity,n.getIsLiked()+"",Toast.LENGTH_LONG).show();
        if (n.getIsLiked() == 0) {
            n.setIsLiked(1);
            n.setLike(n.getLike() + 1);
            like.setText("Unlike");

        } else {
            n.setIsLiked(0);
            n.setLike(n.getLike() - 1);
            like.setText("Like");
        }
    }

    public static String encodeLinkSpace(String link) {
        return link.replace(" ", "%20");
    }

    public static String makeUrl(String link) {
        return link.replace(" ", "%20");
    }

    public static void likePost(final Posts n, final Button like, final Activity activity, final TextView tv) {

        String encodedDate = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        String current_date = d1.format(cal.getTime());
        //Log.d("date in comment",current_date);
        try {
            encodedDate = URLEncoder.encode(current_date, "utf-8");
        } catch (Exception e) {

        }

        String liked = Integer.toString(n.getIsLiked());
        String postNumber = Integer.toString(n.getNewsNo());
        String userNum = Integer.toString(new UserInfo().getUserInfoFromSharedPreferences(activity).getUserNumber());
        String likeLink = FragmentCodes.CHHALFAL + "Like/like.php";//?liked="+liked+"&postNumber="+postNumber+"&userNumber="+userNum+"&date="+encodedDate;


        likeUnlike(like, n, activity);
        singularOrPlural(tv, n.getLike(), "like", "likes");

        new PhpConnect(encodeLinkSpace(likeLink), "", activity, 0,
                new String[]{FragmentCodes.CMDXXX, liked, postNumber, userNum},
                new String[]{"cmdxxx", "liked", "postNumber", "userNumber"}).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        //Log.d("got from like.php", res);
                        if (!res.trim().equals("1")) {

                            likeUnlike(like, n, activity);
                            singularOrPlural(tv, n.getLike(), "like", "likes");

                        } else if (res.trim().equals("1")) {


                        }
                    }
                }
        );
    }

    public static void singularOrPlural(TextView tv, int count, String singular, String plural) {
        if (count == 1) {
            tv.setText(count + "");//+" "+singular);
        } else {
            tv.setText(count + "");//" "+plural);
        }
    }

    public static String[] getLevels(Context c) {
        SharedPreferences sp = c.getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
        if (sp.getBoolean("hasGotLevel", false)) {
            Set<String> levels = sp.getStringSet("levels", null);
            return levels.toArray(new String[levels.size()]);
        } else
            return new String[]{};
    }

    public static void saveUserInfo(String username, String fullname, int userNumber, String level, String institution, String gender, Activity activity) {
        Writer writer = null;
        try {
            OutputStream out = activity.getApplicationContext()
                    .openFileOutput("userinf", Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("fullname", fullname);
            jsonObject.put("usernumber", userNumber);
            jsonObject.put("level", level);
            jsonObject.put("institution", institution);
            jsonObject.put("gender", gender);
            writer.write(jsonObject.toString());
        } catch (Exception e) {
            //Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception e) {
                    //Toast.makeText(activity,"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public static void updateUISharedPreferences(Context c) {
        try {
            JSONArray jsonArray = (JSONArray) new JSONTokener(UtilitiesAdi.loadProfileJSON(c)).nextValue();
            JSONObjectAuto jO = new JSONObjectAuto(jsonArray.getJSONObject(0));
            String userNumber = jO.getString("user_no");
            String firstName = jO.getString("firstName");
            String lastName = jO.getString("lastName");
            String userid = jO.getString("userid");
            String verified = jO.getString("verified");
            String totalPosts = jO.getString("totalPosts");
            String totalAnswers = jO.getString("totalAnswers");
            String bestAnswers = jO.getString("bestAnswers");
            String institution = jO.getString("institution");
            String level = jO.getString("level");
            String dbName = jO.getString("dbName");
            String genderPHP = jO.getString("gender");
            String profilepic = jO.getString("profile_picture_location");
            String phoneNum = jO.getString("phone_number");
            String location = jO.getString("location");
            String interest = jO.getString("interest");
            String hobby = jO.getString("hobby");
            String thumbnail = jO.getString("thumbmailLocation");
            String SeePostsFrom = jO.getString("SeePostsFrom");
            String gender = "";
            if (genderPHP.equals("m")) gender = "Male";
            else gender = "Female";

            UserInfo userInfo = new UserInfo();
            userInfo.setFirstName(firstName);
            userInfo.setLocation(location);
            userInfo.setLevel(level);
            userInfo.setLastName(lastName);
            userInfo.setUserName(userid);
            userInfo.setSeePostsFrom(SeePostsFrom);
            userInfo.setVerified(Integer.parseInt(verified));
            userInfo.setGender(gender);
            userInfo.setInstitution(institution);


            userInfo.setFullName(firstName + " " + lastName);
            userInfo.setDbName(dbName);
            userInfo.setPhoneNumber(phoneNum);
            userInfo.setInterest(interest);
            userInfo.setHobby(hobby);

            userInfo.setUserNumber(Integer.parseInt(userNumber));

            SharedPreferences sharedPreferences = c.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("firstName", userInfo.getFirstName());
            editor.putString("lastName", userInfo.getLastName());
            editor.putString("level", userInfo.getLevel());
            editor.putString("institution", userInfo.getInstitution());
            editor.putString("gender", userInfo.getGender());
            editor.putString("fullName", userInfo.getFullName());
            editor.putInt("number", userInfo.getUserNumber());
            editor.putString("username", userInfo.getUserName());
            editor.putInt("verified", userInfo.getVerified());
            editor.putString("hobby", userInfo.getHobby());
            editor.putString("intrest", userInfo.getInterest());
            editor.putString("location", userInfo.getLocation());
            editor.putString("profile_pic_location", userInfo.getProfilepiclocation());
            editor.putString("phone_number", userInfo.getPhoneNumber());
            editor.putString("dbName", userInfo.getDbName());
            editor.commit();

            SharedPreferences sp = c.getSharedPreferences("dbName", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putString("dbName", userInfo.getDbName());
            if (userInfo.getDbName().equals("none"))
                e.putBoolean("collegeValid", false);
            else
                e.putBoolean("collegeValid", true);
            e.commit();
        } catch (Exception e) {

        }
    }

    public static UserInfo loadUserInfo(Activity activity) {

        UserInfo userInfo = new UserInfo();
        JSONArray array = new JSONArray();
        String line = null;
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = activity.getApplicationContext().openFileInput("userinf");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            String username = jsonObject.getString("username");
            String fullname = jsonObject.getString("fullname");
            int usernumber = jsonObject.getInt("usernumber");
            userInfo = new UserInfo(usernumber, fullname);
            userInfo.setUserName(username);

        } catch (Exception e) {
            //Toast.makeText(activity, "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e) {
                    //Toast.makeText(activity, "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return userInfo;
    }

    public static int getUserNumber(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("number", 0);
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    public static String getInstitution(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("institution", "null");
    }

    public static String getGender(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("gender", "null");
    }

    public static String getFullname(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("fullName", "");
    }

    public static ArrayList<question> questionsArrayMaker(JSONArray array) {
        ArrayList<question> questions = new ArrayList<question>();

        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject jO = array.getJSONObject(i);
                question n = new question();
                String nothing = jO.getString("question_code");
                String questionnum = Integer.toString(jO.getInt("question_number"));
                n.setQuestion(jO.getString("question"));
                n.setQuestionNumber(jO.getInt("question_number"));
                n.setOption1(jO.getString("option1"));
                n.setOption2(jO.getString("option2"));
                n.setOption3(jO.getString("option3"));
                n.setOption4(jO.getString("option4"));
                n.setCorrect_answer(jO.getInt("correct_option"));
                questions.add(n);
            }
        } catch (Exception e) {
            //Log.d("here error Utilities",e.toString());
        }
        return questions;

    }

    public static void fileWriter(JSONArray jsonArray, String CODE, Activity activity) {
        Writer writer = null;
        try {
            if (doesFileExist(CODE)) {
                return;
            }
            OutputStream out = activity.getApplicationContext()
                    .openFileOutput(CODE, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());

        } catch (Exception e) {
            //Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception e) {
                    // Toast.makeText(activity,"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }

    }

    public static boolean doesFileExist(String fileName) {
        return new File(fileName).exists();
    }

    public static JSONArray fileLoader(String mFilename, Activity activity)// throws IOException, JSONException
    {
        JSONArray array = new JSONArray();
        String line = null;
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = activity.getApplicationContext().openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

        } catch (Exception e) {
            //Toast.makeText(activity, "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e) {
                    //Toast.makeText(activity, "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return array;
    }
}
